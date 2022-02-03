package emma.galzio.goodenergysports.productos.commons.persistence.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "talle")
public class TalleEntity {

    @EmbeddedId
    private TalleEntityId id;

    @Column
    private String equivalencia;

    @ManyToOne
    @JoinColumn(name = "categoria_producto", insertable = false, updatable = false)
    @MapsId("categoriaProducto")
    private CategoriaEntity categoria;

    @Column
    private LocalDate fechaBaja;

    public TalleEntityId getId() {
        return id;
    }

    public void setId(TalleEntityId id) {
        this.id = id;
    }

    public String getEquivalencia() {
        return equivalencia;
    }

    public void setEquivalencia(String equivalencia) {
        this.equivalencia = equivalencia;
    }

    public CategoriaEntity getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaEntity categoria) {
        this.categoria = categoria;
    }

    public LocalDate getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(LocalDate fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TalleEntity that = (TalleEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
