package emma.galzio.goodenergysports.productos.admin.transferObject;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.RepresentationModel;

import java.util.Comparator;


public class ImagenProductoDto extends RepresentationModel<ImagenProductoDto>
                               implements Comparable<ImagenProductoDto> {

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String originalName;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String base64Image;
    private Integer orden;
    //@JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String uri;

    public ImagenProductoDto(Integer orden, String uri) {
        this.orden = orden;
        this.uri = uri;
    }


    public ImagenProductoDto() {
    }



    @Override
    public int compareTo(ImagenProductoDto o) {
        return this.getOrden().compareTo(o.getOrden());
    }

    public String getOriginalName() {
        return originalName;
    }

    public static Comparator<ImagenProductoDto> compareByOrden(){
        return new OrdenComparator();
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getBase64Image() {
        return base64Image;
    }

    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }


    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    private static class OrdenComparator implements Comparator<ImagenProductoDto> {
        @Override
        public int compare(ImagenProductoDto o1, ImagenProductoDto o2) {
            if(o1 == null && o2 == null) throw new NullPointerException("Los objetos no pueden ser nulos");
            if(o1 == null) return -1;
            if(o2 == null) return 1;
            return o1.getOrden().compareTo(o2.getOrden());
        }
    }
}
