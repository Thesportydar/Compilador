package compi;

public class AS2 implements AccionSemantica {
    @Override
    public void ejecutar(String buffer, char c) {
        System.out.println("AS2: " + buffer + " " + c);
    }
}
