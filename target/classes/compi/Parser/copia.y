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
                     | sen_declarativa
                     | sen_control
                     | error ','                 {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una sentencia ejecutable");}
                     ;

sen_declarativa      : tipo list_var ',' { if (declarandoInstancia) declarandoInstancia = false; }
                     | sentencia_check tipo list_var ',' { check=false; if (declarandoInstancia) declarandoInstancia = false; }
                     | funcion
                     | clase
                     | clase ','                           {agregarError(errores_sintacticos, Parser.ERROR, "Las clases no se declaran con ,");}
                     | clase ';'                           {agregarError(errores_sintacticos, Parser.ERROR, "Las clases no se declaran con ;");}
                     | funcion ','                           {agregarError(errores_sintacticos, Parser.ERROR, "Las funciones no se declaran con ,");}
                     | funcion ';'                           {agregarError(errores_sintacticos, Parser.ERROR, "Las funciones no se declaran con ;");}
                     | sentencia_check tipo list_var     {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una ,");}
                     | tipo list_var                     {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una ,");}
                     ;

sentencia_check      : CHECK { check=true; }
                     ;

tipo                 : SHORT { $$.ival = SHORT; tipo = (int)SHORT; }
                     | UINT  { $$.ival = UINT; tipo = (int)UINT; }
                     | FLOAT { $$.ival = FLOAT; tipo = (int)FLOAT; }
                     | ID    { $$.ival = getTipoClase($1.ival, ambitoActual.copy()); tipo = $$.ival; declarandoInstancia = true; }
                     | error { agregarError(errores_sintacticos, Parser.ERROR, "El tipo ingresado no es valido");}
                     ;

CTE                  : CTE_SHORT     {verificarRango($1.ival); $$.ival = $1.ival; $$.dval = SHORT;}
                     | CTE_UINT      {$$.ival = $1.ival; $$.dval = UINT;}
                     | CTE_FLOAT     {verificarRango($1.ival); $$.ival = $1.ival; $$.dval = FLOAT;}
                     | '-' CTE_SHORT {resolverSigno($2.ival); verificarRango($2.ival); $$.ival = $2.ival; $$.dval = SHORT; }
                     | '-' CTE_FLOAT {resolverSigno($2.ival); verificarRango($2.ival); $$.ival = $2.ival; $$.dval = FLOAT; }
                     | '-' CTE_UINT  { agregarError(errores_sintacticos, Parser.ERROR, "No se puede negar un unsigned int");}
                     | error         { agregarError(errores_sintacticos, Parser.ERROR, "La constante ingresada no es valida"); }
                     ;

list_var             : list_var ';' ID { declararVariable($3.ival); }
                     | ID               { declararVariable($1.ival); }
                     ;

funcion              : header_funcion '{' cuerpo_funcion sen_retorno '}' { ambitoActual.pop(); crearTerceto("RET", $1.ival, -1, null, null); }
                     | header_funcion '{' cuerpo_funcion seleccion_func '}' { ambitoActual.pop(); crearTerceto("RET", $1.ival, -1, null, null); }
                     | header_funcion cuerpo_funcion '}' {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al comienzo de la función");}
                     ;

header_funcion       : VOID ID '(' parametro ')' {  agregarFuncion($2.ival, VOID, $4.ival);
                                                    $$.ival = $2.ival;
                                                    if ($4.ival != 0)
                                                        st.setLexema($4.ival, st.getLexema($4.ival) + ":" + ambitoActual.toString());
                                                 }
                     | VOID ID '(' ')'           { agregarFuncion($2.ival, VOID, null); $$.ival = $2.ival;}
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
                     | inv_metodo ','
                     | inv_funcion ','
                     | seleccion ','
                     | imprimir ','
                     | asignacion ';'   {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una ,");}
                     | inv_metodo ';'   {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una ,");}
                     | inv_funcion ';'  {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una ,");}
                     | seleccion ';'    {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una ,");}
                     | imprimir ';'     {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una ,");}
                     | inv_metodo       {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una ,");}
                     | asignacion       {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una ,");}
                     | inv_funcion      {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una ,");}
                     | seleccion        {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una ,");}
                     | imprimir         {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una ,");}
                     ;

asignacion           : ID '=' exp_aritmetica               { $$.ival = crearTercetoAsignacion($1.ival, $3); $$.sval = "terceto"; if ($$.ival != 0) $$.dval = $1.dval; }
                     | atributo_clase '=' exp_aritmetica   { $$.ival = crearTercetoAsignacion($1.ival, $3); $$.sval = "terceto"; if ($$.ival != 0) $$.dval = $1.dval; }
                     | ID ':' '=' exp_aritmetica                { agregarError(errores_sintacticos, Parser.ERROR, "La asignacion debe ser unicamente con el ="); }
                     | atributo_clase ':' '=' exp_aritmetica    { agregarError(errores_sintacticos, Parser.ERROR, "La asignacion debe ser unicamente con el ="); }
                     | '=' exp_aritmetica               {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un identificador");}
                     | atributo_clase exp_aritmetica    {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un signo igual");}
                     ;

inv_funcion          : ID '(' exp_aritmetica ')'        { invocacionFuncion($1.ival, $3); }
                     | ID '(' ')'                       { invocacionFuncion($1.ival); }
                     | ID '(' exp_aritmetica            {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
                     | ID ')'                           {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
                     ;

inv_metodo           : atributo_clase '(' exp_aritmetica ')'{ if ($1.ival != 0) invocacionMetodo($1.ival, $3); }
                     | atributo_clase '(' ')'               { if ($1.ival != 0) invocacionMetodo($1.ival); }
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
                                                        if (check_lhs.equals("true")){
                                                            agregarEstructura(String.format(ESTRUCTURA_CHECK,st.getLexema($$.ival)));
                                                            Integer aux = st.addEntry("CHECK "+st.getLexema($$.ival)+" CUMPLIDO", STR_1LN);
                                                            st.setAttribute(aux, "valid", "1");
                                                            st.setAttribute(aux, "tipo", ""+STR_1LN);
                                                            Terceto terceto = new Terceto("PRINT", aux, -1, false, false);
                                                            pilaTercetosCHECK.apilar(terceto);
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
                                                            if (check_lhs.equals("true")){
                                                                agregarEstructura(String.format(ESTRUCTURA_CHECK,st.getLexema($$.ival)));
                                                                Integer aux = st.addEntry("CHECK "+st.getLexema($$.ival)+" CUMPLIDO", STR_1LN);
                                                                st.setAttribute(aux, "valid", "1");
                                                                st.setAttribute(aux, "tipo", ""+STR_1LN);
                                                                Terceto terceto = new Terceto("PRINT", aux, -1, false, false);
                                                                pilaTercetosCHECK.apilar(terceto);
                                                            }
                                                        }
                                                    }
                                                }
                     | atributo_clase INCREMENT { $$ = crearTercetoIncrement($1.ival); }
                     | error INCREMENT          { agregarError(errores_sintacticos, Parser.ERROR, "Variable o constante invalida");}
                     ;

comparador           : NOT_EQUAL     { $$.ival = NOT_EQUAL; }
                     | EQUAL         { $$.ival = EQUAL; }
                     | GREATER_EQUAL { $$.ival = GREATER_EQUAL; }
                     | LESS_EQUAL    { $$.ival = LESS_EQUAL; }
                     | '<'           { $$.ival = 60; }
                     | '>'           { $$.ival = 62; }
                     | '='           { $$.ival = EQUAL;
                                        agregarError(errores_sintacticos, Parser.ERROR, "Error en el comparador igual, se esperaba ==");}
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
                      ;

sen_ejecutable_r     : sen_ejecutable_r sen_ejecutable
                     | sen_ejecutable
                     ;

imprimir             : PRINT STR_1LN             { crearTerceto("PRINT", $2.ival, -1, "st", null);
                                                   st.setAttribute($2.ival, "valid", "1"); }
                     | PRINT ','                 {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una cadena para imprimir");}
                     ;

sen_control          : inicio_while condicion DO '{' sen_ejecutable_r '}'   {   agregarEstructura("WHILE");
                                                                                crearTerceto("BI", -1, -1, "", "");
                                                                                completarB("BF", pilaTercetos.getContador()+1);
                                                                                completarWhile(); 
                                                                                crearTerceto("END_WHILE"+countWHILE++, -1, -1, "", "");
                                                                            }
                     | inicio_while condicion DO sen_ejecutable             {   agregarEstructura("WHILE");
                                                                                crearTerceto("BI", -1, -1, "", "");
                                                                                completarB("BF", pilaTercetos.getContador()+1);
                                                                                completarWhile(); 
                                                                                crearTerceto("END_WHILE"+countWHILE++, -1, -1, "", "");
                                                                            }
                     | inicio_while condicion DO '{' '}'                           {  agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una o mas sentencias ejecutables"); }
                     ;

inicio_while         : WHILE { crearTerceto("START_WHILE"+countWHILE, -1, -1, "", ""); }
                     ;

clase                : class_header '{' '}'                   { ambitoActual.pop(); claseActual = null; }
                     | class_header '{' cuerpo_clase '}'      { ambitoActual.pop(); claseActual = null; }
                     | class_header '{' cuerpo_clase herencia_clase '}'      { ambitoActual.pop(); claseActual = null; }
                     | class_header '{' herencia_clase '}'      { ambitoActual.pop(); claseActual = null; }
                     | CLASS ID ','                       { agregarClase($2.ival, "FDCLASS"); ambitoActual.pop(); claseActual = null; }
                     | class_header cuerpo_clase '}'       { agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al final de la clase");
                                                             ambitoActual.pop(); }
                     ;

class_header         : CLASS ID             { agregarClase($2.ival, "CLASS"); }
                     ;

cuerpo_clase         : cuerpo_clase sen_declarativa
                     | cuerpo_clase herencia_clase sen_declarativa       { agregarError(errores_sintacticos, Parser.ERROR, "La declaracion de herencia debe ser la ultima sentencia de la clase");}
                     | herencia_clase sen_declarativa       { agregarError(errores_sintacticos, Parser.ERROR, "La declaracion de herencia debe ser la ultima sentencia de la clase");}
                     | sen_declarativa
                     ;

// herencia por composicion
herencia_clase       : ID ',' { heredar($1.ival); }
                     ;

%%
