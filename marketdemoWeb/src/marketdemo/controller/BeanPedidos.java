package marketdemo.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import marketdemo.model.dto.LoginDTO;
import marketdemo.model.entities.PedidoCab;
import marketdemo.model.entities.Producto;
import marketdemo.model.manager.ManagerClientes;
import marketdemo.model.manager.ManagerFacturacion;
import marketdemo.model.manager.ManagerPedidos;
import marketdemo.model.manager.ManagerProductos;
import marketdemo.model.manager.ManagerSeguridad;

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
	@EJB
	private ManagerSeguridad managerSeguridad;
	
	private PedidoCab pedidoCabTmp;
	
	@Inject
	private BeanLogin beanLogin;

	public BeanPedidos() {

	}
	@PostConstruct
	public void iniciar(){
		listaProductos=managerProductos.findAllProductos();
	}

	public String actionComprobarCedula() {
		try {
			LoginDTO loginDTO=managerSeguridad.accederSistemaClientes(cedula, clave);
			if(loginDTO==null)
				return "registro";//el usuario debe registrarse.
			//Caso contrario es un usuario existente:
			beanLogin.setLoginDTO(loginDTO);
			clave="";//borramos la clave de la sesion.
			//Recuperamos la informacion
			//para presentarla en la pagina de pedidos:
			nombres = loginDTO.getUsuario();
			direccion = loginDTO.getDireccion();
			//creamos el pedido temporal y asignamos el cliente automaticamente:
			pedidoCabTmp=managerPedidos.crearPedidoTmp(cedula);
			//llenamos datos en loginDTO:
			return loginDTO.getRutaAcceso()+"?faces-redirect=true";
		} catch (Exception e) {
			e.printStackTrace();
			JSFUtil.crearMensajeERROR(e.getMessage());
			return "";
		}
	}

	public String actionInsertarCliente() {
		try {
			managerClientes.insertarCliente(cedula, nombres, apellidos, direccion, clave);
			//una vez registrado el cliente, hacemos automaticamente login:
			LoginDTO loginDTO=managerSeguridad.accederSistemaClientes(cedula, clave);
			beanLogin.setLoginDTO(loginDTO);
			//creamos el pedido temporal y asignamos el cliente automaticamente:
			pedidoCabTmp=managerPedidos.crearPedidoTmp(cedula);
			return loginDTO.getRutaAcceso()+"?faces-redirect=true";
		} catch (Exception e) {
			e.printStackTrace();
			JSFUtil.crearMensajeERROR(e.getMessage());
		}
		return "";
	}
	
	public void actionInsertarProducto(Producto p){
		try {
			if(pedidoCabTmp==null)
				pedidoCabTmp=managerPedidos.crearPedidoTmp(cedula);
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
		try {
			//creamos el pedido temporal y asignamos el cliente automaticamente:
			pedidoCabTmp=managerPedidos.crearPedidoTmp(cedula);
			return beanLogin.getLoginDTO()+"?faces-redirect=true";
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}
		return "";
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
