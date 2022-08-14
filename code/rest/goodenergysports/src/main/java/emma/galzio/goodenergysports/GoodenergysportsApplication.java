package emma.galzio.goodenergysports;


import emma.galzio.goodenergysports.clientes.domain.Usuario;
import emma.galzio.goodenergysports.clientes.persistence.entity.LocalidadEntity;
import emma.galzio.goodenergysports.clientes.persistence.entity.ProvinciaEntity;
import emma.galzio.goodenergysports.clientes.persistence.entity.UsuarioEntity;
import emma.galzio.goodenergysports.clientes.persistence.repository.LocalidadEntityRepository;
import emma.galzio.goodenergysports.localidadesService.Localidad;
import emma.galzio.goodenergysports.localidadesService.LocalidadesIndecParser;
import emma.galzio.goodenergysports.localidadesService.Provincia;
import emma.galzio.goodenergysports.productos.admin.initializationController.ProductoInitializerController;
import emma.galzio.goodenergysports.productos.admin.initializationController.CategoriaInitializationController;
import emma.galzio.goodenergysports.productos.admin.transferObject.CategoriaAdminDto;
import emma.galzio.goodenergysports.security.discovering.ApplicationContextResourcesDiscoveringService;
import emma.galzio.goodenergysports.security.discovering.ResourcesDiscoveringService;
import emma.galzio.goodenergysports.security.domain.Permiso;
import emma.galzio.goodenergysports.security.domain.RolUsuario;
import emma.galzio.goodenergysports.security.persistence.entity.RolUsuarioEntity;
import emma.galzio.goodenergysports.security.persistence.repository.PermisoRepository;
import emma.galzio.goodenergysports.security.persistence.repository.RolUsuarioRepository;
import emma.galzio.goodenergysports.security.persistence.repository.UsuarioRepository;
import emma.galzio.goodenergysports.security.utils.mapper.PermisoBusinessToEntityMapper;
import emma.galzio.goodenergysports.security.utils.mapper.PermisoEntityMapper;
import emma.galzio.goodenergysports.utils.exception.DomainException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@SpringBootApplication
@Slf4j
public class GoodenergysportsApplication {

    @Autowired
    private ApplicationContext context;
    @Autowired
    private PermisoRepository permisoRepository;
    @Autowired
    private PermisoBusinessToEntityMapper permisoEntityMapper;
    @Value("${server.servlet.context-path}")
    private String basePath;
    @Autowired
    private RolUsuarioRepository rolRepository;
    @Autowired
    private PermisoEntityMapper permisoEntityToBusinessMapper;
    @Autowired
    private LocalidadEntityRepository localidadRepository;
    @Autowired
    private Environment environment;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CategoriaInitializationController categoriaInitializationController;
    @Autowired
    private ProductoInitializerController productoInitializerController;

