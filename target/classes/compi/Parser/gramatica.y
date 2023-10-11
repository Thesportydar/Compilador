%{
package compi.Parser;
import compi.AccionesSemanticas.AccionSemantica;
import compi.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
%}

%token ID IF ELSE END_IF PRINT CLASS VOID WHILE DO SHORT UINT FLOAT
       CTE_SHORT CTE_UINT CTE_FLOAT INCREMENT GREATER_EQUAL LESS_EQUAL EQUAL NOT_EQUAL STR_1LN RETURN

%left '+' '-'
%left '*' '/'

%start prog

%%
prog                 : '{' sentencias '}'
                     | '{' sentencias            {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al final del programa");}
                     ;

sentencias           : sentencias sen_declarativa
                     | sentencias sen_ejecutable
                     | sentencias sen_control
                     | sen_ejecutable
                     | sen_control
                     | sen_declarativa
                     ;

sen_declarativa      : tipo list_var ','
                     | funcion
                     | clase
                     ;

tipo                 : SHORT
                     | UINT
                     | FLOAT
                     ;

CTE                  : CTE_SHORT     {verificarRango($1.ival);}
                     | CTE_UINT
                     | CTE_FLOAT     {verificarRango($1.ival);}
                     | '-' CTE_SHORT {resolverSigno($2.ival); verificarRango($2.ival);}
                     | '-' CTE_FLOAT {resolverSigno($2.ival); verificarRango($2.ival);}
                     ;

list_var             : list_var ';' ID
                     | ID
                     ;

funcion              : VOID ID '(' parametro ')' '{' cuerpo_funcion '}'   { agregarFuncion($2.ival, "VOID", $4.ival); }
                     | VOID ID '(' ')' '{' cuerpo_funcion '}'             { agregarFuncion($2.ival, "VOID", -1); }
                     /*| VOID ID '(' parametro ')' '{' cuerpo_funcion       {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al final de la función");}*/
                     /*| VOID ID '(' ')' '{' cuerpo_funcion                 {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al final de la función");}*/
                     | VOID ID '(' parametro ')' cuerpo_funcion '}'       {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al comienzo de la función");}
                     | VOID ID '(' ')' cuerpo_funcion '}'                 {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al comienzo de la función");}
                     ;

parametro            : tipo ID              { agregarParametro($2.ival, $1.ival); }
                     | tipo                 {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un identificador");}
                     | ID                   {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el tipo del parametro");}
                     ;

cuerpo_funcion       : cuerpo_funcion sen_declarativa sen_retorno ','
                     | cuerpo_funcion sen_ejecutable sen_retorno ','
                     | cuerpo_funcion sen_ejecutable
                     | sen_declarativa sen_retorno ','
                     | sen_ejecutable sen_retorno ','
                     | sen_ejecutable
                     ;

sen_retorno          : RETURN
                     ;

                     // deberian ir sentencias declarativas
sen_ejecutable       : asignacion ','
                     | inv_funcion ','
                     | seleccion ','
                     | imprimir ','
                     | inv_metodo ','
                     //| asignacion                {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una coma al final de la sentencia ejecutable");}
                     //| inv_funcion               {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una coma al final de la sentencia ejecutable");}
                     //| seleccion                 {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una coma al final de la sentencia ejecutable");}
                     //| imprimir                  {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una coma al final de la sentencia ejecutable");}
                     //| inv_metodo                {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una coma al final de la sentencia ejecutable");}
                     ;

asignacion           : ID '=' exp_aritmetica                {agregarEstructura("Asignacion al identificador ", $1.ival);}
                     | atributo_clase '=' exp_aritmetica    {agregarEstructura("Asignacion al identificador ", $1.ival);}
                     //| ID '='                           {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una expresión aritmética");}
                     | '=' exp_aritmetica               {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un identificador");}
                     | ID exp_aritmetica                {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un signo igual");}
                     | atributo_clase '='               {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una expresión aritmética");}
                     | atributo_clase exp_aritmetica    {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un signo igual");}
                     ;

inv_funcion          : ID '(' exp_aritmetica ')'        {agregarEstructura("Invocacion a la funcion ", $1.ival);}
                     | ID '(' ')'                       {agregarEstructura("Invocacion a la funcion ", $1.ival);}
                     | ID '(' exp_aritmetica            {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
                     | ID exp_aritmetica ')'            {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
                     //| ID ')'                           {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
                     //| ID '('                           {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
                     ;

inv_metodo           : atributo_clase '(' exp_aritmetica ')' {agregarEstructura("Invocacion al metodo ", $1.ival);}
                     | atributo_clase '(' ')'                {agregarEstructura("Invocacion al metodo ", $1.ival);}
                     | atributo_clase '('               {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
                     | atributo_clase ')'               {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
                     ;

atributo_clase       : ID '.' ID
                     ;

seleccion            : IF '(' condicion ')' bloque_sen_ejecutable ELSE bloque_sen_ejecutable END_IF {agregarEstructura("IF");}
                     | IF '(' condicion ')' bloque_sen_ejecutable END_IF {agregarEstructura("IF");}
                     | IF '(' condicion ')' ELSE bloque_sen_ejecutable END_IF {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un bloque de sentencias ejecutables");}
                     | IF '(' condicion ')' bloque_sen_ejecutable ELSE END_IF {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un bloque de sentencias ejecutables");}
                     | IF '(' condicion ')' END_IF  {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un bloque de sentencias ejecutables");}
                     ;

condicion            : exp_aritmetica comparador exp_aritmetica
                     | exp_aritmetica comparador {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una expresión aritmética");}
                     | comparador exp_aritmetica {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una expresión aritmética");}
                     | exp_aritmetica           {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un comparador");}
                     | comparador               {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba dos expresión aritmética");}
                     ;

                     //Los operandos de las expresiones aritméticas pueden ser variables, constantes, u otras expresiones aritméticas
                     //No se deben permitir anidamientos de expresiones con paréntesis
exp_aritmetica       : exp_aritmetica '+' termino
                     | exp_aritmetica '-' termino
                     | termino
                     | exp_aritmetica '+' '+' termino   {agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
                     | exp_aritmetica '+' '*' termino   {agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
                     | exp_aritmetica '+' '/' termino   {agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
                     /*| exp_aritmetica '+' '-' termino   {agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}*/
                     | exp_aritmetica '-' '+' termino   {agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
                     /*| exp_aritmetica '-' '-' termino   {agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}*/
                     | exp_aritmetica '-' '' termino   {agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
                     | exp_aritmetica '-' '/' termino   {agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
                     ;

termino              : termino '*' factor
                     | termino '/' factor
                     | factor
                     //| termino '*' '*' factor   {agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
                     //| termino '*' '/' factor   {agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
                     //| termino '*' '-' factor   {agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
                     //| termino '*' '+' factor   {agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
                     //| termino '/' '*' factor   {agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
                     //| termino '/' '/' factor   {agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
                     //| termino '/' '-' factor   {agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
                     //| termino '/' '+' factor   {agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
                     //| termino '-' '*' factor   {agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
                     //| termino '-' '/' factor   {agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
                     //| termino '-' '-' factor   {agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
                     //| termino '-' '+' factor   {agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
                     //| termino '+' '*' factor   {agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
                     //| termino '+' '/' factor   {agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
                     //| termino '+' '-' factor   {agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
                     ;

factor               : ID
                     | ID INCREMENT
                     | CTE
                     | atributo_clase
                     ;

comparador           : NOT_EQUAL
                     | EQUAL
                     | GREATER_EQUAL
                     | LESS_EQUAL
                     | '<'
                     | '>'
                     ;

                     // una sola sentencia ejecutable o varias encerradas por llave
bloque_sen_ejecutable: sen_ejecutable
                     | '{' sen_ejecutable_r '}'
                     | sen_ejecutable_r '}'      {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al comienzo del bloque de sentencias ejecutables");}
                     | '{' sen_ejecutable_r      {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al final del bloque de sentencias ejecutables");}
                     | '{' '}'                   {agregarError(errores_sintacticos, Parser.ERROR, "Se espera un bloque de sentencias ejecutables");}
                     ;

                     // conjunto sentencias ejecutable
sen_ejecutable_r     : sen_ejecutable_r sen_ejecutable
                     | sen_ejecutable
                     ;

imprimir             : PRINT STR_1LN             {agregarEstructura("PRINT");}
                     | PRINT ','                 {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una cadena para imprimir");}
                     ;

sen_control          : WHILE '(' condicion ')' DO bloque_sen_ejecutable ',' {agregarEstructura("WHILE");}
                     | WHILE condicion ')' DO bloque_sen_ejecutable ',' {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un parentesis al comienzo de la condicion");}
                     | WHILE '(' condicion DO bloque_sen_ejecutable ',' {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un parentesis al final de la condicion");}
                     | WHILE condicion DO bloque_sen_ejecutable ','     {agregarError(errores_sintacticos, Parser.ERROR, "Se espera que la condicion este encerrada entre parentesis");}
                     | WHILE '(' condicion ')' DO ','                   {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un bloque de sentencias ejecutables");}
                     ;

clase                : CLASS ID '{' '}'                   {agregarClase($2.ival, "CLASS");}
                     | CLASS ID '{' cuerpo_clase '}'
                     | CLASS ID ','                       {agregarClase($2.ival, "FDCLASS");}
                     //| CLASS ID '{' cuerpo_clase        {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al final de la clase");}
                     | CLASS ID cuerpo_clase '}'       {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al final de la clase");}
                     //| CLASS ID                         {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una coma al final de la clase");}
                     ;

cuerpo_clase         : cuerpo_clase sen_declarativa
                     | sen_declarativa // deberia llevar ',' para funciones
                     ;

%%

public static final String ERROR = "Error";
public static final List<String> errores_lexicos = new ArrayList<>();
public static final List<String> errores_sintacticos = new ArrayList<>();
public static final List<String> estructuras = new ArrayList<>();
public static final float FLOAT_MIN = 1.17549435E-38f;

private static boolean errores_compilacion;

private static String tipo;

void yyerror(String mensaje) {
        // funcion utilizada para imprimir errores que produce yacc
        System.out.println("Error yacc: " + mensaje);
}

int yylex() {
    try {
        int token = lexicalAnalyzer.nextToken();
        if (lexicalAnalyzer.getPtrActual() > 0)
            yyval = new ParserVal(lexicalAnalyzer.getPtrActual());

        return token;
    } catch (IOException e) {
        System.out.println("FIN LEXICO - Error: " + e.getMessage());
        return 0;
    }
}

public void agregarError(List<String> errores, String tipo, String error) {
        if (tipo == Parser.ERROR) {
                errores_compilacion = true;
        }

        int linea_actual = lexicalAnalyzer.getLine();

        errores.add(tipo + " (Linea " + linea_actual + "): " + error);
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

public void agregarEstructura(String s) {
    estructuras.add("Linea(" + lexicalAnalyzer.getLine() + "): " + s);
}

public void agregarEstructura(String s, Integer ptr) {
    estructuras.add("Linea(" + lexicalAnalyzer.getLine() + "): " + s + st.getLexema(ptr));
}

public void agregarClase(Integer id, String description) {
    st.setAttribute(id, "description", description);
    agregarEstructura("CLASS :" + st.getLexema(id));
}

public void resolverSigno(Integer ptr_cte) {
    st.setLexema(ptr_cte, "-" + st.getLexema(ptr_cte));
}

public void verificarRango(Integer ptr_cte) {
    String lexema = st.getLexema(ptr_cte);
    String tipo = st.getAttribute(ptr_cte,"description");
    switch (tipo) {
      case "float":
        float value_f = Float.parseFloat(lexema);
        if (!(value_f <= Float.MAX_VALUE && value_f >= FLOAT_MIN || value_f >= -Float.MAX_VALUE && value_f <= -FLOAT_MIN || value_f == 0)){
            agregarError(errores_sintacticos, Parser.ERROR, "Constante FLOAT fuera de rango");
        }
      case "shortint":
        Short value_s = Short.parseShort(lexema);
        if (!(value_s <= Short.MAX_VALUE && value_s >= Short.MIN_VALUE)){
            agregarError(errores_sintacticos, Parser.ERROR, "Constante ShortInt fuera de rango");
        }
    }
}

public void agregarFuncion(Integer id, String tipo, Integer parametro) {
    st.setAttribute(id, "description", "FUNCTION");
    st.setAttribute(id, "type", tipo);
    if (parametro != null) {
      st.setAttribute(id, "parameter", parametro.toString());
    }
    
    agregarEstructura("FUNCTION :" + st.getLexema(id));
}

public void agregarParametro(Integer id, Integer tipo){
    String tipo_s;
    switch (tipo){
        case 266:
          tipo_s = "shortint";
          break;
        case 267:
          tipo_s = "uint";
          break;
        case 268:
          tipo_s = "float";
          break;
        default:
          return;
    }

    st.setAttribute(id, "description", "PARAMETER");
    st.setAttribute(id, "type", tipo_s);
}

LexicalAnalyzer lexicalAnalyzer;
SymbolTable st;

public static void main(String[] args) {
    TransitionMatrix<Integer> mI = new TransitionMatrix<>(19, 28);
    TransitionMatrix<AccionSemantica> mA = new TransitionMatrix<>(19, 28);
    SymbolTable sttemp = new SymbolTable();

    Main.loadMatrixs(mI, mA, "test.csv", sttemp, errores_lexicos);
    Parser parser = new Parser(new LexicalAnalyzer("test.txt", mI, mA, errores_lexicos), sttemp);
    parser.run();
    
    Parser.imprimirErrores(errores_lexicos, "Errores Lexicos");
    Parser.imprimirErrores(errores_sintacticos, "Errores Sintacticos");
    Parser.imprimirErrores(estructuras, "Estructuras Sintacticas");
    
    parser.st.print();
}

public Parser(LexicalAnalyzer lexicalAnalyzer, SymbolTable st) {
    this.lexicalAnalyzer = lexicalAnalyzer;
    this.st = st;
    yydebug = true;
}