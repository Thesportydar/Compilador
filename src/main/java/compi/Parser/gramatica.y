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
                     | ID    { $$.ival = getTipoClase($1.ival, ambitoActual.copy()); st.delEntry($1.ival); $1.ival = $$.ival; tipo = $$.ival; }
                     ;

CTE                  : CTE_SHORT     {verificarRango($1.ival); $$.ival = $1.ival; $$.dval = SHORT;}
                     | CTE_UINT      {$$.ival = $1.ival; $$.dval = UINT;}
                     | CTE_FLOAT     {verificarRango($1.ival); $$.ival = $1.ival; $$.dval = FLOAT;}
                     | '-' CTE_SHORT {resolverSigno($2.ival); verificarRango($2.ival); $$.ival = $2.ival;}
                     | '-' CTE_FLOAT {resolverSigno($2.ival); verificarRango($2.ival); $$.ival = $2.ival;}
                     | '-' CTE_UINT { agregarError(errores_sintacticos, Parser.ERROR, "No se puede negar un unsigned int");}
                     ;

list_var             : list_var ';' ID {if (tipo != 0) {
                                            if (!verificarDeclaracion($3.ival)) {
                                                st.delEntry($3.ival);
                                            } else {
                                                st.setAttribute($3.ival, "tipo", tipo.toString());
                                                st.setLexema($3.ival, st.getLexema($3.ival) + ":" + ambitoActual.toString());
                                            }
                                        } else{
                                            agregarError(errores_semanticos, Parser.ERROR, String.format(ERROR_TIPO, st.getLexema($3.ival)));
                                            st.delEntry($3.ival);
                                        }}
                     | ID              {if (tipo != 0) {
                                            if (!verificarDeclaracion($1.ival)) {
                                                st.delEntry($1.ival);
                                            } else {
                                                st.setAttribute($1.ival, "tipo", tipo.toString());
                                                st.setLexema($1.ival, st.getLexema($1.ival) + ":" + ambitoActual.toString());
                                            }
                                        } else{
                                            agregarError(errores_semanticos, Parser.ERROR, String.format(ERROR_TIPO, st.getLexema($1.ival)));
                                            st.delEntry($1.ival);
                                        }}
                     ;

