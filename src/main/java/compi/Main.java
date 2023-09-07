package compi;

import java.io.*;
import java.util.Scanner;

public class Main {
    private static AccionSemantica getAccionSemantica(String s) {
        // en un futuro estaria bueno implementarlo con reflexion
        switch (s) {
            case "as0":
                return new AS0();
            case "as1":
                return new AS1();
            case "as2":
                return new AS2();
            case "as3":
                return new AS3();
            case "as4":
                return new AS4();
            case "as5":
                return new AS5();
            case "as6":
                return new AS6();
            case "as7":
                return new AS7();
            case "as8":
                return new AS8();
            case "as9":
                return new AS9();
            default:
                return null;
        }
    }

    private static void loadMatrixs(TransitionMatrix<Integer> mI, TransitionMatrix<AccionSemantica> mA, String filename) {
        try {
            Scanner scanner = new Scanner(new File(filename));
            scanner.useDelimiter(",|\n");

            for (int row = 0; row < mI.getRows(); row++)
                for (int column = 0; column < mI.getColumns(); column++)
                {
                    String value = scanner.next();
                    String[] values = value.split("\\|");
                    mI.set(row, column, Integer.parseInt(values[0]));

                    try {
                        mA.set(row, column, getAccionSemantica(values[1]));
                    } catch (ArrayIndexOutOfBoundsException e) {
                        throw new ArrayIndexOutOfBoundsException("Error en la matriz de acciones semanticas: " + e.getMessage());
                    }

                }

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
        }
    }

    public static void main(String[] args) {
        TransitionMatrix<Integer> mI = new TransitionMatrix<>(3, 3);
        TransitionMatrix<AccionSemantica> mA = new TransitionMatrix<>(3, 3);
        loadMatrixs(mI, mA, "test.csv");

        System.out.println(mI);

        //for each one xall ejecutar
        for (int row = 0; row < mI.getRows(); row++)
            for (int column = 0; column < mI.getColumns(); column++)
                mA.get(row, column).ejecutar("AS"+column, 'c');
    }
}
