package emma.galzio.goodenergysports.security.persistence.repository;

import emma.galzio.goodenergysports.security.persistence.entity.PermisoEntity;
import emma.galzio.goodenergysports.security.persistence.entity.PermisoEntityId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PermisoRepository extends JpaRepository<PermisoEntity, PermisoEntityId> {
    List<PermisoEntity> findById_Url(String url);
}