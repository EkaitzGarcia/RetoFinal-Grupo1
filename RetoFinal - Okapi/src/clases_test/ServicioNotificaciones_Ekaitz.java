package clases_test;

public class ServicioNotificaciones_Ekaitz {

    private final EmailSender emailSender;

    public ServicioNotificaciones_Ekaitz(EmailSender emailSender) {
        if (emailSender == null) {
            throw new IllegalArgumentException("EmailSender no puede ser nulo");
        }
        this.emailSender = emailSender;
    }

    public boolean notificarBienvenida(String destinatario) {
        if (destinatario == null || destinatario.isBlank()) {
            throw new IllegalArgumentException("El destinatario no puede ser nulo o vacío");
        }
        return emailSender.enviar(destinatario, "¡Bienvenido!", "Gracias por unirte.");
    }

    public boolean notificarAlerta(String destinatario, String mensaje) {
        if (destinatario == null || mensaje == null) {
            throw new IllegalArgumentException("Destinatario y mensaje son obligatorios");
        }
        return emailSender.enviar(destinatario, "ALERTA", mensaje);
    }

    public String obtenerResumenEnvios(String destinatario) {
        int total = emailSender.contarEnviadosA(destinatario);
        if (total == 0) {
            return null; // Sin historial de envíos
        }
        return "Total de emails enviados a " + destinatario + ": " + total;
    }

    public boolean tieneHistorialDeEnvios(String destinatario) {
        return emailSender.contarEnviadosA(destinatario) > 0;
    }
}