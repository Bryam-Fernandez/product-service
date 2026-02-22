package com.bobax.product_service.model;

import jakarta.persistence.*;

@Entity
@Table(name = "imagenes_producto")
public class ImgProducto extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String url;

    @Column(nullable = false)
    private Integer orden = 0;

    @Column(nullable = false)
    private Boolean principal = false;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

   
    public ImgProducto() {}

    public ImgProducto(Long id, String url, Integer orden, Boolean principal, Producto producto) {
        this.id = id;
        this.url = url;
        this.orden = orden;
        this.principal = principal;
        this.producto = producto;
    }

 
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public Integer getOrden() { return orden; }
    public void setOrden(Integer orden) { this.orden = orden; }

    public Boolean getPrincipal() { return principal; }
    public void setPrincipal(Boolean principal) { this.principal = principal; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

  
    @Override
    public String toString() {
        return "ImgProducto{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", orden=" + orden +
                ", principal=" + principal +
                '}';
    }
}
