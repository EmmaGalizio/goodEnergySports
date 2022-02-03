package emma.galzio.goodenergysports.productos.admin.transferObject;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;


@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoriaAdminDto extends RepresentationModel<CategoriaAdminDto> {

    private Integer idCategoria;
    private String nombre;
    private String descripcion;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate fechaBaja;
    private CategoriaAdminDto categoriaSuperior;
    //@JsonProperty(access = JsonProperty.Access.READ_ONLY)
    //private List<CategoriaAdminDto> subCategorias;

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

    public CategoriaAdminDto getCategoriaSuperior() {
        return categoriaSuperior;
    }

    public void setCategoriaSuperior(CategoriaAdminDto categoriaSuperior) {
        this.categoriaSuperior = categoriaSuperior;
    }
    /*
    public List<CategoriaAdminDto> getSubCategorias() {
        return subCategorias;
    }

    public void setSubCategorias(List<CategoriaAdminDto> subCategorias) {
        this.subCategorias = subCategorias;
    }*/
}
