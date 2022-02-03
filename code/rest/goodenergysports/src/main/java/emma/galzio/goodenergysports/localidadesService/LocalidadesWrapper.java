package emma.galzio.goodenergysports.localidadesService;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocalidadesWrapper {

    @JsonProperty("municipios")
    private List<Localidad> localidades;
    @JsonProperty("cantidad")
    private Integer cantidad;
    @JsonProperty("inicio")
    private Integer inicio;
    @JsonProperty("parametros")
    private List<String> parametros;
    @JsonProperty("total")
    private Integer total;



}
