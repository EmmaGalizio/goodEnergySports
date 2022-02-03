package emma.galzio.goodenergysports.productos.admin.controller;

import emma.galzio.goodenergysports.productos.admin.transferObject.ImagenProductoDto;
import emma.galzio.goodenergysports.productos.admin.transferObject.ProductoAdminDto;
import emma.galzio.goodenergysports.productos.commons.domain.ImagenProducto;
import emma.galzio.goodenergysports.productos.commons.domain.Producto;
import emma.galzio.goodenergysports.productos.commons.domain.Talle;
import emma.galzio.goodenergysports.productos.commons.persistence.entity.CategoriaEntity;
import emma.galzio.goodenergysports.productos.commons.persistence.entity.ImagenProductoEntity;
import emma.galzio.goodenergysports.productos.commons.persistence.entity.ProductoEntity;
import emma.galzio.goodenergysports.productos.commons.persistence.entity.TalleEntity;
import emma.galzio.goodenergysports.productos.commons.persistence.repository.CategoriaEntityRepository;
import emma.galzio.goodenergysports.productos.commons.persistence.repository.ImagenProductoEntityRepository;
import emma.galzio.goodenergysports.productos.commons.persistence.repository.ProductoRepository;
import emma.galzio.goodenergysports.productos.commons.persistence.repository.TalleRepository;
import emma.galzio.goodenergysports.utils.StorageService;
import emma.galzio.goodenergysports.utils.exception.DomainException;
import emma.galzio.goodenergysports.utils.exception.FileStorageException;
import emma.galzio.goodenergysports.utils.exception.PersistenceException;
import emma.galzio.goodenergysports.utils.mapper.BusinessToDtoMapper;
import emma.galzio.goodenergysports.utils.mapper.EntityMapper;
import emma.galzio.goodenergysports.utils.mapper.TransferMapper;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service("productoAdminService")
public class ProductoAdminService implements IProductoAdminService {


    public enum ORDER{
        DEFAULT("Codigo", "codigoProducto", "ASC"),
        MENOR_PRECIO( "Menor Precio", "precio", "ASC"),
        MAYOR_PRECIO("Mayor Precio", "precio", "DESC"),
        MENOR_FECHA_ALTA("Menor Fecha Alta", "fechaAlta", "ASC"),
        MAYOR_FECHA_ALTA( "Mayor Fecha Alta", "fechaAlta", "DESC"),
        MENOR_FECHA_BAJA("Menor Fecha Baja", "fechaBaja", "ASC"),
        MAYOR_FECHA_BAJA("Mayor Fecha Baja", "fechaBaja", "DESC");

        private final String value;
        private final String field;
        private final String dir;

        ORDER(String value, String field, String dir) {
            this.value = value;
            this.field = field;
            this.dir = dir;
        }

        public String getValue() {
            return value;
        }
        public String getField() {
            return field;
        }
        public String getDir(){
            return dir;
        }
    }

    @Value("${goodEnergy.WebResourceDirectory}")
    private String resourcesRootDirectory;

    @Autowired
    @Qualifier("productoAdminTransferMapper")
    private TransferMapper<ProductoAdminDto, Producto> productoTransferMapper;
    @Autowired
    @Qualifier("productoEntityMapper")
    private EntityMapper<ProductoEntity, Producto> productoEntityMapper;
    @Autowired
    @Qualifier("talleCategoriaEntityMapper")
    private EntityMapper<TalleEntity, Talle> talleCategoriaEntityMapper;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private CategoriaEntityRepository categoriaEntityRepository;
    @Autowired
    private ImagenProductoEntityRepository imagenProductoEntityRepository;
    @Autowired
    private ServletContext servletContext;
    @Autowired
    private BusinessToDtoMapper<ImagenProductoDto, ImagenProducto> imagenProductoTransferMapper;
    @Autowired
    private TalleRepository talleRepository;
    @Autowired
    @Qualifier("imagenProductoEntityMapper")
    private EntityMapper<ImagenProductoEntity, ImagenProducto> imagenProductoEntityMapper;

