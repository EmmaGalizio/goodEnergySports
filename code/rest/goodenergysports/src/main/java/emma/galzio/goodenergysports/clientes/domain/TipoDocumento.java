package emma.galzio.goodenergysports.clientes.domain;

import emma.galzio.goodenergysports.clientes.domain.documentoValidator.IValidadorDocumento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TipoDocumento {

    private String tipo;
    private Integer longitud;
    @NonNull
    private IValidadorDocumento validadorDocumento;

    public boolean validar(String documento){
        return validadorDocumento.validarDocumento(documento,longitud);
    }
}
