package emma.galzio.goodenergysports.model.TransferModel;


public class TalleDto {

    private Integer idTalle;
    private String talle;
    private String equivalencia;
    private CategoriaDto categoriaProducto;

    public Integer getIdTalle() {
        return idTalle;
    }

    public void setIdTalle(Integer idTalle) {
        this.idTalle = idTalle;
    }

    public String getTalle() {
        return talle;
    }

    public void setTalle(String talle) {
        this.talle = talle;
    }

    public String getEquivalencia() {
        return equivalencia;
    }

    public void setEquivalencia(String equivalencia) {
        this.equivalencia = equivalencia;
    }

    public CategoriaDto getCategoriaProducto() {
        return categoriaProducto;
    }

    public void setCategoriaProducto(CategoriaDto categoriaProducto) {
        this.categoriaProducto = categoriaProducto;
    }
}
