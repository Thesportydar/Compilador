package compi;

public class AS11 implements AccionSemantica {
    private SymbolTable st;
    public AS11(SymbolTable st) {
        this.st = st;
    }

    @Override
    public boolean ejecutar(StringBuffer buffer, char c) {
        st.addEntry(buffer.toString(), 20, "string de 1 linea");
        return true;
    }
}
