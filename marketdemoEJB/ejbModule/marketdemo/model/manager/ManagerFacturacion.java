package marketdemo.model.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import marketdemo.model.entities.Cliente;
import marketdemo.model.entities.FacturaCab;
import marketdemo.model.entities.FacturaDet;
import marketdemo.model.entities.PedidoCab;
import marketdemo.model.entities.PedidoDet;
import marketdemo.model.entities.Producto;
import marketdemo.model.entities.Usuario;

/**
 * Clase que implementa las reglas de negocio relacionadas a la facturacion del sistema de marketdemo.
 * @author mrea
 *
 */
@Stateless
@LocalBean
public class ManagerFacturacion {
	@EJB
	private ManagerDAO managerDAO;
	@EJB
	private ManagerClientes managerClientes;
	@EJB
	private ManagerProductos managerProductos;
	@EJB
	private ManagerParametros managerParametros;
	@EJB
	private ManagerSeguridad managerSeguridad;
	
	
	public ManagerFacturacion(){
		
	}
	
	/**
  	 * Obtiene el valor actual para el porcentaje de impuesto IVA.
  	 * @return valor del IVA
	 * @throws Exception 
  	 */
  	public double getPorcentajeIVA() throws Exception{
  		return managerParametros.getValorParametroDouble("valor_iva");
  	}
	
	/**
	 * Retorna el valor actual del contador de facturas. 
	 * Este contador es un parametro del sistema.
	 * @return ultimo valor del contador de facturas
	 * @throws Exception
	 */
	private int getContadorFacturas() throws Exception{
		return managerParametros.getValorParametroInteger("cont_facturas");
	}
	
	/**
	 * Actualiza el valor del contador de facturas.
	 * @param nuevoContadorFacturas nuevo valor del contador.
	 * @throws Exception
	 */
	private void actualizarContFacturas(int nuevoContadorFacturas) throws Exception{
		managerParametros.actualizarParametro("cont_facturas", nuevoContadorFacturas);
	}
	
	//MANEJO DE FACTURAS:
	
	/**
	 * Metodo finder para la consulta de facturas.
	 * Hace uso del componente {@link marketdemo.model.manager.ManagerDAO ManagerDAO} de la capa model.
	 * @return Listado de facturas ordenadas por fecha de emision y numero de factura.
	 */
	@SuppressWarnings("unchecked")
	public List<FacturaCab> findAllFacturaCab(){
		List<FacturaCab> listado= managerDAO.findAll(FacturaCab.class, "o.fechaEmision desc,o.numeroFactura desc");
		//recorremos las facturas cabecera para extraer los datos de los detalles:
		for(FacturaCab fc:listado){
			for(FacturaDet fd:fc.getFacturaDets()){
				fd.getCantidad();
			}
		}
		return listado;
	}
	
	/**
	 * Crea una nueva cabecera de factura temporal, para que desde el programa
	 * cliente pueda manipularla y llenarle con la informacion respectiva.
	 * Esta informacion solo se mantiene en memoria.
	 * @return la nueva factura temporal.
	 */
	public FacturaCab crearFacturaTmp(){
		FacturaCab facturaCabTmp=new FacturaCab();
		facturaCabTmp.setFechaEmision(new Date());
		facturaCabTmp.setFacturaDets(new ArrayList<FacturaDet>());
		return facturaCabTmp;
	}
	
	/**
	 * Asigna un cliente a una factura temporal.
	 * @param facturaCabTmp Factura temporal creada en memoria.
	 * @param cedulaCliente codigo del cliente.
	 * @throws Exception
	 */
	public void asignarClienteFacturaTmp(FacturaCab facturaCabTmp,String cedulaCliente) throws Exception{
		
		Cliente cliente=null;
		if(cedulaCliente==null||cedulaCliente.length()==0)
			throw new Exception("Error debe especificar la cedula del cliente.");
		try {
			cliente=managerClientes.findClienteById(cedulaCliente);
			if(cliente==null)
				throw new Exception("Error al asignar cliente.");
			facturaCabTmp.setCliente(cliente);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error al asignar cliente: "+e.getMessage());
		}
	}
	
	/**
	 * Realiza los calculos de subtotales, impuestos y totales.
	 * @param facturaCabTmp Factura temporal creada en memoria.
	 * @throws Exception 
	 */
	private void calcularFacturaTmp(FacturaCab facturaCabTmp) throws Exception{
		double sumaSubtotales;
		double porcentajeIVA,valorIVA,totalFactura;
		//verificamos los campos calculados:
		sumaSubtotales=0;
		for(FacturaDet det:facturaCabTmp.getFacturaDets()){
			sumaSubtotales+= det.getCantidad().intValue() * det.getPrecioUnitarioVenta().doubleValue();
		}
		
		porcentajeIVA=getPorcentajeIVA();
		valorIVA=sumaSubtotales*porcentajeIVA/100;
		totalFactura=sumaSubtotales+valorIVA;
		
		facturaCabTmp.setSubtotal(new BigDecimal(sumaSubtotales));
		facturaCabTmp.setValorIva(new BigDecimal(valorIVA));
		facturaCabTmp.setBaseCero(new BigDecimal(0));//no calculamos la base cero en este ejemplo.
		facturaCabTmp.setTotal(new BigDecimal(totalFactura));
	}
	
