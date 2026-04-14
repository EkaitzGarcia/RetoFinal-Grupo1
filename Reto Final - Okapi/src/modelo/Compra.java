

package modelo;

import java.time.LocalDate;

public class Compra {
	
	private int id_compra;
	private LocalDate fecha;
	private float total_compra;
	private MetodoPago metodo_pago;
	public Compra(int id_compra, LocalDate fecha, float total_compra, MetodoPago metodo_pago) {
		this.id_compra = id_compra;
		this.fecha = fecha;
		this.total_compra = total_compra;
		this.metodo_pago = metodo_pago;
	}
	public int getId_compra() {
		return id_compra;
	}
	public void setId_compra(int id_compra) {
		this.id_compra = id_compra;
	}
	public LocalDate getFecha() {
		return fecha;
	}
	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}
	public float getTotal_compra() {
		return total_compra;
	}
	public void setTotal_compra(float total_compra) {
		this.total_compra = total_compra;
	}
	public MetodoPago getMetodo_pago() {
		return metodo_pago;
	}
	public void setMetodo_pago(MetodoPago metodo_pago) {
		this.metodo_pago = metodo_pago;
	}
	@Override
	public String toString() {
		return "Compra [id_compra=" + id_compra + ", fecha=" + fecha + ", total_compra=" + total_compra
				+ ", metodo_pago=" + metodo_pago + "]";
	}
	
	

}
