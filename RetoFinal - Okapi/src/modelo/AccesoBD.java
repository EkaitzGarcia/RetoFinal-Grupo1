package modelo;

import java.sql.*;
import java.util.*;

/**
 * Clase de acceso a la base de datos del sistema Okapi.
 * <p>
 * Proporciona todos los métodos necesarios para realizar operaciones CRUD
 * (Create, Read, Update, Delete) sobre las entidades del sistema:
 * {@link Cliente}, {@link Producto}, {@link Trabajador} y {@link Compra}.
 * </p>
 * <p>
 * La configuración de conexión (URL, usuario y contraseña) se lee
 * automáticamente del fichero de propiedades {@code okapi.properties}
 * al instanciar la clase.
 * </p>
 *
 * @see Cliente
 * @see Producto
 * @see Trabajador
 * @see Compra
 */
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

	/**
	 * Constructor de {@code AccesoBD}.
	 * <p>
	 * Carga automáticamente los parámetros de conexión (URL, usuario y contraseña)
	 * desde el fichero de propiedades {@code okapi.properties} ubicado en el
	 * classpath del proyecto.
	 * </p>
	 */
	public AccesoBD() {
		this.configFile = ResourceBundle.getBundle("okapi");
		this.urlBD = this.configFile.getString("Conn");
		this.userBD = this.configFile.getString("DBUser");
		this.passwordBD = this.configFile.getString("DBPass");
	}

	/**
	 * Busca un cliente en la base de datos por su DNI y lo añade al mapa proporcionado.
	 *
	 * @param dni      DNI del cliente a buscar (p. ej. {@code "12345678A"}).
	 * @param clientes Mapa donde se almacenará el cliente encontrado,
	 *                 indexado por su DNI.
	 * @throws Exception Si ocurre un error de conexión o de consulta SQL.
	 */
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

	/**
	 * Recupera todos los productos de la base de datos mediante una consulta directa
	 * a la tabla {@code PRODUCTO} y los almacena en el mapa proporcionado.
	 *
	 * @param productos Mapa donde se almacenarán los productos obtenidos,
	 *                  indexados por su referencia ({@code REF}).
	 * @throws Exception Si ocurre un error de conexión o de consulta SQL.
	 */
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

	/**
	 * Inserta una compra con múltiples productos en la base de datos.
	 * <p>
	 * El proceso realiza tres operaciones en secuencia:
	 * <ol>
	 *   <li>Llama al procedimiento almacenado {@code COMPRA_COMPLETA} para crear la cabecera.</li>
	 *   <li>Inserta cada línea de producto en la tabla {@code ESTA} mediante un batch.</li>
	 *   <li>Actualiza el total de la compra usando la función {@code TOTALFINALCOMPRAAVANZADO}.</li>
	 * </ol>
	 * </p>
	 *
	 * @param dniCliente  DNI del cliente que realiza la compra.
	 * @param metodoPago  Método de pago utilizado ({@code "TARJETA"} o {@code "EFECTIVO"}).
	 * @param productos   Lista de productos incluidos en la compra.
	 * @param cantidades  Lista de cantidades correspondientes a cada producto
	 *                    (debe tener el mismo tamaño que {@code productos}).
	 * @throws Exception Si ocurre un error de conexión o de operación SQL.
	 */
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

	/**
	 * Recupera todas las compras realizadas por un cliente identificado por su DNI.
	 *
	 * @param dni     DNI del cliente cuyas compras se desean obtener.
	 * @param compras Mapa donde se almacenarán las compras encontradas,
	 *                indexadas por el ID de compra.
	 * @throws Exception Si ocurre un error de conexión o de consulta SQL.
	 */
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

	/**
	 * Recupera todas las compras registradas en el sistema, incluyendo los datos
	 * del cliente asociado a cada una, ordenadas por ID de compra.
	 * <p>
	 * Cada elemento de la lista es un array de objetos con la siguiente estructura:
	 * {@code [ID, FECHA, TOTAL, METODO_PAGO, DNI, NOMBRE_C, APELLIDO_C, TELEFONO_C, CORREO_C, DIRECCION]}
	 * </p>
	 *
	 * @return Lista de arrays de objetos con los datos de compras y clientes.
	 * @throws Exception Si ocurre un error de conexión o de consulta SQL.
	 */
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

	/**
	 * Recupera una compra específica junto con los datos del cliente asociado,
	 * buscada por el identificador de la compra.
	 * <p>
	 * Cada elemento de la lista es un array de objetos con la siguiente estructura:
	 * {@code [ID, FECHA, TOTAL, METODO_PAGO, DNI, NOMBRE_C, APELLIDO_C, TELEFONO_C, CORREO_C, DIRECCION]}
	 * </p>
	 *
	 * @param id Identificador único de la compra a buscar.
	 * @return Lista con un único array de objetos si se encuentra la compra,
	 *         o lista vacía si no existe ninguna compra con ese ID.
	 * @throws Exception Si ocurre un error de conexión o de consulta SQL.
	 */
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

	/**
	 * Recupera los productos asociados a una compra concreta.
	 * <p>
	 * Cada elemento de la lista es un array de objetos con la siguiente estructura:
	 * {@code [REF, CANTIDAD, PRECIO, DESCUENTO]}
	 * </p>
	 *
	 * @param idCompra Identificador de la compra cuyos productos se desean obtener.
	 * @return Lista de arrays de objetos con los datos de cada línea de producto.
	 * @throws Exception Si ocurre un error de conexión o de consulta SQL.
	 */
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

	/**
	 * Actualiza los datos personales de un cliente existente en la base de datos.
	 * <p>
	 * Modifica los campos: nombre, apellido, teléfono, correo y dirección.
	 * El DNI se usa como clave de búsqueda y no se modifica.
	 * </p>
	 *
	 * @param c Objeto {@link Cliente} con los nuevos datos. El DNI debe estar
	 *          establecido para identificar al cliente a actualizar.
	 * @throws Exception Si ocurre un error de conexión o de actualización SQL.
	 */
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

	/**
	 * Actualiza la fecha y el método de pago de una compra existente.
	 *
	 * @param id         Identificador único de la compra a actualizar.
	 * @param fecha      Nueva fecha de la compra en formato {@code "yyyy-MM-dd"}.
	 * @param metodoPago Nuevo método de pago ({@code "TARJETA"} o {@code "EFECTIVO"}).
	 * @throws Exception Si ocurre un error de conexión, de formato de fecha
	 *                   o de actualización SQL.
	 */
	public void actualizarCompra(int id, String fecha, String metodoPago) throws Exception {
		try (Connection con = DriverManager.getConnection(urlBD, userBD, passwordBD);
				PreparedStatement stmt = con.prepareStatement(SQL_ACTUALIZAR_COMPRA)) {
			stmt.setDate(1, java.sql.Date.valueOf(fecha));
			stmt.setString(2, metodoPago);
			stmt.setInt(3, id);
			stmt.executeUpdate();
		}
	}

	/**
	 * Elimina una compra de la base de datos de forma transaccional.
	 * <p>
	 * El proceso elimina primero las líneas de la tabla {@code ESTA} asociadas
	 * a la compra y, a continuación, elimina el registro de la tabla {@code COMPRA}.
	 * Si cualquiera de las dos operaciones falla, se realiza un {@code rollback}
	 * para mantener la consistencia de los datos.
	 * </p>
	 *
	 * @param id Identificador único de la compra a eliminar.
	 * @throws Exception Si ocurre un error de conexión o de eliminación SQL,
	 *                   propagándose tras el rollback.
	 */
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

	/**
	 * Calcula el ahorro total acumulado por un cliente gracias a los descuentos
	 * aplicados en sus compras, invocando la función SQL {@code AhorroCliente}.
	 *
	 * @param dni DNI del cliente cuyo ahorro se desea calcular.
	 * @return Ahorro total en euros como valor {@code double},
	 *         o {@code -1} si el cliente no existe o no tiene compras.
	 * @throws Exception Si ocurre un error de conexión o de consulta SQL.
	 */
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

	/**
	 * Sobrecarga de {@link #insertarCompraMultiple(String, String, List, List)}
	 * para un único producto.
	 * <p>
	 * <b>Nota:</b> Este método está pendiente de implementación.
	 * </p>
	 *
	 * @param dni      DNI del cliente que realiza la compra.
	 * @param metodo   Método de pago ({@code "TARJETA"} o {@code "EFECTIVO"}).
	 * @param p        Producto a incluir en la compra.
	 * @param cantidad Cantidad del producto.
	 * @deprecated Pendiente de implementación. Usar
	 *             {@link #insertarCompra(String, String, Producto, int, double)} en su lugar.
	 */
	public void insertarCompraMultiple(String dni, String metodo, Producto p, int cantidad) {
		// TODO Auto-generated method stub

	}

	/**
	 * Inserta una compra con un único producto en la base de datos.
	 * <p>
	 * El proceso:
	 * <ol>
	 *   <li>Obtiene el siguiente ID disponible consultando el máximo actual.</li>
	 *   <li>Llama al procedimiento {@code COMPRA_COMPLETA} para crear la cabecera.</li>
	 *   <li>Actualiza el total de la compra con el valor proporcionado.</li>
	 *   <li>Inserta la línea del producto en la tabla {@code ESTA}.</li>
	 * </ol>
	 * </p>
	 *
	 * @param dniCliente  DNI del cliente que realiza la compra.
	 * @param metodoPago  Método de pago ({@code "TARJETA"} o {@code "EFECTIVO"}).
	 * @param producto    Producto a incluir en la compra.
	 * @param cantidad    Cantidad del producto comprado.
	 * @param total       Importe total de la compra en euros.
	 * @throws Exception Si ocurre un error de conexión o de operación SQL.
	 */
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

	/**
	 * Recupera todos los trabajadores registrados en la base de datos.
	 *
	 * @param trabajadores Lista donde se añadirán los trabajadores obtenidos.
	 * @throws Exception Si ocurre un error de conexión o de consulta SQL.
	 */
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

	/**
	 * Recupera todos los clientes registrados en la base de datos
	 * y los almacena en el mapa proporcionado.
	 *
	 * @param clientes Mapa donde se almacenarán los clientes obtenidos,
	 *                 indexados por su DNI.
	 * @throws Exception Si ocurre un error de conexión o de consulta SQL.
	 */
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

	/**
	 * Sobrecarga de {@link #getTodosClientes(Map)} que devuelve los clientes
	 * en forma de lista en lugar de mapa, para facilitar su uso en componentes
	 * de interfaz gráfica.
	 *
	 * @param lista Lista donde se añadirán todos los clientes obtenidos.
	 * @throws Exception Si ocurre un error de conexión o de consulta SQL.
	 */
	public void getTodosClientes(List<Cliente> lista) throws Exception {
		Map<String, Cliente> mapa = new HashMap<>();
		getTodosClientes(mapa); // reutilizas el método original
		lista.addAll(mapa.values()); // conviertes a lista para la UI
	}

	/**
	 * Inserta un nuevo cliente en la base de datos.
	 *
	 * @param c Objeto {@link Cliente} con todos los datos del nuevo cliente
	 *          (DNI, nombre, apellido, teléfono, correo y dirección).
	 * @throws Exception Si ocurre un error de conexión, de inserción SQL
	 *                   o si el DNI ya existe (clave primaria duplicada).
	 */
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

	/**
	 * Elimina un cliente de la base de datos de forma transaccional.
	 * <p>
	 * Para mantener la integridad referencial, el proceso realiza las siguientes
	 * operaciones en orden dentro de una misma transacción:
	 * <ol>
	 *   <li>Elimina las líneas de {@code ESTA} asociadas a las compras del cliente.</li>
	 *   <li>Elimina las compras del cliente en la tabla {@code COMPRA}.</li>
	 *   <li>Elimina el registro del cliente en la tabla {@code CLIENTE}.</li>
	 * </ol>
	 * Si alguna operación falla, se realiza un {@code rollback} completo.
	 * </p>
	 *
	 * @param c Objeto {@link Cliente} cuyo DNI se usará para identificar
	 *          y eliminar todos sus registros asociados.
	 * @throws Exception Si ocurre un error de conexión o de eliminación SQL,
	 *                   propagándose tras el rollback.
	 */
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

	/**
	 * Da de alta un trabajador invocando el procedimiento almacenado
	 * {@code ALTATRABAJADOR_VALIDADO}, que realiza validaciones adicionales
	 * en la base de datos (p. ej. comprobar si el NSS ya existe).
	 *
	 * @param t Objeto {@link Trabajador} con los datos del trabajador a registrar.
	 * @return Mensaje de resultado devuelto por el procedimiento almacenado
	 *         (p. ej. confirmación o aviso de duplicado),
	 *         o {@code "OPERACIÓN COMPLETADA"} si no hay mensaje explícito.
	 * @throws Exception Si ocurre un error de conexión o de ejecución del procedimiento.
	 */
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

	/**
	 * Actualiza únicamente el método de pago de una compra existente.
	 *
	 * @param idCompra Identificador único de la compra a modificar.
	 * @param metodo   Nuevo método de pago ({@code "TARJETA"} o {@code "EFECTIVO"}).
	 * @throws Exception Si ocurre un error de conexión o de actualización SQL.
	 */
	public void actualizarMetodoPago(int idCompra, String metodo) throws Exception {

		String sql = "UPDATE COMPRA SET METODO_PAGO = ? WHERE ID = ?";

		try (Connection con = DriverManager.getConnection(urlBD, userBD, passwordBD);

				PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, metodo);

			ps.setInt(2, idCompra);

			ps.executeUpdate();

		}

	}

	/**
	 * Carga todos los productos disponibles invocando el procedimiento almacenado
	 * {@code VerProductos()}, que utiliza un cursor internamente.
	 * <p>
	 * El procedimiento puede devolver múltiples ResultSets (uno por producto);
	 * este método los itera todos mediante {@link CallableStatement#getMoreResults()}
	 * para construir el mapa completo de productos.
	 * </p>
	 *
	 * @param productos Mapa donde se almacenarán los productos obtenidos,
	 *                  indexados por un entero autoincremental.
	 * @throws Exception Si ocurre un error de conexión o de ejecución del procedimiento.
	 */
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

	/**
	 * Inserta un nuevo producto en la base de datos.
	 *
	 * @param producto Objeto {@link Producto} con los datos del nuevo producto
	 *                 (nombre, referencia, precio y descuento).
	 * @throws Exception Si ocurre un error de conexión, de inserción SQL
	 *                   o si la referencia ya existe (clave primaria duplicada).
	 */
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

	/**
	 * Actualiza los datos de un producto existente en la base de datos.
	 * <p>
	 * Modifica los campos: nombre, precio y descuento.
	 * La referencia ({@code REF}) se utiliza como clave de búsqueda y no se modifica.
	 * </p>
	 *
	 * @param producto Objeto {@link Producto} con los datos actualizados.
	 *                 La referencia debe estar establecida para identificar
	 *                 el producto a modificar.
	 * @throws Exception Si ocurre un error de conexión o de actualización SQL.
	 */
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

	/**
	 * Elimina un producto de la base de datos identificado por su referencia.
	 * <p>
	 * <b>Atención:</b> Si el producto está referenciado en alguna compra
	 * (tabla {@code ESTA}), la operación fallará por integridad referencial.
	 * </p>
	 *
	 * @param ref Referencia única del producto a eliminar (clave primaria).
	 * @throws Exception Si ocurre un error de conexión, de eliminación SQL
	 *                   o por restricción de integridad referencial.
	 */
	public void eliminarProducto(String ref) throws Exception {
		try (Connection con = DriverManager.getConnection(urlBD, this.userBD, this.passwordBD);
				PreparedStatement stmt = con.prepareStatement(DELETEPRODUCTO)) {

			stmt.setString(1, ref);
			stmt.executeUpdate();
		}
	}
}