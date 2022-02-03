package emma.galzio.goodenergysports.model.entityModel;

import javax.persistence.*;

@Entity
@Table(name = "talle")
public class TalleEntity {

    @Id
    @Column(name = "idtalle")
    private Integer idTalle;
    @Column(name = "talle")
    private String talle;
    @Column(name = "equivalencia")
    private String equivalencia;
    @ManyToOne
    @JoinColumn(name = "categoriaProducto")
    private CategoriaEntity categoriaProducto;

    public Integer getIdTalle() {
        return idTalle;
    }

    public void setIdTalle(Integer idTalle) {
        this.idTalle = idTalle;
    }

    public String getTalle() {
        return talle;
    }

    public void setTalle(String talle) {
        this.talle = talle;
    }

    public String getEquivalencia() {
        return equivalencia;
    }

    public void setEquivalencia(String equivalencia) {
        this.equivalencia = equivalencia;
    }

    public CategoriaEntity getCategoriaProducto() {
        return categoriaProducto;
    }

    public void setCategoriaProducto(CategoriaEntity categoriaProducto) {
        this.categoriaProducto = categoriaProducto;
    }

    @Override
    public String toString() {
        return "Talle:\n\tCategoria" + categoriaProducto.getNombre() + "\n\tTalle: " + this.talle +
                "\n\tEquivalencia: " + this.equivalencia;
    }
}
