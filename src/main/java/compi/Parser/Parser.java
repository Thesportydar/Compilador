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
    9,    9,    6,    6,    7,    7,   10,   10,   12,   11,
   11,   11,   11,   11,   11,   13,    3,    3,    3,    3,
    3,   15,   15,   15,   15,   15,   15,   16,   16,   16,
   16,   16,   19,   19,   19,   19,   21,   21,   14,   14,
   14,   14,   17,   17,   24,   25,   22,   27,   27,   27,
   27,   27,   20,   20,   20,   20,   20,   20,   20,   20,
   20,   29,   29,   29,   29,   29,   29,   29,   30,   30,
   30,   30,   28,   28,   28,   28,   28,   28,   26,   26,
   26,   26,   26,   23,   23,   23,   31,   31,   18,   18,
    4,    4,   32,    8,    8,    8,    8,   33,   34,   34,
};
final static short yylen[] = {                            2,
    3,    2,    2,    2,    2,    1,    1,    1,    2,    3,
    1,    1,    1,    1,    1,    1,    1,    1,    1,    2,
    2,    2,    3,    1,    4,    3,    5,    4,    2,    4,
    4,    2,    3,    3,    1,    1,    2,    2,    2,    2,
    2,    3,    3,    2,    2,    2,    2,    4,    3,    3,
    2,    2,    4,    3,    2,    2,    3,    3,    7,    5,
    5,    5,    4,    6,    1,    1,    3,    3,    2,    2,
    1,    1,    3,    3,    1,    4,    4,    4,    4,    4,
    4,    3,    3,    1,    4,    4,    4,    4,    1,    2,
    1,    1,    1,    1,    1,    1,    1,    1,    1,    3,
    2,    2,    2,    2,    5,    1,    2,    1,    2,    2,
    5,    4,    1,    3,    4,    2,    3,    2,    2,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,  113,   13,
   14,   15,    0,    0,    8,    6,    7,    0,   11,   12,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    9,
    0,   51,    0,    0,    0,    0,  109,  110,  118,    0,
    0,   17,   18,   19,    0,   91,    0,    0,    0,   84,
    1,    3,    4,    5,   24,    0,    0,    0,    0,    0,
    0,   35,   37,   38,   39,   40,   41,    0,   56,    0,
    0,    0,    0,   16,    0,  116,  120,    0,   49,    0,
    0,   57,   95,   96,   94,   93,   97,   98,    0,    0,
    0,    0,    0,    0,    0,   65,    0,    0,   90,   20,
   22,   21,    0,    0,    0,    0,   10,    0,    0,    0,
   36,    0,    0,   26,    0,    0,   32,   54,    0,    0,
   58,    0,  114,    0,  117,  119,   48,    0,   67,    0,
  103,  108,    0,    0,   63,  101,  107,   28,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   82,    0,    0,   83,   23,    0,    0,  106,    0,    0,
   25,   33,   34,    0,    0,   53,  112,    0,  115,    0,
  100,    0,   66,   29,   27,    0,    0,    0,    0,    0,
    0,   85,   86,   87,   88,    0,    0,    0,  104,    0,
    0,   30,   31,  111,   64,    0,   61,    0,   62,    0,
   60,    0,    0,  105,   59,
};
final static short yydgoto[] = {                          2,
   14,   59,   94,   17,   18,   56,   19,   20,   46,   21,
   61,  140,  159,   62,   22,   23,   24,   25,   26,   47,
   48,   36,  160,   95,  172,   96,   90,   91,   49,   50,
   97,   28,   29,   78,
};
final static short yysindex[] = {                      -106,
  144,    0,   -7,  145,   54,  -39, -174, -149,    0,    0,
    0,    0,   12,  163,    0,    0,    0, -147,    0,    0,
  199,   69,   72,  117,  128,  130,   74,   54,  126,    0,
   89,    0,   12, -121,   66,  286,    0,    0,    0,  123,
  -16,    0,    0,    0, -146,    0,   58,  105,   85,    0,
    0,    0,    0,    0,    0,   26,   54,  215, -138, -138,
  176,    0,    0,    0,    0,    0,    0,   92,    0,   12,
  -77,   58,  -75,    0,  -79,    0,    0,  -65,    0,  103,
   58,    0,    0,    0,    0,    0,    0,    0,   45,  153,
   12,  145,  -43,    0, -197,    0,  272,  118,    0,    0,
    0,    0,   96,  107,  100,  111,    0,  -62,   56,  229,
    0,  152,  156,    0, -138, -138,    0,    0,  119,   58,
    0,   34,    0,  254,    0,    0,    0,   12,    0,   58,
    0,    0,  326,  286,    0,    0,    0,    0,  -53,  166,
   12,   12,   12,   85,   12,   12,   12,   85,   12,   12,
    0,   12,   12,    0,    0,  -19,  -43,    0,  165,  -83,
    0,    0,    0,  167,  168,    0,    0,  169,    0,   58,
    0,  -44,    0,    0,    0,   85,   85,   85,   85,   85,
   85,    0,    0,    0,    0,  -32,  -40,   94,    0,  -17,
  173,    0,    0,    0,    0,  138,    0,  177,    0,  -38,
    0,   98,  183,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,  -27,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  228,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  192,    0,  204,    0,    0,    0,    0,    0,    0,    0,
  -41,    0,    0,    0,    0,    0,  206,  -34,  -29,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  207,    0,  213,
    0,  214,    0,    0,    0,    0,    0,    0,    0,  218,
  219,    0,    0,    0,    0,    0,    0,    0,  230,    0,
  248,    0,    0,   68,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  226,
    0,    0,    0,    0,    0,    0,    0,  252,    0,  253,
    0,    0,   43,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  -21,    0,    0,    0,   -9,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  255,
    0,    0,    0,    0,    0,   -1,    4,   11,   24,   31,
   36,    0,    0,    0,    0,    0,    0,  -78,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,  470,  449,  294,  232,    0,    0,    0,    0,    0,
  258,    0,  331,  -11,    0,    0,    0,    0,    0,  140,
  479,   61,  -98,    0,    0,  -25,    0,  235,  409,   16,
  -55,    0,    0,  257,
};
final static int YYTABLESIZE=675;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         89,
   89,   89,   89,   89,   38,   89,   92,   92,   92,   92,
   92,   75,   92,   75,   75,   75,    1,   13,   89,   73,
   89,   73,   73,   73,  158,   92,  158,   92,   13,   34,
   75,   74,   75,   74,   74,   74,   30,  133,   73,   76,
   73,   76,   76,   76,   77,  123,   77,   77,   77,  117,
   74,   78,   74,   78,   78,   78,   45,  187,   76,  125,
   76,  134,  135,   77,   79,   77,   79,   79,   79,  107,
   78,   80,   78,   80,   80,   80,   81,  167,   81,   81,
   81,  131,   39,   79,  108,   79,  102,  103,   73,  104,
   80,  200,   80,   35,   13,   81,  168,   81,  117,  158,
  103,  188,  104,  186,   87,  186,   88,   40,  173,   55,
   45,   99,   63,   68,   69,   64,   13,  109,   45,   71,
  151,  154,  100,  101,  102,   87,  105,   88,  108,   79,
  196,  106,  118,   45,   70,   82,   45,  142,  141,  111,
   45,  149,  143,  127,   45,  103,  150,  104,  146,  145,
   71,   45,  152,  147,   13,   45,   93,  153,  138,  166,
   65,  103,   98,  104,  182,  183,   72,  184,  185,   76,
   80,   66,   81,   67,   89,  190,  191,   74,  157,  121,
  102,  102,    7,    8,   31,   32,   10,   11,   12,  122,
   34,   74,  108,  129,  155,  162,    7,    8,   13,  163,
   10,   11,   12,  174,   13,   33,  175,  119,  189,  120,
  192,  193,  194,   92,    5,  195,  201,    6,  171,  197,
  202,  203,  204,   13,   92,    5,  205,    2,    6,   16,
  130,   89,   89,   89,   89,   52,   13,   37,   92,   92,
   92,   92,  199,   75,   75,   75,   75,   44,   75,   45,
   55,   73,   73,   73,   73,   99,   46,   47,  111,   13,
  111,   50,   42,   74,   74,   74,   74,  170,   41,   43,
   71,   76,   76,   76,   76,   13,   77,   77,   77,   77,
   42,   43,   44,   78,   78,   78,   78,   51,   72,   13,
   92,    5,   69,   70,    6,   68,   79,   79,   79,   79,
  114,  102,  102,   80,   80,   80,   80,   54,   81,   81,
   81,   81,   92,    5,  156,  110,    6,   83,   84,   85,
   86,   58,   41,  128,  108,  108,   99,   99,  108,  139,
   41,  124,   13,  111,   42,   43,   44,    0,   83,   84,
   85,   86,   42,   43,   44,   41,   13,    0,   41,    0,
   92,    5,   41,  161,    6,    0,   41,   42,   43,   44,
   42,   43,   44,   41,   42,   43,   44,   41,   42,   43,
   44,  111,    0,    0,   74,   42,   43,   44,  169,   42,
   43,   44,   74,   10,   11,   12,   13,    7,    8,  112,
  113,   10,   11,   12,   92,    5,  136,    0,    6,    3,
    4,    5,    0,    0,    6,    7,    8,    9,   93,   10,
   11,   12,    0,    0,    0,  111,    0,    0,    0,    4,
    5,    0,    0,    6,    7,    8,    9,    0,   10,   11,
   12,    0,    4,   57,    0,    0,    6,    7,    8,    0,
    0,   10,   11,   12,    0,  164,  165,    0,    0,   16,
  171,    0,    0,    0,    0,    4,   57,    0,    0,    6,
    7,    8,   53,    0,   10,   11,   12,    0,    0,   60,
   15,    4,   57,    0,    0,    6,    7,    8,    0,   27,
   10,   11,   12,   52,    0,    4,   57,    0,    0,    6,
    7,    8,   27,    0,   10,   11,   12,    0,   77,   27,
    0,    0,    0,    0,    0,    0,   60,    0,    0,  116,
   74,  144,  148,    0,   27,    7,    8,    0,  198,   10,
   11,   12,    0,    0,    0,    0,  198,    0,   92,    5,
  115,    0,    6,    0,    0,    0,   27,    0,    0,   27,
    0,  132,   92,    5,   77,  137,    6,  126,    0,  176,
  177,  178,    0,  179,  180,  181,    0,    0,  116,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   27,    0,    0,    0,   27,    0,    0,    0,  115,
    0,  137,   92,    5,    0,    0,    6,   27,   27,    0,
    0,    0,    0,  126,    0,    0,    0,    0,    0,    0,
   27,    0,    0,    0,    0,  132,    0,    0,    0,    0,
    0,   27,   27,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  132,   27,  137,    0,    0,    0,
    0,    0,    0,    0,  137,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   27,    0,   27,    0,    0,    0,
    0,    0,    0,    0,   27,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
   42,   43,   44,   45,   44,   47,   41,   42,   43,   44,
   45,   41,   47,   43,   44,   45,  123,   61,   60,   41,
   62,   43,   44,   45,   44,   60,   44,   62,   61,   46,
   60,   41,   62,   43,   44,   45,   44,   93,   60,   41,
   62,   43,   44,   45,   41,  125,   43,   44,   45,   61,
   60,   41,   62,   43,   44,   45,   45,  156,   60,  125,
   62,  259,  260,   60,   41,   62,   43,   44,   45,   44,
   60,   41,   62,   43,   44,   45,   41,   44,   43,   44,
   45,  125,  257,   60,   59,   62,   44,   43,   28,   45,
   60,  190,   62,   40,   61,   60,  122,   62,  110,   44,
   43,  157,   45,  123,   60,  123,   62,  257,  134,  257,
   45,   44,   44,   40,   41,   44,   61,   57,   45,   46,
  105,  106,  269,  270,  271,   60,   42,   62,   61,   41,
  186,   47,   41,   45,   61,  257,   45,   42,   43,  278,
   45,   42,   47,   41,   45,   43,   47,   45,   42,   43,
   46,   45,   42,   47,   61,   45,  123,   47,   41,   41,
   44,   43,   40,   45,  149,  150,   27,  152,  153,   44,
   31,   44,   33,   44,   35,  259,  260,  257,  123,  257,
  259,  260,  262,  263,   40,   41,  266,  267,  268,  265,
   46,  257,  125,   41,  257,   44,  262,  263,   61,   44,
  266,  267,  268,  257,   61,   61,   41,   68,   44,   70,
   44,   44,   44,  257,  258,  260,   44,  261,  125,  260,
   44,  260,  125,   61,  257,  258,   44,    0,  261,  257,
   91,  273,  274,  275,  276,   44,   61,  277,  273,  274,
  275,  276,  260,  273,  274,  275,  276,   44,  123,   44,
   44,  273,  274,  275,  276,  272,   44,   44,  278,   61,
  278,   44,   44,  273,  274,  275,  276,  128,  257,   44,
   41,  273,  274,  275,  276,   61,  273,  274,  275,  276,
  269,  270,  271,  273,  274,  275,  276,  125,   41,   61,
  257,  258,   41,   41,  261,   41,  273,  274,  275,  276,
  125,  259,  260,  273,  274,  275,  276,   14,  273,  274,
  275,  276,  257,  258,  259,   58,  261,  273,  274,  275,
  276,  123,  257,   89,  257,  258,  259,  260,  261,   98,
  257,   75,   61,  278,  269,  270,  271,   -1,  273,  274,
  275,  276,  269,  270,  271,  257,   61,   -1,  257,   -1,
  257,  258,  257,  125,  261,   -1,  257,  269,  270,  271,
  269,  270,  271,  257,  269,  270,  271,  257,  269,  270,
  271,  278,   -1,   -1,  257,  269,  270,  271,  125,  269,
  270,  271,  257,  266,  267,  268,   61,  262,  263,   59,
   60,  266,  267,  268,  257,  258,  125,   -1,  261,  256,
  257,  258,   -1,   -1,  261,  262,  263,  264,  123,  266,
  267,  268,   -1,   -1,   -1,  278,   -1,   -1,   -1,  257,
  258,   -1,   -1,  261,  262,  263,  264,   -1,  266,  267,
  268,   -1,  257,  258,   -1,   -1,  261,  262,  263,   -1,
   -1,  266,  267,  268,   -1,  115,  116,   -1,   -1,    1,
  125,   -1,   -1,   -1,   -1,  257,  258,   -1,   -1,  261,
  262,  263,   14,   -1,  266,  267,  268,   -1,   -1,   21,
    1,  257,  258,   -1,   -1,  261,  262,  263,   -1,    1,
  266,  267,  268,   14,   -1,  257,  258,   -1,   -1,  261,
  262,  263,   14,   -1,  266,  267,  268,   -1,   29,   21,
   -1,   -1,   -1,   -1,   -1,   -1,   58,   -1,   -1,   61,
  257,  103,  104,   -1,   36,  262,  263,   -1,  188,  266,
  267,  268,   -1,   -1,   -1,   -1,  196,   -1,  257,  258,
   61,   -1,  261,   -1,   -1,   -1,   58,   -1,   -1,   61,
   -1,   93,  257,  258,   75,   97,  261,   78,   -1,  141,
  142,  143,   -1,  145,  146,  147,   -1,   -1,  110,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   93,   -1,   -1,   -1,   97,   -1,   -1,   -1,  110,
   -1,  133,  257,  258,   -1,   -1,  261,  109,  110,   -1,
   -1,   -1,   -1,  124,   -1,   -1,   -1,   -1,   -1,   -1,
  122,   -1,   -1,   -1,   -1,  157,   -1,   -1,   -1,   -1,
   -1,  133,  134,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  186,  157,  188,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  196,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  186,   -1,  188,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  196,
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
"funcion : header_funcion '{' cuerpo_funcion '}'",
"funcion : header_funcion cuerpo_funcion '}'",
"header_funcion : VOID ID '(' parametro ')'",
"header_funcion : VOID ID '(' ')'",
"parametro : tipo ID",
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
"bloque_ejecutable_func : sen_retorno ','",
"bloque_ejecutable_func : '{' sen_ejecutable_r sen_retorno ',' '}'",
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
"clase : class_header ','",
"clase : class_header cuerpo_clase '}'",
"class_header : CLASS ID",
"cuerpo_clase : cuerpo_clase sen_declarativa",
"cuerpo_clase : sen_declarativa",
};

