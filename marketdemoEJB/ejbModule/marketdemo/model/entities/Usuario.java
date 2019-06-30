package marketdemo.model.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the usuario database table.
 * 
 */
@Entity
@Table(name="usuario")
@NamedQuery(name="Usuario.findAll", query="SELECT u FROM Usuario u")
public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="codigo_usuario", unique=true, nullable=false, length=20)
	private String codigoUsuario;

	@Column(length=10)
	private String clave;

	@Column(name="nombres_usuario", nullable=false, length=100)
	private String nombresUsuario;

	@Column(name="tipo_usuario", nullable=false, length=2)
	private String tipoUsuario;

	//bi-directional many-to-one association to Bitacora
	@OneToMany(mappedBy="usuario")
	private List<Bitacora> bitacoras;

	//bi-directional many-to-one association to FacturaCab
	@OneToMany(mappedBy="usuario")
	private List<FacturaCab> facturaCabs;

	public Usuario() {
	}

	public String getCodigoUsuario() {
		return this.codigoUsuario;
	}

	public void setCodigoUsuario(String codigoUsuario) {
		this.codigoUsuario = codigoUsuario;
	}

	public String getClave() {
		return this.clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getNombresUsuario() {
		return this.nombresUsuario;
	}

	public void setNombresUsuario(String nombresUsuario) {
		this.nombresUsuario = nombresUsuario;
	}

	public String getTipoUsuario() {
		return this.tipoUsuario;
	}

	public void setTipoUsuario(String tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}

	public List<Bitacora> getBitacoras() {
		return this.bitacoras;
	}

	public void setBitacoras(List<Bitacora> bitacoras) {
		this.bitacoras = bitacoras;
	}

	public Bitacora addBitacora(Bitacora bitacora) {
		getBitacoras().add(bitacora);
		bitacora.setUsuario(this);

		return bitacora;
	}

	public Bitacora removeBitacora(Bitacora bitacora) {
		getBitacoras().remove(bitacora);
		bitacora.setUsuario(null);

		return bitacora;
	}

	public List<FacturaCab> getFacturaCabs() {
		return this.facturaCabs;
	}

	public void setFacturaCabs(List<FacturaCab> facturaCabs) {
		this.facturaCabs = facturaCabs;
	}

	public FacturaCab addFacturaCab(FacturaCab facturaCab) {
		getFacturaCabs().add(facturaCab);
		facturaCab.setUsuario(this);

		return facturaCab;
	}

	public FacturaCab removeFacturaCab(FacturaCab facturaCab) {
		getFacturaCabs().remove(facturaCab);
		facturaCab.setUsuario(null);

		return facturaCab;
	}

}