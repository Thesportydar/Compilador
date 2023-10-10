%{
package compi;

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

CTE                  : CTE_SHORT
                     | CTE_UINT
                     | CTE_FLOAT
                     ;

list_var             : list_var ';' ID
                     | ID
                     ;

funcion              : VOID ID '(' parametro ')' '{' cuerpo_funcion '}'
                     | VOID ID '(' ')' '{' cuerpo_funcion '}'
                     //| VOID ID '(' parametro ')' '{' cuerpo_funcion       {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al final de la función");}
                     //| VOID ID '(' ')' '{' cuerpo_funcion                 {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al final de la función");}
                     | VOID ID '(' parametro ')' cuerpo_funcion '}'       {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al comienzo de la función");}
                     | VOID ID '(' ')' cuerpo_funcion '}'                 {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al comienzo de la función");}
                     ;

parametro            : tipo ID
                     | tipo                 {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un identificador");}
                     | ID                   {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el tipo del parametro");}
                     ;

cuerpo_funcion       : cuerpo_funcion sen_declarativa sen_retorno
                     | cuerpo_funcion sen_ejecutable sen_retorno
                     | cuerpo_funcion sen_ejecutable
                     | sen_declarativa sen_retorno
                     | sen_ejecutable sen_retorno
                     | sen_ejecutable
                     ;

sen_retorno          : RETURN ','
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

asignacion           : ID '=' exp_aritmetica
                     | atributo_clase '=' exp_aritmetica
                     //| ID '='                           {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una expresión aritmética");}
                     | '=' exp_aritmetica               {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un identificador");}
                     | ID exp_aritmetica                {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un signo igual");}
                     | atributo_clase '='               {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una expresión aritmética");}
                     | atributo_clase exp_aritmetica    {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un signo igual");}
                     ;

inv_funcion          : ID '(' exp_aritmetica ')'
                     | ID '(' ')'
                     | ID '(' exp_aritmetica            {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
                     | ID exp_aritmetica ')'            {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
                     //| ID ')'                           {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
                     //| ID '('                           {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
                     ;

inv_metodo           : atributo_clase '(' exp_aritmetica ')'
                     | atributo_clase '(' ')'
                     | atributo_clase '('               {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
                     | atributo_clase ')'               {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
                     ;

atributo_clase       : ID '.' ID
                     ;

seleccion            : IF '(' condicion ')' bloque_sen_ejecutable ELSE bloque_sen_ejecutable END_IF
                     | IF '(' condicion ')' bloque_sen_ejecutable END_IF
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
                     | exp_aritmetica '+' '' termino   {agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
                     | exp_aritmetica '+' '/' termino   {agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
                     | exp_aritmetica '+' '-' termino   {agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
                     | exp_aritmetica '-' '+' termino   {agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
                     | exp_aritmetica '-' '-' termino   {agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
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

imprimir             : PRINT STR_1LN
                     | PRINT ','                 {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una cadena para imprimir");}
                     ;

sen_control          : WHILE '(' condicion ')' DO bloque_sen_ejecutable ','
                     | WHILE condicion ')' DO bloque_sen_ejecutable ',' {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un parentesis al comienzo de la condicion");}
                     | WHILE '(' condicion DO bloque_sen_ejecutable ',' {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un parentesis al final de la condicion");}
                     | WHILE condicion DO bloque_sen_ejecutable ','     {agregarError(errores_sintacticos, Parser.ERROR, "Se espera que la condicion este encerrada entre parentesis");}
                     | WHILE '(' condicion ')' DO ','                   {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un bloque de sentencias ejecutables");}
                     ;

clase                : CLASS ID '{' '}'
                     | CLASS ID '{' cuerpo_clase '}'
                     | CLASS ID ','
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

private static boolean errores_compilacion;

private static String tipo;

void yyerror(String mensaje) {
        // funcion utilizada para imprimir errores que produce yacc
        System.out.println("Error yacc: " + mensaje);
}

int yylex() {
    try {
        int token = lexicalAnalyzer.nextToken();
        return token;
    } catch (IOException e) {
        System.out.println("FIN LEXICO - Error: " + e.getMessage());
        return 0;
    }
}
public static void agregarError(List<String> errores, String tipo, String error) {
        if (tipo == Parser.ERROR) {
                errores_compilacion = true;
        }

        int linea_actual = 1;

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

LexicalAnalyzer lexicalAnalyzer;
SymbolTable st;

public static void main(String[] args) {
    TransitionMatrix<Integer> mI = new TransitionMatrix<>(19, 28);
    TransitionMatrix<AccionSemantica> mA = new TransitionMatrix<>(19, 28);
    SymbolTable sttemp = new SymbolTable();

    Main.loadMatrixs(mI, mA, "test.csv", sttemp);
    Parser parser = new Parser(new LexicalAnalyzer("test.txt", mI, mA), sttemp);
    parser.run();
    
    Parser.imprimirErrores(errores_lexicos, "Errores Lexicos");
    Parser.imprimirErrores(errores_sintacticos, "Errores Sintacticos");
    
    parser.st.print();
}

public Parser(LexicalAnalyzer lexicalAnalyzer, SymbolTable st) {
    this.lexicalAnalyzer = lexicalAnalyzer;
    this.st = st;
    yydebug = true;
}