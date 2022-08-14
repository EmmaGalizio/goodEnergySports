package emma.galzio.goodenergysports.productos.admin.utils;

import emma.galzio.goodenergysports.productos.client.controller.ICategoriaController;
import org.springframework.core.convert.converter.Converter;

public class CategoriaSortConverter implements Converter<String, ICategoriaController.SORT> {
    @Override
    public ICategoriaController.SORT convert(String sort) {
        try{
            return ICategoriaController.SORT.valueOf(sort);
        }catch(Exception e){
            return ICategoriaController.SORT.DEFAULT;
        }
    }
}
