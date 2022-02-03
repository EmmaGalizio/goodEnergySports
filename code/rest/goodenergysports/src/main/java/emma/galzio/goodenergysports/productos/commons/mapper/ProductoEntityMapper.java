package emma.galzio.goodenergysports.productos.commons.mapper;

import emma.galzio.goodenergysports.productos.commons.domain.ImagenProducto;
import emma.galzio.goodenergysports.productos.commons.domain.Producto;
import emma.galzio.goodenergysports.productos.commons.domain.Stock;
import emma.galzio.goodenergysports.productos.commons.domain.Talle;
import emma.galzio.goodenergysports.productos.commons.persistence.entity.*;
import emma.galzio.goodenergysports.productos.commons.persistence.repository.CategoriaEntityRepository;
import emma.galzio.goodenergysports.productos.commons.persistence.repository.ProductoRepository;
import emma.galzio.goodenergysports.utils.exception.DomainException;
import emma.galzio.goodenergysports.utils.mapper.EntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service("productoEntityMapper")
public class ProductoEntityMapper implements EntityMapper<ProductoEntity, Producto> {

    @Autowired
    private CategoriaEntityMapper categoriaEntityMapper;
    @Autowired
    private CategoriaEntityRepository categoriaEntityRepository;
    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public ProductoEntity mapToEntity(Producto business) {

        if(business == null) throw new NullPointerException("El producto no puede estar vacío");
        if(business.getStock() == null || business.getStock().isEmpty()) throw new NullPointerException("El producto debe indicar al menos un talle disponible");
        ProductoEntity productoEntity = new ProductoEntity();
        productoEntity.setCodigoProducto(business.getCodigo());
        if(productoEntity.getCodigoProducto() == null){
            Integer lastID = productoRepository.getLastId();
            lastID = lastID != null ? lastID+1 : 1;
            productoEntity.setCodigoProducto(lastID);

        }
        productoEntity.setNombre(business.getNombre());
        productoEntity.setDescripcion(business.getDescripcion());
        productoEntity.setFechaAlta(business.getFechaAlta());
        productoEntity.setFechaBaja(business.getFechaBaja());

        CategoriaEntity categoriaEntity = categoriaEntityRepository.getById(business.getCategoria().getIdCategoria());
        productoEntity.setCategoria(categoriaEntity);

        this.setEntityImagenes(business,productoEntity);
        productoEntity.setPrecio(new BigDecimal(business.getPrecio().toString()));

        this.setEntityStock(business,productoEntity);
        this.setEntityMetadata(business,productoEntity);
        return productoEntity;
    }

    private void setEntityImagenes(Producto business, ProductoEntity productoEntity){
        if(business.getImagenes() != null && !business.getImagenes().isEmpty()){
            List<ImagenProductoEntity> imagenesEntity = business.getImagenes().stream()
                    .map( imageBusiness-> {ImagenProductoEntity ipe = new ImagenProductoEntity();
                        ipe.setUrl(imageBusiness.getRutaArchivo());
                        ipe.setProducto(productoEntity);
                        ipe.setOrden(imageBusiness.getOrden());
                        return ipe;}).collect(Collectors.toList());
            productoEntity.setImagenes(imagenesEntity);
        }
    }

    private void setEntityStock(Producto business, ProductoEntity productoEntity){
        List<StockEntity> stockEntityList = new ArrayList<>();
        for(Stock stock: business.getStock()){

            TalleEntity talleEntity = new TalleEntity();
            TalleEntityId talleEntityId = new TalleEntityId();
            talleEntityId.setCategoriaProducto(business.getCategoria().getIdCategoria());
            talleEntityId.setTalle(stock.getTalle().getTalle());

            talleEntity.setId(talleEntityId);
            talleEntity.setCategoria(productoEntity.getCategoria());
            talleEntity.setEquivalencia(stock.getTalle().getEquivalencia());

            StockEntity stockEntity = new StockEntity();
            StockEntityId stockEntityId = new StockEntityId();
            stockEntityId.setProducto(business.getCodigo());
            stockEntityId.setTalleId(talleEntityId);

            stockEntity.setId(stockEntityId);
            stockEntity.setProducto(productoEntity);
            stockEntity.setStockDisponible(stock.getStockDisponible());
            stockEntity.setTalle(talleEntity);
            stockEntity.setFechaBaja(stock.getFechaBaja());
            stockEntityList.add(stockEntity);

        }
        productoEntity.setStock(stockEntityList);
    }

