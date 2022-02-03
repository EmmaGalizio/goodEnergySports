package emma.galzio.goodenergysports.utils.exception;

import org.springframework.http.HttpStatus;

import java.util.Hashtable;
import java.util.Map;

/****
 * Maps all the exceptions caused by business restrictions
 * Offer the possibillity of create a Map<String,String> wich contains as
 * key the name of the attribute that causes the error in UPPERCASE, and as a value
 * contais a message
 */
public class DomainException extends RuntimeException {

    private Map<String, String> causes;
    private HttpStatus status;

    public DomainException(String message) {
        super(message);
    }
    public DomainException(){
        super();
    }

    public void addCause(String key, String value){
        if(key == null || key.isEmpty() || value == null || value.isEmpty()) return;

        if(causes == null) causes = new Hashtable<>();
        causes.put(key, value);
    }

    public Map<String, String> getCauses() {
        return causes;
    }

    public void setCauses(Map<String, String> causes) {
        if(causes == null) throw new NullPointerException();
        if(causes.isEmpty()) throw new IllegalArgumentException("causes can't be empty");
        this.causes = causes;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}
