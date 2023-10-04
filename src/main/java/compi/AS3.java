package compi;

public class AS3 implements AccionSemantica {
    private SymbolTable st;
    public AS3(SymbolTable st) {
        this.st = st;
    }
    @Override
    public boolean ejecutar(StringBuffer buffer, char c) {
        // find the buffer(identifier) in the semantic table
        if (!st.contains(buffer.toString())) {
            // if it doesn't exist, add it
            st.addEntry(buffer.toString(), 0, "Identificador");
        }
        return false;
    }
}