//#line 308 ".\gramatica.y"
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
//#line 958 "Parser.java"
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
{ yyval.ival = getTipoClase(val_peek(0).ival, ambitoActual.copy()); st.delEntry(val_peek(0).ival); val_peek(0).ival = yyval.ival; tipo = yyval.ival; }
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
{if (tipo != 0) {
                                            if (!verificarDeclaracion(val_peek(0).ival)) {
                                                st.delEntry(val_peek(0).ival);
                                            } else {
                                                st.setAttribute(val_peek(0).ival, "tipo", tipo.toString());
                                                st.setLexema(val_peek(0).ival, st.getLexema(val_peek(0).ival) + ":" + ambitoActual.toString());
                                            }
                                        } else{
                                            agregarError(errores_semanticos, Parser.ERROR, String.format(ERROR_TIPO, st.getLexema(val_peek(0).ival)));
                                            st.delEntry(val_peek(0).ival);
                                        }}
break;
case 24:
//#line 64 ".\gramatica.y"
{if (tipo != 0) {
                                            if (!verificarDeclaracion(val_peek(0).ival)) {
                                                st.delEntry(val_peek(0).ival);
                                            } else {
                                                st.setAttribute(val_peek(0).ival, "tipo", tipo.toString());
                                                st.setLexema(val_peek(0).ival, st.getLexema(val_peek(0).ival) + ":" + ambitoActual.toString());
                                            }
                                        } else{
                                            agregarError(errores_semanticos, Parser.ERROR, String.format(ERROR_TIPO, st.getLexema(val_peek(0).ival)));
                                            st.delEntry(val_peek(0).ival);
                                        }}
