package emma.galzio.goodenergysports.clientes.common.controller;

import emma.galzio.goodenergysports.clientes.domain.Cliente;
import emma.galzio.goodenergysports.clientes.persistence.entity.ClienteEntity;
import emma.galzio.goodenergysports.clientes.persistence.repository.ClienteEntityRepository;
import emma.galzio.goodenergysports.clientes.transferObject.ClienteDto;
import emma.galzio.goodenergysports.utils.exception.DomainException;
import emma.galzio.goodenergysports.utils.mapper.EntityMapper;
import emma.galzio.goodenergysports.utils.mapper.TransferMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClienteController implements IClientesService {

    private final TransferMapper<ClienteDto, Cliente> clienteTransferMapper;
    private final ClienteEntityRepository clienteRepository;
    private final EntityMapper<ClienteEntity,Cliente> clienteEntityMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public ClienteDto registrarCliente(ClienteDto clienteDto) {

        if(clienteDto == null) throw new NullPointerException("El cliente no puede ser nulo");
        if(clienteDto.getCodigo() != null){
            DomainException domainException = new DomainException("El código del cliente debe ser nulo");
            domainException.addCause("CODIGO", "No es posible insertar un nuevo cliente con un código predefinido");
            throw domainException;
        }
        Cliente cliente = clienteTransferMapper.mapToBusiness(clienteDto);
        cliente.validar();
        cliente.validarPasswordUsuario();
        //Hashea la contraseña del usuario;
        cliente.getUsuario().setPassword(passwordEncoder.encode(cliente.getUsuario().getPassword()));
        ClienteEntity clienteEntity = clienteEntityMapper.mapToEntity(cliente);
        clienteEntity = clienteRepository.save(clienteEntity);
        cliente = clienteEntityMapper.mapToBusiness(clienteEntity);
        return clienteTransferMapper.mapToDto(cliente);
    }

    //Implementar métodos para recuperar los datos del cliente, SOLO LO PODRA HACER EL CLIENTE AUTENTICADO
    //Se deberá pasar el authentication header como parámetro para comprobar que el usuario sea correcto
    //Antes de recuperar el nombre de usuario del token se deberá verificar el token, y en caso
    //De que haya una excepcion de token alterado retornar un error
    //PEro lo del token alterado se debería comprobar antes de que se ejecute el metodo en el RestController con el filtro

    //https://github.com/ronmamo/reflections
    //Con esa api puedo obtener todas las clases de un determinado package y comprobar que estén
    //Anotadas con @RestController y @RequestMapping
}
