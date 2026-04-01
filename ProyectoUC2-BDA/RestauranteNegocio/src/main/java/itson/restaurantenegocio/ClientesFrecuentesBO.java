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
import java.util.logging.Logger;

/**
 *
 * @author Andrea Lara, Nahomi Figueroa, Zaira Barajas
 */
public class ClientesFrecuentesBO implements IClientesFrecuentesBO {

    private static final Logger LOGGER = Logger.getLogger(ClientesFrecuentesBO.class.getName());
    private IClientesFrecuentesDAO clienteDAO;

    /**
     * Constructor por omisión.
     */
    public ClientesFrecuentesBO() {
        this.clienteDAO = new ClientesFrecuentesDAO();
    }

    /**
     * Método que se asegura de validar que la entrada de la busqueda es
     * correcta.
     *
     * @param busqueda filtro aplicado
     * @return Lista de objetos tipo ClienteFrecuenteDTO
     * @throws NegocioException en caso de que algo falle en el
     */
    @Override
    public List<ClienteFrecuenteDTO> buscarClientes(String busqueda) throws NegocioException {
        List<ClienteFrecuente> resultadosEntidad;

        try {
            // Si el buscador está vacío, se hace SELECT *
            if (busqueda == null || busqueda.trim().isEmpty()) {
                resultadosEntidad = clienteDAO.bucarTodosClientes();
            } else {
                resultadosEntidad = clienteDAO.buscarPorFiltro(busqueda.trim());
            }

            //convertimos a clienteFrecuenteDTO para mostrarlo luego
            List<ClienteFrecuenteDTO> listaDTO = convertirADTO(resultadosEntidad);

            
            //A cada cliente le agregamos los datos de visitas, total gastado y los puntos calculados
            for (ClienteFrecuenteDTO dto : listaDTO) {
                Long id = dto.getId();

                dto.setVisitas(obtenerVisitas(id));
                dto.setTotalGastado(obtenerTotalGastado(id));
                dto.setPuntos(calcularPuntos(id));
            }

            return listaDTO;

        } catch (PersistenciaException ex) {
            LOGGER.severe(ex.getMessage());
            throw new NegocioException("No fue posible consultar a los clientes.");
        }
    }

