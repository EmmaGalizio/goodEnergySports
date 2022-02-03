package emma.galzio.goodenergysports.utils.exception;

import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ApiError implements Serializable {

    private HttpStatus status;
    private Map<String,String> causes;
    private String message;

    public ApiError(HttpStatus status, Map<String, String> causes, String message) {
        this.status = status;
        this.causes = causes;
        this.message = message;
    }

    public ApiError(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public ApiError(HttpStatus status, Map<String, String> causes) {
        this.status = status;
        this.causes = causes;
    }

    public ApiError() {
    }

    public ApiError(String message) {
        this.message = message;
    }

    public void addCause(String cause, String message){
        if(causes == null) causes = new HashMap<>();
        causes.put(cause, message);
    }

    public HttpStatus getStatus() {
        return status;
    }

    public Map<String, String> getCauses() {
        return causes;
    }

    public String getMessage() {
        return message;
    }
}
