package emma.galzio.goodenergysports.security.persistence.repository;

import emma.galzio.goodenergysports.security.persistence.entity.RolUsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolUsuarioRepository extends JpaRepository<RolUsuarioEntity, Integer> {

    RolUsuarioEntity findByRol(String rol);
}