    /**
     * Método intermedio entre la presentación y persistencia que verifica los
     * datos recibidos del cliente a registrar para determinar si estos son
     * válidos o no.
     *
     * @param clienteNuevo DTO con los datos del cliente nuevo a registrar.
     * @return Objeto tipo ClienteFrecuente con los datos que fueron almacenados
     * en la base de datos.
     * @throws NegocioException si algún dato es inválido o si surge un error en
     * la capa de persistencia.
     */
    @Override
    public ClienteFrecuente crearCliente(ClienteFrecuenteNuevoDTO clienteNuevo) throws NegocioException {
         if (clienteNuevo.getNombre() == null) {
            throw new NegocioException("El nombre es un campo obligatorio.");
        } else if (clienteNuevo.getNombre().length() > 100) {
            throw new NegocioException("El nombre es demasiado largo.");
        } 

        if (clienteNuevo.getApellidoP() == null) {
            throw new NegocioException("El apellido paterno es demasiado largo.");
        } else if (clienteNuevo.getApellidoP().length() > 50) {
            throw new NegocioException("El apellido paterno es un campo obligatorio.");
        }

        if (clienteNuevo.getApellidoM() == null) {
            throw new NegocioException("El apellido materno es demasiado largo.");
        } else if (clienteNuevo.getApellidoM().length() > 50) {
            throw new NegocioException("El apellido paterno es un campo obligatorio.");
        }

        if (clienteNuevo.getNumeroTelefono() == null) {
            throw new NegocioException("El número de teléfono es demasiado largo.");
        } else if (clienteNuevo.getNumeroTelefono().length() > 20) {
            throw new NegocioException("El teléfono es un campo obligatorio.");
        } else if (!clienteNuevo.getNumeroTelefono().matches("^(\\(\\+\\d{1,3}\\))?\\d{4}-\\d{2}-\\d{2}-\\d{2}$")) {
            throw new NegocioException("Formato de teléfono no válido.");
        }
        
        if (clienteNuevo.getCorreo() != null) {
            if (clienteNuevo.getCorreo().length() > 100) {
                throw new NegocioException("El correo es demasiado largo.");
            } else if (!clienteNuevo.getCorreo().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")){
                throw new NegocioException("Formato de correo no válido.");
            }
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
     * Método intermedio entre la presentación y persistencia que verifica los
     * datos recibidos del cliente a actualizar para determinar si estos son
     * válidos o no.
     *
     * @param clienteActualizado DTO con la información a actualizar del
     * cliente.
     * @return Objeto tipo ClienteFrecuente con los datos que fueron
     * actualizados en la base de datos.
     * @throws NegocioException si algún dato es inválido o si surge un error en
     * la capa de persistencia.
     */
    @Override
    public ClienteFrecuente actualizarCliente(ClienteFrecuenteActualizadoDTO clienteActualizado) throws NegocioException {
        if (clienteActualizado == null){
            throw new NegocioException("Cliente vacío, no hay cambios a realizar.");
        }
        
        if (clienteActualizado.getNombre() != null && clienteActualizado.getNombre().length() > 100) {
            throw new NegocioException("El nombre es demasiado largo.");
        }

        if (clienteActualizado.getApellidoP() != null && clienteActualizado.getApellidoP().length() > 50) {
            throw new NegocioException("El apellido paterno es demasiado largo.");
        }

        if (clienteActualizado.getApellidoM() != null && clienteActualizado.getApellidoM().length() > 50) {
            throw new NegocioException("El apellido materno es demasiado largo.");
        }

        if (clienteActualizado.getNumeroTelefono() != null && clienteActualizado.getNumeroTelefono().length() > 20) {
            throw new NegocioException("El número de teléfono es demasiado largo.");
        } else if (clienteActualizado.getNumeroTelefono() != null && !clienteActualizado.getNumeroTelefono().matches("^(\\(\\+\\d{1,3}\\))?\\d{4}-\\d{2}-\\d{2}-\\d{2}$")){
            throw new NegocioException("Formato de teléfono no válido.");
        }

        if (clienteActualizado.getCorreo() != null && clienteActualizado.getCorreo().length() > 100) {
            throw new NegocioException("El correo es demasiado largo.");
        } else if (clienteActualizado.getCorreo() != null && !clienteActualizado.getCorreo().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")){
            throw new NegocioException("Formato de correo no válido.");
        }

        try {
            ClienteFrecuente cliente = clienteDAO.actualizar(clienteActualizado);
            return cliente;
        } catch (PersistenciaException ex) {
            LOGGER.severe(ex.getMessage());
            throw new NegocioException("No fue posible crear al cliente.");
        }

    }

    /**
     * Método con validaciones que evita errores imprevistos al consultar datos
     * del cliente.
     *
     * @param IdClienteFrecuente Id correspondiente al cliente a consultar .
     * @return numero de visitas asociadas al cliente.
     * @throws NegocioException en caso de que algun dato no sea optimo.
     */
    @Override
    public Long obtenerVisitas(Long IdClienteFrecuente) throws NegocioException {

        if (IdClienteFrecuente == null) {
            throw new NegocioException("El cliente es obligatorio.");
        }

        try {
            ClienteFrecuente cliente = clienteDAO.buscarPorId(IdClienteFrecuente);

            if (cliente == null) {
                throw new NegocioException("El cliente no existe.");
            }

            Long visitas = clienteDAO.obtenerVisitasClienteFrecuente(IdClienteFrecuente);

            if (visitas == null || visitas < 0) {
                visitas = 0L;
            }

            return visitas;

        } catch (PersistenciaException ex) {
            LOGGER.severe(ex.getMessage());
            throw new NegocioException("No fue posible calcular las visitas.");
        }
    }

    /**
     * Método con validaciones que evita errores imprevistos al consultar datos
     * del cliente.
     *
     * @param IdClienteFrecuente Id correspondiente al cliente a consultar .
     * @return total gastado por el cliente.
     * @throws NegocioException en caso de que algun dato no sea optimo.
     */
    @Override
    public Double obtenerTotalGastado(Long IdClienteFrecuente) throws NegocioException {
        if (IdClienteFrecuente == null) {
            throw new NegocioException("El cliente es obligatorio.");
        }

        try {
            ClienteFrecuente cliente = clienteDAO.buscarPorId(IdClienteFrecuente);

            if (cliente == null) {
                throw new NegocioException("El cliente no existe.");
            }

            Double totalGastado = clienteDAO.obtenerTotalGastadoClienteFrecuente(IdClienteFrecuente);

            if (totalGastado == null || totalGastado < 0) {
                totalGastado = 0.0;
            }

            return totalGastado;

        } catch (PersistenciaException ex) {
            LOGGER.severe(ex.getMessage());
            throw new NegocioException("No fue posible calcular las visitas.");
        }
    }

    /**
     * Método que calcula los puntos de un cliente en especifico (en base a su
     * total gastado).
     *
     * @param IdClienteFrecuente Id correspondiente al cliente a calcular sus
     * puntos .
     * @return numero de puntos del cliente.
     * @throws NegocioException en caso de que algun dato no sea optimo.
     */
    @Override
    public int calcularPuntos(Long IdClienteFrecuente) throws NegocioException {

        Double total = obtenerTotalGastado(IdClienteFrecuente);

        if (total <= 0) {
            return 0;
        }

        return (int) Math.floor(total / 20);
    }
    
    /**
     * Método que consulta los datos de un cliente especifico por ID.
     * 
     * @param idCliente Id correspondiente al cliente a buscar
     * @return DTO del cliente que coincide con la busqueda
     * @throws NegocioException en caso de haber algun fallo en la consulta
     */
    @Override
    public ClienteFrecuenteDTO obtenerClientePorId(Long idCliente) throws NegocioException {
        try {
            ClienteFrecuente cliente = clienteDAO.buscarPorId(idCliente);
            if (cliente == null) {
                return null;
            }
            ClienteFrecuenteDTO clienteDTO = new ClienteFrecuenteDTO(
                    cliente.getIdCliente(), 
                    cliente.getNombre(), 
                    cliente.getApellidoP(), 
                    cliente.getApellidoM(), 
                    cliente.getNumeroTelefono(), 
                    cliente.getCorreo()
            );
            clienteDTO.setPuntos(cliente.getPuntos());
            return clienteDTO;

        } catch (PersistenciaException ex) { 
            throw new NegocioException("Error al consultar el cliente por ID: " + ex.getMessage());
        }
    }
}
