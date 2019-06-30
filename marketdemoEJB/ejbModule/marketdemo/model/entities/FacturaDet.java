package marketdemo.model.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the factura_det database table.
 * 
 */
@Entity
@Table(name="factura_det")
@NamedQuery(name="FacturaDet.findAll", query="SELECT f FROM FacturaDet f")
public class FacturaDet implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="FACTURA_DET_NUMEROFACTURADET_GENERATOR", sequenceName="SEQ_FACTURA_DET")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="FACTURA_DET_NUMEROFACTURADET_GENERATOR")
	@Column(name="numero_factura_det", unique=true, nullable=false)
	private Integer numeroFacturaDet;

	@Column(nullable=false)
	private Integer cantidad;

	@Column(name="precio_unitario_venta", nullable=false, precision=12, scale=2)
	private BigDecimal precioUnitarioVenta;

	//bi-directional many-to-one association to FacturaCab
	@ManyToOne
	@JoinColumn(name="numero_factura", nullable=false)
	private FacturaCab facturaCab;

	//bi-directional many-to-one association to Producto
	@ManyToOne
	@JoinColumn(name="codigo_producto", nullable=false)
	private Producto producto;

	public FacturaDet() {
	}

	public Integer getNumeroFacturaDet() {
		return this.numeroFacturaDet;
	}

	public void setNumeroFacturaDet(Integer numeroFacturaDet) {
		this.numeroFacturaDet = numeroFacturaDet;
	}

	public Integer getCantidad() {
		return this.cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public BigDecimal getPrecioUnitarioVenta() {
		return this.precioUnitarioVenta;
	}

	public void setPrecioUnitarioVenta(BigDecimal precioUnitarioVenta) {
		this.precioUnitarioVenta = precioUnitarioVenta;
	}

	public FacturaCab getFacturaCab() {
		return this.facturaCab;
	}

	public void setFacturaCab(FacturaCab facturaCab) {
		this.facturaCab = facturaCab;
	}

	public Producto getProducto() {
		return this.producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

}