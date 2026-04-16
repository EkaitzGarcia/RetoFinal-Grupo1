package clases_test;

public class Calculadora {

    public double sumar(double a, double b) {
        return a + b;
    }

    public double restar(double a, double b) {
        return a - b;
    }

    public double multiplicar(double a, double b) {
        return a * b;
    }

    public double dividir(double a, double b) {
        if (b == 0) {
            throw new ArithmeticException("No se puede dividir entre cero");
        }
        return a / b;
    }

    public boolean esPositivo(double numero) {
        return numero > 0;
    }

    public boolean esPar(int numero) {
        return numero % 2 == 0;
    }
}