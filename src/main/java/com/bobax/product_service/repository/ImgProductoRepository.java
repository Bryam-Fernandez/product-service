package com.bobax.product_service.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bobax.product_service.model.ImgProducto;
import com.bobax.product_service.model.Producto;

import java.util.List;
import java.util.Optional;

public interface ImgProductoRepository extends JpaRepository<ImgProducto, Long> {


  List<ImgProducto> findByProducto(Producto producto, Sort sort);


  Optional<ImgProducto> findFirstByProducto(Producto producto, Sort sort);
}
