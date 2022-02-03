package emma.galzio.goodenergysports.productos.admin.controller;

import emma.galzio.goodenergysports.productos.admin.transferObject.CategoriaAdminDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ICategoriaAdminService {
    @Transactional
    List<CategoriaAdminDto> listAllCategories(Integer page, Integer size, boolean active);

    int countAllCategories(boolean active);

    @Transactional
    CategoriaAdminDto getCategoriaById(Integer id);

    @Transactional
    CategoriaAdminDto postNewCategoria(CategoriaAdminDto categoriaAdminDtoaDto);

    @Transactional
    CategoriaAdminDto updateCategoria(CategoriaAdminDto categoriaAdminDto);

    @Transactional
    CategoriaAdminDto disableCategoria(Integer idCategoria);
}
