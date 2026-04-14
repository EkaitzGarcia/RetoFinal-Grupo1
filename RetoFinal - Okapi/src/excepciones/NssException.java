package excepciones;

import javax.swing.JOptionPane;

public class NssException extends Exception {

    private static final long serialVersionUID = 1L;

    public NssException(String mensaje) {
        super(mensaje);
    }

    public void visualizarMsg() {
        JOptionPane.showMessageDialog(null, getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
    }
}
