package com.bobax.product_service.model;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable {

  @CreatedDate
  @Column(name = "fecha_creacion", updatable = false)
  private LocalDateTime fechaCreacion;

  @LastModifiedDate
  @Column(name = "fecha_actualizacion")
  private LocalDateTime fechaActualizacion;


  public LocalDateTime getFechaCreacion() { return fechaCreacion; }
  public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
  public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
  public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
}
