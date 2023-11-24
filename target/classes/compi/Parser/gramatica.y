%{
package compi.Parser;
import compi.AccionesSemanticas.AccionSemantica;
import compi.AssemblyGenerator.AssemblyGenerator;
import compi.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
%}

%token ID IF ELSE END_IF PRINT CLASS VOID WHILE DO SHORT UINT FLOAT
       CTE_SHORT CTE_UINT CTE_FLOAT INCREMENT GREATER_EQUAL LESS_EQUAL EQUAL NOT_EQUAL STR_1LN RETURN CHECK

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

sen_declarativa      : tipo list_var ',' { if (declarandoInstancia) declarandoInstancia = false; }
                     | sentencia_check tipo list_var ',' { check=false; if (declarandoInstancia) declarandoInstancia = false; }
                     | tipo list_var             {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una ,");}
                     | funcion
                     | clase
                     ;

sentencia_check      : CHECK { check=true; }
                     ;

tipo                 : SHORT { $$.ival = SHORT; tipo = (int)SHORT; }
                     | UINT  { $$.ival = UINT; tipo = (int)UINT; }
                     | FLOAT { $$.ival = FLOAT; tipo = (int)FLOAT; }
                     | ID    { $$.ival = getTipoClase($1.ival, ambitoActual.copy()); tipo = $$.ival; declarandoInstancia = true; }
                     ;

CTE                  : CTE_SHORT     {verificarRango($1.ival); $$.ival = $1.ival; $$.dval = SHORT;}
                     | CTE_UINT      {$$.ival = $1.ival; $$.dval = UINT;}
                     | CTE_FLOAT     {verificarRango($1.ival); $$.ival = $1.ival; $$.dval = FLOAT;}
                     | '-' CTE_SHORT {resolverSigno($2.ival); verificarRango($2.ival); $$.ival = $2.ival; $$.dval = SHORT; }
                     | '-' CTE_FLOAT {resolverSigno($2.ival); verificarRango($2.ival); $$.ival = $2.ival; $$.dval = FLOAT; }
                     | '-' CTE_UINT { agregarError(errores_sintacticos, Parser.ERROR, "No se puede negar un unsigned int");}
                     ;

list_var             : list_var ';' ID { declararVariable($3.ival); }
                     | ID               { declararVariable($1.ival); }
                     ;

funcion              : header_funcion '{' cuerpo_funcion sen_retorno '}' { ambitoActual.pop(); crearTerceto("RET", -1, -1, null, null); }
                     | header_funcion '{' cuerpo_funcion seleccion_func '}' { ambitoActual.pop(); crearTerceto("RET", -1, -1, null, null); }
                     | header_funcion cuerpo_funcion '}' {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al comienzo de la función");}
                     ;

header_funcion       : VOID ID '(' parametro ')' { agregarFuncion($2.ival, VOID, $4.ival);
                                                    if ($4.ival != 0)
                                                        st.setLexema($4.ival, st.getLexema($4.ival) + ":" + ambitoActual.toString());
                                                 }
                     | VOID ID '(' ')'           { agregarFuncion($2.ival, VOID, null); }
                     ;

parametro            : tipo ID              {   if (tipo == 0) {
                                                    agregarError(errores_semanticos, Parser.ERROR,
                                                            String.format(ERROR_TIPO, st.getLexema($2.ival)));
                                                    $$.ival = 0;
                                                    break;
                                                }
                                                st.setAttribute($2.ival, "tipo", ""+$1.ival);
                                                $$.ival = $2.ival;
                                            }
                     ;

cuerpo_funcion       : cuerpo_funcion sen_declarativa
                     | cuerpo_funcion sen_ejecutable
                     | sen_declarativa
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
                     ;

asignacion           : ID '=' exp_aritmetica                { $$.ival = crearTercetoAsignacion($1.ival, $3); $$.sval = "terceto"; if ($$.ival != 0) $$.dval = $1.dval; }
                     | atributo_clase '=' exp_aritmetica    { $$.ival = crearTercetoAsignacion($1.ival, $3); $$.sval = "terceto"; if ($$.ival != 0) $$.dval = $1.dval; }
                     | ID '='                           {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una expresión aritmética");}
                     | '=' exp_aritmetica               {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un identificador");}
                     /*| ID exp_aritmetica                {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un signo igual");}*/
                     | atributo_clase '='               {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una expresión aritmética");}
                     | atributo_clase exp_aritmetica    {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un signo igual");}
                     ;

inv_funcion          : ID '(' exp_aritmetica ')'        { invocacionFuncion($1.ival, $3); }
                     | ID '(' ')'                       { invocacionFuncion($1.ival); }
                     | ID '(' exp_aritmetica            {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
                     /*| ID exp_aritmetica ')'            {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}*/
                     | ID ')'                           {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
                     | ID '('                           {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
                     ;

inv_metodo           : atributo_clase '(' exp_aritmetica ')'{ if ($1.ival != 0) invocacionFuncion($1.ival, $3); }
                     | atributo_clase '(' ')'               { if ($1.ival != 0) invocacionFuncion($1.ival); }
                     | atributo_clase '('               {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
                     | atributo_clase ')'               {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
                     ;

atributo_clase       : ID '.' ID                        { $$.ival = agregarAtributo($1.ival, $3.ival, ambitoActual.copy()); }
                     | atributo_clase '.' ID             { $$.ival = agregarAtributo($1.ival, $3.ival, ambitoActual.copy()); }
                     ;

seleccion_func       : IF condicion bloque_ejecutable_func ELSE bloque_ejecutable_func END_IF ',' {agregarEstructura("IF");}
                     | IF condicion bloque_ejecutable_func END_IF ',' {agregarEstructura("IF");}
                     | IF condicion ELSE bloque_ejecutable_func END_IF {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un bloque de sentencias ejecutables");}
                     | IF condicion bloque_ejecutable_func ELSE END_IF {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un bloque de sentencias ejecutables");}
                     ;

seleccion            : IF condicion cuerpo_then END_IF { agregarEstructura("IF");
                                                         completarB("BI", pilaTercetos.getContador()); }
                     | IF condicion cuerpo_then ELSE cuerpo_else END_IF { agregarEstructura("IF"); 
                                                                          completarB("BI", pilaTercetos.getContador()+1);
                                                                          crearTerceto("END_IF"+countIF++, -1, -1, "", ""); }
                     ;

cuerpo_then          : bloque_sen_ejecutable { crearTerceto("BI", -1, -1, "", "");
                                               completarB("BF", pilaTercetos.getContador()+1);
                                               crearTerceto("END_IF"+countIF++, -1, -1, "", ""); }
                     ;

cuerpo_else          : bloque_sen_ejecutable
                     ;

condicion            : '(' exp_logica ')' { $$.ival = crearTerceto("BF", $2.ival, -1, $2.sval, ""); } //verificar null
                     ;

exp_logica           : exp_aritmetica comparador exp_aritmetica {   if ($1.ival == 0 || $3.ival == 0) break;

                                                                    if ($1.dval != $3.dval) {
                                                                        agregarError(errores_semanticos, Parser.ERROR,
                                                                            String.format(ERROR_TIPOS_INCOMPATIBLES, $1.sval, $3.sval));
                                                                        break;
                                                                    }
                                                                    $$.ival = crearTerceto(getCmp($2.ival), $1.ival, $3.ival, $1.sval, $3.sval);
                                                                    $$.sval = "terceto";
                                                                }
                     | exp_aritmetica comparador {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una expresión aritmética");}
                     | comparador exp_aritmetica {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una expresión aritmética");}
                     | exp_aritmetica           {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un comparador");}
                     | comparador               {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba dos expresión aritmética");}
                     ;

                     //Los operandos de las expresiones aritméticas pueden ser variables, constantes, u otras expresiones aritméticas
                     //No se deben permitir anidamientos de expresiones con paréntesis
exp_aritmetica       : exp_aritmetica '+' termino       { $$.ival = crearTercetoExp($1, $3, "+"); $$.sval = "terceto"; if ($$.ival != 0) $$.dval = $1.dval; }
                     | exp_aritmetica '-' termino       { $$.ival = crearTercetoExp($1, $3, "-"); $$.sval = "terceto"; if ($$.ival != 0) $$.dval = $1.dval; }
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


factor               : ID                       {   $$.ival = st.getPtr(st.getLexema($1.ival), ambitoActual.copy(), "identificador");
                                                    if ($$.ival == 0) {
                                                        agregarError(errores_semanticos, Parser.ERROR,
                                                            String.format(ERROR_ALCANCE, st.getLexema($1.ival), ambitoActual.toString()));
                                                        break;
                                                    }
                                                    String check_lhs = st.getAttribute($$.ival,"check_lhs");
                                                    String check_rhs = st.getAttribute($$.ival,"check_rhs");
                                                    if (check_rhs != null && check_rhs.equals("false")){
                                                        st.setAttribute($$.ival, "check_rhs", "true");
                                                        if (check_lhs.equals("true")) {
                                                          agregarEstructura(String.format(ESTRUCTURA_CHECK,st.getLexema($$.ival)));
                                                        }
                                                    }
                                                    $$.sval = "st";
                                                    $$.dval = Integer.parseInt(st.getAttribute($$.ival, "tipo")); //chequear
                                                }
                     | ID INCREMENT             { $$ = crearTercetoIncrement($1.ival); }
                     | CTE                      { $$.ival = $1.ival; $$.sval = "st"; $$.dval = $1.dval; }
                     | atributo_clase           {   $$.ival = st.getPtr(st.getLexema($1.ival), ambitoActual.copy());
                                                    if ($$.ival != 0) {
                                                        $$.sval = "st";
                                                        $$.dval = Integer.parseInt(st.getAttribute($$.ival, "tipo"));
                                                        String check_lhs = st.getAttribute($$.ival,"check_lhs");
                                                        String check_rhs = st.getAttribute($$.ival,"check_rhs");
                                                        if (check_rhs != null && check_rhs.equals("false")){
                                                            st.setAttribute($$.ival, "check_rhs", "true");
                                                            if (check_lhs.equals("true")) {
                                                                agregarEstructura(String.format(ESTRUCTURA_CHECK,st.getLexema($$.ival)));
                                                            }
                                                        }
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

bloque_ejecutable_func: sen_retorno
                      | '{' sen_ejecutable_r sen_retorno '}'
                      | ',' 
                      ;

                     // conjunto sentencias ejecutable
sen_ejecutable_r     : sen_ejecutable_r sen_ejecutable
                     | sen_ejecutable
                     ;

imprimir             : PRINT STR_1LN             { crearTerceto("PRINT", $2.ival, -1, "st", null);
                                                   st.setAttribute($2.ival, "valid", "1"); }
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

clase                : class_header '{' '}'                   { ambitoActual.pop(); claseActual = null; }
                     | class_header '{' cuerpo_clase '}'      { ambitoActual.pop(); claseActual = null; }
                     | class_header '{' herencia_clase '}'      { ambitoActual.pop(); claseActual = null; }
                     | CLASS ID ','                       { agregarClase($2.ival, "FDCLASS"); ambitoActual.pop(); claseActual = null; }
                     | class_header cuerpo_clase '}'       { agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al final de la clase");
                                                             ambitoActual.pop(); }
                     ;

class_header         : CLASS ID             { agregarClase($2.ival, "CLASS"); }
                     ;

cuerpo_clase         : cuerpo_clase sen_declarativa
                     | cuerpo_clase herencia_clase
                     | sen_declarativa
                     ;

// herencia por composicion
herencia_clase       : ID ',' { heredar($1.ival); }
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
private static String ERROR_HERENCIA = "La clase %s ya hereda de la clase %s";
private static String ERROR_CONTROL_HERENCIA = "Se excedio el limite de herencia con la clase %s";
private static String ESTRUCTURA_CHECK = "Identificador %s fue usado en el lado derecho y tambien 2+ veces en el lado izquierdo";

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

public void declararVariable(Integer ptr) {
    if (tipo == 0) {
        agregarError(errores_semanticos, Parser.ERROR,
                String.format(ERROR_TIPO, st.getLexema(ptr)));
        return;
    }
    Integer aux = 0;
    // las declaraciones dentro de una clase deben tener mas consideraciones
    if (claseActual != null)
        aux = obtenerAtributo(claseActual, ptr, null);
    if (aux == 0)
        aux = st.getPtr(st.getLexema(ptr) + ":" + ambitoActual.toString());

    if (aux != 0) {
        agregarError(errores_semanticos, Parser.ERROR, String.format(ERROR_REDECLARACION, st.getLexema(ptr), ambitoActual.toString()));
        return;
    }
    st.setAttribute(ptr, "tipo", tipo.toString());
    st.setAttribute(ptr, "valid", "1");
    st.setLexema(ptr, st.getLexema(ptr) + ":" + ambitoActual.toString());
    if (check) {
        st.setAttribute(ptr, "check_lhs", "false");
        st.setAttribute(ptr, "check_rhs", "false");
    }
    if (claseActual != null) { // agregarlo a la lista de atributos de la clase}
        String attList = st.getAttribute(claseActual, "attList");
        if (attList == null)
            attList = "";
        else
            attList += ",";
        attList += ptr.toString();
        st.setAttribute(claseActual, "attList", attList);
    }
    if (declarandoInstancia) {
        String attList = st.getAttribute(tipo, "attList");
        // declrar cada uno de los atributos como lexema.attx:ambito    
        if (attList != null) {
            String[] atts_cls = attList.split(","); // 3,4,6,8
            for (String att_cls: atts_cls) {
                String att0 = st.getLexema(Integer.parseInt(att_cls)).split(":")[0]; // var1:global:class1 -> var1
                String[] instance_parts = st.getLexema(ptr).split(":");

                String instance_att = instance_parts[0] + "." + att0; // c1.var1
                for (int i = 1; i < instance_parts.length; i++)
                    instance_att += ":" + instance_parts[i]; // c1.var1:global:main
                
                ptr = st.addEntry(instance_att, ID);
                st.setAttribute(ptr, "tipo", st.getAttribute(Integer.parseInt(att_cls), "tipo"));
                st.setAttribute(ptr, "valid", "1");
                st.setAttribute(ptr, "uso", "identificador");
            }
        }
    }
}

public void heredar(Integer padre) {
    Integer ptr = st.getPtr(st.getLexema(padre), ambitoActual.copy(), "CLASS");
    if (ptr == 0) {
        agregarError(errores_semanticos, Parser.ERROR,
            String.format(ERROR_ALCANCE, st.getLexema(padre), ambitoActual.toString()));
        return;
    }
    String tipo = st.getAttribute(claseActual, "tipo");
    if (tipo != null){
        agregarError(errores_semanticos, Parser.ERROR,
            String.format(ERROR_HERENCIA, st.getLexema(claseActual), st.getLexema(Integer.parseInt(tipo))));
        return;
    }
    st.setAttribute(claseActual, "tipo", ""+ptr);

    if (st.getAttribute(ptr, "tipo") != null) {
        ptr = Integer.parseInt(st.getAttribute(ptr, "tipo"));
        if (st.getAttribute(ptr, "tipo") != null)
            agregarError(errores_semanticos, Parser.ERROR,
                    String.format(ERROR_CONTROL_HERENCIA, st.getLexema(ptr)));
    }
}

public void agregarClase(Integer id, String desc) {
    int i = 0;
    Integer ptr;
    do {
        ptr = st.getPtr(st.getLexema(id) + ":" + ambitoActual.toString(), "CLASS");
        if (ptr != 0)
            //split by @ and add 1 to the number
            st.setLexema(id, st.getLexema(id).split("@")[0] + "@" + i++);
    } while (ptr != 0);

    if (i != 0)
        agregarError(errores_semanticos, Parser.ERROR,
                String.format(ERROR_REDECLARACION, st.getLexema(id).split("@")[0], ambitoActual.toString()));

    // add the scope to the lexema
    st.setLexema(id, st.getLexema(id) + ":" + ambitoActual.toString());
    st.setAttribute(id, "uso", desc);
    st.setAttribute(id, "valid", "1");// TODO: CAMBIAR
    ambitoActual.push(st.getLexema(id).split(":")[0]);
    claseActual = id;
    agregarEstructura(desc +" : " + st.getLexema(id));

    // si implementa una FDCLASS, borramos esta entrada
    if (desc.equals("CLASS")) {
        ptr = st.getPtr(st.getLexema(id), "FDCLASS");
        if (ptr != 0) {
            agregarEstructura("Implementacion de FDCLASS: " + st.getLexema(id));
            st.delEntry(ptr);
        }
    }
}

public Integer getTipoClase(Integer id, Ambito ambito) {
    //id ptr a class1
    String lexema = st.getLexema(id); // class1
    // ver si esta al alcance
    Integer ptr = st.getPtr(lexema, ambito.copy(), "CLASS");
    if (ptr == 0) {
        agregarError(errores_semanticos, Parser.ERROR,
                String.format(ERROR_ALCANCE, lexema, ambito.toString()));
        return 0;
    }
    // ver si es tiene uso = CLASS
    String uso = st.getAttribute(ptr, "uso");
    if (uso == null) {
        agregarError(errores_semanticos, Parser.ERROR,
                String.format(ERROR_ATRIBUTO, lexema, "uso"));
        return 0;
    } else if (!uso.equals("CLASS")) {
        agregarError(errores_semanticos, Parser.ERROR,
                String.format(ERROR_CLASE, lexema));
        return 0;
    }
    return ptr;
}

public Integer agregarAtributo(Integer ptr_lhs, Integer ptr_rhs, Ambito ambito) {
    ptr_lhs = obtenerAtributo(ptr_lhs, ptr_rhs, ambito);
    if (ptr_lhs == 0) {
        agregarError(errores_semanticos, Parser.ERROR,
                String.format(ERROR_ALCANCE, st.getLexema(ptr_rhs), ambito.toString()));
        return 0;
    }
    return ptr_lhs;
}

public Integer obtenerAtributo(Integer ptr_lhs, Integer ptr_rhs, Ambito ambito) {
    // ptr_lhs puede ser un id o un atributo_clase, eg: c1:global:main, c1, var1
    // ptr_rhs es el lexema de un id detectado por lex, eg:var1, obj2
    int max_nivel = 3;
    Integer ptr_rhs_aux = 0;
    do {
        // si ptr_lhs es la clase no hace falta buscarla, si es una instancia de ella si
        if (ambito != null)
            ptr_lhs = getClase(ptr_lhs, ambito);
        
        if (ptr_lhs == 0) return 0;
        // obtenemos el lexema de la clase, el cual es class1:global:main
        String clase = st.getLexema(ptr_lhs);
        String[] partes = clase.split(":");
        // creamos un ambito partes[1:] + partes[0]
        ambito = new Ambito();
        for (int i = 1; i < partes.length; i++)
            ambito.push(partes[i]);
        ambito.push(partes[0]);
        // el resultado es global:main:class1
        // a partir de este ambito hay que buscar ptr_rhs
        ptr_rhs_aux = st.getPtr(st.getLexema(ptr_rhs) + ":" + ambito.toString());
        // si no lo encuentra, mira si hereda y busca en la clase padre
    } while (ptr_rhs_aux == 0 && --max_nivel > 0 && st.getAttribute(ptr_lhs, "tipo") != null);

    return ptr_rhs_aux;
}

private Integer getClase(Integer id, Ambito ambito) {
    // id puede ser un id o un atributo_clase, eg: c1:global:main, c1, var1
    // primero hay que detectar que ptr_lhs este al alcance
    id = st.getPtr(st.getLexema(id), ambito.copy());
    if (id == 0) {
        agregarError(errores_semanticos, Parser.ERROR,
                String.format(ERROR_ALCANCE, st.getLexema(id), ambito.toString()));
        return 0;
    }
    // verificamos si id es de tipo de alguna clase
    String tipo = st.getAttribute(id, "tipo");
    if (tipo == null) { //TODO:borrar
        agregarError(errores_semanticos, Parser.ERROR,
                String.format(ERROR_ATRIBUTO, st.getLexema(id), "tipo"));
        return 0;
    // para las instancias, tipo es un puntero a una entrada de la tabla de simbolos
    }
    if (!st.contains(Integer.parseInt(tipo))) {
        agregarError(errores_semanticos, Parser.ERROR,
                String.format(ERROR_CLASE_NO_DECLARADA, st.getLexema(id), Integer.parseInt(tipo)));
        return 0;
    }
    // esta entrada tiene como atributo uso = CLASS
    if (!st.getAttribute(Integer.parseInt(tipo), "uso").equals("CLASS")) {
        agregarError(errores_semanticos, Parser.ERROR,
                String.format(ERROR_CLASE, st.getLexema(Integer.parseInt(tipo))));
        return 0;
    }
    return Integer.parseInt(tipo);
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
    //if (id == 0) {
        //agregarError(errores_semanticos, Parser.ERROR,
                //String.format(ERROR_ST, st.getLexema(id)));
        //return;
    //}
    Integer ptr;
    if (!st.getLexema(id).contains(":")) // inv a funcion
        ptr = st.getPtr(st.getLexema(id), ambitoActual.copy(), "FUNCTION");
    else if (st.getAttribute(id, "uso").equals("FUNCTION")) // inv a metodo
        ptr = id;
    else // intento de inv a algo que no es una funcion o metodo
        return;

    if (ptr == 0) {
        agregarError(errores_semanticos, Parser.ERROR,
                String.format(ERROR_ALCANCE, st.getLexema(id), ambitoActual.toString()));
        return;
    }
    if (param != null && param.ival == 0)
        return;

    String param_formal = st.getAttribute(ptr, "parameter");
    if (param_formal != null) {
        param_formal = st.getAttribute(Integer.parseInt(param_formal), "tipo");
        if (param == null || Integer.parseInt(param_formal) != (int)param.dval) {
            agregarError(errores_semanticos, Parser.ERROR,
                    String.format(ERROR_PARAMETRO, st.getLexema(ptr), param_formal));
            return;
        }
    }
    else if (param != null) {
        agregarError(errores_semanticos, Parser.ERROR,
                String.format(ERROR_PARAMETRO, st.getLexema(ptr), "void"));
        return;
    }
    agregarEstructuraLlamados("Invocacion a la funcion ", ptr);
    if (param != null) 
        crearTerceto("CALL",id, param.ival,"","");
    else
        crearTerceto("CALL",id, -1,"","");
}

public void invocacionFuncion(Integer id) {
    invocacionFuncion(id, null);
}

public void agregarFuncion(Integer id, Short tipo, Integer parametro) {
    int i = 0;
    Integer ptr;
    do {
        ptr = st.getPtr(st.getLexema(id) + ":" + ambitoActual.toString());
        if (ptr != 0)
            //split by @ and add 1 to the number
            st.setLexema(id, st.getLexema(id).split("@")[0] + "@" + i++);
    } while (ptr != 0);

    if (i != 0)
        agregarError(errores_semanticos, Parser.ERROR,
                String.format(ERROR_REDECLARACION, st.getLexema(id).split("@")[0], ambitoActual.toString()));

    // add the scope to the lexema
    st.setLexema(id, st.getLexema(id) + ":" + ambitoActual.toString());
    st.setAttribute(id, "uso", "FUNCTION");
    st.setAttribute(id, "tipo", ""+tipo);
    st.setAttribute(id, "valid", "1");
    
    if (parametro != null && parametro != 0) {
        st.setAttribute(id, "parameter", parametro.toString());
        st.setAttribute(parametro, "valid", "1");
    }
    // agrego el ambito al ambitoActual
    ambitoActual.push(st.getLexema(id).split(":")[0]);
    agregarEstructura("FUNCTION :" + st.getLexema(id));
    crearTerceto("PROC", id, -1, "st", null);
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
    if (lhs.ival == 0 || rhs.ival == 0) return 0;

    if (lhs.dval != rhs.dval) {
        agregarError(errores_semanticos, Parser.ERROR,
            String.format(ERROR_TIPOS_INCOMPATIBLES, getTipo((int)lhs.dval), getTipo((int)rhs.dval)));
        return 0;
    }
    return crearTerceto(op, lhs.ival, rhs.ival, lhs.sval, rhs.sval);
}

public Integer crearTercetoTermino(ParserVal lhs, ParserVal rhs, String op) {
    if (lhs.ival == 0 || rhs.ival == 0) return 0;

    if (lhs.dval != rhs.dval) {
        agregarError(errores_semanticos, Parser.ERROR,
            String.format(ERROR_TIPOS_INCOMPATIBLES, getTipo((int)lhs.dval), getTipo((int)rhs.dval)));
        return 0;
    }
    return crearTerceto(op, lhs.ival, rhs.ival, lhs.sval, rhs.sval);
}

public Integer crearTercetoAsignacion(Integer lhs, ParserVal rhs) {
    if (lhs == 0) {
        agregarError(errores_semanticos, Parser.ERROR,
                String.format(ERROR_ST, st.getLexema(lhs)));
        return 0;
    }
    Integer ptr = st.getPtr(st.getLexema(lhs), ambitoActual.copy(), "identificador");
    if (ptr == 0) {
        agregarError(errores_semanticos, Parser.ERROR,
                String.format(ERROR_ALCANCE, st.getLexema(lhs), ambitoActual.toString()));
        return 0;
    }
    if (rhs.ival == 0) return 0;

    Integer tipo_lhs = Integer.parseInt(st.getAttribute(ptr, "tipo"));
    if (tipo_lhs != rhs.dval) { 
        agregarError(errores_semanticos, Parser.ERROR,
                String.format(ERROR_TIPOS_INCOMPATIBLES, getTipo(tipo_lhs), getTipo((int)rhs.dval)));
        return 0;
    }

    //CHEQUEO de clausula CHECK
    String check_lhs = st.getAttribute(ptr, "check_lhs");
    String check_rhs = st.getAttribute(ptr, "check_rhs");
    
    if (check_lhs != null && !check_lhs.equals("true")) { // si es true o ya se imprimio o falta cambiar rhs
        if (!check_lhs.equals("false")) {
            if (!check_lhs.equals(ambitoActual.toString())) { // tiene que aparecer en ambitos distintos
                st.setAttribute(ptr, "check_lhs", "true"); // de ahora en mas ya se que aparecio en 2+ ambitos
                if (check_rhs.equals("true")) // ya fue usada tmb del rhs
                    agregarEstructura(String.format(ESTRUCTURA_CHECK, st.getLexema(ptr)));
            }
        } else // si es false(primer uso del lhs) le seteo ambito actual
            st.setAttribute(ptr, "check_lhs", ambitoActual.toString());
    }
    agregarEstructura("Asignacion al identificador ", ptr);
    return crearTerceto("=", ptr, rhs.ival, "st", rhs.sval);
} 

public ParserVal crearTercetoIncrement(Integer id) {
    ParserVal val = new ParserVal();
    if (id == 0) {// TODO:esto mmm...
        agregarError(errores_semanticos, Parser.ERROR,
                String.format(ERROR_ST, st.getLexema(id)));
        return val;
    }
    Integer ptr = st.getPtr(st.getLexema(id), ambitoActual.copy(), "identificador");
    if (ptr == 0) {
        agregarError(errores_semanticos, Parser.ERROR,
                String.format(ERROR_ALCANCE, st.getLexema(id), ambitoActual.toString()));
        return val;
    }
    Integer tipo = Integer.parseInt(st.getAttribute(ptr, "tipo"));
    if (lexicalAnalyzer.getReservedWord(tipo) == null) {
        agregarError(errores_semanticos, Parser.ERROR,
                String.format(ERROR_TIPOS_INCOMPATIBLES, getTipo(tipo), "int(1)"));
        return val;
    }
    //CHEQUEO de clausula CHECK
    String check_lhs = st.getAttribute(id,"check_lhs");
    String check_rhs = st.getAttribute(id,"check_rhs");
    if (check_lhs != null && check_rhs.equals("false")){
      st.setAttribute(id, "check_rhs", "true");
      if (check_lhs.equals("true"))
        agregarEstructura(String.format(ESTRUCTURA_CHECK,st.getLexema(id)));
    }

    agregarEstructura("Increment al identificador ", ptr);
    ptr =  crearTerceto("=", ptr,
            crearTerceto("+", ptr, newEntryCte(tipo), "st", "st"),
            "st", "terceto");
    val.ival = ptr;
    val.dval = tipo;
    val.sval = "terceto";
    return val;
}

public Integer newEntryCte(Integer tipo) {
    if (tipo == FLOAT)
        return st.addEntry("1.0", UINT);
    else
        return st.addEntry("1", tipo);
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
        if (terceto.getOperador().equals(b.toString())) {
            if (b.equals("BI") && terceto.getOperando1() == -1) {
                terceto.setOperando1(tercetoActual, true);
                pilaTercetos.apilar(terceto);
                break;
            }
            else if (b.equals("BF") && terceto.getOperando2() == -1) {
                terceto.setOperando2(tercetoActual, true);
                pilaTercetos.apilar(terceto);
                break;
            }
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

LexicalAnalyzer lexicalAnalyzer;
SymbolTable st;
PilaTercetos pilaTercetos;
Integer inicio_while, tipo, claseActual;
int countIF = 0;
Ambito ambitoActual = new Ambito("global");
boolean check, declarandoInstancia = false;

public static void main(String[] args) {
    TransitionMatrix<Integer> mI = new TransitionMatrix<>(19, 28);
    TransitionMatrix<AccionSemantica> mA = new TransitionMatrix<>(19, 28);
    SymbolTable sttemp = new SymbolTable();

    FuncionesAuxiliares.loadMatrixs(mI, mA, "test.csv", sttemp, errores_lexicos);
    Parser parser = new Parser(new LexicalAnalyzer("test2.txt", mI, mA, errores_lexicos), sttemp);
    parser.run();
    
    Parser.imprimirErrores(errores_lexicos, "Errores Lexicos");
    Parser.imprimirErrores(errores_sintacticos, "Errores Sintacticos");
    Parser.imprimirErrores(estructuras, "Estructuras Sintacticas");
    Parser.imprimirErrores(errores_semanticos, "Errores Semanticos");
    
    parser.st.print();
    parser.pilaTercetos.print();

    if (errores_lexicos.isEmpty() && errores_sintacticos.isEmpty() && errores_semanticos.isEmpty()) {
        AssemblyGenerator asm = new AssemblyGenerator(parser.pilaTercetos, parser.st, "output.asm");
        try {
            asm.generarAssembler();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    System.out.println("Ambito actual: " + parser.ambitoActual.toString());
}

public Parser(LexicalAnalyzer lexicalAnalyzer, SymbolTable st) {
    this.lexicalAnalyzer = lexicalAnalyzer;
    this.st = st;
    this.pilaTercetos = new PilaTercetos();
    this.declarandoInstancia = false;
    //yydebug = true;
}