package emma.galzio.goodenergysports.controller.administrator;

import emma.galzio.goodenergysports.model.TransferModel.CategoriaDto;
import emma.galzio.goodenergysports.model.businessModel.Categoria;
import emma.galzio.goodenergysports.model.entityModel.CategoriaEntity;
import emma.galzio.goodenergysports.model.mappers.CategoriaMapper;
import emma.galzio.goodenergysports.persistence.jpaRepository.CategoriaRepository;
import emma.galzio.goodenergysports.webController.administratorController.CategoriaWebController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
public class CategoriaAdminServiceImpl implements CategoriaAdminService {

    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private CategoriaMapper categoriaMapper;

    private CategoriaWebController webController;
    private List<CategoriaDto> categoriaDtos;


    @Override
    public void displayAllCategories() {

        Pageable pageable = PageRequest.of(0, new Long(categoriaRepository.count()).intValue());

        Page<CategoriaEntity> entidades = categoriaRepository.findAllOrderByFechaBajaAndIdCategoria(pageable);
        List<Categoria> categorias = categoriaMapper.mapAllFromEntity(entidades.getContent());
        categoriaDtos = categoriaMapper.mapAllToDto(categorias);
        webController.setCategorias(categoriaDtos);

    }

    @Override
    public CategoriaDto getCategoriaToModify(Integer id) throws IllegalArgumentException, NullPointerException{

        if(id == null || id <= 0) throw new IllegalArgumentException("El id de la categoría no puede ser nulo");
        CategoriaEntity categoriaEntity = categoriaRepository.getOne(id);
        if(categoriaEntity == null) throw new NullPointerException("El id provisto no corresponde a una categoria existente");
        Categoria categoria = categoriaMapper.mapFromEntity(categoriaEntity);

        return categoriaMapper.mapToDto(categoria);

    }

    @Override
    public CategoriaDto searchCategoriaById(Integer id) throws IllegalArgumentException, NullPointerException {
        if (categoriaDtos == null) return null;
        if (id == null || id <= 0) throw new IllegalArgumentException("El id de la categoría no puede ser nulo");
        CategoriaDto categoriaDto = new CategoriaDto();
        categoriaDto.setIdCategoria(id);
        try {

            categoriaDto = categoriaDtos.stream().filter((cat) -> cat.getIdCategoria().equals(id)).findFirst().get();
        } catch (Exception e) {
            throw new IllegalArgumentException("El id provisto no corresponde a una categoria existente");
        }

        return categoriaDto;
    }

    @Override
    public String saveOrUpdateCategoria(CategoriaDto categoriaDto) throws IllegalArgumentException {
        String success;

        if(categoriaDto.getIdCategoria() == null){
            this.createNewCategoria(categoriaDto);
            return "La categoria se creó correctamente";
        } else{
            this.updateCategoria(categoriaDto);
            return "La categoria se actualizó correctamente";
        }

    }

    @Override
    public boolean createNewCategoria(CategoriaDto categoriaDto) throws IllegalArgumentException {

        Categoria newCategoria = categoriaMapper.mapFromDto(categoriaDto);
        boolean esValida = newCategoria.esCategoriaValida();
        categoriaRepository.save(categoriaMapper.mapToEntity(newCategoria));
        return esValida;

    }

    @Override
    public void updateCategoria(CategoriaDto categoriaDto) throws IllegalArgumentException{
        this.createNewCategoria(categoriaDto);
    }

    @Override
    public void deleteCategoria(CategoriaDto categoriaDto) {

        CategoriaEntity categoriaEntity = null;
        try{
            categoriaEntity = categoriaRepository.getOne(categoriaDto.getIdCategoria());

        } catch (Exception e){
            throw new IllegalArgumentException("El id proporcionado no corresponde a una categoria registrada");
        }
        Categoria categoria = categoriaMapper.mapFromEntity(categoriaEntity);

        if(categoria.sePuedeRegistrarBaja()){

            categoria.setFechaBaja(LocalDate.now());
            categoriaRepository.deactivateCategoria(categoria.getIdCategoria(), categoria.getFechaBaja());

        } else{
            throw new IllegalArgumentException("No es posible registrar la baja de esta categoría, es padre de una o mas sub categorias activas");
        }

    }

    @Override
    public void setWebController(CategoriaWebController webController) {
        this.webController = webController;

    }
}
