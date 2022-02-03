package emma.galzio.goodenergysports.productos.commons.mapper;

import emma.galzio.goodenergysports.productos.admin.transferObject.ImagenProductoDto;
import emma.galzio.goodenergysports.productos.commons.domain.ImagenProducto;
import emma.galzio.goodenergysports.utils.mapper.BusinessToDtoMapper;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImagenProductoBusinessToDtoMapper implements BusinessToDtoMapper<ImagenProductoDto, ImagenProducto> {

    @Autowired
    private ServletContext servletContext;

    @Override
    public ImagenProductoDto mapToDto(ImagenProducto business) {
        if(business == null) throw new NullPointerException("La imágen no puede ser nula");

        String rootPath = servletContext.getContextPath().concat("/resources/images/producto");
        ImagenProductoDto imagenProductoDto = new ImagenProductoDto();
        String imageName = FilenameUtils.getName(business.getRutaArchivo());
        imagenProductoDto.setUri(rootPath.concat("/").concat(imageName));
        imagenProductoDto.setOrden(business.getOrden());

        return imagenProductoDto;
    }



    @Override
    public List<ImagenProductoDto> mapAllToDto(List<ImagenProducto> businessList) {
        if(businessList == null || businessList.isEmpty()) throw new NullPointerException("La lista de imágenes no puede estar vacía");
        return businessList.stream().map(this::mapToDto).collect(Collectors.toList());
    }

}
