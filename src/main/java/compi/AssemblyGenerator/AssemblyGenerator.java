package compi.AssemblyGenerator;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.print.attribute.standard.MediaSize.ISO;

import compi.PilaTercetos;
import compi.SymbolTable;
import compi.Terceto;
import compi.Parser.Parser;

public class AssemblyGenerator {
    private PilaTercetos pilaTercetos;
    private SymbolTable st;
    private String filePath;
    private FileWriter fileWriter;
    private int contadorAux = 0;

    public AssemblyGenerator(PilaTercetos pilaTercetos, SymbolTable st, String filePath) {
        this.pilaTercetos = pilaTercetos;
        this.st = st;
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
        fileWriter.write(".386\n");
        fileWriter.write(".model flat, stdcall\n");
        fileWriter.write("option casemap :none\n");
        fileWriter.write("include \\masm32\\include\\windows.inc\n");
        fileWriter.write("include \\masm32\\include\\kernel32.inc\n");
        fileWriter.write("include \\masm32\\include\\user32.inc\n");
        fileWriter.write("includelib \\masm32\\lib\\kernel32.lib\n");
        fileWriter.write("includelib \\masm32\\lib\\user32.lib\n");
        fileWriter.write(".data\n");
        volcarTablaSimbolos();
    }
    
    private void volcarTablaSimbolos() throws IOException {
        // codigo para volcar la tabla de simbolos a la seccion .data como dd ?
        for (Integer key : st.keySet()) {
            String tipo = st.getAttribute(key, "tipo");
            if (tipo == null)
                continue;
            switch (tipo) {
                case "string_1_ln":
                    fileWriter.write("@" + key.toString() + " db " + st.getLexema(key) + ", 0\n");
                    break;
                case ""+Parser.CTE_FLOAT:
                case ""+Parser.CTE_SHORT:
                case ""+Parser.CTE_UINT:
                    fileWriter.write("@" + key.toString() + " dd " + st.getLexema(key) + "\n");
                    break;
                default:
                    fileWriter.write("@" + key.toString() + " dd ?\n");
                    break;
            }
        }
    }

    public Integer obtenerVarAux(Short tipo) {
        st.setAttribute(st.addEntry("@aux"+contadorAux, Parser.ID), "tipo", ""+tipo);
        return contadorAux++;
    }

