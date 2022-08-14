package emma.galzio.goodenergysports.security.config;

import emma.galzio.goodenergysports.security.domain.Permiso;
import emma.galzio.goodenergysports.security.domain.RolUsuario;
import emma.galzio.goodenergysports.security.filter.CustomAuthenticationFilter;
import emma.galzio.goodenergysports.security.filter.CustomAuthorizationFilter;
import emma.galzio.goodenergysports.security.persistence.repository.PermisoRepository;
import emma.galzio.goodenergysports.security.service.PermisosService;
import emma.galzio.goodenergysports.security.utils.JWTUtils;
import emma.galzio.goodenergysports.security.utils.mapper.PermisoEntityMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtils jwtUtils;
    private final PermisoRepository permisoRepository;

    @Value("${goodEnergy.api.login-url}")
    private String loginPath;
    @Value("${goodEnergy.api.refresh-token}")
    private String refreshTokenPath;
    @Value("${server.servlet.context-path}")
    private String basePath;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable(); //Desactiva cross site recuest forgery (falsificación de petición en sitios cruzados)
        CustomAuthenticationFilter customAuthenticationFilter
                                = new CustomAuthenticationFilter(authenticationManagerBean(),jwtUtils);
        customAuthenticationFilter.setFilterProcessesUrl("/login");

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        log.info("Comenzando a cargar permisos desde DDBB");
        //Se mapean los permisos a partir de los que están almacenados en la base de datos.
        //Se utiliza como POJO en vez de bean porque se van a utilizar solo una vez, para ahorrar memoria
        PermisoEntityMapper permisoEntityMapper = new PermisoEntityMapper();
        PermisosService permisosService = new PermisosService(permisoRepository,permisoEntityMapper);

        List<Permiso> permisos = permisosService.cargarPermisos();
        for(Permiso permiso:permisos){
            if(permiso.getRoles() != null && !permiso.getRoles().isEmpty()) {
                for (RolUsuario rol : permiso.getRoles()) {
                    http.authorizeRequests().antMatchers(HttpMethod.resolve(permiso.getMetodo()),
                            permiso.getUrl()).hasAuthority(rol.getRol());
                }
            }else{
                http.authorizeRequests().antMatchers(HttpMethod.resolve(permiso.getMetodo()),
                        permiso.getUrl()).permitAll();
            }
        }
        http.authorizeRequests().antMatchers("/login/**", refreshTokenPath).permitAll();
        //http.authorizeRequests().antMatchers(HttpMethod.GET,"/admin/producto/**")
        //        .hasAuthority("ADMIN");
        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(jwtUtils,loginPath,refreshTokenPath),
                                                    UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }



}
