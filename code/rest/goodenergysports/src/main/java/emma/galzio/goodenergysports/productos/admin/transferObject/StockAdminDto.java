package emma.galzio.goodenergysports.productos.admin.transferObject;

import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;

public class StockAdminDto extends RepresentationModel<StockAdminDto> {

    private Integer cantidadDisponible;
    private TalleAdminDto talle;
    private LocalDate fechaBaja;


    public Integer getCantidadDisponible() {
        return cantidadDisponible;
    }


    public void setCantidadDisponible(Integer cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }

    public TalleAdminDto getTalle() {
        return talle;
    }

    public void setTalle(TalleAdminDto talle) {
        this.talle = talle;
    }

    public LocalDate getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(LocalDate fechaBaja) {
        this.fechaBaja = fechaBaja;
    }
}
