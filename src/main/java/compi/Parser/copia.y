NcrearTerceto("=", $1.ival, $3.ival, $1.sval, $3.sval);
                                                             $$.sval = "terceto";}
                     | atributo_clase '=' exp_aritmetica    {agregarEstructura("Asignacion al identificador ", $1.ival);
                                                             $$.ival = crearTerceto("=", $1.ival, $3.ival, $1.sval, $3.sval);
                                                             $$.sval = "terceto";}
                     | ID '='                           {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una expresión aritmética");}
                     | '=' exp_aritmetica               {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un identificador");}
                     | ID exp_aritmetica                {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un signo igual");}
                     | atributo_clase '='               {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una expresión aritmética");}
                     | atributo_clase exp_aritmetica    {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un signo igual");}
                     ;

inv_funcion          : ID '(' exp_aritmetica ')'        {agregarEstructuraLlamados("Invocacion a la funcion ", $1.ival);}
                     | ID '(' ')'                       {agregarEstructuraLlamados("Invocacion a la funcion ", $1.ival);}
                     | ID '(' exp_aritmetica            {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
                     | ID exp_aritmetica ')'            {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
                     | ID ')'                           {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
                     | ID '('                           {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
                     ;

inv_metodo           : atributo_clase '(' exp_aritmetica ')' {agregarEstructuraLlamados("Invocacion al metodo ", $1.ival);}
                     | atributo_clase '(' ')'                {agregarEstructuraLlamados("Invocacion al metodo ", $1.ival);}
                     | atributo_clase '('               {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
                     | atributo_clase ')'               {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
                     ;

atributo_clase       : ID '.' ID                        {$$.ival = agregarAtributo($1.ival, $3.ival);}
                     ;

seleccion            : IF '(' condicion ')' bloque_sen_ejecutable ELSE bloque_sen_ejecutable END_IF {agregarEstructura("IF");}
                     | IF '(' condicion ')' bloque_sen_ejecutable END_IF {agregarEstructura("IF");}
                     | IF '(' condicion ')' ELSE bloque_sen_ejecutable END_IF {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un bloque de sentencias ejecutables");}
                     | IF '(' condicion ')' bloque_sen_ejecutable ELSE END_IF {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un bloque de sentencias ejecutables");}
                     | IF '(' condicion ')' END_IF  {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un bloque de sentencias ejecutables");}
                     ;

seleccion_func       : IF '(' condicion ')' bloque_ejecutable_func ELSE bloque_ejecutable_func END_IF ',' {agregarEstructura("IF");}
                     | IF '(' condicion ')' bloque_ejecutable_func END_IF ',' {agregarEstructura("IF");}
                     | IF '(' condicion ')' ELSE bloque_ejecutable_func END_IF {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un bloque de sentencias ejecutables");}
                     | IF '(' condicion ')' bloque_ejecutable_func ELSE END_IF {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un bloque de sentencias ejecutables");}
                     ;

condicion            : exp_aritmetica comparador exp_aritmetica { resolverCondicion($1.ival, $2, $3.ival); }
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

factor               : ID                       { $$.ival = $1.ival; $$.sval = "st"; }
                     | ID INCREMENT             { $$.ival = $1.ival; $$.sval = "st"; }
                     | CTE                      { $$.ival = $1.ival; $$.sval = "st"; }
                     | atributo_clase           { $$.ival = $1.ival; $$.sval = "st"; }
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

sen_control          : WHILE '(' condicion ')' DO bloque_sen_ejecutable ',' {agregarEstructura("WHILE");}
                     | WHILE condicion ')' DO bloque_sen_ejecutable ',' {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un parentesis al comienzo de la condicion");}
                     | WHILE '(' condicion DO bloque_sen_ejecutable ',' {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un parentesis al final de la condicion");}
                     | WHILE condicion DO bloque_sen_ejecutable ','     {agregarError(errores_sintacticos, Parser.ERROR, "Se espera que la condicion este encerrada entre parentesis");}
                     | WHILE '(' condicion ')' DO ','                   {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un bloque de sentencias ejecutables");}
                     ;

clase                : CLASS ID '{' '}'                   {agregarClase($2.ival, "CLASS");}
                     | CLASS ID '{' cuerpo_clase '}'      {agregarClase($2.ival, "CLASS");}  
                     | CLASS ID ','                       {agregarClase($2.ival, "FDCLASS");}
                     | CLASS ID cuerpo_clase '}'       {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al final de la clase");}
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

private static String tipo;

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
    st.setAttribute(id, "description", description);
    agregarEstructura(description +" : " + st.getLexema(id));
}

public Integer agregarAtributo(Integer ptr_lhs, Integer ptr_rhs) {
    String lexema = st.getLexema(ptr_lhs) + "." + st.getLexema(ptr_rhs);
    return st.addEntry(lexema, ID, "ATTRIBUTE");
}

public void resolverSigno(Integer ptr_cte) {
    st.setLexema(ptr_cte, "-" + st.getLexema(ptr_cte));
}

public void verificarRango(Integer ptr_cte) {
    if (ptr_cte == -1) return;
    String lexema = st.getLexema(ptr_cte);
    String tipo = st.getAttribute(ptr_cte,"description");

    switch (tipo) {
        case "float":
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
        case "shortint":
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

public void agregarFuncion(Integer id, String tipo, Integer parametro) {
    st.setAttribute(id, "description", "FUNCTION");
    st.setAttribute(id, "type", tipo);
    if (parametro != -1) {
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

public Integer crearTerceto(String op, Integer lhs, Integer rhs, String tipoLhs, String tipoRhs) {
    Terceto terceto = new Terceto(op, lhs, rhs, tipoLhs == "terceto", tipoRhs == "terceto");
    pilaTercetos.apilar(terceto);
    return pilaTercetos.getContador();
}

LexicalAnalyzer lexicalAnalyzer;
SymbolTable st;
PilaTercetos pilaTercetos;

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
}

public Parser(LexicalAnalyzer lexicalAnalyzer, SymbolTable st) {
    this.lexicalAnalyzer = lexicalAnalyzer;
    this.st = st;
    this.pilaTercetos = new PilaTercetos();
    //yydebug = true;
}
