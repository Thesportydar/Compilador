package compi.AccionesSemanticas;

public class AS2 implements AccionSemantica {
    @Override
    public Integer ejecutar(StringBuffer buffer, char c) {
        // if long < 20 append
        if (buffer.length() < 20)
            buffer.append(c);
        return 0;
    }

    @Override
    public Boolean leer() {
        return true;
    }
}
