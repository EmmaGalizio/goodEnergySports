package emma.galzio.goodenergysports.security.service;

import emma.galzio.goodenergysports.clientes.domain.Usuario;
import emma.galzio.goodenergysports.security.UsuarioService;
import emma.galzio.goodenergysports.security.utils.JWTUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final JWTUtils jwtUtils;
    private final UsuarioService usuarioService;

    public Map<String,String> refreshToken(String authorizationHeader, String issuer){

        String username = jwtUtils.getTokenUsername(authorizationHeader);
        Usuario usuario = usuarioService.buscarUsuario(username);
        User user = new User(usuario.getUsuario(),usuario.getPassword(),usuario.estaActivo(),
                true, true, true,
                Collections.singleton(new SimpleGrantedAuthority(usuario.getRol().getRol())));
        String accessToken = jwtUtils.generateAccessToken(user, issuer);
        String refreshToken = jwtUtils.generateRefreshToken(user, issuer);

        Map<String,String> tokens = new HashMap<>();
        tokens.put("access_token",accessToken);
        tokens.put("refresh_token",refreshToken);
        return  tokens;
    }
}
