package marketdemo.model.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import marketdemo.model.entities.Cliente;
import marketdemo.model.entities.EstadoPedido;
import marketdemo.model.entities.PedidoCab;
import marketdemo.model.entities.PedidoDet;
import marketdemo.model.entities.Producto;
/**
 * Objeto que encapsula la logica basica de acceso a datos mediante JPA. Maneja
 * el patron de diseno singleton para administrar los componentes
 * EntityManagerFactory y EntityManager.
 * 
 * @author mrea
 *
 */
@Stateless
@LocalBean
public class ManagerPedidos {
	@EJB
	private ManagerDAO managerDAO;
	@EJB
	private ManagerFacturacion managerFacturacion;
	@EJB
	private ManagerProductos managerProductos;
	@EJB
	private ManagerAuditoria managerAuditoria;
	@EJB
	private ManagerClientes managerClientes;

	public ManagerPedidos() {
		
	}

	// MANEJO DE PEDIDOS:

	/**
	 * Metodo finder para la consulta de pedidos. Hace uso del componente
	 * {@link marketdemo.model.manager.ManagerDAO ManagerDAO} de la capa model.
	 * 
	 * @return Listado de pedidos ordenados por numero de pedido.
	 */
	@SuppressWarnings("unchecked")
	public List<PedidoCab> findAllPedidoCab() {
		List<PedidoCab> listado=managerDAO.findAll(PedidoCab.class, "o.numeroPedido asc");
		//recorremos los pedidos para extraer los datos de los detalles:
		for(PedidoCab pc:listado){
			for(PedidoDet pd:pc.getPedidoDets()){
				pd.getCantidad();
			}
		}
		return listado;
	}
	/**
	 * Busca un PedidoCab mediante su numero de pedido.
	 * @param numeroPedido El numero del pedido a buscar.
	 * @return El PedidoCab encontrado.
	 * @throws Exception
	 */
	public PedidoCab findPedidoCabById(Integer numeroPedido) throws Exception{
		PedidoCab pedidoCab;
		pedidoCab= (PedidoCab)managerDAO.findById(PedidoCab.class, numeroPedido);
		return pedidoCab;
	}
	/**
	 * Busca un conjunto de pedidos de compra dentro de un rango de fechas.
	 * Para buscar todos los pedidos sin excepcion, se debe pasar <b>null</b>
	 * en los parametros de fechas.
	 * @param fechaInicio fecha de inicio de la busqueda.
	 * @param fechaFinal fecha final de la busqueda.
	 * @return listado de pedidos resultante.
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public List<PedidoCab> findPedidoCabByFechas(Date fechaInicio,Date fechaFinal) throws Exception{
		List<PedidoCab> listado=null;
		if(fechaInicio==null||fechaFinal==null)
			return findAllPedidoCab();
		try{
			//Debido a que son insuficientes los metodos genericos de ManagerDAO,
			//creamos un nuevo Query:
			EntityManager em=managerDAO.getEntityManager();
			String sql="SELECT p FROM PedidoCab p WHERE p.fechaPedido between :fechaInicio and :fechaFinal order by p.numeroPedido asc";
			Query query=em.createQuery(sql);
			//pasamos los parametros a la consulta:
			query.setParameter("fechaInicio",fechaInicio,TemporalType.DATE);
			query.setParameter("fechaFinal",fechaFinal,TemporalType.DATE);
			//ejecutamos la consulta:
			listado=query.getResultList();
			//recorremos los pedidos para extraer los datos de los detalles:
			for(PedidoCab pc:listado){
				for(PedidoDet pd:pc.getPedidoDets()){
					pd.getCantidad();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		return listado;
	}
	

	/**
	 * Crea una nueva cabecera de pedido temporal, para que desde el programa
	 * cliente pueda manipularla y llenarle con la informacion respectiva. Esta
	 * informacion solo se mantiene en memoria.
	 * 
	 * @return el nuevo pedido temporal.
	 */
	public PedidoCab crearPedidoTmp() {
		PedidoCab pedidoCabTmp = new PedidoCab();
		pedidoCabTmp.setFechaPedido(new Date());
		pedidoCabTmp.setPedidoDets(new ArrayList<PedidoDet>());
		return pedidoCabTmp;
	}

	/**
	 * Asigna un cliente a un pedido temporal.
	 * 
	 * @param pedidoCabTmp Pedido temporal creado en memoria.
	 * @param cedulaCliente codigo del cliente.
	 * @throws Exception
	 */
	public void asignarClientePedidoTmp(PedidoCab pedidoCabTmp,
			String cedulaCliente) throws Exception {

		Cliente cliente = null;
		if (cedulaCliente == null || cedulaCliente.length() == 0)
			throw new Exception("Error debe especificar la cedula del cliente.");
		try {
			// invocamos al ManagerClientes:
			cliente = managerClientes.findClienteById(cedulaCliente);
			if (cliente == null)
				throw new Exception("Error al asignar cliente.");
			// si el pedido no esta creado, se crea automaticamente:
			if (pedidoCabTmp == null)
				pedidoCabTmp = crearPedidoTmp();

			pedidoCabTmp.setCliente(cliente);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error al asignar cliente: " + e.getMessage());
		}
	}