break;
case 25:
//#line 77 ".\gramatica.y"
{ ambitoActual.pop(); }
break;
case 26:
//#line 78 ".\gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al comienzo de la función");}
break;
case 27:
//#line 81 ".\gramatica.y"
{  if (!verificarDeclaracion(val_peek(3).ival)) {
                                                        st.delEntry(val_peek(3).ival);
                                                    } else {
                                                        st.setLexema(val_peek(3).ival, st.getLexema(val_peek(3).ival) + ":" + ambitoActual.toString());
                                                        agregarFuncion(val_peek(3).ival, VOID, val_peek(1).ival);
                                                        st.setLexema(val_peek(1).ival, st.getLexema(val_peek(1).ival) + ":" + ambitoActual.toString());
                                                    }
                                                 }
break;
case 28:
//#line 89 ".\gramatica.y"
{  if (!verificarDeclaracion(val_peek(2).ival)) {
                                                        st.delEntry(val_peek(2).ival);
                                                    } else {
                                                        st.setLexema(val_peek(2).ival, st.getLexema(val_peek(2).ival) + ":" + ambitoActual.toString());
                                                        agregarFuncion(val_peek(2).ival, VOID, -1); 
                                                    }
                                                 }
break;
case 29:
//#line 98 ".\gramatica.y"
{   if (tipo != 0) {
                                                    agregarParametro(val_peek(0).ival, val_peek(1).ival);
                                                    yyval.ival = val_peek(0).ival;
                                                } else {
                                                    agregarError(errores_semanticos, Parser.ERROR, String.format(ERROR_TIPO, st.getLexema(val_peek(0).ival)));
                                                    st.delEntry(val_peek(1).ival);
                                                    st.delEntry(val_peek(0).ival);
                                                }
                                            }
