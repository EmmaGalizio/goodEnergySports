package emma.galzio.goodenergysports.model.entityModel;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "categoria", schema = "goodenergysport", catalog = "")
public class CategoriaEntity {

    @Id
    @Column(name = "idcategoria")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCategoria;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "fechabaja")
    @PastOrPresent
    private LocalDate fechaBaja;

    @ManyToOne()
    @JoinColumn(name = "categoriaSuperior")
    private CategoriaEntity categoriaSuperior;

    @OneToMany(mappedBy = "categoriaSuperior", fetch = FetchType.EAGER)
    private List<CategoriaEntity> subCategorias;


    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoriaEntity that = (CategoriaEntity) o;
        return Objects.equals(idCategoria, that.idCategoria) &&
                Objects.equals(nombre, that.nombre) &&
                Objects.equals(descripcion, that.descripcion) &&
                Objects.equals(fechaBaja, that.fechaBaja);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCategoria, nombre, descripcion, fechaBaja);
    }

    @Override
    public String toString() {

        StringBuffer categoriaBuffer = new StringBuffer("");
        categoriaBuffer.append("Categoía\n\tID: ").append(this.idCategoria);
        categoriaBuffer.append("\n\tNombre: ").append(this.nombre);
        categoriaBuffer.append("\n\tDescripcion: ").append(this.descripcion);
        if(this.fechaBaja != null) categoriaBuffer.append("\n\tFecha de Baja: ").append(this.fechaBaja);

        if(this.getCategoriaSuperior() != null) {
            categoriaBuffer.append("\n\tCategoria Superior:\n\t\t");
            categoriaBuffer.append(this.getCategoriaSuperior());}

         /*
        if(this.getSubCategorias() != null && !this.getSubCategorias().isEmpty()){
            categoriaBuffer.append("\n\tSub Categorias:\n\t\t");
            this.getSubCategorias().forEach((subCat) ->{categoriaBuffer.append(subCat.toString());});
        }
        */
        return categoriaBuffer.toString();
    }
}
