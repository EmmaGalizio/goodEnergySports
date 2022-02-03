package emma.galzio.goodenergysports.utils.mapper;

import org.springframework.hateoas.RepresentationModel;

import java.util.List;

public interface DtoToBusinessMapper<D extends RepresentationModel<D>,B> {

    B mapToBusiness(D dto);
    List<B> mapAllToBusiness(List<D> dtoList);
}
