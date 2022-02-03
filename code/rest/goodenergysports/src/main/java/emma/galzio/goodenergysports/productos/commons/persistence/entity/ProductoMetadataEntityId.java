package emma.galzio.goodenergysports.productos.commons.persistence.entity;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ProductoMetadataEntityId implements Serializable {

    private Integer producto;
    private String clave;

    public Integer getProducto() {
        return producto;
    }

    public void setProducto(Integer producto) {
        this.producto = producto;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(! (obj instanceof ProductoMetadataEntityId)) return false;
        ProductoMetadataEntityId aux;
        try{
            aux = (ProductoMetadataEntityId) obj;
        } catch(ClassCastException e){
            return false;
        }

        return aux.getProducto().equals(this.producto) && aux.getClave().equals(this.clave);
    }

    @Override
    public int hashCode() {
        return Objects.hash(producto, clave);
    }
}