break;
case 42:
//#line 128 ".\gramatica.y"
{ yyval.ival = crearTercetoAsignacion(val_peek(2).ival, val_peek(0)); st.delEntry(val_peek(2).ival); }
break;
case 43:
//#line 129 ".\gramatica.y"
{ yyval.ival = crearTercetoAsignacion(val_peek(2).ival, val_peek(0)); }
break;
case 44:
//#line 130 ".\gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una expresión aritmética");}
break;
case 45:
//#line 131 ".\gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un identificador");}
break;
case 46:
//#line 133 ".\gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una expresión aritmética");}
break;
case 47:
//#line 134 ".\gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un signo igual");}
break;
case 48:
//#line 137 ".\gramatica.y"
{ invocacionFuncion(val_peek(3).ival, val_peek(1)); st.delEntry(val_peek(3).ival); val_peek(3).ival = yyval.ival; }
break;
case 49:
//#line 138 ".\gramatica.y"
{ invocacionFuncion(val_peek(2).ival); st.delEntry(val_peek(2).ival); val_peek(2).ival = yyval.ival; }
break;
case 50:
//#line 139 ".\gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
break;
case 51:
//#line 141 ".\gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
break;
case 52:
//#line 142 ".\gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
break;
case 53:
//#line 145 ".\gramatica.y"
{  if (val_peek(3).ival != 0)
                                                                    if (val_peek(1).ival != 0)
                                                                        agregarEstructuraLlamados("Invocacion al metodo ", val_peek(3).ival);
                                                            }
