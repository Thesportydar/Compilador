package compi.AccionesSemanticas;

import java.util.List;

public class AS9 implements AccionSemantica {
    // dictionary with reserved words
    private List<String> errores;
    static final String[] reservedWords = {
        "IF", "ELSE", "END_IF", "PRINT", "CLASS", "VOID", "WHILE", "DO", "SHORT", "UINT", "FLOAT"
    };

    public AS9(List<String> errores_lexicos){
        this.errores = errores_lexicos;
    }
    @Override
    public Integer ejecutar(StringBuffer buffer, char c) {
        String word = buffer.toString();
        for (String reservedWord : reservedWords)
            if (word.equals(reservedWord))
                return 0;

        errores.add("Palabra reservada no reconocida: "+ word);
        return -1;
    }

    @Override
    public Boolean leer() {
        return false;
    }
}
