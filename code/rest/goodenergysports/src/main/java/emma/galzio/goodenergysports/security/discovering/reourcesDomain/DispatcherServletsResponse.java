package emma.galzio.goodenergysports.security.discovering.reourcesDomain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.boot.actuate.web.mappings.servlet.DispatcherServletMappingDescription;
import org.springframework.boot.actuate.web.mappings.servlet.DispatcherServletMappingDetails;
import org.springframework.boot.actuate.web.mappings.servlet.RequestMappingConditionsDescription;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class DispatcherServletsResponse {

    private List<DispatcherServletMappingDescription> dispatcherServlet;

    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Dispatcher Servlets:\n");
        for(DispatcherServletMappingDescription description: dispatcherServlet){
            stringBuilder.append("\tDispatcher Servlet:\n");
            stringBuilder.append("\t\tHandler: ").append(description.getHandler());
            stringBuilder.append("\t\tPredicate: ").append(description.getPredicate());
            DispatcherServletMappingDetails dispatcherServletMappingDetails = description.getDetails();
            Set<RequestMethod> methods = dispatcherServletMappingDetails.getRequestMappingConditions().getMethods();
            Set<String> patterns = dispatcherServletMappingDetails.getRequestMappingConditions().getPatterns();
            List<RequestMappingConditionsDescription.MediaTypeExpressionDescription> consumes = dispatcherServletMappingDetails.getRequestMappingConditions().getConsumes();
            List<RequestMappingConditionsDescription.MediaTypeExpressionDescription> produces = dispatcherServletMappingDetails.getRequestMappingConditions().getProduces();

            stringBuilder.append("\t\tPatterns:\n");
            patterns.forEach(pattern -> stringBuilder.append("\t\t\t").append(pattern).append("\n"));
            stringBuilder.append("\t\tMethods\n");
            methods.forEach(method-> stringBuilder.append("\t\t\t").append(method.toString()).append("\n"));
            stringBuilder.append("\t\tConsumes:\n");
            consumes.forEach(consume -> stringBuilder.append("\t\t\t").append(consume).append("\n"));
            stringBuilder.append("\t\tProduces\n");
            produces.forEach(produce-> stringBuilder.append("\t\t\t").append(produce.toString()).append("\n"));
        }
        return stringBuilder.toString();
    }
}
