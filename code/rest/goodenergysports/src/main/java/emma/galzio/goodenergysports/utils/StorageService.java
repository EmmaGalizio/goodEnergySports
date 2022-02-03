package emma.galzio.goodenergysports.utils;

import emma.galzio.goodenergysports.productos.commons.domain.ImagenProducto;
import emma.galzio.goodenergysports.utils.exception.FileStorageException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.*;
import java.util.Base64;
import java.util.List;

public class StorageService {

    public static final String[] IMAGE_FILE_EXTENTIONS = {"JPG","JPEG","PNG"};
    public static final String VALID_IMAGE_EXTENTIONS = "Las extensiones de imágenes válidas son JPG, JPEG y PNG";


    //private String fileLocationPathString;
    //private Path fileStorageLocation;

    public Path storeProductImage(MultipartFile file, String fileLocationPathString, String fileName) {

        fileLocationPathString+= "/images/producto";

        Path fileStorageLocation = Paths.get(fileLocationPathString);
        File storageDirectory = new File(fileLocationPathString);
        if(!storageDirectory.exists()){
            try {
                Files.createDirectories(fileStorageLocation);
            } catch (Exception ex) {
                throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
            }
        }

        try {
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            Path targetLocation = fileStorageLocation.resolve(fileName);
            if(!Files.exists(targetLocation)) {
                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            } else{
                throw new FileStorageException("Could not store file "+ fileName + ". A file with the same name already exists");
            }

            return targetLocation;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public Path storeBase64ProductoImage(String fileName, String fileLocationPathString, ImagenProducto imagenProducto){

        //fileLocationPathString+= "/images/producto";
        Path fileStorageLocation = Paths.get(fileLocationPathString);
        File storageDirectory = new File(fileLocationPathString);
        if(!storageDirectory.exists()){
            try {
                Files.createDirectories(fileStorageLocation);
            } catch (Exception ex) {
                throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
            }
        }
        if(fileName.contains("..")) {
            throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
        }

        byte [] data = Base64.getDecoder().decode(imagenProducto.getBase64());
        Path destinationPath = Paths.get(fileLocationPathString).resolve(fileName);
        if(destinationPath.toFile().exists()){
            throw new FileStorageException("Could not store file "+ fileName + ". A file with the same name already exists");
        }

        try(OutputStream stream = new FileOutputStream(destinationPath.toFile())){
            stream.write(data);
        } catch (Exception e){
            throw new FileStorageException("The file couldn't be written ");
        }
        return destinationPath;

    }

    public boolean deleteFile(String path){
        return this.deleteFile(Path.of(path));
    }
    public boolean deleteFile(Path path){

        File file = path.toFile();
        if(file.exists()){
            return file.delete();
        }
        return false;
    }

    /**
     * Por type erasure, Java en tiempo de ejecución elimina los tipos de los generycs
     * haciendo que, si tengo dos métodos con el mismo nombre pero distinta signatura,
     * pero que reciban tipos genéricos (Por ejemplo List<String> y List<Path>, en tiempo
     * de ejecución java los detecte como que tienen la misma signatura (List<T>) y colisionen
     * */
    public boolean deleteFiles(Iterable<String> paths){
        for(String path: paths){
            if(!Path.of(path).toFile().exists()) return false;
        }
        paths.forEach(this::deleteFile);
        return true;
    }

    public boolean deleteFiles(List<Path> paths){

        for(Path path : paths){
            if(!path.toFile().exists()) return false;
        }
        paths.forEach(this::deleteFile);
        return true;
    }

}
