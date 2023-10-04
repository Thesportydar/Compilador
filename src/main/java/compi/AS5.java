package compi;

public class AS5 implements AccionSemantica {
    private SymbolTable st;
    final int RANGE;
    final String DESC;
    final int ID;

    public AS5(SymbolTable st, int range, String desc, int id) {
        this.st = st;
        this.RANGE = range;
        this.DESC = desc;
        this.ID = id;
    }

    @Override
    public boolean ejecutar(StringBuffer buffer, char c) {
        // buffer is a short int. check range
        System.out.println("AS5: " + buffer.toString());
        int value = Integer.parseInt(buffer.toString());
        if (value > RANGE) {
            System.out.println("Error: " + DESC + " out of range (" + RANGE + ")");
            return true;
        }
        st.addEntry(buffer.toString(), ID, DESC);
        return true;
    }
}
