package compi;

public class AS5 implements AccionSemantica {
    @Override
    public void ejecutar(String buffer, char c) {
        System.out.println("AS5: " + buffer + " " + c);
    }
}
