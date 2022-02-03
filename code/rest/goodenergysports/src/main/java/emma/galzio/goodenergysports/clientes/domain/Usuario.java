package emma.galzio.goodenergysports.clientes.domain;

import emma.galzio.goodenergysports.security.domain.RolUsuario;
import emma.galzio.goodenergysports.utils.exception.DomainException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @NonNull
    private String email;
    @NonNull
    private String usuario;
    @NonNull
    private String password;
    private LocalDate fechaAlta;
    private LocalDate fechaBaja;
    @NonNull
    private RolUsuario rol;

    public boolean validar(){
        Map<String, String> causas = new HashMap<>();

        if(!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")){
            causas.put("EMAIL", "El email ingresado no posee un formato válido");
        }
        if(usuario.isEmpty() || !usuario.matches("^(?!.*\\.\\.)(?!.*\\.$)[^\\W][\\w.]{0,100}$") ||
                                usuario.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")){
            causas.put("USUARIO", "El usuario ingresado no posee un formato válido");
        }
        if(fechaAlta.isAfter(LocalDate.now())){
            causas.put("FECHA_ALTA", "El usuario no puede ser dado de alta en una fecha posterior a su registro");
        }
        if(fechaBaja != null && fechaBaja.isAfter(LocalDate.now())){
            causas.put("FECHA_BAJA", "No es posible programar la desactivación del usuario para una fecha posterior a la actual");
        }
        if(fechaBaja != null && fechaBaja.isBefore(fechaAlta)){
            causas.put("FECHA_BAJA", "No es posible desactivar un usuario en una fecha anterior a su registro");
        }
        if(!causas.isEmpty()){
            DomainException domainException = new DomainException("Ocurrió un error al validar los datos del usuario!");
            domainException.setCauses(causas);
            throw domainException;
        }
        return true;
    }
    public boolean estaActivo(){
        return fechaBaja == null;
    }

    public boolean validarPassword(){
        Map<String, String> causas = new HashMap<>();
        if(!password.matches("^(?=.*\\d)(?=.*[a-z])(?=.*[a-zA-Z]).{8,}$")){
            causas.put("PASSWORD", "La contraseña debe contener al menos 8 caracteres, al menos una mayúscula y al menos un número");
        }
        if(!causas.isEmpty()){
            DomainException domainException = new DomainException("Ocurrió un error al validar la constraseña del usuario!");
            domainException.setCauses(causas);
            throw domainException;
        }
        return true;
    }


}
