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






//#line 2 ".\gramatica.y"
package compi.Parser;
import compi.AccionesSemanticas.AccionSemantica;
import compi.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
//#line 26 "Parser.java"




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
    0,    0,    1,    1,    1,    1,    1,    1,    1,    2,
    2,    2,    5,    5,    5,    5,    9,    9,    9,    9,
    9,    9,    6,    6,    7,    7,    7,   10,   10,   14,
   11,   11,   11,   11,   12,    3,    3,    3,    3,    3,
   15,   15,   15,   15,   15,   15,   16,   16,   16,   16,
   16,   19,   19,   19,   19,   21,   21,   13,   13,   13,
   13,   17,   17,   24,   25,   22,   27,   27,   27,   27,
   27,   20,   20,   20,   20,   20,   20,   20,   20,   20,
   29,   29,   29,   29,   29,   29,   29,   30,   30,   30,
   30,   28,   28,   28,   28,   28,   28,   26,   26,   26,
   26,   26,   23,   23,   23,   31,   31,   18,   18,    4,
    4,   32,    8,    8,    8,    8,    8,   33,   34,   34,
   34,   35,
};
final static short yylen[] = {                            2,
    3,    2,    2,    2,    2,    1,    1,    1,    2,    3,
    1,    1,    1,    1,    1,    1,    1,    1,    1,    2,
    2,    2,    3,    1,    5,    5,    3,    5,    4,    2,
    2,    2,    1,    1,    2,    2,    2,    2,    2,    2,
    3,    3,    2,    2,    2,    2,    4,    3,    3,    2,
    2,    4,    3,    2,    2,    3,    3,    7,    5,    5,
    5,    4,    6,    1,    1,    3,    3,    2,    2,    1,
    1,    3,    3,    1,    4,    4,    4,    4,    4,    4,
    3,    3,    1,    4,    4,    4,    4,    1,    2,    1,
    1,    1,    1,    1,    1,    1,    1,    1,    3,    2,
    2,    2,    1,    4,    1,    2,    1,    2,    2,    5,
    4,    1,    3,    4,    4,    3,    3,    2,    2,    2,
    1,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,  112,   13,
   14,   15,    0,    0,    8,    6,    7,    0,   11,   12,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    9,
    0,   50,    0,    0,    0,    0,  108,  109,    0,    0,
    0,   17,   18,   19,    0,   90,    0,    0,    0,   83,
    1,    3,    4,    5,   24,    0,    0,   33,   34,    0,
   36,   37,   38,   39,   40,    0,   55,    0,    0,    0,
    0,   16,    0,  121,    0,   48,    0,    0,   56,   94,
   95,   93,   92,   96,   97,    0,    0,    0,    0,    0,
    0,    0,   64,    0,  116,    0,   89,   20,   22,   21,
    0,    0,    0,    0,   10,    0,    0,   27,   31,   32,
   53,    0,    0,   57,    0,    0,  113,    0,    0,  117,
  119,  120,   47,    0,   66,    0,  102,  107,    0,    0,
   62,  100,  106,   29,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   81,    0,    0,   82,
   23,    0,    0,    0,    0,   52,  111,    0,  122,  114,
  115,    0,   99,    0,   65,   30,   28,    0,    0,    0,
    0,    0,    0,   84,   85,   86,   87,    0,   35,   25,
   26,  110,   63,    0,    0,  105,  103,    0,    0,    0,
    0,    0,    0,    0,   60,    0,   61,    0,   59,  104,
    0,   58,
};
final static short yydgoto[] = {                          2,
   14,   58,   91,   17,   18,   56,   19,   20,   46,   21,
   60,  187,  155,  136,   22,   23,   24,   25,   26,   47,
   48,   36,  188,   92,  164,   93,   87,   88,   49,   50,
   94,   28,   29,   75,  122,
};
final static short yysindex[] = {                      -106,
  148,    0,    6,   62,   23,  -39, -172, -165,    0,    0,
    0,    0,   12,  164,    0,    0,    0, -147,    0,    0,
  199,   83,  116,  117,  131,  153,   74,   23,  -94,    0,
   89,    0,   12, -121,   66,  227,    0,    0,  160,  152,
  -16,    0,    0,    0, -105,    0,   17,  105,   71,    0,
    0,    0,    0,    0,    0,   50,  215,    0,    0,  176,
    0,    0,    0,    0,    0,   92,    0,   12, -117,   17,
  -60,    0,  -79,    0,  -67,    0,  103,   17,    0,    0,
    0,    0,    0,    0,    0,   45,  165,   12,   62,  -43,
    0, -138,    0,  229,    0,  118,    0,    0,    0,    0,
   96,  107,  100,  111,    0,  -47,  130,    0,    0,    0,
    0,  179,   17,    0,   34,  172,    0,  -55,   98,    0,
    0,    0,    0,   12,    0,   17,    0,    0,  269,  227,
    0,    0,    0,    0,  -40,  180,   12,   12,   12,   71,
   12,   12,   12,   71,   12,   12,    0,   12,   12,    0,
    0,   23,  182,  104,  123,    0,    0,  192,    0,    0,
    0,   17,    0,  -10,    0,    0,    0,   71,   71,   71,
   71,   71,   71,    0,    0,    0,    0,   56,    0,    0,
    0,    0,    0,  -19,  -43,    0,    0, -135,  188,   -3,
   94,  -17,  207,  142,    0,  133,    0,    2,    0,    0,
  219,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,   13,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  268,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  249,    0,  250,    0,    0,    0,    0,    0,  -86,    0,
  -41,    0,    0,    0,    0,    0,  252,  -34,  -29,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  264,    0,  272,    0,  280,
    0,    0,    0,    0,    0,    0,  288,  289,    0,    0,
    0,    0,    0,    0,    0,  230,    0,  297,    0,    0,
   68,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  303,    0,    0,   13,    0,    0,    0,    0,
    0,    0,    0,  307,    0,  315,    0,    0,   43,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -21,
    0,    0,    0,   -9,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  332,    0,    0,    0,    0,    0,   -1,    4,   11,
   24,   31,   36,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
 -128,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,
};
final static short yygindex[] = {                         0,
    0,  440,  450,  360,  283,    0,    0,    0,    0,    0,
  326,  -24,    0,    0,    0,    0,    0,    0,    0,  466,
  434,   10,  -95,    0,    0,  -14,    0,  304,  435,   82,
    9,    0,    0,  316,  322,
};
final static int YYTABLESIZE=644;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         88,
   88,   88,   88,   88,   38,   88,   91,   91,   91,   91,
   91,   74,   91,   74,   74,   74,    1,   13,   88,   72,
   88,   72,   72,   72,  186,   91,  186,   91,   73,   34,
   74,   73,   74,   73,   73,   73,  118,   71,   72,   75,
   72,   75,   75,   75,   76,  117,   76,   76,   76,   30,
   73,   77,   73,   77,   77,   77,   45,  120,   75,  101,
   75,  102,   35,   76,   78,   76,   78,   78,   78,  160,
   77,   79,   77,   79,   79,   79,   80,  157,   80,   80,
   80,  127,  154,   78,   39,   78,  101,  101,  190,  102,
   79,   40,   79,  105,   13,   80,  198,   80,  129,  186,
  158,   31,   32,  189,   84,  189,   85,   34,  106,   55,
   45,   98,  103,   66,   67,  165,   13,  104,   45,   69,
  130,  131,   33,  192,  193,   84,   61,   85,  107,   76,
  101,  101,  111,   45,   68,   79,   45,  138,  137,  114,
   45,  145,  139,  123,   45,  101,  146,  102,  142,  141,
   69,   45,  148,  143,   13,   45,   90,  149,  134,   62,
   63,  178,   72,   98,   99,  100,  196,    7,    8,  196,
  118,   10,   11,   12,   64,  118,  118,  116,  185,  118,
  118,  118,    7,    8,  147,  150,   10,   11,   12,  116,
   13,   96,  107,  191,    7,    8,   65,  194,   10,   11,
   12,  116,   13,   95,  115,  125,    7,    8,   13,  151,
   10,   11,   12,   89,    5,  159,  166,    6,  163,  156,
  167,  101,  161,  102,   13,  179,  174,  175,  180,  176,
  177,   88,   88,   88,   88,  182,   13,   37,   91,   91,
   91,   91,  197,   74,   74,   74,   74,  181,   13,  183,
  199,   72,   72,   72,   72,   97,  195,  200,  153,   13,
  153,  201,  202,   73,   73,   73,   73,    2,   41,   16,
   70,   75,   75,   75,   75,   13,   76,   76,   76,   76,
   42,   43,   44,   77,   77,   77,   77,   13,   51,   13,
   89,    5,   51,   43,    6,   44,   78,   78,   78,   78,
  108,  101,  101,   79,   79,   79,   79,   54,   80,   80,
   80,   80,   89,    5,  184,   45,    6,   80,   81,   82,
   83,   57,   41,   46,  107,  107,   98,   98,  107,   13,
   41,   49,   41,  153,   42,   43,   44,   71,   80,   81,
   82,   83,   42,   43,   44,   41,   42,   68,   41,   90,
   89,    5,   41,  132,    6,   69,   41,   42,   43,   44,
   42,   43,   44,   41,   42,   43,   44,   41,   42,   43,
   44,  153,   67,   54,   72,   42,   43,   44,  135,   42,
   43,   44,  107,   10,   11,   12,    4,  152,  118,  124,
    6,    7,    8,  163,  119,   10,   11,   12,   89,    5,
    0,    0,    6,    3,    4,    5,    0,  153,    6,    7,
    8,    9,    0,   10,   11,   12,    0,    0,    0,  153,
    4,    5,    0,    0,    6,    7,    8,    9,    0,   10,
   11,   12,    4,    5,   27,    0,    6,    7,    8,    0,
   15,   10,   11,   12,   89,    5,    0,   27,    6,    0,
   16,    0,    0,   52,   27,    4,    5,    0,    0,    6,
    7,    8,    0,   53,   10,   11,   12,    0,   74,   27,
   59,    4,    5,    0,    0,    6,    7,    8,    0,    0,
   10,   11,   12,   89,    5,   89,    5,    6,    0,    6,
   27,    0,   70,   27,    0,    0,   77,    0,   78,  109,
   86,    0,    0,    0,    0,    0,   59,    0,    0,  110,
    0,    0,   74,    0,  121,    0,    0,    0,    0,    0,
    0,    0,    0,   27,    0,   89,    5,   27,    0,    6,
    0,  112,    0,  113,    0,  140,  144,    0,    0,  128,
   27,    0,    0,  133,    0,    0,  109,    0,   27,    0,
    0,    0,    0,  126,    0,    0,  110,  121,    0,    0,
    0,    0,   27,   27,    0,    0,    0,    0,    0,    0,
    0,  168,  169,  170,    0,  171,  172,  173,  133,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  162,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   27,    0,    0,    0,    0,    0,    0,   27,    0,
    0,    0,   27,    0,   27,    0,    0,   27,    0,    0,
    0,    0,    0,    0,  128,    0,    0,    0,  128,    0,
  133,    0,    0,  133,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   42,   43,   44,   45,   44,   47,   41,   42,   43,   44,
   45,   41,   47,   43,   44,   45,  123,   61,   60,   41,
   62,   43,   44,   45,   44,   60,   44,   62,  123,   46,
   60,   41,   62,   43,   44,   45,  123,   28,   60,   41,
   62,   43,   44,   45,   41,  125,   43,   44,   45,   44,
   60,   41,   62,   43,   44,   45,   45,  125,   60,   43,
   62,   45,   40,   60,   41,   62,   43,   44,   45,  125,
   60,   41,   62,   43,   44,   45,   41,   44,   43,   44,
   45,  125,  107,   60,  257,   62,   44,   43,  184,   45,
   60,  257,   62,   44,   61,   60,  192,   62,   90,   44,
  115,   40,   41,  123,   60,  123,   62,   46,   59,  257,
   45,   44,   42,   40,   41,  130,   61,   47,   45,   46,
  259,  260,   61,  259,  260,   60,   44,   62,   61,   41,
  259,  260,   41,   45,   61,  257,   45,   42,   43,  257,
   45,   42,   47,   41,   45,   43,   47,   45,   42,   43,
   46,   45,   42,   47,   61,   45,  123,   47,   41,   44,
   44,  152,  257,  269,  270,  271,  191,  262,  263,  194,
  257,  266,  267,  268,   44,  262,  263,  257,  123,  266,
  267,  268,  262,  263,  103,  104,  266,  267,  268,  257,
   61,   40,  125,  185,  262,  263,   44,  189,  266,  267,
  268,  257,   61,   44,  265,   41,  262,  263,   61,  257,
  266,  267,  268,  257,  258,   44,  257,  261,  125,   41,
   41,   43,  125,   45,   61,   44,  145,  146,  125,  148,
  149,  273,  274,  275,  276,   44,   61,  277,  273,  274,
  275,  276,  260,  273,  274,  275,  276,  125,   61,  260,
   44,  273,  274,  275,  276,  272,  260,  125,  278,   61,
  278,  260,   44,  273,  274,  275,  276,    0,  257,  257,
   41,  273,  274,  275,  276,   61,  273,  274,  275,  276,
  269,  270,  271,  273,  274,  275,  276,   61,  125,   61,
  257,  258,   44,   44,  261,   44,  273,  274,  275,  276,
  125,  259,  260,  273,  274,  275,  276,   44,  273,  274,
  275,  276,  257,  258,  259,   44,  261,  273,  274,  275,
  276,  123,  257,   44,  257,  258,  259,  260,  261,   61,
  257,   44,   44,  278,  269,  270,  271,   41,  273,  274,
  275,  276,  269,  270,  271,  257,   44,   41,  257,  123,
  257,  258,  257,  125,  261,   41,  257,  269,  270,  271,
  269,  270,  271,  257,  269,  270,  271,  257,  269,  270,
  271,  278,   41,   14,  257,  269,  270,  271,   96,  269,
  270,  271,   57,  266,  267,  268,  257,  258,   73,   86,
  261,  262,  263,  125,   73,  266,  267,  268,  257,  258,
   -1,   -1,  261,  256,  257,  258,   -1,  278,  261,  262,
  263,  264,   -1,  266,  267,  268,   -1,   -1,   -1,  278,
  257,  258,   -1,   -1,  261,  262,  263,  264,   -1,  266,
  267,  268,  257,  258,    1,   -1,  261,  262,  263,   -1,
    1,  266,  267,  268,  257,  258,   -1,   14,  261,   -1,
    1,   -1,   -1,   14,   21,  257,  258,   -1,   -1,  261,
  262,  263,   -1,   14,  266,  267,  268,   -1,   29,   36,
   21,  257,  258,   -1,   -1,  261,  262,  263,   -1,   -1,
  266,  267,  268,  257,  258,  257,  258,  261,   -1,  261,
   57,   -1,   27,   60,   -1,   -1,   31,   -1,   33,   60,
   35,   -1,   -1,   -1,   -1,   -1,   57,   -1,   -1,   60,
   -1,   -1,   73,   -1,   75,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   90,   -1,  257,  258,   94,   -1,  261,
   -1,   66,   -1,   68,   -1,  101,  102,   -1,   -1,   90,
  107,   -1,   -1,   94,   -1,   -1,  107,   -1,  115,   -1,
   -1,   -1,   -1,   88,   -1,   -1,  107,  118,   -1,   -1,
   -1,   -1,  129,  130,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  137,  138,  139,   -1,  141,  142,  143,  129,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  124,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  178,   -1,   -1,   -1,   -1,   -1,   -1,  185,   -1,
   -1,   -1,  189,   -1,  191,   -1,   -1,  194,   -1,   -1,
   -1,   -1,   -1,   -1,  185,   -1,   -1,   -1,  189,   -1,
  191,   -1,   -1,  194,
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
"prog : '{' sentencias",
"sentencias : sentencias sen_declarativa",
"sentencias : sentencias sen_ejecutable",
"sentencias : sentencias sen_control",
"sentencias : sen_ejecutable",
"sentencias : sen_control",
"sentencias : sen_declarativa",
"sentencias : error ','",
"sen_declarativa : tipo list_var ','",
"sen_declarativa : funcion",
"sen_declarativa : clase",
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

