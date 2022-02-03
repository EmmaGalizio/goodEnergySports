package emma.galzio.goodenergysports.clientes.domain;

import emma.galzio.goodenergysports.utils.exception.DomainException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Cliente {

    @EqualsAndHashCode.Include
    private Integer codigo;
    private String nombres;
    private String apellidos;
    @EqualsAndHashCode.Include
    private TipoDocumento tipoDocumento;
    @EqualsAndHashCode.Include
    private String nroDocumento;
    private String telefono;
    private Usuario usuario;
    private Domicilio domicilio;

    public boolean validar(){
        usuario.validar();
        Map<String,String> causas = new HashMap<>();
        if(nombres.isEmpty() || nombres.length() > 200){
            causas.put("NOMBRE", "El nombre del cliente no puede estar vacío ni superar los 200 caracteres");
        }
        if(apellidos.isEmpty() || nombres.length() > 200){
            causas.put("APELLIDO", "El apellido del cliente no puede estar vacío ni superar los 200 caracteres");
        }
        //A cada tipo documento le corresponde un campo que contiene un objeto que se encarga de validar
        //el número de documento siguiendo el patrón strategy, no es necesario hacer esa estructura pero
        //no cuesta nada hacerlo y no está de más
        if(!tipoDocumento.validar(nroDocumento)){
            causas.put("NRO_DOCuMENTO", "El número de documento proporcionado no tiene el formato correspondiente al tipo de documento indicado");
        }
        if(telefono != null && !telefono.matches("^(\\d{9,13})$")){
            causas.put("TELEFONO", "El número de teléfono ingresado no tiene el formate correcto");
        }
        try{
            domicilio.validar();
        }catch(DomainException d){
            causas.putAll(d.getCauses());
        }
        if(!causas.isEmpty()){
            DomainException domainException = new DomainException();
            domainException.setCauses(causas);
            throw domainException;
        }
        return true;
    }
    public boolean validarPasswordUsuario(){
        return usuario.validarPassword();
    }

}
