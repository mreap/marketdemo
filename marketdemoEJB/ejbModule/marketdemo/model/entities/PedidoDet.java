package marketdemo.model.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the pedido_det database table.
 * 
 */
@Entity
@Table(name="pedido_det")
@NamedQuery(name="PedidoDet.findAll", query="SELECT p FROM PedidoDet p")
public class PedidoDet implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="PEDIDO_DET_NUMEROPEDIDODET_GENERATOR", sequenceName="SEQ_PEDIDO_DET",allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PEDIDO_DET_NUMEROPEDIDODET_GENERATOR")
	@Column(name="numero_pedido_det", unique=true, nullable=false)
	private Integer numeroPedidoDet;

	@Column(nullable=false)
	private Integer cantidad;

	@Column(name="precio_unitario_venta", nullable=false, precision=12, scale=2)
	private BigDecimal precioUnitarioVenta;

	//bi-directional many-to-one association to PedidoCab
	@ManyToOne
	@JoinColumn(name="numero_pedido", nullable=false)
	private PedidoCab pedidoCab;

	//bi-directional many-to-one association to Producto
	@ManyToOne
	@JoinColumn(name="codigo_producto", nullable=false)
	private Producto producto;

	public PedidoDet() {
	}

	public Integer getNumeroPedidoDet() {
		return this.numeroPedidoDet;
	}

	public void setNumeroPedidoDet(Integer numeroPedidoDet) {
		this.numeroPedidoDet = numeroPedidoDet;
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

	public PedidoCab getPedidoCab() {
		return this.pedidoCab;
	}

	public void setPedidoCab(PedidoCab pedidoCab) {
		this.pedidoCab = pedidoCab;
	}

	public Producto getProducto() {
		return this.producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

}