package emma.galzio.goodenergysports.configuration;

import emma.galzio.goodenergysports.productos.admin.utils.CategoriaSortConverter;
import emma.galzio.goodenergysports.productos.admin.utils.ProductoAdminOrderConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class CustomWebConfigurationSupport extends WebMvcConfigurationSupport {

    @Override
    public FormattingConversionService mvcConversionService() {
        FormattingConversionService f = super.mvcConversionService();
        f.addConverter(new ProductoAdminOrderConverter());
        f.addConverter(new CategoriaSortConverter());
        return f;
    }
}
