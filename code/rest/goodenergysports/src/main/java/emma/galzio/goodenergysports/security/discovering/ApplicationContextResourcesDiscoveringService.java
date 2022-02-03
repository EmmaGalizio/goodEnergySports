package emma.galzio.goodenergysports.security.discovering;

import emma.galzio.goodenergysports.security.domain.Permiso;
import emma.galzio.goodenergysports.security.domain.RolUsuario;
import emma.galzio.goodenergysports.security.persistence.entity.PermisoEntity;
import emma.galzio.goodenergysports.security.persistence.entity.RolUsuarioEntity;
import emma.galzio.goodenergysports.security.persistence.repository.PermisoRepository;
import emma.galzio.goodenergysports.security.persistence.repository.RolUsuarioRepository;
import emma.galzio.goodenergysports.security.utils.mapper.PermisoBusinessToEntityMapper;
import emma.galzio.goodenergysports.security.utils.mapper.PermisoEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.web.mappings.servlet.DispatcherServletMappingDescription;
import org.springframework.boot.actuate.web.mappings.servlet.DispatcherServletsMappingDescriptionProvider;
import org.springframework.boot.actuate.web.mappings.servlet.RequestMappingConditionsDescription;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.*;

@RequiredArgsConstructor
public class ApplicationContextResourcesDiscoveringService implements ResourcesDiscoveringService{

    private final ApplicationContext applicationContext;
    private final PermisoRepository permisoRepository;
    private final PermisoBusinessToEntityMapper permisoEntityMapper;

    @Override
    public List<Permiso> descubrirRecursosExpuestos(String basePath) {

        DispatcherServletsMappingDescriptionProvider dispatcherServletsMappingDescriptionProvider =
                new DispatcherServletsMappingDescriptionProvider();
        Map<String,List<DispatcherServletMappingDescription>> mappingDescriptions =
                dispatcherServletsMappingDescriptionProvider.describeMappings(applicationContext);

        List<Permiso> permisos = new LinkedList<>();

        for(DispatcherServletMappingDescription description: mappingDescriptions.get("dispatcherServlet")){

                if(description.getDetails() != null) {
                    RequestMappingConditionsDescription conditionsDescription = description.getDetails()
                                                                            .getRequestMappingConditions();
                    Set<String> patterns = conditionsDescription.getPatterns();
                    Set<RequestMethod> methods = conditionsDescription.getMethods();
                    for(String pattern: patterns){
                        for(RequestMethod method: methods){
                            Permiso permiso = new Permiso(pattern,method.name(),null);
                            permisos.add(permiso);
                        }
                    }
                }
            }
        return permisos;
    }

    @Override
    @Transactional
    public List<Permiso> almacenarPermisosDeRecursosExpuestos(String basePath) {
        List<Permiso> permisos = this.descubrirRecursosExpuestos(basePath);
        List<PermisoEntity> permisoEntities = permisoRepository
                .saveAll(permisoEntityMapper.mapAllToEntity(permisos));
        return permisos;
    }

    @Override
    @Transactional
    public List<Permiso> establecerPermisosAdmin(RolUsuarioRepository rolRepository, PermisoEntityMapper permisoEntityMapper, String basePath) {

        List<Permiso> permisos = this.descubrirRecursosExpuestos(basePath);
        if(permisos == null || permisos.isEmpty()) return null;

        List<PermisoEntity> permisoEntities = this.permisoEntityMapper.mapAllToEntity(permisos);
        RolUsuarioEntity rolAdmin = rolRepository.findByRol("ADMIN");
        for(PermisoEntity permisoEntity : permisoEntities){
            if(permisoEntity.getId().getUrl().startsWith("/admin")|| permisoEntity.getId().getUrl().startsWith("/actuator")){
                if(permisoEntity.getRoles() == null) permisoEntity.setRoles(new LinkedList<>());
                if(!permisoEntity.getRoles().contains(rolAdmin)){
                    permisoEntity.getRoles().add(rolAdmin);
                }
            }
        }
        System.out.println("Permisos antes de salvar: ");
     permisoEntityMapper.mapAllToBusiness(permisoEntities).forEach(System.out::println);
        permisoEntities = permisoRepository.saveAllAndFlush(permisoEntities);
        permisos = permisoEntityMapper.mapAllToBusiness(permisoEntities);
        return permisos;
    }
}
