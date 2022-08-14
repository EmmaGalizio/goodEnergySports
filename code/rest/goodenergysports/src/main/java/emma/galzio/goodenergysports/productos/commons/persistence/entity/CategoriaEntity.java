package emma.galzio.goodenergysports.productos.commons.persistence.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name="categoria")
public class CategoriaEntity {

    @Id
    @Column(name = "id_categoria")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCategoria;
    @Column(unique = true)
    private String nombre;
    @Column
    private String descripcion;
    @Column
    private LocalDate fechaBaja;
    @ManyToOne
    @JoinColumn(name = "categoria_superior")
    private CategoriaEntity categoriaSuperior;

    @OneToMany(mappedBy = "categoriaSuperior", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<CategoriaEntity> subCategorias;

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer id) {
        this.idCategoria = id;
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

    public LocalDate getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(LocalDate fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public CategoriaEntity getCategoriaSuperior() {
        return categoriaSuperior;
    }

    public void setCategoriaSuperior(CategoriaEntity categoriaSuperior) {
        this.categoriaSuperior = categoriaSuperior;
    }

    public List<CategoriaEntity> getSubCategorias() {
        return subCategorias;
    }

    public void setSubCategorias(List<CategoriaEntity> subCategorias) {
        this.subCategorias = subCategorias;
    }
}
