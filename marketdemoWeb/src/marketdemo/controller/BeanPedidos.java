package marketdemo.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import marketdemo.model.entities.Cliente;
import marketdemo.model.entities.PedidoCab;
import marketdemo.model.entities.Producto;
import marketdemo.model.manager.ManagerClientes;
import marketdemo.model.manager.ManagerFacturacion;
import marketdemo.model.manager.ManagerPedidos;
import marketdemo.model.manager.ManagerProductos;

@Named
@SessionScoped
public class BeanPedidos  implements Serializable {
	private static final long serialVersionUID = 1L;
	private String cedula;
	private String nombres;
	private String apellidos;
	private String direccion;
	private String clave;
	
	private List<Producto> listaProductos;
	@EJB
	private ManagerFacturacion managerFacturacion;
	@EJB
	private ManagerPedidos managerPedidos;
	@EJB
	private ManagerProductos managerProductos;
	@EJB
	private ManagerClientes managerClientes;
	
	private PedidoCab pedidoCabTmp;

	public BeanPedidos() {

	}
	@PostConstruct
	public void iniciar(){
		listaProductos=managerProductos.findAllProductos();
	}

	public String actionComprobarCedula() {
		try {
			Cliente c = managerClientes.findClienteById(cedula);
			// verificamos la existencia del cliente:
			if (c == null)
				return "registro";// debe registrarse
			
			//caso contrario si ya existe el cliente, recuperamos la informacion
			// para presentarla en la pagina de pedidos:
			nombres = c.getNombres();
			apellidos = c.getApellidos();
			direccion = c.getDireccion();
			//creamos el pedido temporal y asignamos el cliente automaticamente:
			pedidoCabTmp=managerPedidos.crearPedidoTmp();
			managerPedidos.asignarClientePedidoTmp(pedidoCabTmp, cedula);

			return "pedido";
		} catch (Exception e) {
			// error no esperado:
			e.printStackTrace();
			JSFUtil.crearMensajeERROR(e.getMessage());
			return "";
		}
	}

	public String actionInsertarCliente() {
		try {
			managerClientes.insertarCliente(cedula, nombres, apellidos,
					direccion, clave);
			return "pedido";
		} catch (Exception e) {
			e.printStackTrace();
			JSFUtil.crearMensajeERROR(e.getMessage());
		}
		return "";

	}
	
	public void actionInsertarProducto(Producto p){
		try {
			if(pedidoCabTmp==null)
				pedidoCabTmp=managerPedidos.crearPedidoTmp();
			//agregamos un nuevo producto al carrito de compras:
			managerPedidos.agregarDetallePedidoTmp(pedidoCabTmp, p.getCodigoProducto(), 1);
		} catch (Exception e) {
			e.printStackTrace();
			JSFUtil.crearMensajeERROR(e.getMessage());
		}
	}
	public String actionGuardarPedido(){
		try {
			managerPedidos.guardarPedidoTemporal(pedidoCabTmp);
		} catch (Exception e) {
			e.printStackTrace();
			JSFUtil.crearMensajeERROR(e.getMessage());
		}
		return "pedido_imprimir";
	}
	public String actionCerrarPedido(){
		pedidoCabTmp=null;
		//creamos el pedido temporal y asignamos el cliente automaticamente:
		pedidoCabTmp=managerPedidos.crearPedidoTmp();
		try {
			managerPedidos.asignarClientePedidoTmp(pedidoCabTmp, cedula);
		} catch (Exception e) {
			e.printStackTrace();
			JSFUtil.crearMensajeERROR(e.getMessage());
		}
		return "pedido";
	}

	public String getCedula() {
		return cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public List<Producto> getListaProductos() {
		return listaProductos;
	}

	public void setListaProductos(List<Producto> listaProductos) {
		this.listaProductos = listaProductos;
	}

	public PedidoCab getPedidoCabTmp() {
		return pedidoCabTmp;
	}

	public void setPedidoCabTmp(PedidoCab pedidoCabTmp) {
		this.pedidoCabTmp = pedidoCabTmp;
	}

}