break;
case 54:
//#line 149 ".\gramatica.y"
{  if (val_peek(2).ival != 0)
                                                                    agregarEstructuraLlamados("Invocacion al metodo ", val_peek(2).ival);
                                                            }
break;
case 55:
//#line 152 ".\gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
break;
case 56:
//#line 153 ".\gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
break;
case 57:
//#line 156 ".\gramatica.y"
{ yyval.ival = agregarAtributo(val_peek(2).ival, val_peek(0).ival, ambitoActual.copy());
                                                          st.delEntry(val_peek(2).ival); val_peek(2).ival = yyval.ival;
                                                          st.delEntry(val_peek(0).ival); val_peek(0).ival = yyval.ival;}
break;
case 58:
//#line 159 ".\gramatica.y"
{ yyval.ival = agregarAtributo(val_peek(2).ival, val_peek(0).ival, ambitoActual.copy());
                                                           st.delEntry(val_peek(0).ival); val_peek(0).ival = yyval.ival;}
break;
case 59:
//#line 163 ".\gramatica.y"
{agregarEstructura("IF");}
break;
case 60:
//#line 164 ".\gramatica.y"
{agregarEstructura("IF");}
break;
case 61:
//#line 165 ".\gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un bloque de sentencias ejecutables");}
break;
case 62:
//#line 166 ".\gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un bloque de sentencias ejecutables");}
break;
case 63:
//#line 169 ".\gramatica.y"
{ agregarEstructura("IF"); 
                                                         completarB("BI", pilaTercetos.getContador()+1); }
