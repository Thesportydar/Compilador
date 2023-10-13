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
    0,    0,    1,    1,    1,    1,    1,    1,    2,    2,
    2,    5,    5,    5,    9,    9,    9,    9,    9,    6,
    6,    7,    7,    7,    7,   10,   10,   10,   11,   11,
   11,   11,   11,   11,   12,    3,    3,    3,    3,    3,
   14,   14,   14,   14,   14,   14,   14,   15,   15,   15,
   15,   15,   15,   18,   18,   18,   18,   20,   16,   16,
   16,   16,   16,   13,   13,   13,   13,   21,   21,   21,
   21,   21,   19,   19,   19,   19,   19,   19,   19,   19,
   19,   25,   25,   25,   25,   25,   25,   25,   26,   26,
   26,   26,   24,   24,   24,   24,   24,   24,   22,   22,
   22,   22,   22,   23,   23,   23,   27,   27,   17,   17,
    4,    4,    4,    4,    4,    8,    8,    8,    8,   28,
   28,
};
final static short yylen[] = {                            2,
    3,    2,    2,    2,    2,    1,    1,    1,    3,    1,
    1,    1,    1,    1,    1,    1,    1,    2,    2,    3,
    1,    8,    7,    7,    6,    2,    1,    1,    4,    4,
    2,    3,    3,    1,    1,    2,    2,    2,    2,    2,
    3,    3,    2,    2,    2,    2,    2,    4,    3,    3,
    3,    2,    2,    4,    3,    2,    2,    3,    8,    6,
    7,    7,    5,    9,    7,    7,    7,    3,    2,    2,
    1,    1,    3,    3,    1,    4,    4,    4,    4,    4,
    4,    3,    3,    1,    4,    4,    4,    4,    1,    2,
    1,    1,    1,    1,    1,    1,    1,    1,    1,    3,
    2,    2,    2,    2,    5,    1,    2,    1,    2,    2,
    7,    6,    6,    5,    6,    4,    5,    3,    4,    2,
    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,   12,   13,
   14,    0,    0,    8,    6,    7,    0,   10,   11,    0,
    0,    0,    0,    0,    0,    0,   15,   16,   17,    0,
    0,   52,    0,    0,   91,    0,   92,    0,   84,    0,
  109,  110,    0,    0,   95,   96,   94,   93,    0,   97,
   98,    0,    0,    0,    0,    1,    3,    4,    5,   21,
    0,   36,   37,   38,   39,   40,    0,   57,    0,    0,
   90,   18,   19,   49,    0,    0,   58,    0,    0,   51,
    0,    0,    0,    0,  118,  121,    0,    0,    0,    0,
    0,    0,    0,    9,    0,   55,    0,    0,   48,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   82,
    0,    0,   83,    0,  116,    0,  119,  120,   28,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   20,   54,    0,    0,    0,    0,    0,    0,   85,   86,
   87,   88,    0,   63,    0,  117,    0,    0,    0,    0,
    0,   34,   26,    0,    0,    0,  103,  108,    0,  114,
  101,  107,    0,    0,    0,   60,    0,    0,   35,    0,
    0,   25,    0,    0,   31,    0,    0,  113,  115,    0,
  100,  112,   61,   62,    0,    0,   23,   32,   33,    0,
    0,    0,   24,  111,   59,    0,   29,   30,   22,    0,
    0,  106,    0,    0,    0,    0,  104,    0,    0,   66,
    0,   67,    0,    0,   65,    0,    0,    0,  105,   64,
};
final static short yydgoto[] = {                          2,
   13,  149,  127,   16,   17,   61,   18,   19,   35,  122,
  151,  203,  152,   20,   21,   22,   23,   24,   52,   37,
   53,  145,  204,   54,   38,   39,  129,   87,
};
final static short yysindex[] = {                      -121,
  160,    0,   93,  -22,  -38, -182, -170,  -40,    0,    0,
    0,  -44,  188,    0,    0,    0, -156,    0,    0,   69,
   75,   80,   88,  109,   96,  -16,    0,    0,    0, -250,
  111,    0,  -44,  -99,    0,   99,    0,  104,    0,   85,
    0,    0,  136,  126,    0,    0,    0,    0,   85,    0,
    0,   65,  -37,  -44,   33,    0,    0,    0,    0,    0,
   62,    0,    0,    0,    0,    0,  114,    0,  -44,   33,
    0,    0,    0,    0,  132,   33,    0,  -19,   38,    0,
  118,  122,  -34,  -79,    0,    0,  -72,  129,  -10,  -44,
  153, -103,   33,    0,  -89,    0,  421,   33,    0,  -44,
  -44,  -44,  104,  -44,  -44,  -44,  104,  -44,  -44,    0,
  -44,  -44,    0,  282,    0,  315,    0,    0,    0,  340,
  -83,  144,  153,  -73,   33,  265,    0,  142,  313,  153,
    0,    0,  104,  104,  104,  104,  104,  104,    0,    0,
    0,    0,  153,    0, -216,    0,  161,  417,  -85,  -85,
  352,    0,    0,  364,  154,   27,    0,    0,  436,    0,
    0,    0,  163,  -56,  431,    0,   85,  376,    0,  166,
  167,    0,  -85,  -85,    0,  417,  392,    0,    0,  171,
    0,    0,    0,    0,  -42,  175,    0,    0,    0,  178,
  179,  404,    0,    0,    0,   74,    0,    0,    0,   59,
  265,    0,  180, -204,   -7,  158,    0,   58,  210,    0,
  214,    0,  115,    1,    0,  135,  -58,  220,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  202,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -33,    0,    0,    0,    0,
  237,    0,  242,    0,    0,  252,    0,  -28,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   -2,    0,    9,  257,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  266,    0,  271,  277,
    0,    0,    0,    0,  305,  307,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   10,
    0,    0,   22,    0,    0,    0,    0,  326,    0,    0,
    0,    0,   -8,    0,    0,    0,   -3,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  336,    0,    0,    0,   41,    0,   87,    0,    0,    0,
    0,    0,    4,   17,   24,   29,   49,   54,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   52,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0, -137,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,  634,  399,  365,  306,    0,    0,    0,    0,    0,
   -5,  -45,  292,    0,    0,    0,    0,    0,  670,  356,
   77,  318,  -93,  354,  329,   97, -101,  348,
};
final static int YYTABLESIZE=826;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         49,
   30,    1,   12,   92,   30,   42,  114,   89,   89,   89,
   89,   89,   75,   89,   75,   75,   75,   40,   72,   50,
   73,   51,  101,  100,  159,   30,   89,  102,   89,   34,
  124,   75,   73,   75,   73,   73,   73,   74,   71,   74,
   74,   74,  165,  166,   76,  115,   76,   76,   76,   72,
   69,   73,  117,   73,  208,  209,   74,   77,   74,   77,
   77,   77,   70,   76,   78,   76,   78,   78,   78,   79,
  179,   79,   79,   79,   43,   78,   77,   79,   77,  105,
  104,   68,   30,   78,  106,   78,   44,   12,   79,   80,
   79,   80,   80,   80,   81,  102,   81,   81,   81,  206,
   60,  202,  202,  170,  171,   94,  205,   78,   80,   79,
   80,  217,   62,   81,  214,   81,   83,  202,   63,   12,
   95,  102,  102,   64,   50,   89,   51,  190,  191,   30,
   99,   65,   31,   32,   12,   67,   68,   30,   34,   80,
   30,   78,  168,   79,   50,   81,   51,  108,  177,  126,
   82,   74,   66,   33,   96,   30,   69,   77,   30,  108,
  211,  130,   30,  111,  109,   88,   30,  131,  112,  120,
  192,  211,   99,  153,   78,   12,   79,  110,  113,   85,
  213,  201,    6,    7,  154,  160,    9,   10,   11,    6,
    7,  156,  169,    9,   10,   11,  201,  178,    3,    4,
  167,    2,    5,  183,  139,  140,  182,  141,  142,  188,
  189,  108,   26,   12,  194,  196,   26,  195,   12,  169,
   12,  197,  198,  207,   27,   28,   29,   91,   27,   28,
   29,   89,   45,   46,   47,   48,   75,   26,   41,   89,
   89,   89,   89,  186,   75,   75,   75,   75,   12,   27,
   28,   29,  210,  215,  123,   71,   73,  216,   84,  219,
  218,   74,   71,  220,   73,   73,   73,   73,   76,   74,
   74,   74,   74,   72,   69,  126,   76,   76,   76,   76,
   53,   77,  181,    3,    4,   43,   70,    5,   78,   77,
   77,   77,   77,   79,   26,   45,   78,   78,   78,   78,
   44,   79,   79,   79,   79,   68,   27,   28,   29,   56,
  102,  102,   56,   80,   46,    3,    4,  212,   81,    5,
   47,   80,   80,   80,   80,   12,   81,   81,   81,   81,
    3,    4,  200,  144,    5,  169,  169,   45,   46,   47,
   48,   26,   12,  108,  108,   99,   99,  108,   50,   26,
   41,  169,   26,   27,   28,   29,   25,   45,   46,   47,
   48,   27,   28,   29,   27,   28,   29,   26,   25,   42,
   26,    3,    4,   12,   26,    5,   27,   59,   26,   27,
   28,   29,   27,   28,   29,  119,   27,   28,   29,  157,
   27,   28,   29,  121,    9,   10,   11,    6,    7,   15,
   12,    9,   10,   11,  126,   90,  103,  107,  128,    3,
    4,   58,   12,    5,    3,    4,    3,    4,    5,    0,
    5,    6,    7,    8,   12,    9,   10,   11,  133,  134,
  135,  116,  136,  137,  138,  169,   12,  161,    0,  146,
  155,    0,  175,    0,    3,    4,   25,  163,    5,    6,
    7,    8,   12,    9,   10,   11,    0,    0,    0,  175,
  164,  132,  148,   78,   12,   79,    0,    0,  175,   25,
    0,    0,    0,  180,    0,   25,  172,   12,   25,    0,
    0,   25,  185,  175,   25,   25,  176,    0,    0,    0,
    0,   12,    0,    0,    0,    0,   12,    0,   25,    0,
  187,    0,    0,   25,    0,    0,   25,    0,    0,   25,
    0,   25,    0,    0,   25,    0,  193,  164,  150,    0,
   25,    3,    4,   25,  158,    5,    0,  162,  199,    0,
    0,   25,   25,    0,    0,    0,    0,    0,    3,    4,
  143,  144,    5,    0,    0,    0,  150,   25,    0,  174,
    0,   25,  150,  126,    0,   25,   25,  162,    0,    0,
  181,   25,    0,    0,    0,    0,  174,    0,   25,    3,
    4,    0,   25,    5,  150,  174,    6,    7,    0,    0,
    9,   10,   11,    0,    0,    0,    0,    0,    0,    0,
  174,    0,    0,    0,    0,    0,    3,  147,    0,  158,
    5,    6,    7,    0,  162,    9,   10,   11,    3,  147,
    0,  158,    5,    6,    7,  162,    0,    9,   10,   11,
    3,  147,    0,    0,    5,    6,    7,    0,    0,    9,
   10,   11,    3,  147,   14,    0,    5,    6,    7,    0,
    0,    9,   10,   11,    0,    0,   57,    0,    3,  147,
    0,    0,    5,    6,    7,    0,    0,    9,   10,   11,
    3,  147,    0,    0,    5,    6,    7,    0,    0,    9,
   10,   11,   36,    3,  147,    0,   86,    5,    6,    7,
    0,   55,    9,   10,   11,    0,    0,    3,    4,    0,
  184,    5,    3,    4,   70,    0,    5,    0,    0,    0,
   75,    0,   76,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   86,    0,    0,
  118,    0,    0,   93,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   97,    0,   98,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  118,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  125,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  173,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  173,    0,    0,    0,    0,    0,    0,    0,    0,
  173,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  173,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
   45,  123,   61,   41,   45,   44,   41,   41,   42,   43,
   44,   45,   41,   47,   43,   44,   45,   40,  269,   60,
  271,   62,   42,   43,  126,   45,   60,   47,   62,   46,
   41,   60,   41,   62,   43,   44,   45,   41,   41,   43,
   44,   45,  259,  260,   41,  125,   43,   44,   45,   41,
   41,   60,  125,   62,  259,  260,   60,   41,   62,   43,
   44,   45,   41,   60,   41,   62,   43,   44,   45,   41,
   44,   43,   44,   45,  257,   43,   60,   45,   62,   42,
   43,   41,   45,   60,   47,   62,  257,   61,   60,   41,
   62,   43,   44,   45,   41,   44,   43,   44,   45,  201,
  257,   44,   44,  149,  150,   44,  200,   43,   60,   45,
   62,  213,   44,   60,  208,   62,   40,   44,   44,   61,
   59,  259,  260,   44,   60,   49,   62,  173,  174,   45,
   44,   44,   40,   41,   61,   40,   41,   45,   46,   41,
   45,   43,  148,   45,   60,   42,   62,   61,  154,  123,
   47,   41,   44,   61,   41,   45,   61,  257,   45,   42,
  206,  265,   45,   42,   47,   40,   45,  257,   47,   41,
  176,  217,   41,  257,   43,   61,   45,   81,   82,   44,
  123,  123,  262,  263,   41,   44,  266,  267,  268,  262,
  263,  265,  278,  266,  267,  268,  123,   44,  257,  258,
   40,    0,  261,  260,  108,  109,   44,  111,  112,   44,
   44,  125,  257,   61,   44,   41,  257,  260,   61,  278,
   61,   44,   44,   44,  269,  270,  271,  265,  269,  270,
  271,  265,  273,  274,  275,  276,  265,  257,  277,  273,
  274,  275,  276,  167,  273,  274,  275,  276,   61,  269,
  270,  271,  260,   44,  265,  272,  265,   44,  123,  125,
  260,  265,  265,   44,  273,  274,  275,  276,  265,  273,
  274,  275,  276,  265,  265,  123,  273,  274,  275,  276,
   44,  265,  125,  257,  258,   44,  265,  261,  265,  273,
  274,  275,  276,  265,  257,   44,  273,  274,  275,  276,
   44,  273,  274,  275,  276,  265,  269,  270,  271,   44,
  259,  260,  125,  265,   44,  257,  258,  260,  265,  261,
   44,  273,  274,  275,  276,   61,  273,  274,  275,  276,
  257,  258,  259,  260,  261,  278,  278,  273,  274,  275,
  276,  257,   61,  257,  258,  259,  260,  261,   44,  257,
   44,  278,  257,  269,  270,  271,    1,  273,  274,  275,
  276,  269,  270,  271,  269,  270,  271,  257,   13,   44,
  257,  257,  258,   61,  257,  261,   41,   13,  257,  269,
  270,  271,  269,  270,  271,  257,  269,  270,  271,  125,
  269,  270,  271,   88,  266,  267,  268,  262,  263,    1,
   61,  266,  267,  268,  123,   52,   78,   79,   91,  257,
  258,   13,   61,  261,  257,  258,  257,  258,  261,   -1,
  261,  262,  263,  264,   61,  266,  267,  268,  100,  101,
  102,   84,  104,  105,  106,  278,   61,  125,   -1,  125,
  123,   -1,  151,   -1,  257,  258,   91,  130,  261,  262,
  263,  264,   61,  266,  267,  268,   -1,   -1,   -1,  168,
  143,   41,  123,   43,   61,   45,   -1,   -1,  177,  114,
   -1,   -1,   -1,  156,   -1,  120,  125,   61,  123,   -1,
   -1,  126,  165,  192,  129,  130,  123,   -1,   -1,   -1,
   -1,   61,   -1,   -1,   -1,   -1,   61,   -1,  143,   -1,
  125,   -1,   -1,  148,   -1,   -1,  151,   -1,   -1,  154,
   -1,  156,   -1,   -1,  159,   -1,  125,  200,  120,   -1,
  165,  257,  258,  168,  126,  261,   -1,  129,  125,   -1,
   -1,  176,  177,   -1,   -1,   -1,   -1,   -1,  257,  258,
  259,  260,  261,   -1,   -1,   -1,  148,  192,   -1,  151,
   -1,  196,  154,  123,   -1,  200,  201,  159,   -1,   -1,
  125,  206,   -1,   -1,   -1,   -1,  168,   -1,  213,  257,
  258,   -1,  217,  261,  176,  177,  262,  263,   -1,   -1,
  266,  267,  268,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  192,   -1,   -1,   -1,   -1,   -1,  257,  258,   -1,  201,
  261,  262,  263,   -1,  206,  266,  267,  268,  257,  258,
   -1,  213,  261,  262,  263,  217,   -1,  266,  267,  268,
  257,  258,   -1,   -1,  261,  262,  263,   -1,   -1,  266,
  267,  268,  257,  258,    1,   -1,  261,  262,  263,   -1,
   -1,  266,  267,  268,   -1,   -1,   13,   -1,  257,  258,
   -1,   -1,  261,  262,  263,   -1,   -1,  266,  267,  268,
  257,  258,   -1,   -1,  261,  262,  263,   -1,   -1,  266,
  267,  268,    3,  257,  258,   -1,   43,  261,  262,  263,
   -1,   12,  266,  267,  268,   -1,   -1,  257,  258,   -1,
  260,  261,  257,  258,   25,   -1,  261,   -1,   -1,   -1,
   31,   -1,   33,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   84,   -1,   -1,
   87,   -1,   -1,   54,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   67,   -1,   69,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  116,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   90,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  151,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  168,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  177,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  192,
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

