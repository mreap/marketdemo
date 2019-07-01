package marketdemo.controller;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import marketdemo.model.entities.Parametro;
import marketdemo.model.manager.ManagerParametros;

/**
 * ManagedBean JSF para el manejo de parametros.
 * @author mrea
 *
 */
@Named
@RequestScoped
public class BeanParametro {
	@EJB
	private ManagerParametros managerParametros;
	
	public List<Parametro> getListaParametros(){
		return managerParametros.findAllParametros();
	}
	
}
