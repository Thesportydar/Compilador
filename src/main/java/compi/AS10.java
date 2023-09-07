package compi;

public class AS10 implements AccionSemantica {
    @Override
    public void ejecutar(String buffer, char c) {
        System.out.println("AS10: " + buffer + " " + c);
    }
}
