package emma.galzio.goodenergysports.localidadesService;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProvinciasWrapper {

    @JsonProperty("provincias")
    private List<Provincia> provincias;
    @JsonProperty("total")
    private Integer total;
    @JsonProperty("cantidad")
    private Integer cantidad;
    @JsonProperty("parametros")
    private List<String> parametros;
    @JsonProperty("inicio")
    private Integer inicio;

}