	/**
	 * Realiza los calculos de subtotales, sin impuestos.
	 * 
	 * @param pedidoCabTmp Pedido temporal creado en memoria.
	 */
	private void calcularPedidoTmp(PedidoCab pedidoCabTmp) {
		double sumaSubtotales;

		if (pedidoCabTmp == null)
			return;

		// verificamos los campos calculados:
		sumaSubtotales = 0;
		for (PedidoDet det : pedidoCabTmp.getPedidoDets()) {
			sumaSubtotales += det.getCantidad().intValue()
					* det.getPrecioUnitarioVenta().doubleValue();
		}

		pedidoCabTmp.setSubtotal(new BigDecimal(sumaSubtotales));
	}

	/**
	 * Adiciona un item detalle a un pedido temporal. Estos valores permanecen
	 * en memoria.
	 * 
	 * @param codigoProducto codigo del producto.
	 * @param cantidad cantidad del producto.
	 * @throws Exception problemas ocurridos al momento de insertar el item detalle.
	 */
	public void agregarDetallePedidoTmp(PedidoCab pedidoCabTmp,
			Integer codigoProducto, Integer cantidad) throws Exception {
		Producto prod;
		PedidoDet pedidoDet;

		// si no esta creado el pedido, lo creamos automaticamente:
		if (pedidoCabTmp == null)
			pedidoCabTmp = crearPedidoTmp();
		if (codigoProducto == null || codigoProducto.intValue() < 0)
			throw new Exception(
					"Error debe especificar el codigo del producto.");
		if (cantidad == null || cantidad.intValue() <= 0)
			throw new Exception(
					"Error debe especificar una cantidad mayor a cero.");

		// buscamos el producto:
		prod = managerProductos.findProductoById(codigoProducto);
		// creamos un nuevo detalle y llenamos sus propiedades:
		pedidoDet = new PedidoDet();
		pedidoDet.setCantidad(cantidad);
		pedidoDet.setPrecioUnitarioVenta(prod.getPrecioUnitario());
		pedidoDet.setProducto(prod);

		// asignacion bidireccional:
		pedidoDet.setPedidoCab(pedidoCabTmp);
		pedidoCabTmp.getPedidoDets().add(pedidoDet);

		// verificamos los campos calculados:
		calcularPedidoTmp(pedidoCabTmp);
	}

	/**
	 * Guarda en la base de datos un pedido.
	 * 
	 * @param pedidoCabTmp pedido temporal creado en memoria.
	 * @throws Exception problemas ocurridos en la insercion.
	 */
	public void guardarPedidoTemporal(PedidoCab pedidoCabTmp) throws Exception {

		if (pedidoCabTmp == null)
			throw new Exception("Debe crear un pedido primero.");
		if (pedidoCabTmp.getPedidoDets() == null
				|| pedidoCabTmp.getPedidoDets().size() == 0)
			throw new Exception("Debe ingresar los productos en el pedido.");
		if (pedidoCabTmp.getCliente() == null)
			throw new Exception("Debe registrar el cliente.");

		pedidoCabTmp.setFechaPedido(new Date());
		// asignamos el estado NUEVO al pedido:
		EstadoPedido estado = findEstadoPedidoById("NV");
		pedidoCabTmp.setEstadoPedido(estado);

		// verificamos los campos calculados:
		calcularPedidoTmp(pedidoCabTmp);

		// guardamos el pedido completo en la bdd. Hay que
		// tomar en cuenta que los codigos de cabecera y
		// detalles se crean automaticamente mediante
		// secuencias de base de datos:
		managerDAO.insertar(pedidoCabTmp);

	}

	public EstadoPedido findEstadoPedidoById(String idEstado) throws Exception {
		EstadoPedido estado = (EstadoPedido) managerDAO.findById(
				EstadoPedido.class, idEstado);
		return estado;
	}
	/**
	 * Genera una factura automaticamente mediante la informacion de un
	 * pedido especifico. Adicionalmente, el pedido que es despachado
	 * cambio de estado a "OK" (despachado).
	 * @param codigoUsuario Codigo del usuario que despacha el pedido.
	 * @param numeroPedido
	 * @throws Exception
	 */
	public void despacharPedido(String codigoUsuario,Integer numeroPedido) throws Exception{
		//recuperamos la informacion del pedido:
		PedidoCab pedidoCab=findPedidoCabById(numeroPedido);
		if(pedidoCab.getEstadoPedido().getIdEstadoPedido().equals("OK"))
			throw new Exception("Ya fue despachado el pedido.");
		if(pedidoCab.getEstadoPedido().getIdEstadoPedido().equals("AN"))
			throw new Exception("No puede despachar un pedido anulado.");
		//creamos la factura automaticamente:
		managerFacturacion.crearFacturaConPedido(pedidoCab);
		//si no existen excepciones, actualizamos el estado del pedido:
		EstadoPedido estado=findEstadoPedidoById("OK");
		pedidoCab.setEstadoPedido(estado);
		managerDAO.actualizar(pedidoCab);
		
		//generamos la pista de auditoria:
		managerAuditoria.crearEvento(codigoUsuario, 
				this.getClass(),"despacharPedido()", 
				"Pedido despachado nro: "+pedidoCab.getNumeroPedido());
	}
}
