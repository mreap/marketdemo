package marketdemo.model.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
/**
 * Clase utilitaria para la capa modelo
 * @author mrea
 *
 */
public class ModelUtil {
	/**
	 * Verifica si una cadena es igual a null o tiene longitud igual a cero (0).
	 * @param cadena Cadena que va a verificarse.
	 * @return
	 */
	public static boolean isEmpty(String cadena){
		if(cadena==null||cadena.length()==0)
			return true;
		return false;
	}
	/**
	 * Devuelve el valor del anio actual.
	 * @return valor correspondiente al anio actual.
	 */
	public static int getAnioActual(){
		Date fechaActual=new Date();
		SimpleDateFormat formato=new SimpleDateFormat("yyyy");
		int anioActual = Integer.parseInt(formato.format(fechaActual));
		return anioActual;
	}
	
	/**
	 * Devuelve el valor del mes actual.
	 * @return valor correspondiente al mes actual.
	 */
	public static int getMesActual(){
		Date fechaActual=new Date();
		SimpleDateFormat formato=new SimpleDateFormat("MM");
		int mesActual = Integer.parseInt(formato.format(fechaActual));
		return mesActual;
	}
	/**
	 * Suma o resta dias a una fecha.
	 * @param Fecha original.
	 * @param numero de dias a ser sumados.
	 * @return fecha sumada el numero de dias
	 */
	public static Date getSumarDias(Date fecha, int dias)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha); // Configuramos la fecha que se recibe
		calendar.add(Calendar.DAY_OF_YEAR, dias);  // numero de dias a sumar, o restar en caso de dias<0
		return calendar.getTime(); // Devuelve el objeto Date con los nuevos dias anadidos
	}
	/**
	 * Formatea una fecha con el patron dd/MM/yyyy hh:mm
	 * @param fecha La fecha que se debe formatear
	 * @return fecha formateada
	 */
	public static String fecha(Date fecha){
		SimpleDateFormat formato=new SimpleDateFormat("dd/MM/yyyy hh:mm");
		return formato.format(fecha);
	}
}

