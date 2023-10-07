%{
package compi;

import java.io.*;
%}

%token ID CTE ASSIGN IF ELSE END_IF PRINT CLASS VOID WHILE DO CTE_SHORT CTE_UINT CTE_FLOAT
       EQUAL GREATER_EQUAL LESS_EQUAL NOT_EQUAL INCREMENT STR_1LN RETURN

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
                     | ERROR ','                {agregarError(errores_sintacticos, Parser.ERROR, "Error en la declaración");}
                     ;

tipo                 : CTE_SHORT
                     | CTE_UINT
                     | CTE_FLOAT
                     ;

list_var             : list_var ';' ID
                     | ID
                     ;

funcion              : VOID ID '(' parametro ')' '{' cuerpo_funcion '}'
                     | VOID ID '(' ')' '{' cuerpo_funcion '}'
                     | VOID ID '(' parametro ')' '{' cuerpo_funcion      {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al final de la función");}
                     | VOID ID '(' ')' '{' cuerpo_funcion                  {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al final de la función");}
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

                     // deberian ir sentencias declarativas?
sen_ejecutable       : asignacion ','
                     | inv_funcion ','
                     | seleccion ','
                     | imprimir ','
                     | asignacion                {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una coma al final de la sentencia ejecutable");}
                     | inv_funcion               {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una coma al final de la sentencia ejecutable");}
                     | seleccion                 {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una coma al final de la sentencia ejecutable");}
                     | imprimir                  {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una coma al final de la sentencia ejecutable");}
                     | ERROR ','                {agregarError(errores_sintacticos, Parser.ERROR, "Error en la sentencia ejecutable");}
                     ;

asignacion           : ID '=' exp_aritmetica ','
                     | ID '=' exp_aritmetica    {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una coma al final de la asignación");}
                     | ID '='                   {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una expresión aritmética");}
                     | '=' exp_aritmetica ','   {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un identificador");}
                     | ID exp_aritmetica ','    {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un signo igual");}
                     ;

inv_funcion          : ID '(' parametro_real ')' ','
                     | ID '(' ')' ','
                     | ID '(' parametro_real ')' {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una coma al final de la invocación");}
                     | ID '(' ')'               {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una coma al final de la invocación");}
                     ;

parametro_real       : exp_aritmetica
                     | ID
                     | tipo
                     ;

seleccion            : IF '(' condicion ')' bloque_sen_ejecutable ELSE bloque_sen_ejecutable END_IF ','
                     | IF '(' condicion ')' bloque_sen_ejecutable END_IF ','
seleccion            | IF '(' condicion ')' ELSE bloque_sen_ejecutable END_IF ',' {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un bloque de sentencias ejecutables");}
                     | IF '(' condicion ')' bloque_sen_ejecutable ELSE END_IF ',' {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un bloque de sentencias ejecutables");}
                     | IF '(' condicion ')' END_IF ',' {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un bloque de sentencias ejecutables");}
                     | ERROR ','                {agregarError(errores_sintacticos, Parser.ERROR, "Error en la sentencia de selección");}
                     ;

condicion            : exp_aritmetica comparador exp_aritmetica
                     ;

                     //Los operandos de las expresiones aritméticas pueden ser variables, constantes, u otras expresiones aritméticas.
                     //No se deben permitir anidamientos de expresiones con paréntesis.
exp_aritmetica       : exp_aritmetica '+' termino
                     | exp_aritmetica '-' termino
                     | termino
                     ;

termino              : termino '*' factor
                     | termino '/' factor
                     | factor
                     ;

                     // CTE_FLOAT, CTE_SHORT, CTE_UINT tambien? 
factor               : ID
                     | ID INCREMENT
                     | CTE
                     ;

comparador           : NOT_EQUAL
                     | EQUAL
                     | GREATER_EQUAL
                     | LESS_EQUAL
                     | '<'
                     | '>'
                     ;

                     // una sola sentencia ejecutable o varias encerradas por llaves
bloque_sen_ejecutable: sen_ejecutable
                     | '{' sen_ejecutable_r '}'
                     | sen_ejecutable_r '}'      {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al comienzo del bloque de sentencias ejecutables");}
                     | '{' sen_ejecutable_r      {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al final del bloque de sentencias ejecutables");}
                     | '{' '}'                   {agregarError(errores_sintacticos, Parser.ERROR, "Se espera un bloque de sentencias ejecutables");}
                     | ERROR ','                {agregarError(errores_sintacticos, Parser.ERROR, "Error en el bloque de sentencias ejecutables");}
                     ;

                     // conjunto sentencias ejecutables
sen_ejecutable_r     : sen_ejecutable_r sen_ejecutable
                     | sen_ejecutable
                     ;

imprimir             : PRINT STR_1LN ','
                     | PRINT ','                 {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una cadena para imprimir");}
                     | ERROR ','                {agregarError(errores_sintacticos, Parser.ERROR, "Error en la sentencia de impresión");}
                     ;

sen_control          : WHILE '(' condicion ')' DO bloque_sen_ejecutable ','
                     | WHILE condicion ')' DO bloque_sen_ejecutable ',' {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un parentesis al comienzo de la condicion");}
                     | WHILE '(' condicion DO bloque_sen_ejecutable ',' {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un parentesis al final de la condicion");}
                     | WHILE condicion DO bloque_sen_ejecutable ','     {agregarError(errores_sintacticos, Parser.ERROR, "Se espera que la condicion este encerrada entre parentesis");}
                     | WHILE '(' condicion ')' DO ','                   {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un bloque de sentencias ejecutables");}
                     | ERROR ','                {agregarError(errores_sintacticos, Parser.ERROR, "Error en la sentencia de control");}
                     ;


clase                : CLASS ID '{' '}'
                     | CLASS ID '{' cuerpo_clase '}'
                     | CLASS ID '{' '}'          {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al final de la clase");}
                     | CLASS ID '{' cuerpo_clase {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al final de la clase");}
                     ;

cuerpo_clase         : cuerpo_clase sen_declarativa
                     | sen_declarativa
                     ;
