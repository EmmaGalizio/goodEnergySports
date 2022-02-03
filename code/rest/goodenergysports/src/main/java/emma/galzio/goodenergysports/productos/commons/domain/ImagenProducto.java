package emma.galzio.goodenergysports.productos.commons.domain;

import emma.galzio.goodenergysports.utils.exception.DomainException;
import org.apache.commons.io.FilenameUtils;

import java.util.Comparator;
import java.util.Objects;

public class ImagenProducto {

    private String rutaArchivo;
    private String uri;
    private Integer orden;
    private String nombreOriginal;
    private String base64;

    public String getRutaArchivo() {
        return rutaArchivo;
    }

    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public static Comparator<ImagenProducto> getOrdenComparator(){
        return new OrdenComparator();
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getNombreOriginal() {
        return nombreOriginal;
    }

    public void setNombreOriginal(String nombreOriginal) {
        this.nombreOriginal = nombreOriginal;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    public boolean isNew(){
        if(base64 != null && uri != null){
            throw new DomainException("La nueva imagen no puede contener una URI asignada");
        }
        return base64 != null;
    }
    public String getExtension(){
        if(nombreOriginal != null) {
            return FilenameUtils.getExtension(this.getNombreOriginal());
        }
        return FilenameUtils.getExtension(this.getRutaArchivo());
    }

    public Integer getUriOrder(){
        try{
            if (uri == null || uri.isEmpty()) return -1;
            String[] uriComponents = uri.split("/");
            String fileName = uriComponents[uriComponents.length - 1];

            fileName = fileName.split("-")[1];
            String nameComponents[] = fileName.split("\\.");
            String sOrder = nameComponents[0];
            return Integer.parseInt(sOrder);
        }catch(Exception e){
            DomainException domainException = new DomainException("La URI de la imagen tiene un formato inválido");
            domainException.addCause("IMAGEN_PRODUCTO", "La URI de la imagen tiene un formato inválido");
            domainException.addCause("URI", uri);
            throw  domainException;
        }
    }

    public boolean isUpToDate(){
        return this.getUriOrder().equals(orden);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImagenProducto that = (ImagenProducto) o;
        return rutaArchivo.equals(that.rutaArchivo) &&
                orden.equals(that.orden);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rutaArchivo, orden);
    }

    private static class OrdenComparator implements Comparator<ImagenProducto> {
        @Override
        public int compare(ImagenProducto o1, ImagenProducto o2) {
            if(o1 == null) return -1;
            if(o2 == null) return 1;
            return o1.getOrden().compareTo(o2.getOrden());
        }
    }

}
