package emma.galzio.goodenergysports.productos.commons.persistence.entity;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class TalleEntityId implements Serializable {

    private String talle;
    private Integer categoriaProducto;

    public TalleEntityId() {
    }

    public TalleEntityId(String talle, Integer categoriaProducto) {
        this.talle = talle;
        this.categoriaProducto = categoriaProducto;
    }

    public String getTalle() {
        return talle;
    }

    public void setTalle(String talle) {
        this.talle = talle;
    }

    public Integer getCategoriaProducto() {
        return categoriaProducto;
    }

    public void setCategoriaProducto(Integer categoriaProducto) {
        this.categoriaProducto = categoriaProducto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TalleEntityId that = (TalleEntityId) o;
        return talle.equals(that.talle) &&
                categoriaProducto.equals(that.categoriaProducto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(talle, categoriaProducto);
    }
}
