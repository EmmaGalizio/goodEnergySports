package emma.galzio.goodenergysports.clientes.transferObject;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class UsuarioDto extends RepresentationModel<UsuarioDto> {

    private String email;
    private String usuario;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private LocalDate fechaAlta;
    private LocalDate fechaBaja;

}
