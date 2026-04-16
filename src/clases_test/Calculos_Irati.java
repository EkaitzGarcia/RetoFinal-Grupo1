package clases_test;

public class Calculos_Irati {

	    public double calcularTotal(double precio, int cantidad, int descuento) {

	        if (precio < 0 || cantidad <= 0)
	            throw new IllegalArgumentException("Datos incorrectos");

	        double total = precio * cantidad;
	        if (descuento > 0) {
	            total = total - (total * descuento / 100.0);
	        }
	        return total;
	    }

	    public boolean puedeComprar(double saldo, double total) {
	        return saldo >= total;
	    }
	    public String obtenerResultado(double saldo, double total) {
	    	if (saldo >= total) { 
	    		return "OK"; 
	    		} 
	    	return null; 
	    }

}