package emma.galzio.goodenergysports.model.entityModel;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

@Entity
@javax.persistence.Table(name = "productometadata", schema = "goodenergysport", catalog = "")
public class ProductometadataEntity implements Serializable {
    private Integer producto;

    @Id
    @javax.persistence.Column(name = "producto")
    public Integer getProducto() {
        return producto;
    }

    public void setProducto(Integer producto) {
        this.producto = producto;
    }

    private String clave;

    @Id
    @javax.persistence.Column(name = "clave")
    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    private String valor;

    @Basic
    @javax.persistence.Column(name = "valor")
    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductometadataEntity that = (ProductometadataEntity) o;
        return Objects.equals(producto, that.producto) &&
                Objects.equals(clave, that.clave) &&
                Objects.equals(valor, that.valor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(producto, clave, valor);
    }
}
