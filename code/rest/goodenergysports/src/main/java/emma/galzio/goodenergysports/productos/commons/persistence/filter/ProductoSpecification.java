package emma.galzio.goodenergysports.productos.commons.persistence.filter;

import emma.galzio.goodenergysports.productos.commons.persistence.entity.*;
import emma.galzio.goodenergysports.productos.commons.persistence.entity.CategoriaEntity_;
import emma.galzio.goodenergysports.productos.commons.persistence.entity.ProductoEntity_;
import emma.galzio.goodenergysports.productos.commons.persistence.entity.StockEntity_;
import emma.galzio.goodenergysports.productos.commons.persistence.entity.TalleEntityId_;
import emma.galzio.goodenergysports.productos.commons.persistence.entity.TalleEntity_;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import javax.persistence.metamodel.EntityType;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProductoSpecification {

    public static Specification<ProductoEntity> categoriaFilter(Optional<CategoriaEntity> categoria){
        //Cambiar para que busque subcategorias tambien
        if(!categoria.isPresent()) return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.conjunction();

        return ((root, criteriaQuery, criteriaBuilder) ->{
                Predicate categoriaPredicate = root.get(ProductoEntity_.categoria).in(categoria.get());
                if(categoria.get().getSubCategorias() != null && !categoria.get().getSubCategorias().isEmpty()) {
                    Predicate subCatPedicate = root.get(ProductoEntity_.categoria).in(categoria.get().getSubCategorias());
                    return criteriaBuilder.or(categoriaPredicate, subCatPedicate);
                }
                return categoriaPredicate;
        });
    }

    public static  Specification<ProductoEntity> precioFilter(Optional<BigDecimal> min, Optional<BigDecimal> max){
        if(min.isPresent() && max.isPresent()){
            return (root, query, builder)->
                    builder.between(root.get(ProductoEntity_.precio), min.get(),max.get());
        }
        if(min.isPresent()){
            return (root,query,builder)-> builder
                    .greaterThanOrEqualTo(root.get(ProductoEntity_.precio),min.get());
        }
        return max.<Specification<ProductoEntity>>map(bigDecimal -> (root, query, builder) -> builder.lessThanOrEqualTo(root.get("precio"), bigDecimal))
                .orElseGet(() -> (root, query, builder) -> builder.conjunction());
    }

    public static Specification<ProductoEntity> tallesFilter(Optional<List<String>> talles, Optional<CategoriaEntity> categoria){

        if(talles.isEmpty() || talles.get().isEmpty() || categoria.isEmpty()) return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.conjunction();

        return ((root, criteriaQuery, criteriaBuilder) -> {

            //https://stackoverflow.com/questions/4483576/jpa-2-0-criteria-api-subqueries-in-expressions
            String sqlQuery = "select * from Producto WHERE codigo_producto IN " +
                    "(SELECT s.producto as codigo from Stock s WHERE s.talle IN (\"S\",\"XS\") and s.categoria_talle=7);";
            String jpqlQuery = "SELECT p from ProductoEntity p WHERE EXISTS(" +
                    "SELECT s.producto FROM StockEntity s WHERE s.talle.talle = :sTalle AND s.talle.categoria = :categoria)";

            //Necesito una suquery que retorne un "resultset" listando los codigos de producto que pertenezcan al talle
            Subquery<Integer> stockSubquery = criteriaQuery.subquery(Integer.class);
            //Obtengo el objeto root de la subquery, el stock que es está procesando en el momento
            Root<StockEntity> stockRoot = stockSubquery.from(StockEntity.class);
            //Obtengo el objeto Path que representa el talle del stock que se está procesando
            Path<TalleEntity> tallePath = stockRoot.get(StockEntity_.talle);
            //Se arma lo que debe retornar la subquery, en este caso los codigos de producto
            stockSubquery.select(stockRoot.get(StockEntity_.producto).get(ProductoEntity_.codigoProducto));
            //Se obtiene una expression que representa el valor del talle del stock que se está procesando
            Expression<String> talleExp = tallePath.get(TalleEntity_.id).get(TalleEntityId_.talle);
            //Se obtiene un predicado que busca los talles pertenecientes a la lista pasada por parámetro
            Predicate talleInPredicate = talleExp.in(talles.get());
            //Se obtiene un predicado que busca los talles de la categoria pasada por parámetro

            //Obtener una expresion del id de la categoria (como la del talle) para obtener el
            //predicacdo con el .in a partir de ella
            //En vez de ampliar la lista de subcategorias concatenar dos predicados con un OR
            //Una parte la pertenencia a la lista de subcategorias, y la otra parte la igualdad a la categoria
            Predicate categoriaStockPredicate = criteriaBuilder
                    .equal(tallePath.get(TalleEntity_.categoria).get(CategoriaEntity_.idCategoria), categoria.get()
                                                                                                        .getIdCategoria());
            if(categoria.get().getSubCategorias() != null && !categoria.get().getSubCategorias().isEmpty()){
                Path<CategoriaEntity> categoriaPath = tallePath.get(TalleEntity_.categoria);
                Expression<Integer>  idCategoriaExpr = categoriaPath.get(CategoriaEntity_.idCategoria);
                Predicate subCategoriasPredicate = idCategoriaExpr.in(categoria.get().getSubCategorias()
                                                    .stream().map(CategoriaEntity::getIdCategoria)
                                                    .collect(Collectors.toList()));
                categoriaStockPredicate = criteriaBuilder.or(categoriaStockPredicate, subCategoriasPredicate);
            }
            //Se hace la conjuncion de los dos predicados anteriores
            Predicate talleInAndCategoriaEqualPredicate = criteriaBuilder
                    .and(talleInPredicate,categoriaStockPredicate);

            //Se arma la subquery que busca los codigos de producto
            stockSubquery.where(talleInAndCategoriaEqualPredicate);

            //Se arma la query que compara si el codigo del producto actual pertenece a los codigo
            //retornados en la subquery
            //Debería funcionar
            criteriaQuery.where(criteriaBuilder
                    .in(root.get(ProductoEntity_.codigoProducto)).value(stockSubquery));

            return criteriaQuery.getRestriction();
        });

    }

    public static Specification<ProductoEntity> productosActivos(){
        return ((root, criteriaQuery, criteriaBuilder) -> criteriaQuery
                .where(root.get(ProductoEntity_.fechaBaja).isNull()).getRestriction());
    }

    public static Specification<ProductoEntity> productosActivos(boolean active){

        return active ? productosActivos() : ((root, criteriaQuery, criteriaBuilder) -> {
            return criteriaQuery.where(root.get(ProductoEntity_.fechaBaja).isNotNull()).getRestriction();
        });

    }

}
