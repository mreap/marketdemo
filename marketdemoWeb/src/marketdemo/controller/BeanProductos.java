package marketdemo.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import marketdemo.model.manager.ManagerFacturacion;
import marketdemo.model.manager.ManagerProductos;
import marketdemo.model.entities.Producto;

@Named
@SessionScoped
public class BeanProductos  implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<Producto> listaProductos;
	@EJB
	private ManagerFacturacion managerFacturacion;
	@EJB
	private ManagerProductos managerProductos;
	
	private Integer codigoProducto;
	private String descripcion;
	private Integer existencia;
	private String nombre;
	private BigDecimal precioUnitario;
	private String tieneImpuesto;
	
	@PostConstruct
	public void inicializar() {
		listaProductos=managerProductos.findAllProductos();
	}
	
	public String actionInsertarProducto(){
		Producto p= new Producto();
		p.setCodigoProducto(codigoProducto);
		p.setDescripcion(descripcion);
		p.setExistencia(existencia);
		p.setNombre(nombre);
		p.setPrecioUnitario(precioUnitario);
		p.setTieneImpuesto(tieneImpuesto);
		try {
			managerProductos.insertarProducto(p);
			listaProductos=managerProductos.findAllProductos();
			//limpiamos las variables del formulario:
			codigoProducto=null;
			descripcion="";
			existencia=null;
			nombre="";
			precioUnitario=null;
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}
		return "";
	}
	public String actionEliminarProducto(Producto producto){
		try {
			managerProductos.eliminarProducto(producto.getCodigoProducto());
			listaProductos=managerProductos.findAllProductos();
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}
		return "";
	}
	public String actionCargarProducto(Producto producto){
		codigoProducto=producto.getCodigoProducto();
		descripcion=producto.getDescripcion();
		nombre=producto.getNombre();
		precioUnitario=producto.getPrecioUnitario();
		existencia=producto.getExistencia();
		tieneImpuesto=producto.getTieneImpuesto();
		return "productos_update";
	}
	public String actionActualizarProducto(){
		Producto p=new Producto();
		p.setCodigoProducto(codigoProducto);
		p.setNombre(nombre);
		p.setDescripcion(descripcion);
		p.setPrecioUnitario(precioUnitario);
		p.setExistencia(existencia);
		p.setTieneImpuesto(tieneImpuesto);
		try {
			managerProductos.actualizarProducto(p);
			listaProductos=managerProductos.findAllProductos();
			//limpiamos las variables del formulario:
			codigoProducto=null;
			descripcion="";
			existencia=null;
			nombre="";
			precioUnitario=null;
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}
		return "productos";
	}
	
	public List<Producto> getListaProductos(){
		return listaProductos;
	}
	public Integer getCodigoProducto() {
		return codigoProducto;
	}
	public void setCodigoProducto(Integer codigoProducto) {
		this.codigoProducto = codigoProducto;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Integer getExistencia() {
		return existencia;
	}
	public void setExistencia(Integer existencia) {
		this.existencia = existencia;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public BigDecimal getPrecioUnitario() {
		return precioUnitario;
	}
	public void setPrecioUnitario(BigDecimal precioUnitario) {
		this.precioUnitario = precioUnitario;
	}
	public String getTieneImpuesto() {
		return tieneImpuesto;
	}
	public void setTieneImpuesto(String tieneImpuesto) {
		this.tieneImpuesto = tieneImpuesto;
	}
	
}
