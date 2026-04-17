package excepciones;
import javax.swing.JOptionPane;

public class FormatoIncorrectoException extends Exception {

    private static final long serialVersionUID = 1L;

    public FormatoIncorrectoException(String mensaje) {
        super(mensaje);
    }

    public void visualizarMsg() {
        JOptionPane.showMessageDialog(null, getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
    }
}
