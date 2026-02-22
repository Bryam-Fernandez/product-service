package com.bobax.product_service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bobax.product_service.model.Categoria;

import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
  Optional<Categoria> findByRuta(String ruta);
  boolean existsByNombre(String nombre);
  Page<Categoria> findByNombreContainingIgnoreCaseOrRutaContainingIgnoreCase(String nombre, String ruta, Pageable pageable);

}