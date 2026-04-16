package modelo;

public class Esta {

    private Compra compra;
    private Producto producto;
    private int cantidad;

    public Esta(Compra compra, Producto producto, int cantidad) {
        this.compra = compra;
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public Compra getCompra() {
        return compra;
    }

    public Producto getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}