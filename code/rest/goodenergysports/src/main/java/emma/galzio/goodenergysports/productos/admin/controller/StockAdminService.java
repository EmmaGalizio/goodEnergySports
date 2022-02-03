package emma.galzio.goodenergysports.productos.admin.controller;

import emma.galzio.goodenergysports.productos.admin.transferObject.StockAdminDto;
import emma.galzio.goodenergysports.productos.commons.domain.Producto;
import emma.galzio.goodenergysports.productos.commons.domain.Stock;
import emma.galzio.goodenergysports.productos.commons.domain.Talle;
import emma.galzio.goodenergysports.productos.commons.persistence.entity.*;
import emma.galzio.goodenergysports.productos.commons.persistence.repository.StockRepository;
import emma.galzio.goodenergysports.utils.exception.DomainException;
import emma.galzio.goodenergysports.utils.mapper.EntityMapper;
import emma.galzio.goodenergysports.utils.mapper.EntityToBusinessMapper;
import emma.galzio.goodenergysports.utils.mapper.TransferMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class StockAdminService {

    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private TransferMapper<StockAdminDto, Stock> stockTransferMapper;
    @Autowired
    private EntityMapper<ProductoEntity, Producto> productoEntityMapper;
    @Autowired
    private EntityMapper<TalleEntity, Talle> talleEntityMapper;
    @Autowired
    private EntityToBusinessMapper<StockEntity, Stock> stockEntityToBusinessMapper;
    @Autowired
    private IProductoAdminService productoAdminService;


    public List<StockAdminDto> listarStockProducto(Integer codigoProducto, boolean active){

        ProductoEntity productoEntity = productoAdminService.buscarProductoEntity(codigoProducto);
        List<StockEntity> stockEntities;
        if(active){
            stockEntities = stockRepository.findByProductoAndAndFechaBajaIsNull(productoEntity);
        } else{
            stockEntities = stockRepository.findByProducto(productoEntity);
        }
        List<Stock> stock = stockEntityToBusinessMapper.mapAllToBusiness(stockEntities);
        return stockTransferMapper.mapAllToDto(stock);
    }

    public int contarStockPorProducto(Integer codigoProducto, boolean active){
        ProductoEntity productoEntity = productoAdminService.buscarProductoEntity(codigoProducto);
        if(active){
            return stockRepository.countByProductoAndFechaBajaIsNull(productoEntity);
        }
        return stockRepository.countByProducto(productoEntity);
    }

    public StockAdminDto buscarStock(Integer codigoProducto, String talle){

        ProductoEntity productoEntity = productoAdminService.buscarProductoEntity(codigoProducto);
        TalleEntityId talleEntityId = new TalleEntityId(talle,productoEntity.getCategoria().getIdCategoria());
        StockEntityId stockEntityId = new StockEntityId(codigoProducto,talleEntityId);
        StockEntity stockEntity = stockRepository.getById(stockEntityId);
        Stock stock;
        try{
            stock = stockEntityToBusinessMapper.mapToBusiness(stockEntity);
        }catch(EntityNotFoundException e){
            DomainException domainException = new DomainException("No existe stock registrado para el talle especificado");
            domainException.addCause("TALLE", "No existe stock registrado para el talle especificado");
            throw domainException;
        }
        return stockTransferMapper.mapToDto(stock);
    }

    public StockAdminDto crearNuevoStockItem(Integer codigoProducto, StockAdminDto stockAdminDto){

        Producto producto = productoAdminService.buscarProductoBusiness(codigoProducto);
        if(!producto.estaActivo()){
            DomainException domainException = new DomainException("El producto se encuentra inactivo");
            domainException.addCause("PRODUCTO", "No es posble modificar el stock de un producto que se encuentra inactivo, por favor primero active el producto mediante el método correspondiente");
            throw domainException;
        }

        if(!producto.estaActivo()){
            DomainException domainException = new DomainException("No es posible modificar el stock de un producto inactivo");
            domainException.addCause("PRODUCTO", "No es posble modificar el stock de un producto inactivo, por favor asegurese de activar el producto antes de modificar su stock");
            throw domainException;
        }
        //No es necesario validar si la categoria está activa, por regla de negocio si el producto está activo entonces la categoria tambien
        Stock stock = stockTransferMapper.mapToBusiness(stockAdminDto);
        stock.getTalle().setIdCategoriaTalle(producto.getCategoria().getIdCategoria());
        if(!stock.validate()){
            DomainException domainException = new DomainException("Uno o mas campos no cumplen con las restricciones de negocio");
            domainException.addCause("Stock", "Uno o mas campos no cumplen con las restricciones de negocio");
            throw domainException;
        }
        if(producto.verificarStockExistente(stock)){
            DomainException domainException = new DomainException("Ya existe registrado un ítem de stock para el talle indicado en el producto, por favor utilice el metodo correspondiente para modificarlo");
            domainException.addCause("TALLE", "Ya existe registrado un ítem de stock para el talle indicado en el producto");
            throw domainException;
        }
        StockEntity stockEntity = this.mapStockToEntity(stock,producto);
        stockEntity = stockRepository.save(stockEntity);
        stock = stockEntityToBusinessMapper.mapToBusiness(stockEntity);
        return stockTransferMapper.mapToDto(stock);
    }

    /***
     *
     * @param codigoProducto
     * @param talle
     * @return
     */
    public StockAdminDto registrarBajaStock(Integer codigoProducto, String talle){

        if(codigoProducto == null || talle == null || talle.trim().isEmpty())
            throw new NullPointerException("Se deben indicar un código de producto y un talle válidos");
        Producto producto = productoAdminService.buscarProductoBusiness(codigoProducto);
        if(!producto.estaActivo()){
            DomainException domainException = new DomainException("El producto se encuentra inactivo");
            domainException.addCause("PRODUCTO", "No es posble modificar el stock de un producto que se encuentra inactivo, por favor primero active el producto mediante el método correspondiente");
            throw domainException;
        }
        Stock stock = producto.registrarBajaStock(talle);
        if(stock == null){
            DomainException domainException = new DomainException("No existe un ítem de stock del talle "+talle+" para el producto indicado");
            domainException.addCause("STOCK","No existe un ítem de stock del talle "+talle+" para el producto indicado");
            throw domainException;
        }
        StockEntity stockEntity = this.mapStockToEntity(stock,producto);
        stockEntity = stockRepository.save(stockEntity);
        return stockTransferMapper.mapToDto(stock);

    }
    private StockEntity mapStockToEntity(Stock stock, Producto producto){
        TalleEntity talleEntity = talleEntityMapper.mapToEntity(stock.getTalle());
        StockEntityId stockEntityId = new StockEntityId(producto.getCodigo(),talleEntity.getId());
        StockEntity stockEntity = new StockEntity();
        stockEntity.setId(stockEntityId);
        stockEntity.setTalle(talleEntity);
        stockEntity.setFechaBaja(stock.getFechaBaja());
        stockEntity.setProducto(productoEntityMapper.mapToEntity(producto));
        stockEntity.setStockDisponible(stock.getStockDisponible());
        return stockEntity;
    }

    /***
     *
     * En caso de que el se quiera reactivar el ítem de stock y el talle esté también inactivo entonces se activará
     * también el talle
     * @param codigo
     * @param talle
     * @param stockAdminDto
     * @return
     */
    public StockAdminDto actualizarStock(Integer codigo, String talle, StockAdminDto stockAdminDto) {

        Producto producto = productoAdminService.buscarProductoBusiness(codigo);
        if(!producto.estaActivo()){
            DomainException domainException = new DomainException("El producto se encuentra inactivo");
            domainException.addCause("PRODUCTO", "No es posble modificar el stock de un producto que se encuentra inactivo, por favor primero active el producto mediante el método correspondiente");
            throw domainException;
        }
        Stock stockPersistido = producto.buscarItemStock(talle);
        Stock stock = stockTransferMapper.mapToBusiness(stockAdminDto);
        if(!stock.validate()){
            DomainException domainException = new DomainException("El ítem de stock no tiene un formato válido");
            domainException.addCause("STOCK", "El ítem de stock no tiene un formato válido");
            throw domainException;
        }
        if(stockPersistido.estaActivo() && !stock.estaActivo()){
            DomainException domainException = new DomainException("No es posible registrar la baja de un ítem mediante este método");
            domainException.addCause("BAJA_STOCK", "No es posible registrar la baja de un ítem mediante este método");
            throw domainException;
        }
        if(stock.estaActivo() && !stockPersistido.estaActivo()){
            if(!stockPersistido.getTalle().estaActivo()){
                stock.getTalle().activar();
            }
        }
        StockEntity stockEntity = this.mapStockToEntity(stock,producto);
        stockRepository.save(stockEntity);
        return stockTransferMapper.mapToDto(stock);
    }
}
