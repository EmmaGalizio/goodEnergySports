package emma.galzio.goodenergysports.productos.commons.domain;

import emma.galzio.goodenergysports.utils.exception.DomainException;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Producto {

    private Integer codigo;
    private String nombre;
    private String descripcion;
    private Float precio;
    private LocalDate fechaAlta;
    private LocalDate fechaBaja;
    private Categoria categoria;
    private List<Stock> stock;
    private List<ImagenProducto> imagenes;

    private Map<String, String> metadata;

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
        if(this.nombre == null && !this.nombre.trim().isEmpty()){
            this.nombre = this.nombre.trim();
            this.nombre = this.nombre.substring(0,1).toUpperCase().concat(this.nombre.substring(1).toLowerCase());
        }
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
        if(this.descripcion != null){
            this.descripcion = this.descripcion.trim();
        }
    }

    public Float getPrecio() {
        return precio;
    }

    public void setPrecio(Float precio) {
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

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public List<Stock> getStock() {
        return stock;
    }

    public void setStock(List<Stock> stock) {
        this.stock = stock;
    }

    public List<ImagenProducto> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<ImagenProducto> imagenes) {
        this.imagenes = imagenes;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    public void addMetadata(String key, String value){
        if(key != null && value != null && !key.trim().isEmpty() && !value.trim().isEmpty()){
            if(metadata == null) metadata = new Hashtable<>();
        }
    }

    public String getMetedataForKey(String key){
        if(metadata != null && metadata.containsKey(key)){
            return metadata.get(key);
        }
        return null;
    }

    public boolean estaActivo(){
        return this.fechaBaja == null;
    }
    public boolean validarStockTallesActivos(List<Talle> tallesDeCategoria){

        if(stock == null || stock.isEmpty()){
            DomainException domainException = new DomainException("El producto debe estar disponible en al menos un talle");
            domainException.addCause("STOCK", "El producto debe estar disponible en al menos un talle");
            throw domainException;
        }
        Map<String, Talle> tallesMap = tallesDeCategoria.stream().collect(Collectors.toMap(Talle::getTalle, (talle)->talle));
        for(Stock stockItem : stock){
            Talle talleAux = tallesMap.get(stockItem.getTalle().getTalle());
            if(talleAux != null && !talleAux.estaActivo()){
                DomainException domainException = new DomainException("El producto debe estar disponible en al menos un talle");
                domainException.addCause("STOCK_ITEM", "El producto no puede estar disponible para un talle que se encuentra inactivo: "+talleAux.getTalle());
                throw domainException;
            }
        }
        return true;
    }

    public boolean validate(){
        boolean error = false;
        Map<String,String> causes = null;

        if(codigo != null && codigo <= 0){
            error = true;
            causes = new Hashtable<>();
            causes.put("CODIGO", "El código del producto no puede ser un valor negativo");
        }
        if(nombre == null || nombre.isEmpty() || nombre.length() > 100){
            error = true;
            if(causes == null) causes = new Hashtable<>();
            causes.put("NOMBRE", "El nombre del producto no puede contener más de 100 caracteres");
        }
        if(descripcion != null && descripcion.length() > 200){
            error = true;
            if(causes == null) causes = new Hashtable<>();
            causes.put("DESCRIPCION", "La descripción del producto no puede contener más de 200 caracteres");
        }
        if(precio == null || precio <= 0){
            error = true;
            if(causes == null) causes = new Hashtable<>();
            causes.put("PRECIO", "El precio unitario del producto debe ser un valor positivo");
        }
        if(categoria == null || !categoria.esCategoriaValida()){
            error = true;
            if(causes == null) causes = new Hashtable<>();
            causes.put("CATEGORIA", "El producto debe pertenecer a una categoría activa");
        }
        if(fechaAlta != null && fechaAlta.isAfter(LocalDate.now())){
            error = true;
            if(causes == null) causes = new Hashtable<>();
            causes.put("FECHA_ALTA", "El producto debe haber sido dado de alta en la fecha actual, o una fecha anterior");
        }
        if(stock == null || stock.isEmpty()){
            error = true;
            if(causes == null) causes = new Hashtable<>();
            causes.put("STOCK", "El producto debe estar disponible en al menos un talle");
        }

        for(Stock stockItem : stock){
            if(!stockItem.validate()){
                error = true;
                if(causes == null) causes = new Hashtable<>();
                causes.put("STOCK_ITEM", "El stock disponible no puede ser un número negativo");
            }
        }

        if(error){
            DomainException domainException = new DomainException("Se ha producido un error con la validación del producto");
            domainException.setCauses(causes);
            throw  domainException;
        }
        return true;
    }

    public void registrarBaja() {
        this.fechaBaja = LocalDate.now();
    }

    public void ordenarImageges(){
        imagenes.sort(ImagenProducto.getOrdenComparator());
    }
    public boolean eliminarImagen(int orden){
        if(imagenes == null || imagenes.isEmpty()) return false;
        Iterator<ImagenProducto> imagenProductoIterator = imagenes.iterator();
        while(imagenProductoIterator.hasNext()){
            if(imagenProductoIterator.next().getOrden().equals(orden)){
                imagenProductoIterator.remove();
                return true;
            }
        }
        return false;

    }
    private int buscarImagenRandomAccess(int orden){
        for(int i = 0; i<imagenes.size();i++){
            if(imagenes.get(i).getOrden().equals(orden)){
                return i;
            }
        }
        return -1;
    }
    private int buscarImagenSecuential(int orden){
        for(ImagenProducto imagenProducto: imagenes){
            if(imagenProducto.getOrden().equals(orden)){
                return imagenes.indexOf(imagenProducto);
            }
        }
        return -1;
    }

    public int reOrganizarImagenes() {
        if(imagenes == null) return -1;
        int ultimoOrden = 0;
        for(ImagenProducto imagenProducto: imagenes){
            if((imagenProducto.getOrden() - ultimoOrden) > 1){
                imagenProducto.setOrden(ultimoOrden+1);
            }
            ultimoOrden++;
        }
        return ultimoOrden;
    }

    public boolean validarStockModificado(List<Stock> stockAntiguo){

        Map<String,Stock> stockAntiguoMap = stockAntiguo.stream()
                .collect(Collectors.toMap((stock)->stock.getTalle().getTalle(), (stock)->stock));

        Iterator<Stock> stockIterator = stock.iterator();
        boolean todosItemsValidos = true;

        while(stockIterator.hasNext()){
            Stock stockItemActual = stockIterator.next();
            Stock stockItemAntiguo = stockAntiguoMap.get(stockItemActual.getTalle().getTalle());
            if(stockItemAntiguo == null) continue;
            if(!stockItemAntiguo.getTalle().estaActivo() || !stockItemAntiguo.estaActivo()){
                stockIterator.remove();
                todosItemsValidos = false;
            }
        }
        return todosItemsValidos;
    }
    public boolean validarImagenesAModificar(List<ImagenProducto> imagenesPersistidas){
        HashSet<String> urisImagenesAModificar = imagenes.stream()
                .map(ImagenProducto::getUri).collect(Collectors.toCollection(HashSet::new));
        for(ImagenProducto imagenProducto: imagenesPersistidas){
            String uri = imagenProducto.getUri();
            if(!urisImagenesAModificar.contains(uri)) return false;
        }
        return true;
    }
    public boolean verificarStockExistente(Stock stockAux){
        if(stockAux == null) return false;
        if(stockAux.getTalle() == null) throw new DomainException("No es posible utilizar un item de stock sin especificar el talle");
        if(stock == null || stock.isEmpty()) return false;
        for(Stock stockItem: stock){
            if(stockItem.getTalle().getTalle().toUpperCase().equals(stockAux.getTalle().getTalle().toUpperCase())) return true;
        }
        return false;
    }

    public Stock registrarBajaStock(String talle) {
        if (stock == null || stock.isEmpty()) return null;
        for (Stock stockItem : stock) {
            if (stockItem.getTalle().getTalle().equals(talle)) {
                stockItem.registrarBaja();
                return stockItem;
            }
        }
        return null;
    }
    public Stock buscarItemStock(String talle){
        return stock.stream().filter(stockItem -> stockItem.esDeTalle(talle)).findFirst().orElse(null);
    }

}
