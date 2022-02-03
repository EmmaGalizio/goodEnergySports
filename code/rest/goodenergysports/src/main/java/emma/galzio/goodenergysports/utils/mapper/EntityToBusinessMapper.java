package emma.galzio.goodenergysports.utils.mapper;

import java.util.List;

public interface EntityToBusinessMapper<E,B> {

    B mapToBusiness(E entity);
    List<B> mapAllToBusiness(List<E> entityList);
}
