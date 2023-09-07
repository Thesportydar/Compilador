package compi;

public class AS0 implements AccionSemantica {
    public AS0() {
    }
    @Override
    public void ejecutar(String buffer, char c) {
        System.out.println("AS0: " + buffer + " " + c);
    }
}
