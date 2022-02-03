package emma.galzio.goodenergysports.productos.admin.controller;

import emma.galzio.goodenergysports.productos.admin.transferObject.ImagenProductoDto;
import emma.galzio.goodenergysports.productos.admin.transferObject.ProductoAdminDto;
import emma.galzio.goodenergysports.productos.commons.domain.Producto;
import emma.galzio.goodenergysports.productos.commons.persistence.entity.ProductoEntity;

import java.util.List;

public interface IProductoAdminService {
    ProductoAdminDto createNewProducto(ProductoAdminDto producto);

    List<ProductoAdminDto> listAllProductos(boolean active, Integer page, Integer size, ProductoAdminService.ORDER order, Integer categoria);

    ProductoAdminDto findProducto(Integer codigo);

    ProductoAdminDto productoWithdraw(Integer codigo);

    List<ImagenProductoDto> listAllImages(Integer codigo);

    List<ImagenProductoDto> deleteImage(Integer codigoProducto, Integer orden);

    ProductoAdminDto updateProducto(ProductoAdminDto productoAdminDto, Integer codigo);

    Integer countProducts(boolean active, Integer categoria);

    ImagenProductoDto findProductImage(Integer codigo, Integer orden);

    Producto buscarProductoBusiness(Integer codigo);
    ProductoEntity buscarProductoEntity(Integer codigo);
}
