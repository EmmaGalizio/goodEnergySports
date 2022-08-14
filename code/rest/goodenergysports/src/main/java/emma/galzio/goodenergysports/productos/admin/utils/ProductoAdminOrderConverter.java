package emma.galzio.goodenergysports.productos.admin.utils;

import emma.galzio.goodenergysports.productos.admin.controller.ProductoAdminService;
import org.springframework.core.convert.converter.Converter;

public class ProductoAdminOrderConverter implements Converter<String, ProductoAdminService.ORDER> {
    @Override
    public ProductoAdminService.ORDER convert(String sOrder) {

        try{
            return ProductoAdminService.ORDER.valueOf(sOrder);
        }catch(Exception e){
            return null;
        }
    }
}
