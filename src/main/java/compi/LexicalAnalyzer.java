package compi;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class LexicalAnalyzer {
    private FileReader reader;
    private TransitionMatrix<Integer> stateMatrix;
    private TransitionMatrix<AccionSemantica> accionMatrix;
    static final int ESTADO_FINAL = 100;
    int currentChar;
    HashMap<Character, Integer> charMap = new HashMap<Character, Integer>();

    public LexicalAnalyzer(String sourceFile, TransitionMatrix<Integer> stateMatrix,
            TransitionMatrix<AccionSemantica> accionMatrix) {
        this.stateMatrix = stateMatrix;
        this.accionMatrix = accionMatrix;

        try {
            reader = new FileReader(sourceFile);
            currentChar = reader.read();
            loadCharMap();
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

                return new Token(returnToken(currentCharacter), lexema.toString());
            }

            // actualizar estado
            state = nextState;
            currentChar = reader.read();
        }
        return null;
    }

    private Boolean executeAction(int state, char character, StringBuffer lexema) {
        AccionSemantica action = accionMatrix.get(state, mapChar(character));
        return action.ejecutar(lexema, character);
    }

    private int transition(int state, int mappedChar) {
        return stateMatrix.get(state, mappedChar);
    }

    private int mapChar(char character) {
        return charMap.get(character);
    }

    private int returnToken(int mappedChar) {
        return mappedChar - 100;
    }

    private void loadCharMap() {
        charMap.put('0', 0);
        charMap.put('1', 0);
        charMap.put('2', 0);
        charMap.put('3', 0);
        charMap.put('4', 0);
        charMap.put('5', 0);
        charMap.put('6', 0);
        charMap.put('7', 0);
        charMap.put('8', 0);
        charMap.put('9', 0);
        charMap.put('.', 1);
        charMap.put('_', 2);
        charMap.put('u', 3);
        charMap.put('s', 4);
        charMap.put('i', 5);
        charMap.put('e', 6);
        charMap.put('E', 7);
        charMap.put('+', 8);
        charMap.put('-', 9);
        charMap.put('*', 10);
        charMap.put('/', 11);
        charMap.put('<', 12);
        charMap.put('>', 13);
        charMap.put('=', 14);
        charMap.put('!', 15);
        charMap.put('(', 16);
        charMap.put(')', 17);
        charMap.put('{', 18);
        charMap.put('}', 19);
        charMap.put(',', 20);
        charMap.put(';', 21);
        charMap.put('#', 22);
        charMap.put('\n', 23);
        charMap.put('\r', 23);
        charMap.put(' ', 24);
        charMap.put('\t', 25);
        charMap.put('A', 26);
        charMap.put('B', 26);
        charMap.put('C', 26);
        charMap.put('D', 26);
        charMap.put('F', 26);
        charMap.put('G', 26);
        charMap.put('H', 26);
        charMap.put('I', 26);
        charMap.put('J', 26);
        charMap.put('K', 26);
        charMap.put('L', 26);
        charMap.put('M', 26);
        charMap.put('N', 26);
        charMap.put('O', 26);
        charMap.put('P', 26);
        charMap.put('Q', 26);
        charMap.put('R', 26);
        charMap.put('S', 26);
        charMap.put('T', 26);
        charMap.put('U', 26);
        charMap.put('V', 26);
        charMap.put('W', 26);
        charMap.put('X', 26);
        charMap.put('Y', 26);
        charMap.put('Z', 26);
        charMap.put('a', 27);
        charMap.put('b', 27);
        charMap.put('c', 27);
        charMap.put('d', 27);
        charMap.put('f', 27);
        charMap.put('g', 27);
        charMap.put('h', 27);
        charMap.put('j', 27);
        charMap.put('k', 27);
        charMap.put('l', 27);
        charMap.put('m', 27);
        charMap.put('n', 27);
        charMap.put('o', 27);
        charMap.put('p', 27);
        charMap.put('q', 27);
        charMap.put('r', 27);
        charMap.put('t', 27);
        charMap.put('v', 27);
        charMap.put('w', 27);
        charMap.put('x', 27);
        charMap.put('y', 27);
        charMap.put('z', 27);
    }
}
