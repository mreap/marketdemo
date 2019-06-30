package marketdemo.model.manager;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import marketdemo.model.entities.Producto;

/**
 * Logica de manejo de productos.
 */
@Stateless
@LocalBean
public class ManagerProductos {
	@EJB
	private ManagerDAO managerDAO;
    /**
     * Default constructor. 
     */
    public ManagerProductos() {
        
    }
	
  	public int obtenerExistencia(Integer codigoProducto) throws Exception{
  		Producto p;
  		p=findProductoById(codigoProducto);
  		return p.getExistencia().intValue();
  	}
  	/**
  	 * Metodo finder para consulta de productos.
  	 * Hace uso del componente {@link marketdemo.model.manager.ManagerDAO ManagerDAO} de la capa model.
  	 * @return listado de Productos ordenados por nombre.
  	 */
  	@SuppressWarnings("unchecked")
  	public List<Producto> findAllProductos(){
  		return managerDAO.findAll(Producto.class, "o.nombre");
  	}
  	
  	/**
  	 * Metodo finder para consulta de productos.
  	 * Hace uso del componente {@link marketdemo.model.manager.ManagerDAO ManagerDAO} de la capa model.
  	 * @param codigoProducto codigo del producto que se desea buscar.
  	 * @return el producto encontrado.
  	 * @throws Exception
  	 */
  	public Producto findProductoById(Integer codigoProducto) throws Exception{
  		return (Producto) managerDAO.findById(Producto.class, codigoProducto);
  	}
  	
  	/**
  	 * Guarda un nuevo producto en la base de datos.
  	 * Hace uso del componente {@link marketdemo.model.manager.ManagerDAO ManagerDAO} de la capa model.
  	 * @param p El nuevo producto.
  	 * @throws Exception
  	 */
  	public void insertarProducto(Producto p) throws Exception{
  		managerDAO.insertar(p);
  	}
  	
  	/**
  	 * Borra de la base de datos un producto especifico.
  	 * Hace uso del componente {@link marketdemo.model.manager.ManagerDAO ManagerDAO} de la capa model.
  	 * @param codigoProducto el codigo del producto que se desea eliminar.
  	 * @throws Exception
  	 */
  	public void eliminarProducto(Integer codigoProducto) throws Exception{
  		managerDAO.eliminar(Producto.class, codigoProducto);
  	}
  	
  	/**
  	 * Actualiza la informacion de un producto en la base de datos.
  	 * Hace uso del componente {@link marketdemo.model.manager.ManagerDAO ManagerDAO} de la capa model.
  	 * @param producto Los datos del producto que se desea actualizar.
  	 * @throws Exception
  	 */
  	public void actualizarProducto(Producto producto) throws Exception{
  		Producto p=null;
  		try {
  			//buscamos el producto a modificar desde la bdd:
  			p=findProductoById(producto.getCodigoProducto());
  			//actualizamos las propiedades:
  			p.setDescripcion(producto.getDescripcion());
  			p.setExistencia(producto.getExistencia());
  			p.setNombre(producto.getNombre());
  			p.setPrecioUnitario(producto.getPrecioUnitario());
  			p.setTieneImpuesto(producto.getTieneImpuesto());
  			//actualizamos:
  			managerDAO.actualizar(p);
  		} catch (Exception e) {
  			e.printStackTrace();
  			throw new Exception(e.getMessage());
  		}
  	}
}
