package compi;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        String sourceFile;
        String transitionMatrixFile = "transition_matrix.csv";
        String outputAsmFile = "output.asm";

        if (args.length == 0) {
            System.out.println("Uso: java Main <archivo> [<output>]");
            return;
        }
        // Si se pasan parámetros, sobrescribir los valores por defecto
        sourceFile = args[0];

        if (args.length > 1) {
            outputAsmFile = args[1];
        }

        List<String> erroresLexicos = new ArrayList<>();
        List<String> erroresSintacticos = new ArrayList<>();
        List<String> erroresSemanticos = new ArrayList<>();
        List<String> estructuras = new ArrayList<>();

        boolean success = Compiler.compile(
                sourceFile,
                transitionMatrixFile,
                outputAsmFile,
                erroresLexicos,
                erroresSintacticos,
                erroresSemanticos,
                estructuras
        );

        imprimirErrores(erroresLexicos, "Errores Lexicos");
        imprimirErrores(erroresSintacticos, "Errores Sintacticos");
        imprimirErrores(estructuras, "Estructuras Sintácticas");
        imprimirErrores(erroresSemanticos, "Errores Semánticos");

        if (success) {
            System.out.println("Compilación exitosa. Ensamblador generado en 'output.asm'.");
        } else {
            System.out.println("Compilación fallida. Revise los errores.");
        }
    }

    public static void imprimirErrores(List<String> errores, String cabecera) {
        // Imprimo los errores encontrados en el programa
        if (!errores.isEmpty()) {
            System.out.println();
            System.out.println(cabecera + ":");

            for (String error: errores) {
                System.out.println(error);
            }

        }
    }
}