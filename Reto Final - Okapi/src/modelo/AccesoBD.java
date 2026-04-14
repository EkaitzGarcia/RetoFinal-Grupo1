package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.ResourceBundle;

public class AccesoBD implements Interface_Vista_Producto {

	private ResourceBundle configFile;
	private String urlBD;
	private String userBD;
	private String passwordBD;

	//Sentencias SQL
	final String VERPRODUCTO = "{CALL VerProductos()}";
	final String INSERTPRODUCTO = "INSERT INTO PRODUCTO (NOMBRE, REF, PRECIO, DESCUENTO) VALUES (?, ?, ?, ?)";
	final String UPDATEPRODUCTO   = "UPDATE PRODUCTO SET NOMBRE = ?, PRECIO = ?, DESCUENTO = ? WHERE REF = ?";
    final String DELETEPRODUCTO   = "DELETE FROM PRODUCTO WHERE REF = ?";

	public AccesoBD() {
		this.configFile = ResourceBundle.getBundle("configClase");
		this.urlBD = this.configFile.getString("Conn");
		this.userBD = this.configFile.getString("DBUser");
		this.passwordBD = this.configFile.getString("DBPass");
		System.out.println("[DEBUG] URL: " + this.urlBD);
		System.out.println("[DEBUG] User: " + this.userBD);
	}

	@Override
	public void verProductos(Map<Integer, Producto> productos) throws Exception {
		System.out.println("[DEBUG] Intentando conectar a la BD...");

		try (Connection con = DriverManager.getConnection(urlBD, this.userBD, this.passwordBD)) {
			System.out.println("[DEBUG] Conexión establecida correctamente.");

			try (CallableStatement stmt = con.prepareCall(VERPRODUCTO)) {
				System.out.println("[DEBUG] Ejecutando CALL VerProductos()...");

				boolean tieneResultSet = stmt.execute();
				System.out.println("[DEBUG] tieneResultSet: " + tieneResultSet);

				if (tieneResultSet) {
					try (ResultSet rs = stmt.getResultSet()) {
						int indice = 0;
						while (rs.next()) {
							Producto producto = new Producto();
							producto.setRef_producto(rs.getString("REF"));
							producto.setNom_prod(rs.getString("NOMBRE"));
							producto.setPrecio(rs.getFloat("PRECIO"));
							producto.setDescuento(rs.getInt("DESCUENTO"));
							productos.put(indice++, producto);
							System.out.println("[DEBUG] Producto leído: " + producto.toString());
						}
						System.out.println("[DEBUG] Total productos cargados: " + productos.size());
					}
				} else {
					System.out.println("[DEBUG] El CALL no devolvió ningún ResultSet.");
				}
			}
		}
	}
	
	public void insertarProducto(Producto producto) throws Exception {
		try (Connection con = DriverManager.getConnection(urlBD, this.userBD, this.passwordBD);
			 PreparedStatement stmt = con.prepareStatement(INSERTPRODUCTO)) {
 
			stmt.setString(1, producto.getNom_prod());
			stmt.setString(2, producto.getRef_producto());
			stmt.setFloat(3, producto.getPrecio());
			stmt.setInt(4, producto.getDescuento());
 
			stmt.executeUpdate();
		}
	}
	
	public void actualizarProducto(Producto producto) throws Exception {
        try (Connection con = DriverManager.getConnection(urlBD, this.userBD, this.passwordBD);
             PreparedStatement stmt = con.prepareStatement(UPDATEPRODUCTO)) {
 
            stmt.setString(1, producto.getNom_prod());
            stmt.setFloat(2, producto.getPrecio());
            stmt.setInt(3, producto.getDescuento());
            stmt.setString(4, producto.getRef_producto());
            stmt.executeUpdate();
        }
    }
 
    public void eliminarProducto(String ref) throws Exception {
        try (Connection con = DriverManager.getConnection(urlBD, this.userBD, this.passwordBD);
             PreparedStatement stmt = con.prepareStatement(DELETEPRODUCTO)) {
 
            stmt.setString(1, ref);
            stmt.executeUpdate();
        }
    }
}