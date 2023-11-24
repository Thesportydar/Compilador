package compi.AssemblyGenerator;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.print.attribute.standard.MediaSize.ISO;

import compi.LexicalAnalyzer;
import compi.PilaTercetos;
import compi.SymbolTable;
import compi.Terceto;
import compi.Parser.Parser;

public class AssemblyGenerator {
    private PilaTercetos pilaTercetos;
    private SymbolTable st;
    private String filePath;
    private FileWriter fileWriter;
    StringBuilder dataBuffer, codeBuffer, cursorBuffer, startBuffer;
    private int contadorAux = 0;
    private int label = 0;
    private int nesting = 0;

    public AssemblyGenerator(PilaTercetos pilaTercetos, SymbolTable st, String filePath) {
        this.pilaTercetos = pilaTercetos;
        this.st = st;
        this.dataBuffer = new StringBuilder();
        this.codeBuffer = new StringBuilder();
        this.startBuffer = new StringBuilder();
        this.cursorBuffer = startBuffer;
        this.filePath = filePath;

        try {
            this.fileWriter = new FileWriter(filePath);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void generarCabecera() throws IOException {
        // genera la cabecera .data
        dataBuffer.append(".386\n");
        dataBuffer.append(".model flat, stdcall\n");
        dataBuffer.append("option casemap :none\n");
        dataBuffer.append("include \\masm32\\include\\windows.inc\n");
        dataBuffer.append("include \\masm32\\include\\kernel32.inc\n");
        dataBuffer.append("include \\masm32\\include\\user32.inc\n");
        dataBuffer.append("includelib \\masm32\\lib\\kernel32.lib\n");
        dataBuffer.append("includelib \\masm32\\lib\\user32.lib\n");
        dataBuffer.append(".data\n");
        dataBuffer.append("callStack DWORD 100 DUP(?)\n");
        dataBuffer.append("stackPointer DWORD 0\n");

        volcarTablaSimbolos();
    }
    // Mapear los tipos de datos a los tamaños correspondientes
    private static final Map<String, String> tipoToSize = new HashMap<>();
    // Bloque estático para inicializar el mapa
    static {
        tipoToSize.put("" + Parser.SHORT, "dw");
        tipoToSize.put("" + Parser.UINT, "dd");
        tipoToSize.put("" + Parser.FLOAT, "dd");
    }
    
    private void volcarTablaSimbolos() throws IOException {
        // codigo para volcar la tabla de simbolos a la seccion .data como dd ?
        for (Integer key : st.keySet()) {
            String tipo = st.getAttribute(key, "tipo");
            String uso = st.getAttribute(key, "uso");
            String size = tipoToSize.get(tipo);
            String valid = st.getAttribute(key, "valid");
            
            if (valid == null || !valid.equals("1") || tipo == null)
                continue;
            
            switch(tipo) {
                case ""+Parser.STR_1LN:
                    dataBuffer.append("@" + key + " db \"" + st.getLexema(key) + "\", 0\n");
                    break;
                case ""+Parser.SHORT:
                case ""+Parser.UINT:
                case ""+Parser.FLOAT:
                    dataBuffer.append("@" + key + " " + size + " ");
                    if (uso.equals("cte"))
                    dataBuffer.append(st.getLexema(key) + "\n");
                    else
                        dataBuffer.append("?\n");
                    break;
            }
        }
    }

    public Integer obtenerVarAux(Short tipo, String size) { // TODO: reutilizar aux, hashmap con valid bit
        // agregamos la nueva variable auxiliar a la tabla de simbolos para poder guardar si tipo
        Integer ptr = st.addEntry("@aux"+contadorAux, Parser.ID);
        st.setAttribute(ptr, "tipo", ""+tipo);
        // volcamos la variable auxiliar a la seccion .data, size es dw, dd o db
        dataBuffer.append("@aux" + contadorAux + " " + size + " ?\n");
        return contadorAux++;
    }

    public void generarAssembler() throws IOException{
        generarCabecera();

        for (Terceto terceto : pilaTercetos)
            generarCodigoTerceto(terceto);

        // genero el codigo para manejo de errores
        checkRecursividad();
        pushToCallStack();
        popFromCallStack();
        overflow();
        // volcar al fileWriter
        fileWriter.write(dataBuffer.toString());
        fileWriter.write(".code\n");
        fileWriter.write(codeBuffer.toString());
        fileWriter.write("start:\n");
        fileWriter.write(startBuffer.toString());
        fileWriter.write("invoke ExitProcess, 0\n");
        fileWriter.write("end start\n");
        fileWriter.close();
    }

    private Short getTipo(Integer op, boolean esEtiqueta) {
        if (!esEtiqueta)
            return Short.parseShort(st.getAttribute(op, "tipo"));
        else {
            int terceto = pilaTercetos.get(op-1).getOperando1();
            terceto = st.getPtr("@aux"+terceto);
            if (terceto == 0)
                return 0;
            else
                return Short.parseShort(st.getAttribute(terceto, "tipo"));
        }
    }

    private void generarCodigoTerceto(Terceto terceto) throws IOException {
        // case con el operador y llamar a generarCodigo{operacion}
        // operacion : [asignacion, suma, mul, comparacion, while, if, print, call, etc...]
        String op = terceto.getOperador();
        // obtener los operandos, son integers y hay que verificar que si estan en la tabla de
        // simbolos o son tercetos con el metodo esEtiqueta
        Integer op1 = terceto.getOperando1();
        Integer op2 = terceto.getOperando2();
        String sop1 = ""+op1;
        String sop2 = ""+op2;
        Short tipo = 0;

        if (terceto.esEtiqueta2())
            sop2 = "aux" + pilaTercetos.get(op2-1).getOperando1().toString();
        if (terceto.esEtiqueta1())
            sop1 = "aux" + pilaTercetos.get(op1-1).getOperando1().toString();
        
        if (op1 != -1)
            tipo = getTipo(op1, terceto.esEtiqueta1());

        Integer aux;

        switch (op) {
            case "+":
                aux = generarCodigoSuma(tipo, sop1, sop2);
                terceto.setOperando1(aux, true);
                break;
            case "-":
                aux = generarCodigoResta(tipo, sop1, sop2);
                terceto.setOperando1(aux, true);
                break;
            case "*":
                aux = generarCodigoMultiplicacion(tipo, sop1, sop2);
                terceto.setOperando1(aux, true);
                break;
            case "/":
                aux = generarCodigoDivision(tipo, sop1, sop2);
                terceto.setOperando1(aux, true);
                break;
            case "=":
                generarCodigoAsignacion(tipo, sop1, sop2);
                break;
            case "BF":
                generarCodigoBF(terceto);
                break;
            case "BI":
                generarCodigoBI(terceto);
                break;
            case "PRINT":
                cursorBuffer.append("invoke MessageBox, NULL, addr @" + sop1 + ", addr @" + sop1 + ", MB_OK\n");
                break;
            case "CALL":
                cursorBuffer.append("call " + st.getLexema(op1).replaceAll(":", "@") + "\n");
                break;
            case "PROC":
                cursorBuffer = codeBuffer;
                sop1 = st.getLexema(op1).replaceAll(":","@");
                cursorBuffer.append(sop1 + " proc\n");
                nesting++;
                cursorBuffer.append("mov eax, offset" + sop1 + "\n");
                cursorBuffer.append("call CheckCallStack\n");
                cursorBuffer.append("call PushToCallStack\n");
                break;
            case "RET":
                cursorBuffer.append("call PopFromCallStack\n");
                cursorBuffer.append("ret\n");
                //cursorBuffer.append(st.getLexema(op1) + " endp\n");
                if (--nesting == 0)
                    cursorBuffer = startBuffer;
                break;
            case "<":
            case ">":
            case "<=":
            case ">=":
            case "==":
            case "!!":
                generarCodigoComparacion(tipo, sop1, sop2);
                break;
            default:
                // es una etiqueta, agregarla
                cursorBuffer.append(op + ":\n");
                break;
        }
    }

    private Integer generarCodigoSuma(Short tipo, String op1, String op2) throws IOException {
        Integer aux = 0;

        switch(tipo) {
            case (Parser.SHORT)://16b
                cursorBuffer.append("mov ax, @" + op1 + "\n");
                cursorBuffer.append("add ax, @" + op2 + "\n");
                aux = obtenerVarAux(tipo, "dw");
                cursorBuffer.append("mov @aux" + aux.toString() + ", ax\n");
                break;
            case (Parser.UINT)://16b 0 to 65535
                cursorBuffer.append("mov eax, @" + op1 + "\n");
                cursorBuffer.append("add eax, @" + op2 + "\n");
                aux = obtenerVarAux(tipo, "dd");
                cursorBuffer.append("mov @aux" + aux.toString() + ", eax\n");
                break;
            case (Parser.FLOAT)://32b -3.4E38 to 3.4E38
                cursorBuffer.append("fld @" + op1 + "\n");
                cursorBuffer.append("fadd @" + op2 + "\n");
                aux = obtenerVarAux(tipo, "dd");
                cursorBuffer.append("fstp @aux" + aux.toString() + "\n");
                // verificar overflow
                cursorBuffer.append("fstsw ax\n");
                cursorBuffer.append("sahf\n");
                cursorBuffer.append("jo OverflowDetected\n");
                break;
        }
        return aux;
    }

    private Integer generarCodigoResta(Short tipo, String op1, String op2) throws IOException {
        Integer aux = 0;

        switch(tipo) {
            case (Parser.SHORT)://16b
                cursorBuffer.append("mov ax, @" + op1 + "\n");
                cursorBuffer.append("sub ax, @" + op2 + "\n");
                aux = obtenerVarAux(tipo, "dw");
                cursorBuffer.append("mov @aux" + aux.toString() + ", ax\n");
                break;
            case (Parser.UINT)://16b 0 to 65535
                cursorBuffer.append("mov eax, @" + op1 + "\n");
                cursorBuffer.append("sub eax, @" + op2 + "\n");
                aux = obtenerVarAux(tipo, "dd");
                cursorBuffer.append("mov @aux" + aux.toString() + ", eax\n");
                break;
            case (Parser.FLOAT)://32b -3.4E38 to 3.4E38
                cursorBuffer.append("fld @" + op1 + "\n");
                cursorBuffer.append("fsub @" + op2 + "\n");
                aux = obtenerVarAux(tipo, "dd");
                cursorBuffer.append("fstp @aux" + aux.toString() + "\n");
                break;
        }
        return aux;
    }

    private Integer generarCodigoMultiplicacion(Short tipo, String op1, String op2) throws IOException {
        Integer aux = 0;
        
        switch(tipo) {
            case (Parser.SHORT)://16b con signo
                cursorBuffer.append("mov ax, @" + op1 + "\n");
                cursorBuffer.append("imul @" + op2 + "\n"); // multiplicacion con signo
                aux = obtenerVarAux(tipo, "dw");
                cursorBuffer.append("mov @aux" + aux.toString() + ", ax\n");
                // verificar overflow
                cursorBuffer.append("jo OverflowDetected\n");
                break;
            case (Parser.UINT)://16b 0 to 65535
                cursorBuffer.append("mov eax, @" + op1 + "\n");
                cursorBuffer.append("mul @" + op2 + "\n"); // multiplicacion sin signo
                aux = obtenerVarAux(tipo, "dd");
                cursorBuffer.append("mov @aux" + aux.toString() + ", eax\n");
                cursorBuffer.append("jo OverflowDetected\n");
                break;
            case (Parser.FLOAT)://32b -3.4E38 to 3.4E38
                cursorBuffer.append("fld @" + op1 + "\n");
                cursorBuffer.append("fmul @" + op2 + "\n");
                aux = obtenerVarAux(tipo, "dd");
                cursorBuffer.append("fstp @aux" + aux.toString() + "\n");
                break;
        }
        return aux;
    }

private Integer generarCodigoDivision(Short tipo, String op1, String op2) throws IOException {
    Integer aux = 0;

    switch (tipo) {
        case (Parser.SHORT):
            cursorBuffer.append("mov ax, @" + op1 + "\n");
            cursorBuffer.append("idiv @" + op2 + "\n");
            aux = obtenerVarAux(tipo, "dw");
            cursorBuffer.append("mov @aux" + aux.toString() + ", ax\n");
            break;

        case (Parser.UINT):
            cursorBuffer.append("mov eax, @" + op1 + "\n");
            cursorBuffer.append("xor edx, edx\n"); // Limpiar edx para la división sin signo
            cursorBuffer.append("div @" + op2 + "\n");
            aux = obtenerVarAux(tipo, "dd");
            cursorBuffer.append("mov @aux" + aux.toString() + ", eax\n");
            break;

        case (Parser.FLOAT):
            cursorBuffer.append("fld @" + op1 + "\n");
            cursorBuffer.append("fdiv @" + op2 + "\n");
            aux = obtenerVarAux(tipo, "dd");
            cursorBuffer.append("fstp @aux" + aux.toString() + "\n");
            break;
    }

    return aux;
}

    public void generarCodigoAsignacion(Short tipo, String op1, String op2) throws IOException {
        switch(tipo) {
            case (Parser.SHORT)://16b
                cursorBuffer.append("mov ax, @" + op2 + "\n");
                cursorBuffer.append("mov @" + op1 + ", ax\n");
                break;
            case (Parser.UINT)://16b 0 to 65535
                cursorBuffer.append("mov eax, @" + op2 + "\n");
                cursorBuffer.append("mov @" + op1 + ", eax\n");
                break;
            case (Parser.FLOAT)://32b -3.4E38 to 3.4E38
                cursorBuffer.append("fld @" + op2 + "\n");
                cursorBuffer.append("fstp @" + op1 + "\n");
                break;
        }
    }

    public void generarCodigoComparacion(Short tipo, String op1, String op2) throws IOException {
        switch(tipo) {
            case (Parser.SHORT)://16b
                cursorBuffer.append("mov ax, @" + op1 + "\n");
                cursorBuffer.append("cmp ax, @" + op2 + "\n");
                break;
            case (Parser.UINT)://16b 0 to 65535
                cursorBuffer.append("mov eax, @" + op1 + "\n");
                cursorBuffer.append("cmp eax, @" + op2 + "\n");
                break;
            case (Parser.FLOAT)://32b -3.4E38 to 3.4E38
                cursorBuffer.append("fld @" + op1 + "\n");
                cursorBuffer.append("fcomp @" + op2 + "\n");
                // almacenar flags
                cursorBuffer.append("fstsw ax\n");
                cursorBuffer.append("sahf\n");
                break;
        }
    }

    public void generarCodigoBF(Terceto terceto) throws IOException {
        // saltar si el resultado de la comparacion anterior es falso
        // tag es el numero de terceto al que salta, buscar el tag
        String cmp = pilaTercetos.get(terceto.getOperando1()-1).getOperador();
        String tag = terceto.getOperando2().toString();
        tag = pilaTercetos.get(Integer.parseInt(tag)-1).getOperador();

        switch (cmp) {
            case "!!":
                cursorBuffer.append("je " + tag + "\n");
                break;
            case "==":
                // agregar je, jpm y tagnuevo
                cursorBuffer.append("je label" + label + "\n");
                cursorBuffer.append("jmp " + tag + "\n");
                cursorBuffer.append("label" + label + ":\n");
                label++;
                break;
            case "<":
                cursorBuffer.append("jg " + tag + "\n");
                cursorBuffer.append("je " + tag + "\n");
                break;
            case ">":
                cursorBuffer.append("jl " + tag + "\n");
                cursorBuffer.append("je " + tag + "\n");
                break;
            case "<=":
                cursorBuffer.append("jg " + tag + "\n");
                break;
            case ">=":
                cursorBuffer.append("jl " + tag + "\n");
                break;
            default:
                break;
        }
    }

    public void generarCodigoBI(Terceto terceto) throws IOException {
        // saltar si el resultado de la comparacion anterior es falso
        // tag es el numero de terceto al que salta, buscar el tag
        String tag = terceto.getOperando1().toString();
        tag = pilaTercetos.get(Integer.parseInt(tag)-1).getOperador();

        cursorBuffer.append("jmp " + tag + "\n");
    }

    private void overflow() throws IOException {
        cursorBuffer.append("OverflowDetected:\n");
        cursorBuffer.append("invoke MessageBox, NULL, addr @overflow, addr @overflow, MB_OK\n");
        cursorBuffer.append("invoke ExitProcess, 0\n");
    }

    private void checkRecursividad() {
    //; Parámetro: eax = Dirección del procedimiento a verificar
    cursorBuffer.append("CheckCallStack PROC\n");
    cursorBuffer.append("mov ecx, stackPointer\n");
    cursorBuffer.append("cmp exc, 0\n");
    cursorBuffer.append("je NoRecursion\n");
    cursorBuffer.append("mov edx, DWORD PTR [callStack + (ecx - 1) * 4]\n"); // obtengo el ultimo elemento
    cursorBuffer.append("cmp edx, eax\n");
    cursorBuffer.append("je Recursion\n");
    cursorBuffer.append("jmp NoRecursion\n");
    cursorBuffer.append("Recursion:\n");
    cursorBuffer.append("invoke MessageBox, NULL, addr @recursividad, addr @recursividad, MB_OK\n");
    cursorBuffer.append("invoke ExitProcess, 0\n");
    cursorBuffer.append("NoRecursion:\n");
    cursorBuffer.append("ret\n");
    }

    public void pushToCallStack() {
        cursorBuffer.append("PushToCallStack PROC\n");
        cursorBuffer.append("mov edx, stackPointer\n");
        cursorBuffer.append("mov DWORD PTR [callStack + edx * 4], eax\n");
        cursorBuffer.append("inc stackPointer\n"); //TODO:verificar, esto puede fallar tranquilamente
        cursorBuffer.append("ret\n");
    }

    public void popFromCallStack() {
        cursorBuffer.append("PopFromCallStack PROC\n");
        cursorBuffer.append("dec stackPointer\n"); //TODO:verificar, esto puede fallar tranquilamente
        cursorBuffer.append("mov eax, DWORD PTR [callStack + stackPointer * 4]\n");
        cursorBuffer.append("ret\n");
    }
}
