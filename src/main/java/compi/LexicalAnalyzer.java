package compi;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import compi.AccionesSemanticas.AccionSemantica;

public class LexicalAnalyzer {
    private FileReader reader;
    private TransitionMatrix<Integer> stateMatrix;
    private TransitionMatrix<AccionSemantica> accionMatrix;
    private List<String> errores;
    static final int ESTADO_FINAL = 100;
    Integer TOKEN_RESERVED_WORD = 258;
    boolean isWindows = System.getProperty("os.name").startsWith("Windows");
    String eol = System.getProperty("line.separator");
    int currentChar, linea, ptrActual;

    HashMap<Character, Integer> charMap;
    HashMap<String, Integer> RESERVED_WORDS;

    static final String charmap_file = "char_map.csv";
    static final String reserved_words_file = "reserved_words.csv";


    public LexicalAnalyzer(String sourceFile, TransitionMatrix<Integer> stateMatrix,
            TransitionMatrix<AccionSemantica> accionMatrix, List<String> erroresLexicos) {
        this.stateMatrix = stateMatrix;
        this.accionMatrix = accionMatrix;
        this.errores = erroresLexicos;
        linea = 1;
        ptrActual = 0;

        try {
            reader = new FileReader(sourceFile);
            currentChar = reader.read();

            charMap = new HashMap<Character, Integer>();
            RESERVED_WORDS = new HashMap<String, Integer>();

            loadCharMap(charMap, charmap_file);
            loadReservedWords(RESERVED_WORDS, reserved_words_file);

            if (isWindows)
                charMap.put('\r', 23);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public int getLine() {
        return linea;
    }

    public Integer nextToken() throws IOException {
        StringBuffer lexema = new StringBuffer();
        int state=0, currentMappedChar;
        Integer nextState;
        Integer ptr = 0;
        AccionSemantica action;
    
        while(currentChar != -1) {
            char currentCharacter = (char) currentChar;
            currentMappedChar = mapChar(currentCharacter);
            // nuevo estado
            nextState = stateMatrix.get(state, currentMappedChar);

            if (nextState == null) {
                errores.add("(Linea " + getLine() + "):" + "Panic mode. Leyendo hasta el caracter de sincronizacion\n\tCaracter:" + currentCharacter + "\n\tLexema:" + lexema.toString());
                panicLex();
                ptrActual = -1;
                currentChar = reader.read();
                return 44;
            }
            
            if (currentMappedChar == 23){
                if (isWindows)
                    reader.read();
                linea++;
            }
            // accion semantica
            action = accionMatrix.get(state, mapChar(currentCharacter));
            if (action != null)
                ptr = action.ejecutar(lexema, currentCharacter);

            // mira que el estado sea final
            if (nextState >= ESTADO_FINAL) {
                Integer tkDetectado = returnToken(nextState, lexema);
                
                if (action == null || action.leer())
                    currentChar = reader.read();

                if (ptr > 0)
                    ptrActual = ptr;
                else if (ptr == 0)
                    ptrActual = 0;
                else {
                    String error = errores.remove(errores.size() -1);
                    errores.add("Error (Linea " + getLine() + "): " + error);
                    ptrActual = -1;
                } 

                return tkDetectado;
            }

            // actualizar estado
            state = nextState;
            currentChar = reader.read();
        }
        return 0;
    }

    private int mapChar(char character) {
        return charMap.get(character);
    }

    private int returnToken(int nextState, StringBuffer lexema) {
        if (nextState == TOKEN_RESERVED_WORD){
            try {
                return RESERVED_WORDS.get(lexema.toString());
            } catch (NullPointerException e) {
                return 256;
            }
        }
        else if (nextState < TOKEN_RESERVED_WORD-1)
            return nextState - 100;
        else
            return nextState;
    }

    public int getPtrActual() {
        return ptrActual;
    }

    public void panicLex() throws IOException {
        // read until EOL
        while (currentChar != ',' && currentChar != -1)
            currentChar = reader.read();
    }

    private Character verifySpecialChar(String character) {
        switch(character) {
            case "<":
                return '<';
            case "<EOL>":
                return '\n';
            case "<SPACE>":
                return ' ';
            case "<TAB>":
                return '\t';
            case "<COMMA>":
                return ',';
            default:
                return null;
        }
    }

    private void loadCharMap(HashMap<Character, Integer> map, String file) {
        try {
            Scanner scanner = new Scanner(new File(file));
            scanner.useDelimiter(",|" + eol);

            while (scanner.hasNext()) {
                String s = scanner.next();
                Character c;
                if (s.charAt(0) == '<')
                    c = verifySpecialChar(s);
                else
                    c = s.charAt(0);

                int mappedChar = scanner.nextInt();
                //print mappedChar and the type of the class of mappedChar
                map.put(c, mappedChar);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public String getReservedWord(int token) {
        for (String key : RESERVED_WORDS.keySet()) {
            if (RESERVED_WORDS.get(key) == token)
                return key;
        }
        return null;
    }

    private void loadReservedWords(HashMap<String, Integer> map, String file) {
        try {
            Scanner scanner = new Scanner(new File(file));
            scanner.useDelimiter(",|" + eol);

            while (scanner.hasNext()) {
                String s = scanner.next();
                int mappedChar = scanner.nextInt();
                map.put(s, mappedChar);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        LexicalAnalyzer analyzer = new LexicalAnalyzer("test.csv", null, null, null);
    }
}
