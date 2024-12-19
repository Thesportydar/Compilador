package compi;

import compi.AccionesSemanticas.AccionSemantica;
import compi.AssemblyGenerator.AssemblyGenerator;
import compi.Parser.Parser;

import java.util.List;

public class Compiler {
    public static boolean compile(
            String sourceFile,
            String transitionMatrixFile,
            String outputAsmFile,
            List<String> erroresLexicos,
            List<String> erroresSintacticos,
            List<String> erroresSemanticos,
            List<String> estructuras) {
        try {
            // configuracion inicial
            TransitionMatrix<Integer> mI = new TransitionMatrix<>(19, 28);
            TransitionMatrix<AccionSemantica> mA = new TransitionMatrix<>(19, 28);
            SymbolTable symbolTable = new SymbolTable();

            // cargar la matriz de transicion
            FuncionesAuxiliares.loadMatrixs(mI, mA, transitionMatrixFile, symbolTable, erroresLexicos);

            LexicalAnalyzer lexer = new LexicalAnalyzer(sourceFile, mI, mA, erroresLexicos);
            Parser parser = new Parser(lexer, symbolTable, erroresSintacticos, erroresSemanticos, estructuras);

            parser.exec();

            if (!erroresLexicos.isEmpty() || !erroresSintacticos.isEmpty() || !erroresSemanticos.isEmpty()) {
                return false; // Hay errores, no se genera ensamblador
            }

            // Generar código ensamblador
            AssemblyGenerator asm = new AssemblyGenerator(parser.getPilaTercetos(), parser.getSymbolTable(), outputAsmFile);
            asm.generarAssembler();
            return true;
        } catch (Exception e) {
            System.err.println("Error durante la compilación: " + e.getMessage());
            return false;
        }
    }
}