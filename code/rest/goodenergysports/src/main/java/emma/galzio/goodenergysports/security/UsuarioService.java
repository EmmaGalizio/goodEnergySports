package emma.galzio.goodenergysports.security;

import emma.galzio.goodenergysports.clientes.domain.Usuario;
import emma.galzio.goodenergysports.clientes.persistence.entity.UsuarioEntity;
import emma.galzio.goodenergysports.security.persistence.repository.UsuarioRepository;
import emma.galzio.goodenergysports.utils.mapper.EntityToBusinessMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final EntityToBusinessMapper<UsuarioEntity, Usuario> usuarioEntityMapper;

    /***
     *
     * @param usuario An string with the username or email of the requested user
     * @return An object with the user data corresponding to the provided username or email, null if the user
     * doesn't exists
     */
    public Usuario buscarUsuario(String usuario){
        if(usuario == null || usuario.trim().isEmpty()) throw new NullPointerException("Debe indicar el nombre de usuario o email");

        UsuarioEntity usuarioEntity = usuario.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
                                            ? usuarioRepository.findByEmailIgnoreCase(usuario.trim())
                                            :usuarioRepository.findByUsuario(usuario.trim());
        return usuarioEntityMapper.mapToBusiness(usuarioEntity);

    }

}