    @Override
    @Transactional
    public ProductoAdminDto createNewProducto(ProductoAdminDto productoAdminDto) {

        if(productoAdminDto.getImagenes() == null || productoAdminDto.getImagenes().isEmpty()){
            DomainException domainException = new DomainException("El producto incluir al menos una imagen");
            domainException.addCause("IMAGEN", "El producto debe incluir al menos una imagen");
            throw domainException;
        }
        Producto producto = productoTransferMapper.mapToBusiness(productoAdminDto);
        producto.setFechaAlta(LocalDate.now());
        Integer codigo = productoRepository.getLastId();
        producto.setCodigo(codigo != null ? codigo+1 : 1);
        producto.validate();

        StorageService storageService = new StorageService();
        for(ImagenProducto imagen : producto.getImagenes()){
            Path path = this.persistImage(imagen, storageService,producto);
            imagen.setRutaArchivo(path.toAbsolutePath().toString());
        }
      
        ProductoEntity persistedProductoEntity = productoEntityMapper.mapToEntity(producto);
        List<TalleEntity> tallesDeCategoria = talleRepository.findByCategoria(persistedProductoEntity.getCategoria());
        if(tallesDeCategoria != null && !tallesDeCategoria.isEmpty()) {
            List<Talle> talles = talleCategoriaEntityMapper.mapAllToBusiness(tallesDeCategoria);
            producto.validarStockTallesActivos(talles);
        }
        try{
            persistedProductoEntity = productoRepository
                    .save(persistedProductoEntity);
        }catch(Exception e){
            storageService.deleteFiles(producto.getImagenes().stream().map(ImagenProducto::getRutaArchivo).collect(Collectors.toList()));
            throw new PersistenceException("Ocurrió un error al momento de almacenar el nuevo producto!");
        }
        Producto finalProducto = productoEntityMapper.mapToBusiness(persistedProductoEntity);
        return productoTransferMapper.mapToDto(finalProducto);
    }

    private Path persistImage(ImagenProducto imagenProducto, StorageService storageService, Producto producto){

        String extension = imagenProducto.getExtension();

        String fileName = producto.getCodigo().toString() +"-"+ imagenProducto.getOrden() + "."+extension;
       return this.persistImage(imagenProducto, storageService, fileName);
    }
    private Path persistImage(ImagenProducto imagenProducto, StorageService storageService, String nuevoNombre){
        String extension = FilenameUtils.getExtension(imagenProducto.getNombreOriginal());
        if(Arrays.stream(StorageService.IMAGE_FILE_EXTENTIONS).noneMatch(ext -> ext.equals(extension.trim().toUpperCase()))){
            DomainException domainException = new DomainException("El archivo provisto no corresponde con un tipo valido");
            domainException.addCause("TIPO_ARCHIVO", StorageService.VALID_IMAGE_EXTENTIONS);
            throw domainException;
        }
        //String fileName = producto.getCodigo().toString() +"-"+ imagenProducto.getOrden() + "."+extension;
        if(Files.exists(Path.of(resourcesRootDirectory+"/images/producto"+nuevoNombre))){
            throw new FileStorageException("Ya existe almacenada una imagen con el mismo orden para el producto especidicado");
        }
        return storageService.storeBase64ProductoImage(nuevoNombre, resourcesRootDirectory+"/images/producto", imagenProducto);
    }

    @Transactional
    @Override
    public List<ProductoAdminDto> listAllProductos(boolean active, Integer page, Integer size, ORDER order, Integer categoria){

        Sort sort = Sort.by(Sort.Direction.valueOf(order.getDir()),order.getField());
        Pageable pageable = PageRequest.of(page-1,size,sort);

        Page<ProductoEntity> result = null;

        if(!active && categoria == null){
            result = productoRepository.findBy(pageable);
        }
        if(active && categoria == null){
            result = productoRepository.findByFechaBajaIsNull(pageable);
        }
        if(!active && categoria != null){
            CategoriaEntity categoriaEntity;
            try {
                categoriaEntity = categoriaEntityRepository.getById(categoria);
            }catch (Exception e){
                throw new DomainException("No existe una categoria con el ID especificado");
            }
            result = productoRepository.findByCategoria(categoriaEntity,pageable);
        }
        if(active && categoria != null){
            CategoriaEntity categoriaEntity;
            try {
                categoriaEntity = categoriaEntityRepository.getById(categoria);
            }catch (Exception e){
                throw new DomainException("No existe una categoria con el ID especificado");
            }
            result = productoRepository.findByCategoriaAndFechaBajaIsNull(categoriaEntity,pageable);
        }
        if(!result.hasContent()){
            return Collections.emptyList();
        }
        List<Producto> productos = productoEntityMapper.mapAllToBusiness(result.getContent());

        productos.forEach(Producto::validate);

        return productoTransferMapper.mapAllToDto(productos);
    }
/*
    private Producto findBussinesProduct(Integer codigo){
        if(codigo == null) throw new NullPointerException("El código debe ser distinto de null");

        try {
            ProductoEntity productoEntity = productoRepository.getById(codigo);
            Producto producto = productoEntityMapper.mapToBusiness(productoEntity);
            producto.validate();
            return producto;
        }catch (EntityNotFoundException e){
            throw new DomainException("No existe un producto con el código especificado: " + codigo );
        }

    }*/

