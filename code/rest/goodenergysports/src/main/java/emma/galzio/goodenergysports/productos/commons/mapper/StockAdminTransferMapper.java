package emma.galzio.goodenergysports.productos.commons.mapper;

import emma.galzio.goodenergysports.productos.admin.transferObject.StockAdminDto;
import emma.galzio.goodenergysports.productos.admin.transferObject.TalleAdminDto;
import emma.galzio.goodenergysports.productos.commons.domain.Stock;
import emma.galzio.goodenergysports.productos.commons.domain.Talle;
import emma.galzio.goodenergysports.utils.exception.DomainException;
import emma.galzio.goodenergysports.utils.mapper.TransferMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockAdminTransferMapper implements TransferMapper<StockAdminDto, Stock> {
    @Override
    public StockAdminDto mapToDto(Stock business) throws DomainException {

        StockAdminDto stockAdminDto = new StockAdminDto();
        stockAdminDto.setCantidadDisponible(business.getStockDisponible());
        stockAdminDto.setFechaBaja(business.getFechaBaja());

        TalleAdminDto talleAdminDto = new TalleAdminDto();
        talleAdminDto.setTalle(business.getTalle().getTalle());
        talleAdminDto.setEquivalencia(business.getTalle().getEquivalencia());
        stockAdminDto.setTalle(talleAdminDto);
        return stockAdminDto;
    }

    @Override
    public Stock mapToBusiness(StockAdminDto dto) throws DomainException {
        if(dto == null) throw new NullPointerException("El stock no puede ser nulo");
        Stock stock = new Stock();
        stock.setStockDisponible(dto.getCantidadDisponible());
        stock.setFechaBaja(dto.getFechaBaja());
        Talle talle = new Talle();
        talle.setTalle(dto.getTalle().getTalle() != null ?dto.getTalle().getTalle().trim().toUpperCase():null);
        talle.setEquivalencia(dto.getTalle().getEquivalencia());
        stock.setTalle(talle);
        return stock;
    }

    @Override
    public List<StockAdminDto> mapAllToDto(List<Stock> businessList) throws DomainException {
        if(businessList == null || businessList.isEmpty()) throw  new NullPointerException("La lista de stock no puede estar vacia");
        return businessList.stream().map(this::mapToDto).collect(Collectors.toList());

    }

    @Override
    public List<Stock> mapAllToBusiness(List<StockAdminDto> dtoList) throws DomainException {
        if(dtoList == null || dtoList.isEmpty()) throw  new NullPointerException("La lista de stock no puede estar vacia");
        return dtoList.stream().map(this::mapToBusiness).collect(Collectors.toList());
    }
}
