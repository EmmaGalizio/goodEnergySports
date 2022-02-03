package emma.galzio.goodenergysports.productos.commons.persistence.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "producto")
public class ProductoEntity {

    @Id
    @Column(name = "codigo_producto")
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer codigoProducto;

    @Column
    private String nombre;
    @Column
    private String descripcion;
    @Column
    private BigDecimal precio;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "categoria", updatable = false)
    private CategoriaEntity categoria;
    @Column(name = "fecha_alta",updatable = false)
    private LocalDate fechaAlta;
    @Column(name = "fecha_baja")
    private LocalDate fechaBaja;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL)
    private List<ImagenProductoEntity> imagenes;
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL)
    private List<StockEntity> stock;
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL)
    private List<ProductoMetadataEntity> productoMetadata;

    public Integer getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(Integer codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public CategoriaEntity getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaEntity categoria) {
        this.categoria = categoria;
    }

    public LocalDate getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(LocalDate fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public LocalDate getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(LocalDate fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public List<ImagenProductoEntity> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<ImagenProductoEntity> imagenes) {
        this.imagenes = imagenes;
    }

    public List<StockEntity> getStock() {
        return stock;
    }

    public void setStock(List<StockEntity> stock) {
        this.stock = stock;
    }


    public List<ProductoMetadataEntity> getProductoMetadata() {
        return productoMetadata;
    }

    public void setProductoMetadata(List<ProductoMetadataEntity> productoMetadata) {
        this.productoMetadata = productoMetadata;
    }
}
