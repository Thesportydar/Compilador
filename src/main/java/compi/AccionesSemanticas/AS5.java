package compi.AccionesSemanticas;
import compi.SymbolTable;
import java.util.List;

public class AS5 implements AccionSemantica {
    private SymbolTable st;
    final int RANGE;
    final String DESC;
    final int ID_TOKEN, ID_TIPO;
    private List<String> errores;

    public AS5(SymbolTable st, int range, String desc, int id, int id_tipo, List<String> errores_lexicos) {
        this.st = st;
        this.RANGE = range;
        this.DESC = desc;
        this.ID_TOKEN = id;
        this.ID_TIPO = id_tipo;
        this.errores = errores_lexicos;
    }

    @Override
    public Integer ejecutar(StringBuffer buffer, char c) {
        Long value;
        try {
             value = Long.parseLong(buffer.toString());
        } catch (Exception e) {
            errores.add("Error: " + DESC + " invalid (" + buffer.toString() + ")");
            return -1;
        }
        if (value > RANGE) {
            errores.add("Error: " + DESC + " out of range (" + buffer.toString() + ")");
            return -1;
        }
        Integer ptr =  st.addEntry(buffer.toString(), ID_TOKEN);
        st.setAttribute(ptr, "tipo", ""+ID_TIPO);
        st.setAttribute(ptr, "uso", "cte");
        st.setAttribute(ptr, "valid","1");
        return ptr;
    }

    @Override
    public Boolean leer() {
        return true;
    }
}
