import compi.Compiler;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static compi.Main.imprimirErrores;
import static org.junit.Assert.assertTrue;

public class CompilerTest {
    private List<String> erroresLexicos;
    private List<String> erroresSintacticos;
    private List<String> erroresSemanticos;
    private List<String> estructuras;

    @Before
    public void setUp() {
        // Inicializar listas compartidas para los tests
        erroresLexicos = new ArrayList<>();
        erroresSintacticos = new ArrayList<>();
        erroresSemanticos = new ArrayList<>();
        estructuras = new ArrayList<>();
    }

    @Test
    public void testControl() {
        boolean success = Compiler.compile(
                "prueba_control.txt",
                "transition_matrix.csv",
                "output.asm",
                erroresLexicos,
                erroresSintacticos,
                erroresSemanticos,
                estructuras
        );
        assertTrue("La compilación debería ser exitosa", success);
        assertTrue("No debería haber errores léxicos", erroresLexicos.isEmpty());
        assertTrue("No debería haber errores sintácticos", erroresSintacticos.isEmpty());
        assertTrue("No debería haber errores semánticos", erroresSemanticos.isEmpty());
    }

    @Test
    public void incompatibleTypes() {
        // Ejecutar el compilador con un caso de prueba distinto
        boolean success = Compiler.compile(
                "tipos_incompatibles.txt",
                "transition_matrix.csv",
                "output.asm",
                erroresLexicos,
                erroresSintacticos,
                erroresSemanticos,
                estructuras
        );

        // Verificar que se detectaron errores semánticos
        imprimirErrores(erroresSemanticos, "Errores Semanticos");
        assertTrue("La compilación no debería ser exitosa", !success);
        assertTrue("Debería haber errores semánticos", !erroresSemanticos.isEmpty());
    }
}
