package emma.galzio.goodenergysports.model.TransferModel;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoriaDto implements Comparable<CategoriaDto> {

    private Integer idCategoria;
    private String nombre;
    private String descripcion;
    private LocalDate fechaBaja;
    private Integer idCategoriaSuperior;
    private String nombreCategoriaSuperior;
    private Map<Integer, String> subCategorias;
    private boolean bajaRegistrada;

    public CategoriaDto() {
    }

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

    public Integer getIdCategoriaSuperior() {
        return idCategoriaSuperior;
    }

    public void setIdCategoriaSuperior(Integer idCategoriaSuperior) {
        this.idCategoriaSuperior = idCategoriaSuperior;
    }

    public String getNombreCategoriaSuperior() {
        return nombreCategoriaSuperior;
    }

    public void setNombreCategoriaSuperior(String nombreCategoriaSuperior) {
        this.nombreCategoriaSuperior = nombreCategoriaSuperior;
    }

    public boolean isBajaRegistrada() {
        return bajaRegistrada;
    }

    public void setBajaRegistrada(boolean bajaRegistrada) {
        this.bajaRegistrada = bajaRegistrada;
    }

    public Map<Integer, String> getSubCategorias() {
        return subCategorias;
    }

    public void setSubCategorias(Map<Integer, String> subCategorias) {
        this.subCategorias = subCategorias;
    }
    public void agregarSubcategoria(Integer id, String nombre){

        if(id == null || id <= 0) throw new IllegalArgumentException("El id debe contener un valor entero positivo");
        if(nombre == null || nombre.trim().isEmpty()) throw new IllegalArgumentException("El nombre debe ser una cadena no vacia");

        if(subCategorias == null) subCategorias = Collections.synchronizedMap(new HashMap<>());
        subCategorias.put(id, nombre);

    }

    @Override
    public int compareTo(CategoriaDto o) {
        return this.idCategoria - o.idCategoria;
    }

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("");
        stringBuffer.append("Categoria:\n\tId: ").append(this.idCategoria);
        stringBuffer.append("\n\tNombre: ").append(this.nombre);
        stringBuffer.append("\n\tDescripcion: ").append(this.descripcion);

        if(idCategoriaSuperior != null){
            stringBuffer.append("\n\tId Categoria Superior: ").append(this.idCategoriaSuperior);
        }
        if(subCategorias != null && !subCategorias.isEmpty()){
            stringBuffer.append("\n\tSubCategorias:");
            subCategorias.entrySet().forEach((subCatEntry) ->{
                stringBuffer.append("\n\t\tCategoria:\n\t\tId: ").append(subCatEntry.getKey());
                stringBuffer.append("\n\t\tNombre: ").append(subCatEntry.getValue());

            });
        }

        return stringBuffer.toString();
    }
}
