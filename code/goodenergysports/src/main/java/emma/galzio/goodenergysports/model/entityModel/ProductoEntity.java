package emma.galzio.goodenergysports.model.entityModel;


import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.sql.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;

@Entity
@Table(name = "producto", schema = "goodenergysport")
public class ProductoEntity {
    @Id
    @Column(name = "codigoProducto")
    private Integer codigoProducto;
    @Basic
    @Column(name = "nombre")
    private String nombre;
    @Basic
    @Column(name = "descripcion")
    private String descripcion;
    @Basic
    @Column(name = "precio")
    private Double precio;
    @Basic
    @Column(name = "fechaAlta")
    private Date fechaAlta;
    @Basic
    @Column(name = "fechaBaja")
    private Date fechaBaja;

    @ManyToOne
    @JoinColumn(name = "categoria")
    private CategoriaEntity categoria;

    @OneToMany(mappedBy = "producto")
    @LazyCollection(LazyCollectionOption.FALSE) //En lugar de usar un fetch type eager, se usa esta annotation, jpa e hibernate no permiten multiples colecciones con fetch type eager
    private List<StockEntity> stock;

    @OneToMany(mappedBy = "producto", fetch = FetchType.EAGER)
    private List<ImagenproductoEntity> imagenes;


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

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Date getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(Date fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public CategoriaEntity getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaEntity categoria) {
        this.categoria = categoria;
    }

    public List<StockEntity> getStock() {
        return stock;
    }

    public void setStock(List<StockEntity> stock) {
        this.stock = stock;
    }

    public List<ImagenproductoEntity> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<ImagenproductoEntity> imagenes) {
        this.imagenes = imagenes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductoEntity that = (ProductoEntity) o;
        return Objects.equals(codigoProducto, that.codigoProducto) &&
                Objects.equals(nombre, that.nombre) &&
                Objects.equals(fechaAlta, that.fechaAlta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigoProducto, nombre, descripcion, precio, fechaAlta, fechaBaja);
    }

    @Override
    public String toString() {

        StringBuffer sb = new StringBuffer("");
        sb.append("Producto:\n\tCodigo: " + this.codigoProducto);
        sb.append("\n\tNombre: " + this.nombre);
        sb.append("\n\tPrecio: " + this.precio);
        if(stock != null){
            sb.append("\n\tStock Disponible:");
            stock.forEach((stockEntity -> sb.append("\n\t\t"+ stockEntity)));
        }

        return sb.toString();
    }
}
