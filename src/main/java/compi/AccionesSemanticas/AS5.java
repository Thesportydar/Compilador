package compi.AccionesSemanticas;
import compi.SymbolTable;
import java.util.List;

public class AS5 implements AccionSemantica {
    private SymbolTable st;
    final int RANGE;
    final String DESC;
    final int ID_TOKEN;
    private List<String> errores;

    public AS5(SymbolTable st, int range, String desc, int id, List<String> errores_lexicos) {
        this.st = st;
        this.RANGE = range;
        this.DESC = desc;
        this.ID_TOKEN = id;
        this.errores = errores_lexicos;
    }

    @Override
    public Integer ejecutar(StringBuffer buffer, char c) {
        // buffer is a short int. check range
        int value = Integer.parseInt(buffer.toString());
        if (value > RANGE) {
            errores.add("Error: " + DESC + " out of range (" + RANGE + ")");
            return -1;
        }
        return st.addEntry(buffer.toString(), ID_TOKEN, DESC);
    }

    @Override
    public Boolean leer() {
        return true;
    }
}
