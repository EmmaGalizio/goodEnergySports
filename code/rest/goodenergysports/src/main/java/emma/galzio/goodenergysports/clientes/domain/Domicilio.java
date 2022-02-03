package emma.galzio.goodenergysports.clientes.domain;


import emma.galzio.goodenergysports.utils.exception.DomainException;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class Domicilio {

    @NonNull
    private String calle;
    @NonNull
    private Integer numero;
    private Integer piso;
    private String dpto;
    private String referencia;
    private Localidad localidad;

    public boolean validar(){
        Map<String, String> causas = new HashMap<>();
        if(calle == null || calle.isEmpty() || calle.length() > 100){
            causas.put("CALLE", "La calle del domicilio no puede estar vacía ni contener más de 100 caracteres");
        }
        if(dpto != null && dpto.length() > 5){
            causas.put("DPTO", "El departamento de puede contener más de 5 caracteres");
        }
        if(referencia !=null && referencia.length()>200){
            causas.put("REFERENCIA", "La referencia del domicilio es demasiado extensa");
        }
        //La localidad se valida en el mapper
        if(!causas.isEmpty()){
            DomainException domainException = new DomainException();
            domainException.setCauses(causas);
            throw domainException;
        }
        return true;

    }

}