//#line 290 ".\gramatica.y"
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

    // las declaraciones dentro de una clase deben tener mas consideraciones
    if (claseActual != null) declararEnClase(ptr);
    else {
        Integer ptr_aux = st.getPtr(st.getLexema(ptr) + ":" + ambitoActual.toString());
        if (ptr_aux != 0) {
            agregarError(errores_semanticos, Parser.ERROR, String.format(ERROR_REDECLARACION, st.getLexema(ptr), ambitoActual.toString()));
            return;
        }
        st.setAttribute(ptr, "tipo", tipo.toString());
        st.setAttribute(ptr, "valid", "1");
        st.setLexema(ptr, st.getLexema(ptr) + ":" + ambitoActual.toString());
    }
}

public void declararEnClase(Integer ptr) {}

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
    st.setAttribute(id, "valid", "1");
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
    // ptr_lhs puede ser un id o un atributo_clase, eg: c1:global:main, c1, var1
    // ptr_rhs es el lexema de un id detectado por lex, eg:var1, obj2
    int max_nivel = 3;
    Integer ptr_rhs_aux = 0;
    do {
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
        ptr_rhs_aux = st.getPtr(st.getLexema(ptr_rhs), ambito.copy());
        // si no lo encuentra, mira si hereda y busca en la clase padre
    } while (ptr_rhs_aux == 0 && --max_nivel > 0 && st.getAttribute(ptr_lhs, "tipo") != null);

    if (ptr_rhs_aux == 0) {
        agregarError(errores_semanticos, Parser.ERROR,
                String.format(ERROR_ALCANCE, st.getLexema(ptr_rhs), ambito.toString()));
        return 0;
    }
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

    if (lhs.dval == rhs.dval) {
        agregarError(errores_semanticos, Parser.ERROR,
            String.format(ERROR_TIPOS_INCOMPATIBLES, getTipo((int)lhs.dval), getTipo((int)rhs.dval)));
        return 0;
    }
    return crearTerceto("*", lhs.ival, rhs.ival, lhs.sval, rhs.sval);
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
    agregarEstructura("Asignacion al identificador ", ptr);
    return crearTerceto("=", ptr, rhs.ival, "st", rhs.sval);
}

