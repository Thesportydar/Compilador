package compi;

public class AS2 implements AccionSemantica {
    @Override
    public boolean ejecutar(StringBuffer buffer, char c) {
        // if long < 20 append
        if (buffer.length() < 20)
            buffer.append(c);
        return true;
    }
}
