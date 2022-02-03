package emma.galzio.goodenergysports.utils;

import emma.galzio.goodenergysports.utils.exception.ApiError;
import emma.galzio.goodenergysports.utils.exception.DomainException;
import emma.galzio.goodenergysports.utils.exception.FileStorageException;
import emma.galzio.goodenergysports.utils.exception.PersistenceException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {DomainException.class, FileStorageException.class})
    protected ResponseEntity<Object> handleDomainException(RuntimeException ex,
                                                             WebRequest request){

        if(ex instanceof DomainException) {
            DomainException domainException = (DomainException) ex;

            HttpStatus status = HttpStatus.BAD_REQUEST;
            if (domainException.getStatus() != null) {
                status = domainException.getStatus();
            }
            ApiError apiError = new ApiError(status, domainException.getCauses(), domainException.getMessage());
            return handleExceptionInternal(domainException, apiError, new HttpHeaders(), status, request);
        }
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String message = "Ocurrió un error al almacenar las imagenes correspondientes al producto";
        ApiError apiError = new ApiError(status, message);
        return handleExceptionInternal(ex, apiError, new HttpHeaders(), status,request);
    }

    @ExceptionHandler(value = {PersistenceException.class})
    protected ResponseEntity<Object> handleGenericException(RuntimeException ex, WebRequest request){

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String message = ex.getMessage();
        ApiError apiError = new ApiError(status,message);
        return handleExceptionInternal(ex,apiError, new HttpHeaders(), status, request);

    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, WebRequest request) {
        Class<?> type = e.getRequiredType();
        String message;
        if(type.isEnum()){
            message = "The parameter " + e.getName() + " must have a value among : " + StringUtils.join(Arrays.stream(type.getEnumConstants()).iterator(), ", ");
        }
        else{
            message = "The parameter " + e.getName() + " must be of type " + type.getTypeName();
        }
        ApiError apiError = new ApiError(HttpStatus.UNPROCESSABLE_ENTITY, message);

        return handleExceptionInternal(e,apiError,new HttpHeaders(),HttpStatus.UNPROCESSABLE_ENTITY, request);
    }
}
