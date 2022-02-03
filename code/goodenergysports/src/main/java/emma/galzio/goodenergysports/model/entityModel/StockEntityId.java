package emma.galzio.goodenergysports.model.entityModel;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class StockEntityId implements Serializable {

    @Column(name = "producto")
    private Integer producto;
    @Column(name = "talle")
    private Integer talle;

    public StockEntityId() {
    }

    public Integer getProducto() {
        return producto;
    }

    public void setProducto(Integer producto) {
        this.producto = producto;
    }

    public Integer getTalle() {
        return talle;
    }

    public void setTalle(Integer talle) {
        this.talle = talle;
    }
}
