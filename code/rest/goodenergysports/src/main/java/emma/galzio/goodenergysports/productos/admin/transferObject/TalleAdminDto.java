package emma.galzio.goodenergysports.productos.admin.transferObject;


import org.springframework.hateoas.RepresentationModel;
import java.time.LocalDate;

public class TalleAdminDto extends RepresentationModel<TalleAdminDto> {

    private String talle;
    private String equivalencia;
    private LocalDate fechaBaja;

    public String getTalle() {
        return talle;
    }

    public void setTalle(String talle) {
        this.talle = talle;
    }

    public String getEquivalencia() {
        return equivalencia;
    }

    public void setEquivalencia(String equivalencia) {
        this.equivalencia = equivalencia;
    }

    public LocalDate getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(LocalDate fechaBaja) {
        this.fechaBaja = fechaBaja;
    }
}
