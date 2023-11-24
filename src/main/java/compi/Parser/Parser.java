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
import compi.AccionesSemanticas.AccionSemantica;
import compi.AssemblyGenerator.AssemblyGenerator;
import compi.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
//#line 27 "Parser.java"




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
public final static short CHECK=279;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    1,    1,    1,    1,    1,    1,    1,    2,
    2,    2,    2,    2,    7,    5,    5,    5,    5,   10,
   10,   10,   10,   10,   10,    6,    6,    8,    8,    8,
   11,   11,   15,   12,   12,   12,   12,   13,    3,    3,
    3,    3,    3,   16,   16,   16,   16,   16,   16,   17,
   17,   17,   17,   17,   20,   20,   20,   20,   22,   22,
   14,   14,   14,   14,   18,   18,   25,   26,   23,   28,
   28,   28,   28,   28,   21,   21,   21,   21,   21,   21,
   21,   21,   21,   30,   30,   30,   30,   30,   30,   30,
   31,   31,   31,   31,   29,   29,   29,   29,   29,   29,
   27,   27,   27,   27,   27,   24,   24,   24,   32,   32,
   19,   19,    4,    4,   33,    9,    9,    9,    9,    9,
   34,   35,   35,   35,   36,
};
final static short yylen[] = {                            2,
    3,    2,    2,    2,    2,    1,    1,    1,    2,    3,
    4,    2,    1,    1,    1,    1,    1,    1,    1,    1,
    1,    1,    2,    2,    2,    3,    1,    5,    5,    3,
    5,    4,    2,    2,    2,    1,    1,    2,    2,    2,
    2,    2,    2,    3,    3,    2,    2,    2,    2,    4,
    3,    3,    2,    2,    4,    3,    2,    2,    3,    3,
    7,    5,    5,    5,    4,    6,    1,    1,    3,    3,
    2,    2,    1,    1,    3,    3,    1,    4,    4,    4,
    4,    4,    4,    3,    3,    1,    4,    4,    4,    4,
    1,    2,    1,    1,    1,    1,    1,    1,    1,    1,
    1,    3,    2,    2,    2,    1,    4,    1,    2,    1,
    2,    2,    5,    4,    1,    3,    4,    4,    3,    3,
    2,    2,    2,    1,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,  115,   16,
   17,   18,   15,    0,    0,    8,    6,    7,    0,    0,
   13,   14,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    9,    0,   53,    0,    0,    0,    0,  111,  112,
    0,    0,    0,   20,   21,   22,    0,   93,    0,    0,
    0,   86,    1,    3,    4,    5,   27,    0,   19,    0,
    0,   36,   37,    0,   39,   40,   41,   42,   43,    0,
   58,    0,    0,    0,    0,    0,  124,    0,   51,    0,
    0,   59,   97,   98,   96,   95,   99,  100,    0,    0,
    0,    0,    0,    0,    0,   67,    0,  119,    0,   92,
   23,   25,   24,    0,    0,    0,    0,   10,    0,    0,
    0,   30,   34,   35,   56,    0,    0,   60,    0,    0,
  116,    0,    0,  120,  122,  123,   50,    0,   69,    0,
  105,  110,    0,    0,   65,  103,  109,   32,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   84,    0,    0,   85,   26,   11,    0,    0,    0,    0,
   55,  114,    0,  125,  117,  118,    0,  102,    0,   68,
   33,   31,    0,    0,    0,    0,    0,    0,   87,   88,
   89,   90,    0,   38,   28,   29,  113,   66,    0,    0,
  108,  106,    0,    0,    0,    0,    0,    0,    0,   63,
    0,   64,    0,   62,  107,    0,   61,
};
final static short yydgoto[] = {                          2,
   15,   62,   94,   18,   19,   58,   20,   21,   22,   48,
   23,   64,  192,  160,  140,   24,   25,   26,   27,   28,
   49,   50,   38,  193,   95,  169,   96,   90,   91,   51,
   52,   97,   30,   31,   78,  126,
};
final static short yysindex[] = {                      -103,
  -54,    0,  -15,  103,    6,  -44, -215, -188,    0,    0,
    0,    0,    0,   21,  145,    0,    0,    0, -185,  -46,
    0,    0,  177,   35,   85,  101,  102,  117,   90,    6,
  -92,    0,    3,    0,   21, -134,   79,  250,    0,    0,
  123,  129,   25,    0,    0,    0,  -71,    0,   58,  109,
   62,    0,    0,    0,    0,    0,    0,   -4,    0, -185,
  224,    0,    0,  238,    0,    0,    0,    0,    0,   87,
    0,   21,  -84,   58,  -79,  -78,    0,  335,    0,   97,
   58,    0,    0,    0,    0,    0,    0,    0,   65,  139,
   21,  103,  274,    0, -168,    0,  276,    0,    9,    0,
    0,    0,    0,  105,  111,  115,  121,    0,  -65,   55,
  191,    0,    0,    0,    0,  150,   58,    0,  281,  161,
    0,  354,   92,    0,    0,    0,    0,   21,    0,   58,
    0,    0,  303,  250,    0,    0,    0,    0,  -34,  183,
   21,   21,   21,   62,   21,   21,   21,   62,   21,   21,
    0,   21,   21,    0,    0,    0,    6,  174,  104,  106,
    0,    0,  182,    0,    0,    0,   58,    0,  -33,    0,
    0,    0,   62,   62,   62,   62,   62,   62,    0,    0,
    0,    0,   73,    0,    0,    0,    0,    0,  -17,  274,
    0,    0, -148,  -42,  -30,  262,   -7,  184,  136,    0,
  107,    0,  -21,    0,    0,  196,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,  -12,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  251,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  206,    0,  254,    0,    0,    0,    0,    0,
  -85,    0,  -39,    0,    0,    0,    0,    0,  257,  -32,
  -27,    0,    0,    0,    0,    0,    0,    1,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  260,
    0,  261,    0,  266,    0,    0,    0,    0,    0,  268,
  273,    0,    0,    0,    0,    0,    0,    0,  283,    0,
  292,    0,    0,  322,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  278,    0,    0,  -12,
    0,    0,    0,    0,    0,    0,    0,  302,    0,  304,
    0,    0,   43,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  -19,    0,    0,    0,    8,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  305,    0,    0,    0,
    0,    0,   13,   20,   33,   40,   45,   53,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0, -139,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,  512,  460,  350,   19,  307,    0,    0,    0,    0,
    0,  308,  -77,    0,    0,    0,    0,    0,    0,    0,
  517,  513,    2, -130,    0,    0,  -74,    0,  282,  284,
  313,  -57,    0,    0,  294,  301,
};
final static int YYTABLESIZE=712;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         40,
   12,   91,   91,   91,   91,   91,   14,   91,   94,   94,
   94,   94,   94,   77,   94,   77,   77,   77,   14,    1,
   91,   75,   91,   75,   75,   75,  191,   94,   32,   94,
   76,   75,   77,  159,   77,  133,  191,  121,   60,  108,
   75,   41,   75,   79,  163,   37,  121,   47,   76,  138,
   76,   76,   76,   78,  109,   78,   78,   78,  195,  170,
   79,   12,   79,   79,   79,   47,  203,   76,   42,   76,
   36,   57,   78,   80,   78,   80,   80,   80,   65,   79,
   81,   79,   81,   81,   81,   82,  104,   82,   82,   82,
  134,  135,   80,   83,   80,   83,   83,   83,  156,   81,
  104,   81,  105,  106,   82,  194,   82,  104,  107,  105,
  197,  198,   83,  109,   83,  194,  191,  139,  201,  104,
  104,  201,   82,   47,   87,   12,   88,  115,   66,   70,
   71,   47,  196,   14,   47,   73,  199,  127,   87,  104,
   88,  105,   33,   34,   67,   68,  142,  141,   36,   47,
   72,  143,  146,  145,   73,   47,  149,  147,  183,   47,
   69,  150,  152,   35,   59,   47,   98,  153,   99,    7,
    8,  121,  118,   10,   11,   12,  121,  121,  120,  129,
  121,  121,  121,    7,    8,  119,   13,   10,   11,   12,
  161,  155,  104,  121,  105,  190,   14,  101,  102,  103,
   13,    3,    4,    5,  164,   14,    6,    7,    8,    9,
   59,   10,   11,   12,   92,    5,  166,  184,    6,   10,
   11,   12,  171,  172,   13,  187,  188,  204,  185,  200,
  186,  205,   39,   91,   91,   91,   91,   14,  206,  207,
   94,   94,   94,   94,   19,   77,   77,   77,   77,   54,
    2,   14,  202,   75,   75,   75,   75,   12,   12,   43,
  158,   12,   12,   12,   12,   59,   12,   12,   12,   53,
  158,   44,   45,   46,   10,   11,   12,   43,   12,   12,
   76,   76,   76,   76,   14,   78,   78,   78,   78,   44,
   45,   46,   79,   79,   79,   79,  100,   46,   14,   61,
   47,  104,  104,   57,   48,   80,   80,   80,   80,   49,
   14,   52,   81,   81,   81,   81,   44,   82,   82,   82,
   82,   45,   14,   73,  162,   83,   83,   83,   83,   92,
    5,  189,   74,    6,   14,   43,   14,   83,   84,   85,
   86,   14,   71,   43,   72,   70,   43,   44,   45,   46,
  158,   83,   84,   85,   86,   44,   45,   46,   44,   45,
   46,   43,  112,   14,   56,  101,  110,   43,  111,  122,
  128,   43,   93,   44,   45,   46,  123,   43,    0,   44,
   45,   46,  110,   44,   45,   46,  168,  144,  148,   44,
   45,   46,   92,    5,    0,    0,    6,    0,  131,    0,
  136,    4,    5,   93,    0,    6,    7,    8,    9,    0,
   10,   11,   12,  158,    0,    0,    0,    0,  151,  154,
    0,    0,    0,   13,  173,  174,  175,  168,  176,  177,
  178,    0,    0,    4,    5,    0,    0,    6,    7,    8,
    0,    0,   10,   11,   12,    0,  110,    4,  157,    0,
    0,    6,    7,    8,    0,   13,   10,   11,   12,  124,
   17,  179,  180,    0,  181,  182,    0,    0,  158,   13,
    0,    0,    0,    0,   55,    0,    0,    0,  165,    0,
    4,    5,   63,    0,    6,    7,    8,    0,    0,   10,
   11,   12,    0,    0,    4,    5,    0,    0,    6,    7,
    8,    0,   13,   10,   11,   12,   92,    5,    0,    0,
    6,    0,   16,   29,    0,    0,   13,    0,   92,    5,
   63,    0,    6,  114,    0,    0,   54,   29,    0,    0,
   92,    5,   92,    5,    6,   29,    6,   92,    5,  158,
    0,    6,   77,    0,    0,   74,    0,    0,    0,   80,
   29,   81,  132,   89,    0,    0,  137,    0,    0,   92,
    5,    0,    0,    6,    0,    0,    0,    0,    0,    0,
  114,    0,    0,   29,    0,  113,   29,    0,  110,  110,
  101,  101,  110,    0,    0,    0,  116,   77,  117,  125,
    0,  120,  137,    0,    0,    0,    7,    8,    0,    0,
   10,   11,   12,    0,    0,   29,    0,  130,    0,   29,
  120,    0,    0,   13,    0,    7,    8,    0,    0,   10,
   11,   12,  113,   29,    0,    0,    0,    0,    0,    0,
    0,   29,   13,  125,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  167,   29,   29,    0,    0,  132,
    0,    0,    0,  132,    0,  137,    0,    0,  137,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   29,    0,    0,    0,    0,
    0,    0,   29,    0,    0,    0,   29,    0,   29,    0,
    0,   29,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         44,
    0,   41,   42,   43,   44,   45,   61,   47,   41,   42,
   43,   44,   45,   41,   47,   43,   44,   45,   61,  123,
   60,   41,   62,   43,   44,   45,   44,   60,   44,   62,
  123,   30,   60,  111,   62,   93,   44,  123,   20,   44,
   60,  257,   62,   41,  119,   40,  125,   45,   41,   41,
   43,   44,   45,   41,   59,   43,   44,   45,  189,  134,
   41,   61,   43,   44,   45,   45,  197,   60,  257,   62,
   46,  257,   60,   41,   62,   43,   44,   45,   44,   60,
   41,   62,   43,   44,   45,   41,   44,   43,   44,   45,
  259,  260,   60,   41,   62,   43,   44,   45,   44,   60,
   43,   62,   45,   42,   60,  123,   62,   43,   47,   45,
  259,  260,   60,   59,   62,  123,   44,   99,  196,  259,
  260,  199,  257,   45,   60,  125,   62,   41,   44,   40,
   41,   45,  190,   61,   45,   46,  194,   41,   60,   43,
   62,   45,   40,   41,   44,   44,   42,   43,   46,   45,
   61,   47,   42,   43,   46,   45,   42,   47,  157,   45,
   44,   47,   42,   61,  257,   45,   44,   47,   40,  262,
  263,  257,  257,  266,  267,  268,  262,  263,  257,   41,
  266,  267,  268,  262,  263,  265,  279,  266,  267,  268,
   41,  257,   43,  279,   45,  123,   61,  269,  270,  271,
  279,  256,  257,  258,   44,   61,  261,  262,  263,  264,
  257,  266,  267,  268,  257,  258,  125,   44,  261,  266,
  267,  268,  257,   41,  279,   44,  260,   44,  125,  260,
  125,  125,  277,  273,  274,  275,  276,   61,  260,   44,
  273,  274,  275,  276,  257,  273,  274,  275,  276,   44,
    0,   61,  260,  273,  274,  275,  276,  257,  258,  257,
  278,  261,  262,  263,  264,  257,  266,  267,  268,  125,
  278,  269,  270,  271,  266,  267,  268,  257,  278,  279,
  273,  274,  275,  276,   61,  273,  274,  275,  276,  269,
  270,  271,  273,  274,  275,  276,  272,   44,   61,  123,
   44,  259,  260,   44,   44,  273,  274,  275,  276,   44,
   61,   44,  273,  274,  275,  276,   44,  273,  274,  275,
  276,   44,   61,   41,   44,  273,  274,  275,  276,  257,
  258,  259,   41,  261,   61,  257,   61,  273,  274,  275,
  276,   61,   41,  257,   41,   41,  257,  269,  270,  271,
  278,  273,  274,  275,  276,  269,  270,  271,  269,  270,
  271,  257,  125,   61,   15,   44,   60,  257,   61,   76,
   89,  257,  123,  269,  270,  271,   76,  257,   -1,  269,
  270,  271,   61,  269,  270,  271,  125,  104,  105,  269,
  270,  271,  257,  258,   -1,   -1,  261,   -1,  125,   -1,
  125,  257,  258,  123,   -1,  261,  262,  263,  264,   -1,
  266,  267,  268,  278,   -1,   -1,   -1,   -1,  106,  107,
   -1,   -1,   -1,  279,  141,  142,  143,  125,  145,  146,
  147,   -1,   -1,  257,  258,   -1,   -1,  261,  262,  263,
   -1,   -1,  266,  267,  268,   -1,  125,  257,  258,   -1,
   -1,  261,  262,  263,   -1,  279,  266,  267,  268,  125,
    1,  149,  150,   -1,  152,  153,   -1,   -1,  278,  279,
   -1,   -1,   -1,   -1,   15,   -1,   -1,   -1,  125,   -1,
  257,  258,   23,   -1,  261,  262,  263,   -1,   -1,  266,
  267,  268,   -1,   -1,  257,  258,   -1,   -1,  261,  262,
  263,   -1,  279,  266,  267,  268,  257,  258,   -1,   -1,
  261,   -1,    1,    1,   -1,   -1,  279,   -1,  257,  258,
   61,   -1,  261,   64,   -1,   -1,   15,   15,   -1,   -1,
  257,  258,  257,  258,  261,   23,  261,  257,  258,  278,
   -1,  261,   31,   -1,   -1,   29,   -1,   -1,   -1,   33,
   38,   35,   93,   37,   -1,   -1,   97,   -1,   -1,  257,
  258,   -1,   -1,  261,   -1,   -1,   -1,   -1,   -1,   -1,
  111,   -1,   -1,   61,   -1,   64,   64,   -1,  257,  258,
  259,  260,  261,   -1,   -1,   -1,   70,   76,   72,   78,
   -1,  257,  133,   -1,   -1,   -1,  262,  263,   -1,   -1,
  266,  267,  268,   -1,   -1,   93,   -1,   91,   -1,   97,
  257,   -1,   -1,  279,   -1,  262,  263,   -1,   -1,  266,
  267,  268,  111,  111,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  119,  279,  122,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  128,  133,  134,   -1,   -1,  190,
   -1,   -1,   -1,  194,   -1,  196,   -1,   -1,  199,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  183,   -1,   -1,   -1,   -1,
   -1,   -1,  190,   -1,   -1,   -1,  194,   -1,  196,   -1,
   -1,  199,
};
}
final static short YYFINAL=2;
final static short YYMAXTOKEN=279;
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
"CHECK",
};
final static String yyrule[] = {
"$accept : prog",
"prog : '{' sentencias '}'",
"prog : '{' sentencias",
"sentencias : sentencias sen_declarativa",
"sentencias : sentencias sen_ejecutable",
"sentencias : sentencias sen_control",
"sentencias : sen_ejecutable",
"sentencias : sen_control",
"sentencias : sen_declarativa",
"sentencias : error ','",
"sen_declarativa : tipo list_var ','",
"sen_declarativa : sentencia_check tipo list_var ','",
"sen_declarativa : tipo list_var",
"sen_declarativa : funcion",
"sen_declarativa : clase",
"sentencia_check : CHECK",
"tipo : SHORT",
"tipo : UINT",
"tipo : FLOAT",
"tipo : ID",
"CTE : CTE_SHORT",
"CTE : CTE_UINT",
"CTE : CTE_FLOAT",
"CTE : '-' CTE_SHORT",
"CTE : '-' CTE_FLOAT",
"CTE : '-' CTE_UINT",
"list_var : list_var ';' ID",
"list_var : ID",
"funcion : header_funcion '{' cuerpo_funcion sen_retorno '}'",
"funcion : header_funcion '{' cuerpo_funcion seleccion_func '}'",
"funcion : header_funcion cuerpo_funcion '}'",
"header_funcion : VOID ID '(' parametro ')'",
"header_funcion : VOID ID '(' ')'",
"parametro : tipo ID",
"cuerpo_funcion : cuerpo_funcion sen_declarativa",
"cuerpo_funcion : cuerpo_funcion sen_ejecutable",
"cuerpo_funcion : sen_declarativa",
"cuerpo_funcion : sen_ejecutable",
"sen_retorno : RETURN ','",
"sen_ejecutable : asignacion ','",
"sen_ejecutable : inv_funcion ','",
"sen_ejecutable : seleccion ','",
"sen_ejecutable : imprimir ','",
"sen_ejecutable : inv_metodo ','",
"asignacion : ID '=' exp_aritmetica",
"asignacion : atributo_clase '=' exp_aritmetica",
"asignacion : ID '='",
"asignacion : '=' exp_aritmetica",
"asignacion : atributo_clase '='",
"asignacion : atributo_clase exp_aritmetica",
"inv_funcion : ID '(' exp_aritmetica ')'",
"inv_funcion : ID '(' ')'",
"inv_funcion : ID '(' exp_aritmetica",
"inv_funcion : ID ')'",
"inv_funcion : ID '('",
"inv_metodo : atributo_clase '(' exp_aritmetica ')'",
"inv_metodo : atributo_clase '(' ')'",
"inv_metodo : atributo_clase '('",
"inv_metodo : atributo_clase ')'",
"atributo_clase : ID '.' ID",
"atributo_clase : atributo_clase '.' ID",
"seleccion_func : IF condicion bloque_ejecutable_func ELSE bloque_ejecutable_func END_IF ','",
"seleccion_func : IF condicion bloque_ejecutable_func END_IF ','",
"seleccion_func : IF condicion ELSE bloque_ejecutable_func END_IF",
"seleccion_func : IF condicion bloque_ejecutable_func ELSE END_IF",
"seleccion : IF condicion cuerpo_then END_IF",
"seleccion : IF condicion cuerpo_then ELSE cuerpo_else END_IF",
"cuerpo_then : bloque_sen_ejecutable",
"cuerpo_else : bloque_sen_ejecutable",
"condicion : '(' exp_logica ')'",
"exp_logica : exp_aritmetica comparador exp_aritmetica",
"exp_logica : exp_aritmetica comparador",
"exp_logica : comparador exp_aritmetica",
"exp_logica : exp_aritmetica",
"exp_logica : comparador",
"exp_aritmetica : exp_aritmetica '+' termino",
"exp_aritmetica : exp_aritmetica '-' termino",
"exp_aritmetica : termino",
"exp_aritmetica : exp_aritmetica '+' '+' termino",
"exp_aritmetica : exp_aritmetica '+' '*' termino",
"exp_aritmetica : exp_aritmetica '+' '/' termino",
"exp_aritmetica : exp_aritmetica '-' '+' termino",
"exp_aritmetica : exp_aritmetica '-' '*' termino",
"exp_aritmetica : exp_aritmetica '-' '/' termino",
"termino : termino '*' factor",
"termino : termino '/' factor",
"termino : factor",
"termino : termino '*' '*' factor",
"termino : termino '*' '/' factor",
"termino : termino '/' '*' factor",
"termino : termino '/' '/' factor",
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
"bloque_sen_ejecutable : sen_ejecutable_r '}'",
"bloque_sen_ejecutable : '{' sen_ejecutable_r",
"bloque_sen_ejecutable : '{' '}'",
"bloque_ejecutable_func : sen_retorno",
"bloque_ejecutable_func : '{' sen_ejecutable_r sen_retorno '}'",
"bloque_ejecutable_func : ','",
"sen_ejecutable_r : sen_ejecutable_r sen_ejecutable",
"sen_ejecutable_r : sen_ejecutable",
"imprimir : PRINT STR_1LN",
"imprimir : PRINT ','",
"sen_control : inicio_while condicion DO bloque_sen_ejecutable ','",
"sen_control : inicio_while condicion DO ','",
"inicio_while : WHILE",
"clase : class_header '{' '}'",
"clase : class_header '{' cuerpo_clase '}'",
"clase : class_header '{' herencia_clase '}'",
"clase : CLASS ID ','",
"clase : class_header cuerpo_clase '}'",
"class_header : CLASS ID",
"cuerpo_clase : cuerpo_clase sen_declarativa",
"cuerpo_clase : cuerpo_clase herencia_clase",
"cuerpo_clase : sen_declarativa",
"herencia_clase : ID ','",
};

