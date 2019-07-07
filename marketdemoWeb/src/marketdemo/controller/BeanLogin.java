package marketdemo.controller;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import marketdemo.model.dto.LoginDTO;
import marketdemo.model.manager.ManagerSeguridad;

import java.io.Serializable;

@Named
@javax.enterprise.context.SessionScoped
public class BeanLogin implements Serializable {
	private static final long serialVersionUID = 1L;
	private String codigoUsuario;
	private String clave;
	private String tipoUsuario;
	private boolean acceso;
	@EJB
	private ManagerSeguridad managerSeguridad;
	private LoginDTO loginDTO;

	@PostConstruct
	public void inicializar() {
		loginDTO=new LoginDTO();
	}
	/**
	 * Action que permite el acceso al sistema.
	 * @return
	 */
	public String accederSistema(){
		acceso=false;
		try {
			loginDTO=managerSeguridad.accederSistema(codigoUsuario, clave);
			//verificamos el acceso del usuario:
			tipoUsuario=loginDTO.getTipoUsuario();
			//redireccion dependiendo del tipo de usuario:
			return loginDTO.getRutaAcceso()+"?faces-redirect=true";
		} catch (Exception e) {
			e.printStackTrace();
			JSFUtil.crearMensajeERROR(e.getMessage());
		}
		return "";
	}
	
	/**
	 * Finaliza la sesion web del usuario.
	 * @return
	 */
	public String salirSistema(){
		System.out.println("salirSistema");
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return "/index.html?faces-redirect=true";
	}

	public String getCodigoUsuario() {
		return codigoUsuario;
	}

	public void setCodigoUsuario(String codigoUsuario) {
		this.codigoUsuario = codigoUsuario;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public boolean isAcceso() {
		return acceso;
	}

	public String getTipoUsuario() {
		return tipoUsuario;
	}

	public void setTipoUsuario(String tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}
	
}
