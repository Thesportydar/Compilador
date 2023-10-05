package compi;

public class AS1 implements AccionSemantica {
    @Override
    public Integer ejecutar(StringBuffer lexema, char c) {
        lexema.append(c);
        return 0;
    }
    @Override
    public Boolean leer() {
        return true;
    }
}
