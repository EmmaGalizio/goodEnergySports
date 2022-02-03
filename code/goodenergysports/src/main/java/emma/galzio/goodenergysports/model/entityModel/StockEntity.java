package emma.galzio.goodenergysports.model.entityModel;

import javax.persistence.*;

@Entity
@Table(name = "stock")
public class StockEntity {

    @EmbeddedId
    private StockEntityId stockEntityId;

    @MapsId("producto")
    @ManyToOne
    @JoinColumn(name = "producto", referencedColumnName = "codigoProducto")
    private ProductoEntity producto;

    @MapsId("talle")
    @ManyToOne
    @JoinColumn(name = "talle", referencedColumnName = "idtalle")
    private TalleEntity talle;

    @Column(name="stockDisponible")
    private Integer stockDisponible;

    public StockEntityId getStockEntityId() {
        return stockEntityId;
    }

    public void setStockEntityId(StockEntityId stockEntityId) {
        this.stockEntityId = stockEntityId;
    }

    public ProductoEntity getProducto() {
        return producto;
    }

    public void setProducto(ProductoEntity producto) {
        this.producto = producto;
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

    public void setStockDisponible(Integer stockDisponible) {
        this.stockDisponible = stockDisponible;
    }

    @Override
    public String toString() {
        return "StockEntity:\n\tTalle: " + talle.toString() + "\n\tCantidad Disp: " + this.stockDisponible;
    }
}
