package emma.galzio.goodenergysports.security.discovering;

import emma.galzio.goodenergysports.security.domain.Permiso;
import emma.galzio.goodenergysports.security.persistence.entity.PermisoEntity;
import emma.galzio.goodenergysports.security.persistence.repository.PermisoRepository;
import emma.galzio.goodenergysports.security.persistence.repository.RolUsuarioRepository;
import emma.galzio.goodenergysports.security.utils.mapper.PermisoEntityMapper;
import emma.galzio.goodenergysports.utils.mapper.BusinessToEntityMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.reflections.scanners.Scanners.TypesAnnotated;

@RequiredArgsConstructor
@Slf4j
public class ReflectionResourcesDiscoveringService implements ResourcesDiscoveringService {

    private final PermisoRepository permisoRepository;
    private final BusinessToEntityMapper<PermisoEntity, Permiso> permisoBusinessToEntityMapper;

    @SneakyThrows
    @Override
    public List<Permiso> descubrirRecursosExpuestos(String basePath) {

        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder()
                .forPackage("emma.galzio.goodenergysports")
                .filterInputsBy(new FilterBuilder().includePackage("emma.galzio.goodenergysports")
                        .excludePackage("emma.galzio.goodenergysports.security.api"));
        //Reflections reflections = new Reflections("emma.galzio.goodenergysports");
        Reflections reflections = new Reflections(configurationBuilder);
        Set<Class<?>> endpoints = reflections.get(TypesAnnotated.with(RestController.class).asClass());
        List<Permiso> permisos = new ArrayList<>();

        for (Class<?> endpoint : endpoints) {
            log.info("Rest Controller: {}", endpoint.getName());
            Annotation requestMappingAnnotation = endpoint.getAnnotation(RequestMapping.class);
            Method requestMappingAnnotationValueMethod = requestMappingAnnotation.
                    annotationType().getMethod("value");

            String[] endpointBasePaths = (String[]) requestMappingAnnotationValueMethod
                    .invoke(requestMappingAnnotation);
            if(Arrays.stream(endpointBasePaths).anyMatch(path -> path.startsWith("${"))) continue;
            log.info("Base paths de endpoint: {}", Arrays.toString(endpointBasePaths));
            Method[] endpointMethods = endpoint.getMethods();

            for (Method method : endpointMethods) {
                Class<? extends RequestMapping> requestMappingClass = RequestMapping.class.asSubclass(RequestMapping.class);

                String[] methodLevelUrls = null;
                RequestMethod[] methodLevelHttpVerbs = null;
                boolean isRequestMethod = false;

                Annotation methodRequestMappingAnnotation = method
                        .getAnnotation(requestMappingClass);
                if (methodRequestMappingAnnotation != null) {
                    //Si methodRequestMappingAnnotation != null, quiere decir que el método está directamente
                    //anotado con RequestMApping, entonces directamente obtengo las uri y los métodos desde esa annotation
                    Method valueMethod = methodRequestMappingAnnotation.annotationType().getMethod("value");
                    Method httpVerbsMethods = methodRequestMappingAnnotation.annotationType().getMethod("method");
                    methodLevelUrls = (String[]) valueMethod.invoke(methodRequestMappingAnnotation);
                    methodLevelHttpVerbs = (RequestMethod[]) httpVerbsMethods.invoke(methodRequestMappingAnnotation);
                    isRequestMethod = true;
                } else {
                    //Si no, quiere decir que está anotado con GetMapping o alguna de esas,
                    //para eso obtengo los paths de la annotation directa, y el método desde RequestMaping
                    Annotation[] methodAnnotations = method.getDeclaredAnnotations();
                    for (Annotation methodAnnotation : methodAnnotations) {
                        if (methodAnnotation.annotationType().isAnnotationPresent(requestMappingClass)) {
                            Method valueMethod = methodAnnotation.annotationType().getMethod("value");
                            methodLevelUrls = (String[]) valueMethod.invoke(methodAnnotation);
                            Annotation superRequestMappingAnnotation = methodAnnotation.annotationType().getAnnotation(RequestMapping.class);
                            Method httpVerbsMethods = superRequestMappingAnnotation.annotationType().getMethod("method");
                            methodLevelHttpVerbs = (RequestMethod[]) httpVerbsMethods.invoke(superRequestMappingAnnotation);
                            isRequestMethod = true;
                            break;
                        }
                    }
                }
                if (!isRequestMethod) continue;
                permisos.addAll(this.createMethodPermissions(endpointBasePaths,methodLevelUrls,methodLevelHttpVerbs));
            }
        }
        return permisos;
    }

    private List<Permiso> createMethodPermissions(String [] endpointPaths, String[] methodLevelPaths, RequestMethod[] requestMethods){

        List<Permiso> methodPermissions = new ArrayList<>();
        for(String endpointPath: endpointPaths){
            List<Permiso> pathPermissions = methodLevelPaths.length==0 ?
                    this.createPermissionsForEmptyMethodPath(endpointPath,requestMethods) :
                    this.createMethodPermissionsForMultipleMethodPath(endpointPath, methodLevelPaths, requestMethods);
            methodPermissions.addAll(pathPermissions);
        }
        return methodPermissions;
    }

    private List<Permiso> createMethodPermissionsForMultipleMethodPath(String endpointPath, String[] methodLevelPaths, RequestMethod[] requestMethods) {

        List<Permiso> methodPermissions = new ArrayList<>();
        if(endpointPath.endsWith("/"))endpointPath = endpointPath.substring(0,endpointPath.length()-1);
        for(String methodLevelPath: methodLevelPaths){
            for(RequestMethod requestMethod: requestMethods){
                if(methodLevelPath.endsWith("/")) methodLevelPath = methodLevelPath.substring(0, methodLevelPaths.length-1);
                String path = endpointPath+methodLevelPath;
                methodPermissions.add(new Permiso(path, requestMethod.name(),null));
            }
        }
        return methodPermissions;
    }

    private List<Permiso> createPermissionsForEmptyMethodPath(String endpointPath, RequestMethod[] requestMethods){
        List<Permiso> methodPermissions = new ArrayList<>();
        for(RequestMethod method: requestMethods){
            methodPermissions.add(new Permiso(endpointPath, method.name(),null));
        }
        return methodPermissions;
    }

    @Transactional
    public List<Permiso> almacenarPermisosDeRecursosExpuestos(String basePath){

        List<Permiso> permisos = this.descubrirRecursosExpuestos("/api");
        List<PermisoEntity> permisosEntity = permisoBusinessToEntityMapper.mapAllToEntity(permisos);
        permisosEntity = permisoRepository.saveAll(permisosEntity);
        return permisos;
    }

    @Override
    public List<Permiso> establecerPermisosAdmin(RolUsuarioRepository rolRepository, PermisoEntityMapper permisoEntityMapper, String basePath) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
