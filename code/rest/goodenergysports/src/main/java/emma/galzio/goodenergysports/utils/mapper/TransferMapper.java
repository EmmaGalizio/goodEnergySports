package emma.galzio.goodenergysports.utils.mapper;

import org.springframework.hateoas.RepresentationModel;

public interface TransferMapper<D extends RepresentationModel<D>,B> extends BusinessToDtoMapper<D,B>, DtoToBusinessMapper<D,B> {


}
