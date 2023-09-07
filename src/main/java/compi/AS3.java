package compi;

public class AS3 implements AccionSemantica {
    @Override
    public void ejecutar(String buffer, char c) {
        System.out.println("AS3: " + buffer + " " + c);
    }
}
