package emma.galzio.goodenergysports.model.TransferModel;

public class StockDto {

    private TalleDto talle;
    private Integer cantDisponible;

    public TalleDto getTalle() {
        return talle;
    }

    public void setTalle(TalleDto talle) {
        this.talle = talle;
    }

    public Integer getCantDisponible() {
        return cantDisponible;
    }

    public void setCantDisponible(Integer cantDisponible) {
        this.cantDisponible = cantDisponible;
    }
}
