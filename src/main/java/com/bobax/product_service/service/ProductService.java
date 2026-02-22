package com.bobax.product_service.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bobax.product_service.exception.RecursoNoEncontradoException;
import com.bobax.product_service.model.Categoria;
import com.bobax.product_service.model.Producto;
import com.bobax.product_service.repository.CategoriaRepository;
import com.bobax.product_service.repository.ProductoRepository;
import com.bobax.product_service.util.SlugUtil;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductService {

    private final ProductoRepository repo;
    private final CategoriaRepository categoriaRepo;

    @Value("${app.uploads.dir:uploads}")
    private String uploadsDir;

    public ProductService(ProductoRepository repo, CategoriaRepository categoriaRepo) {
        this.repo = repo;
        this.categoriaRepo = categoriaRepo;
    }

    @Transactional(readOnly = true)
    public List<Producto> listar() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public List<Producto> listarPorCategoria(Long categoriaId) {
        return repo.findByCategoriaIdAndHabilitadoTrue(categoriaId);
    }

    @Transactional(readOnly = true)
    public List<Producto> listarPorCategoriaHabilitados(Categoria categoria) {
        return repo.findByCategoriaAndHabilitadoTrue(categoria);
    }

    @Transactional(readOnly = true)
    public Optional<Producto> buscarPorId(Long id) {
        return repo.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Producto> buscarPorRuta(String ruta) {
        return repo.findByRuta(ruta);
    }

    public Producto crear(Producto producto, Long categoriaId) {

        Categoria cat = categoriaRepo.findById(categoriaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Categoría no encontrada"));

        if (producto.getSku() != null && producto.getSku().isBlank()) {
            producto.setSku(null);
        }

        if (producto.getRuta() == null || producto.getRuta().isBlank()) {
            producto.setRuta(slugUnico(producto.getNombre()));
        } else {
            producto.setRuta(SlugUtil.slug(producto.getRuta()));
        }

        if (producto.getHabilitado() == null)
            producto.setHabilitado(true);

        if (producto.getExistencias() == null)
            producto.setExistencias(0);

        producto.setCategoria(cat);

        return repo.save(producto);
    }

    public Producto actualizar(Long id, Producto datos, Long categoriaId) {

        Producto p = repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado"));

        if (categoriaId != null) {
            Categoria cat = categoriaRepo.findById(categoriaId)
                    .orElseThrow(() -> new RecursoNoEncontradoException("Categoría no encontrada"));
            p.setCategoria(cat);
        }

        p.setNombre(datos.getNombre());
        p.setDescripcion(datos.getDescripcion());
        p.setPrecio(datos.getPrecio());

        if (datos.getSku() != null && datos.getSku().isBlank()) {
            p.setSku(null);
        } else {
            p.setSku(datos.getSku());
        }

        if (datos.getExistencias() != null)
            p.setExistencias(datos.getExistencias());

        if (datos.getHabilitado() != null)
            p.setHabilitado(datos.getHabilitado());

        if (datos.getRuta() != null && !datos.getRuta().isBlank()) {

            String nuevaRuta = SlugUtil.slug(datos.getRuta());

            if (repo.findByRuta(nuevaRuta)
                    .filter(prod -> !prod.getId().equals(id))
                    .isPresent()) {
                nuevaRuta = slugUnico(datos.getRuta());
            }

            p.setRuta(nuevaRuta);
        }

        return repo.save(p);
    }

    public void actualizarStock(Long id, Integer existencias) {
        Producto p = repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado"));
        p.setExistencias(existencias);
        repo.save(p);
    }

    public void habilitar(Long id, boolean habilitado) {
        Producto p = repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado"));
        p.setHabilitado(habilitado);
        repo.save(p);
    }

    public void eliminar(Long id) {
        Producto prod = repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado"));

        borrarDirectorioProducto(id);
        repo.delete(prod);
    }

    @Transactional(readOnly = true)
    public List<Producto> listarHabilitados() {
        return repo.findByHabilitadoTrue();
    }

    @Transactional(readOnly = true)
    public List<Producto> listarTodos() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public Producto getProductoById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado"));
    }

    @Transactional(readOnly = true)
    public Page<Producto> listarPaginado(int pagina, int tamanio) {
        return repo.findAll(PageRequest.of(pagina, tamanio));
    }

    @Transactional(readOnly = true)
    public Page<Producto> buscarPorNombreOSkuPaginado(String q, int pagina, int tamanio) {

        if (q == null || q.isBlank())
            return listarPaginado(pagina, tamanio);

        return repo.findByNombreContainingIgnoreCaseOrSkuContainingIgnoreCase(
                q, q, PageRequest.of(pagina, tamanio)
        );
    }

    private String slugUnico(String base) {
        String s = SlugUtil.slug(base);
        String intento = s;
        int i = 2;

        while (repo.findByRuta(intento).isPresent()) {
            intento = s + "-" + i++;
        }
        return intento;
    }

    private void borrarDirectorioProducto(Long productoId) {
        try {
            Path dir = Paths.get(uploadsDir).toAbsolutePath().normalize()
                    .resolve("products")
                    .resolve(String.valueOf(productoId));

            if (Files.exists(dir)) {
                Files.walk(dir)
                        .sorted((a, b) -> b.compareTo(a))
                        .forEach(p -> {
                            try {
                                Files.deleteIfExists(p);
                            } catch (Exception ignored) {}
                        });
            }
        } catch (Exception ignored) {}
    }
}