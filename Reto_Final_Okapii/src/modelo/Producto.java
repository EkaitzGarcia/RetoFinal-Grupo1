package modelo;

public class Producto {
	
	private String ref_producto;
	private float precio;
	private int descuento;
	public Producto(String ref_producto, float precio, int descuento, String nom_prod) {
		super();
		this.ref_producto = ref_producto;
		this.precio = precio;
		this.descuento = descuento;
		
	}
    public Producto() {}
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
	
	@Override
	public String toString() {
	    if (descuento > 0) {
	        return ref_producto + " - " + String.format("%.2f", precio) + "€  *** DESCUENTO " + descuento + "% ***";
	    }
	    return ref_producto + " - " + String.format("%.2f", precio) + "€";
	}
	
	
}

	