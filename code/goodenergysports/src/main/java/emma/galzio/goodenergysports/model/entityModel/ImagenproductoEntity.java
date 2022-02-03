package emma.galzio.goodenergysports.model.entityModel;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "imagenproducto", schema = "goodenergysport", catalog = "")
public class ImagenproductoEntity {

    @Id
    @Column(name = "idimagenProducto")
    private Integer idimagenProducto;

    @Basic
    @Column(name = "urlImagen")
    private String urlImagen;

    @ManyToOne
    @JoinColumn(name = "producto")
    private ProductoEntity producto;

    public Integer getIdimagenProducto() {
        return idimagenProducto;
    }

    public void setIdimagenProducto(Integer idimagenProducto) {
        this.idimagenProducto = idimagenProducto;
    }


    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImagenproductoEntity that = (ImagenproductoEntity) o;
        return Objects.equals(idimagenProducto, that.idimagenProducto) &&
                Objects.equals(urlImagen, that.urlImagen);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idimagenProducto, urlImagen);
    }
}
