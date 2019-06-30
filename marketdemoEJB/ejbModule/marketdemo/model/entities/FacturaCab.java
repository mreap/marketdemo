package marketdemo.model.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the factura_cab database table.
 * 
 */
@Entity
@Table(name="factura_cab")
@NamedQuery(name="FacturaCab.findAll", query="SELECT f FROM FacturaCab f")
public class FacturaCab implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="numero_factura", unique=true, nullable=false, length=17)
	private String numeroFactura;

	@Column(name="base_cero", nullable=false, precision=12, scale=2)
	private BigDecimal baseCero;

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_emision", nullable=false)
	private Date fechaEmision;

	@Column(nullable=false, precision=12, scale=2)
	private BigDecimal subtotal;

	@Column(nullable=false, precision=12, scale=2)
	private BigDecimal total;

	@Column(name="valor_iva", nullable=false, precision=12, scale=2)
	private BigDecimal valorIva;

	//bi-directional many-to-one association to Cliente
	@ManyToOne
	@JoinColumn(name="cedula_cliente", nullable=false)
	private Cliente cliente;

	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="codigo_usuario", nullable=false)
	private Usuario usuario;

	//bi-directional many-to-one association to FacturaDet
	@OneToMany(mappedBy="facturaCab",cascade = CascadeType.ALL)
	private List<FacturaDet> facturaDets;

	public FacturaCab() {
	}

	public String getNumeroFactura() {
		return this.numeroFactura;
	}

	public void setNumeroFactura(String numeroFactura) {
		this.numeroFactura = numeroFactura;
	}

	public BigDecimal getBaseCero() {
		return this.baseCero;
	}

	public void setBaseCero(BigDecimal baseCero) {
		this.baseCero = baseCero;
	}

	public Date getFechaEmision() {
		return this.fechaEmision;
	}

	public void setFechaEmision(Date fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	public BigDecimal getSubtotal() {
		return this.subtotal;
	}

	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}

	public BigDecimal getTotal() {
		return this.total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public BigDecimal getValorIva() {
		return this.valorIva;
	}

	public void setValorIva(BigDecimal valorIva) {
		this.valorIva = valorIva;
	}

	public Cliente getCliente() {
		return this.cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<FacturaDet> getFacturaDets() {
		return this.facturaDets;
	}

	public void setFacturaDets(List<FacturaDet> facturaDets) {
		this.facturaDets = facturaDets;
	}

	public FacturaDet addFacturaDet(FacturaDet facturaDet) {
		getFacturaDets().add(facturaDet);
		facturaDet.setFacturaCab(this);

		return facturaDet;
	}

	public FacturaDet removeFacturaDet(FacturaDet facturaDet) {
		getFacturaDets().remove(facturaDet);
		facturaDet.setFacturaCab(null);

		return facturaDet;
	}

}