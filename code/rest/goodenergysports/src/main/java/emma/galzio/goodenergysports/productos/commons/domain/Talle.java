package emma.galzio.goodenergysports.productos.commons.domain;

import emma.galzio.goodenergysports.utils.exception.DomainException;

import java.time.LocalDate;
import java.util.Hashtable;
import java.util.Map;
import java.util.Objects;

public class Talle {

    private String talle;
    private String equivalencia;
    private Integer idCategoriaTalle;
    private LocalDate fechaBaja;

    public Talle() {
    }

    public Talle(String talle, String equivalencia, Integer idCategoriaTalle) {
        this.talle = talle != null ? talle.trim() : null;
        this.equivalencia = equivalencia != null ? equivalencia.trim() : null;
        this.idCategoriaTalle = idCategoriaTalle;
    }

    public String getTalle() {
        return talle;
    }

    public void setTalle(String talle) {
        this.talle = talle != null ? talle.trim().toUpperCase() : null;
    }

    public String getEquivalencia() {
        return equivalencia != null ? equivalencia.trim() : null;
    }

    public void setEquivalencia(String equivalencia) {
        this.equivalencia = equivalencia;
    }

    public Integer getIdCategoriaTalle() {
        return idCategoriaTalle;
    }

    public void setIdCategoriaTalle(Integer idCategoriaTalle) {
        this.idCategoriaTalle = idCategoriaTalle;
    }

    public LocalDate getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(LocalDate fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public boolean estaActivo(){
        return fechaBaja == null;
    }

    public boolean isCorrectCategoria(Integer idCategoria){
        return this.idCategoriaTalle == idCategoria;
    }

    public void registrarBaja(){
        fechaBaja = LocalDate.now();
    }

    public boolean validate(){

       boolean error = false;
        Map<String, String> causes = null;

        if(talle == null){
            error = true;
            causes = new Hashtable<>(7);
            causes.put("TALLE", "El talle no puede estar vacío");
        }
        if(idCategoriaTalle == null || idCategoriaTalle<=0){
            error = true;
            String errorMesage = "El talle debe corresponder a una categoria válida";

            if(causes == null) causes = new Hashtable<>();
            causes.compute("CATEGORIA",(k,v) ->
                    (v == null) ? errorMesage : v.concat(errorMesage+"\n"));
        }
        if(fechaBaja != null && fechaBaja.isAfter(LocalDate.now())){
            error = true;
            String errorMesage = "La fecha de baja no puede ser posterior a la fecha actual";

            if(causes == null) causes = new Hashtable<>();
            causes.compute("FECHA_BAJA",(k,v) ->
                    (v == null) ? errorMesage : v.concat(errorMesage+"\n"));

        }
        if(talle != null && (talle.trim().length() > 6 || talle.trim().isEmpty())){
            error = true;
            String errorMesage = "El talle debe ser un valor alfanumérico de entre 1 y 6 caracteres (No soporta simbolos)";

            if(causes == null) causes = new Hashtable<>();
            causes.compute("TALLE",(k,v) ->
                    (v == null) ? errorMesage : v.concat(errorMesage+"\n"));
        }

        if(equivalencia != null && (equivalencia.trim().length() > 45 )){
            error = true;
            if(causes== null) causes = new Hashtable<>();
            causes.put("EQUIVALENCIA_TALLE", "La equivalencia de talle debe ser alfanumérica y no puede contener más de 45 caracteres");
        }
        if(error){
            DomainException domainException = new DomainException("Ocurrió un error durante la validación del talle");
            domainException.setCauses(causes);
            throw domainException;
        }
        return true;
    }

    private boolean isAlfanumeric(String value){
        for(int i = 0; i < value.length(); i++){
            int codePoint = value.codePointAt(i);
            if(!Character.isAlphabetic(codePoint) || !Character.isDigit(codePoint) || !Character.isSpaceChar(codePoint)){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Talle talle1 = (Talle) o;
        return talle.trim().toUpperCase().equals(talle1.talle.trim().toUpperCase()) &&
                idCategoriaTalle.equals(talle1.idCategoriaTalle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(talle, idCategoriaTalle);
    }

    public void activar() {
        fechaBaja = null;
    }
}
