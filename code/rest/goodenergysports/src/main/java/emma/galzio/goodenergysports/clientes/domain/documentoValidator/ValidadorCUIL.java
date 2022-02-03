package emma.galzio.goodenergysports.clientes.domain.documentoValidator;

import org.springframework.stereotype.Component;

@Component("CUIL")
public class ValidadorCUIL implements IValidadorDocumento {

    private final int[] multiplicadores = {5,4,3,2,7,6,5,4,3,2};

    @Override
    public boolean validarDocumento(String documento, int longitud) {
        if(documento == null) return false;
        if(!documento.matches("^(\\d{10,"+longitud+"})$")) return false;
        String[] partes = documento.length() < longitud ? this.normalizarCuil(documento) :
                                                            this.obtenerPartes(documento);
        String prefDni = partes[0]+partes[1];
        int productoEscalar = 0;
        for(int i = 0; i < multiplicadores.length ; i++){
            productoEscalar += multiplicadores[i]* Character.getNumericValue(prefDni.charAt(i));
        }
        int resto = productoEscalar % longitud;
        return Integer.parseInt(partes[2]) == (longitud-resto);
    }

    private String[] normalizarCuil(String cuil){
        String prefijo = cuil.substring(0,2);
        String dni = "0"+cuil.substring(2,cuil.length()-1);
        String codigo = cuil.substring(cuil.length()-1);
        return new String[]{prefijo,dni,codigo};
    }
    private String[] obtenerPartes(String cuil){
        String prefijo = cuil.substring(0,2);
        String dni = cuil.substring(2,cuil.length()-1);
        String codigo = cuil.substring(cuil.length()-1);
        return new String[]{prefijo,dni,codigo};
    }
}
