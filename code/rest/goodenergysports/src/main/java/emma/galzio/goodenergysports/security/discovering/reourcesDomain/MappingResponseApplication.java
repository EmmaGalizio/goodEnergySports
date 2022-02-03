package emma.galzio.goodenergysports.security.discovering.reourcesDomain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MappingResponseApplication {

    private EndpointMappings mappings;
    private String parentId;
}
