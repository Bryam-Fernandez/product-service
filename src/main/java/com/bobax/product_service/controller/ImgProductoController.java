package com.bobax.product_service.controller;

import com.bobax.product_service.model.ImgProducto;
import com.bobax.product_service.service.ImgProductoService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/productos/{productoId}/imagenes")
public class ImgProductoController {

    private final ImgProductoService service;

    public ImgProductoController(ImgProductoService service) {
        this.service = service;
    }

    // ===== LISTAR IMÁGENES DE UN PRODUCTO =====
    @GetMapping
    public List<ImgProducto> listar(@PathVariable Long productoId) {
        return service.listarPorProducto(productoId);
    }

    // ===== SUBIR IMÁGENES =====
    @PostMapping
    public ResponseEntity<Void> subir(
            @PathVariable Long productoId,
            @RequestParam("archivos") List<MultipartFile> archivos) {

        service.subirImagenes(productoId, archivos);
        return ResponseEntity.ok().build();
    }

    // ===== OBTENER URL PRINCIPAL =====
    @GetMapping("/principal/url")
    public String obtenerUrlPrincipal(@PathVariable Long productoId) {
        return service.obtenerUrlPrincipal(productoId);
    }

    // ===== OBTENER IMAGEN PRINCIPAL =====
    @GetMapping("/principal")
    public ImgProducto obtenerPrincipal(@PathVariable Long productoId) {
        return service.obtenerPrincipalPorProducto(productoId);
    }

    // ===== MARCAR COMO PRINCIPAL =====
    @PatchMapping("/{imagenId}/principal")
    public ResponseEntity<Void> marcarPrincipal(@PathVariable Long imagenId) {
        service.marcarComoPrincipal(imagenId);
        return ResponseEntity.ok().build();
    }

    // ===== REORDENAR =====
    @PatchMapping("/{imagenId}/orden")
    public ResponseEntity<Void> reordenar(
            @PathVariable Long imagenId,
            @RequestParam int orden) {

        service.reordenar(imagenId, orden);
        return ResponseEntity.ok().build();
    }

    // ===== ELIMINAR =====
    @DeleteMapping("/{imagenId}")
    public ResponseEntity<Void> eliminar(@PathVariable Long imagenId) {
        service.eliminar(imagenId);
        return ResponseEntity.ok().build();
    }
}
