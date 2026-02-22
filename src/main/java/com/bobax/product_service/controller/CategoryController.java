package com.bobax.product_service.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bobax.product_service.model.Categoria;
import com.bobax.product_service.service.CategoriaService;
import org.springframework.data.domain.Page;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoryController {

    private final CategoriaService service;

    public CategoryController(CategoriaService service) {
        this.service = service;
    }

    // LISTAR TODO
    @GetMapping
    public List<Categoria> listar() {
        return service.listar();
    }

    // LISTAR PAGINADO
    @GetMapping("/paginado")
    public Page<Categoria> listarPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return service.listarPaginado(page, size);
    }

    // BUSCAR PAGINADO
    @GetMapping("/buscar")
    public Page<Categoria> buscar(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return service.buscarPorNombreORutaPaginado(q, page, size);
    }

    // OBTENER POR ID
    @GetMapping("/{id}")
    public Categoria obtener(@PathVariable Long id) {
        return service.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
    }

    // CREAR
    @PostMapping
    public Categoria crear(@RequestBody Categoria categoria) {
        return service.crear(categoria);
    }

    // ACTUALIZAR
    @PutMapping("/{id}")
    public Categoria actualizar(
            @PathVariable Long id,
            @RequestBody Categoria categoria) {

        return service.actualizar(id, categoria);
    }

    // ELIMINAR
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