    @Override
    public ProductoAdminDto findProducto(Integer codigo) {

        Producto producto = this.buscarProductoBusiness(codigo);
        return productoTransferMapper.mapToDto(producto);
    }

    @Override
    @Transactional
    public ProductoAdminDto productoWithdraw(Integer codigo) {
        if(codigo == null) throw new NullPointerException("El código del producto no puede ser nulo");

        ProductoEntity productoEntity;
        Producto producto;
        try{
            productoEntity = productoRepository.getById(codigo);
            producto = productoEntityMapper.mapToBusiness(productoEntity);
        }catch (Exception e){
            throw new DomainException("No existe un producto con el código indicado");
        }

        if(!producto.estaActivo()) throw new DomainException("El producto ya se dió de baja en: " +producto.getFechaBaja());
        producto.registrarBaja();
        producto.validate();
        try {
            productoEntity = productoRepository.save(productoEntityMapper.mapToEntity(producto));
        } catch(Exception e){
            throw new DomainException("No se pudo registrar la baja del producto");
        }
        producto = productoEntityMapper.mapToBusiness(productoEntity);
        return productoTransferMapper.mapToDto(producto);
    }

    @Override
    @Transactional
    /**
     * Actualiza un pedido, permite agregar o modificar el orden de las imágenes pero no permite su eliminacion
     * Para eliminar imágenes se debe utilizar el endpoint creado para tal fin
     * Actualiza el stock disponible, pero para dar de baja el stock para un determinado talle se
     * debe utilizar el endpoint creado para tal fin
     */
    public ProductoAdminDto updateProducto(ProductoAdminDto productoAdminDto, Integer codigo) {

        if(productoAdminDto.getImagenes() == null || productoAdminDto.getImagenes().isEmpty()){
            DomainException domainException = new DomainException("El producto incluir al menos una imagen");
            domainException.addCause("IMAGEN", "El producto debe incluir al menos una imagen");
            throw domainException;
        }
        Producto producto = productoTransferMapper.mapToBusiness(productoAdminDto);
        producto.validate();
        ProductoEntity productoEntityPersistido = productoRepository.getById(codigo);

        Producto persistedProducto = productoEntityMapper.mapToBusiness(productoEntityPersistido);
        if(persistedProducto.estaActivo() && !producto.estaActivo()){
            DomainException domainException =  new DomainException("No es posible realizar la baja del producto mediante este método, en su lugar utilice el metodo DELETE");
            domainException.addCause("BAJA_PRODUCTO", "No es posible realizar la baja del producto mediante el formulario de actualización");
            throw  domainException;
        }
        if(!producto.validarImagenesAModificar(persistedProducto.getImagenes())){
            DomainException domainException = new DomainException("No es posible eliminar imágenes mediante este método");
            domainException.addCause("IMAGEN", "No es posible eliminar imágenes mediante este método");
            throw domainException;
        }
        producto.validarStockModificado(persistedProducto.getStock()); //Retorna un false si elimino algun item de stock que esté dado de baja
        //Se eliminan los items de stock modificados de la lista cuando el stock o el talle está dado de baja.
        //Pero no se eliminan de la base de datos porque Hibernate no elimina elementos faltantes de una coleccion
        //Tendría que ver una forma de avisar al usuario que se eliminaron algunos items por estar dados de baja
        if(!persistedProducto.estaActivo() && producto.estaActivo() && !producto.getCategoria().estaActiva()){
            DomainException domainException = new DomainException("No es posible activar el producto debido a que la categoría a la que pertenece está inactiva!");
            domainException.addCause("CATEGORIA_INACTIVA","Para poder activar este producto primero es necesario activar la categoría a la que pertenece");
        }
        List<String> persistedImages = new LinkedList<>();
        boolean necesitaRenombrar = false;
        StorageService storageService = null;
        for(ImagenProducto imagenProducto : producto.getImagenes()){

            if(!imagenProducto.isUpToDate()){
                necesitaRenombrar = true;
                if(imagenProducto.isNew()){
                    storageService = new StorageService();
                    try{
                        String nuevoNombre = producto.getCodigo()+"-aux-"+imagenProducto.getOrden()+"."+imagenProducto.getExtension();
                        Path path = this.persistImage(imagenProducto,storageService,nuevoNombre);
                        imagenProducto.setRutaArchivo(path.toAbsolutePath().toString());
                        persistedImages.add(imagenProducto.getRutaArchivo());
                    }catch (Exception e){
                        storageService.deleteFiles(persistedImages);
                        throw new FileStorageException("Ocurrió un error mientras se almacenaban las nuevas imágenes", e);
                    }
                }else{
                    imagenProducto.setRutaArchivo(imagenProductoEntityRepository
                            .getImageUrl(producto.getCodigo(), imagenProducto.getUriOrder()));
                }
            } else{
                imagenProducto.setRutaArchivo(imagenProductoEntityRepository.getImageUrl(producto.getCodigo(), imagenProducto.getOrden()));
            }
        }
        if(necesitaRenombrar){
            Map<Integer, String> nombresAuxiliares = new Hashtable<>();
            try {
                this.renombrarImagenes(producto, nombresAuxiliares, true);
                this.renombrarImagenes(producto, nombresAuxiliares, false);
            } catch(IOException e){
                if(storageService == null) storageService = new StorageService();
                storageService.deleteFiles(persistedImages);
                throw new FileStorageException("Ocurrió un error al renombrar las imágenes");
            }
        }
        ProductoEntity productoEntity = productoRepository.save(productoEntityMapper.mapToEntity(producto));
        producto = productoEntityMapper.mapToBusiness(productoEntity);
        return productoTransferMapper.mapToDto(producto);
    }

