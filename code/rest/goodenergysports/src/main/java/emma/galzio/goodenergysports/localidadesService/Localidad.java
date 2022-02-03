package emma.galzio.goodenergysports.localidadesService;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Localidad {

    @JsonProperty("nombre_completo")
    private String nombreCompleto;
    private String fuente;
    private String nombre;
    private Integer id;
    private ProvinciaMunicipio provincia;
    private String categoria;
    private Centroide centroide;

}
