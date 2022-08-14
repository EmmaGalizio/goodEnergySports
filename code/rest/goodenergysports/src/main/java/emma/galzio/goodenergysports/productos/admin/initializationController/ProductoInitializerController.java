package emma.galzio.goodenergysports.productos.admin.initializationController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import emma.galzio.goodenergysports.localidadesService.LocalidadesWrapper;
import emma.galzio.goodenergysports.productos.admin.controller.ProductoAdminService;
import emma.galzio.goodenergysports.productos.admin.initializationController.CategoriaAdminDtoWrapper;
import emma.galzio.goodenergysports.productos.admin.transferObject.CategoriaAdminDto;
import emma.galzio.goodenergysports.productos.admin.transferObject.ProductoAdminDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductoInitializerController {

    private final ProductoAdminService productoAdminService;

    public List<ProductoAdminDto> inicializarProductos(List<ProductoAdminDto> productos){

        if(productos == null || productos.isEmpty()) throw new NullPointerException("Debe proporcionar una lista de productos válidos");

        return productos.stream().map(productoAdminService::createNewProducto).collect(Collectors.toList());

    }

    public List<ProductoAdminDto> loadProductosFromJson(String relativePath){

        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<ProductoAdminDtoWrapper> typeReference = new TypeReference<>() {};
        InputStream inputStream = TypeReference.class.getResourceAsStream(relativePath);
        try{
            ProductoAdminDtoWrapper productoAdminDtoWrapper = objectMapper.readValue(inputStream, typeReference);
            return inicializarProductos(productoAdminDtoWrapper.getProductos());
        }catch(IOException e){
            e.printStackTrace();
            System.out.println("Ocurrió un error al cargar las provincias");
        }
        return null;
    }

}
