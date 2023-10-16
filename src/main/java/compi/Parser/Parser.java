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
    2,    2,    5,    5,    5,    9,    9,    9,    9,    9,
    9,    6,    6,    7,    7,    7,    7,   10,   10,   10,
   11,   11,   11,   11,   11,   11,   12,    3,    3,    3,
    3,    3,   14,   14,   14,   14,   14,   14,   14,   15,
   15,   15,   15,   15,   15,   18,   18,   18,   18,   20,
   16,   16,   16,   16,   16,   13,   13,   13,   13,   21,
   21,   21,   21,   21,   19,   19,   19,   19,   19,   19,
   19,   19,   19,   25,   25,   25,   25,   25,   25,   25,
   26,   26,   26,   26,   24,   24,   24,   24,   24,   24,
   22,   22,   22,   22,   22,   23,   23,   23,   27,   27,
   17,   17,    4,    4,    4,    4,    4,    8,    8,    8,
    8,   28,   28,
};
final static short yylen[] = {                            2,
    3,    2,    2,    2,    2,    1,    1,    1,    2,    3,
    1,    1,    1,    1,    1,    1,    1,    1,    2,    2,
    2,    3,    1,    8,    7,    7,    6,    2,    1,    1,
    4,    4,    2,    3,    3,    1,    1,    2,    2,    2,
    2,    2,    3,    3,    2,    2,    2,    2,    2,    4,
    3,    3,    3,    2,    2,    4,    3,    2,    2,    3,
    8,    6,    7,    7,    5,    9,    7,    7,    7,    3,
    2,    2,    1,    1,    3,    3,    1,    4,    4,    4,
    4,    4,    4,    3,    3,    1,    4,    4,    4,    4,
    1,    2,    1,    1,    1,    1,    1,    1,    1,    1,
    1,    3,    2,    2,    2,    2,    5,    1,    2,    1,
    2,    2,    7,    6,    6,    5,    6,    4,    5,    3,
    4,    2,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   13,
   14,   15,    0,    0,    8,    6,    7,    0,   11,   12,
    0,    0,    0,    0,    0,    0,    9,    0,   16,   17,
   18,    0,    0,   54,    0,    0,   93,    0,   94,    0,
   86,    0,  111,  112,    0,    0,   97,   98,   96,   95,
    0,   99,  100,    0,    0,    0,    0,    1,    3,    4,
    5,   23,    0,   38,   39,   40,   41,   42,    0,   59,
    0,    0,   92,   19,   21,   20,   51,    0,    0,   60,
    0,    0,   53,    0,    0,    0,    0,  120,  123,    0,
    0,    0,    0,    0,    0,    0,   10,    0,   57,    0,
    0,   50,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   84,    0,    0,   85,    0,  118,    0,  121,
  122,   30,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   22,   56,    0,    0,    0,    0,    0,
    0,   87,   88,   89,   90,    0,   65,    0,  119,    0,
    0,    0,    0,    0,   36,   28,    0,    0,    0,  105,
  110,    0,  116,  103,  109,    0,    0,    0,   62,    0,
    0,   37,    0,    0,   27,    0,    0,   33,    0,    0,
  115,  117,    0,  102,  114,   63,   64,    0,    0,   25,
   34,   35,    0,    0,    0,   26,  113,   61,    0,   31,
   32,   24,    0,    0,  108,    0,    0,    0,    0,  106,
    0,    0,   68,    0,   69,    0,    0,   67,    0,    0,
    0,  107,   66,
};
final static short yydgoto[] = {                          2,
   14,  152,  130,   17,   18,   63,   19,   20,   37,  125,
  154,  206,  155,   21,   22,   23,   24,   25,   54,   39,
   55,  148,  207,   56,   40,   41,  132,   90,
};
final static short yysindex[] = {                      -121,
  193,    0,  -26,   93,   61,  -38, -250, -151,  -40,    0,
    0,    0,  -44,  282,    0,    0,    0, -144,    0,    0,
   73,   79,   82,  102,  107,   96,    0,  -16,    0,    0,
    0,  -92,  111,    0,  -44,  -95,    0,   99,    0,   77,
    0,   85,    0,    0,  136,  113,    0,    0,    0,    0,
   85,    0,    0,   65,  -37,  -44,  -24,    0,    0,    0,
    0,    0,   63,    0,    0,    0,    0,    0,  114,    0,
  -44,  -24,    0,    0,    0,    0,    0,  166,  -24,    0,
  -19,   38,    0,  118,  122,  125,  -79,    0,    0,  -72,
  129,  -10,  -44,  309,  -94,  -24,    0,  -71,    0,  427,
  -24,    0,  -44,  -44,  -44,   77,  -44,  -44,  -44,   77,
  -44,  -44,    0,  -44,  -44,    0,  153,    0,  251,    0,
    0,    0,  311,  -65,  144,  309,  -67,  -24,  426,    0,
  149,  435,  309,    0,    0,   77,   77,   77,   77,   77,
   77,    0,    0,    0,    0,  309,    0, -216,    0,  168,
  401,  -68,  -68,  296,    0,    0,  344,  171,   27,    0,
    0,  440,    0,    0,    0,  172,  -42,  414,    0,   85,
  356,    0,  177,  178,    0,  -68,  -68,    0,  401,  369,
    0,    0,  179,    0,    0,    0,    0,  -11,  183,    0,
    0,    0,  200,  209,  381,    0,    0,    0,   74,    0,
    0,    0,   59,  426,    0,  214, -204,    1,  158,    0,
   58,  216,    0,  220,    0,  333,   21,    0,  161,  -58,
  252,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  301,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  -33,    0,    0,
    0,    0,  266,    0,  271,    0,    0,  277,    0,  -28,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   -2,    0,    9,  305,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  307,    0,
  325,  329,    0,    0,    0,    0,    0,  330,  334,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   10,    0,    0,   22,    0,    0,    0,    0,
  364,    0,    0,    0,    0,   -8,    0,    0,    0,   -3,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  368,    0,    0,    0,   41,    0,   87,
    0,    0,    0,    0,    0,    4,   17,   24,   29,   49,
   54,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   52,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0, -184,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,  583,  376,  404,  340,    0,    0,    0,    0,    0,
  -36,  -48,  -22,    0,    0,    0,    0,    0,  677,  312,
   36,  562,  -35,  366,  319,   90, -104,  338,
};
final static int YYTABLESIZE=778;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         51,
   32,    1,   13,   95,   32,   44,   45,   91,   91,   91,
   91,   91,   77,   91,   77,   77,   77,   27,   81,   52,
   82,   53,  104,  103,  162,   32,   91,  105,   91,   36,
  127,   77,   75,   77,   75,   75,   75,   76,   73,   76,
   76,   76,  168,  169,   78,  118,   78,   78,   78,   74,
   71,   75,  120,   75,  211,  212,   76,   79,   76,   79,
   79,   79,   72,   78,   80,   78,   80,   80,   80,   81,
  182,   81,   81,   81,  104,  104,   79,   86,   79,  108,
  107,   70,   32,   80,  109,   80,   92,   13,   81,   82,
   81,   82,   82,   82,   83,  104,   83,   83,   83,  209,
   42,  205,  205,  173,  174,   46,   97,   81,   82,   82,
   82,  220,   62,   83,  171,   83,   64,  205,   84,   13,
  180,   98,   65,   85,   52,   66,   53,  193,  194,   32,
  101,  178,   33,   34,   13,   69,   70,   32,   36,   83,
   32,   81,  195,   82,   52,   67,   53,  110,  178,  129,
   68,   77,   91,   35,   99,   32,   71,  178,   32,  111,
  214,   80,   32,  114,  112,  117,   32,  208,  115,  123,
  133,  214,  178,  113,  116,  217,   74,   75,   76,   88,
  216,  204,    7,    8,  157,  134,   10,   11,   12,    7,
    8,  156,  163,   10,   11,   12,  204,  159,    4,    5,
  142,  143,    6,  144,  145,  189,  102,  170,   81,  172,
   82,  110,   28,   13,  181,  185,   28,  186,   13,  172,
  191,  192,  197,  199,   29,   30,   31,   94,   29,   30,
   31,   91,   47,   48,   49,   50,   77,   28,   43,   91,
   91,   91,   91,  200,   77,   77,   77,   77,  198,   29,
   30,   31,  201,   13,  126,   73,   75,  210,   87,  218,
  213,   76,   73,  219,   75,   75,   75,   75,   78,   76,
   76,   76,   76,   74,   71,  129,   78,   78,   78,   78,
  221,   79,  184,    4,    5,  222,   72,    6,   80,   79,
   79,   79,   79,   81,   28,  223,   80,   80,   80,   80,
    2,   81,   81,   81,   81,   70,   29,   30,   31,   55,
  104,  104,   26,   82,   45,    4,    5,  215,   83,    6,
   47,   82,   82,   82,   82,   26,   83,   83,   83,   83,
    4,    5,  203,  147,    6,  172,  172,   47,   48,   49,
   50,   28,   13,  110,  110,  101,  101,  110,   46,   28,
   58,  172,   28,   29,   30,   31,   13,   47,   48,   49,
   50,   29,   30,   31,   29,   30,   31,   28,   48,   13,
   28,   13,   49,   52,   28,  149,   16,   43,   28,   29,
   30,   31,   29,   30,   31,  122,   29,   30,   31,   60,
   29,   30,   31,   13,   10,   11,   12,    7,    8,  106,
  110,   10,   11,   12,   13,   26,   58,   44,   29,    4,
    5,  146,  147,    6,    4,    5,   13,   61,    6,   93,
  175,  136,  137,  138,  119,  139,  140,  141,   26,   13,
  124,  129,    0,  151,   26,  172,    0,   26,    0,    0,
   26,   13,    0,   26,   26,    0,    0,    0,    3,    4,
    5,    0,    0,    6,    7,    8,    9,   26,   10,   11,
   12,   13,   26,    0,    0,   26,  179,  135,   26,   81,
   26,   82,    0,   26,   13,    0,    0,    0,    0,   26,
  190,    0,   26,    0,    0,    0,   13,    0,    0,    0,
   26,   26,    0,  196,    0,   13,    0,    0,  153,    0,
   13,    0,    0,    0,  161,  202,   26,  165,    0,    0,
   26,    0,    7,    8,   26,   26,   10,   11,   12,    0,
   26,    0,    0,    0,    0,    0,  153,   26,    0,  177,
    0,   26,  153,    0,    0,    0,  129,  165,    4,    5,
    0,    0,    6,    7,    8,    9,  177,   10,   11,   12,
  160,    0,    4,  150,  153,  177,    6,    7,    8,  164,
    0,   10,   11,   12,  184,    4,    5,    4,  150,    6,
  177,    6,    7,    8,    0,    0,   10,   11,   12,  161,
    0,    0,    0,   15,  165,    0,    0,    0,    0,    4,
    5,  161,    0,    6,    0,  165,   59,    0,    0,    0,
    4,  150,    0,    0,    6,    7,    8,    0,    0,   10,
   11,   12,    4,  150,    0,    0,    6,    7,    8,    0,
    0,   10,   11,   12,    0,    4,  150,   89,    0,    6,
    7,    8,    0,    0,   10,   11,   12,    4,  150,    0,
    0,    6,    7,    8,    0,    0,   10,   11,   12,    0,
    0,    0,    0,    0,    0,  131,    0,    4,  150,    0,
    0,    6,    7,    8,    0,    0,   10,   11,   12,   89,
    4,    5,  121,  187,    6,    0,    0,    0,    0,    0,
   38,    0,    4,    5,    0,    0,    6,  158,    0,   57,
    0,    4,    5,    0,  166,    6,    4,    5,    0,    0,
    6,  121,   72,    0,    0,    0,    0,  167,    0,   78,
    0,   79,    0,    0,    0,    0,    0,    0,    0,    0,
  183,    0,    0,    0,    0,    0,    0,    0,    0,  188,
    0,    0,   96,    0,    0,    0,  176,    0,    0,    0,
    0,    0,    0,    0,    0,  100,    0,  101,    0,    0,
    0,    0,    0,  176,    0,    0,    0,    0,    0,    0,
    0,    0,  176,    0,  167,    0,    0,    0,    0,  128,
    0,    0,    0,    0,    0,    0,    0,  176,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
   45,  123,   61,   41,   45,   44,  257,   41,   42,   43,
   44,   45,   41,   47,   43,   44,   45,   44,   43,   60,
   45,   62,   42,   43,  129,   45,   60,   47,   62,   46,
   41,   60,   41,   62,   43,   44,   45,   41,   41,   43,
   44,   45,  259,  260,   41,  125,   43,   44,   45,   41,
   41,   60,  125,   62,  259,  260,   60,   41,   62,   43,
   44,   45,   41,   60,   41,   62,   43,   44,   45,   41,
   44,   43,   44,   45,  259,  260,   60,   42,   62,   42,
   43,   41,   45,   60,   47,   62,   51,   61,   60,   41,
   62,   43,   44,   45,   41,   44,   43,   44,   45,  204,
   40,   44,   44,  152,  153,  257,   44,   43,   60,   45,
   62,  216,  257,   60,  151,   62,   44,   44,   42,   61,
  157,   59,   44,   47,   60,   44,   62,  176,  177,   45,
   44,  154,   40,   41,   61,   40,   41,   45,   46,   41,
   45,   43,  179,   45,   60,   44,   62,   61,  171,  123,
   44,   41,   40,   61,   41,   45,   61,  180,   45,   42,
  209,  257,   45,   42,   47,   41,   45,  203,   47,   41,
  265,  220,  195,   84,   85,  211,  269,  270,  271,   44,
  123,  123,  262,  263,   41,  257,  266,  267,  268,  262,
  263,  257,   44,  266,  267,  268,  123,  265,  257,  258,
  111,  112,  261,  114,  115,  170,   41,   40,   43,  278,
   45,  125,  257,   61,   44,   44,  257,  260,   61,  278,
   44,   44,   44,   41,  269,  270,  271,  265,  269,  270,
  271,  265,  273,  274,  275,  276,  265,  257,  277,  273,
  274,  275,  276,   44,  273,  274,  275,  276,  260,  269,
  270,  271,   44,   61,  265,  272,  265,   44,  123,   44,
  260,  265,  265,   44,  273,  274,  275,  276,  265,  273,
  274,  275,  276,  265,  265,  123,  273,  274,  275,  276,
  260,  265,  125,  257,  258,  125,  265,  261,  265,  273,
  274,  275,  276,  265,  257,   44,  273,  274,  275,  276,
    0,  273,  274,  275,  276,  265,  269,  270,  271,   44,
  259,  260,    1,  265,   44,  257,  258,  260,  265,  261,
   44,  273,  274,  275,  276,   14,  273,  274,  275,  276,
  257,  258,  259,  260,  261,  278,  278,  273,  274,  275,
  276,  257,   61,  257,  258,  259,  260,  261,   44,  257,
   44,  278,  257,  269,  270,  271,   61,  273,  274,  275,
  276,  269,  270,  271,  269,  270,  271,  257,   44,   61,
  257,   61,   44,   44,  257,  125,    1,   44,  257,  269,
  270,  271,  269,  270,  271,  257,  269,  270,  271,   14,
  269,  270,  271,   61,  266,  267,  268,  262,  263,   81,
   82,  266,  267,  268,   61,   94,  125,   44,   41,  257,
  258,  259,  260,  261,  257,  258,   61,   14,  261,   54,
  125,  103,  104,  105,   87,  107,  108,  109,  117,   61,
   91,  123,   -1,  123,  123,  278,   -1,  126,   -1,   -1,
  129,   61,   -1,  132,  133,   -1,   -1,   -1,  256,  257,
  258,   -1,   -1,  261,  262,  263,  264,  146,  266,  267,
  268,   61,  151,   -1,   -1,  154,  123,   41,  157,   43,
  159,   45,   -1,  162,   61,   -1,   -1,   -1,   -1,  168,
  125,   -1,  171,   -1,   -1,   -1,   61,   -1,   -1,   -1,
  179,  180,   -1,  125,   -1,   61,   -1,   -1,  123,   -1,
   61,   -1,   -1,   -1,  129,  125,  195,  132,   -1,   -1,
  199,   -1,  262,  263,  203,  204,  266,  267,  268,   -1,
  209,   -1,   -1,   -1,   -1,   -1,  151,  216,   -1,  154,
   -1,  220,  157,   -1,   -1,   -1,  123,  162,  257,  258,
   -1,   -1,  261,  262,  263,  264,  171,  266,  267,  268,
  125,   -1,  257,  258,  179,  180,  261,  262,  263,  125,
   -1,  266,  267,  268,  125,  257,  258,  257,  258,  261,
  195,  261,  262,  263,   -1,   -1,  266,  267,  268,  204,
   -1,   -1,   -1,    1,  209,   -1,   -1,   -1,   -1,  257,
  258,  216,   -1,  261,   -1,  220,   14,   -1,   -1,   -1,
  257,  258,   -1,   -1,  261,  262,  263,   -1,   -1,  266,
  267,  268,  257,  258,   -1,   -1,  261,  262,  263,   -1,
   -1,  266,  267,  268,   -1,  257,  258,   45,   -1,  261,
  262,  263,   -1,   -1,  266,  267,  268,  257,  258,   -1,
   -1,  261,  262,  263,   -1,   -1,  266,  267,  268,   -1,
   -1,   -1,   -1,   -1,   -1,   94,   -1,  257,  258,   -1,
   -1,  261,  262,  263,   -1,   -1,  266,  267,  268,   87,
  257,  258,   90,  260,  261,   -1,   -1,   -1,   -1,   -1,
    4,   -1,  257,  258,   -1,   -1,  261,  126,   -1,   13,
   -1,  257,  258,   -1,  133,  261,  257,  258,   -1,   -1,
  261,  119,   26,   -1,   -1,   -1,   -1,  146,   -1,   33,
   -1,   35,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  159,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  168,
   -1,   -1,   56,   -1,   -1,   -1,  154,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   69,   -1,   71,   -1,   -1,
   -1,   -1,   -1,  171,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  180,   -1,  203,   -1,   -1,   -1,   -1,   93,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  195,
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
"CTE : CTE_SHORT",
"CTE : CTE_UINT",
"CTE : CTE_FLOAT",
"CTE : '-' CTE_SHORT",
"CTE : '-' CTE_FLOAT",
"CTE : '-' CTE_UINT",
"list_var : list_var ';' ID",
"list_var : ID",
"funcion : VOID ID '(' parametro ')' '{' cuerpo_funcion '}'",
"funcion : VOID ID '(' ')' '{' cuerpo_funcion '}'",
"funcion : VOID ID '(' parametro ')' cuerpo_funcion '}'",
"funcion : VOID ID '(' ')' cuerpo_funcion '}'",
"parametro : tipo ID",
"parametro : tipo",
"parametro : ID",
"cuerpo_funcion : cuerpo_funcion sen_declarativa sen_retorno ','",
"cuerpo_funcion : cuerpo_funcion sen_ejecutable sen_retorno ','",
"cuerpo_funcion : cuerpo_funcion seleccion_func",
"cuerpo_funcion : sen_declarativa sen_retorno ','",
"cuerpo_funcion : sen_ejecutable sen_retorno ','",
"cuerpo_funcion : seleccion_func",
"sen_retorno : RETURN",
"sen_ejecutable : asignacion ','",
"sen_ejecutable : inv_funcion ','",
"sen_ejecutable : seleccion ','",
"sen_ejecutable : imprimir ','",
"sen_ejecutable : inv_metodo ','",
"asignacion : ID '=' exp_aritmetica",
"asignacion : atributo_clase '=' exp_aritmetica",
"asignacion : ID '='",
"asignacion : '=' exp_aritmetica",
"asignacion : ID exp_aritmetica",
"asignacion : atributo_clase '='",
"asignacion : atributo_clase exp_aritmetica",
"inv_funcion : ID '(' exp_aritmetica ')'",
"inv_funcion : ID '(' ')'",
"inv_funcion : ID '(' exp_aritmetica",
"inv_funcion : ID exp_aritmetica ')'",
"inv_funcion : ID ')'",
"inv_funcion : ID '('",
"inv_metodo : atributo_clase '(' exp_aritmetica ')'",
"inv_metodo : atributo_clase '(' ')'",
"inv_metodo : atributo_clase '('",
"inv_metodo : atributo_clase ')'",
"atributo_clase : ID '.' ID",
"seleccion : IF '(' condicion ')' bloque_sen_ejecutable ELSE bloque_sen_ejecutable END_IF",
"seleccion : IF '(' condicion ')' bloque_sen_ejecutable END_IF",
"seleccion : IF '(' condicion ')' ELSE bloque_sen_ejecutable END_IF",
"seleccion : IF '(' condicion ')' bloque_sen_ejecutable ELSE END_IF",
"seleccion : IF '(' condicion ')' END_IF",
"seleccion_func : IF '(' condicion ')' bloque_ejecutable_func ELSE bloque_ejecutable_func END_IF ','",
"seleccion_func : IF '(' condicion ')' bloque_ejecutable_func END_IF ','",
"seleccion_func : IF '(' condicion ')' ELSE bloque_ejecutable_func END_IF",
"seleccion_func : IF '(' condicion ')' bloque_ejecutable_func ELSE END_IF",
"condicion : exp_aritmetica comparador exp_aritmetica",
"condicion : exp_aritmetica comparador",
"condicion : comparador exp_aritmetica",
"condicion : exp_aritmetica",
"condicion : comparador",
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
"bloque_ejecutable_func : sen_retorno ','",
"bloque_ejecutable_func : '{' sen_ejecutable_r sen_retorno ',' '}'",
"bloque_ejecutable_func : ','",
"sen_ejecutable_r : sen_ejecutable_r sen_ejecutable",
"sen_ejecutable_r : sen_ejecutable",
"imprimir : PRINT STR_1LN",
"imprimir : PRINT ','",
"sen_control : WHILE '(' condicion ')' DO bloque_sen_ejecutable ','",
"sen_control : WHILE condicion ')' DO bloque_sen_ejecutable ','",
"sen_control : WHILE '(' condicion DO bloque_sen_ejecutable ','",
"sen_control : WHILE condicion DO bloque_sen_ejecutable ','",
"sen_control : WHILE '(' condicion ')' DO ','",
"clase : CLASS ID '{' '}'",
"clase : CLASS ID '{' cuerpo_clase '}'",
"clase : CLASS ID ','",
"clase : CLASS ID cuerpo_clase '}'",
"cuerpo_clase : cuerpo_clase sen_declarativa",
"cuerpo_clase : sen_declarativa",
};

