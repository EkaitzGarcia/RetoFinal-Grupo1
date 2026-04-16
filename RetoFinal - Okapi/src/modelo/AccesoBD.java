package modelo;

import java.sql.*;
import java.util.*;

public class AccesoBD {
	private ResourceBundle configFile;
	private String urlBD;
	private String userBD;
	private String passwordBD;

	final String SQL_CLIENTE_POR_DNI = "SELECT * FROM CLIENTE WHERE DNI = ?";
	final String SQL_TODOS_PRODUCTOS = "SELECT * FROM PRODUCTO";
	final String SQL_MAX_ID_COMPRA = "SELECT MAX(ID) FROM COMPRA";
	final String SQL_INSERTAR_ESTA = "INSERT INTO ESTA (ID, REF, CANTIDAD) VALUES (?, ?, ?)";
	final String SQL_COMPRAS_CLIENTE = "SELECT * FROM COMPRA WHERE DNI = ?";
	final String SQL_COMPRA_COMPLETA = "{CALL COMPRA_COMPLETA(?, ?, ?, ?, ?)}";

	final String SQL_ACTUALIZAR_TOTAL = "UPDATE COMPRA SET TOTAL = TOTALFINALCOMPRAAVANZADO(?) WHERE ID = ?";

	final String SQL_TODAS_COMPRAS_CON_CLIENTE = "SELECT C.ID, C.FECHA, C.TOTAL, C.METODO_PAGO, "
			+ "CL.DNI, CL.NOMBRE_C, CL.APELLIDO_C, CL.TELEFONO_C, CL.CORREO_C, CL.DIRECCION "
			+ "FROM COMPRA C JOIN CLIENTE CL ON C.DNI = CL.DNI ORDER BY C.ID";

	final String SQL_COMPRA_POR_ID_CON_CLIENTE = "SELECT C.ID, C.FECHA, C.TOTAL, C.METODO_PAGO, "
			+ "CL.DNI, CL.NOMBRE_C, CL.APELLIDO_C, CL.TELEFONO_C, CL.CORREO_C, CL.DIRECCION "
			+ "FROM COMPRA C JOIN CLIENTE CL ON C.DNI = CL.DNI WHERE C.ID = ?";

	final String SQL_PRODUCTOS_DE_COMPRA = "SELECT E.REF, E.CANTIDAD, P.PRECIO, P.DESCUENTO "
			+ "FROM ESTA E JOIN PRODUCTO P ON E.REF = P.REF WHERE E.ID = ?";

	final String SQL_ACTUALIZAR_CLIENTE = "UPDATE CLIENTE SET NOMBRE_C=?, APELLIDO_C=?, TELEFONO_C=?, CORREO_C=?, DIRECCION=? WHERE DNI=?";

	final String SQL_ACTUALIZAR_COMPRA = "UPDATE COMPRA SET FECHA=?, METODO_PAGO=? WHERE ID=?";

	final String SQL_ELIMINAR_ESTA_POR_COMPRA = "DELETE FROM ESTA WHERE ID=?";
	final String SQL_ELIMINAR_COMPRA = "DELETE FROM COMPRA WHERE ID=?";

	final String SQL_AHORRO_CLIENTE = "SELECT AhorroCliente(?) AS AHORRO";
	final String SQL_TODOS_TRABAJADORES = "SELECT * FROM TRABAJADOR";

	final String SQL_TODOS_CLIENTES = "SELECT * FROM CLIENTE";

	final String SQL_INSERTAR_CLIENTE = "INSERT INTO CLIENTE (DNI, NOMBRE_C, APELLIDO_C, TELEFONO_C, CORREO_C, DIRECCION) "
			+ "VALUES (?, ?, ?, ?, ?, ?)";

	final String SQL_ELIMINAR_CLIENTE = "DELETE FROM CLIENTE WHERE DNI = ?";

	final String SQL_INSERTAR_TRABAJADOR = "INSERT INTO TRABAJADOR (NSS, NOMBRE_T, APELLIDO_T, TELEFONO_T, CORREO_T) "
			+ "VALUES (?, ?, ?, ?, ?)";

	final String SQL_ACTUALIZAR_TRABAJADOR = "UPDATE TRABAJADOR SET NOMBRE_T=?, APELLIDO_T=?, TELEFONO_T=?, CORREO_T=? "
			+ "WHERE NSS=?";

	final String SQL_ELIMINAR_TRABAJADOR = "DELETE FROM TRABAJADOR WHERE NSS = ?";

	final String SQL_ELIMINAR_ESTA_POR_DNI = "DELETE FROM ESTA WHERE ID IN (SELECT ID FROM COMPRA WHERE DNI = ?)";

	final String SQL_ELIMINAR_COMPRAS_POR_DNI = "DELETE FROM COMPRA WHERE DNI = ?";

