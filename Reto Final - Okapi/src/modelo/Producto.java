

package modelo;

public class Producto {
	
	private String ref_producto;
	private float precio;
	private int descuento;
	private String nom_prod;
	public Producto(String ref_producto, float precio, int descuento, String nom_prod) {
		super();
		this.ref_producto = ref_producto;
		this.precio = precio;
		this.descuento = descuento;
		this.nom_prod = nom_prod;
	}
	
	public Producto() {
		
	}
	public String getRef_producto() {
		return ref_producto;
	}
	public void setRef_producto(String ref_producto) {
		this.ref_producto = ref_producto;
	}
	public float getPrecio() {
		return precio;
	}
	public void setPrecio(float precio) {
		this.precio = precio;
	}
	public int getDescuento() {
		return descuento;
	}
	public void setDescuento(int descuento) {
		this.descuento = descuento;
	}
	public String getNom_prod() {
		return nom_prod;
	}
	public void setNom_prod(String nom_prod) {
		this.nom_prod = nom_prod;
	}
	@Override
	public String toString() {
		return "Producto [ref_producto=" + ref_producto + ", precio=" + precio + ", descuento=" + descuento
				+ ", nom_prod=" + nom_prod + "]";
	}
	
	
}

	
