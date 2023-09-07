package compi;

public class AS7 implements AccionSemantica {
    @Override
    public void ejecutar(String buffer, char c) {
        System.out.println("AS7: " + buffer + " " + c);
    }
}
