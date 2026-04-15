package excepciones;

import javax.swing.JOptionPane;

/**
 * Excepción personalizada para manejar errores relacionados con el DNI.
 * Esta excepción puede ser lanzada cuando se detecta un problema con un DNI,
 * como formato incorrecto o duplicado.
 */
public class DniException extends Exception {
	private String msg;
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * Constructor que permite pasar un mensaje de error.
     * 
     * @param msg El mensaje que describe el error
     */
    public DniException(String msg) {
        this.msg = msg;
    }
    
    public void visualizarMsg() {
        JOptionPane.showMessageDialog(null, this.msg, "ERROR", JOptionPane.ERROR_MESSAGE);
    }
}