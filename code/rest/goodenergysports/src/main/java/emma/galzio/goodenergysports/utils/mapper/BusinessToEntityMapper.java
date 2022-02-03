package emma.galzio.goodenergysports.utils.mapper;

import java.util.List;

public interface BusinessToEntityMapper <E,B>{
    E mapToEntity(B business);
    List<E> mapAllToEntity(List<B> businessList);
}