//#line 290 "gramatica.y"
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
    if (declarandoInstancia)
        declararAtributos(ptr);
}

public void declararAtributos(Integer ptr) {
    Integer tipo = Integer.parseInt(st.getAttribute(ptr, "tipo"));
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
            
            Integer ptr_ins = st.addEntry(instance_att, ID);
            st.setAttribute(ptr_ins, "tipo", st.getAttribute(Integer.parseInt(att_cls), "tipo"));
            st.setAttribute(ptr_ins, "valid", "1");
            st.setAttribute(ptr_ins, "uso", "identificador");
            // si es una instancia, hay que declarar los atributos de la clase
            if (isInstancia(ptr_ins))
                declararAtributos(ptr_ins);
        }
    }
}

public boolean isInstancia(Integer ptr) {
    Integer tipo = Integer.parseInt(st.getAttribute(ptr, "tipo"));
    return lexicalAnalyzer.getReservedWord(tipo) == null;
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
    ptr_lhs = obtenerAtributoInstancia(ptr_lhs, ptr_rhs, ambito);
    if (ptr_lhs == 0) {
        agregarError(errores_semanticos, Parser.ERROR,
                String.format(ERROR_ALCANCE, st.getLexema(ptr_rhs), ambito.toString()));
        return 0;
    }
    return ptr_lhs;
}

