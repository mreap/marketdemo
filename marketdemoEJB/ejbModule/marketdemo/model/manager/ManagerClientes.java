package marketdemo.model.manager;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import marketdemo.model.entities.Cliente;

/**
 * Manejo de clientes
 */
@Stateless
@LocalBean
public class ManagerClientes {
	@EJB
	private ManagerDAO managerDAO;
    /**
     * Default constructor. 
     */
    public ManagerClientes() {
        
    }
	
  	/**
  	 * Metodo finder para la consulta de clientes.
  	 * Hace uso del componente {@link facturacion.model.manager.ManagerDAO ManagerDAO} de la capa model.
  	 * @return listado de clientes ordenados por apellidos.
  	 */
  	@SuppressWarnings("unchecked")
  	public List<Cliente> findAllClientes(){
  		return managerDAO.findAll(Cliente.class, "apellidos");
  	}
  	
  	/**
  	 * Metodo finder para la consulta de un cliente especifico.
  	 * @param cedula cedula del cliente que se desea buscar.
  	 * @return datos del cliente.
  	 * @throws Exception
  	 */
  	public Cliente findClienteById(String cedula) throws Exception{
  		Cliente cliente=null;
  		try {
  			cliente=(Cliente)managerDAO.findById(Cliente.class, cedula);
  		} catch (Exception e) {
  			e.printStackTrace();
  			throw new Exception("Error al buscar cliente: "+e.getMessage());
  		}
  		return cliente;
  	}
  	
  	/**
  	 * Permite guardar un nuevo cliente en la BDD.
  	 * @param cedula Cedula del nuevo cliente.
  	 * @param nombres Los nombres.
  	 * @param apellidos Los apellidos.
  	 * @param direccion La direccion del cliente.
  	 * @param clave La clave o password.
  	 * @throws Exception
  	 */
  	public void insertarCliente(String cedula,String nombres,
  			String apellidos,String direccion, String clave) throws Exception{
  		Cliente c=new Cliente();
  		c.setCedulaCliente(cedula);
  		c.setNombres(nombres);
  		c.setApellidos(apellidos);
  		c.setDireccion(direccion);
  		c.setClave(clave);
  		managerDAO.insertar(c);
  	}

}
