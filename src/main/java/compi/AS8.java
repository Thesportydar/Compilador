package compi;

public class AS8 implements AccionSemantica {
    @Override
    public void ejecutar(String buffer, char c) {
        System.out.println("AS8: " + buffer + " " + c);
    }
}
