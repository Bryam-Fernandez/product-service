package com.bobax.product_service.service;

	import org.springframework.data.domain.Page;
	import org.springframework.data.domain.PageRequest;
	import org.springframework.stereotype.Service;
	import org.springframework.transaction.annotation.Transactional;

import com.bobax.product_service.exception.RecursoNoEncontradoException;
import com.bobax.product_service.model.Categoria;
import com.bobax.product_service.repository.CategoriaRepository;
import com.bobax.product_service.util.SlugUtil;

import java.util.List;
	import java.util.Optional;

	@Service
	@Transactional
	public class CategoriaService {

	    private final CategoriaRepository repo;

	    public CategoriaService(CategoriaRepository repo) {
	        this.repo = repo;
	    }

	    // ===== LISTAR =====

	    @Transactional(readOnly = true)
	    public List<Categoria> listar() {
	        return repo.findAll();
	    }

	    @Transactional(readOnly = true)
	    public List<Categoria> listarTodos() {
	        return repo.findAll();
	    }

	    @Transactional(readOnly = true)
	    public Optional<Categoria> buscarPorId(Long id) {
	        return repo.findById(id);
	    }

	    @Transactional(readOnly = true)
	    public Optional<Categoria> buscarPorRuta(String ruta) {
	        return repo.findByRuta(ruta);
	    }

	    // ===== CREAR =====

	    public Categoria crear(Categoria categoria) {

	        if (categoria.getRuta() == null || categoria.getRuta().isBlank()) {
	            categoria.setRuta(SlugUtil.slug(categoria.getNombre()));
	        }

	        categoria.setHabilitado(Boolean.TRUE);
	        return repo.save(categoria);
	    }

	    // ===== ACTUALIZAR =====

	    public Categoria actualizar(Long id, Categoria datos) {

	        Categoria cat = repo.findById(id)
	                .orElseThrow(() -> new RecursoNoEncontradoException("Categoría no encontrada"));

	        cat.setNombre(datos.getNombre());
	        cat.setDescripcion(datos.getDescripcion());

	        if (datos.getHabilitado() != null) {
	            cat.setHabilitado(datos.getHabilitado());
	        }

	        if (datos.getRuta() != null && !datos.getRuta().isBlank()) {
	            cat.setRuta(SlugUtil.slug(datos.getRuta()));
	        }

	        return repo.save(cat);
	    }

	    // ===== ELIMINAR =====

	    public void eliminar(Long id) {

	        if (!repo.existsById(id)) {
	            throw new RecursoNoEncontradoException("Categoría no encontrada");
	        }

	        repo.deleteById(id);
	    }

	    // ===== PAGINACIÓN =====

	    @Transactional(readOnly = true)
	    public Page<Categoria> listarPaginado(int pagina, int tamanio) {
	        return repo.findAll(PageRequest.of(pagina, tamanio));
	    }

	    @Transactional(readOnly = true)
	    public Page<Categoria> buscarPorNombreORutaPaginado(String q, int pagina, int tamanio) {

	        if (q == null || q.isBlank()) {
	            return listarPaginado(pagina, tamanio);
	        }

	        return repo.findByNombreContainingIgnoreCaseOrRutaContainingIgnoreCase(
	                q, q, PageRequest.of(pagina, tamanio));
	    }
	}

