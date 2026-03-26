package modelo;

import java.sql.*;
import java.util.*;
import java.util.ResourceBundle;

public class AccesoBD {
	private ResourceBundle configFile;
	private String urlBD;
	private String userBD;
	private String passwordBD;

	final String SQL_CLIENTE_POR_DNI  = "SELECT * FROM CLIENTE WHERE DNI = ?";
	final String SQL_TODOS_PRODUCTOS  = "SELECT * FROM PRODUCTO";
	final String SQL_MAX_ID_COMPRA    = "SELECT MAX(ID) FROM COMPRA";
	final String SQL_INSERTAR_ESTA    = "INSERT INTO ESTA (ID, REF, CANTIDAD) VALUES (?, ?, ?)";
	final String SQL_COMPRAS_CLIENTE  = "SELECT * FROM COMPRA WHERE DNI = ?";
	final String SQL_COMPRA_COMPLETA  = "{CALL COMPRA_COMPLETA(?, ?, ?, ?, ?)}";


	final String SQL_TODAS_COMPRAS_CON_CLIENTE =
		    "SELECT C.ID, C.FECHA, C.TOTAL, C.METODO_PAGO, " +
		    "CL.DNI, CL.NOMBRE_C, CL.APELLIDO_C, CL.TELEFONO_C, CL.CORREO_C, CL.DIRECCION " +
		    "FROM COMPRA C JOIN CLIENTE CL ON C.DNI = CL.DNI " +
		    "ORDER BY C.ID";
	
	final String SQL_COMPRA_POR_ID_CON_CLIENTE =
		"SELECT C.ID, C.FECHA, C.TOTAL, C.METODO_PAGO, " +
		"CL.DNI, CL.NOMBRE_C, CL.APELLIDO_C " +
		"FROM COMPRA C JOIN CLIENTE CL ON C.DNI = CL.DNI " +
		"WHERE C.ID = ?";

	final String SQL_ACTUALIZAR_CLIENTE =
		"UPDATE CLIENTE SET NOMBRE_C=?, APELLIDO_C=?, " +
		"TELEFONO_C=?, CORREO_C=?, DIRECCION=? WHERE DNI=?";

	final String SQL_ACTUALIZAR_COMPRA =
		"UPDATE COMPRA SET FECHA=?, METODO_PAGO=? WHERE ID=?";

	final String SQL_ELIMINAR_ESTA_POR_COMPRA = "DELETE FROM ESTA WHERE ID=?";
	final String SQL_ELIMINAR_COMPRA          = "DELETE FROM COMPRA WHERE ID=?";

	final String SQL_AHORRO_CLIENTE = "SELECT AhorroCliente(?) AS AHORRO";
	
	final String SQL_TODOS_TRABAJADORES = "SELECT * FROM TRABAJADOR";