	final String SQL_ALTA_TRABAJADOR_PROC = "{CALL ALTATRABAJADOR_VALIDADO(?, ?, ?, ?, ?)}";

	final String VERPRODUCTO = "{CALL VerProductos()}";

	final String INSERTPRODUCTO = "INSERT INTO PRODUCTO (NOMBRE, REF, PRECIO, DESCUENTO) VALUES (?, ?, ?, ?)";

	final String UPDATEPRODUCTO = "UPDATE PRODUCTO SET NOMBRE = ?, PRECIO = ?, DESCUENTO = ? WHERE REF = ?";

	final String DELETEPRODUCTO = "DELETE FROM PRODUCTO WHERE REF = ?";

	public AccesoBD() {
		this.configFile = ResourceBundle.getBundle("okapi");
		this.urlBD = this.configFile.getString("Conn");
		this.userBD = this.configFile.getString("DBUser");
		this.passwordBD = this.configFile.getString("DBPass");
	}

	public void getClientePorDni(String dni, Map<String, Cliente> clientes) throws Exception {
		try (Connection con = DriverManager.getConnection(urlBD, userBD, passwordBD);
				PreparedStatement stmt = con.prepareStatement(SQL_CLIENTE_POR_DNI)) {
			stmt.setString(1, dni);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Cliente c = new Cliente();
					c.setDni(rs.getString("DNI"));
					c.setNom(rs.getString("NOMBRE_C"));
					c.setApellido(rs.getString("APELLIDO_C"));
					c.setTelefono(rs.getString("TELEFONO_C"));
					c.setCorreo(rs.getString("CORREO_C"));
					c.setDireccion(rs.getString("DIRECCION"));
					clientes.put(c.getDni(), c);
				}
			}
		}
	}

	public void getTodosProductos(Map<String, Producto> productos) throws Exception {
		try (Connection con = DriverManager.getConnection(urlBD, userBD, passwordBD);
				PreparedStatement stmt = con.prepareStatement(SQL_TODOS_PRODUCTOS)) {
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Producto p = new Producto();
					p.setRef_producto(rs.getString("REF"));
					p.setPrecio(rs.getFloat("PRECIO"));
					p.setDescuento(rs.getInt("DESCUENTO"));
					productos.put(p.getRef_producto(), p);
				}
			}
		}
	}

	public void insertarCompraMultiple(String dniCliente, String metodoPago, List<Producto> productos,
			List<Integer> cantidades) throws Exception {
		int nuevoId = 1;
		try (Connection con = DriverManager.getConnection(urlBD, userBD, passwordBD);
				PreparedStatement st = con.prepareStatement(SQL_MAX_ID_COMPRA);
				ResultSet rs = st.executeQuery()) {
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
				PreparedStatement st = con.prepareStatement(SQL_INSERTAR_ESTA)) {
			for (int i = 0; i < productos.size(); i++) {
				st.setInt(1, nuevoId);
				st.setString(2, productos.get(i).getRef_producto());
				st.setInt(3, cantidades.get(i));
				st.addBatch();
			}
			st.executeBatch();
		}
		try (Connection con = DriverManager.getConnection(urlBD, userBD, passwordBD);
				PreparedStatement st = con.prepareStatement(SQL_ACTUALIZAR_TOTAL)) {
			st.setInt(1, nuevoId);
			st.setInt(2, nuevoId);
			st.executeUpdate();
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

	public List<Object[]> getTodasLasComprasConCliente() throws Exception {
		List<Object[]> filas = new ArrayList<>();
		try (Connection con = DriverManager.getConnection(urlBD, userBD, passwordBD);
				PreparedStatement stmt = con.prepareStatement(SQL_TODAS_COMPRAS_CON_CLIENTE);
				ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				filas.add(new Object[] { rs.getInt("ID"), rs.getString("FECHA"), rs.getFloat("TOTAL"),
						rs.getString("METODO_PAGO"), rs.getString("DNI"), rs.getString("NOMBRE_C"),
						rs.getString("APELLIDO_C"), rs.getString("TELEFONO_C"), rs.getString("CORREO_C"),
						rs.getString("DIRECCION") });
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
					filas.add(new Object[] { rs.getInt("ID"), rs.getString("FECHA"), rs.getFloat("TOTAL"),
							rs.getString("METODO_PAGO"), rs.getString("DNI"), rs.getString("NOMBRE_C"),
							rs.getString("APELLIDO_C"), rs.getString("TELEFONO_C"), rs.getString("CORREO_C"),
							rs.getString("DIRECCION") });
				}
			}
		}
		return filas;
	}

	public List<Object[]> getProductosDeCompra(int idCompra) throws Exception {
		List<Object[]> filas = new ArrayList<>();
		try (Connection con = DriverManager.getConnection(urlBD, userBD, passwordBD);
				PreparedStatement stmt = con.prepareStatement(SQL_PRODUCTOS_DE_COMPRA)) {
			stmt.setInt(1, idCompra);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					filas.add(new Object[] { rs.getString("REF"), rs.getInt("CANTIDAD"), rs.getFloat("PRECIO"),
							rs.getInt("DESCUENTO") });
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
				try (PreparedStatement st = con.prepareStatement(SQL_ELIMINAR_ESTA_POR_COMPRA)) {
					st.setInt(1, id);
					st.executeUpdate();
				}
				try (PreparedStatement st = con.prepareStatement(SQL_ELIMINAR_COMPRA)) {
					st.setInt(1, id);
					st.executeUpdate();
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
				if (rs.next())
					return rs.getDouble("AHORRO");
			}
		}
		return -1;
	}

	public void insertarCompraMultiple(String dni, String metodo, Producto p, int cantidad) {
		// TODO Auto-generated method stub

	}

	public void insertarCompra(String dniCliente, String metodoPago, Producto producto, int cantidad, double total)
			throws Exception {

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
				PreparedStatement stTotal = con.prepareStatement("UPDATE COMPRA SET TOTAL = ? WHERE ID = ?")) {
			stTotal.setDouble(1, total);
			stTotal.setInt(2, nuevoId);
			stTotal.executeUpdate();
		}

		try (Connection con = DriverManager.getConnection(urlBD, userBD, passwordBD);
				PreparedStatement stE = con.prepareStatement(SQL_INSERTAR_ESTA)) {
			stE.setInt(1, nuevoId);
			stE.setString(2, producto.getRef_producto());
			stE.setInt(3, cantidad);
			stE.executeUpdate();
		}
	}

	public void getTodosLosTrabajaores(List<Trabajador> trabajadores) throws Exception {
		try (Connection con = DriverManager.getConnection(urlBD, userBD, passwordBD);
				PreparedStatement stmt = con.prepareStatement(SQL_TODOS_TRABAJADORES);
				ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				Trabajador t = new Trabajador(rs.getString("NSS"), rs.getString("NOMBRE_T"), rs.getString("APELLIDO_T"),
						rs.getString("TELEFONO_T"), rs.getString("CORREO_T"));
				trabajadores.add(t);
			}
		}
	}

	public void getTodosClientes(Map<String, Cliente> clientes) throws Exception {
		try (Connection con = DriverManager.getConnection(urlBD, userBD, passwordBD);
				PreparedStatement stmt = con.prepareStatement(SQL_TODOS_CLIENTES);
				ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				Cliente c = new Cliente();
				c.setDni(rs.getString("DNI"));
				c.setNom(rs.getString("NOMBRE_C"));
				c.setApellido(rs.getString("APELLIDO_C"));
				c.setTelefono(rs.getString("TELEFONO_C"));
				c.setCorreo(rs.getString("CORREO_C"));
				c.setDireccion(rs.getString("DIRECCION"));

				clientes.put(c.getDni(), c);
			}
		}
	}

	public void getTodosClientes(List<Cliente> lista) throws Exception {
		Map<String, Cliente> mapa = new HashMap<>();
		getTodosClientes(mapa); // reutilizas el método original
		lista.addAll(mapa.values()); // conviertes a lista para la UI
	}

	public void insertarCliente(Cliente c) throws Exception {
		try (Connection con = DriverManager.getConnection(urlBD, userBD, passwordBD);
				PreparedStatement stmt = con.prepareStatement(SQL_INSERTAR_CLIENTE)) {
			stmt.setString(1, c.getDni());
			stmt.setString(2, c.getNom());
			stmt.setString(3, c.getApellido());
			stmt.setString(4, c.getTelefono());
			stmt.setString(5, c.getCorreo());
			stmt.setString(6, c.getDireccion());
			stmt.executeUpdate();
		}
	}

	public void eliminarCliente(Cliente c) throws Exception {
		try (Connection con = DriverManager.getConnection(urlBD, userBD, passwordBD)) {
			con.setAutoCommit(false);
			try {
				// 1. Borrar líneas de ESTA asociadas a las compras del cliente
				try (PreparedStatement stEsta = con.prepareStatement(SQL_ELIMINAR_ESTA_POR_DNI)) {
					stEsta.setString(1, c.getDni());
					stEsta.executeUpdate();
				}
				// 2. Borrar las compras del cliente
				try (PreparedStatement stCompra = con.prepareStatement(SQL_ELIMINAR_COMPRAS_POR_DNI)) {
					stCompra.setString(1, c.getDni());
					stCompra.executeUpdate();
				}
				// 3. Borrar el cliente
				try (PreparedStatement stCliente = con.prepareStatement(SQL_ELIMINAR_CLIENTE)) {
					stCliente.setString(1, c.getDni());
					stCliente.executeUpdate();
				}
				con.commit();
			} catch (Exception ex) {
				con.rollback();
				throw ex;
			}
		}
	}

	/**
	 * Inserta un nuevo trabajador en la base de datos.
	 *
	 * @param t Trabajador a insertar
	 * @throws Exception Si ocurre un error de conexión o SQL
	 */
	public void insertarTrabajador(Trabajador t) throws Exception {
		try (Connection con = DriverManager.getConnection(urlBD, userBD, passwordBD);
				PreparedStatement stmt = con.prepareStatement(SQL_INSERTAR_TRABAJADOR)) {
			stmt.setString(1, t.getNss());
			stmt.setString(2, t.getNomTrabajador());
			stmt.setString(3, t.getApeTrabajador());
			stmt.setString(4, t.getTlfnTrabajador());
			stmt.setString(5, t.getCorreoTrabajador());
			stmt.executeUpdate();
		}
	}

	/**
	 * Actualiza los datos de un trabajador existente.
	 *
	 * @param t Trabajador con los datos actualizados
	 * @throws Exception Si ocurre un error de conexión o SQL
	 */
	public void actualizarTrabajador(Trabajador t) throws Exception {
		try (Connection con = DriverManager.getConnection(urlBD, userBD, passwordBD);
				PreparedStatement stmt = con.prepareStatement(SQL_ACTUALIZAR_TRABAJADOR)) {
			stmt.setString(1, t.getNomTrabajador());
			stmt.setString(2, t.getApeTrabajador());
			stmt.setString(3, t.getTlfnTrabajador());
			stmt.setString(4, t.getCorreoTrabajador());
			stmt.setString(5, t.getNss());
			stmt.executeUpdate();
		}
	}

	/**
	 * Elimina un trabajador de la base de datos por su NSS.
	 *
	 * @param t Trabajador a eliminar (solo se usa el NSS)
	 * @throws Exception Si ocurre un error de conexión o SQL
	 */
	public void eliminarTrabajador(Trabajador t) throws Exception {
		try (Connection con = DriverManager.getConnection(urlBD, userBD, passwordBD);
				PreparedStatement stmt = con.prepareStatement(SQL_ELIMINAR_TRABAJADOR)) {
			stmt.setString(1, t.getNss());
			stmt.executeUpdate();
		}
	}

	public String altaTrabajadorProcedimiento(Trabajador t) throws Exception {
		try (Connection con = DriverManager.getConnection(urlBD, userBD, passwordBD);
				CallableStatement cs = con.prepareCall(SQL_ALTA_TRABAJADOR_PROC)) {
			cs.setString(1, t.getNss());
			cs.setString(2, t.getNomTrabajador());
			cs.setString(3, t.getApeTrabajador());
			cs.setString(4, t.getTlfnTrabajador());
			cs.setString(5, t.getCorreoTrabajador());

			boolean tieneResultado = cs.execute();
			if (tieneResultado) {
				try (ResultSet rs = cs.getResultSet()) {
					if (rs.next()) {
						return rs.getString("MENSAJE");
					}
				}
			}
		}
		return "OPERACIÓN COMPLETADA";
	}

	public void actualizarMetodoPago(int idCompra, String metodo) throws Exception {

		String sql = "UPDATE COMPRA SET METODO_PAGO = ? WHERE ID = ?";

		try (Connection con = DriverManager.getConnection(urlBD, userBD, passwordBD);

				PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, metodo);

			ps.setInt(2, idCompra);

			ps.executeUpdate();

		}

	}

	public void verProductos(Map<Integer, Producto> productos) throws Exception {
		System.out.println("[DEBUG] Intentando conectar a la BD...");

		try (Connection con = DriverManager.getConnection(urlBD, this.userBD, this.passwordBD)) {
			System.out.println("[DEBUG] Conexión establecida correctamente.");

			try (CallableStatement stmt = con.prepareCall(VERPRODUCTO)) {
				System.out.println("[DEBUG] Ejecutando CALL VerProductos()...");

				int indice =0;
				boolean tieneResultSet = stmt.execute();
				
				System.out.println("[DEBUG] tieneResultSet: " + tieneResultSet);

				while (tieneResultSet) {
					try (ResultSet rs = stmt.getResultSet()) {
						
						while (rs.next()) {
							Producto producto = new Producto();
							producto.setRef_producto(rs.getString("REF"));
							producto.setNom_prod(rs.getString("NOMBRE"));
							producto.setPrecio(rs.getFloat("PRECIO"));
							producto.setDescuento(rs.getInt("DESCUENTO"));
							productos.put(indice++, producto);
							System.out.println("[DEBUG] Producto leído: " + producto.toString());
						}
						
					}			
					tieneResultSet = stmt.getMoreResults();
				}
				System.out.println("[DEBUG] Total productos cargados: " + productos.size());
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