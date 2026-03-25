package modelo;

import modelo.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

public class AccesoBD implements Interface_Vista_Producto {
	// Los siguientes atributos se utilizan para recoger los valores del fich de
	// configuracion
	private ResourceBundle configFile;
	private String urlBD;
	private String userBD;
	private String passwordBD;

		
	final String VERPRODUCTO = "CALL VerProductos();"; //Procedure para listar los productos de la tabla Producto.

	// Para la conexion utilizamos un fichero de configuaracion, config que
	// guardamos en el paquete control:
	public AccesoBD() {
		this.configFile = ResourceBundle.getBundle("configClase");
		this.urlBD = this.configFile.getString("Conn");
		this.userBD = this.configFile.getString("DBUser");
		this.passwordBD = this.configFile.getString("DBPass");
	}
	
	public void verProductos(String ref_producto, Map<Integer, Producto> productos) throws Exception {
		try(Connection con = DriverManager.getConnection(urlBD, this.userBD, this.passwordBD);
				PreparedStatement stmt = con.prepareStatement(VERPRODUCTO)) {
			stmt.setString(1, ref_producto);
			
			try(ResultSet rs = stmt.executeQuery()){
				while(rs.next()) {
					
					Producto producto = new Producto();
					producto.setRef_producto(rs.getString("REF"));
					producto.setNom_prod(rs.getString("NOMBRE"));
					producto.setPrecio(rs.getFloat("PRECIO"));
					producto.setDescuento(rs.getInt("DESCUENTO"));
				}
			}
		}
	}
	
}