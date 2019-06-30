package marketdemo.model.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the producto database table.
 * 
 */
@Entity
@Table(name="producto")
@NamedQuery(name="Producto.findAll", query="SELECT p FROM Producto p")
public class Producto implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="codigo_producto", unique=true, nullable=false)
	private Integer codigoProducto;

	@Column(length=100)
	private String descripcion;

	@Column(nullable=false)
	private Integer existencia;

	@Column(nullable=false, length=50)
	private String nombre;

	@Column(name="precio_unitario", nullable=false, precision=12, scale=2)
	private BigDecimal precioUnitario;

	@Column(name="tiene_impuesto", nullable=false, length=1)
	private String tieneImpuesto;

	//bi-directional many-to-one association to FacturaDet
	@OneToMany(mappedBy="producto")
	private List<FacturaDet> facturaDets;

	//bi-directional many-to-one association to PedidoDet
	@OneToMany(mappedBy="producto")
	private List<PedidoDet> pedidoDets;

	public Producto() {
	}

	public Integer getCodigoProducto() {
		return this.codigoProducto;
	}

	public void setCodigoProducto(Integer codigoProducto) {
		this.codigoProducto = codigoProducto;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Integer getExistencia() {
		return this.existencia;
	}

	public void setExistencia(Integer existencia) {
		this.existencia = existencia;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public BigDecimal getPrecioUnitario() {
		return this.precioUnitario;
	}

	public void setPrecioUnitario(BigDecimal precioUnitario) {
		this.precioUnitario = precioUnitario;
	}

	public String getTieneImpuesto() {
		return this.tieneImpuesto;
	}

	public void setTieneImpuesto(String tieneImpuesto) {
		this.tieneImpuesto = tieneImpuesto;
	}

	public List<FacturaDet> getFacturaDets() {
		return this.facturaDets;
	}

	public void setFacturaDets(List<FacturaDet> facturaDets) {
		this.facturaDets = facturaDets;
	}

	public FacturaDet addFacturaDet(FacturaDet facturaDet) {
		getFacturaDets().add(facturaDet);
		facturaDet.setProducto(this);

		return facturaDet;
	}

	public FacturaDet removeFacturaDet(FacturaDet facturaDet) {
		getFacturaDets().remove(facturaDet);
		facturaDet.setProducto(null);

		return facturaDet;
	}

	public List<PedidoDet> getPedidoDets() {
		return this.pedidoDets;
	}

	public void setPedidoDets(List<PedidoDet> pedidoDets) {
		this.pedidoDets = pedidoDets;
	}

	public PedidoDet addPedidoDet(PedidoDet pedidoDet) {
		getPedidoDets().add(pedidoDet);
		pedidoDet.setProducto(this);

		return pedidoDet;
	}

	public PedidoDet removePedidoDet(PedidoDet pedidoDet) {
		getPedidoDets().remove(pedidoDet);
		pedidoDet.setProducto(null);

		return pedidoDet;
	}

}