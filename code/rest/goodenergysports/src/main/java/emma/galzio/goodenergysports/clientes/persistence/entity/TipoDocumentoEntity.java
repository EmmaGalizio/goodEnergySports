package emma.galzio.goodenergysports.clientes.persistence.entity;

import javax.persistence.*;

@Entity
@Table(name = "tipo_documento")
public class TipoDocumentoEntity {

    @Id
    @Column(name = "tipo_documento", nullable = false, length = 20)
    private String tipo;

    @Column(name = "longitud")
    private Integer longitud;

    public Integer getLongitud() {
        return longitud;
    }

    public void setLongitud(Integer longitud) {
        this.longitud = longitud;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipoDocumento) {
        this.tipo = tipoDocumento;
    }

}