package compi;

public class AS6 implements AccionSemantica {
    @Override
    public void ejecutar(String buffer, char c) {
        System.out.println("AS6: " + buffer + " " + c);
    }
}