//#line 206 "gramatica.y"

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
        if (lexicalAnalyzer.getPtrActual() > 0)
            yylval = new ParserVal(lexicalAnalyzer.getPtrActual());
        else
            yylval = new ParserVal();

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

public void resolverSigno(Integer ptr_cte) {
    st.setLexema(ptr_cte, "-" + st.getLexema(ptr_cte));
}

public void verificarRango(Integer ptr_cte) {
    String lexema = st.getLexema(ptr_cte);
    String tipo = st.getAttribute(ptr_cte,"description");
    switch (tipo) {
      case "float":
        float value_f = Float.parseFloat(lexema);
        if (!(value_f <= Float.MAX_VALUE && value_f >= FLOAT_MIN || value_f >= -Float.MAX_VALUE && value_f <= -FLOAT_MIN || value_f == 0)){
            agregarError(errores_sintacticos, Parser.ERROR, "Constante FLOAT fuera de rango");
        }
      case "shortint":
        Short value_s = Short.parseShort(lexema);
        if (!(value_s <= Short.MAX_VALUE && value_s >= Short.MIN_VALUE)){
            agregarError(errores_sintacticos, Parser.ERROR, "Constante ShortInt fuera de rango");
        }
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

    Main.loadMatrixs(mI, mA, "test.csv", sttemp, errores_lexicos);
    Parser parser = new Parser(new LexicalAnalyzer("test.txt", mI, mA, errores_lexicos), sttemp);
    parser.run();
    
    Parser.imprimirErrores(errores_lexicos, "Errores Lexicos");
    Parser.imprimirErrores(errores_sintacticos, "Errores Sintacticos");
    Parser.imprimirErrores(estructuras, "Estructuras Sintacticas");
    
    parser.st.print();
}

public Parser(LexicalAnalyzer lexicalAnalyzer, SymbolTable st) {
    this.lexicalAnalyzer = lexicalAnalyzer;
    this.st = st;
    yydebug = true;
}
//#line 713 "Parser.java"
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
case 12:
//#line 38 "gramatica.y"
{ yyval.ival = SHORT; }
break;
case 13:
//#line 39 "gramatica.y"
{ yyval.ival = UINT; }
break;
case 14:
//#line 40 "gramatica.y"
{ yyval.ival = FLOAT; }
break;
case 15:
//#line 43 "gramatica.y"
{verificarRango(val_peek(0).ival);}
break;
case 17:
//#line 45 "gramatica.y"
{verificarRango(val_peek(0).ival);}
break;
case 18:
//#line 46 "gramatica.y"
{resolverSigno(val_peek(0).ival); verificarRango(val_peek(0).ival);}
break;
case 19:
//#line 47 "gramatica.y"
{resolverSigno(val_peek(0).ival); verificarRango(val_peek(0).ival);}
break;
case 22:
//#line 54 "gramatica.y"
{ agregarFuncion(val_peek(6).ival, "VOID", val_peek(4).ival); }
break;
case 23:
//#line 55 "gramatica.y"
{ agregarFuncion(val_peek(5).ival, "VOID", -1); }
break;
case 24:
//#line 56 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al comienzo de la función");}
break;
case 25:
//#line 57 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al comienzo de la función");}
break;
case 26:
//#line 60 "gramatica.y"
{agregarParametro(val_peek(0).ival, val_peek(1).ival); yyval.ival = val_peek(0).ival; }
break;
case 27:
//#line 61 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un identificador");}
break;
case 28:
//#line 62 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el tipo del parametro");}
break;
case 41:
//#line 84 "gramatica.y"
{agregarEstructura("Asignacion al identificador ", val_peek(2).ival);}
break;
case 42:
//#line 85 "gramatica.y"
{agregarEstructura("Asignacion al identificador ", val_peek(2).ival);}
break;
case 43:
//#line 86 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una expresión aritmética");}
break;
case 44:
//#line 87 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un identificador");}
break;
case 45:
//#line 88 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un signo igual");}
break;
case 46:
//#line 89 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una expresión aritmética");}
break;
case 47:
//#line 90 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un signo igual");}
break;
case 48:
//#line 93 "gramatica.y"
{agregarEstructura("Invocacion a la funcion ", val_peek(3).ival);}
break;
case 49:
//#line 94 "gramatica.y"
{agregarEstructura("Invocacion a la funcion ", val_peek(2).ival);}
break;
case 50:
//#line 95 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
break;
case 51:
//#line 96 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
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
//#line 101 "gramatica.y"
{agregarEstructura("Invocacion al metodo ", val_peek(3).ival);}
break;
case 55:
//#line 102 "gramatica.y"
{agregarEstructura("Invocacion al metodo ", val_peek(2).ival);}
break;
case 56:
//#line 103 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
break;
case 57:
//#line 104 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
break;
case 59:
//#line 110 "gramatica.y"
{agregarEstructura("IF");}
break;
case 60:
//#line 111 "gramatica.y"
{agregarEstructura("IF");}
break;
case 61:
//#line 112 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un bloque de sentencias ejecutables");}
break;
case 62:
//#line 113 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un bloque de sentencias ejecutables");}
break;
case 63:
//#line 114 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un bloque de sentencias ejecutables");}
break;
case 64:
//#line 117 "gramatica.y"
{agregarEstructura("IF");}
break;
case 65:
//#line 118 "gramatica.y"
{agregarEstructura("IF");}
break;
case 66:
//#line 119 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un bloque de sentencias ejecutables");}
break;
case 67:
//#line 120 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un bloque de sentencias ejecutables");}
break;
case 69:
//#line 124 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una expresión aritmética");}
break;
case 70:
//#line 125 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una expresión aritmética");}
break;
case 71:
//#line 126 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un comparador");}
break;
case 72:
//#line 127 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba dos expresión aritmética");}
break;
case 76:
//#line 135 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
break;
case 77:
//#line 136 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
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
case 85:
//#line 146 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
break;
case 86:
//#line 147 "gramatica.y"
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
case 101:
//#line 169 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al comienzo del bloque de sentencias ejecutables");}
break;
case 102:
//#line 170 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al final del bloque de sentencias ejecutables");}
break;
case 103:
//#line 171 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera un bloque de sentencias ejecutables");}
break;
case 109:
//#line 184 "gramatica.y"
{agregarEstructura("PRINT");}
break;
case 110:
//#line 185 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una cadena para imprimir");}
break;
case 111:
//#line 188 "gramatica.y"
{agregarEstructura("WHILE");}
break;
case 112:
//#line 189 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un parentesis al comienzo de la condicion");}
break;
case 113:
//#line 190 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un parentesis al final de la condicion");}
break;
case 114:
//#line 191 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera que la condicion este encerrada entre parentesis");}
break;
case 115:
//#line 192 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un bloque de sentencias ejecutables");}
break;
case 116:
//#line 195 "gramatica.y"
{agregarClase(val_peek(2).ival, "CLASS");}
break;
case 117:
//#line 196 "gramatica.y"
{agregarClase(val_peek(3).ival, "CLASS");}
break;
case 118:
//#line 197 "gramatica.y"
{agregarClase(val_peek(1).ival, "FDCLASS");}
break;
case 119:
//#line 198 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al final de la clase");}
break;
//#line 1138 "Parser.java"
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