    private void renombrarImagenes(Producto producto,Map<Integer, String> nombresAuxiliares, boolean esAuxiliar) throws IOException{
        String basePath = resourcesRootDirectory+"/images/producto";
        for(ImagenProducto imagenProducto: producto.getImagenes()){
            String nuevoNombre;
            Path rutaAntigua;
            if(esAuxiliar){
                rutaAntigua = Path.of(imagenProducto.getRutaArchivo());
                nuevoNombre = producto.getCodigo()+"-aux-"+imagenProducto.getOrden()+"."+imagenProducto.getExtension();
                nombresAuxiliares.put(imagenProducto.getOrden(),basePath+ File.separator+nuevoNombre);
            }else{
                rutaAntigua = Path.of(nombresAuxiliares.get(imagenProducto.getOrden()));
                nuevoNombre = producto.getCodigo()+"-"+imagenProducto.getOrden()+"."+imagenProducto.getExtension();
            }
            Path nuevaRuta = Path.of(basePath,nuevoNombre);
            Files.move(rutaAntigua, nuevaRuta, StandardCopyOption.REPLACE_EXISTING);
            imagenProducto.setRutaArchivo(nuevaRuta.toString());
        }
    }

    @Override
    public Integer countProducts(boolean active, Integer idCategoria) {

        Integer count;
        if(idCategoria != null){
            CategoriaEntity categoriaEntity;
            try {
                categoriaEntity = categoriaEntityRepository.getById(idCategoria);
            }catch(EntityNotFoundException e){
                DomainException domainException = new DomainException("No existe una categoria con el Id indicado");
                domainException.addCause("ID_CATEGORIA", "No existe una categoría con el Id indicado");
                throw domainException;
            }
            count = active ? productoRepository
                    .countByCategoriaAndFechaBajaIsNull(categoriaEntity) :
                    productoRepository.countByCategoria(categoriaEntity);
        } else{
            count = active ? productoRepository.countByFechaBajaIsNull() :
                    productoRepository.countBy();
        }
        return count;
    }