	/**
	 * Adiciona un item detalle a una factura temporal. Estos valores permanecen
	 * en memoria. 
	 * @param codigoProducto codigo del producto.
	 * @param cantidad cantidad del producto.
	 * @throws Exception problemas ocurridos al momento de insertar el item detalle.
	 */
	public void agregarDetalleFacturaTmp(FacturaCab facturaCabTmp,Integer codigoProducto,Integer cantidad) throws Exception{
		Producto p;
		FacturaDet fd;	
		
		if(facturaCabTmp==null)
			throw new Exception("Error primero debe crear una nueva factura.");
		if(codigoProducto==null||codigoProducto.intValue()<0)
			throw new Exception("Error debe especificar el codigo del producto.");
		if(cantidad==null||cantidad.intValue()<=0)
			throw new Exception("Error debe especificar la cantidad del producto.");
		
		//buscamos el producto:
		p=managerProductos.findProductoById(codigoProducto);
		//creamos un nuevo detalle y llenamos sus propiedades:
		fd=new FacturaDet();
		fd.setCantidad(cantidad);
		fd.setPrecioUnitarioVenta(p.getPrecioUnitario());
		fd.setProducto(p);
		facturaCabTmp.getFacturaDets().add(fd);
		
		//verificamos los campos calculados:
		calcularFacturaTmp(facturaCabTmp);
	}
	
	/**
	 * Guarda en la base de datos una factura.
	 * @param codigoUsuario Codigo del usuario que genera la factura.
	 * @param facturaCabTmp factura temporal creada en memoria.
	 * @throws Exception problemas ocurridos en la insercion.
	 */
	public void guardarFacturaTemporal(String codigoUsuario,FacturaCab facturaCabTmp) throws Exception{
		
		if(facturaCabTmp==null)
			throw new Exception("Debe crear una factura primero.");
		if(facturaCabTmp.getFacturaDets()==null || facturaCabTmp.getFacturaDets().size()==0)
			throw new Exception("Debe ingresar los productos en la factura.");
		if(facturaCabTmp.getCliente()==null)
			throw new Exception("Debe registrar el cliente.");
		
		//asignacion del usuario que crea la factura
		Usuario usuario=managerSeguridad.findUsuarioById(codigoUsuario);
		facturaCabTmp.setUsuario(usuario);
		
		facturaCabTmp.setFechaEmision(new Date());
		
		//obtenemos el numero de la nueva factura:
		int contFacturas;
		contFacturas=getContadorFacturas();
		contFacturas++;
		facturaCabTmp.setNumeroFactura(Integer.toString(contFacturas));
		
		//verificamos los campos calculados:
		calcularFacturaTmp(facturaCabTmp);
		
		
		for(FacturaDet det:facturaCabTmp.getFacturaDets()){
			det.setFacturaCab(facturaCabTmp);
		}
		
		//guardamos la factura completa en la bdd:
		managerDAO.insertar(facturaCabTmp);
		
		//actualizamos los parametros contadores de facturas:
		actualizarContFacturas(contFacturas);
		
		facturaCabTmp=null;		
		
	}
	/**
	 * Permite generar una factura a partir de un pedido de compra
	 * de un cliente. Este metodo reutiliza toda la logica de negocio
	 * que previamente fue implementada en ManagerFacturacion.
	 * @param codigoUsuario Codigo del usuario que despacha el pedido.
	 * @param pedidoCab El pedido del cliente.
	 * @throws Exception
	 */
	public void crearFacturaConPedido(String codigoUsuario,PedidoCab pedidoCab) throws Exception{
		if(pedidoCab==null)
			throw new Exception("Debe crear un pedido primero.");
		//Creamos una factura temporal:
		FacturaCab facturaCabTmp=crearFacturaTmp();
		//Asignamos la informacion de cliente:
		asignarClienteFacturaTmp(facturaCabTmp, pedidoCab.getCliente().getCedulaCliente());
		//Agregamos los productos:
		for(PedidoDet pd:pedidoCab.getPedidoDets()){
			agregarDetalleFacturaTmp(facturaCabTmp, pd.getProducto().getCodigoProducto(), pd.getCantidad());
		}
		//Finalmente guardamos la nueva factura:
		guardarFacturaTemporal(codigoUsuario,facturaCabTmp);
	}
	
}
