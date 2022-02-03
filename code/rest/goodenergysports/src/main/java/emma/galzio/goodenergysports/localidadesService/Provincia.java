package emma.galzio.goodenergysports.localidadesService;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Provincia {

    @JsonProperty("nombre_completo")
    private String nombreCompleto;
    @JsonProperty("fuente")
    private String fuente;
    @JsonProperty("iso_id")
    private String isoId;
    @JsonProperty("nombre")
    private String nombre;
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("categoria")
    private String categoria;
    @JsonProperty("iso_nombre")
    private String isoNombre;
    @JsonProperty("centroide")
    private Centroide centroide;


}
