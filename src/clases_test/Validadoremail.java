package clases_test;

public class Validadoremail {

    private static final int LONGITUD_MAXIMA = 50;

    public boolean esValido(String email) {
        if (email == null) {
            throw new IllegalArgumentException("El email no puede ser nulo");
        }
        if (email.isBlank()) {
            throw new IllegalArgumentException("El email no puede estar vacío");
        }
        if (email.length() > LONGITUD_MAXIMA) {
            throw new IllegalArgumentException(
                "El email supera la longitud máxima de " + LONGITUD_MAXIMA + " caracteres"
            );
        }
        return email.matches("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$");
    }

    public boolean tieneDominioCorporativo(String email) {
        if (email == null || !esValido(email)) {
            return false;
        }
        String dominio = email.substring(email.indexOf('@') + 1);
        return dominio.equalsIgnoreCase("empresa.com") ||
               dominio.equalsIgnoreCase("corporativo.es");
    }

    public String extraerDominio(String email) {
        if (email == null || !email.contains("@")) {
            return null;
        }
        return email.substring(email.indexOf('@') + 1);
    }
}