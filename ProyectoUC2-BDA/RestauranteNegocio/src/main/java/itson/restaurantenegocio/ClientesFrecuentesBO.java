/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itson.restaurantenegocio;

import itson.restaurantedominio.ClienteFrecuente;
import itson.restaurantedtos.ClienteFrecuenteDTO;
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
public class ClientesFrecuentesBO {

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
}
