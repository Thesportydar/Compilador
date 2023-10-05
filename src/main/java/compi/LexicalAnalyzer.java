package compi;

import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

public class LexicalAnalyzer {
    private FileReader reader;
    private TransitionMatrix<Integer> stateMatrix;
    private TransitionMatrix<AccionSemantica> accionMatrix;
    static final int ESTADO_FINAL = 100;
    Integer TOKEN_RESERVED_WORD = 258;
    int currentChar;

    HashMap<Character, Integer> charMap;
    HashMap<String, Integer> RESERVED_WORDS;

    static final String charmap_file = "char_map.csv";
    static final String reserved_words_file = "reserved_words.csv";


    public LexicalAnalyzer(String sourceFile, TransitionMatrix<Integer> stateMatrix,
            TransitionMatrix<AccionSemantica> accionMatrix) {
        this.stateMatrix = stateMatrix;
        this.accionMatrix = accionMatrix;

        try {
            reader = new FileReader(sourceFile);
            currentChar = reader.read();

            charMap = new HashMap<Character, Integer>();
            RESERVED_WORDS = new HashMap<String, Integer>();

            loadCharMap(charMap, charmap_file);
            loadReservedWords(RESERVED_WORDS, reserved_words_file);

            //charMap.put('\r', 23);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public Integer nextToken() throws IOException {
        StringBuffer lexema = new StringBuffer();
        int state=0, nextState, currentMappedChar;
        Boolean leer = true;
        Integer ptr;
        AccionSemantica action;
    
        while(currentChar != -1) {
            char currentCharacter = (char) currentChar;
            currentMappedChar = mapChar(currentCharacter);

            // nuevo estado
            nextState = stateMatrix.get(state, currentMappedChar);

            // accion semantica
            action = accionMatrix.get(state, mapChar(currentCharacter));
            if (action != null)
                ptr = action.ejecutar(lexema, currentCharacter);

            // mira que el estado sea final
            if (nextState >= ESTADO_FINAL) {
                if (action == null || action.leer())
                    currentChar = reader.read();

                return returnToken(nextState, lexema);
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
        if (nextState == TOKEN_RESERVED_WORD)
            return RESERVED_WORDS.get(lexema.toString());
        else if (nextState < TOKEN_RESERVED_WORD-1)
            return nextState - 100;
        else
            return nextState;
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
            scanner.useDelimiter(",|\n|\r");

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

    private void loadReservedWords(HashMap<String, Integer> map, String file) {
        try {
            Scanner scanner = new Scanner(new File(file));
            scanner.useDelimiter(",|\n|\r");

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
        LexicalAnalyzer analyzer = new LexicalAnalyzer("test.csv", null, null);
    }
}
