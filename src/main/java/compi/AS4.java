package compi;

public class AS4 implements AccionSemantica {
    @Override
    public void ejecutar(String buffer, char c) {
        System.out.println("AS4: " + buffer + " " + c);
    }
}
