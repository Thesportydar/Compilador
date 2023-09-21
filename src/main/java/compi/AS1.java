package compi;

public class AS1 implements AccionSemantica {
    @Override
    public boolean ejecutar(StringBuffer lexema, char c) {
        lexema.append(c);
        return true;
    }
}
