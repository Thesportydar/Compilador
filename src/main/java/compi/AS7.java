package compi;

public class AS7 implements AccionSemantica {
    final float RANGE = 3.4028235E38f;
    static final int TOKEN_CTE_FLOAT = 271;
    SymbolTable st;

    public AS7(SymbolTable st) {
        this.st = st;
    }

    @Override
    public Integer ejecutar(StringBuffer buffer, char c) {
        float value = Float.parseFloat(buffer.toString());
        
        if (value > RANGE) {
            System.out.println("Error: el numero es demasiado grande");
            return -1;
        }

        return st.addEntry(buffer.toString(), TOKEN_CTE_FLOAT, "float");
    }

    @Override
    public Boolean leer() {
        return false;
    }
}
