%{
package compi.Parser;
import compi.AccionesSemanticas.AccionSemantica;
import compi.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
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
                     | error ','                 {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una sentencia ejecutable");}
                     ;

sen_declarativa      : tipo list_var ','
                     | funcion
                     | clase
                     ;

tipo                 : SHORT { $$.ival = SHORT; tipo = (int)SHORT; }
                     | UINT  { $$.ival = UINT; tipo = (int)UINT; }
                     | FLOAT { $$.ival = FLOAT; tipo = (int)FLOAT; }
                     | ID    { $$.ival = st.getPtr(st.getLexema($1.ival), ambitoActual.copy()); st.delEntry($1.ival); tipo = $$.ival; }
                     ;

CTE                  : CTE_SHORT     {verificarRango($1.ival);}
                     | CTE_UINT
                     | CTE_FLOAT     {verificarRango($1.ival);}
                     | '-' CTE_SHORT {resolverSigno($2.ival); verificarRango($2.ival); $$.ival = $2.ival;}
                     | '-' CTE_FLOAT {resolverSigno($2.ival); verificarRango($2.ival); $$.ival = $2.ival;}
                     | '-' CTE_UINT { agregarError(errores_sintacticos, Parser.ERROR, "No se puede negar un unsigned int");}
                     ;

list_var             : list_var ';' ID { st.setAttribute($3.ival, "tipo", tipo.toString());
                                         st.setLexema($3.ival, st.getLexema($3.ival) + ":" + ambitoActual.toString()); }
                                         /*st.upgradeLexema($3.ival, ":"+ambitoActual.toString()); }*/
                     | ID              { st.setAttribute($1.ival, "tipo", tipo.toString());
                                         st.setLexema($1.ival, st.getLexema($1.ival) + ":" + ambitoActual.toString()); }
                                         /*st.upgradeLexema($1.ival, ":"+ambitoActual.toString()); }*/
                     ;

funcion              : header_funcion '{' cuerpo_funcion '}' { ambitoActual.pop(); }
funcion              : header_funcion cuerpo_funcion '}' {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al comienzo de la función");}
                     ;

header_funcion       : VOID ID '(' parametro ')' { st.setLexema($2.ival, st.getLexema($2.ival) + ":" + ambitoActual.toString());
                                                   agregarFuncion($2.ival, VOID, $4.ival);
                                                   st.setLexema($4.ival, st.getLexema($4.ival) + ":" + ambitoActual.toString()); }
                                                   /*Integer ptr = st.upgradeLexema($2.ival, ":"+ambitoActual.toString());*/
                                                   /*agregarFuncion(ptr, VOID, $4.ival);*/
                                                   /*st.upgradeLexema($4.ival, ":"+ambitoActual.toString()); }*/
                     | VOID ID '(' ')'           { st.setLexema($2.ival, st.getLexema($2.ival) + ":" + ambitoActual.toString());
                                                   agregarFuncion($2.ival, VOID, -1); }
                                                   /*Integer ptr = st.upgradeLexema($2.ival, ":"+ambitoActual.toString());*/
                                                   /*agregarFuncion(ptr, VOID, -1); }*/
                     ;

parametro            : tipo ID              {agregarParametro($2.ival, $1.ival); $$.ival = $2.ival; }
                     ;

cuerpo_funcion       : cuerpo_funcion sen_declarativa sen_retorno ','
                     | cuerpo_funcion sen_ejecutable sen_retorno ','
                     | cuerpo_funcion seleccion_func
                     | sen_declarativa sen_retorno ','
                     | sen_ejecutable sen_retorno ','
                     | seleccion_func
                     ;

sen_retorno          : RETURN
                     ;

                     // deberian ir sentencias declarativas
sen_ejecutable       : asignacion ','
                     | inv_funcion ','
                     | seleccion ','
                     | imprimir ','
                     | inv_metodo ','
                     ;

asignacion           : ID '=' exp_aritmetica                { Integer ptr = st.getPtr(st.getLexema($1.ival), ambitoActual.copy());
                                                              st.delEntry($1.ival); $1.ival = $$.ival;
                                                             agregarEstructura("Asignacion al identificador ", ptr);
                                                             $$.ival = crearTerceto("=", ptr, $3.ival, "st", $3.sval);
                                                             $$.sval = "terceto";}
                     | atributo_clase '=' exp_aritmetica    { Integer ptr = st.getPtr(st.getLexema($1.ival), ambitoActual.copy());
                                                              st.delEntry($1.ival); $1.ival = $$.ival;
                                                              agregarEstructura("Asignacion al identificador ", ptr);
                                                              $$.ival = crearTerceto("=", ptr, $3.ival, "st", $3.sval);
                                                              $$.sval = "terceto";}
                     | ID '='                           {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una expresión aritmética");}
                     | '=' exp_aritmetica               {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un identificador");}
                     /*| ID exp_aritmetica                {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un signo igual");}*/
                     | atributo_clase '='               {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una expresión aritmética");}
                     | atributo_clase exp_aritmetica    {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un signo igual");}
                     ;

inv_funcion          : ID '(' exp_aritmetica ')'        { Integer ptr = st.getPtr(st.getLexema($1.ival), ambitoActual.copy());
                                                          st.delEntry($1.ival);
                                                          agregarEstructuraLlamados("Invocacion a la funcion ", ptr);}
                     | ID '(' ')'                       { Integer ptr = st.getPtr(st.getLexema($1.ival), ambitoActual.copy());
                                                          st.delEntry($1.ival);
                                                          agregarEstructuraLlamados("Invocacion a la funcion ", ptr);}
                     | ID '(' exp_aritmetica            {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
                     /*| ID exp_aritmetica ')'            {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}*/
                     | ID ')'                           {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
                     | ID '('                           {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
                     ;

inv_metodo           : atributo_clase '(' exp_aritmetica ')' { Integer ptr = st.getPtr(st.getLexema($1.ival), ambitoActual.copy());
                                                               agregarEstructuraLlamados("Invocacion al metodo ", $1.ival);}
                     | atributo_clase '(' ')'                { Integer ptr = st.getPtr(st.getLexema($1.ival), ambitoActual.copy());
                                                               agregarEstructuraLlamados("Invocacion al metodo ", $1.ival);}
                     | atributo_clase '('               {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
                     | atributo_clase ')'               {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
                     ;

atributo_clase       : ID '.' ID                        {$$.ival = agregarAtributo($1.ival, $3.ival, ambitoActual.toString());}
                     | atributo_clase '.' ID             {$$.ival = agregarAtributo($1.ival, $3.ival, ambitoActual.toString());}
                     ;

seleccion_func       : IF condicion bloque_ejecutable_func ELSE bloque_ejecutable_func END_IF ',' {agregarEstructura("IF");}
                     | IF condicion bloque_ejecutable_func END_IF ',' {agregarEstructura("IF");}
                     | IF condicion ELSE bloque_ejecutable_func END_IF {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un bloque de sentencias ejecutables");}
                     | IF condicion bloque_ejecutable_func ELSE END_IF {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un bloque de sentencias ejecutables");}
                     ;

seleccion            : IF condicion cuerpo_then END_IF { agregarEstructura("IF"); 
                                                         completarB("BI", pilaTercetos.getContador()+1); }
                     | IF condicion cuerpo_then ELSE cuerpo_else END_IF { agregarEstructura("IF"); 
                                                                          completarB("BI", pilaTercetos.getContador()+1); }
                     ;

cuerpo_then          : bloque_sen_ejecutable { crearTerceto("BI", -1, -1, "", ""); completarB("BF", pilaTercetos.getContador()+1);  }
                     ;

cuerpo_else          : bloque_sen_ejecutable
                     ;

condicion            : '(' exp_logica ')' { $$.ival = crearTerceto("BF", $2.ival, -1, $2.sval, ""); }
                     ;

exp_logica           : exp_aritmetica comparador exp_aritmetica { $$.ival = crearTerceto(getCmp($2.ival), $1.ival, $3.ival, $1.sval, $3.sval);                                                                  $$.sval = "terceto"; }
                     | exp_aritmetica comparador {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una expresión aritmética");}
                     | comparador exp_aritmetica {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una expresión aritmética");}
                     | exp_aritmetica           {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un comparador");}
                     | comparador               {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba dos expresión aritmética");}
                     ;

                     //Los operandos de las expresiones aritméticas pueden ser variables, constantes, u otras expresiones aritméticas
                     //No se deben permitir anidamientos de expresiones con paréntesis
exp_aritmetica       : exp_aritmetica '+' termino       { $$.ival = crearTerceto("+", $1.ival, $3.ival, $1.sval, $3.sval);
                                                          $$.sval = "terceto"; }
                     | exp_aritmetica '-' termino       { $$.ival = crearTerceto("-", $1.ival, $3.ival, $1.sval, $3.sval);
                                                          $$.sval = "terceto"; }
                     | termino                          { $$ = $1; }
                     | exp_aritmetica '+' '+' termino   {agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
                     | exp_aritmetica '+' '*' termino   {agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
                     | exp_aritmetica '+' '/' termino   {agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
                     | exp_aritmetica '-' '+' termino   {agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
                     | exp_aritmetica '-' '*' termino    {agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
                     | exp_aritmetica '-' '/' termino   {agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
                     ;

termino              : termino '*' factor       { $$.ival = crearTerceto("*", $1.ival, $3.ival, $1.sval, $3.sval); $$.sval = "terceto"; }
                     | termino '/' factor       { $$.ival = crearTerceto("/", $1.ival, $3.ival, $1.sval, $3.sval); $$.sval = "terceto"; }
                     | factor                   { $$ = $1; }
                     | termino '*' '*' factor   {agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
                     | termino '*' '/' factor   {agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
                     | termino '/' '*' factor   {agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
                     | termino '/' '/' factor   {agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
                     ;

factor               : ID                       { $$.ival = st.getPtr(st.getLexema($1.ival), ambitoActual.copy());
                                                  $$.sval = "st";
                                                  st.delEntry($1.ival); $1.ival = $$.ival; }
                     | ID INCREMENT             { $$.ival = st.getPtr(st.getLexema($1.ival), ambitoActual.copy());
                                                  $$.sval = "st";
                                                  st.delEntry($1.ival); $1.ival = $$.ival; }
                     | CTE                      { $$.ival = $1.ival; $$.sval = "st"; }
                     | atributo_clase           { $$.ival = st.getPtr(st.getLexema($1.ival), ambitoActual.copy());
                                                  $$.sval = "st";
                                                  st.delEntry($1.ival); $1.ival = $$.ival; }
                     ;

comparador           : NOT_EQUAL     { $$.ival = NOT_EQUAL; }
                     | EQUAL         { $$.ival = EQUAL; }
                     | GREATER_EQUAL { $$.ival = GREATER_EQUAL; }
                     | LESS_EQUAL    { $$.ival = LESS_EQUAL; }
                     | '<'           { $$.ival = 60; }
                     | '>'           { $$.ival = 62; }
                     ;

                     // una sola sentencia ejecutable o varias encerradas por llave
bloque_sen_ejecutable: sen_ejecutable
                     | '{' sen_ejecutable_r '}'
                     | sen_ejecutable_r '}'      {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al comienzo del bloque de sentencias ejecutables");}
                     | '{' sen_ejecutable_r      {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al final del bloque de sentencias ejecutables");}
                     | '{' '}'                   {agregarError(errores_sintacticos, Parser.ERROR, "Se espera un bloque de sentencias ejecutables");}
                     ;

bloque_ejecutable_func: sen_retorno ','
                      | '{' sen_ejecutable_r sen_retorno ',' '}'
                      | ',' 
                      ;

                     // conjunto sentencias ejecutable
sen_ejecutable_r     : sen_ejecutable_r sen_ejecutable
                     | sen_ejecutable
                     ;

imprimir             : PRINT STR_1LN             {agregarEstructura("PRINT");}
                     | PRINT ','                 {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una cadena para imprimir");}
                     ;

sen_control          : inicio_while condicion DO bloque_sen_ejecutable ',' { agregarEstructura("WHILE");
                                                                             crearTerceto("BI", -1, -1, "", "");
                                                                             completarB("BF", pilaTercetos.getContador()+1);
                                                                             completarWhile(); }
                     | inicio_while condicion DO ','                   {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un bloque de sentencias ejecutables");}
                     ;

inicio_while         : WHILE { inicio_while = pilaTercetos.getContador()+1; }
                     ;

clase                : class_header '{' '}'                   { ambitoActual.pop(); }
                     | class_header '{' cuerpo_clase '}'      { ambitoActual.pop(); }
                     | class_header ','                       { agregarClase($2.ival, "FDCLASS"); ambitoActual.pop(); }
                     | class_header cuerpo_clase '}'       { agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al final de la clase");
                                                             ambitoActual.pop(); }
                     ;

class_header         : CLASS ID { st.setLexema($2.ival, st.getLexema($2.ival) + ":" + ambitoActual.toString());
                                  agregarClase($2.ival, "CLASS"); }
                                  /*Integer ptr = st.upgradeLexema($2.ival, ":"+ambitoActual.toString());*/
                                  /*agregarClase(ptr, "CLASS"); }*/
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
private Stack<Integer> pilaLineas = new Stack<>();

private static boolean errores_compilacion;

void yyerror(String mensaje) {
        // funcion utilizada para imprimir errores que produce yacc
        System.out.println("Error yacc: " + mensaje);
}

int yylex() {
    try {
        int token = lexicalAnalyzer.nextToken();
        yylval = new ParserVal(lexicalAnalyzer.getPtrActual());

        if (token == IF || token == PRINT || token == WHILE || token == CLASS || token == 61 || token == VOID) {
            pilaLineas.push(lexicalAnalyzer.getLine());
        }

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

private Integer getLinea(){
    if (!(pilaLineas.isEmpty())) {
      return pilaLineas.pop();
    } else {
      return -1;
    }
}

public void agregarEstructuraLlamados(String s, Integer ptr) {
    estructuras.add("Linea(" + lexicalAnalyzer.getLine() + "): " + s + st.getLexema(ptr));
}

public void agregarEstructura(String s) {
    estructuras.add("Linea(" + getLinea().toString() + "): " + s);
}

public void agregarEstructura(String s, Integer ptr) {
    estructuras.add("Linea(" + getLinea().toString() + "): " + s + st.getLexema(ptr));
}

public void agregarClase(Integer id, String description) {
    st.setAttribute(id, "uso", description);
    ambitoActual.push(st.getLexema(id).split(":")[0]);
    agregarEstructura(description +" : " + st.getLexema(id));
}

public Integer agregarAtributo(Integer ptr_lhs, Integer ptr_rhs, String ambito) {
    String lexema = st.getLexema(ptr_lhs) + "." + st.getLexema(ptr_rhs) + ":" + ambito;
    Integer ptr = st.addEntry(lexema, ID);
    st.setAttribute(ptr, "uso", "atributo_clase");
    st.delEntry(ptr_rhs);
    st.delEntry(ptr_lhs);
    return ptr;
}

public void resolverSigno(Integer ptr_cte) {
    st.setLexema(ptr_cte, "-" + st.getLexema(ptr_cte));
}

public void verificarRango(Integer ptr_cte) {
    if (ptr_cte == -1) return;
    String lexema = st.getLexema(ptr_cte);
    Short tipo = Short.parseShort(st.getAttribute(ptr_cte,"tipo"));

    switch (tipo) {
        case FLOAT:
            float value_f;
            try {
                value_f = Float.parseFloat(lexema);
            } catch (Exception e) {
                agregarError(errores_sintacticos, Parser.ERROR,
                        "No se pudo convertir a float la constante " + lexema);
                break;
            }
            if (!(value_f <= Float.MAX_VALUE && value_f >= FLOAT_MIN ||
                        value_f >= -Float.MAX_VALUE && value_f <= -FLOAT_MIN || 
                        value_f == 0)){
                agregarError(errores_sintacticos, Parser.ERROR, "Constante FLOAT fuera de rango");
            }
            break;
        case SHORT:
            short value_s;
            try {
                value_s = Short.parseShort(lexema);
            } catch (Exception e) {
                agregarError(errores_sintacticos, Parser.ERROR,
                        "No se pudo convertir a shortint la constante " + lexema);
                break;
            }
            if (!(value_s <= Short.MAX_VALUE && value_s >= Short.MIN_VALUE)){
                agregarError(errores_sintacticos, Parser.ERROR, "Constante ShortInt fuera de rango");
            }
            break;
    }
}

public void agregarFuncion(Integer id, Short tipo, Integer parametro) {
    st.setAttribute(id, "uso", "FUNCTION");
    st.setAttribute(id, "tipo", ""+tipo);
    if (parametro != -1) {
      st.setAttribute(id, "parameter", parametro.toString());
    }
    
    ambitoActual.push(st.getLexema(id).split(":")[0]);
    agregarEstructura("FUNCTION :" + st.getLexema(id));
}

public void agregarParametro(Integer id, Integer tipo){
    st.setAttribute(id, "uso", "PARAMETER");
    st.setAttribute(id, "tipo", tipo.toString());
}

public Integer crearTerceto(String op, Integer lhs, Integer rhs, String tipoLhs, String tipoRhs) {
    Terceto terceto = new Terceto(op, lhs, rhs, tipoLhs == "terceto", tipoRhs == "terceto");
    pilaTercetos.apilar(terceto);
    return pilaTercetos.getContador();
}

public String getCmp(Integer cmpID) {
    switch (cmpID) {
        case 273: //GREATER_EQUAL
            return ">=";
        case 274: //LESS_EQUAL
            return "<=";
        case 275: //EQUAL
            return "==";
        case 276: //NOT_EQUAL
            return "!=";
        case 60: //'<'
            return "<";
        case 62: //'>'
            return ">";
        default:
            return "";
    }
}

public void completarB(String b, Integer tercetoActual) {
    PilaTercetos aux = new PilaTercetos();
    Terceto terceto;

    while (pilaTercetos.getContador() > 0) {
        terceto = pilaTercetos.pop();
        if (terceto.getOperador().equals(b.toString()) && terceto.getOperando2() == -1) {
            if (b.equals("BI"))
                terceto.setOperando1(tercetoActual, true);
             else
                terceto.setOperando2(tercetoActual, true);
            pilaTercetos.apilar(terceto);
            break;
        }
        aux.apilar(terceto);
    }

    while (aux.getContador() > 0) {
        pilaTercetos.apilar(aux.pop());
    }
}

public void completarWhile() {
    Terceto bi = pilaTercetos.pop();
    bi.setOperando1(inicio_while, true);
    pilaTercetos.apilar(bi);
}

/*public void completarWhile() {*/
    /*PilaTercetos aux = new PilaTercetos();*/
    /*Terceto terceto;*/
    /*Terceto bi = pilaTercetos.pop();*/
    /*Integer terceActual = pilaTercetos.getContador();*/

    /*while (pilaTercetos.getContador() > 0) {*/
        /*terceto = pilaTercetos.pop();*/
        /*if (terceto.getOperador().equals("BF") && terceto.getOperando2() == -1)*/
            /*terceto.setOperando2(terceActual, true);*/

        /*else if (terceto.getOperador().equals("WHILE")) {*/
            /*bi.setOperando1(pilaTercetos.getContador()+1, true);*/
            /*break;*/
        /*}*/
        /*aux.apilar(terceto);*/
    /*}*/

    /*while (aux.getContador() > 0) {*/
        /*pilaTercetos.apilar(aux.pop());*/
    /*}*/

    /*pilaTercetos.apilar(bi);*/
/*}*/

LexicalAnalyzer lexicalAnalyzer;
SymbolTable st;
PilaTercetos pilaTercetos;
Integer inicio_while, tipo;
Ambito ambitoActual = new Ambito("global");

public static void main(String[] args) {
    TransitionMatrix<Integer> mI = new TransitionMatrix<>(19, 28);
    TransitionMatrix<AccionSemantica> mA = new TransitionMatrix<>(19, 28);
    SymbolTable sttemp = new SymbolTable();

    FuncionesAuxiliares.loadMatrixs(mI, mA, "test.csv", sttemp, errores_lexicos);
    Parser parser = new Parser(new LexicalAnalyzer(args[0], mI, mA, errores_lexicos), sttemp);
    parser.run();
    
    Parser.imprimirErrores(errores_lexicos, "Errores Lexicos");
    Parser.imprimirErrores(errores_sintacticos, "Errores Sintacticos");
    Parser.imprimirErrores(estructuras, "Estructuras Sintacticas");
    
    parser.st.print();
    parser.pilaTercetos.print();
    System.out.println("Ambito actual: " + parser.ambitoActual.toString());
}

public Parser(LexicalAnalyzer lexicalAnalyzer, SymbolTable st) {
    this.lexicalAnalyzer = lexicalAnalyzer;
    this.st = st;
    this.pilaTercetos = new PilaTercetos();
    //yydebug = true;
}
