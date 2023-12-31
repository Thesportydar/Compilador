package compi.AccionesSemanticas;
import compi.SymbolTable;
import java.util.List;

public class AS7 implements AccionSemantica {
    final float RANGE = 3.40282347E+38f;
    static final int TOKEN_CTE_FLOAT = 271;
    static final int TOKEN_FLOAT = 268;
    private List<String> errores;
    SymbolTable st;

    public AS7(SymbolTable st, List<String> errores_lexicos) {
        this.st = st;
        this.errores = errores_lexicos;
    }

    @Override
    public Integer ejecutar(StringBuffer buffer, char c) {
        Float value;
        try {
            value = Float.parseFloat(buffer.toString());
        } catch (Exception e) {
            errores.add("Error: float invalido " + buffer.toString());
            return -1;
        }
        if (value > RANGE) {
            errores.add("Error: float es demasiado grande " + buffer.toString());
            return -1;
        }
        Integer ptr = st.addEntry(buffer.toString(), TOKEN_CTE_FLOAT);
        st.setAttribute(ptr, "tipo", ""+TOKEN_FLOAT);
        st.setAttribute(ptr, "uso", "cte");
        st.setAttribute(ptr, "valid","1");
        return ptr;
    }

    @Override
    public Boolean leer() {
        return false;
    }
}
