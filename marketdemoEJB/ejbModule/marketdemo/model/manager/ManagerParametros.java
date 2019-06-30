package marketdemo.model.manager;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import marketdemo.model.entities.Parametro;

/**
 * Manejo de parametros
 */
@Stateless
@LocalBean
public class ManagerParametros {
	@EJB
	private ManagerDAO managerDAO;
    /**
     * Default constructor. 
     */
    public ManagerParametros() {
        
    }

  	/**
  	 * Metodo finder para la consulta de parametros.
  	 * Hace uso del componente {@link marketdemo.model.manager.ManagerDAO ManagerDAO} de la capa model.
  	 * @return listado de parametros.
  	 */
  	@SuppressWarnings("unchecked")
  	public List<Parametro> findAllParametros(){
  		return managerDAO.findAll(Parametro.class);
  	}
  	
  	public Parametro findParametroById(String nombreParametro) throws Exception{
  		Parametro p=(Parametro)managerDAO.findById(Parametro.class, nombreParametro);
  		if(p==null)
  			throw new Exception("No existe el parametro "+nombreParametro);
  		return p;
  	}
  	
  	public int getValorParametroInteger(String nombreParametro) throws Exception {
  		return Integer.parseInt(findParametroById(nombreParametro).getValorParametro());
  	}
  	
  	public double getValorParametroDouble(String nombreParametro) throws Exception {
  		return Double.parseDouble(findParametroById(nombreParametro).getValorParametro());
  	}
  	public String getValorParametro(String nombreParametro) throws Exception {
  		return findParametroById(nombreParametro).getValorParametro();
  	}
  	
  	public void actualizarParametro(String nombreParametro,int valorParametro) throws Exception {
  		Parametro p=findParametroById(nombreParametro);
  		p.setValorParametro(""+valorParametro);
  		managerDAO.actualizar(p);
  	}
  	public void actualizarParametro(String nombreParametro,double valorParametro) throws Exception {
  		Parametro p=findParametroById(nombreParametro);
  		p.setValorParametro(Double.toString(valorParametro));
  		managerDAO.actualizar(p);
  	}
  	public void actualizarParametro(String nombreParametro,String valorParametro) throws Exception {
  		Parametro p=findParametroById(nombreParametro);
  		p.setValorParametro(valorParametro);
  		managerDAO.actualizar(p);
  	}
}
