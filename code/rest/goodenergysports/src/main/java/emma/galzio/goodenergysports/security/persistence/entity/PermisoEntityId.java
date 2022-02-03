package emma.galzio.goodenergysports.security.persistence.entity;

import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PermisoEntityId implements Serializable {
    private static final long serialVersionUID = -1260423401546457665L;
    @Column(name = "url", nullable = false, length = 100)
    private String url;
    @Column(name = "metodo", nullable = false, length = 10)
    private String metodo;

    public PermisoEntityId() {
    }

    public PermisoEntityId(String url, String metodo) {
        this.url = url;
        this.metodo = metodo;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int hashCode() {
        return Objects.hash(metodo, url);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PermisoEntityId entity = (PermisoEntityId) o;
        return Objects.equals(this.metodo, entity.metodo) &&
                Objects.equals(this.url, entity.url);
    }
}