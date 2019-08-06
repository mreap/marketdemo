package marketdemo.controller;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import marketdemo.model.entities.PedidoCab;
import marketdemo.model.manager.ManagerPedidos;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@Named
@SessionScoped
public class BeanSupervisor  implements Serializable {
	private static final long serialVersionUID = 1L;
	private Date fechaInicio;
	private Date fechaFinal;
	@EJB
	private ManagerPedidos managerPedidos;
	private PedidoCab pedidoCabTmp;
	
	//Inyeccion de beans manejados:
	@Inject
	private BeanLogin beanLogin;
	
	public BeanSupervisor(){
		
	}
	public String actionBuscar(){
		//unicamente se invoca esta accion para actualizar
		//los parametros de fechas.
		return "";
	}
	/**
	 * 
	 * @param pedidoCab
	 * @return
	 */
	public String actionCargarPedido(PedidoCab pedidoCab){
		try {
			//capturamos el valor enviado desde el DataTable:
			pedidoCabTmp=pedidoCab;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	public String actionDespacharPedido(PedidoCab pedidoCab){
		try {
			//invocamos a ManagerFacturacion para crear una nueva factura:
			managerPedidos.despacharPedido(beanLogin.getCodigoUsuario(),pedidoCab.getNumeroPedido());
		} catch (Exception e) {
			e.printStackTrace();
			JSFUtil.crearMensajeERROR(e.getMessage());
		}
		return "";
	}
	public List<PedidoCab> getListaPedidoCab(){
		try {
			return managerPedidos.findPedidoCabByFechas(fechaInicio, fechaFinal);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String actionReporte() {
		Map<String, Object> parametros = new HashMap<String, Object>();
		/*
		 * parametros.put("p_titulo_principal",p_titulo_principal);
		 * parametros.put("p_titulo",p_titulo);
		 */
		
		FacesContext context = FacesContext.getCurrentInstance();
		ServletContext servletContext = (ServletContext) context.getExternalContext().getContext();
		String ruta = servletContext.getRealPath("supervisor/clientes.jasper");
		System.out.println(ruta);
		HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
		response.addHeader("Content-disposition", "attachment;filename=clientes.pdf");
		response.setContentType("application/pdf");
		try {
			Class.forName("org.postgresql.Driver");
			Connection connection = null;
			connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/marketdemo", "postgres", "postgres");
			JasperPrint impresion = JasperFillManager.fillReport(ruta, parametros, connection);
			JasperExportManager.exportReportToPdfStream(impresion, response.getOutputStream());
			context.getApplication().getStateManager().saveView(context);
			System.out.println("reporte generado.");
			context.responseComplete();
		} catch (Exception e) {
			JSFUtil.crearMensajeERROR(e.getMessage());
			e.printStackTrace();
		}
		return "";
	}
	
	public Date getFechaInicio() {
		return fechaInicio;
	}
	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	public Date getFechaFinal() {
		return fechaFinal;
	}
	public void setFechaFinal(Date fechaFinal) {
		this.fechaFinal = fechaFinal;
	}
	public PedidoCab getPedidoCabTmp() {
		return pedidoCabTmp;
	}
	public void setPedidoCabTmp(PedidoCab pedidoCabTmp) {
		this.pedidoCabTmp = pedidoCabTmp;
	}
	//Un bean inyectado debe tener sus metodos accesores:
	public void setBeanLogin(BeanLogin beanLogin) {
		this.beanLogin = beanLogin;
	}
	public BeanLogin getBeanLogin() {
		return beanLogin;
	}
	
	
}
