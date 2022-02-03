package emma.galzio.goodenergysports.model.businessModel;

public class Stock {

    private Talle talle;
    private Integer cantDisponible;

    public Talle getTalle() {
        return talle;
    }

    public void setTalle(Talle talle) {
        this.talle = talle;
    }

    public Integer getCantDisponible() {
        return cantDisponible;
    }

    public void setCantDisponible(Integer cantDisponible) {
        this.cantDisponible = cantDisponible;
    }
}
