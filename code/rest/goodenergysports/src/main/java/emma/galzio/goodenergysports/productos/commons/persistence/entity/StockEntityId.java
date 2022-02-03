package emma.galzio.goodenergysports.productos.commons.persistence.entity;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class StockEntityId implements Serializable {

    private Integer producto;
    private TalleEntityId talleId;

    public StockEntityId() {
    }

    public StockEntityId(Integer producto, TalleEntityId talleId) {
        this.producto = producto;
        this.talleId = talleId;
    }

    public Integer getProducto() {
        return producto;
    }

    public void setProducto(Integer producto) {
        this.producto = producto;
    }

    public TalleEntityId getTalleId() {
        return talleId;
    }

    public void setTalleId(TalleEntityId talleId) {
        this.talleId = talleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockEntityId that = (StockEntityId) o;
        return producto.equals(that.producto) &&
                talleId.equals(that.talleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(producto, talleId);
    }
}
