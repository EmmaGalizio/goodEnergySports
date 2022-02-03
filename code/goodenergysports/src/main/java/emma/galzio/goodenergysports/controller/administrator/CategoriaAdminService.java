package emma.galzio.goodenergysports.controller.administrator;

import emma.galzio.goodenergysports.model.TransferModel.CategoriaDto;
import emma.galzio.goodenergysports.webController.administratorController.CategoriaWebController;
import org.springframework.stereotype.Service;

import java.util.List;


public interface CategoriaAdminService {

    void displayAllCategories();

    CategoriaDto getCategoriaToModify(Integer id);

    void setWebController(CategoriaWebController webController);

    CategoriaDto searchCategoriaById(Integer id);

    boolean createNewCategoria(CategoriaDto categoriaDto);

    void updateCategoria(CategoriaDto categoriaDto);

    void deleteCategoria(CategoriaDto categoriaDto);

    String saveOrUpdateCategoria(CategoriaDto categoriaDto);

}
