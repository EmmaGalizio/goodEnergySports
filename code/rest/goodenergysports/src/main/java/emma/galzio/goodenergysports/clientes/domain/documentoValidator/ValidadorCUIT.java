package emma.galzio.goodenergysports.clientes.domain.documentoValidator;

import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component("CUIT")
public class ValidadorCUIT implements IValidadorDocumento {

    private final String[]prefijos = {"20","23","24","25","26","27","30"};

    @Override
    public boolean validarDocumento(String documento, int longitud) {
        if(!documento.matches("^(\\d{"+(longitud-1)+","+longitud+"})$")) return false;
        if(!Arrays.stream(prefijos).anyMatch((prefix) -> documento.substring(0,2).equals(prefix))) return false;
        return true;
    }
}
