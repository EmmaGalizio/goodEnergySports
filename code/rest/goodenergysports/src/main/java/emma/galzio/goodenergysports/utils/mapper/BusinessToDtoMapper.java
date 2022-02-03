package emma.galzio.goodenergysports.utils.mapper;

import org.springframework.hateoas.RepresentationModel;

import java.util.List;

public interface BusinessToDtoMapper<D extends RepresentationModel<D>,B> {

    D mapToDto(B business);
    List<D> mapAllToDto(List<B> businessList);

}
