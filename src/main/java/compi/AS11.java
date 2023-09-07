package compi;

public class AS11 implements AccionSemantica {
    @Override
    public void ejecutar(String buffer, char c) {
        System.out.println("AS11: " + buffer + " " + c);
    }
}
