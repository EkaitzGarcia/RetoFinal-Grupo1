package utiles;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

import modelo.Cliente;
import modelo.Producto;
import modelo.Trabajador;

public class convertiro_exportador_de_xml {

	private String esc(String v) {
		if (v == null)
			return "";
		return v.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;").replace("'",
				"&apos;");
	}

	private String ind(int n) {
		return "    ".repeat(n);
	}

	private BufferedWriter abrir(String ruta) throws Exception {
		return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(ruta), StandardCharsets.UTF_8));
	}

	public void exportarTodo(List<Cliente> clientes, List<Producto> productos, List<Trabajador> trabajadores,
			List<Object[]> compras, String ruta) throws Exception {

		try (BufferedWriter bw = abrir(ruta)) {

			bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			bw.newLine();
			bw.write("<Okapi>");
			bw.newLine();

			bw.write(ind(1) + "<Clientes>");
			bw.newLine();
			for (Cliente c : clientes) {
				bw.write(ind(2) + "<Cliente dni=\"" + esc(c.getDni()) + "\">");
				bw.newLine();
				bw.write(ind(3) + "<Nombre>" + esc(c.getNom()) + "</Nombre>");
				bw.newLine();
				bw.write(ind(3) + "<Apellido>" + esc(c.getApellido()) + "</Apellido>");
				bw.newLine();
				bw.write(ind(3) + "<Telefono>" + esc(c.getTelefono()) + "</Telefono>");
				bw.newLine();
				bw.write(ind(3) + "<Correo>" + esc(c.getCorreo()) + "</Correo>");
				bw.newLine();
				bw.write(ind(3) + "<Direccion>" + esc(c.getDireccion()) + "</Direccion>");
				bw.newLine();
				bw.write(ind(2) + "</Cliente>");
				bw.newLine();
			}
			bw.write(ind(1) + "</Clientes>");
			bw.newLine();

			bw.write(ind(1) + "<Productos>");
			bw.newLine();
			for (Producto p : productos) {
				bw.write(ind(2) + "<Producto ref=\"" + esc(p.getRef_producto()) + "\">");
				bw.newLine();
				bw.write(ind(3) + "<Precio>" + p.getPrecio() + "</Precio>");
				bw.newLine();
				bw.write(ind(3) + "<Descuento>" + p.getDescuento() + "</Descuento>");
				bw.newLine();
				bw.write(ind(2) + "</Producto>");
				bw.newLine();
			}
			bw.write(ind(1) + "</Productos>");
			bw.newLine();

			bw.write(ind(1) + "<Trabajadores>");
			bw.newLine();
			for (Trabajador t : trabajadores) {
				bw.write(ind(2) + "<Trabajador nss=\"" + t.getNss() + "\">");
				bw.newLine();
				bw.write(ind(3) + "<Nombre>" + esc(t.getNomTrabajador()) + "</Nombre>");
				bw.newLine();
				bw.write(ind(3) + "<Apellido>" + esc(t.getApeTrabajador()) + "</Apellido>");
				bw.newLine();
				bw.write(ind(3) + "<Telefono>" + esc(t.getTlfnTrabajador()) + "</Telefono>");
				bw.newLine();
				bw.write(ind(3) + "<Correo>" + esc(t.getCorreoTrabajador()) + "</Correo>");
				bw.newLine();
				bw.write(ind(2) + "</Trabajador>");
				bw.newLine();
			}
			bw.write(ind(1) + "</Trabajadores>");
			bw.newLine();

			bw.write(ind(1) + "<Compras>");
			bw.newLine();
			for (Object[] f : compras) {
				int id = (int) f[0];
				String fecha = (String) f[1];
				float total = (float) f[2];
				String metodo = (String) f[3];
				String dni = (String) f[4];
				String nombre = (String) f[5];
				String apellido = (String) f[6];

				bw.write(ind(2) + "<Compra id=\"" + id + "\">");
				bw.newLine();
				bw.write(ind(3) + "<Fecha>" + esc(fecha) + "</Fecha>");
				bw.newLine();
				bw.write(ind(3) + "<Total>" + total + "</Total>");
				bw.newLine();
				bw.write(ind(3) + "<MetodoPago>" + esc(metodo) + "</MetodoPago>");
				bw.newLine();
				bw.write(ind(3) + "<Cliente dni=\"" + esc(dni) + "\">");
				bw.newLine();
				bw.write(ind(4) + "<Nombre>" + esc(nombre) + "</Nombre>");
				bw.newLine();
				bw.write(ind(4) + "<Apellido>" + esc(apellido) + "</Apellido>");
				bw.newLine();
				bw.write(ind(3) + "</Cliente>");
				bw.newLine();
				bw.write(ind(2) + "</Compra>");
				bw.newLine();
			}
			bw.write(ind(1) + "</Compras>");
			bw.newLine();

			bw.write("</Okapi>");
			bw.newLine();
		}
	}
}