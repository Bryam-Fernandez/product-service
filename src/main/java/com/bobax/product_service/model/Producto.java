package com.bobax.product_service.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "productos")
public class Producto extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(nullable = false, unique = true, length = 120)
    private String ruta;

    @Column(length = 2000)
    private String descripcion;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precio;

    @Column(name = "imagen")
    private String imagen;

    @Column(length = 50, unique = true)
    private String sku;

    @Column(nullable = false)
    private Integer existencias = 0;

    @Column(nullable = false)
    private Boolean habilitado = true;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

  
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ImgProducto> imagenes = new ArrayList<>();

   
    public Producto() {}

    public Producto(Long id, String nombre, String ruta, String descripcion, BigDecimal precio, String sku,
                    Integer existencias, Boolean habilitado, Categoria categoria) {
        this.id = id;
        this.nombre = nombre;
        this.ruta = ruta;
        this.descripcion = descripcion;
        this.precio = precio;
        this.sku = sku;
        this.existencias = existencias;
        this.habilitado = habilitado;
        this.categoria = categoria;
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getRuta() { return ruta; }
    public void setRuta(String ruta) { this.ruta = ruta; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public Integer getExistencias() { return existencias; }
    public void setExistencias(Integer existencias) { this.existencias = existencias; }

    public Boolean getHabilitado() { return habilitado; }
    public void setHabilitado(Boolean habilitado) { this.habilitado = habilitado; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    public List<ImgProducto> getImagenes() { return imagenes; }
    public void setImagenes(List<ImgProducto> imagenes) { this.imagenes = imagenes; }

   
    public void agregarImagen(ImgProducto img) {
        imagenes.add(img);
        img.setProducto(this);
    }

    public void quitarImagen(ImgProducto img) {
        imagenes.remove(img);
        img.setProducto(null);
    }
    @Transient
    public ImgProducto getImagenPrincipal() {
        if (imagenes != null && !imagenes.isEmpty()) {
            
            for (ImgProducto img : imagenes) {
                if (Boolean.TRUE.equals(img.getPrincipal())) {
                    return img;
                }
            }
            
            return imagenes.get(0);
        }
        return null;
    }

}