    private void setEntityMetadata(Producto business, ProductoEntity productoEntity){
        if(business.getMetadata() != null && !business.getMetadata().isEmpty()){
            List<ProductoMetadataEntity> productoMetadataEntityList = new ArrayList<>();
            business.getMetadata().forEach((k,v) -> {
                ProductoMetadataEntityId productoMetadataEntityId = new ProductoMetadataEntityId();
                productoMetadataEntityId.setClave(k);
                productoMetadataEntityId.setProducto(business.getCodigo());

                ProductoMetadataEntity productoMetadataEntity = new ProductoMetadataEntity();
                productoMetadataEntity.setId(productoMetadataEntityId);
                productoMetadataEntity.setProducto(productoEntity);
                productoMetadataEntity.setValor(v);
                productoMetadataEntityList.add(productoMetadataEntity);
            });
            productoEntity.setProductoMetadata(productoMetadataEntityList);
        }
    }

    @Override
    public Producto mapToBusiness(ProductoEntity entity) {

        Producto producto = new Producto();
        producto.setCodigo(entity.getCodigoProducto());
        producto.setNombre(entity.getNombre());
        producto.setDescripcion(entity.getDescripcion());
        producto.setFechaAlta(entity.getFechaAlta());
        producto.setFechaBaja(entity.getFechaBaja());
        producto.setPrecio(entity.getPrecio().floatValue());
        this.setBusinessStock(producto, entity);
        producto.setCategoria(categoriaEntityMapper.mapToBusiness(entity.getCategoria()));
        this.setBusinessImages(producto, entity);
        this.setBusinesMetadata(producto,entity);

        return producto;
    }

    private void setBusinesMetadata(Producto business, ProductoEntity entity){
        if(entity.getProductoMetadata() == null) return;

        Map<String, String> metadata = new Hashtable<>();
        for(ProductoMetadataEntity metadataEntity: entity.getProductoMetadata()){
            metadata.put(metadataEntity.getId().getClave(), metadataEntity.getValor());
        }
        business.setMetadata(metadata);
    }

    private void setBusinessImages(Producto business, ProductoEntity entity){
        List<ImagenProducto> imagenProductoList = new LinkedList<>();
        for(ImagenProductoEntity imagenProductoEntity: entity.getImagenes()){
            ImagenProducto imagenProducto = new ImagenProducto();
            imagenProducto.setRutaArchivo(imagenProductoEntity.getUrl());
            imagenProducto.setOrden(imagenProductoEntity.getOrden());
            imagenProductoList.add(imagenProducto);
        }
        business.setImagenes(imagenProductoList);
    }

    private void setBusinessStock(Producto business, ProductoEntity entity){
        List<Stock> stockList = new LinkedList<>();
        for(StockEntity stockEntity: entity.getStock()){
            Stock stock = new Stock();
            stock.setStockDisponible(stockEntity.getStockDisponible());
            stock.setFechaBaja(stockEntity.getFechaBaja());
            Talle talle = new Talle();
            talle.setIdCategoriaTalle(entity.getCategoria().getIdCategoria());
            talle.setEquivalencia(stockEntity.getTalle().getEquivalencia());
            talle.setTalle(stockEntity.getTalle().getId().getTalle());
            stock.setTalle(talle);
            stockList.add(stock);
        }
        business.setStock(stockList);
    }

    @Override
    public List<ProductoEntity> mapAllToEntity(List<Producto> businessList) {
        return null;
    }

    @Override
    public List<Producto> mapAllToBusiness(List<ProductoEntity> entityList) {
        if(entityList == null || entityList.isEmpty()) throw  new DomainException("No existen Productos con las características especificadas");
        return entityList.stream().map(this::mapToBusiness).collect(Collectors.toList());
    }
}
