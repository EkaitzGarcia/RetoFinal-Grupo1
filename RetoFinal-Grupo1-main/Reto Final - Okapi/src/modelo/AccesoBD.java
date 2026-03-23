package modelo;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.ResourceBundle;

public class AccesoBD {

	private ResourceBundle configFile;
	private String urlBD;
	private String userBD;
	private String passwordBD;

	// Sentencias SQL
	final String SQL_CLIENTE_POR_DNI = "SELECT * FROM CLIENTE WHERE DNI = ?";
	final String SQL_TODOS_PRODUCTOS = "SELECT * FROM PRODUCTO";
	final String SQL_MAX_ID_COMPRA = "SELECT MAX(ID) FROM COMPRA";
	final String SQL_INSERTAR_COMPRA = "INSERT INTO COMPRA (ID, FECHA, TOTAL, METODO_PAGO, DNI, NSS) VALUES (?, CURDATE(), ?, ?, ?, '111111111111')";
	final String SQL_INSERTAR_ESTA = "INSERT INTO ESTA (ID, REF, CANTIDAD) VALUES (?, ?, ?)";
	final String SQL_COMPRAS_CLIENTE = "SELECT * FROM COMPRA WHERE DNI = ?";
	final String SQL_COMPRA_COMPLETA = "{CALL COMPRA_COMPLETA(?, ?, ?, ?, ?)}";

	// Constructor
	public AccesoBD() {
		this.configFile = ResourceBundle.getBundle("okapi");
		this.urlBD = this.configFile.getString("Conn");
		this.userBD = this.configFile.getString("DBUser");
		this.passwordBD = this.configFile.getString("DBPass");
	}

	// Buscar cliente por DNI
	public void getClientePorDni(String dni, Map<String, Cliente> clientes) throws Exception {
		try (Connection con = DriverManager.getConnection(urlBD, userBD, passwordBD);
				PreparedStatement stmt = con.prepareStatement(SQL_CLIENTE_POR_DNI)) {

			stmt.setString(1, dni);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Cliente cliente = new Cliente();
					cliente.setDni(rs.getString("DNI"));
					cliente.setNom(rs.getString("NOMBRE_C"));
					cliente.setApellido(rs.getString("APELLIDO_C"));
					cliente.setTelefono(rs.getString("TELEFONO_C"));
					cliente.setCorreo(rs.getString("CORREO_C"));
					cliente.setDireccion(rs.getString("DIRECCION"));
					clientes.put(cliente.getDni(), cliente);
				}
			}
		}
	}

	// Obtener todos los productos
	public void getTodosProductos(Map<String, Producto> productos) throws Exception {
		try (Connection con = DriverManager.getConnection(urlBD, userBD, passwordBD);
				PreparedStatement stmt = con.prepareStatement(SQL_TODOS_PRODUCTOS)) {

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Producto producto = new Producto();
					producto.setRef_producto(rs.getString("REF"));
					producto.setPrecio(rs.getFloat("PRECIO"));
					producto.setDescuento(rs.getInt("DESCUENTO"));
					productos.put(producto.getRef_producto(), producto);
				}
			}
		}
	}

	// Insertar compra completa
	public void insertarCompra(String dniCliente, String metodoPago, Producto producto, int cantidad) throws Exception {

// Obtener nuevo ID
		int nuevoId = 1;
		try (Connection con = DriverManager.getConnection(urlBD, userBD, passwordBD);
				PreparedStatement stMax = con.prepareStatement(SQL_MAX_ID_COMPRA);
				ResultSet rs = stMax.executeQuery()) {
			if (rs.next() && rs.getObject(1) != null)
				nuevoId = rs.getInt(1) + 1;
		}

// Llamar al procedimiento
		try (Connection con = DriverManager.getConnection(urlBD, userBD, passwordBD);
				CallableStatement cs = con.prepareCall(SQL_COMPRA_COMPLETA)) {
			cs.setInt(1, nuevoId);
			cs.setDate(2, java.sql.Date.valueOf(java.time.LocalDate.now()));
			cs.setString(3, metodoPago);
			cs.setString(4, dniCliente);
			cs.setString(5, "111111111111");
			cs.execute();
		}

// Insertar producto en ESTA
		try (Connection con = DriverManager.getConnection(urlBD, userBD, passwordBD);
				PreparedStatement stE = con.prepareStatement(SQL_INSERTAR_ESTA)) {
			stE.setInt(1, nuevoId);
			stE.setString(2, producto.getRef_producto());
			stE.setInt(3, cantidad);
			stE.executeUpdate();
		}
	}

	public void getComprasPorCliente(String dni, Map<Integer, Compra> compras) throws Exception {
		try (Connection con = DriverManager.getConnection(urlBD, userBD, passwordBD);
				PreparedStatement stmt = con.prepareStatement(SQL_COMPRAS_CLIENTE)) {

			stmt.setString(1, dni);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Compra compra = new Compra(rs.getInt("ID"), rs.getDate("FECHA").toLocalDate(), rs.getFloat("TOTAL"),
							MetodoPago.valueOf(rs.getString("METODO_PAGO")));
					compras.put(compra.getId_compra(), compra);
				}
			}
		}
	}

	// Main para pruebas
	public static void main(String[] args) {
		try {
			AccesoBD bd = new AccesoBD();
			System.out.println("Conexión OK");

			Map<String, Producto> productos = new java.util.TreeMap<>();
			bd.getTodosProductos(productos);

			for (Producto p : productos.values()) {
				System.out.println(p);
			}

		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
	}
}