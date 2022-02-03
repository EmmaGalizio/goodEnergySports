package emma.galzio.goodenergysports.productos.commons.persistence.entity;

import javax.persistence.*;

@Entity
@Table(name = "imagen_producto")
public class ImagenProductoEntity {

    @Id
    private String url;
    @ManyToOne
    @JoinColumn(name = "producto")
    private ProductoEntity producto;
    @Column
    private Integer orden;

    public String getUrl() {
        return url;
    }

    public void setUrl(String urlImagen) {
        this.url = urlImagen;
    }

    public ProductoEntity getProducto() {
        return producto;
    }

    public void setProducto(ProductoEntity producto) {
        this.producto = producto;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }
}
