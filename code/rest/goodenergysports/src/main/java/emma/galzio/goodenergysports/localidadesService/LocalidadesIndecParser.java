package emma.galzio.goodenergysports.localidadesService;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class LocalidadesIndecParser {

    public List<Provincia> parseProvincias(String resoursePath){

        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<ProvinciasWrapper> typeReference = new TypeReference<ProvinciasWrapper>() {};
        InputStream inputStream = TypeReference.class.getResourceAsStream(resoursePath);
        try{
            ProvinciasWrapper provinciasWrapper = objectMapper.readValue(inputStream, typeReference);
            return provinciasWrapper.getProvincias();
        }catch(IOException e){
            e.printStackTrace();
            System.out.println("Ocurrió un error al cargar las provincias");
        }
        return null;
    }
    public List<Localidad> parseLocalidades(String resoursePath){
        ObjectMapper objectMapper = new ObjectMapper();
        TypeReference<LocalidadesWrapper> typeReference = new TypeReference<>() {};
        InputStream inputStream = TypeReference.class.getResourceAsStream(resoursePath);
        try{
            LocalidadesWrapper localidadesWrapper = objectMapper.readValue(inputStream, typeReference);
            return localidadesWrapper.getLocalidades();
        }catch(IOException e){
            e.printStackTrace();
            System.out.println("Ocurrió un error al cargar las provincias");
        }
        return null;

    }

}
