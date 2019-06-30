package marketdemo.model.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the bitacora database table.
 * 
 */
@Entity
@Table(name="bitacora")
@NamedQuery(name="Bitacora.findAll", query="SELECT b FROM Bitacora b")
public class Bitacora implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="BITACORA_CODIGOEVENTO_GENERATOR", sequenceName="SEQ_BITACORA",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="BITACORA_CODIGOEVENTO_GENERATOR")
	@Column(name="codigo_evento", unique=true, nullable=false)
	private Integer codigoEvento;

	@Column(length=200)
	private String descripcion;

	@Column(name="direccion_ip", length=20)
	private String direccionIp;

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_evento", nullable=false)
	private Date fechaEvento;

	@Column(nullable=false, length=100)
	private String metodo;

	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="codigo_usuario", nullable=false)
	private Usuario usuario;

	public Bitacora() {
	}

	public Integer getCodigoEvento() {
		return this.codigoEvento;
	}

	public void setCodigoEvento(Integer codigoEvento) {
		this.codigoEvento = codigoEvento;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDireccionIp() {
		return this.direccionIp;
	}

	public void setDireccionIp(String direccionIp) {
		this.direccionIp = direccionIp;
	}

	public Date getFechaEvento() {
		return this.fechaEvento;
	}

	public void setFechaEvento(Date fechaEvento) {
		this.fechaEvento = fechaEvento;
	}

	public String getMetodo() {
		return this.metodo;
	}

	public void setMetodo(String metodo) {
		this.metodo = metodo;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}