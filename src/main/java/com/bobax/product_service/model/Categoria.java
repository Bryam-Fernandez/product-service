package com.bobax.product_service.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categorias")
@Getter @Setter 
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Categoria extends Auditable {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true, length = 80)
  private String nombre;

  public Long getId() {
	return id;
}

public void setId(Long id) {
	this.id = id;
}

public String getNombre() {
	return nombre;
}

public void setNombre(String nombre) {
	this.nombre = nombre;
}

public String getRuta() {
	return ruta;
}

public void setRuta(String ruta) {
	this.ruta = ruta;
}

public String getDescripcion() {
	return descripcion;
}

public void setDescripcion(String descripcion) {
	this.descripcion = descripcion;
}

public Boolean getHabilitado() {
	return habilitado;
}

public void setHabilitado(Boolean habilitado) {
	this.habilitado = habilitado;
}

@Column(nullable = false, unique = true, length = 100)
  private String ruta; // slug/url amigable

  @Column(length = 255)
  private String descripcion;

  @Column(nullable = false)
  private Boolean habilitado = true;
}
