package emma.galzio.goodenergysports.productos.commons.mapper;

import emma.galzio.goodenergysports.productos.commons.domain.ImagenProducto;
import emma.galzio.goodenergysports.productos.commons.persistence.entity.ImagenProductoEntity;
import emma.galzio.goodenergysports.utils.mapper.EntityMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("imagenProductoEntityMapper")
public class ImagenPRoductoEntityMapper implements EntityMapper<ImagenProductoEntity, ImagenProducto> {

    @Override
    public ImagenProductoEntity mapToEntity(ImagenProducto business) {
        ImagenProductoEntity imagenProductoEntity = new ImagenProductoEntity();
        imagenProductoEntity.setOrden(business.getUriOrder());
        imagenProductoEntity.setUrl(business.getRutaArchivo());
        return  imagenProductoEntity;
    }

    @Override
    public ImagenProducto mapToBusiness(ImagenProductoEntity entity) {
        ImagenProducto imagenProducto = new ImagenProducto();
        imagenProducto.setOrden(entity.getOrden());
        imagenProducto.setRutaArchivo(entity.getUrl());
        return imagenProducto;
    }

    @Override
    public List<ImagenProductoEntity> mapAllToEntity(List<ImagenProducto> businessList) {
        return businessList.stream().map(this::mapToEntity).collect(Collectors.toList());
    }

    @Override
    public List<ImagenProducto> mapAllToBusiness(List<ImagenProductoEntity> entityList) {
        return entityList.stream().map(this::mapToBusiness).collect(Collectors.toList());
    }
}
