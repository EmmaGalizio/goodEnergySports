package emma.galzio.goodenergysports.clientes.persistence.entity;

import javax.persistence.*;

@Entity
@Table(name = "domicilio")
public class DomicilioEntity {
    @Id
    @Column(name = "id_domicilio", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDomicilio;

    @Column(name = "calle", nullable = false, length = 100)
    private String calle;

    @Column(name = "numero", nullable = false)
    private Integer numero;

    @Column(name = "piso")
    private Integer piso;

    @Column(name = "dpto", length = 5)
    private String dpto;

    @Column(name = "referencia", length = 200)
    private String referencia;

    @ManyToOne
    @JoinColumn(name = "localidad",updatable = false, insertable = false)
    private LocalidadEntity localidad;

    public LocalidadEntity getLocalidad() {
        return localidad;
    }

    public void setLocalidad(LocalidadEntity localidad) {
        this.localidad = localidad;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getDpto() {
        return dpto;
    }

    public void setDpto(String dpto) {
        this.dpto = dpto;
    }

    public Integer getPiso() {
        return piso;
    }

    public void setPiso(Integer piso) {
        this.piso = piso;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public Integer getIdDomicilio() {
        return idDomicilio;
    }

    public void setIdDomicilio(Integer id) {
        this.idDomicilio = id;
    }
}