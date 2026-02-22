package com.bobax.product_service.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.bobax.product_service.exception.RecursoNoEncontradoException;
import com.bobax.product_service.model.ImgProducto;
import com.bobax.product_service.model.Producto;
import com.bobax.product_service.repository.ImgProductoRepository;
import com.bobax.product_service.repository.ProductoRepository;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ImgProductoService {

    private final ImgProductoRepository imgRepo;
    private final ProductoRepository productoRepo;

    @Value("${app.uploads.dir:uploads}")
    private String uploadsDir;

    private static final Sort IMG_SORT = Sort.by(
            Sort.Order.desc("principal"),
            Sort.Order.asc("orden"),
            Sort.Order.asc("id")
    );

    public ImgProductoService(ImgProductoRepository imgRepo,
                               ProductoRepository productoRepo) {
        this.imgRepo = imgRepo;
        this.productoRepo = productoRepo;
    }

    private Path dirProducto(Long productoId) throws IOException {
        Path base = Paths.get(uploadsDir).toAbsolutePath().normalize();
        Path dir = base.resolve("products").resolve(String.valueOf(productoId));
        Files.createDirectories(dir);
        return dir;
    }

    private static String ext(String name) {
        if (name == null) return "";
        int i = name.lastIndexOf('.');
        return i < 0 ? "" : name.substring(i + 1).toLowerCase();
    }

    // ===== LISTAR =====

    @Transactional(readOnly = true)
    public List<ImgProducto> listarPorProducto(Long productoId) {
        Producto p = productoRepo.findById(productoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado"));

        return imgRepo.findByProducto(p, IMG_SORT);
    }

    // ===== SUBIR =====

    public void subirImagenes(Long productoId, List<MultipartFile> archivos) {

        if (archivos == null || archivos.isEmpty()) return;

        Producto p = productoRepo.findById(productoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado"));

        try {
            Path destino = dirProducto(productoId);

            boolean tienePrincipal = !imgRepo.findByProducto(p, IMG_SORT).isEmpty();

            for (MultipartFile mf : archivos) {

                if (mf.isEmpty()) continue;

                String extension = ext(mf.getOriginalFilename());
                String nombre = UUID.randomUUID().toString().replace("-", "")
                        + (extension.isBlank() ? "" : "." + extension);

                Files.copy(mf.getInputStream(),
                        destino.resolve(nombre),
                        StandardCopyOption.REPLACE_EXISTING);

                ImgProducto img = new ImgProducto();
                img.setProducto(p);
                img.setUrl("/uploads/products/" + productoId + "/" + nombre);
                img.setOrden(0);
                img.setPrincipal(!tienePrincipal);

                imgRepo.save(img);
                tienePrincipal = true;
            }

        } catch (IOException e) {
            throw new RuntimeException("Error guardando imagen", e);
        }
    }

    // ===== ELIMINAR =====

    public void eliminar(Long imagenId) {

        ImgProducto img = imgRepo.findById(imagenId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Imagen no encontrada"));

        Long prodId = img.getProducto().getId();

        try {
            String fileName = Paths.get(img.getUrl()).getFileName().toString();
            Path path = dirProducto(prodId).resolve(fileName);
            Files.deleteIfExists(path);
        } catch (IOException ignore) {}

        boolean eraPrincipal = Boolean.TRUE.equals(img.getPrincipal());
        Producto producto = img.getProducto();

        imgRepo.delete(img);

        if (eraPrincipal) {
            var restantes = imgRepo.findByProducto(producto, IMG_SORT);
            if (!restantes.isEmpty()) {
                restantes.get(0).setPrincipal(true);
                imgRepo.save(restantes.get(0));
            }
        }
    }

    // ===== PRINCIPAL =====

    @Transactional(readOnly = true)
    public ImgProducto obtenerPrincipalPorProducto(Long productoId) {
        Producto p = productoRepo.findById(productoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado"));

        return imgRepo.findFirstByProducto(p, IMG_SORT).orElse(null);
    }

    @Transactional(readOnly = true)
    public String obtenerUrlPrincipal(Long productoId) {
        Producto p = productoRepo.findById(productoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado"));

        return imgRepo.findFirstByProducto(p, IMG_SORT)
                .map(ImgProducto::getUrl)
                .orElse("/img/no-image.png");
    }

    public void marcarComoPrincipal(Long imagenId) {

        ImgProducto img = imgRepo.findById(imagenId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Imagen no encontrada"));

        Producto producto = img.getProducto();
        var todas = imgRepo.findByProducto(producto, IMG_SORT);

        for (ImgProducto i : todas) {
            i.setPrincipal(i.getId().equals(imagenId));
        }

        imgRepo.saveAll(todas);
    }

    public void reordenar(Long imagenId, int orden) {

        ImgProducto img = imgRepo.findById(imagenId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Imagen no encontrada"));

        img.setOrden(orden);
        imgRepo.save(img);
    }
}