package emma.galzio.goodenergysports.clientes.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Localidad {

    @NonNull
    private Integer idLocalidad;
    @NonNull
    private String nombre;
    private Integer codigoPostal;
    @NonNull
    private Provincia provincia;

}
