package com.bobax.product_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bobax.product_service.dto.ProductDTO;
import com.bobax.product_service.model.Producto;
import com.bobax.product_service.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
	
	private final ProductService productoService;

	    public ProductController(ProductService productoService) {
	        this.productoService = productoService;
	    }

	    // LISTAR PAGINADO
	    @GetMapping
	    public Page<Producto> listar(
	            @RequestParam(defaultValue = "0") int page,
	            @RequestParam(defaultValue = "10") int size) {

	        return productoService.listarPaginado(page, size);
	    }

	    // BUSCAR
	    @GetMapping("/buscar")
	    public Page<Producto> buscar(
	            @RequestParam String q,
	            @RequestParam(defaultValue = "0") int page,
	            @RequestParam(defaultValue = "10") int size) {

	        return productoService.buscarPorNombreOSkuPaginado(q, page, size);
	    }

	    // OBTENER POR ID
	    @GetMapping("/{id}")
	    public Producto obtener(@PathVariable Long id) {
	        return productoService.getProductoById(id);
	    }

	    // CREAR
	    @PostMapping
	    public Producto crear(
	            @RequestBody Producto producto,
	            @RequestParam Long categoriaId) {

	        return productoService.crear(producto, categoriaId);
	    }

	    // ACTUALIZAR
	    @PutMapping("/{id}")
	    public Producto actualizar(
	            @PathVariable Long id,
	            @RequestBody Producto producto,
	            @RequestParam Long categoriaId) {

	        return productoService.actualizar(id, producto, categoriaId);
	    }

	    // CAMBIAR ESTADO
	    @PatchMapping("/{id}/estado")
	    public void cambiarEstado(
	            @PathVariable Long id,
	            @RequestParam boolean habilitado) {

	        productoService.habilitar(id, habilitado);
	    }

	    // ELIMINAR
	    @DeleteMapping("/{id}")
	    public void eliminar(@PathVariable Long id) {
	        productoService.eliminar(id);
	    }
}
