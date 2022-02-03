package emma.galzio.goodenergysports.productos.client.utils;

import emma.galzio.goodenergysports.productos.commons.domain.Stock;
import emma.galzio.goodenergysports.productos.commons.persistence.entity.CategoriaEntity;
import emma.galzio.goodenergysports.productos.commons.persistence.entity.ProductoEntity;
import emma.galzio.goodenergysports.productos.commons.persistence.entity.StockEntity;
import emma.galzio.goodenergysports.productos.commons.persistence.entity.TalleEntity;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class ProductoSpecification {

    public static Specification<ProductoEntity> categoriaFilter(Optional<CategoriaEntity> categoria){
        //Cambiar para que busque subcategorias tambien
        if(!categoria.isPresent()) return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.conjunction();

        return ((root, criteriaQuery, criteriaBuilder) ->{
                Predicate categoriaPredicate = root.get("categoria").in(categoria.get());
                Predicate subCatPedicate = root.get("categoria").in(categoria.get().getSubCategorias());
                return criteriaBuilder.or(categoriaPredicate, subCatPedicate);

        });
    }

    public static  Specification<ProductoEntity> precioFilter(Optional<BigDecimal> min, Optional<BigDecimal> max){
        if(min.isPresent() && max.isPresent()){
            return (root, query, builder)->
                    builder.between(root.get("precio"), min.get(),max.get());
        }
        if(min.isPresent()){
            return (root,query,builder)-> builder.greaterThanOrEqualTo(root.get("precio"),min.get());
        }
        return max.<Specification<ProductoEntity>>map(bigDecimal -> (root, query, builder) -> builder.lessThanOrEqualTo(root.get("precio"), bigDecimal))
                .orElseGet(() -> (root, query, builder) -> builder.conjunction());
    }

    public static Specification<ProductoEntity> tallesFilter(Optional<List<String>> talles){

        if(talles.isEmpty() || talles.get().isEmpty()) return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.conjunction();

        //Debe ser string pero primero voy a probar con el stock disponible
        List<String> sTalles = talles.get();
        return ((root, criteriaQuery, criteriaBuilder) -> {

            String sqlQuery = "select * from Producto WHERE codigo_producto IN " +
                    "(SELECT s.producto as codigo from Stock s WHERE s.talle IN (\"S\",\"XS\") and s.categoria_talle=7);";
            String jpqlQuery = "SELECT p from ProductoEntity p WHERE EXISTS(" +
                    "SELECT s.producto FROM StockEntity s WHERE s.talle.talle = :sTalle AND s.talle.categoria = :categoria)";


            criteriaBuilder.isMember(root.get("codigoProducto"),)

            Subquery<TalleEntity> talleEntitySubquery = stockEntitySubquery.subquery(TalleEntity.class);
            Root<TalleEntity> talleEntityRoot = talleEntitySubquery.from(TalleEntity.class);


        });

        return null;
    }

    //Specification de talle


}
