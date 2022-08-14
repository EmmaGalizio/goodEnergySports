package emma.galzio.goodenergysports.productos.commons.utils;

import emma.galzio.goodenergysports.productos.admin.controller.ProductoAdminService;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductoFilter {

    private Integer categoria;
    private Double precioMin;
    private Double precioMax;
    private ProductoAdminService.ORDER sort;
    private String talles;

    public ProductoFilter(){
        sort = ProductoAdminService.ORDER.DEFAULT;
    }

    public void setSort(ProductoAdminService.ORDER sort){
        this.sort = sort != null ? sort : ProductoAdminService.ORDER.DEFAULT;
    }
    public void setTalles(String talles){
        this.talles = talles != null ? talles.toUpperCase() : null;
    }

}
