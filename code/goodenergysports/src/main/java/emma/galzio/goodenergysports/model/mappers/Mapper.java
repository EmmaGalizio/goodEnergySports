package emma.galzio.goodenergysports.model.mappers;

import java.util.List;

public interface Mapper<B,E,D> {

    B mapFromDto(D dto);
    B mapFromEntity(E entity);
    E mapToEntity(B bussines);
    D mapToDto(B bussines);
    List<B> mapAllFromEntity(List<E> entities);
    List<D> mapAllToDto(List<B> bussines);

}
