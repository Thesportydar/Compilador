//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 2 "gramatica.y"
package compi.Parser;
import compi.*;
import compi.AccionesSemanticas.AccionSemantica;

import java.io.*;
//#line 21 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short ID=257;
public final static short IF=258;
public final static short ELSE=259;
public final static short END_IF=260;
public final static short PRINT=261;
public final static short CLASS=262;
public final static short VOID=263;
public final static short WHILE=264;
public final static short DO=265;
public final static short SHORT=266;
public final static short UINT=267;
public final static short FLOAT=268;
public final static short CTE_SHORT=269;
public final static short CTE_UINT=270;
public final static short CTE_FLOAT=271;
public final static short INCREMENT=272;
public final static short GREATER_EQUAL=273;
public final static short LESS_EQUAL=274;
public final static short EQUAL=275;
public final static short NOT_EQUAL=276;
public final static short STR_1LN=277;
public final static short RETURN=278;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    1,    1,    1,    1,    2,    2,    2,
    5,    5,    5,    9,    9,    9,    6,    6,    7,    7,
   10,   11,   11,   11,   11,   11,   11,   12,    3,    3,
    3,    3,    3,   13,   13,   14,   14,   17,   17,   19,
   15,   15,   20,   18,   18,   18,   23,   23,   23,   24,
   24,   24,   24,   22,   22,   22,   22,   22,   22,   21,
   21,   25,   25,   16,    4,    8,    8,    8,   26,   26,
};
final static short yylen[] = {                            2,
    3,    2,    2,    2,    1,    1,    1,    3,    1,    1,
    1,    1,    1,    1,    1,    1,    3,    1,    8,    7,
    2,    3,    3,    2,    2,    2,    1,    2,    2,    2,
    2,    2,    2,    3,    3,    4,    3,    4,    3,    3,
    8,    6,    3,    3,    3,    1,    3,    3,    1,    1,
    2,    1,    1,    1,    1,    1,    1,    1,    1,    1,
    3,    2,    1,    2,    7,    4,    5,    3,    2,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,   11,   12,
   13,    0,    7,    5,    6,    0,    9,   10,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   64,    0,
    0,    0,    1,    2,    3,    4,   18,    0,   29,   30,
   31,   32,   33,    0,    0,    0,   14,   15,   16,   37,
   52,    0,   53,    0,   49,    0,   40,    0,    0,    0,
   68,    0,    0,    8,    0,   39,    0,    0,   51,    0,
    0,   36,    0,    0,   56,   57,   55,   54,   58,   59,
    0,    0,   66,   70,    0,    0,    0,    0,    0,   17,
   38,    0,    0,   47,   48,    0,    0,   60,    0,   67,
   69,    0,   21,    0,    0,   63,    0,    0,   42,    0,
    0,    0,    0,    0,   61,   62,    0,    0,   25,   26,
   20,    0,    0,    0,   65,   41,   28,   22,   23,   19,
};
final static short yydgoto[] = {                          2,
   12,  110,   98,   15,   16,   38,   17,   18,   51,   88,
  112,  119,   19,   20,   21,   22,   23,   58,   24,   59,
   99,   81,   54,   55,  107,   85,
};
final static short yysindex[] = {                      -101,
 -148,    0,   24,  -12, -238, -196, -191,   28,    0,    0,
    0, -114,    0,    0,    0, -182,    0,    0,   43,   58,
   60,   67,   77,  -23,  -16, -192, -167, -192,    0,  -29,
   89, -192,    0,    0,    0,    0,    0,   -2,    0,    0,
    0,    0,    0,   -1, -192,   -5,    0,    0,    0,    0,
    0,   48,    0,    3,    0,   11,    0,  -25,   51,  -58,
    0,    5,   91,    0, -127,    0,   62,   11,    0, -192,
 -192,    0, -192, -192,    0,    0,    0,    0,    0,    0,
 -192,  -39,    0,    0,  -51,   10, -123,   94, -129,    0,
    0,    3,    3,    0,    0,   11, -175,    0, -164,    0,
    0,   17,    0,   14,  -39,    0,  -37,  -39,    0, -140,
 -140, -102,   17,   96,    0,    0, -119,   98,    0,    0,
    0, -140, -140,  -89,    0,    0,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -41,    0,    0,    0,    0,
    0,    0,    0,  -36,    0,  101,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  102,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  -31,  -11,    0,    0,  110,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  -77,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  -65,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,   46,   15,  145,  100,    0,    0,    0,    0,    0,
   50,  -42,    0,    0,    0,    0,    0,   18,   27,  135,
  -32,    0,   53,   52,    0,    0,
};
final static int YYTABLESIZE=285;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         50,
   50,   50,   50,   50,   46,   50,   46,   46,   46,   44,
   33,   44,   44,   44,   61,   14,   44,   70,   50,   71,
   50,    1,  121,   46,   50,   46,   35,   28,   44,   45,
   44,   45,   45,   45,   79,  130,   80,   45,   29,   66,
   27,   64,   52,   56,   73,   86,   13,   27,   45,   74,
   45,   53,   53,   70,   53,   71,   65,   34,   53,   24,
   30,   67,   68,   25,   46,   31,   83,   32,  120,   27,
   53,   53,  114,  100,   37,  117,   47,   48,   49,  128,
  129,    3,    4,   97,   26,    5,   39,  115,   72,   57,
   70,   82,   71,   60,  108,  109,   53,   53,   96,   53,
   53,   40,   91,   41,   70,   84,   71,   53,    3,    4,
   42,  106,    5,    6,    7,    8,  111,    9,   10,   11,
   43,  116,   92,   93,   94,   95,  123,  111,   62,   90,
  101,   89,  102,  103,  104,  105,  113,  118,  123,  125,
  126,  127,    3,    4,   34,   35,    5,    6,    7,    8,
   43,    9,   10,   11,    3,    4,   36,  122,    5,    6,
    7,   87,  124,    9,   10,   11,   63,    3,    4,  122,
    0,    5,    6,    7,    0,    0,    9,   10,   11,   27,
   27,    0,    0,   27,   27,   27,    0,    0,   27,   27,
   27,   24,   24,    0,    0,   24,   24,   24,    0,    0,
   24,   24,   24,    6,    7,    0,    0,    9,   10,   11,
    6,    7,    0,    0,    9,   10,   11,    3,    4,    3,
    4,    5,    0,    5,    0,    0,    0,    0,    0,    0,
    0,   50,   50,   50,   50,    0,   46,   46,   46,   46,
   46,   44,   44,   44,   44,    0,    0,   75,   76,   77,
   78,    0,   47,   48,   49,   46,    0,    0,    0,    0,
    0,   45,   45,   45,   45,    0,   69,   47,   48,   49,
    9,   10,   11,    3,    4,    0,    0,    5,    6,    7,
    0,    0,    9,   10,   11,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   42,   43,   44,   45,   41,   47,   43,   44,   45,   41,
  125,   43,   44,   45,   44,    1,   40,   43,   60,   45,
   62,  123,  125,   60,   41,   62,   12,   40,   60,   41,
   62,   43,   44,   45,   60,  125,   62,   61,  277,   41,
   46,   44,   25,   26,   42,   41,    1,  125,   60,   47,
   62,   25,   26,   43,   28,   45,   59,   12,   32,  125,
  257,   44,   45,   40,  257,  257,  125,   40,  111,   46,
   44,   45,  105,  125,  257,  108,  269,  270,  271,  122,
  123,  257,  258,  123,   61,  261,   44,  125,   41,  257,
   43,   41,   45,  123,  259,  260,   70,   71,   81,   73,
   74,   44,   41,   44,   43,   60,   45,   81,  257,  258,
   44,   97,  261,  262,  263,  264,  102,  266,  267,  268,
   44,  107,   70,   71,   73,   74,  112,  113,   40,  257,
   85,   41,  123,  257,   41,  265,  123,  278,  124,   44,
  260,   44,  257,  258,   44,   44,  261,  262,  263,  264,
   41,  266,  267,  268,  257,  258,   12,  112,  261,  262,
  263,   62,  113,  266,  267,  268,   32,  257,  258,  124,
   -1,  261,  262,  263,   -1,   -1,  266,  267,  268,  257,
  258,   -1,   -1,  261,  262,  263,   -1,   -1,  266,  267,
  268,  257,  258,   -1,   -1,  261,  262,  263,   -1,   -1,
  266,  267,  268,  262,  263,   -1,   -1,  266,  267,  268,
  262,  263,   -1,   -1,  266,  267,  268,  257,  258,  257,
  258,  261,   -1,  261,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  273,  274,  275,  276,   -1,  273,  274,  275,  276,
  257,  273,  274,  275,  276,   -1,   -1,  273,  274,  275,
  276,   -1,  269,  270,  271,  257,   -1,   -1,   -1,   -1,
   -1,  273,  274,  275,  276,   -1,  272,  269,  270,  271,
  266,  267,  268,  257,  258,   -1,   -1,  261,  262,  263,
   -1,   -1,  266,  267,  268,
};
}
final static short YYFINAL=2;
final static short YYMAXTOKEN=278;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'","'.'","'/'",null,null,null,null,null,null,null,null,null,null,null,"';'",
"'<'","'='","'>'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,"ID","IF","ELSE","END_IF","PRINT","CLASS",
"VOID","WHILE","DO","SHORT","UINT","FLOAT","CTE_SHORT","CTE_UINT","CTE_FLOAT",
"INCREMENT","GREATER_EQUAL","LESS_EQUAL","EQUAL","NOT_EQUAL","STR_1LN","RETURN",
};
final static String yyrule[] = {
"$accept : prog",
"prog : '{' sentencias '}'",
"sentencias : sentencias sen_declarativa",
"sentencias : sentencias sen_ejecutable",
"sentencias : sentencias sen_control",
"sentencias : sen_ejecutable",
"sentencias : sen_control",
"sentencias : sen_declarativa",
"sen_declarativa : tipo list_var ','",
"sen_declarativa : funcion",
"sen_declarativa : clase",
"tipo : SHORT",
"tipo : UINT",
"tipo : FLOAT",
"CTE : CTE_SHORT",
"CTE : CTE_UINT",
"CTE : CTE_FLOAT",
"list_var : list_var ';' ID",
"list_var : ID",
"funcion : VOID ID '(' parametro ')' '{' cuerpo_funcion '}'",
"funcion : VOID ID '(' ')' '{' cuerpo_funcion '}'",
"parametro : tipo ID",
"cuerpo_funcion : cuerpo_funcion sen_declarativa sen_retorno",
"cuerpo_funcion : cuerpo_funcion sen_ejecutable sen_retorno",
"cuerpo_funcion : cuerpo_funcion sen_ejecutable",
"cuerpo_funcion : sen_declarativa sen_retorno",
"cuerpo_funcion : sen_ejecutable sen_retorno",
"cuerpo_funcion : sen_ejecutable",
"sen_retorno : RETURN ','",
"sen_ejecutable : asignacion ','",
"sen_ejecutable : inv_funcion ','",
"sen_ejecutable : seleccion ','",
"sen_ejecutable : imprimir ','",
"sen_ejecutable : inv_metodo ','",
"asignacion : ID '=' exp_aritmetica",
"asignacion : atributo_clase '=' exp_aritmetica",
"inv_funcion : ID '(' exp_aritmetica ')'",
"inv_funcion : ID '(' ')'",
"inv_metodo : atributo_clase '(' exp_aritmetica ')'",
"inv_metodo : atributo_clase '(' ')'",
"atributo_clase : ID '.' ID",
"seleccion : IF '(' condicion ')' bloque_sen_ejecutable ELSE bloque_sen_ejecutable END_IF",
"seleccion : IF '(' condicion ')' bloque_sen_ejecutable END_IF",
"condicion : exp_aritmetica comparador exp_aritmetica",
"exp_aritmetica : exp_aritmetica '+' termino",
"exp_aritmetica : exp_aritmetica '-' termino",
"exp_aritmetica : termino",
"termino : termino '*' factor",
"termino : termino '/' factor",
"termino : factor",
"factor : ID",
"factor : ID INCREMENT",
"factor : CTE",
"factor : atributo_clase",
"comparador : NOT_EQUAL",
"comparador : EQUAL",
"comparador : GREATER_EQUAL",
"comparador : LESS_EQUAL",
"comparador : '<'",
"comparador : '>'",
"bloque_sen_ejecutable : sen_ejecutable",
"bloque_sen_ejecutable : '{' sen_ejecutable_r '}'",
"sen_ejecutable_r : sen_ejecutable_r sen_ejecutable",
"sen_ejecutable_r : sen_ejecutable",
"imprimir : PRINT STR_1LN",
"sen_control : WHILE '(' condicion ')' DO bloque_sen_ejecutable ','",
"clase : CLASS ID '{' '}'",
"clase : CLASS ID '{' cuerpo_clase '}'",
"clase : CLASS ID ','",
"cuerpo_clase : cuerpo_clase sen_declarativa",
"cuerpo_clase : sen_declarativa",
};
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

LexicalAnalyzer lexicalAnalyzer;
SymbolTable st;

public static void main(String[] args) {
    TransitionMatrix<Integer> mI = new TransitionMatrix<>(19, 28);
    TransitionMatrix<AccionSemantica> mA = new TransitionMatrix<>(19, 28);
    SymbolTable sttemp = new SymbolTable();

    Main.loadMatrixs(mI, mA, "test.csv", sttemp);
    Parser parser = new Parser(new LexicalAnalyzer("test.txt", mI, mA), sttemp);
    parser.run();

    parser.st.print();
}

public Parser(LexicalAnalyzer lexicalAnalyzer, SymbolTable st) {
    this.lexicalAnalyzer = lexicalAnalyzer;
    this.st = st;
    yydebug = true;
}
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 11:
//#line 33 "gramatica.y"
{ System.out.println("short"); }
break;
//#line 506 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
