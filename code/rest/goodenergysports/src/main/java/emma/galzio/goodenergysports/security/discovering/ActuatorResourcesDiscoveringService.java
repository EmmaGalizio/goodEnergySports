package emma.galzio.goodenergysports.security.discovering;

import emma.galzio.goodenergysports.security.discovering.reourcesDomain.ResorusesActuatorResponse;
import emma.galzio.goodenergysports.security.domain.Permiso;
import emma.galzio.goodenergysports.security.persistence.repository.PermisoRepository;
import emma.galzio.goodenergysports.security.persistence.repository.RolUsuarioRepository;
import emma.galzio.goodenergysports.security.utils.mapper.PermisoBusinessToEntityMapper;
import emma.galzio.goodenergysports.security.utils.mapper.PermisoEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.List;

@RequiredArgsConstructor
public class ActuatorResourcesDiscoveringService implements ResourcesDiscoveringService {

    private final PermisoRepository permisoRepository;
    private final PermisoBusinessToEntityMapper permisoBusinessToEntityMapper;

    //http://localhost:8080/api/actuator/mappings

    @Override
    /***
     * No se terminó nunca porque es más eficiente el enfoque con DispatcherServletsMappingDescriptionProvider
     * utilizando el ApplicationContext
     */
    public List<Permiso> descubrirRecursosExpuestos(String basePath) {

        WebClient webClient = WebClient.create("http://localhost:8080");
        WebClient.UriSpec<WebClient.RequestBodySpec> uriSpec = webClient.method(HttpMethod.GET);
        WebClient.RequestBodySpec bodySpec = uriSpec.uri("/api/actuator/mappings");
        WebClient.RequestHeadersSpec<?> headersSpec = bodySpec.bodyValue("");
        WebClient.ResponseSpec responseSpec = headersSpec.accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8).ifNoneMatch("*").ifModifiedSince(ZonedDateTime.now())
                .retrieve();

        /*Mono<String> monoResponse = headersSpec.exchangeToMono(response -> {
            if(response.statusCode().equals(HttpStatus.OK)){
                return response.bodyToMono(String.class);
            } else if(response.statusCode().is4xxClientError() ){
                return Mono.just("Error Response");
            } else{
                return response.createException().flatMap(Mono::error);
            }

        });*/
        Mono<ResorusesActuatorResponse> monoResponse = headersSpec.retrieve().bodyToMono(ResorusesActuatorResponse.class);
        //Mono<String> monoResponse = headersSpec.retrieve().bodyToMono(String.class);
        //String monoString = monoResponse.block();
        //System.out.println("Respuesta de actuator");
        //System.out.println(monoString);
        System.out.println("Respuesta del actuator:");
        System.out.println(monoResponse.block().toString());

        return null;
    }

    @Override
    public List<Permiso> almacenarPermisosDeRecursosExpuestos(String basePath) {
        return null;
    }

    @Override
    public List<Permiso> establecerPermisosAdmin(RolUsuarioRepository rolRepository, PermisoEntityMapper permisoEntityMapper,String basePath) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
