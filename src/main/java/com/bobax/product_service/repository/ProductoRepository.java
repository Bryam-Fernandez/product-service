package com.bobax.product_service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bobax.product_service.model.Categoria;
import com.bobax.product_service.model.Producto;

import java.util.List;
import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
  Optional<Producto> findByRuta(String ruta);
  
  List<Producto> findByCategoriaAndHabilitadoTrue(Categoria categoria);
  
  Page<Producto> findByNombreContainingIgnoreCaseOrSkuContainingIgnoreCase(
	      String nombre, String sku, Pageable pageable);  
  List<Producto> findByCategoriaIdAndHabilitadoTrue(Long categoriaId);
  List<Producto> findByHabilitadoTrue();

}