	public AccesoBD() {
		this.configFile  = ResourceBundle.getBundle("okapi");
		this.urlBD       = this.configFile.getString("Conn");
		this.userBD      = this.configFile.getString("DBUser");
		this.passwordBD  = this.configFile.getString("DBPass");
	}

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
					clientes.put(cliente.getDni(), cliente);
				}
			}
		}
	}

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

	public void insertarCompra(String dniCliente, String metodoPago,
	                            Producto producto, int cantidad) throws Exception {
		int nuevoId = 1;
		try (Connection con = DriverManager.getConnection(urlBD, userBD, passwordBD);
			 PreparedStatement stMax = con.prepareStatement(SQL_MAX_ID_COMPRA);
			 ResultSet rs = stMax.executeQuery()) {
			if (rs.next() && rs.getObject(1) != null)
				nuevoId = rs.getInt(1) + 1;
		}
		try (Connection con = DriverManager.getConnection(urlBD, userBD, passwordBD);
			 CallableStatement cs = con.prepareCall(SQL_COMPRA_COMPLETA)) {
			cs.setInt(1, nuevoId);
			cs.setDate(2, java.sql.Date.valueOf(java.time.LocalDate.now()));
			cs.setString(3, metodoPago);
			cs.setString(4, dniCliente);
			cs.setString(5, "111111111111");
			cs.execute();
		}
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
					Compra compra = new Compra(
						rs.getInt("ID"),
						rs.getDate("FECHA").toLocalDate(),
						rs.getFloat("TOTAL"),
						MetodoPago.valueOf(rs.getString("METODO_PAGO"))
					);
					compras.put(compra.getId_compra(), compra);
				}
			}
		}
	}

	
	public List<Object[]> getTodasLasComprasConCliente() throws Exception {
		List<Object[]> filas = new ArrayList<>();
		try (Connection con = DriverManager.getConnection(urlBD, userBD, passwordBD);
			 PreparedStatement stmt = con.prepareStatement(SQL_TODAS_COMPRAS_CON_CLIENTE);
			 ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				filas.add(new Object[]{
					    rs.getInt("ID"),
					    rs.getString("FECHA"),
					    rs.getFloat("TOTAL"),
					    rs.getString("METODO_PAGO"),
					    rs.getString("DNI"),
					    rs.getString("NOMBRE_C"),
					    rs.getString("APELLIDO_C"),
					    rs.getString("TELEFONO_C"),
					    rs.getString("CORREO_C"),
					    rs.getString("DIRECCION")
					});
			}
		}
		return filas;
	}

	public List<Object[]> getCompraConClientePorId(int id) throws Exception {
		List<Object[]> filas = new ArrayList<>();
		try (Connection con = DriverManager.getConnection(urlBD, userBD, passwordBD);
			 PreparedStatement stmt = con.prepareStatement(SQL_COMPRA_POR_ID_CON_CLIENTE)) {
			stmt.setInt(1, id);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					filas.add(new Object[]{
						    rs.getInt("ID"),
						    rs.getString("FECHA"),
						    rs.getFloat("TOTAL"),
						    rs.getString("METODO_PAGO"),
						    rs.getString("DNI"),
						    rs.getString("NOMBRE_C"),
						    rs.getString("APELLIDO_C"),
						    rs.getString("TELEFONO_C"),
						    rs.getString("CORREO_C"),
						    rs.getString("DIRECCION")
					});
				}
			}
		}
		return filas;
	}

	public void actualizarCliente(Cliente c) throws Exception {
		try (Connection con = DriverManager.getConnection(urlBD, userBD, passwordBD);
			 PreparedStatement stmt = con.prepareStatement(SQL_ACTUALIZAR_CLIENTE)) {
			stmt.setString(1, c.getNom());
			stmt.setString(2, c.getApellido());
			stmt.setString(3, c.getTelefono());
			stmt.setString(4, c.getCorreo());
			stmt.setString(5, c.getDireccion());
			stmt.setString(6, c.getDni());
			stmt.executeUpdate();
		}
	}

	public void actualizarCompra(int id, String fecha, String metodoPago) throws Exception {
		try (Connection con = DriverManager.getConnection(urlBD, userBD, passwordBD);
			 PreparedStatement stmt = con.prepareStatement(SQL_ACTUALIZAR_COMPRA)) {
			stmt.setDate(1, java.sql.Date.valueOf(fecha));
			stmt.setString(2, metodoPago);
			stmt.setInt(3, id);
			stmt.executeUpdate();
		}
	}

	public void eliminarCompra(int id) throws Exception {
		try (Connection con = DriverManager.getConnection(urlBD, userBD, passwordBD)) {
			con.setAutoCommit(false);
			try {
				try (PreparedStatement stEsta = con.prepareStatement(SQL_ELIMINAR_ESTA_POR_COMPRA)) {
					stEsta.setInt(1, id);
					stEsta.executeUpdate();
				}
				try (PreparedStatement stCompra = con.prepareStatement(SQL_ELIMINAR_COMPRA)) {
					stCompra.setInt(1, id);
					stCompra.executeUpdate();
				}
				con.commit();
			} catch (Exception ex) {
				con.rollback();
				throw ex;
			}
		}
	}

	public double getAhorroCliente(String dni) throws Exception {
		try (Connection con = DriverManager.getConnection(urlBD, userBD, passwordBD);
			 PreparedStatement stmt = con.prepareStatement(SQL_AHORRO_CLIENTE)) {
			stmt.setString(1, dni);
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					double valor = rs.getDouble("AHORRO");
					return valor;
				}
			}
		}
		return -1;
	}
	
	public void getTodosLosTrabajaores(List<Trabajador> trabajadores) throws Exception {
	    try (Connection con = DriverManager.getConnection(urlBD, userBD, passwordBD);
	         PreparedStatement stmt = con.prepareStatement(SQL_TODOS_TRABAJADORES);
	         ResultSet rs = stmt.executeQuery()) {
	        while (rs.next()) {
	            Trabajador t = new Trabajador(
	                rs.getString("NSS"),
	                rs.getString("NOMBRE_T"),
	                rs.getString("APELLIDO_T"),
	                rs.getString("TELEFONO_T"),
	                rs.getString("CORREO_T")
	            );
	            trabajadores.add(t);
	        }
	    }
	}
}
