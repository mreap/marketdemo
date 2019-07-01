package marketdemo.model.manager;

import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import marketdemo.model.entities.Bitacora;
import marketdemo.model.entities.Usuario;

/**
 * Clase que implementa la logica de manejo de
 * pistas de auditoria.
 * @author mrea
 *
 */
@Stateless
@LocalBean
public class ManagerAuditoria {
	@EJB
	private ManagerDAO managerDAO;
	@EJB
	private ManagerSeguridad managerSeguridad;
	
	public ManagerAuditoria() {
		
	}
	
	/**
	 * Almacena la informacion de un evento en la tabla de auditoria.
	 * @param codigoUsuario Codigo del usuario que genero el evento.
	 * @param clase Clase que genera el evento.
	 * @param metodo Nombre del metodo que genero el evento.
	 * @param descripcion Informacion detallada del evento.
	 * @throws Exception 
	 */
	public void crearEvento(String codigoUsuario,Class clase,String metodo,String descripcion) throws Exception{
		Bitacora evento=new Bitacora();
		//cambio para probar git
		
		if(codigoUsuario==null||codigoUsuario.length()==0)
			throw new Exception("Error auditoria: debe indicar el codigo del usuario.");
		if(metodo==null||metodo.length()==0)
			throw new Exception("Error auditoria: debe indicar el metodo que genera el evento.");

		Usuario usuario=(Usuario)managerDAO.findById(Usuario.class, codigoUsuario);
		if(usuario==null)
			throw new Exception("Error auditoria: no existe el usuario indicado.");
		
		evento.setUsuario(usuario);
		evento.setMetodo(clase.getSimpleName()+"/"+metodo);
		evento.setDescripcion(descripcion);
		evento.setFechaEvento(new Date());
		evento.setDireccionIp("localhost");
		
		managerDAO.insertar(evento);
	}

}
