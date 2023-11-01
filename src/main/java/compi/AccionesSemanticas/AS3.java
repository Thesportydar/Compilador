package compi.AccionesSemanticas;
import compi.SymbolTable;

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
        //if (ptr != 0)
            //System.out.println("existe el id: " + buffer.toString() + " en la tabla de simbolos");
        // if it doesn't exist, add it
        if (ptr == 0)
            //System.out.println("no existe el id: " + buffer.toString() + " en la tabla de simbolos");
            ptr = st.addEntry(buffer.toString(), TOKEN_ID);
            st.setAttribute(ptr, "uso", "identificador");
        return ptr;
    }

    @Override
    public Boolean leer() {
        return false;
    }
}
