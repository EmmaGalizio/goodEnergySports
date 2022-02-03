package emma.galzio.goodenergysports.security.service;

import emma.galzio.goodenergysports.security.domain.Permiso;
import emma.galzio.goodenergysports.security.persistence.entity.PermisoEntity;
import emma.galzio.goodenergysports.security.persistence.entity.PermisoEntityId;
import emma.galzio.goodenergysports.security.persistence.repository.PermisoRepository;
import emma.galzio.goodenergysports.security.utils.mapper.PermisoEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;


@RequiredArgsConstructor
public class PermisosService {

    private final PermisoRepository permisoRepository;
    //Por ahora no va a ser un service
    private final PermisoEntityMapper permisoEntityMapper;

    @Transactional
    public List<Permiso> cargarPermisos(){

        List<PermisoEntity> permisosEntity = permisoRepository.findAll();
        List<Permiso> permisos;
        try{
            permisos = permisoEntityMapper.mapAllToBusiness(permisosEntity);
        }catch(NullPointerException e){
            permisos = Collections.emptyList();
        }
        return permisos;

    }

    public Permiso cargarPermiso(String url, String metodo){
        PermisoEntityId permisoEntityId = new PermisoEntityId(url,metodo);
        PermisoEntity permisoEntity = permisoRepository.getById(permisoEntityId);
        //Si no existe el mapper en vez de lanzar la excepcion retorna null
        return permisoEntityMapper.mapToBusiness(permisoEntity);
    }

    public List<Permiso> cargarPermisos(String url){

        List<PermisoEntity> permisoEntities = permisoRepository.findById_Url(url);
        return permisoEntityMapper.mapAllToBusiness(permisoEntities);
    }

}
