package emma.galzio.goodenergysports.security.persistence.repository;

import emma.galzio.goodenergysports.clientes.persistence.entity.UsuarioEntity;
import emma.galzio.goodenergysports.security.persistence.entity.RolUsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, String> {
    UsuarioEntity findByEmailIgnoreCase(String email);

    List<UsuarioEntity> findByRol(RolUsuarioEntity rol);

    UsuarioEntity findByUsuario(String usuario);




}