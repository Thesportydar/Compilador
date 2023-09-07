package compi;

public class AS1 implements AccionSemantica {
    @Override
    public void ejecutar(String buffer, char c) {
        System.out.println("AS1: " + buffer + " " + c);
    }
}
