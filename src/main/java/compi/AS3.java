package compi;

public class AS3 implements AccionSemantica {
    private SymbolTable st;
    static final int TOKEN_ID = 257;

    public AS3(SymbolTable st) {
        this.st = st;
    }
    @Override
    public Integer ejecutar(StringBuffer buffer, char c) {
        // find the buffer(identifier) in the semantic table
        Integer ptr = st.getPtr(buffer.toString());
        // if it doesn't exist, add it
        if (ptr == 0)
            ptr = st.addEntry(buffer.toString(), TOKEN_ID, "Identificador");

        return ptr;
    }

    @Override
    public Boolean leer() {
        return false;
    }
}