    public void generarAssembler() throws IOException{
        fileWriter.write(".code\n");
        for (Terceto terceto : pilaTercetos)
            generarCodigoTerceto(terceto);
        fileWriter.close();
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
        else if (op1 != -1)
            tipo = Short.parseShort(st.getAttribute(terceto.getOperando1(), "tipo"));

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
            /*case "while":
                generarCodigoWhile(terceto);
                break;
            case "print":
                generarCodigoPrint(terceto);
                break;
            case "call":
                generarCodigoCall(terceto);
                break;
            case "end":
                generarCodigoEnd(terceto);
                break;
            case "&&":
            case "||":
                generarCodigoLogico(terceto);
                break;*/
            case "<":
            case ">":
            case "<=":
            case ">=":
            case "==":
            case "!=":
                generarCodigoComparacion(tipo, sop1, sop2);
                break;
            default:
                // es una etiqueta, agregarla
                fileWriter.write(op + ":\n");
                break;
        }
    }

    private Integer generarCodigoSuma(Short tipo, String op1, String op2) throws IOException {
        Integer aux = obtenerVarAux(tipo);

        switch(tipo) {
            case (Parser.SHORT)://16b
                fileWriter.write("mov ax, @" + op1 + "\n");
                fileWriter.write("add ax, @" + op2 + "\n");
                fileWriter.write("mov @aux" + aux.toString() + ", ax\n");
                break;
            case (Parser.UINT)://16b 0 to 65535
            case (Parser.FLOAT)://32b -3.4E38 to 3.4E38
                fileWriter.write("mov eax, @" + op1 + "\n");
                fileWriter.write("add eax, @" + op2 + "\n");
                fileWriter.write("mov @aux" + aux.toString() + ", eax\n");
                break;
        }
        return aux;
    }

    private Integer generarCodigoResta(Short tipo, String op1, String op2) throws IOException {
        Integer aux = obtenerVarAux(tipo);

        switch(tipo) {
            case (Parser.SHORT)://16b
                fileWriter.write("mov ax, @" + op1 + "\n");
                fileWriter.write("sub ax, @" + op2 + "\n");
                fileWriter.write("mov @aux" + aux.toString() + ", ax\n");
                break;
            case (Parser.UINT)://16b 0 to 65535
            case (Parser.FLOAT)://32b -3.4E38 to 3.4E38
                fileWriter.write("mov eax, @" + op1 + "\n");
                fileWriter.write("sub eax, @" + op2 + "\n");
                fileWriter.write("mov @aux" + aux.toString() + ", eax\n");
                break;
        }
        return aux;
    }

    private Integer generarCodigoMultiplicacion(Short tipo, String op1, String op2) throws IOException {
        Integer aux = obtenerVarAux(tipo);
        
        switch(tipo) {
            case (Parser.SHORT)://16b
                fileWriter.write("mov ax, @" + op1 + "\n");
                fileWriter.write("mul ax, @" + op2 + "\n");
                fileWriter.write("mov @aux" + aux.toString() + ", ax\n");
                break;
            case (Parser.UINT)://16b 0 to 65535
            case (Parser.FLOAT)://32b -3.4E38 to 3.4E38
                fileWriter.write("mov eax, @" + op1 + "\n");
                fileWriter.write("mul eax, @" + op2 + "\n");
                fileWriter.write("mov @aux" + aux.toString() + ", eax\n");
                break;
        }
        return aux;
    }

    private Integer generarCodigoDivision(Short tipo, String op1, String op2) throws IOException {
        Integer aux = obtenerVarAux(tipo);
        
        switch(tipo) {
            case (Parser.SHORT)://16b
                fileWriter.write("mov ax, @" + op1 + "\n");
                fileWriter.write("div ax, @" + op2 + "\n");
                fileWriter.write("mov @aux" + aux.toString() + ", ax\n");
                break;
            case (Parser.UINT)://16b 0 to 65535
            case (Parser.FLOAT)://32b -3.4E38 to 3.4E38
                fileWriter.write("mov eax, @" + op1 + "\n");
                fileWriter.write("div eax, @" + op2 + "\n");
                fileWriter.write("mov @aux" + aux.toString() + ", eax\n");
                break;
        }
        return aux;
    }

    public void generarCodigoAsignacion(Short tipo, String op1, String op2) throws IOException {
        switch(tipo) {
            case (Parser.SHORT)://16b
                fileWriter.write("mov ax, @" + op2 + "\n");
                fileWriter.write("mov @" + op1 + ", ax\n");
                break;
            case (Parser.UINT)://16b 0 to 65535
            case (Parser.FLOAT)://32b -3.4E38 to 3.4E38
                fileWriter.write("mov eax, @" + op2 + "\n");
                fileWriter.write("mov @" + op1 + ", eax\n");
                break;
        }
    }

    public void generarCodigoComparacion(Short tipo, String op1, String op2) throws IOException {
        switch(tipo) {
            case (Parser.SHORT)://16b
                fileWriter.write("mov ax, @" + op1 + "\n");
                fileWriter.write("cmp ax, @" + op2 + "\n");
                break;
            case (Parser.UINT)://16b 0 to 65535
            case (Parser.FLOAT)://32b -3.4E38 to 3.4E38
                fileWriter.write("mov eax, @" + op1 + "\n");
                fileWriter.write("cmp eax, @" + op2 + "\n");
                break;
        }
    }

    public void generarCodigoBF(Terceto terceto) throws IOException {
        // saltar si el resultado de la comparacion anterior es falso
        // tag es el numero de terceto al que salta, buscar el tag
        String tag = terceto.getOperando2().toString();
        tag = pilaTercetos.get(Integer.parseInt(tag)-1).getOperador();

        fileWriter.write("jne " + tag + "\n");
    }

    public void generarCodigoBI(Terceto terceto) throws IOException {
        // saltar si el resultado de la comparacion anterior es falso
        // tag es el numero de terceto al que salta, buscar el tag
        String tag = terceto.getOperando1().toString();
        tag = pilaTercetos.get(Integer.parseInt(tag)-1).getOperador();

        fileWriter.write("jmp " + tag + "\n");
    }
}
