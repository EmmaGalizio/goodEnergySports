package emma.galzio.goodenergysports.clientes.domain.documentoValidator;

import org.springframework.stereotype.Component;

@Component("DNI")
public class ValidadorDNI implements IValidadorDocumento {

    @Override
    public boolean validarDocumento(String documento, int longitud) {
        if(documento == null) return false;
        return documento.matches("^(\\d{7,"+longitud+"})$");
    }
}
