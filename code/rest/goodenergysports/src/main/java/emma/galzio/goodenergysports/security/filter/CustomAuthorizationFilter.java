package emma.galzio.goodenergysports.security.filter;

import com.auth0.jwt.exceptions.TokenExpiredException;
import emma.galzio.goodenergysports.security.utils.JWTUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private final JWTUtils jwtUtils;
    private final String loginPath, refreshTokenPath;


    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {

        if(httpServletRequest.getServletPath().equals(loginPath) ||
            httpServletRequest.getServletPath().equals(refreshTokenPath)){
            filterChain.doFilter(httpServletRequest,httpServletResponse);
            return;
        }
        String authorizationHeader = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);

        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")){
            filterChain.doFilter(httpServletRequest,httpServletResponse);
            return;
        }            //Lo de Bearer es como una comprobación por lo que tengo entendido, nada más

        try{
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = jwtUtils.verfyToken(authorizationHeader);
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            filterChain.doFilter(httpServletRequest,httpServletResponse);

        }catch(Exception e){
            //Se puede lanzar alguna excepcion y capturarla en otro lado, o directamente
            //Se puede usar el ObjectMapper y retornar un error con un ApiError en el cuerpo
            if(e instanceof TokenExpiredException){
                httpServletResponse.setHeader("EXPIRED_TOKEN", "El token ya expiró");
            }
            httpServletResponse.setHeader("ERROR", "Ocurrió un error al autenticar al usuario "+e.getMessage());
            httpServletResponse.sendError(HttpServletResponse.SC_FORBIDDEN);

        }

    }
}
