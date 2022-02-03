package emma.galzio.goodenergysports.security.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import emma.galzio.goodenergysports.security.service.RefreshTokenService;
import emma.galzio.goodenergysports.utils.exception.ApiError;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Map;

@RestController
@RequestMapping("${goodEnergy.api.refresh-token}")
@RequiredArgsConstructor
public class RefreshTokenRestController {

    private final RefreshTokenService refreshTokenService;

    @GetMapping
    @SneakyThrows
    public void refrehToken(HttpServletRequest request, HttpServletResponse response){

        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        try {
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                response.setHeader("ERROR", "Authorization header not found");
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
            }
            Map<String, String> tokens = refreshTokenService.refreshToken(authorizationHeader, request.getRequestURL().toString());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(),tokens);
        }catch(Exception e){
            ApiError apiError = new ApiError();
            apiError.addCause("ERROR", "Ocurrió un error al verificar el refresh token");
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(),apiError);
        }

    }

}