    public static void main(String[] args) {
        SpringApplication.run(GoodenergysportsApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void applicationReadyListener(){
        //this.almacenarLocalidades();

        //this.cargarEndpointsExpuestos();
        //this.cargarEndpointsExpuestos();
        this.verificarUsuarioAdmin();
        //this.inicializarCategorias();
        //productoInitializerController.loadProductosFromJson("/json/productos.json");

    }

    private void inicializarCategorias(){
        List<CategoriaAdminDto> categorias = categoriaInitializationController
                                .loadCategoriasFromJson("/json/categorias.json");
        categorias = categoriaInitializationController.persistCategorias(categorias);
        categorias.forEach(System.out::println);
    }

    //No importa el Listener que ponga, los permisos se cargan antes de que se exponga algun
    //endpoint
    //La unica forma seria cambiar el DiscoveringServices al de reflection
    //@EventListener(ApplicationStartedEvent.class)
    //public void obtenerEndpointsApplicationContext(){
     //   this.cargarEndpointsExpuestos();
    //}

    @Transactional
    public void almacenarLocalidades(){
        LocalidadesIndecParser localidadesIndecParser = new LocalidadesIndecParser();
        //List<Provincia> provincias = localidadesIndecParser.
        //       parseProvincias("/json/provincias.json");
        List<Localidad> localidades = localidadesIndecParser.parseLocalidades("/json/municipios.json");

        //List<ProvinciaEntity> provinciaEntities = provincias.stream()
        //       .map(GoodenergysportsApplication::mapToProvinciaEntity).collect(Collectors.toList());
        //provinciaEntityRepository.saveAll(provinciaEntities);
        log.info("Provincias almacenadas");
        Map<Integer,ProvinciaEntity> provinciasMap = new HashMap<>();
        List<LocalidadEntity> localidadEntities = localidades.stream()
                .map((localidad)-> this.mapToLocalidadEntity(localidad,provinciasMap))
                .collect(Collectors.toList());
        localidadRepository.saveAll(localidadEntities);
        log.info("Localidades almacenadas");
    }

    private void cargarEndpointsExpuestos(){
        if(Arrays.asList(environment.getActiveProfiles()).contains("test")) return;
        //No me gusta el nombre del servicio
        ResourcesDiscoveringService resourcesDiscoveringService = new ApplicationContextResourcesDiscoveringService(context,permisoRepository,permisoEntityMapper);
        //List<Permiso> permisos = resourcesDiscoveringService.almacenarPermisosDeRecursosExpuestos(basePath);
        List<Permiso> permisos = resourcesDiscoveringService.establecerPermisosAdmin(rolRepository,permisoEntityToBusinessMapper, basePath);
        if(permisos == null) return;
        System.out.println("------------------------");
        System.out.println("Permisos Almacenados:");
        permisos.forEach(System.out::println);

    }

    private void verificarUsuarioAdmin(){

        RolUsuarioEntity rolUsuario = rolRepository.findByRol("ADMIN");
        List<UsuarioEntity> usuariosAdmin = usuarioRepository.findByRol(rolUsuario);
        if(usuariosAdmin == null || usuariosAdmin.isEmpty()){
            System.out.println("Aún no se ha configurado un usuario con permisos de administrador");
            System.out.println("Se procederá a crear un nuevo usuario administrador");
            Scanner scanner = new Scanner(System.in);
            boolean usuarioCorrecto;
            do {
                System.out.println("Ingrese el nombre de usuario:");
                String username = scanner.nextLine();
                System.out.println("Ingrese el email");
                String email = scanner.nextLine();
                System.out.println("Ingrese contraseña:");
                String password = scanner.nextLine();

                Usuario usuario = new Usuario();
                usuario.setUsuario(username);
                usuario.setEmail(email);
                usuario.setPassword(password);
                usuario.setFechaAlta(LocalDate.now());
                usuario.setRol(new RolUsuario("ADMIN"));

                usuarioCorrecto = false;
                try {
                    usuarioCorrecto = usuario.validar() && usuario.validarPassword();
                    usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
                    UsuarioEntity usuarioEntity = new UsuarioEntity();
                    usuarioEntity.setUsuario(usuario.getUsuario());
                    usuarioEntity.setEmail(usuario.getEmail());
                    usuarioEntity.setPassword(usuario.getPassword());
                    usuarioEntity.setFechaAlta(usuario.getFechaAlta());
                    usuarioEntity.setRol(rolUsuario);
                    usuarioRepository.save(usuarioEntity);
                    System.out.println("Se registró correctamente al usuario administrador");

                } catch (DomainException d) {
                    System.out.println("Ocurrieron errores al validar al usuario:");
                    for(String causa: d.getCauses().values()){
                        System.out.println(causa);
                    }
                }
            }while(!usuarioCorrecto);
        }

    }


    //Este código es horrible, si en algún momento pasa a producción es necesario implementar un servicio
    //que permita leer las localidades del archivo y almacenarlas
    /*@Bean
    @Transactional
    CommandLineRunner runner(ApplicationContext context, PermisoRepository permisoRepository, PermisoBusinessToEntityMapper permisoEntityMapper){
        return args ->{


            LocalidadesIndecParser localidadesIndecParser = new LocalidadesIndecParser();
            //List<Provincia> provincias = localidadesIndecParser.
             //       parseProvincias("/json/provincias.json");
            List<Localidad> localidades = localidadesIndecParser.parseLocalidades("/json/municipios.json");

            //List<ProvinciaEntity> provinciaEntities = provincias.stream()
             //       .map(GoodenergysportsApplication::mapToProvinciaEntity).collect(Collectors.toList());
            //provinciaEntityRepository.saveAll(provinciaEntities);
            log.info("Provincias almacenadas");
            Map<Integer,ProvinciaEntity> provinciasMap = new HashMap<>();
            List<LocalidadEntity> localidadEntities = localidades.stream()
                    .map((localidad)-> this.mapToLocalidadEntity(localidad,provinciasMap))
                    .collect(Collectors.toList());
            localidadRepository.saveAll(localidadEntities);
            log.info("Localidades almacenadas");

        };

    }*/

    public ProvinciaEntity mapToProvinciaEntity(Provincia provincia){
        ProvinciaEntity provinciaEntity = new ProvinciaEntity();
        provinciaEntity.setId(provincia.getId());
        provinciaEntity.setNombre(provincia.getIsoNombre().trim().toUpperCase());
        return provinciaEntity;
    }
    public LocalidadEntity mapToLocalidadEntity(Localidad localidad, Map<Integer,ProvinciaEntity> provinciasMap){
        LocalidadEntity localidadEntity = new LocalidadEntity();
        //localidadEntity.setIdLocalidad(localidad.getId());
        localidadEntity.setNombre(localidad.getNombre().trim().toUpperCase());
        Integer idProvincia = localidad.getProvincia().getId();
        String nombreProvincia = localidad.getProvincia().getNombre().trim().toUpperCase();
        ProvinciaEntity provinciaEntity;
        if(provinciasMap.get(idProvincia) != null){
            provinciaEntity = provinciasMap.get(idProvincia);
        } else{
            provinciaEntity = new ProvinciaEntity(idProvincia,nombreProvincia);
            provinciasMap.put(idProvincia,provinciaEntity);
        }
        localidadEntity.setProvincia(provinciaEntity);
        return localidadEntity;
    }


}
