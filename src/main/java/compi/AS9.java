package compi;

public class AS9 implements AccionSemantica {
    // dictionary with reserved words
    static final String[] reservedWords = {
        "IF", "ELSE", "END_IF", "PRINT", "CLASS", "VOID", "WHILE", "DO", "SHORT", "UINT", "FLOAT"
    };
    @Override
    public boolean ejecutar(StringBuffer buffer, char c) {
        String word = buffer.toString();
        for (String reservedWord : reservedWords)
            if (word.equals(reservedWord))
                return false;

        System.out.println("Palabra reservada no reconocida: " + word);
        return false;
    }
}