break;
case 64:
//#line 171 ".\gramatica.y"
{ agregarEstructura("IF"); 
                                                                          completarB("BI", pilaTercetos.getContador()+1); }
break;
case 65:
//#line 175 ".\gramatica.y"
{ crearTerceto("BI", -1, -1, "", ""); completarB("BF", pilaTercetos.getContador()+1);  }
break;
case 67:
//#line 181 ".\gramatica.y"
{ yyval.ival = crearTerceto("BF", val_peek(1).ival, -1, val_peek(1).sval, ""); }
break;
case 68:
//#line 184 ".\gramatica.y"
{   if (val_peek(2).ival != 0 && val_peek(0).ival != 0) {
                                                                        if (val_peek(2).dval == val_peek(0).dval) {
                                                                            yyval.ival = crearTerceto(getCmp(val_peek(1).ival), val_peek(2).ival, val_peek(0).ival, val_peek(2).sval, val_peek(0).sval);
                                                                            yyval.sval = "terceto";
                                                                        } else
                                                                            agregarError(errores_semanticos, Parser.ERROR,
                                                                                String.format(ERROR_TIPOS_INCOMPATIBLES, val_peek(2).sval, val_peek(0).sval));
                                                                    }
                                                                }
break;
case 69:
//#line 193 ".\gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una expresión aritmética");}
break;
case 70:
//#line 194 ".\gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una expresión aritmética");}
break;
case 71:
//#line 195 ".\gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un comparador");}
break;
case 72:
//#line 196 ".\gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba dos expresión aritmética");}
break;
case 73:
//#line 201 ".\gramatica.y"
{ yyval.ival = crearTercetoExp(val_peek(2), val_peek(0), "+"); yyval.sval = "terceto"; }
break;
case 74:
//#line 202 ".\gramatica.y"
{ yyval.ival = crearTercetoExp(val_peek(2), val_peek(0), "-"); yyval.sval = "terceto"; }
break;
case 75:
//#line 203 ".\gramatica.y"
{ yyval = val_peek(0); }
break;
case 76:
//#line 204 ".\gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
break;
case 77:
//#line 205 ".\gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
break;
case 78:
//#line 206 ".\gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
break;
case 79:
//#line 207 ".\gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
break;
case 80:
//#line 208 ".\gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
break;
case 81:
//#line 209 ".\gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
break;
case 82:
//#line 212 ".\gramatica.y"
{ yyval.ival = crearTercetoTermino(val_peek(2), val_peek(0), "*"); yyval.sval = "terceto"; if (yyval.ival != 0) yyval.dval = val_peek(2).dval; }
break;
case 83:
//#line 213 ".\gramatica.y"
{ yyval.ival = crearTercetoTermino(val_peek(2), val_peek(0), "/"); yyval.sval = "terceto"; if (yyval.ival != 0) yyval.dval = val_peek(2).dval; }
break;
case 84:
//#line 214 ".\gramatica.y"
{ yyval = val_peek(0); }
break;
case 85:
//#line 215 ".\gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
break;
case 86:
//#line 216 ".\gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
break;
case 87:
//#line 217 ".\gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
break;
case 88:
//#line 218 ".\gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
break;
case 89:
//#line 222 ".\gramatica.y"
{   yyval.ival = st.getPtr(st.getLexema(val_peek(0).ival), ambitoActual.copy());
                                                    if (yyval.ival != 0) {
                                                        yyval.sval = "st";
                                                        yyval.dval = Integer.parseInt(st.getAttribute(yyval.ival, "tipo")); /*chequear*/
                                                        if (!st.getLexema(val_peek(0).ival).contains(":")) {
                                                            st.delEntry(val_peek(0).ival);
                                                            val_peek(0).ival = yyval.ival;
                                                        }
                                                    } else
                                                        agregarError(errores_semanticos, Parser.ERROR,
                                                            String.format(ERROR_ALCANCE, st.getLexema(val_peek(0).ival), ambitoActual.toString()));
                                                }
