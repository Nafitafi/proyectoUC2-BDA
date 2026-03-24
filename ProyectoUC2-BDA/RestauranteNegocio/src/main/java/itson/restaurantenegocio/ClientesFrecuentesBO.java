/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.restaurantenegocio;

import itson.restaurantedominio.ClienteFrecuente;
import itson.restaurantedtos.ClienteFrecuenteActualizadoDTO;
import itson.restaurantedtos.ClienteFrecuenteDTO;
import itson.restaurantedtos.ClienteFrecuenteNuevoDTO;
import static itson.restaurantenegocio.adapters.ClientesFrecuentesAClientesFrecuentesDTOAdapter.convertirADTO;
import itson.restaurantepersistencia.ClientesFrecuentesDAO;
import itson.restaurantepersistencia.IClientesFrecuentesDAO;
import itson.restaurantepersistencia.PersistenciaException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Andrea Lara, Nahomi Figueroa, Zaira Barajas
 */
public class ClientesFrecuentesBO implements IClientesFrecuentesBO{

    private static final Logger LOGGER = Logger.getLogger(ClientesFrecuentesBO.class.getName());
    private IClientesFrecuentesDAO clienteDAO;

    /**
     * Constructor por omisión.
     */
    public ClientesFrecuentesBO() {
        this.clienteDAO = new ClientesFrecuentesDAO();
    }

    
    
    /**
     * Método que se asegura de validar que la entrada de la busqueda es correcta.
     * 
     * @param busqueda filtro aplicado
     * @return Lista de objetos tipo ClienteFrecuenteDTO
     * @throws NegocioException en caso de que algo falle en el 
     */
    public List<ClienteFrecuenteDTO> buscarClientes(String busqueda) throws NegocioException {
        List<ClienteFrecuente> resultadosEntidad;
        try {
            // Si el buscador está vacío, se hace SELECT *
            if (busqueda == null || busqueda.trim().isEmpty()) {
                resultadosEntidad = clienteDAO.bucarTodosClientes();
            } 
            else { // Si el usuario si escribió algo, se manda
                resultadosEntidad = clienteDAO.buscarPorFiltro(busqueda.trim());
            }
            return convertirADTO(resultadosEntidad);
        }catch(PersistenciaException ex){
            LOGGER.severe(ex.getMessage());
            throw new NegocioException("No fue posible consultar a los clientes.");
        }
    }

    /**
     * Método intermedio entre la presentación y persistencia que verifica los datos recibidos del cliente a registrar 
     * para determinar si estos son válidos o no.
     * 
     * @param clienteNuevo DTO con los datos del cliente nuevo a registrar.
     * @return Objeto tipo ClienteFrecuente con los datos que fueron almacenados 
     * en la base de datos.
     * @throws NegocioException si algún dato es inválido o si surge un error en la capa de persistencia.
     */
    @Override
    public ClienteFrecuente crearCliente(ClienteFrecuenteNuevoDTO clienteNuevo) throws NegocioException {
        if (clienteNuevo.getNombre().length() > 100) {
            throw new NegocioException("El nombre es demasiado largo.");
        } else if (clienteNuevo.getNombre() == null){
            throw new NegocioException("El nombre es un campo obligatorio.");
        }
        
        if (clienteNuevo.getApellidoP().length() > 50) {
            throw new NegocioException("El apellido paterno es demasiado largo.");
        } else if (clienteNuevo.getApellidoP() == null){
            throw new NegocioException("El apellido paterno es un campo obligatorio.");
        }
        
        if (clienteNuevo.getApellidoM().length() > 50) {
            throw new NegocioException("El apellido materno es demasiado largo.");
        } else if (clienteNuevo.getApellidoM() == null){
            throw new NegocioException("El apellido paterno es un campo obligatorio.");
        }
        
        if (clienteNuevo.getNumeroTelefono().length() > 20) {
            throw new NegocioException("El número de teléfono es demasiado largo.");
        } else if (clienteNuevo.getNumeroTelefono() == null){
            throw new NegocioException("El teléfono es un campo obligatorio.");
        }
        
        if (clienteNuevo.getCorreo().length() > 100) {
            throw new NegocioException("El correo es demasiado largo.");
        }
        
        try {
            ClienteFrecuente cliente = clienteDAO.guardar(clienteNuevo);
            return cliente;
        } catch (PersistenciaException ex) {
            LOGGER.severe(ex.getMessage());
            throw new NegocioException("No fue posible crear al cliente.");
        }
    }

    /**
     * Método intermedio entre la presentación y persistencia que verifica los datos recibidos del cliente a actualizar 
     * para determinar si estos son válidos o no.
     * 
     * @param clienteActualizado DTO con la información a actualizar del
     * cliente.
     * @return Objeto tipo ClienteFrecuente con los datos que fueron actualizados 
     * en la base de datos.
     * @throws NegocioException si algún dato es inválido o si surge un error en la capa de persistencia.
     */
    @Override
    public ClienteFrecuente actualizarCliente(ClienteFrecuenteActualizadoDTO clienteActualizado) throws NegocioException {
        if (clienteActualizado.getNombre().length() > 100) {
            throw new NegocioException("El nombre es demasiado largo.");
        } 
        
        if (clienteActualizado.getApellidoP().length() > 50) {
            throw new NegocioException("El apellido paterno es demasiado largo.");
        } 
        
        if (clienteActualizado.getApellidoM().length() > 50) {
            throw new NegocioException("El apellido materno es demasiado largo.");
        } 
        
        if (clienteActualizado.getNumeroTelefono().length() > 20) {
            throw new NegocioException("El número de teléfono es demasiado largo.");
        } 
        
        if (clienteActualizado.getCorreo().length() > 100) {
            throw new NegocioException("El correo es demasiado largo.");
        }
        
        try {
            ClienteFrecuente cliente = clienteDAO.actualizar(clienteActualizado);
            return cliente;
        } catch (PersistenciaException ex) {
            LOGGER.severe(ex.getMessage());
            throw new NegocioException("No fue posible crear al cliente.");
        }
        
    }
}
