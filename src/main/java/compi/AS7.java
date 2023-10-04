package compi;

public class AS7 implements AccionSemantica {
    final float RANGE = 3.4028235E38f;
    SymbolTable st;

    public AS7(SymbolTable st) {
        this.st = st;
    }

    @Override
    public boolean ejecutar(StringBuffer buffer, char c) {
        float value = Float.parseFloat(buffer.toString());
        
        if (value > RANGE) {
            System.out.println("Error: el numero es demasiado grande");
            return false;
        }

        st.addEntry(buffer.toString(), 34, "float");
        return true;
    }
}
