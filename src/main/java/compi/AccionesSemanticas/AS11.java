package compi.AccionesSemanticas;
import compi.SymbolTable;

public class AS11 implements AccionSemantica {
    private SymbolTable st;
    static final int TOKEN_STRING_1LN = 277;

    public AS11(SymbolTable st) {
        this.st = st;
    }

    @Override
    public Integer ejecutar(StringBuffer buffer, char c) {
        Integer ptr = st.addEntry(buffer.toString(), TOKEN_STRING_1LN);
        st.setAttribute(ptr, "tipo", "string_1_ln");
        return ptr;
    }

    @Override
    public Boolean leer() {
        return true;
    }
}
