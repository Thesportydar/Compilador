package compi;

import java.io.*;
import java.util.Scanner;

public class Main {
    private static AccionSemantica getAccionSemantica(String s) {
        // en un futuro estaria bueno implementarlo con reflexion
        switch (s) {
            case "asm0":
                return new AS0();
            case "asm1":
                return new AS1();
            case "asm2":
                return new AS2();
            case "asm3":
                return new AS3();
            case "asm4":
                return new AS4();
            case "asm5":
                return new AS5();
            case "asm6":
                return new AS6();
            case "asm7":
                return new AS7();
            case "asm8":
                return new AS8();
            case "asm9":
                return new AS9();
            case "asm10":
                return new AS10();
            case "asm11":
                return new AS11();
            default:
                return null;
        }
    }

    private static void loadMatrixs(TransitionMatrix<Integer> mI, TransitionMatrix<AccionSemantica> mA, String filename) {
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
                                mA.set(row, column, getAccionSemantica(values[1]));
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

    public static void main(String[] args) {
        TransitionMatrix<Integer> mI = new TransitionMatrix<>(19, 28);
        TransitionMatrix<AccionSemantica> mA = new TransitionMatrix<>(19, 28);
        loadMatrixs(mI, mA, "test.csv");

        System.out.println(mI);

        //for each one xall ejecutar
        for (int row = 0; row < mI.getRows(); row++)
            for (int column = 0; column < mI.getColumns(); column++)
                if (mA.get(row, column) != null)
                    mA.get(row, column).ejecutar("AS"+column, 'c');
    }
}
