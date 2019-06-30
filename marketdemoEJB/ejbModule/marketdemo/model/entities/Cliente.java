package marketdemo.model.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the cliente database table.
 * 
 */
@Entity
@Table(name="cliente")
@NamedQuery(name="Cliente.findAll", query="SELECT c FROM Cliente c")
public class Cliente implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="cedula_cliente", unique=true, nullable=false, length=10)
	private String cedulaCliente;

	@Column(nullable=false, length=100)
	private String apellidos;

	@Column(length=20)
	private String clave;

	@Column(length=100)
	private String direccion;

	@Column(nullable=false, length=100)
	private String nombres;

	//bi-directional many-to-one association to FacturaCab
	@OneToMany(mappedBy="cliente")
	private List<FacturaCab> facturaCabs;

	//bi-directional many-to-one association to PedidoCab
	@OneToMany(mappedBy="cliente")
	private List<PedidoCab> pedidoCabs;

	public Cliente() {
	}

	public String getCedulaCliente() {
		return this.cedulaCliente;
	}

	public void setCedulaCliente(String cedulaCliente) {
		this.cedulaCliente = cedulaCliente;
	}

	public String getApellidos() {
		return this.apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getClave() {
		return this.clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getDireccion() {
		return this.direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getNombres() {
		return this.nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public List<FacturaCab> getFacturaCabs() {
		return this.facturaCabs;
	}

	public void setFacturaCabs(List<FacturaCab> facturaCabs) {
		this.facturaCabs = facturaCabs;
	}

	public FacturaCab addFacturaCab(FacturaCab facturaCab) {
		getFacturaCabs().add(facturaCab);
		facturaCab.setCliente(this);

		return facturaCab;
	}

	public FacturaCab removeFacturaCab(FacturaCab facturaCab) {
		getFacturaCabs().remove(facturaCab);
		facturaCab.setCliente(null);

		return facturaCab;
	}

	public List<PedidoCab> getPedidoCabs() {
		return this.pedidoCabs;
	}

	public void setPedidoCabs(List<PedidoCab> pedidoCabs) {
		this.pedidoCabs = pedidoCabs;
	}

	public PedidoCab addPedidoCab(PedidoCab pedidoCab) {
		getPedidoCabs().add(pedidoCab);
		pedidoCab.setCliente(this);

		return pedidoCab;
	}

	public PedidoCab removePedidoCab(PedidoCab pedidoCab) {
		getPedidoCabs().remove(pedidoCab);
		pedidoCab.setCliente(null);

		return pedidoCab;
	}

}