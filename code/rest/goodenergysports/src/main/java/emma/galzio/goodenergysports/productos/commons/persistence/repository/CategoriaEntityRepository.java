package emma.galzio.goodenergysports.productos.commons.persistence.repository;

import emma.galzio.goodenergysports.productos.commons.persistence.entity.CategoriaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CategoriaEntityRepository extends JpaRepository<CategoriaEntity, Integer> {

    /****
     * This method represents a query from method name wich is parsed by Spring to a JPA query
     * @param pageable Contain the page size and number
     * @return A list of all categorias ordered first by fechaBaja Asc, and then by idCategoria Asc
     */
    Page<CategoriaEntity> findByOrderByFechaBajaAscIdCategoriaAsc(Pageable pageable);

    /****
     * This method represents a query from method name wich is parsed by Spring to a JPA query
     * @param pageable Contain the page size and number
     * @return A list of all actives categorias.
     */
    Page<CategoriaEntity> findByFechaBajaIsNull(Pageable pageable);

    @Query("UPDATE CategoriaEntity cat SET cat.fechaBaja=?2 WHERE cat.idCategoria=?1")
    @Modifying
    void deactivateCategoria(Integer id, LocalDate fechaBaja);

    Integer countBy();
    Integer countByFechaBajaIsNull();

    @Query("SELECT subCat.idCategoria FROM CategoriaEntity  subCat WHERE subCat.categoriaSuperior.idCategoria = ?1")
    List<Integer> findSubCategoriesId(Integer idCategoria);

    @Query("SELECT catSup FROM CategoriaEntity catSup WHERE catSup.idCategoria = (SELECT subCat.categoriaSuperior.idCategoria FROM CategoriaEntity subCat WHERE subCat.idCategoria = ?1)")
    CategoriaEntity findCategoriaSuperior(Integer idCategoria);


}