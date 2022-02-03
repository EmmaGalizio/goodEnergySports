package emma.galzio.goodenergysports.productos.admin.transferObject;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductoAdminDto extends RepresentationModel<ProductoAdminDto> {

    private Integer codigoProducto;
    private String nombre;
    private String descripcion;
    private Float precio;
    //private CategoriaAdminDto categoria;
    private Integer idCategoria;
    private String nombreCategoria;
    @JsonFormat(pattern = "dd-MM-yyyy")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate fechaAlta;
    @JsonFormat(pattern = "dd-MM-yyyy")
    //@JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate fechaBaja;

    //@JsonProperty(access = JsonProperty.Access.WRITE_ONLY) //Permite que la propiedad sea deserializada pero no serializada
    //Permite leerla desde la solicitud, pero no la incluye en la respuesta
    private List<StockAdminDto> stock;

    private List<ImagenProductoDto> imagenes;


    public List<ImagenProductoDto> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<ImagenProductoDto> imagenes) {
        this.imagenes = imagenes;
    }

    public Integer getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(Integer codigoProducto) {
        this.codigoProducto = codigoProducto;
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

    public Float getPrecio() {
        return precio;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
    }

    /*
    public CategoriaAdminDto getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaAdminDto categoria) {
        this.categoria = categoria;
    }

     */

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public LocalDate getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(LocalDate fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public LocalDate getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(LocalDate fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public List<StockAdminDto> getStock() {
        return stock;
    }

    public void setStock(List<StockAdminDto> stock) {
        this.stock = stock;
    }


}
