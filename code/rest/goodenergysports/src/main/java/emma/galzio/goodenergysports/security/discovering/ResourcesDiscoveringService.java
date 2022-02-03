package emma.galzio.goodenergysports.security.discovering;

import emma.galzio.goodenergysports.security.domain.Permiso;
import emma.galzio.goodenergysports.security.persistence.repository.RolUsuarioRepository;
import emma.galzio.goodenergysports.security.utils.mapper.PermisoEntityMapper;

import java.util.List;

public interface ResourcesDiscoveringService {

    List<Permiso> descubrirRecursosExpuestos(String basePath);
    List<Permiso> almacenarPermisosDeRecursosExpuestos(String basePath);
    List<Permiso> establecerPermisosAdmin(RolUsuarioRepository rolRepository, PermisoEntityMapper permisoEntityMapper,String basePath);

}