funcion              : header_funcion '{' cuerpo_funcion '}' { ambitoActual.pop(); }
funcion              : header_funcion cuerpo_funcion '}' {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al comienzo de la función");}
                     ;

header_funcion       : VOID ID '(' parametro ')' {  if (!verificarDeclaracion($2.ival)) {
                                                        st.delEntry($2.ival);
                                                    } else {
                                                        st.setLexema($2.ival, st.getLexema($2.ival) + ":" + ambitoActual.toString());
                                                        agregarFuncion($2.ival, VOID, $4.ival);
                                                        st.setLexema($4.ival, st.getLexema($4.ival) + ":" + ambitoActual.toString());
                                                    }
                                                 }
                     | VOID ID '(' ')'           {  if (!verificarDeclaracion($2.ival)) {
                                                        st.delEntry($2.ival);
                                                    } else {
                                                        st.setLexema($2.ival, st.getLexema($2.ival) + ":" + ambitoActual.toString());
                                                        agregarFuncion($2.ival, VOID, -1); 
                                                    }
                                                 }
                     ;

parametro            : tipo ID              {   if (tipo != 0) {
                                                    agregarParametro($2.ival, $1.ival);
                                                    $$.ival = $2.ival;
                                                } else {
                                                    agregarError(errores_semanticos, Parser.ERROR, String.format(ERROR_TIPO, st.getLexema($2.ival)));
                                                    st.delEntry($1.ival);
                                                    st.delEntry($2.ival);
                                                }
                                            }
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

asignacion           : ID '=' exp_aritmetica                { $$.ival = crearTercetoAsignacion($1.ival, $3); st.delEntry($1.ival); }
                     | atributo_clase '=' exp_aritmetica    { $$.ival = crearTercetoAsignacion($1.ival, $3); }
                     | ID '='                           {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una expresión aritmética");}
                     | '=' exp_aritmetica               {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un identificador");}
                     /*| ID exp_aritmetica                {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un signo igual");}*/
                     | atributo_clase '='               {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una expresión aritmética");}
                     | atributo_clase exp_aritmetica    {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un signo igual");}
                     ;

inv_funcion          : ID '(' exp_aritmetica ')'        { invocacionFuncion($1.ival, $3); st.delEntry($1.ival); $1.ival = $$.ival; }
                     | ID '(' ')'                       { invocacionFuncion($1.ival); st.delEntry($1.ival); $1.ival = $$.ival; }
                     | ID '(' exp_aritmetica            {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
                     /*| ID exp_aritmetica ')'            {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}*/
                     | ID ')'                           {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
                     | ID '('                           {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
                     ;

inv_metodo           : atributo_clase '(' exp_aritmetica ')'{  if ($1.ival != 0)
                                                                    if ($3.ival != 0)
                                                                        agregarEstructuraLlamados("Invocacion al metodo ", $1.ival);
                                                            }
                     | atributo_clase '(' ')'               {  if ($1.ival != 0)
                                                                    agregarEstructuraLlamados("Invocacion al metodo ", $1.ival);
                                                            }
                     | atributo_clase '('               {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
                     | atributo_clase ')'               {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
                     ;

atributo_clase       : ID '.' ID                        { $$.ival = agregarAtributo($1.ival, $3.ival, ambitoActual.copy());
                                                          st.delEntry($1.ival); $1.ival = $$.ival;
                                                          st.delEntry($3.ival); $3.ival = $$.ival;}
                     | atributo_clase '.' ID             { $$.ival = agregarAtributo($1.ival, $3.ival, ambitoActual.copy());
                                                           st.delEntry($3.ival); $3.ival = $$.ival;}
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

condicion            : '(' exp_logica ')' { $$.ival = crearTerceto("BF", $2.ival, -1, $2.sval, ""); } //verificar null
                     ;

exp_logica           : exp_aritmetica comparador exp_aritmetica {   if ($1.ival != 0 && $3.ival != 0) {
                                                                        if ($1.dval == $3.dval) {
                                                                            $$.ival = crearTerceto(getCmp($2.ival), $1.ival, $3.ival, $1.sval, $3.sval);
                                                                            $$.sval = "terceto";
                                                                        } else
                                                                            agregarError(errores_semanticos, Parser.ERROR,
                                                                                String.format(ERROR_TIPOS_INCOMPATIBLES, $1.sval, $3.sval));
                                                                    }
                                                                }
                     | exp_aritmetica comparador {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una expresión aritmética");}
                     | comparador exp_aritmetica {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una expresión aritmética");}
                     | exp_aritmetica           {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un comparador");}
                     | comparador               {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba dos expresión aritmética");}
                     ;

                     //Los operandos de las expresiones aritméticas pueden ser variables, constantes, u otras expresiones aritméticas
                     //No se deben permitir anidamientos de expresiones con paréntesis
exp_aritmetica       : exp_aritmetica '+' termino       { $$.ival = crearTercetoExp($1, $3, "+"); $$.sval = "terceto"; }
                     | exp_aritmetica '-' termino       { $$.ival = crearTercetoExp($1, $3, "-"); $$.sval = "terceto"; }
                     | termino                          { $$ = $1; }
                     | exp_aritmetica '+' '+' termino   {agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
                     | exp_aritmetica '+' '*' termino   {agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
                     | exp_aritmetica '+' '/' termino   {agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
                     | exp_aritmetica '-' '+' termino   {agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
                     | exp_aritmetica '-' '*' termino    {agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
                     | exp_aritmetica '-' '/' termino   {agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
                     ;

termino              : termino '*' factor       { $$.ival = crearTercetoTermino($1, $3, "*"); $$.sval = "terceto"; if ($$.ival != 0) $$.dval = $1.dval; };
                     | termino '/' factor       { $$.ival = crearTercetoTermino($1, $3, "/"); $$.sval = "terceto"; if ($$.ival != 0) $$.dval = $1.dval; };
                     | factor                   { $$ = $1; }
                     | termino '*' '*' factor   {agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
                     | termino '*' '/' factor   {agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
                     | termino '/' '*' factor   {agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
                     | termino '/' '/' factor   {agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
                     ;


factor               : ID                       {   $$.ival = st.getPtr(st.getLexema($1.ival), ambitoActual.copy());
                                                    if ($$.ival != 0) {
                                                        $$.sval = "st";
                                                        $$.dval = Integer.parseInt(st.getAttribute($$.ival, "tipo")); //chequear
                                                        if (!st.getLexema($1.ival).contains(":")) {
                                                            st.delEntry($1.ival);
                                                            $1.ival = $$.ival;
                                                        }
                                                    } else
                                                        agregarError(errores_semanticos, Parser.ERROR,
                                                            String.format(ERROR_ALCANCE, st.getLexema($1.ival), ambitoActual.toString()));
                                                }
                     | ID INCREMENT             { $$.ival = st.getPtr(st.getLexema($1.ival), ambitoActual.copy()); // crearTerceto
                                                  $$.sval = "st";
                                                  $$.dval = Integer.parseInt(st.getAttribute($$.ival, "tipo"));
                                                  st.delEntry($1.ival); $1.ival = $$.ival; }
                     | CTE                      { $$.ival = $1.ival; $$.sval = "st"; $$.dval = $1.dval; }
                     | atributo_clase           {   $$.ival = st.getPtr(st.getLexema($1.ival), ambitoActual.copy());
                                                    if ($$.ival != 0) {
                                                        $$.sval = "st";
                                                        $$.dval = Integer.parseInt(st.getAttribute($$.ival, "tipo"));
                                                    }
                                                }
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

class_header         : CLASS ID             {   if (!verificarDeclaracion($2.ival))
                                                    st.delEntry($2.ival);
                                                else {
                                                    st.setLexema($2.ival, st.getLexema($2.ival) + ":" + ambitoActual.toString());
                                                    agregarClase($2.ival, "CLASS");
                                                }
                                            }
                     ;

cuerpo_clase         : cuerpo_clase sen_declarativa
                     | sen_declarativa // deberia llevar ',' para funciones
                     ;

%%
public static final String ERROR = "Error";
public static final List<String> errores_lexicos = new ArrayList<>();
public static final List<String> errores_sintacticos = new ArrayList<>();
public static final List<String> errores_semanticos = new ArrayList<>();
public static final List<String> estructuras = new ArrayList<>();
public static final float FLOAT_MIN = 1.17549435E-38f;
private Stack<Integer> pilaLineas = new Stack<>();

private static String ERROR_ALCANCE = "El identificador %s no esta al alcance del ambito %s";
private static String ERROR_ATRIBUTO = "El identificador %s no tiene un atributo %s";
private static String ERROR_ST = "El identificador %s no se encuentra en la tabla de simbolos";
private static String ERROR_CLASE_NO_DECLARADA = "El tipo del identificador %s no esta declarado (ptr:%d)";
private static String ERROR_CLASE = "El identificador %s no es una clase";
private static String ERROR_FUNCION = "El identificador %s no es una funcion";
private static String ERROR_TIPO = "No se puede declarar la variable %s porque el tipo no esta declarado";
private static String ERROR_REDECLARACION = "Redeclaracion del identificador %s en el ambito %s";
private static String ERROR_TIPOS_INCOMPATIBLES = "Los tipos %s y %s no son compatibles";
private static String ERROR_PARAMETRO = "La funcion %s esperaba un parametro de tipo %s";

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

public boolean verificarDeclaracion(Integer id) {
    Integer ptr = st.getPtr(st.getLexema(id) +":" + ambitoActual.toString());
    if (ptr !=0 ) {
        agregarError(errores_sintacticos, Parser.ERROR,
            String.format(ERROR_REDECLARACION, st.getLexema(id), ambitoActual.toString()));
        return false;
    }
    return true;
}

public Integer getTipoClase(Integer id, Ambito ambito) {
    //id ptr a class1
    String lexema = st.getLexema(id); // class1
    // ver si esta al alcance
    Integer ptr = st.getPtr(lexema, ambito.copy());
    if (ptr == 0) {
        agregarError(errores_sintacticos, Parser.ERROR,
                String.format(ERROR_ALCANCE, lexema, ambito.toString()));
        return 0;
    }
    // ver si es tiene uso = CLASS
    String uso = st.getAttribute(ptr, "uso");
    if (uso == null) {
        agregarError(errores_sintacticos, Parser.ERROR,
                String.format(ERROR_ATRIBUTO, lexema, "uso"));
        return 0;
    } else if (!uso.equals("CLASS")) {
        agregarError(errores_sintacticos, Parser.ERROR,
                String.format(ERROR_CLASE, lexema));
        return 0;
    }
    return ptr;
}

public Integer agregarAtributo(Integer ptr_lhs, Integer ptr_rhs, Ambito ambito) {
    // ptr_lhs puede ser un id o un atributo_clase, eg: c1:global:main, c1, var1
    // ptr_rhs es el lexema de un id detectado por lex, eg:var1, obj2
    // primero hay que detectar que ptr_lhs este al alcance
    ptr_lhs = st.getPtr(st.getLexema(ptr_lhs), ambito.copy());
    if (ptr_lhs == 0) {
        agregarError(errores_sintacticos, Parser.ERROR,
                String.format(ERROR_ALCANCE, st.getLexema(ptr_lhs), ambito.toString()));
        return 0;
    }
    // verificamos si ptr_lhs es de tipo de alguna clase
    String tipo = st.getAttribute(ptr_lhs, "tipo");
    if (tipo == null) {
        agregarError(errores_sintacticos, Parser.ERROR,
                String.format(ERROR_ATRIBUTO, st.getLexema(ptr_lhs), "tipo"));
        return 0;
    // para las instancias, tipo es un puntero a una entrada de la tabla de simbolos
    } else if (!st.contains(Integer.parseInt(tipo))) {
        agregarError(errores_sintacticos, Parser.ERROR,
                String.format(ERROR_CLASE_NO_DECLARADA, st.getLexema(ptr_lhs), Integer.parseInt(tipo)));
        return 0;
    }
    // esta entrada tiene como atributo uso = CLASS
    if (!st.getAttribute(Integer.parseInt(tipo), "uso").equals("CLASS")) {
        agregarError(errores_sintacticos, Parser.ERROR,
                String.format(ERROR_CLASE, st.getLexema(Integer.parseInt(tipo))));
        return 0;
    }
    // obtenemos el lexema de la clase, el cual es class1:global:main
    String clase = st.getLexema(Integer.parseInt(tipo));
    System.out.println("clase: " + clase);
    String[] partes = clase.split(":");
    // creamos un ambito partes[1:] + partes[0]
    Ambito ambito_rhs = new Ambito();
    for (int i = 1; i < partes.length; i++)
        ambito_rhs.push(partes[i]);
    ambito_rhs.push(partes[0]);
    System.out.println("ambito_rhs: " + ambito_rhs.toString());
    // el resultado es global:main:class1
    // a partir de este ambito hay que buscar ptr_rhs
    Integer ptr_rhs2 = st.getPtr(st.getLexema(ptr_rhs), ambito_rhs.copy());
    if (ptr_rhs2 == 0) {
        agregarError(errores_sintacticos, Parser.ERROR,
                String.format(ERROR_ALCANCE, st.getLexema(ptr_rhs), ambito_rhs.toString()));
        return 0;
    }
    st.setAttribute(ptr_rhs2, "uso", "atributo_clase");
    return ptr_rhs2;
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

public void invocacionFuncion(Integer id, ParserVal param) {
    if (id == 0) {
        agregarError(errores_semanticos, Parser.ERROR,
                String.format(ERROR_ST, st.getLexema(id)));
        return;
    }
    Integer ptr = st.getPtr(st.getLexema(id), ambitoActual.copy());
    if (ptr == 0) {
        agregarError(errores_semanticos, Parser.ERROR,
                String.format(ERROR_ALCANCE, st.getLexema(id), ambitoActual.toString()));
        return;
    }
    if (!st.getAttribute(ptr, "uso").equals("FUNCTION")) {
        agregarError(errores_semanticos, Parser.ERROR,
                String.format(ERROR_FUNCION, st.getLexema(ptr)));
        return;
    }
    if (param != null && param.ival == 0)
        return;
    
    Integer param_formal = Integer.parseInt(st.getAttribute(ptr, "parameter"));
    if (param == null && param_formal != null ||
            Integer.parseInt(st.getAttribute(param_formal, "tipo")) != param.dval) {
        agregarError(errores_semanticos, Parser.ERROR,
                String.format(ERROR_PARAMETRO, st.getLexema(ptr),
                    st.getAttribute(param_formal, "tipo")));
        return;
    }
    agregarEstructuraLlamados("Invocacion a la funcion ", ptr);
}

public void invocacionFuncion(Integer id) {
    invocacionFuncion(id, null);
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

public String getTipo(Integer tipo) {
    String t = this.lexicalAnalyzer.getReservedWord(tipo);
    if (t == null)
        // es un tipo declarado, buscarlo en la st
        t = st.getLexema(tipo).split(":")[0];
    return t;
}

public Integer crearTercetoExp(ParserVal lhs, ParserVal rhs, String op) {
    if (lhs.ival != 0 && rhs.ival != 0) {
        if (lhs.dval == rhs.dval)
            return crearTerceto(op, lhs.ival, rhs.ival, lhs.sval, rhs.sval);
        else {
            agregarError(errores_semanticos, Parser.ERROR,
                String.format(ERROR_TIPOS_INCOMPATIBLES, getTipo((int)lhs.dval), getTipo((int)rhs.dval)));
            return 0;
        }
    } else
        return 0;
}

public Integer crearTercetoTermino(ParserVal lhs, ParserVal rhs, String op) {
    if (lhs.ival != 0 && rhs.ival != 0) {
        if (lhs.dval == rhs.dval)
            return crearTerceto("*", lhs.ival, rhs.ival, lhs.sval, rhs.sval);
        else {
            agregarError(errores_semanticos, Parser.ERROR,
                String.format(ERROR_TIPOS_INCOMPATIBLES, getTipo((int)lhs.dval), getTipo((int)rhs.dval)));
            return 0;
        }
    } else
        return 0;
}

public Integer crearTercetoAsignacion(Integer lhs, ParserVal rhs) {
    if (lhs == 0) {
        agregarError(errores_semanticos, Parser.ERROR,
                String.format(ERROR_ST, st.getLexema(lhs)));
        return 0;
    }
    Integer ptr = st.getPtr(st.getLexema(lhs), ambitoActual.copy());
    if (ptr == 0) {
        agregarError(errores_semanticos, Parser.ERROR,
                String.format(ERROR_ALCANCE, st.getLexema(lhs), ambitoActual.toString()));
        return 0;
    }
    if (rhs.ival == 0) return 0;

    Integer tipo_lhs = Integer.parseInt(st.getAttribute(ptr, "tipo"));//supongo que id tiene tipo, TODO: no suponer
    if (tipo_lhs != rhs.dval) { 
        agregarError(errores_semanticos, Parser.ERROR,
                String.format(ERROR_TIPOS_INCOMPATIBLES, getTipo(tipo_lhs), getTipo((int)rhs.dval)));
        return 0;
    }
    agregarEstructura("Asignacion al identificador ", ptr);
    return crearTerceto("=", ptr, rhs.ival, "st", rhs.sval);
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
    Parser.imprimirErrores(errores_semanticos, "Errores Semanticos");
    
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
