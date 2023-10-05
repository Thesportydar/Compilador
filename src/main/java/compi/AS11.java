package compi;

public class AS11 implements AccionSemantica {
    private SymbolTable st;
    static final int TOKEN_STRING_1LN = 277;

    public AS11(SymbolTable st) {
        this.st = st;
    }

    @Override
    public Integer ejecutar(StringBuffer buffer, char c) {
        return st.addEntry(buffer.toString(), TOKEN_STRING_1LN, "string de 1 linea");
    }

    @Override
    public Boolean leer() {
        return false;
    }
}
