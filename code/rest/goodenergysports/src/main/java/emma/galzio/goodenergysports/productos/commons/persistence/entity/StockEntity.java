package emma.galzio.goodenergysports.productos.commons.persistence.entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "stock")
public class StockEntity {

    @EmbeddedId
    private StockEntityId id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumns({
            @JoinColumn(name = "talle", referencedColumnName = "talle"),
            @JoinColumn(name = "categoria_talle", referencedColumnName = "categoria_producto")
    })
    @MapsId("talleId")
    private TalleEntity talle;

    @MapsId("producto")
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "producto", referencedColumnName = "codigo_producto", insertable = false, updatable = false)
    private ProductoEntity producto;

    @Column
    private Integer stockDisponible;
    @Column
    private LocalDate fechaBaja;

    public StockEntityId getId() {
        return id;
    }

    public void setId(StockEntityId id) {
        this.id = id;
    }

    public TalleEntity getTalle() {
        return talle;
    }

    public void setTalle(TalleEntity talle) {
        this.talle = talle;
    }

    public Integer getStockDisponible() {
        return stockDisponible;
    }

    public void setStockDisponible(Integer stock) {
        this.stockDisponible = stock;
    }

    public ProductoEntity getProducto() {
        return producto;
    }

    public void setProducto(ProductoEntity producto) {
        this.producto = producto;
    }

    public LocalDate getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(LocalDate fechaBaja) {
        this.fechaBaja = fechaBaja;
    }
}
