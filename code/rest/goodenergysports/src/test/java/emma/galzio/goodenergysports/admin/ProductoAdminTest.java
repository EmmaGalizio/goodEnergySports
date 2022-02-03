package emma.galzio.goodenergysports.admin;

import emma.galzio.goodenergysports.productos.admin.controller.ProductoAdminService;
import emma.galzio.goodenergysports.productos.admin.transferObject.ImagenProductoDto;
import emma.galzio.goodenergysports.productos.admin.transferObject.ProductoAdminDto;
import emma.galzio.goodenergysports.productos.commons.domain.Producto;
import emma.galzio.goodenergysports.productos.commons.mapper.ProductoEntityMapper;
import emma.galzio.goodenergysports.productos.commons.persistence.entity.*;
import emma.galzio.goodenergysports.productos.commons.persistence.repository.CategoriaEntityRepository;
import emma.galzio.goodenergysports.productos.commons.persistence.repository.ImagenProductoEntityRepository;
import emma.galzio.goodenergysports.productos.commons.persistence.repository.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import static org.assertj.core.api.Assertions.*;
@SpringBootTest
public class ProductoAdminTest {

    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private CategoriaEntityRepository categoriaEntityRepository;
    @Autowired
    private ImagenProductoEntityRepository imagenProductoEntityRepository;
    @Autowired
    private ProductoAdminService productoAdminService;
    @Autowired
    private ProductoEntityMapper productoEntityMapper;


    @Test
    @Rollback(value = false)
    @Transactional
    public void imagenProductoRepositoryTest(){

        ProductoEntity productoEntity = productoRepository.getById(13);
        Producto producto = productoEntityMapper.mapToBusiness(productoEntity);
        System.out.println("Producto: " + producto.getCodigo());
        //productoEntity.setImagenes(null);
        //DE ESTA FORMA NO EJECUTA EL DELETE, CON LAS IMAGENES CARGADAS EN EL OBJETO ENTITY
        //FUNCIONABA CUANDO TODOS LOS CAMPOS ESTABAN SETEADOS EN NULL Y LOS USABA A TRAVEZ DEL INTERCEPTOR
        //SETEAR A NULL LAS IMAGENES Y PROBAR DE NUEVO
        //ProductoEntity productoEntity = new ProductoEntity();
        //productoEntity.setCodigoProducto(13);
        //List<ImagenProductoEntity> imagenes = imagenProductoEntityRepository.deleteByProductoAndOrdenGreaterThan(productoEntity,3);
        imagenProductoEntityRepository.deleteImagenesProductoOrdenMayorQue(productoEntity,3);
        //System.out.println("Orden: " + imagenes.get(0).getOrden());
    }

    //@Test
    @Transactional
    @Rollback(value = false)
    public void testNewProducto(){

        CategoriaEntity categoriaEntity = categoriaEntityRepository.getById(1);
        Integer lastId = productoRepository.getLastId();
        if(lastId == null) lastId = 0;
        lastId++;

        ProductoEntity productoEntity = new ProductoEntity();
        productoEntity.setCodigoProducto(lastId);
        productoEntity.setNombre("Una calza negra");
        productoEntity.setDescripcion("la descripcion de la calza");
        productoEntity.setPrecio(new BigDecimal(1500));
        productoEntity.setFechaAlta(LocalDate.now());
        productoEntity.setCategoria(categoriaEntity);

        TalleEntityId talleEntityId = new TalleEntityId();
        talleEntityId.setTalle("S");

        talleEntityId.setCategoriaProducto(categoriaEntity.getIdCategoria());

        TalleEntity talle = new TalleEntity();
        talle.setId(talleEntityId);
        talle.setCategoria(categoriaEntity);
        talle.setEquivalencia("Jean 36 a 38");

        StockEntityId stockEntityId = new StockEntityId();
        stockEntityId.setTalleId(talleEntityId);
        stockEntityId.setProducto(productoEntity.getCodigoProducto());

        StockEntity stockEntity = new StockEntity();
        stockEntity.setId(stockEntityId);
        stockEntity.setTalle(talle);
        stockEntity.setStockDisponible(5);

        stockEntity.setProducto(productoEntity);

        productoEntity.setStock(Collections.singletonList(stockEntity));
        System.out.println("Se re compilo!");

        ImagenProductoEntity imagenProductoEntity = new ImagenProductoEntity();
        imagenProductoEntity.setProducto(productoEntity);
        imagenProductoEntity.setOrden(1);
        imagenProductoEntity.setUrl("una URL");

        List<ImagenProductoEntity> imagenProductoEntityList = Collections.singletonList(imagenProductoEntity);
        //imagenProductoEntityList = imagenProductoEntityRepository.saveAll(imagenProductoEntityList);
        productoEntity.setImagenes(imagenProductoEntityList);
        productoRepository.save(productoEntity);

        System.out.println("Codigo: " + productoEntity.getCodigoProducto());

    }

    //@Test
    public void testMAxCodigo(){
        Integer codigo = productoRepository.getLastId();
        //Tabla vacia: MAX = null
        System.out.println("Ultimo id: " + codigo);
    }

    //@Test
    public void listAllProductosTest(){

        ProductoAdminService.ORDER order = ProductoAdminService.ORDER.MAYOR_PRECIO;

        List<ProductoAdminDto> productos = productoAdminService.listAllProductos(true,0,3, order,1);

    }

    //@Test
    public void countActiveProductosTest(){

        CategoriaEntity categoriaEntity = categoriaEntityRepository.getById(1);
        Integer activeCount = productoRepository.countByCategoriaAndFechaBajaIsNull(categoriaEntity);

        assertThat(activeCount).isEqualTo(3);

    }

    //@Test
    public void uriOrderTest(){
        ImagenProductoDto imagenProductoDto = new ImagenProductoDto();
        imagenProductoDto.setUri("/api/admin/resources/images/producto/1-2.png");
        //assertThat(imagenProductoDto.getUriOrder()).isEqualTo(2);
    }


}
