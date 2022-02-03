package emma.galzio.goodenergysports.productos.commons.domain;

import emma.galzio.goodenergysports.utils.exception.DomainException;

import java.time.LocalDate;
import java.util.*;

public class Categoria {

    private Integer idCategoria;
    private String nombre;
    private String descripcion;
    private LocalDate fechaBaja;
    private List<Categoria> subCategorias;
    private Categoria categoriaSuperior;
    private Integer idCategoriaSuperior;

    public Categoria() {
    }

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria){
        //if(idCategoria != null && idCategoria < 0) throw new DomainException("El id de la categoria no puede ser negativo");
        this.idCategoria = idCategoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre){
        this.nombre = nombre;
        if(this.nombre != null && !this.nombre.trim().isEmpty()){
            this.nombre = this.nombre.trim();
            this.nombre = this.nombre.substring(0,1).toUpperCase().concat(this.nombre.substring(1).toLowerCase());
        }
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion){
        this.descripcion = descripcion;
        if(this.descripcion != null){
            this.descripcion = this.descripcion.trim();
        }
    }

    public LocalDate getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(LocalDate fechaBaja){
        //if(fechaBaja != null && fechaBaja.isAfter(LocalDate.now())) throw new DomainException("La fecha de baja no puede ser posterior a la fecha actual");
        this.fechaBaja = fechaBaja;
    }

    public List<Categoria> getSubCategorias() {
        return subCategorias;
    }

    public void setSubCategorias(List<Categoria> subCategorias) {

        this.subCategorias = subCategorias;
    }

    public Integer getIdCategoriaSuperior() {
        return idCategoriaSuperior;
    }

    public void setIdCategoriaSuperior(Integer idCategoriaSuperior) {
        this.idCategoriaSuperior = idCategoriaSuperior;
    }

    @Deprecated
    /***
     * This method should not be used never, under any circumstance
     * Could cause integrity issues in the database
     * When most of the project is done i'll delete this
     */
    public void agregarSubCategoria(Categoria categoria){
        if(categoria == null) throw new IllegalArgumentException("No es posible agregar una subcategoria nula");
        if(categoria.getNombre() == null || categoria.getNombre().trim().isEmpty()){
            throw new DomainException("La sub categoria debe tener un nombre no nulo");
        }
        if(subCategorias == null) subCategorias = new ArrayList<>();

       for(Categoria subCategoria : categoria.getSubCategorias()){
            if(subCategoria.getNombre().equals(categoria.getNombre().trim())){
                throw new DomainException("La categoría actual ya es padre de la sub categoria que se intenta agregar");
            }
        }
        subCategorias.add(categoria);
    }
    public void registrarBaja(){
       DomainException domainException = null;
       if(esCategoriaPadre()){
           domainException = new DomainException();
           domainException.addCause("SUB_CATEGORIAS", "No se puede registrar la baja de una categoria que posea sub categorias activas");
       }
       if(!estaActiva()){
           domainException = new DomainException();
           domainException.addCause("FECHA_BAJA", "La categoria ya se encuentra con baja registrada: " + fechaBaja.toString());

       }
       if(domainException != null){
           throw domainException;
       }
       fechaBaja = LocalDate.now();
    }

    public boolean esCategoriaHija(){
        return categoriaSuperior != null || (idCategoriaSuperior != null && idCategoriaSuperior >= 0);
    }
    public boolean esCategoriaPadre(){

        if(subCategorias != null){
            for(Categoria subCat: subCategorias){
                if(subCat.estaActiva()){
                    return true;
                }
            }
        }
        return false;
    }

    public Categoria getCategoriaSuperior() {
        return categoriaSuperior;
    }

    public void setCategoriaSuperior(Categoria categoriaSuperior) {
        this.categoriaSuperior = categoriaSuperior;
    }

    public boolean estaActiva(){
        return this.fechaBaja == null;
    }

    public boolean esCategoriaValida(){

        String error = "";
        String message;
        Map<String, String> causes = new HashMap<>();
        if(this.nombre == null || this.nombre.trim().isEmpty() || this.nombre.length() > 60){
            message = "- El nombre de la categoria debe contener entre 1 y 60 caracteres";
            causes.put("NOMBRE", message);
            error += message + "\n";
        }
        if(this.descripcion != null && this.descripcion.length() > 200){
            message = "- La descripcion de la categoria debe contenter entre 0 y 200 caracteres";
            causes.put("DESCRIPCION", message);
            error += message + "\n";
        }
        if(this.fechaBaja != null && this.fechaBaja.isAfter(LocalDate.now())){
            message = "- La fecha de baja de la categoria no puede ser mayor a la fecha actual";
            causes.put("FECHA_BAJA", message);
            error += message + "\n";

        }
        if(this.idCategoria != null && this.idCategoria <= 0){
            message = "- El id de la categoría debe ser un número entero positivo";
            causes.put("ID", message);
            error += message + "\n";
        }

        if(!error.isEmpty()) {
            DomainException domainException = new DomainException(error);
            domainException.setCauses(causes);
            throw domainException;
        }
        return true;
    }

    public boolean sePuedeRegistrarBaja(){

        if(this.getSubCategorias() == null || this.getSubCategorias().isEmpty()){
            return true;
        } else{
            long cantActiva = this.getSubCategorias().stream().filter((subCat) -> subCat.getFechaBaja() == null).count();

            return  cantActiva == 0;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Categoria categoria = (Categoria) o;
        return Objects.equals(idCategoria, categoria.idCategoria) &&
                nombre.equals(categoria.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCategoria, nombre);
    }

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("");
        stringBuffer.append("Categoria:\n\tId: ").append(this.idCategoria);
        stringBuffer.append("\n\tNombre: ").append(this.nombre);
        stringBuffer.append("\n\tDescripcion: ").append(this.descripcion);
        if(subCategorias != null && !subCategorias.isEmpty()){
            stringBuffer.append("\n\tSubCategorias:");
            subCategorias.forEach((subCat) ->{
                stringBuffer.append("\n\t\tCategoria:\n\t\tId: ").append(subCat.getIdCategoria());
                stringBuffer.append("\n\t\tNombre: ").append(subCat.getNombre());
                stringBuffer.append("\n\t\tDescripcion: ").append(subCat.getDescripcion());
            });
        }

        return stringBuffer.toString();
    }


}
