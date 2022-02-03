package emma.galzio.goodenergysports.security;

import emma.galzio.goodenergysports.clientes.domain.Usuario;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailServiceImpl implements UserDetailsService {

    private final UsuarioService usuarioService;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        log.info("Nombre de usuario Detal Service: {}",s);
        Usuario usuario = usuarioService.buscarUsuario(s);
        if(usuario == null) throw new UsernameNotFoundException("Usuario incorrecto");
        log.info("Usuario:");
        log.info(usuario.toString());
        //El usuario debería tener un solo rol
        Collection<SimpleGrantedAuthority> authorities = Collections
                                        .singleton(new SimpleGrantedAuthority(usuario.getRol().getRol()));

        return new User(usuario.getUsuario(),usuario.getPassword(),usuario.estaActivo(),true,
                                                    true,true,authorities);
    }
}
