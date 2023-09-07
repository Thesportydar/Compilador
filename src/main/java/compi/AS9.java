package compi;

public class AS9 implements AccionSemantica {
    @Override
    public void ejecutar(String buffer, char c) {
        System.out.println("AS9: " + buffer + " " + c);
    }
}
