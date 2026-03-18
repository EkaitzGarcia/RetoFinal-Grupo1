package modelo;

public class Cliente {
	
	private String dni;
	private String nom;
	private String apellido;
	private String telefono;
	private String correo;
	private String direccion;
	public Cliente(String dni, String nom, String apellido, String telefono, String correo, String direccion) {
		
		this.dni = dni;
		this.nom = nom;
		this.apellido = apellido;
		this.telefono = telefono;
		this.correo = correo;
		this.direccion = direccion;
	}
	public String getDni() {
		return dni;
	}
	public void setDni(String dni) {
		this.dni = dni;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getCorreo() {
		return correo;
	}
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	@Override
	public String toString() {
		return "Cliente [dni=" + dni + ", nom=" + nom + ", apellido=" + apellido + ", telefono=" + telefono
				+ ", correo=" + correo + ", direccion=" + direccion + "]";
	}
	
	

}
