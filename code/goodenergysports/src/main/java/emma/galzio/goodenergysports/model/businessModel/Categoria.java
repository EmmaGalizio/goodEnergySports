package emma.galzio.goodenergysports.model.businessModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Categoria {

    private Integer idCategoria;
    private String nombre;
    private String descripcion;
    private LocalDate fechaBaja;
    private List<Categoria> subCategorias;
    private Categoria categoriaSuperior;

    public Categoria() {
    }

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        //if(idCategoria < 0) throw new IllegalArgumentException("El id de la categoria no puede ser negativo");
        this.idCategoria = idCategoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        //if(nombre.length() > 60) throw new IllegalArgumentException("El nombre no puede tener más de 60 caracteres");
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        //if(descripcion.length() > 200) throw new IllegalArgumentException("La descripcion no puede tener más de 200 caracteres");
        this.descripcion = descripcion;
    }

    public LocalDate getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(LocalDate fechaBaja) {
        //if(fechaBaja != null && fechaBaja.isAfter(LocalDate.now())) throw new IllegalArgumentException("La fecha de baja no puede ser posterior a la fecha actual");
        this.fechaBaja = fechaBaja;
    }

    public List<Categoria> getSubCategorias() {
        return subCategorias;
    }

    public void setSubCategorias(List<Categoria> subCategorias) {

        this.subCategorias = subCategorias;
    }

    public void agregarSubCategoria(Categoria categoria){
        if(categoria == null) throw new IllegalArgumentException("No es posible agregar una subcategoria nula");
        if(categoria.getNombre() == null || categoria.getNombre().trim().isEmpty()){
            throw new IllegalArgumentException("La sub categoria debe tener un nombre no nulo");
        }
        if(subCategorias == null) subCategorias = new ArrayList<>();
        subCategorias.forEach((subCategoria) -> {
            if(subCategoria.getNombre().equals(categoria.getNombre().trim())){
                throw new IllegalArgumentException("La categoría actual ya es padre de la sub categoria que se intenta agregar");
            }
        });
        subCategorias.add(categoria);
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

    public boolean esCategoriaValida() throws IllegalArgumentException{

        String error = "";

        if(this.nombre == null || this.nombre.trim().isEmpty() || this.nombre.length() > 60)
                                            error += "- El nombre de la categoria debe contener entre 1 y 60 caracteres\n";

        if(this.descripcion != null && this.descripcion.length() > 200)
                                            error += "- La descripcion de la categoria debe contenter entre 0 y 200 caracteres\n";
        if(this.fechaBaja != null && this.fechaBaja.isAfter(LocalDate.now()))
                                            error += "- LA fecha de baja de la categoria no puede ser mayor a la fecha actual\n";
        if(this.idCategoria != null && this.idCategoria <= 0)
                                            error += "- El id de la categoría debe ser un número entero positivo\n";

        if(!error.isEmpty()) throw new IllegalArgumentException(error);
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
