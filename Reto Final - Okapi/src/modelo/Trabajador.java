
package modelo;

public class Trabajador {
	
	private int nss;
	private String nomTrabajador;
	private String apeTrabajador;
	private String tlfnTrabajador;
	private String correoTrabajador;
	public Trabajador(int nss, String nomTrabajador, String apeTrabajador, String tlfnTrabajador,
			String correoTrabajador) {
		super();
		this.nss = nss;
		this.nomTrabajador = nomTrabajador;
		this.apeTrabajador = apeTrabajador;
		this.tlfnTrabajador = tlfnTrabajador;
		this.correoTrabajador = correoTrabajador;
	}
	public int getNss() {
		return nss;
	}
	public void setNss(int nss) {
		this.nss = nss;
	}
	public String getNomTrabajador() {
		return nomTrabajador;
	}
	public void setNomTrabajador(String nomTrabajador) {
		this.nomTrabajador = nomTrabajador;
	}
	public String getApeTrabajador() {
		return apeTrabajador;
	}
	public void setApeTrabajador(String apeTrabajador) {
		this.apeTrabajador = apeTrabajador;
	}
	public String getTlfnTrabajador() {
		return tlfnTrabajador;
	}
	public void setTlfnTrabajador(String tlfnTrabajador) {
		this.tlfnTrabajador = tlfnTrabajador;
	}
	public String getCorreoTrabajador() {
		return correoTrabajador;
	}
	public void setCorreoTrabajador(String correoTrabajador) {
		this.correoTrabajador = correoTrabajador;
	}
	@Override
	public String toString() {
		return "Trabajador [nss=" + nss + ", nomTrabajador=" + nomTrabajador + ", apeTrabajador=" + apeTrabajador
				+ ", tlfnTrabajador=" + tlfnTrabajador + ", correoTrabajador=" + correoTrabajador + "]";
	}
	
	

}
