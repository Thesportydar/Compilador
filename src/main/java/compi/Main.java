package compi;

import java.io.*;
import java.util.Scanner;
import compi.AccionesSemanticas.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static AccionSemantica getAccionSemantica(String s, SymbolTable st, List<String> erroresLexicos) {
        // en un futuro estaria bueno implementarlo con reflexion
        switch (s) {
            case "asm1":
                return new AS1();
            case "asm2":
                return new AS2();
            case "asm3":
                return new AS3(st);
            case "asm4":
                return new AS1();
            case "asm5":
                return new AS5(st, 32767, "shortint", 269, erroresLexicos);
            case "asm6":
                return new AS5(st, 65535, "uint", 270, erroresLexicos);
            case "asm7":
                return new AS7(st, erroresLexicos);
            case "asm8":
                return new AS8();
            case "asm9":
                return new AS9(erroresLexicos);
            case "asm11":
                return new AS11(st);
            default:
                return null;
        }
    }

    public static void loadMatrixs(TransitionMatrix<Integer> mI, TransitionMatrix<AccionSemantica> mA, String filename, SymbolTable st, List<String> erroresLexicos) {
        try {
            Scanner scanner = new Scanner(new File(filename));
            scanner.useDelimiter(";");

            for (int row = 0; row < mI.getRows(); row++)
                for (int column = 0; column < mI.getColumns(); column++)
                {
                    String value = scanner.next();
                    
                    if (!value.equals("null")) {
                        String[] values = value.split("\\|");
                        mI.set(row, column, Integer.parseInt(values[0]));

                        if (values.length > 1) {
                            try {
                                mA.set(row, column, getAccionSemantica(values[1], st, erroresLexicos));
                            } catch (ArrayIndexOutOfBoundsException e) {
                                throw new ArrayIndexOutOfBoundsException("Error en la matriz de acciones semanticas: " + e.getMessage());
                            }
                        }
                    }
                }

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
        }
    }

    public static void parser(TransitionMatrix<Integer> mI, TransitionMatrix<AccionSemantica> mA, String filename, SymbolTable st, List<String> erroresLexicos) {
        LexicalAnalyzer la = new LexicalAnalyzer(filename, mI, mA, erroresLexicos);
        Integer token = 0;

        do {
            try {
                token = la.nextToken();
                System.out.println("+-" + token + "-+");
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (token != 0);
    }

    public static void main(String[] args) {
        TransitionMatrix<Integer> mI = new TransitionMatrix<>(19, 28);
        TransitionMatrix<AccionSemantica> mA = new TransitionMatrix<>(19, 28);
        SymbolTable st = new SymbolTable();
        List<String> erroresLexicos = new ArrayList<>();
        loadMatrixs(mI, mA, "test.csv", st, erroresLexicos);

        parser(mI, mA, "test.txt", st, erroresLexicos);

        st.print();
    }
}
