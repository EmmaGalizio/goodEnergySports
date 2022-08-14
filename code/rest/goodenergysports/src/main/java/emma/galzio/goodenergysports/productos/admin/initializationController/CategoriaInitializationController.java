package emma.galzio.goodenergysports.productos.admin.initializationController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import emma.galzio.goodenergysports.localidadesService.ProvinciasWrapper;
import emma.galzio.goodenergysports.productos.admin.controller.CategoriaAdminService;
import emma.galzio.goodenergysports.productos.admin.transferObject.CategoriaAdminDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaInitializationController {

    private final CategoriaAdminService categoriaAdminService;

    public List<CategoriaAdminDto> loadCategoriasFromJson(String relativePath){

        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<CategoriaAdminDtoWrapper> typeReference = new TypeReference<CategoriaAdminDtoWrapper>() {};
        InputStream inputStream = TypeReference.class.getResourceAsStream(relativePath);
        try{
            CategoriaAdminDtoWrapper categoriasWrapper = objectMapper.readValue(inputStream, typeReference);
            return categoriasWrapper.getCategorias();
        }catch(IOException e){
            e.printStackTrace();
            System.out.println("Ocurrió un error al cargar las categorias");
        }
        return null;
    }

    public List<CategoriaAdminDto> persistCategorias(List<CategoriaAdminDto> categorias){
        if(categorias == null || categorias.isEmpty()) return null;
        List<CategoriaAdminDto> persistedCategories = new LinkedList<>();

        categorias.forEach((categoria) ->
                persistedCategories.add(categoriaAdminService.postNewCategoria(categoria)));
        return persistedCategories;
    }

}