public ParserVal crearTercetoIncrement(Integer id) {
    ParserVal val = new ParserVal();
    if (id == 0) {
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

LexicalAnalyzer lexicalAnalyzer;
SymbolTable st;
PilaTercetos pilaTercetos;
Integer inicio_while, tipo, claseActual;
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
//#line 1029 "Parser.java"
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
//#line 22 ".\gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al final del programa");}
break;
case 9:
//#line 31 ".\gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una sentencia ejecutable");}
break;
case 13:
//#line 39 ".\gramatica.y"
{ yyval.ival = SHORT; tipo = (int)SHORT; }
break;
case 14:
//#line 40 ".\gramatica.y"
{ yyval.ival = UINT; tipo = (int)UINT; }
break;
case 15:
//#line 41 ".\gramatica.y"
{ yyval.ival = FLOAT; tipo = (int)FLOAT; }
break;
case 16:
//#line 42 ".\gramatica.y"
{ yyval.ival = getTipoClase(val_peek(0).ival, ambitoActual.copy()); tipo = yyval.ival; }
break;
case 17:
//#line 45 ".\gramatica.y"
{verificarRango(val_peek(0).ival); yyval.ival = val_peek(0).ival; yyval.dval = SHORT;}
break;
case 18:
//#line 46 ".\gramatica.y"
{yyval.ival = val_peek(0).ival; yyval.dval = UINT;}
break;
case 19:
//#line 47 ".\gramatica.y"
{verificarRango(val_peek(0).ival); yyval.ival = val_peek(0).ival; yyval.dval = FLOAT;}
break;
case 20:
//#line 48 ".\gramatica.y"
{resolverSigno(val_peek(0).ival); verificarRango(val_peek(0).ival); yyval.ival = val_peek(0).ival;}
break;
case 21:
//#line 49 ".\gramatica.y"
{resolverSigno(val_peek(0).ival); verificarRango(val_peek(0).ival); yyval.ival = val_peek(0).ival;}
break;
case 22:
//#line 50 ".\gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "No se puede negar un unsigned int");}
break;
case 23:
//#line 53 ".\gramatica.y"
{    if (tipo == 0) {
                                                agregarError(errores_semanticos, Parser.ERROR,
                                                        String.format(ERROR_TIPO, st.getLexema(val_peek(0).ival)));
                                                break;
                                            }
                                            if (!verificarDeclaracion(val_peek(0).ival)) {
                                                st.setAttribute(val_peek(0).ival, "tipo", tipo.toString());
                                                st.setAttribute(val_peek(0).ival, "valid", "1");
                                                st.setLexema(val_peek(0).ival, st.getLexema(val_peek(0).ival) + ":" + ambitoActual.toString());
                                            } else {
                                                agregarError(errores_semanticos, Parser.ERROR,
                                                    String.format(ERROR_REDECLARACION, st.getLexema(val_peek(2).ival), ambitoActual.toString()));
                                            }
                                        }
break;
case 24:
//#line 67 ".\gramatica.y"
{   if (tipo == 0) {
                                                agregarError(errores_semanticos, Parser.ERROR,
                                                        String.format(ERROR_TIPO, st.getLexema(val_peek(0).ival)));
                                                break;
                                            }
                                            if (!verificarDeclaracion(val_peek(0).ival)) {
                                                st.setAttribute(val_peek(0).ival, "tipo", tipo.toString());
                                                st.setAttribute(val_peek(0).ival, "valid", "1");
                                                st.setLexema(val_peek(0).ival, st.getLexema(val_peek(0).ival) + ":" + ambitoActual.toString());
                                            } else {
                                                agregarError(errores_semanticos, Parser.ERROR,
                                                    String.format(ERROR_REDECLARACION, st.getLexema(val_peek(0).ival), ambitoActual.toString()));
                                            }
                                        }
break;
case 25:
//#line 83 ".\gramatica.y"
{ ambitoActual.pop(); }
break;
case 26:
//#line 84 ".\gramatica.y"
{ ambitoActual.pop(); }
break;
case 27:
//#line 85 ".\gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al comienzo de la función");}
break;
case 28:
//#line 88 ".\gramatica.y"
{ agregarFuncion(val_peek(3).ival, VOID, val_peek(1).ival);
                                                    if (val_peek(1).ival != 0)
                                                        st.setLexema(val_peek(1).ival, st.getLexema(val_peek(1).ival) + ":" + ambitoActual.toString());
                                                 }
break;
case 29:
//#line 92 ".\gramatica.y"
{ agregarFuncion(val_peek(2).ival, VOID, null); }
break;
case 30:
//#line 95 ".\gramatica.y"
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
case 41:
//#line 123 ".\gramatica.y"
{ yyval.ival = crearTercetoAsignacion(val_peek(2).ival, val_peek(0)); yyval.sval = "terceto"; if (yyval.ival != 0) yyval.dval = val_peek(2).dval; }
break;
case 42:
//#line 124 ".\gramatica.y"
{ yyval.ival = crearTercetoAsignacion(val_peek(2).ival, val_peek(0)); yyval.sval = "terceto"; if (yyval.ival != 0) yyval.dval = val_peek(2).dval; }
break;
case 43:
//#line 125 ".\gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una expresión aritmética");}
break;
case 44:
//#line 126 ".\gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un identificador");}
break;
case 45:
//#line 128 ".\gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una expresión aritmética");}
break;
case 46:
//#line 129 ".\gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un signo igual");}
break;
case 47:
//#line 132 ".\gramatica.y"
{ invocacionFuncion(val_peek(3).ival, val_peek(1)); }
break;
case 48:
//#line 133 ".\gramatica.y"
{ invocacionFuncion(val_peek(2).ival); }
break;
case 49:
//#line 134 ".\gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
break;
case 50:
//#line 136 ".\gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
break;
case 51:
//#line 137 ".\gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
break;
case 52:
//#line 140 ".\gramatica.y"
{ if (val_peek(3).ival != 0) invocacionFuncion(val_peek(3).ival, val_peek(1)); }
break;
case 53:
//#line 141 ".\gramatica.y"
{ if (val_peek(2).ival != 0) invocacionFuncion(val_peek(2).ival); }
break;
case 54:
//#line 142 ".\gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
break;
case 55:
//#line 143 ".\gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
break;
case 56:
//#line 146 ".\gramatica.y"
{ yyval.ival = agregarAtributo(val_peek(2).ival, val_peek(0).ival, ambitoActual.copy()); }
break;
case 57:
//#line 147 ".\gramatica.y"
{ yyval.ival = agregarAtributo(val_peek(2).ival, val_peek(0).ival, ambitoActual.copy()); }
break;
case 58:
//#line 150 ".\gramatica.y"
{agregarEstructura("IF");}
break;
case 59:
//#line 151 ".\gramatica.y"
{agregarEstructura("IF");}
break;
case 60:
//#line 152 ".\gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un bloque de sentencias ejecutables");}
break;
case 61:
//#line 153 ".\gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un bloque de sentencias ejecutables");}
break;
case 62:
//#line 156 ".\gramatica.y"
{ agregarEstructura("IF"); 
                                                         completarB("BI", pilaTercetos.getContador()+1); }
break;
case 63:
//#line 158 ".\gramatica.y"
{ agregarEstructura("IF"); 
                                                                          completarB("BI", pilaTercetos.getContador()+1); }
break;
case 64:
//#line 162 ".\gramatica.y"
{ crearTerceto("BI", -1, -1, "", ""); completarB("BF", pilaTercetos.getContador()+1);  }
break;
case 66:
//#line 168 ".\gramatica.y"
{ yyval.ival = crearTerceto("BF", val_peek(1).ival, -1, val_peek(1).sval, ""); }
break;
case 67:
//#line 171 ".\gramatica.y"
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
case 68:
//#line 181 ".\gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una expresión aritmética");}
break;
case 69:
//#line 182 ".\gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una expresión aritmética");}
break;
case 70:
//#line 183 ".\gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un comparador");}
break;
case 71:
//#line 184 ".\gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba dos expresión aritmética");}
break;
case 72:
//#line 189 ".\gramatica.y"
{ yyval.ival = crearTercetoExp(val_peek(2), val_peek(0), "+"); yyval.sval = "terceto"; if (yyval.ival != 0) yyval.dval = val_peek(2).dval; }
break;
case 73:
//#line 190 ".\gramatica.y"
{ yyval.ival = crearTercetoExp(val_peek(2), val_peek(0), "-"); yyval.sval = "terceto"; if (yyval.ival != 0) yyval.dval = val_peek(2).dval; }
break;
case 74:
//#line 191 ".\gramatica.y"
{ yyval = val_peek(0); }
break;
case 75:
//#line 192 ".\gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
break;
case 76:
//#line 193 ".\gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
break;
case 77:
//#line 194 ".\gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
break;
case 78:
//#line 195 ".\gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
break;
case 79:
//#line 196 ".\gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
break;
case 80:
//#line 197 ".\gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
break;
case 81:
//#line 200 ".\gramatica.y"
{ yyval.ival = crearTercetoTermino(val_peek(2), val_peek(0), "*"); yyval.sval = "terceto"; if (yyval.ival != 0) yyval.dval = val_peek(2).dval; }
break;
case 82:
//#line 201 ".\gramatica.y"
{ yyval.ival = crearTercetoTermino(val_peek(2), val_peek(0), "/"); yyval.sval = "terceto"; if (yyval.ival != 0) yyval.dval = val_peek(2).dval; }
break;
case 83:
//#line 202 ".\gramatica.y"
{ yyval = val_peek(0); }
break;
case 84:
//#line 203 ".\gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
break;
case 85:
//#line 204 ".\gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
break;
case 86:
//#line 205 ".\gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
break;
case 87:
//#line 206 ".\gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
break;
case 88:
//#line 210 ".\gramatica.y"
{   yyval.ival = st.getPtr(st.getLexema(val_peek(0).ival), ambitoActual.copy(), "identificador");
                                                    if (yyval.ival == 0) {
                                                        agregarError(errores_semanticos, Parser.ERROR,
                                                            String.format(ERROR_ALCANCE, st.getLexema(val_peek(0).ival), ambitoActual.toString()));
                                                        break;
                                                    }
                                                    yyval.sval = "st";
                                                    yyval.dval = Integer.parseInt(st.getAttribute(yyval.ival, "tipo")); /*chequear*/
                                                }
break;
case 89:
//#line 219 ".\gramatica.y"
{ yyval = crearTercetoIncrement(val_peek(1).ival); }
break;
case 90:
//#line 220 ".\gramatica.y"
{ yyval.ival = val_peek(0).ival; yyval.sval = "st"; yyval.dval = val_peek(0).dval; }
break;
case 91:
//#line 221 ".\gramatica.y"
{   yyval.ival = st.getPtr(st.getLexema(val_peek(0).ival), ambitoActual.copy());
                                                    if (yyval.ival != 0) {
                                                        yyval.sval = "st";
                                                        yyval.dval = Integer.parseInt(st.getAttribute(yyval.ival, "tipo"));
                                                    }
                                                }
break;
case 92:
//#line 229 ".\gramatica.y"
{ yyval.ival = NOT_EQUAL; }
break;
case 93:
//#line 230 ".\gramatica.y"
{ yyval.ival = EQUAL; }
break;
case 94:
//#line 231 ".\gramatica.y"
{ yyval.ival = GREATER_EQUAL; }
break;
case 95:
//#line 232 ".\gramatica.y"
{ yyval.ival = LESS_EQUAL; }
break;
case 96:
//#line 233 ".\gramatica.y"
{ yyval.ival = 60; }
break;
case 97:
//#line 234 ".\gramatica.y"
{ yyval.ival = 62; }
break;
case 100:
//#line 240 ".\gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al comienzo del bloque de sentencias ejecutables");}
break;
case 101:
//#line 241 ".\gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al final del bloque de sentencias ejecutables");}
break;
case 102:
//#line 242 ".\gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera un bloque de sentencias ejecutables");}
break;
case 108:
//#line 255 ".\gramatica.y"
{agregarEstructura("PRINT");}
break;
case 109:
//#line 256 ".\gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una cadena para imprimir");}
break;
case 110:
//#line 259 ".\gramatica.y"
{ agregarEstructura("WHILE");
                                                                             crearTerceto("BI", -1, -1, "", "");
                                                                             completarB("BF", pilaTercetos.getContador()+1);
                                                                             completarWhile(); }
break;
case 111:
//#line 263 ".\gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un bloque de sentencias ejecutables");}
break;
case 112:
//#line 266 ".\gramatica.y"
{ inicio_while = pilaTercetos.getContador()+1; }
break;
case 113:
//#line 269 ".\gramatica.y"
{ ambitoActual.pop(); claseActual = null; }
break;
case 114:
//#line 270 ".\gramatica.y"
{ ambitoActual.pop(); claseActual = null; }
break;
case 115:
//#line 271 ".\gramatica.y"
{ ambitoActual.pop(); claseActual = null; }
break;
case 116:
//#line 272 ".\gramatica.y"
{ agregarClase(val_peek(1).ival, "FDCLASS"); ambitoActual.pop(); claseActual = null; }
break;
case 117:
//#line 273 ".\gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al final de la clase");
                                                             ambitoActual.pop(); }
break;
case 118:
//#line 277 ".\gramatica.y"
{ agregarClase(val_peek(0).ival, "CLASS"); }
break;
case 122:
//#line 286 ".\gramatica.y"
{ heredar(val_peek(1).ival); }
break;
//#line 1607 "Parser.java"
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