//#line 208 "gramatica.y"

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

LexicalAnalyzer lexicalAnalyzer;
SymbolTable st;

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
}

public Parser(LexicalAnalyzer lexicalAnalyzer, SymbolTable st) {
    this.lexicalAnalyzer = lexicalAnalyzer;
    this.st = st;
    //yydebug = true;
}
//#line 734 "Parser.java"
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
//#line 22 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al final del programa");}
break;
case 9:
//#line 31 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una sentencia ejecutable");}
break;
case 13:
//#line 39 "gramatica.y"
{ yyval.ival = SHORT; }
break;
case 14:
//#line 40 "gramatica.y"
{ yyval.ival = UINT; }
break;
case 15:
//#line 41 "gramatica.y"
{ yyval.ival = FLOAT; }
break;
case 16:
//#line 44 "gramatica.y"
{verificarRango(val_peek(0).ival);}
break;
case 18:
//#line 46 "gramatica.y"
{verificarRango(val_peek(0).ival);}
break;
case 19:
//#line 47 "gramatica.y"
{resolverSigno(val_peek(0).ival); verificarRango(val_peek(0).ival);}
break;
case 20:
//#line 48 "gramatica.y"
{resolverSigno(val_peek(0).ival); verificarRango(val_peek(0).ival);}
break;
case 21:
//#line 49 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "No se puede negar un unsigned int");}
break;
case 24:
//#line 56 "gramatica.y"
{ agregarFuncion(val_peek(6).ival, "VOID", val_peek(4).ival); }
break;
case 25:
//#line 57 "gramatica.y"
{ agregarFuncion(val_peek(5).ival, "VOID", -1); }
break;
case 26:
//#line 58 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al comienzo de la función");}
break;
case 27:
//#line 59 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al comienzo de la función");}
break;
case 28:
//#line 62 "gramatica.y"
{agregarParametro(val_peek(0).ival, val_peek(1).ival); yyval.ival = val_peek(0).ival; }
break;
case 29:
//#line 63 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un identificador");}
break;
case 30:
//#line 64 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el tipo del parametro");}
break;
case 43:
//#line 86 "gramatica.y"
{agregarEstructura("Asignacion al identificador ", val_peek(2).ival);}
break;
case 44:
//#line 87 "gramatica.y"
{agregarEstructura("Asignacion al identificador ", val_peek(2).ival);}
break;
case 45:
//#line 88 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una expresión aritmética");}
break;
case 46:
//#line 89 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un identificador");}
break;
case 47:
//#line 90 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un signo igual");}
break;
case 48:
//#line 91 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una expresión aritmética");}
break;
case 49:
//#line 92 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un signo igual");}
break;
case 50:
//#line 95 "gramatica.y"
{agregarEstructuraLlamados("Invocacion a la funcion ", val_peek(3).ival);}
break;
case 51:
//#line 96 "gramatica.y"
{agregarEstructuraLlamados("Invocacion a la funcion ", val_peek(2).ival);}
break;
case 52:
//#line 97 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
break;
case 53:
//#line 98 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
break;
case 54:
//#line 99 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
break;
case 55:
//#line 100 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
break;
case 56:
//#line 103 "gramatica.y"
{agregarEstructuraLlamados("Invocacion al metodo ", val_peek(3).ival);}
break;
case 57:
//#line 104 "gramatica.y"
{agregarEstructuraLlamados("Invocacion al metodo ", val_peek(2).ival);}
break;
case 58:
//#line 105 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
break;
case 59:
//#line 106 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
break;
case 60:
//#line 109 "gramatica.y"
{yyval.ival = agregarAtributo(val_peek(2).ival, val_peek(0).ival);}
break;
case 61:
//#line 112 "gramatica.y"
{agregarEstructura("IF");}
break;
case 62:
//#line 113 "gramatica.y"
{agregarEstructura("IF");}
break;
case 63:
//#line 114 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un bloque de sentencias ejecutables");}
break;
case 64:
//#line 115 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un bloque de sentencias ejecutables");}
break;
case 65:
//#line 116 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un bloque de sentencias ejecutables");}
break;
case 66:
//#line 119 "gramatica.y"
{agregarEstructura("IF");}
break;
case 67:
//#line 120 "gramatica.y"
{agregarEstructura("IF");}
break;
case 68:
//#line 121 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un bloque de sentencias ejecutables");}
break;
case 69:
//#line 122 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un bloque de sentencias ejecutables");}
break;
case 71:
//#line 126 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una expresión aritmética");}
break;
case 72:
//#line 127 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una expresión aritmética");}
break;
case 73:
//#line 128 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un comparador");}
break;
case 74:
//#line 129 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba dos expresión aritmética");}
break;
case 78:
//#line 137 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
break;
case 79:
//#line 138 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
break;
case 80:
//#line 139 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
break;
case 81:
//#line 140 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
break;
case 82:
//#line 141 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
break;
case 83:
//#line 142 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
break;
case 87:
//#line 148 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
break;
case 88:
//#line 149 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
break;
case 89:
//#line 150 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
break;
case 90:
//#line 151 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
break;
case 103:
//#line 171 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al comienzo del bloque de sentencias ejecutables");}
break;
case 104:
//#line 172 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al final del bloque de sentencias ejecutables");}
break;
case 105:
//#line 173 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera un bloque de sentencias ejecutables");}
break;
case 111:
//#line 186 "gramatica.y"
{agregarEstructura("PRINT");}
break;
case 112:
//#line 187 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una cadena para imprimir");}
break;
case 113:
//#line 190 "gramatica.y"
{agregarEstructura("WHILE");}
break;
case 114:
//#line 191 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un parentesis al comienzo de la condicion");}
break;
case 115:
//#line 192 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un parentesis al final de la condicion");}
break;
case 116:
//#line 193 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera que la condicion este encerrada entre parentesis");}
break;
case 117:
//#line 194 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un bloque de sentencias ejecutables");}
break;
case 118:
//#line 197 "gramatica.y"
{agregarClase(val_peek(2).ival, "CLASS");}
break;
case 119:
//#line 198 "gramatica.y"
{agregarClase(val_peek(3).ival, "CLASS");}
break;
case 120:
//#line 199 "gramatica.y"
{agregarClase(val_peek(1).ival, "FDCLASS");}
break;
case 121:
//#line 200 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al final de la clase");}
break;
//#line 1171 "Parser.java"
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
