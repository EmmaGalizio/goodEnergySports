package emma.galzio.goodenergysports.security.discovering.reourcesDomain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.boot.actuate.web.mappings.servlet.DispatcherServletMappingDescription;
import org.springframework.boot.actuate.web.mappings.servlet.RequestMappingConditionsDescription;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DispatcherServletMapping {

    private String handler;
    private String predicate;
    private RequestMappingConditionsDescription requestMappingConditions;
    //Van dentro de un requestMappingConditions
    private String[] params;
    private String[] methods;
    private String [] patterns;
}
