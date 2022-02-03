package emma.galzio.goodenergysports.clientes.persistence.entity;

import javax.persistence.*;

@Entity
@Table(name = "localidad")
public class LocalidadEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_localidad", nullable = false)
    private Integer idLocalidad;

    @Column(name = "nombre", nullable = false, length = 60)
    private String nombre;

    @Column(name = "codigo_postal")
    private Integer codigoPostal;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "provincia", nullable = false, insertable = true)
    private ProvinciaEntity provincia;

    public ProvinciaEntity getProvincia() {
        return provincia;
    }

    public void setProvincia(ProvinciaEntity provincia) {
        this.provincia = provincia;
    }

    public Integer getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(Integer codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getIdLocalidad() {
        return idLocalidad;
    }

    public void setIdLocalidad(Integer id) {
        this.idLocalidad = id;
    }
}