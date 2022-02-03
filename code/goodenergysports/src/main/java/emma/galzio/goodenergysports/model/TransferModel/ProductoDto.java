package emma.galzio.goodenergysports.model.TransferModel;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public class ProductoDto {

    private Integer codigoProducto;
    private String nombre;
    private String descripcion;
    private Double precio;
    private LocalDate fechaAlta;
    private LocalDate fechaBaja;

    //Para guardar y mostrar en el navegador
    private List<String> imagenes;

    //Para cuando se suben las imagenes desde el navegador
    private List<MultipartFile> archivosImagenes;

    private CategoriaDto categoria;

    private List<StockDto> stock;

    //From here getters and setters

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

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
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

    public List<String> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<String> imagenes) {
        this.imagenes = imagenes;
    }

    public List<MultipartFile> getArchivosImagenes() {
        return archivosImagenes;
    }

    public void setArchivosImagenes(List<MultipartFile> archivosImagenes) {
        this.archivosImagenes = archivosImagenes;
    }

    public CategoriaDto getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaDto categoria) {
        this.categoria = categoria;
    }

    public List<StockDto> getStock() {
        return stock;
    }

    public void setStock(List<StockDto> stock) {
        this.stock = stock;
    }
}
