package emma.galzio.goodenergysports.productos.admin.controller;

import emma.galzio.goodenergysports.productos.admin.transferObject.TalleAdminDto;

import java.util.List;

public interface ICategoriaTalleAdminService {



    List<TalleAdminDto> listAllTalleByCategoria(Integer idCategoria, Integer page,
                                                Integer size, boolean active);
}