public Integer obtenerAtributoInstancia(Integer ptr_lhs, Integer ptr_rhs, Ambito ambito) {
    // ptr_lhs puede ser un id o un atributo_clase, eg: c1.c2:global:main, c1, var1
    // ptr_rhs es el lexema de un id detectado por lex, eg:var1, obj2
    if (!st.getLexema(ptr_lhs).contains("."))
        ptr_lhs = getInstancia(ptr_lhs, ambito);
    // c2:global:main -> c2,global,main -> c2.c1:global:main
    if (ptr_lhs == 0) return 0; // si no lo encuentra devuelve 0

    String[] lhs_parts = st.getLexema(ptr_lhs).split(":");
    String lhs = lhs_parts[0] + "." + st.getLexema(ptr_rhs); // c2.c1
    for (int i = 1; i < lhs_parts.length; i++)
        lhs += ":" + lhs_parts[i]; // c2.c1:global:main

    return st.getPtr(lhs);
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

private Integer getClase(Integer id, Ambito ambito, Boolean instancia) {
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
    if (instancia)
        return id;
    else
        return Integer.parseInt(tipo);
}

private Integer getClase(Integer id, Ambito ambito) {
    return getClase(id, ambito, false);
}

private Integer getInstancia(Integer id, Ambito ambito) {
    return getClase(id, ambito, true);
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
    Parser parser = new Parser(new LexicalAnalyzer("test.txt", mI, mA, errores_lexicos), sttemp);
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
//#line 1167 "Parser.java"
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
case 2:
//#line 23 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al final del programa");}
break;
case 9:
//#line 32 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una sentencia ejecutable");}
break;
case 10:
//#line 35 "gramatica.y"
{ if (declarandoInstancia) declarandoInstancia = false; }
break;
case 11:
//#line 36 "gramatica.y"
{ check=false; if (declarandoInstancia) declarandoInstancia = false; }
break;
case 12:
//#line 37 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una ,");}
break;
case 15:
//#line 42 "gramatica.y"
{ check=true; }
break;
case 16:
//#line 45 "gramatica.y"
{ yyval.ival = SHORT; tipo = (int)SHORT; }
break;
case 17:
//#line 46 "gramatica.y"
{ yyval.ival = UINT; tipo = (int)UINT; }
break;
case 18:
//#line 47 "gramatica.y"
{ yyval.ival = FLOAT; tipo = (int)FLOAT; }
break;
case 19:
//#line 48 "gramatica.y"
{ yyval.ival = getTipoClase(val_peek(0).ival, ambitoActual.copy()); tipo = yyval.ival; declarandoInstancia = true; }
break;
case 20:
//#line 51 "gramatica.y"
{verificarRango(val_peek(0).ival); yyval.ival = val_peek(0).ival; yyval.dval = SHORT;}
break;
case 21:
//#line 52 "gramatica.y"
{yyval.ival = val_peek(0).ival; yyval.dval = UINT;}
break;
case 22:
//#line 53 "gramatica.y"
{verificarRango(val_peek(0).ival); yyval.ival = val_peek(0).ival; yyval.dval = FLOAT;}
break;
case 23:
//#line 54 "gramatica.y"
{resolverSigno(val_peek(0).ival); verificarRango(val_peek(0).ival); yyval.ival = val_peek(0).ival; yyval.dval = SHORT; }
break;
case 24:
//#line 55 "gramatica.y"
{resolverSigno(val_peek(0).ival); verificarRango(val_peek(0).ival); yyval.ival = val_peek(0).ival; yyval.dval = FLOAT; }
break;
case 25:
//#line 56 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "No se puede negar un unsigned int");}
break;
case 26:
//#line 59 "gramatica.y"
{ declararVariable(val_peek(0).ival); }
break;
case 27:
//#line 60 "gramatica.y"
{ declararVariable(val_peek(0).ival); }
break;
case 28:
//#line 63 "gramatica.y"
{ ambitoActual.pop(); crearTerceto("RET", -1, -1, null, null); }
break;
case 29:
//#line 64 "gramatica.y"
{ ambitoActual.pop(); crearTerceto("RET", -1, -1, null, null); }
break;
case 30:
//#line 65 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al comienzo de la función");}
break;
case 31:
//#line 68 "gramatica.y"
{ agregarFuncion(val_peek(3).ival, VOID, val_peek(1).ival);
                                                    if (val_peek(1).ival != 0)
                                                        st.setLexema(val_peek(1).ival, st.getLexema(val_peek(1).ival) + ":" + ambitoActual.toString());
                                                 }
break;
case 32:
//#line 72 "gramatica.y"
{ agregarFuncion(val_peek(2).ival, VOID, null); }
break;
case 33:
//#line 75 "gramatica.y"
{   if (tipo == 0) {
                                                    agregarError(errores_semanticos, Parser.ERROR,
                                                            String.format(ERROR_TIPO, st.getLexema(val_peek(0).ival)));
                                                    yyval.ival = 0;
                                                    break;
                                                }
                                                st.setAttribute(val_peek(0).ival, "tipo", ""+val_peek(1).ival);
                                                yyval.ival = val_peek(0).ival;
                                            }
break;
case 44:
//#line 103 "gramatica.y"
{ yyval.ival = crearTercetoAsignacion(val_peek(2).ival, val_peek(0)); yyval.sval = "terceto"; if (yyval.ival != 0) yyval.dval = val_peek(2).dval; }
break;
case 45:
//#line 104 "gramatica.y"
{ yyval.ival = crearTercetoAsignacion(val_peek(2).ival, val_peek(0)); yyval.sval = "terceto"; if (yyval.ival != 0) yyval.dval = val_peek(2).dval; }
break;
case 46:
//#line 105 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una expresión aritmética");}
break;
case 47:
//#line 106 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un identificador");}
break;
case 48:
//#line 108 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una expresión aritmética");}
break;
case 49:
//#line 109 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un signo igual");}
break;
case 50:
//#line 112 "gramatica.y"
{ invocacionFuncion(val_peek(3).ival, val_peek(1)); }
break;
case 51:
//#line 113 "gramatica.y"
{ invocacionFuncion(val_peek(2).ival); }
break;
case 52:
//#line 114 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
break;
case 53:
//#line 116 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
break;
case 54:
//#line 117 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
break;
case 55:
//#line 120 "gramatica.y"
{ if (val_peek(3).ival != 0) invocacionFuncion(val_peek(3).ival, val_peek(1)); }
break;
case 56:
//#line 121 "gramatica.y"
{ if (val_peek(2).ival != 0) invocacionFuncion(val_peek(2).ival); }
break;
case 57:
//#line 122 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
break;
case 58:
//#line 123 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
break;
case 59:
//#line 126 "gramatica.y"
{ yyval.ival = agregarAtributo(val_peek(2).ival, val_peek(0).ival, ambitoActual.copy()); }
break;
case 60:
//#line 127 "gramatica.y"
{ yyval.ival = agregarAtributo(val_peek(2).ival, val_peek(0).ival, ambitoActual.copy()); }
break;
case 61:
//#line 130 "gramatica.y"
{agregarEstructura("IF");}
break;
case 62:
//#line 131 "gramatica.y"
{agregarEstructura("IF");}
break;
case 63:
//#line 132 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un bloque de sentencias ejecutables");}
break;
case 64:
//#line 133 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un bloque de sentencias ejecutables");}
break;
case 65:
//#line 136 "gramatica.y"
{ agregarEstructura("IF");
                                                         completarB("BI", pilaTercetos.getContador()); }
break;
case 66:
//#line 138 "gramatica.y"
{ agregarEstructura("IF"); 
                                                                          completarB("BI", pilaTercetos.getContador()+1);
                                                                          crearTerceto("END_IF"+countIF++, -1, -1, "", ""); }
break;
case 67:
//#line 143 "gramatica.y"
{ crearTerceto("BI", -1, -1, "", "");
                                               completarB("BF", pilaTercetos.getContador()+1);
                                               crearTerceto("END_IF"+countIF++, -1, -1, "", ""); }
break;
case 69:
//#line 151 "gramatica.y"
{ yyval.ival = crearTerceto("BF", val_peek(1).ival, -1, val_peek(1).sval, ""); }
break;
case 70:
//#line 154 "gramatica.y"
{   if (val_peek(2).ival == 0 || val_peek(0).ival == 0) break;

                                                                    if (val_peek(2).dval != val_peek(0).dval) {
                                                                        agregarError(errores_semanticos, Parser.ERROR,
                                                                            String.format(ERROR_TIPOS_INCOMPATIBLES, val_peek(2).sval, val_peek(0).sval));
                                                                        break;
                                                                    }
                                                                    yyval.ival = crearTerceto(getCmp(val_peek(1).ival), val_peek(2).ival, val_peek(0).ival, val_peek(2).sval, val_peek(0).sval);
                                                                    yyval.sval = "terceto";
                                                                }
break;
case 71:
//#line 164 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una expresión aritmética");}
break;
case 72:
//#line 165 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una expresión aritmética");}
break;
case 73:
//#line 166 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un comparador");}
break;
case 74:
//#line 167 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba dos expresión aritmética");}
break;
case 75:
//#line 172 "gramatica.y"
{ yyval.ival = crearTercetoExp(val_peek(2), val_peek(0), "+"); yyval.sval = "terceto"; if (yyval.ival != 0) yyval.dval = val_peek(2).dval; }
break;
case 76:
//#line 173 "gramatica.y"
{ yyval.ival = crearTercetoExp(val_peek(2), val_peek(0), "-"); yyval.sval = "terceto"; if (yyval.ival != 0) yyval.dval = val_peek(2).dval; }
break;
case 77:
//#line 174 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 78:
//#line 175 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
break;
case 79:
//#line 176 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
break;
case 80:
//#line 177 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
break;
case 81:
//#line 178 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
break;
case 82:
//#line 179 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
break;
case 83:
//#line 180 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
break;
case 84:
//#line 183 "gramatica.y"
{ yyval.ival = crearTercetoTermino(val_peek(2), val_peek(0), "*"); yyval.sval = "terceto"; if (yyval.ival != 0) yyval.dval = val_peek(2).dval; }
break;
case 85:
//#line 184 "gramatica.y"
{ yyval.ival = crearTercetoTermino(val_peek(2), val_peek(0), "/"); yyval.sval = "terceto"; if (yyval.ival != 0) yyval.dval = val_peek(2).dval; }
break;
case 86:
//#line 185 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 87:
//#line 186 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
break;
case 88:
//#line 187 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
break;
case 89:
//#line 188 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
break;
case 90:
//#line 189 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
break;
case 91:
//#line 193 "gramatica.y"
{   yyval.ival = st.getPtr(st.getLexema(val_peek(0).ival), ambitoActual.copy(), "identificador");
                                                    if (yyval.ival == 0) {
                                                        agregarError(errores_semanticos, Parser.ERROR,
                                                            String.format(ERROR_ALCANCE, st.getLexema(val_peek(0).ival), ambitoActual.toString()));
                                                        break;
                                                    }
                                                    String check_lhs = st.getAttribute(yyval.ival,"check_lhs");
                                                    String check_rhs = st.getAttribute(yyval.ival,"check_rhs");
                                                    if (check_rhs != null && check_rhs.equals("false")){
                                                        st.setAttribute(yyval.ival, "check_rhs", "true");
                                                        if (check_lhs.equals("true")) {
                                                          agregarEstructura(String.format(ESTRUCTURA_CHECK,st.getLexema(yyval.ival)));
                                                        }
                                                    }
                                                    yyval.sval = "st";
                                                    yyval.dval = Integer.parseInt(st.getAttribute(yyval.ival, "tipo")); /*chequear*/
                                                }
break;
case 92:
//#line 210 "gramatica.y"
{ yyval = crearTercetoIncrement(val_peek(1).ival); }
break;
case 93:
//#line 211 "gramatica.y"
{ yyval.ival = val_peek(0).ival; yyval.sval = "st"; yyval.dval = val_peek(0).dval; }
break;
case 94:
//#line 212 "gramatica.y"
{   yyval.ival = st.getPtr(st.getLexema(val_peek(0).ival), ambitoActual.copy());
                                                    if (yyval.ival != 0) {
                                                        yyval.sval = "st";
                                                        yyval.dval = Integer.parseInt(st.getAttribute(yyval.ival, "tipo"));
                                                        String check_lhs = st.getAttribute(yyval.ival,"check_lhs");
                                                        String check_rhs = st.getAttribute(yyval.ival,"check_rhs");
                                                        if (check_rhs != null && check_rhs.equals("false")){
                                                            st.setAttribute(yyval.ival, "check_rhs", "true");
                                                            if (check_lhs.equals("true")) {
                                                                agregarEstructura(String.format(ESTRUCTURA_CHECK,st.getLexema(yyval.ival)));
                                                            }
                                                        }
                                                    }
                                                }
break;
case 95:
//#line 228 "gramatica.y"
{ yyval.ival = NOT_EQUAL; }
break;
case 96:
//#line 229 "gramatica.y"
{ yyval.ival = EQUAL; }
break;
case 97:
//#line 230 "gramatica.y"
{ yyval.ival = GREATER_EQUAL; }
break;
case 98:
//#line 231 "gramatica.y"
{ yyval.ival = LESS_EQUAL; }
break;
case 99:
//#line 232 "gramatica.y"
{ yyval.ival = 60; }
break;
case 100:
//#line 233 "gramatica.y"
{ yyval.ival = 62; }
break;
case 103:
//#line 239 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al comienzo del bloque de sentencias ejecutables");}
break;
case 104:
//#line 240 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al final del bloque de sentencias ejecutables");}
break;
case 105:
//#line 241 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera un bloque de sentencias ejecutables");}
break;
case 111:
//#line 254 "gramatica.y"
{ crearTerceto("PRINT", val_peek(0).ival, -1, "st", null);
                                                   st.setAttribute(val_peek(0).ival, "valid", "1"); }
break;
case 112:
//#line 256 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una cadena para imprimir");}
break;
case 113:
//#line 259 "gramatica.y"
{ agregarEstructura("WHILE");
                                                                             crearTerceto("BI", -1, -1, "", "");
                                                                             completarB("BF", pilaTercetos.getContador()+1);
                                                                             completarWhile(); }
break;
case 114:
//#line 263 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un bloque de sentencias ejecutables");}
break;
case 115:
//#line 266 "gramatica.y"
{ inicio_while = pilaTercetos.getContador()+1; }
break;
case 116:
//#line 269 "gramatica.y"
{ ambitoActual.pop(); claseActual = null; }
break;
case 117:
//#line 270 "gramatica.y"
{ ambitoActual.pop(); claseActual = null; }
break;
case 118:
//#line 271 "gramatica.y"
{ ambitoActual.pop(); claseActual = null; }
break;
case 119:
//#line 272 "gramatica.y"
{ agregarClase(val_peek(1).ival, "FDCLASS"); ambitoActual.pop(); claseActual = null; }
break;
case 120:
//#line 273 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al final de la clase");
                                                             ambitoActual.pop(); }
break;
case 121:
//#line 277 "gramatica.y"
{ agregarClase(val_peek(0).ival, "CLASS"); }
break;
case 125:
//#line 286 "gramatica.y"
{ heredar(val_peek(1).ival); }
break;
//#line 1755 "Parser.java"
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
