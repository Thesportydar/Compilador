package compi;

import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

public class LexicalAnalyzer {
    private FileReader reader;
    private TransitionMatrix<Integer> stateMatrix;
    private TransitionMatrix<AccionSemantica> accionMatrix;
    static final int ESTADO_FINAL = 100;
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
            charMap.put('\r', 23);
            loadReservedWords(RESERVED_WORDS, reserved_words_file);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public Token nextToken() throws IOException {
        StringBuffer lexema = new StringBuffer();
        int state=0, nextState, currentMappedChar;
        Boolean leer = true;
    
        while(currentChar != -1) {
            char currentCharacter = (char) currentChar;
            currentMappedChar = mapChar(currentCharacter);

            // nuevo estado
            nextState = transition(state, currentMappedChar);

            // accion semantica
            leer = executeAction(state, currentCharacter, lexema);

            // mira que el estado sea final
            if (nextState >= ESTADO_FINAL) {
                if (leer)
                    currentChar = reader.read();

                return new Token(returnToken(nextState, lexema), lexema.toString());
            }

            // actualizar estado
            state = nextState;
            currentChar = reader.read();
        }
        return null;
    }

    private Boolean executeAction(int state, char character, StringBuffer lexema) {
        AccionSemantica action = accionMatrix.get(state, mapChar(character));

        if (action == null)
            return true;

        return action.ejecutar(lexema, character);
    }

    private int transition(int state, int mappedChar) {
        return stateMatrix.get(state, mappedChar);
    }

    private int mapChar(char character) {
        return charMap.get(character);
    }

    private int returnToken(int nextState, StringBuffer lexema) {
        if (nextState == 121)
            return RESERVED_WORDS.get(lexema.toString());
        else
            return nextState - 100;
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
