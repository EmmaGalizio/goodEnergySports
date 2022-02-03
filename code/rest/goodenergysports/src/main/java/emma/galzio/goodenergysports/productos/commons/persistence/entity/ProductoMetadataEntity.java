package emma.galzio.goodenergysports.productos.commons.persistence.entity;

import javax.persistence.*;

@Entity
@Table(name = "producto_metadata")
public class ProductoMetadataEntity {

    @EmbeddedId
    private ProductoMetadataEntityId id;


    @ManyToOne
    @JoinColumn(name = "producto", referencedColumnName = "codigo_producto", insertable = false, updatable = false)
    @MapsId("producto")
    private ProductoEntity producto;
    @Column
    private String valor;


    public ProductoMetadataEntityId getId() {
        return id;
    }

    public void setId(ProductoMetadataEntityId id) {
        this.id = id;
    }

    public ProductoEntity getProducto() {
        return producto;
    }

    public void setProducto(ProductoEntity producto) {
        this.producto = producto;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