    @Override
    public List<ImagenProductoDto> listAllImages(Integer codigo) {

        if(codigo == null) throw new NullPointerException("El código del producto no puede ser nulo");
        ProductoEntity productoEntity;
        Integer imagesCount;
        try{
            productoEntity = productoRepository.getById(codigo);
            imagesCount = imagenProductoEntityRepository.countByProducto(productoEntity);
        }catch(Exception e){
            throw new DomainException("No existe un producto con el código especificado");
        }
        Pageable pageable = PageRequest.of(0, imagesCount);
        Page<ImagenProductoEntity> imagenesEntities = imagenProductoEntityRepository
                                                            .findByProducto(productoEntity,pageable);

        String rootImagesPath = servletContext.getContextPath().concat("/resources/images/producto/");

        List<ImagenProductoDto> imagenProductoDtos = new LinkedList<>();

        for(ImagenProductoEntity imagenEntity : imagenesEntities.getContent()){
            ImagenProductoDto imagenDto= new ImagenProductoDto();
            imagenDto.setOrden(imagenEntity.getOrden());
            String imageName = FilenameUtils.getName(imagenEntity.getUrl());
            imagenDto.setUri(rootImagesPath.concat(imageName));
            imagenProductoDtos.add(imagenDto);
        }
        imagenProductoDtos.sort(ImagenProductoDto.compareByOrden());
        return imagenProductoDtos;
    }

    @Transactional
    public List<ImagenProductoDto> deleteImage(Integer codigoProducto, Integer orden){

        if(codigoProducto == null || orden == null) throw new NullPointerException("El Código de producto ni el orden de la imagen pueden ser nulos");

        ProductoEntity productoEntity = new ProductoEntity();
        productoEntity.setCodigoProducto(codigoProducto);
        ImagenProductoEntity imagenProducto;
        String imagePath;
        try{
            imagenProducto = imagenProductoEntityRepository
                    .findByProductoAndOrden(productoEntity, orden);
            imagePath = imagenProducto.getUrl();
        }catch (Exception e){
            throw new DomainException("No existe una imagen con el codigo de producto y orden indicado");
        }
        try{
            StorageService storageService = new StorageService();
            storageService.deleteFile(imagePath);
        }catch(Exception e){
            throw new FileStorageException("No se pudo eliminar la imagen del sistema de archivos");
        }
        productoEntity = productoRepository.getById(codigoProducto);
        Producto producto = productoEntityMapper.mapToBusiness(productoEntity);
        producto.ordenarImageges();
        if(!producto.eliminarImagen(orden)) throw new DomainException("No existe una imágen con el orden indicado");
        int ultimoOrden = producto.reOrganizarImagenes();
        Map<Integer, String> nombresAuxiliares = new Hashtable<>();
        try {
            this.renombrarImagenes(producto,nombresAuxiliares,true);
            this.renombrarImagenes(producto, nombresAuxiliares, false);
        } catch (IOException ioException) {
            throw new DomainException("Error al renombrar las imágenes");
        }
        imagenProductoEntityRepository
                .deleteImagenesProductoOrdenMayorQue(productoEntity, ultimoOrden);
        return imagenProductoTransferMapper.mapAllToDto(producto.getImagenes());
    }

    @Override
    public ImagenProductoDto findProductImage(Integer codigo, Integer orden){

        ImagenProducto imagen = null;
        Producto producto = this.buscarProductoBusiness(codigo);
        for(ImagenProducto imagenProductoItem : producto.getImagenes()){
            if(imagenProductoItem.getOrden().equals(orden)){
                imagen = imagenProductoItem;
                break;
            }
        }
        if(imagen == null){
            DomainException domainException = new DomainException("No existe la imagen número "+ orden + "para el producto" + codigo);
            domainException.addCause("ORDEN_IMAGEN","No existe la imagen número "+ orden + "para el producto" + codigo);
            throw domainException;
        }
        return imagenProductoTransferMapper.mapToDto(imagen);
    }
    @Override
    public Producto buscarProductoBusiness(Integer codigo) {
        if(codigo == null) throw new NullPointerException("El código del producto no puede ser nulo");
        ProductoEntity productoEntity = productoRepository.getById(codigo);
        try{
            Producto producto = productoEntityMapper.mapToBusiness(productoEntity);
            producto.validate();
            return producto;
        }catch(EntityNotFoundException e){
            DomainException domainException = new DomainException("El codigo proporcionado no corresponde a un producto registrado");
            domainException.addCause("PRODUCTO", "El código proporcionado no corresponde a un producto registrado");
            throw domainException;
        }

    }

    @Override
    public ProductoEntity buscarProductoEntity(Integer codigo) {
        ProductoEntity productoEntity = productoRepository.getById(codigo);
        try{
            System.out.println(productoEntity.getCodigoProducto());
        }catch(EntityNotFoundException e){
            DomainException domainException = new DomainException("El codigo proporcionado no corresponde a un producto registrado");
            domainException.addCause("PRODUCTO", "El código proporcionado no corresponde a un producto registrado");
            throw domainException;
        }
        return productoEntity;
    }
}
