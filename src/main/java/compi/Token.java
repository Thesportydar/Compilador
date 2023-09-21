package compi;

public class Token {
    int id;
    String lexema;

    public Token(int id, String lexema) {
        this.id = id;
        this.lexema = lexema;
    }

    public int getId() {
        return id;
    }

    public String getLexema() {
        return lexema;
    }
}