break;
case 90:
//#line 234 ".\gramatica.y"
{ yyval.ival = st.getPtr(st.getLexema(val_peek(1).ival), ambitoActual.copy()); /* crearTerceto*/
                                                  yyval.sval = "st";
                                                  yyval.dval = Integer.parseInt(st.getAttribute(yyval.ival, "tipo"));
                                                  st.delEntry(val_peek(1).ival); val_peek(1).ival = yyval.ival; }
break;
case 91:
//#line 238 ".\gramatica.y"
{ yyval.ival = val_peek(0).ival; yyval.sval = "st"; yyval.dval = val_peek(0).dval; }
break;
case 92:
//#line 239 ".\gramatica.y"
{   yyval.ival = st.getPtr(st.getLexema(val_peek(0).ival), ambitoActual.copy());
                                                    if (yyval.ival != 0) {
                                                        yyval.sval = "st";
                                                        yyval.dval = Integer.parseInt(st.getAttribute(yyval.ival, "tipo"));
                                                    }
                                                }
break;
case 93:
//#line 247 ".\gramatica.y"
{ yyval.ival = NOT_EQUAL; }
break;
case 94:
//#line 248 ".\gramatica.y"
{ yyval.ival = EQUAL; }
break;
case 95:
//#line 249 ".\gramatica.y"
{ yyval.ival = GREATER_EQUAL; }
break;
case 96:
//#line 250 ".\gramatica.y"
{ yyval.ival = LESS_EQUAL; }
break;
case 97:
//#line 251 ".\gramatica.y"
{ yyval.ival = 60; }
break;
case 98:
//#line 252 ".\gramatica.y"
{ yyval.ival = 62; }
break;
case 101:
//#line 258 ".\gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al comienzo del bloque de sentencias ejecutables");}
break;
case 102:
//#line 259 ".\gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al final del bloque de sentencias ejecutables");}
break;
case 103:
//#line 260 ".\gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera un bloque de sentencias ejecutables");}
break;
case 109:
//#line 273 ".\gramatica.y"
{agregarEstructura("PRINT");}
break;
case 110:
//#line 274 ".\gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una cadena para imprimir");}
break;
case 111:
//#line 277 ".\gramatica.y"
{ agregarEstructura("WHILE");
                                                                             crearTerceto("BI", -1, -1, "", "");
                                                                             completarB("BF", pilaTercetos.getContador()+1);
                                                                             completarWhile(); }
break;
case 112:
//#line 281 ".\gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un bloque de sentencias ejecutables");}
break;
case 113:
//#line 284 ".\gramatica.y"
{ inicio_while = pilaTercetos.getContador()+1; }
break;
case 114:
//#line 287 ".\gramatica.y"
{ ambitoActual.pop(); }
break;
case 115:
//#line 288 ".\gramatica.y"
{ ambitoActual.pop(); }
break;
case 116:
//#line 289 ".\gramatica.y"
{ agregarClase(val_peek(0).ival, "FDCLASS"); ambitoActual.pop(); }
break;
case 117:
//#line 290 ".\gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al final de la clase");
                                                             ambitoActual.pop(); }
break;
case 118:
//#line 294 ".\gramatica.y"
{   if (!verificarDeclaracion(val_peek(0).ival))
                                                    st.delEntry(val_peek(0).ival);
                                                else {
                                                    st.setLexema(val_peek(0).ival, st.getLexema(val_peek(0).ival) + ":" + ambitoActual.toString());
                                                    agregarClase(val_peek(0).ival, "CLASS");
                                                }
                                            }
break;
//#line 1547 "Parser.java"
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
