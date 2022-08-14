package emma.galzio.goodenergysports.productos.client.controller;

import emma.galzio.goodenergysports.productos.client.transferObject.CategoriaDto;

import java.util.List;

public interface ICategoriaController {

    enum SORT{
        DEFAULT("idCategoria","ASC"),
        NOMBRE_ASC("nombre","ASC"),
        NOMBRE_DESC("nombre", "DESC");

        private final String field;
        private final String dir;
        SORT(String field, String dir) {
            this.field = field;
            this.dir = dir;

        }
        public String getField() {
            return field;
        }

        public String getDir() {
            return dir;
        }
    };

    List<CategoriaDto> listarCategorias(SORT sort);
    CategoriaDto buscarCategoria(Integer idCategoria);
    CategoriaDto buscarCategoria(String nombre);
}
