package marketdemo.controller;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.SelectItem;
import javax.inject.Named;

import marketdemo.model.entities.Cliente;
import marketdemo.model.entities.FacturaCab;
import marketdemo.model.entities.Producto;
import marketdemo.model.manager.ManagerClientes;
import marketdemo.model.manager.ManagerFacturacion;
import marketdemo.model.manager.ManagerProductos;

import java.io.Serializable;

/**
 * ManagedBean JSF para el manejo de la facturacion.
 * @author mrea
 *
 */

@Named
@SessionScoped
public class BeanFactura implements Serializable {
	private static final long serialVersionUID = 1L;
	private String cedulaCliente;
	@EJB
	private ManagerFacturacion managerFacturacion;
	@EJB
	private ManagerProductos managerProductos;
	@EJB
	private ManagerClientes managerClientes;
	
	private Integer codigoProducto;
	private Integer cantidadProducto;
	private FacturaCab facturaCabTmp;
	private boolean facturaCabTmpGuardada;
	
	public BeanFactura() {
		
	}

	/**
	 * Action para la creacion de una factura temporal en memoria.
	 * Hace uso del componente {@link facturacion.model.manager.ManagerFacturacion ManagerFacturacion} de la capa model.
	 * @return outcome para la navegacion.
	 */
	public String crearNuevaFactura(){
		facturaCabTmp=managerFacturacion.crearFacturaTmp();
		cedulaCliente=null;
		codigoProducto=0;
		cantidadProducto=0;
		facturaCabTmpGuardada=false;
		return "";
	}
	/**
	 * Action para asignar un cliente a la factura temporal actual.
	 * Hace uso del componente {@link facturacion.model.manager.ManagerFacturacion ManagerFacturacion} de la capa model.
	 * @return outcome para la navegacion.
	 */
	public void asignarCliente(){
		if(facturaCabTmpGuardada==true){
			JSFUtil.crearMensajeWARN("La factura ya fue guardada.");
		}
		try {
			managerFacturacion.asignarClienteFacturaTmp(facturaCabTmp,cedulaCliente);
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
		}
	}
	
	public void verificarExistencia(){
		try {
			if(managerProductos.obtenerExistencia(codigoProducto)==0)
				JSFUtil.crearMensajeERROR("No hay existencia");
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Action que adiciona un item a una factura temporal.
	 * Hace uso del componente {@link model.manager.ManagerFacturacion ManagerFacturacion} de la capa model.
	 * @return
	 */
	public String insertarDetalle(){
		if(facturaCabTmpGuardada==true){
			JSFUtil.crearMensajeWARN("La factura ya fue guardada.");
			return "";
		}
		try {
			managerFacturacion.agregarDetalleFacturaTmp(facturaCabTmp,codigoProducto, cantidadProducto);
			codigoProducto=0;
			cantidadProducto=0;
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
		}		
		return "";
	}
	
	/**
	 * Action que almacena en la base de datos una factura temporal creada en memoria.
	 * Hace uso del componente {@link facturacion.model.manager.ManagerFacturacion ManagerFacturacion} de la capa model.
	 * @return outcome para la navegacion.
	 */
	public String guardarFactura(){
		if(facturaCabTmpGuardada==true){
			JSFUtil.crearMensajeWARN("La factura ya fue guardada.");
			return "";
		}
		try {
			managerFacturacion.guardarFacturaTemporal(facturaCabTmp);
			facturaCabTmpGuardada=true;
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
		}
		
		return "";
	}
	
	public String getCedulaCliente() {
		return cedulaCliente;
	}

	public void setCedulaCliente(String cedulaCliente) {
		this.cedulaCliente = cedulaCliente;
	}

	public Integer getCodigoProducto() {
		return codigoProducto;
	}

	public void setCodigoProducto(Integer codigoProducto) {
		this.codigoProducto = codigoProducto;
	}

	public Integer getCantidadProducto() {
		return cantidadProducto;
	}

	public void setCantidadProducto(Integer cantidadProducto) {
		this.cantidadProducto = cantidadProducto;
	}

	/**
	 * Devuelve un listado de componentes SelectItem a partir
	 * de un listado de {@link facturacion.model.dao.entities.Cliente Cliente}.
	 * @return listado de SelectItems de clientes.
	 */
	public List<SelectItem> getListaClientesSI(){
		List<SelectItem> listadoSI=new ArrayList<SelectItem>();
		List<Cliente> listadoClientes=managerClientes.findAllClientes();
		
		for(Cliente c:listadoClientes){
			SelectItem item=new SelectItem(c.getCedulaCliente(), 
					                   c.getApellidos()+" "+c.getNombres());
			listadoSI.add(item);
		}
		return listadoSI;
	}
	/**
	 * Devuelve un listado de componentes SelectItem a partir
	 * de un listado de {@link facturacion.model.dao.entities.Producto Producto}.
	 * 
	 * @return listado de SelectItems de productos.
	 */
	public List<SelectItem> getListaProductosSI(){
		List<SelectItem> listadoSI=new ArrayList<SelectItem>();
		List<Producto> listadoProductos=managerProductos.findAllProductos();
		
		for(Producto p:listadoProductos){
			SelectItem item=new SelectItem(p.getCodigoProducto(), 
					                   p.getNombre());
			listadoSI.add(item);
		}
		return listadoSI;
	}

	public FacturaCab getFacturaCabTmp() {
		return facturaCabTmp;
	}

	public void setFacturaCabTmp(FacturaCab facturaCabTmp) {
		this.facturaCabTmp = facturaCabTmp;
	}
	
	public List<FacturaCab> getListaFacturasCab(){
		List<FacturaCab> listadoFacturas=managerFacturacion.findAllFacturaCab();
		return listadoFacturas;
	}

	public boolean isFacturaCabTmpGuardada() {
		return facturaCabTmpGuardada;
	}

	public void setFacturaCabTmpGuardada(boolean facturaCabTmpGuardada) {
		this.facturaCabTmpGuardada = facturaCabTmpGuardada;
	}
	
}
