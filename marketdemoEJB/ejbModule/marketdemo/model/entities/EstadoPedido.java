package marketdemo.model.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the estado_pedido database table.
 * 
 */
@Entity
@Table(name="estado_pedido")
@NamedQuery(name="EstadoPedido.findAll", query="SELECT e FROM EstadoPedido e")
public class EstadoPedido implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="id_estado_pedido", unique=true, nullable=false, length=2)
	private String idEstadoPedido;

	@Column(name="descripcion_estado", nullable=false, length=50)
	private String descripcionEstado;

	//bi-directional many-to-one association to PedidoCab
	@OneToMany(mappedBy="estadoPedido")
	private List<PedidoCab> pedidoCabs;

	public EstadoPedido() {
	}

	public String getIdEstadoPedido() {
		return this.idEstadoPedido;
	}

	public void setIdEstadoPedido(String idEstadoPedido) {
		this.idEstadoPedido = idEstadoPedido;
	}

	public String getDescripcionEstado() {
		return this.descripcionEstado;
	}

	public void setDescripcionEstado(String descripcionEstado) {
		this.descripcionEstado = descripcionEstado;
	}

	public List<PedidoCab> getPedidoCabs() {
		return this.pedidoCabs;
	}

	public void setPedidoCabs(List<PedidoCab> pedidoCabs) {
		this.pedidoCabs = pedidoCabs;
	}

	public PedidoCab addPedidoCab(PedidoCab pedidoCab) {
		getPedidoCabs().add(pedidoCab);
		pedidoCab.setEstadoPedido(this);

		return pedidoCab;
	}

	public PedidoCab removePedidoCab(PedidoCab pedidoCab) {
		getPedidoCabs().remove(pedidoCab);
		pedidoCab.setEstadoPedido(null);

		return pedidoCab;
	}

}