package emma.galzio.goodenergysports.security.discovering.reourcesDomain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.boot.actuate.web.mappings.servlet.DispatcherServletMappingDetails;

@Data
//@JsonIgnoreProperties(ignoreUnknown = true)
public class ResorusesActuatorResponse {

    private MappingResponseContext contexts;


}
