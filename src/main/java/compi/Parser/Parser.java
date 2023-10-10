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
//#line 25 "Parser.java"




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
    2,    5,    5,    5,    9,    9,    9,    9,    9,    9,
    6,    6,    7,    7,    7,    7,   10,   10,   10,   11,
   11,   11,   11,   11,   11,   12,    3,    3,    3,    3,
    3,   13,   13,   13,   13,   13,   13,   14,   14,   14,
   14,   17,   17,   17,   17,   19,   15,   15,   15,   15,
   15,   20,   20,   20,   20,   20,   18,   18,   18,   18,
   18,   18,   18,   18,   18,   23,   23,   23,   24,   24,
   24,   24,   22,   22,   22,   22,   22,   22,   21,   21,
   21,   21,   21,   25,   25,   16,   16,    4,    4,    4,
    4,    4,    8,    8,    8,    8,   26,   26,
};
final static short yylen[] = {                            2,
    3,    2,    2,    2,    2,    1,    1,    1,    3,    1,
    1,    1,    1,    1,    1,    1,    1,    2,    2,    2,
    3,    1,    8,    7,    7,    6,    2,    1,    1,    4,
    4,    2,    3,    3,    1,    1,    2,    2,    2,    2,
    2,    3,    3,    2,    2,    2,    2,    4,    3,    3,
    3,    4,    3,    2,    2,    3,    8,    6,    7,    7,
    5,    3,    2,    2,    1,    1,    3,    3,    1,    4,
    4,    4,    4,    4,    4,    3,    3,    1,    1,    2,
    1,    1,    1,    1,    1,    1,    1,    1,    1,    3,
    2,    2,    2,    2,    1,    2,    2,    7,    6,    6,
    5,    6,    4,    5,    3,    4,    2,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,   12,   13,
   14,    0,    0,    8,    6,    7,    0,   10,   11,    0,
    0,    0,    0,    0,    0,    0,   15,   16,   17,    0,
    0,    0,    0,   81,    0,   82,    0,   78,    0,   96,
   97,    0,    0,   85,   86,   84,   83,    0,   87,   88,
    0,    0,    0,    0,    1,    3,    4,    5,   22,    0,
   37,   38,   39,   40,   41,    0,   55,    0,    0,   80,
   18,   19,   20,   49,    0,    0,   56,    0,    0,   51,
    0,    0,    0,    0,  105,  108,    0,    0,    0,    0,
    0,    0,    0,    9,    0,   53,    0,    0,   48,    0,
    0,    0,    0,    0,    0,    0,    0,   76,   77,    0,
  103,    0,  106,  107,   29,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   21,   52,    0,    0,
    0,    0,    0,    0,    0,   61,    0,  104,    0,    0,
    0,    0,   27,    0,    0,    0,   93,   95,    0,  101,
   91,   94,    0,    0,    0,   58,    0,   36,    0,    0,
   26,    0,    0,    0,    0,  100,  102,    0,   90,   99,
   59,   60,    0,   24,   33,   34,    0,    0,    0,   25,
   98,   57,   30,   31,   23,
};
final static short yydgoto[] = {                          2,
   13,  140,  123,   16,   17,   60,   18,   19,   34,  118,
  142,  159,   20,   21,   22,   23,   24,   51,   36,   52,
  124,   53,   37,   38,  125,   87,
};
final static short yysindex[] = {                      -119,
  -55,    0,   86,  -21,  -37, -245, -195,  -40,    0,    0,
    0,  -43,  140,    0,    0,    0, -188,    0,    0,   47,
   48,   54,   60,   67,   89,  -28,    0,    0,    0, -107,
  104,  -43, -144,    0,   98,    0,  -33,    0,   78,    0,
    0,  100,    4,    0,    0,    0,    0,   78,    0,    0,
   58,  -20,  -43,   -8,    0,    0,    0,    0,    0,  -36,
    0,    0,    0,    0,    0,  107,    0,  -43,   -8,    0,
    0,    0,    0,    0,  153,   -8,    0,   29,  -32,    0,
  -43,  -43,   75, -108,    0,    0,  -91,  -25,    1,  -43,
  352, -148,   -8,    0, -136,    0,  179,   -8,    0,  -43,
  -43,  -43,  -33,  -43,  -43,  -43,  -33,    0,    0,  206,
    0,  -84,    0,    0,    0,  154, -135,   83,  352, -137,
   -8,  357,    0,   91,  365,  352,    0,    0,  -33,  -33,
  -33,  -33,  -33,  -33,  352,    0, -213,    0,  226, -145,
 -145,  211,    0,  185,  102,  129,    0,    0,  370,    0,
    0,    0,  109, -118,  276,    0,  240,    0,  112,  113,
    0, -145, -145,  226,  263,    0,    0,  117,    0,    0,
    0,    0, -109,    0,    0,    0,  123,  125,  284,    0,
    0,    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  170,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -17,    0,    0,    0,    0,
    0,    0,    0,    0,  130,    0,  -12,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   13,    0,   32,  136,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  137,    0,  142,  151,    0,
    0,    0,    0,    0,  155,  156,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   37,
    0,    0,   38,    0,    0,    0,    0,  160,    0,    0,
    0,    0,   -5,    0,    0,    0,    8,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  164,    0,    0,    0,
   46,    0,  124,    0,    0,    0,    0,    0,   15,   20,
   40,   45,   52,   65,    0,    0,    0,    0,    0,    0,
  308,    0,    0,    0,    0,    0,    0,    0,  -41,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  330,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,  560,  543,  184,  122,    0,    0,    0,    0,    0,
  -45,  -26,    0,    0,    0,    0,    0,  645,  514,  -38,
  304,  165,   87,  -15,   99,  166,
};
final static int YYTABLESIZE=739;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         48,
   83,   30,   92,    1,   30,   12,   41,   94,   81,   89,
  104,   42,   30,   82,  105,  116,  111,   33,   39,   49,
   92,   50,   95,   79,   79,   79,   79,   79,   69,   79,
   69,   69,   69,  113,   78,   67,   79,   67,   67,   67,
  138,  120,   79,   88,   79,  155,  156,   69,   68,   69,
   68,   68,   68,   65,   67,   70,   67,   70,   70,   70,
   71,   43,   71,   71,   71,  108,  109,   68,   59,   68,
  101,  100,   66,   30,   70,  102,   70,   63,   64,   71,
   72,   71,   72,   72,   72,   73,   62,   73,   73,   73,
   61,   62,   75,  157,   75,   75,   75,   63,  165,   72,
   78,   72,   79,   64,   73,   74,   73,   74,   74,   74,
   65,   75,   77,   75,  160,  110,  126,   49,  179,   50,
  127,  143,   30,  144,   74,   31,   74,  146,   66,   67,
   30,   33,  158,   30,  150,  177,  178,   49,   80,   50,
   78,  171,   79,   85,   74,  166,   32,   96,   30,   68,
  182,   30,  170,    6,    7,  175,  176,    9,   10,   11,
  181,   71,   72,   73,  103,  107,  183,   89,  184,    2,
    6,    7,  167,   45,    9,   10,   11,    6,    7,   44,
   54,    9,   10,   11,   95,   46,  129,  130,  131,   12,
  132,  133,  134,   99,   47,   78,   58,   79,   50,   42,
   12,    3,    4,   43,   28,    5,    6,    7,    8,  117,
    9,   10,   11,   26,   12,   90,   26,   92,   92,  128,
  149,   78,   84,   79,   26,   27,   28,   29,   27,   28,
   29,  115,   44,   45,   46,   47,   27,   28,   29,   40,
    9,   10,   11,   70,   91,   12,  106,   79,   95,  112,
    0,  122,   69,    0,    0,   79,   79,   79,   79,   67,
   69,   69,   69,   69,   55,  119,   12,   67,   67,   67,
   67,   12,   68,    0,    0,    0,  139,   65,    0,   70,
   68,   68,   68,   68,   71,   26,   12,   70,   70,   70,
   70,    0,   71,   71,   71,   71,   66,   27,   28,   29,
   12,   63,   64,    0,   72,    0,    0,  164,    0,   73,
   62,    0,   72,   72,   72,   72,   75,   73,   73,   73,
   73,    0,    0,   12,   75,   75,   75,   75,  122,   74,
   44,   45,   46,   47,   26,  161,   12,   74,   74,   74,
   74,    0,   26,    0,   12,   26,   27,   28,   29,    0,
   44,   45,   46,   47,   27,   28,   29,   27,   28,   29,
   26,    6,    7,   26,  174,    9,   10,   11,   35,    0,
    0,    0,   27,   28,   29,   27,   28,   29,    0,    0,
   95,   95,   89,   89,   95,    3,    4,  180,    0,    5,
   32,    0,    0,    0,    0,    0,    3,    4,  122,    0,
    5,    6,    7,    8,    0,    9,   10,   11,  185,    0,
    3,    4,   12,  137,    5,    6,    7,   12,    0,    9,
   10,   11,  145,    0,    0,   12,    0,    0,    0,  153,
   12,    0,   35,    0,    0,    0,    0,    0,  154,    0,
    0,    3,    4,    0,    0,    5,    6,    7,    0,  168,
    9,   10,   11,    0,   32,    0,    0,    0,  173,    0,
    0,    0,    3,    4,  135,  136,    5,    3,    4,    0,
    0,    5,    6,    7,  122,    0,    9,   10,   11,    0,
    0,  147,    3,    4,    0,    0,    5,    6,    7,  151,
    0,    9,   10,   11,  169,    0,    3,    4,    0,    0,
    5,    6,    7,    0,    0,    9,   10,   11,    0,    0,
    0,    0,    0,    0,   25,    0,    0,    0,    0,    3,
    4,    0,    0,    5,    6,    7,   25,    0,    9,   10,
   11,    0,    3,    4,    0,  172,    5,    0,    0,    0,
    3,    4,    0,   15,    5,    6,    7,    0,    0,    9,
   10,   11,    0,    0,    0,   57,    0,    0,    0,    0,
   14,    0,    0,    0,   35,   35,    0,    0,   35,   35,
   35,    0,   56,   35,   35,   35,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   32,   32,    0,    0,
   32,   32,   32,    0,    0,   32,   32,   32,    0,    0,
    0,   86,    0,    0,   25,    0,    0,    0,    3,    4,
    0,    0,    5,    3,    4,    0,    0,    5,    0,    0,
    0,    3,    4,   25,    0,    5,    3,    4,    0,   25,
    5,    0,   25,    0,    0,   25,    0,    0,   25,   25,
    0,    0,    0,   86,    0,    0,  114,   35,   25,    0,
    0,    0,   25,    0,    0,   25,   54,   25,  141,   25,
    0,    0,   25,    0,  148,    0,    0,  152,   25,   69,
   25,  114,    0,    0,    0,   75,   76,   25,   25,    0,
    0,  141,    0,    0,  163,    0,  141,    0,    0,    0,
    0,  152,   25,    0,    0,    0,    0,   93,    0,  163,
    0,  162,    0,    0,    0,    0,  141,  163,    0,    0,
   97,    0,   98,    0,    0,    0,  162,    0,    0,    0,
    0,  163,    0,    0,  162,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  121,    0,    0,    0,  162,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
   39,   45,   44,  123,   45,   61,   44,   44,   42,   48,
   43,  257,   45,   47,   47,   41,  125,   46,   40,   60,
   41,   62,   59,   41,   42,   43,   44,   45,   41,   47,
   43,   44,   45,  125,   43,   41,   45,   43,   44,   45,
  125,   41,   60,   40,   62,  259,  260,   60,   41,   62,
   43,   44,   45,   41,   60,   41,   62,   43,   44,   45,
   41,  257,   43,   44,   45,   81,   82,   60,  257,   62,
   42,   43,   41,   45,   60,   47,   62,   41,   41,   60,
   41,   62,   43,   44,   45,   41,   41,   43,   44,   45,
   44,   44,   41,  139,   43,   44,   45,   44,  144,   60,
   43,   62,   45,   44,   60,   41,   62,   43,   44,   45,
   44,   60,  257,   62,  141,   41,  265,   60,  164,   62,
  257,  257,   45,   41,   60,   40,   62,  265,   40,   41,
   45,   46,  278,   45,   44,  162,  163,   60,   41,   62,
   43,  260,   45,   44,   41,   44,   61,   41,   45,   61,
  260,   45,   44,  262,  263,   44,   44,  266,  267,  268,
   44,  269,  270,  271,   78,   79,   44,   44,   44,    0,
  262,  263,   44,   44,  266,  267,  268,  262,  263,   44,
   44,  266,  267,  268,   61,   44,  100,  101,  102,   61,
  104,  105,  106,   41,   44,   43,   13,   45,   44,   44,
   61,  257,  258,   44,   41,  261,  262,  263,  264,   88,
  266,  267,  268,  257,   61,   51,  257,  259,  260,   41,
  122,   43,  123,   45,  257,  269,  270,  271,  269,  270,
  271,  257,  273,  274,  275,  276,  269,  270,  271,  277,
  266,  267,  268,  272,  265,   61,  279,  265,  125,   84,
   -1,  123,  265,   -1,   -1,  273,  274,  275,  276,  265,
  273,  274,  275,  276,  125,  265,   61,  273,  274,  275,
  276,   61,  265,   -1,   -1,   -1,  123,  265,   -1,  265,
  273,  274,  275,  276,  265,  257,   61,  273,  274,  275,
  276,   -1,  273,  274,  275,  276,  265,  269,  270,  271,
   61,  265,  265,   -1,  265,   -1,   -1,  123,   -1,  265,
  265,   -1,  273,  274,  275,  276,  265,  273,  274,  275,
  276,   -1,   -1,   61,  273,  274,  275,  276,  123,  265,
  273,  274,  275,  276,  257,  125,   61,  273,  274,  275,
  276,   -1,  257,   -1,   61,  257,  269,  270,  271,   -1,
  273,  274,  275,  276,  269,  270,  271,  269,  270,  271,
  257,  262,  263,  257,  125,  266,  267,  268,   61,   -1,
   -1,   -1,  269,  270,  271,  269,  270,  271,   -1,   -1,
  257,  258,  259,  260,  261,  257,  258,  125,   -1,  261,
   61,   -1,   -1,   -1,   -1,   -1,  257,  258,  123,   -1,
  261,  262,  263,  264,   -1,  266,  267,  268,  125,   -1,
  257,  258,   61,  110,  261,  262,  263,   61,   -1,  266,
  267,  268,  119,   -1,   -1,   61,   -1,   -1,   -1,  126,
   61,   -1,  125,   -1,   -1,   -1,   -1,   -1,  135,   -1,
   -1,  257,  258,   -1,   -1,  261,  262,  263,   -1,  146,
  266,  267,  268,   -1,  125,   -1,   -1,   -1,  155,   -1,
   -1,   -1,  257,  258,  259,  260,  261,  257,  258,   -1,
   -1,  261,  262,  263,  123,   -1,  266,  267,  268,   -1,
   -1,  125,  257,  258,   -1,   -1,  261,  262,  263,  125,
   -1,  266,  267,  268,  125,   -1,  257,  258,   -1,   -1,
  261,  262,  263,   -1,   -1,  266,  267,  268,   -1,   -1,
   -1,   -1,   -1,   -1,    1,   -1,   -1,   -1,   -1,  257,
  258,   -1,   -1,  261,  262,  263,   13,   -1,  266,  267,
  268,   -1,  257,  258,   -1,  260,  261,   -1,   -1,   -1,
  257,  258,   -1,    1,  261,  262,  263,   -1,   -1,  266,
  267,  268,   -1,   -1,   -1,   13,   -1,   -1,   -1,   -1,
    1,   -1,   -1,   -1,  257,  258,   -1,   -1,  261,  262,
  263,   -1,   13,  266,  267,  268,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  257,  258,   -1,   -1,
  261,  262,  263,   -1,   -1,  266,  267,  268,   -1,   -1,
   -1,   42,   -1,   -1,   91,   -1,   -1,   -1,  257,  258,
   -1,   -1,  261,  257,  258,   -1,   -1,  261,   -1,   -1,
   -1,  257,  258,  110,   -1,  261,  257,  258,   -1,  116,
  261,   -1,  119,   -1,   -1,  122,   -1,   -1,  125,  126,
   -1,   -1,   -1,   84,   -1,   -1,   87,    3,  135,   -1,
   -1,   -1,  139,   -1,   -1,  142,   12,  144,  116,  146,
   -1,   -1,  149,   -1,  122,   -1,   -1,  125,  155,   25,
  157,  112,   -1,   -1,   -1,   31,   32,  164,  165,   -1,
   -1,  139,   -1,   -1,  142,   -1,  144,   -1,   -1,   -1,
   -1,  149,  179,   -1,   -1,   -1,   -1,   53,   -1,  157,
   -1,  142,   -1,   -1,   -1,   -1,  164,  165,   -1,   -1,
   66,   -1,   68,   -1,   -1,   -1,  157,   -1,   -1,   -1,
   -1,  179,   -1,   -1,  165,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   90,   -1,   -1,   -1,  179,
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
"\"\"",
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
"CTE : '-' CTE_UINT",
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
"cuerpo_funcion : cuerpo_funcion sen_ejecutable",
"cuerpo_funcion : sen_declarativa sen_retorno ','",
"cuerpo_funcion : sen_ejecutable sen_retorno ','",
"cuerpo_funcion : sen_ejecutable",
"sen_retorno : RETURN",
"sen_ejecutable : asignacion ','",
"sen_ejecutable : inv_funcion ','",
"sen_ejecutable : seleccion ','",
"sen_ejecutable : imprimir ','",
"sen_ejecutable : inv_metodo ','",
"asignacion : ID '=' exp_aritmetica",
"asignacion : atributo_clase '=' exp_aritmetica",
"asignacion : '=' exp_aritmetica",
"asignacion : ID exp_aritmetica",
"asignacion : atributo_clase '='",
"asignacion : atributo_clase exp_aritmetica",
"inv_funcion : ID '(' exp_aritmetica ')'",
"inv_funcion : ID '(' ')'",
"inv_funcion : ID '(' exp_aritmetica",
"inv_funcion : ID exp_aritmetica ')'",
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
"exp_aritmetica : exp_aritmetica '-' \"\" termino",
"exp_aritmetica : exp_aritmetica '-' '/' termino",
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
"bloque_sen_ejecutable : sen_ejecutable_r '}'",
"bloque_sen_ejecutable : '{' sen_ejecutable_r",
"bloque_sen_ejecutable : '{' '}'",
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

//#line 217 "gramatica.y"

public static final String ERROR = "Error";
public static final List<String> errores_lexicos = new ArrayList<>();
public static final List<String> errores_sintacticos = new ArrayList<>();

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
            yyval = new ParserVal(lexicalAnalyzer.getPtrActual());

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

public void agregarEstructura(Integer tk) {
    String rw = lexicalAnalyzer.getReservedWord(tk);
    if (rw == null)
        rw = st.getLexema(tk);

    agregarEstructura(rw);
}

public void agregarEstructura(String s) {
    System.out.println("Linea(" + lexicalAnalyzer.getLine() + "): " + s);
}

public void agregarClase(Integer id) {
    st.setDescription(id, "CLASS");
    agregarEstructura("CLASS :" + st.getLexema(id));
}

public void resolverSigno(Integer ptr_cte) {
    st.setLexema(ptr_cte, "-" + st.getLexema(ptr_cte));
}

public void verificarRango(Integer ptr_cte) {

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
    
    Parser.imprimirErrores(errores_lexicos, "Errores Lexicos");
    Parser.imprimirErrores(errores_sintacticos, "Errores Sintacticos");
    
    parser.st.print();
}

public Parser(LexicalAnalyzer lexicalAnalyzer, SymbolTable st) {
    this.lexicalAnalyzer = lexicalAnalyzer;
    this.st = st;
    yydebug = true;
}
//#line 600 "Parser.java"
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
//#line 21 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al final del programa");}
break;
case 23:
//#line 54 "gramatica.y"
{ agregarFuncion(val_peek(6), val_peek(7), val_peek(4)); }
break;
case 24:
//#line 55 "gramatica.y"
{ agregarFuncion(val_peek(5), val_peek(6), null); }
break;
case 25:
//#line 58 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al comienzo de la función");}
break;
case 26:
//#line 59 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al comienzo de la función");}
break;
case 27:
//#line 62 "gramatica.y"
{ agregarParametro(val_peek(0), val_peek(1)); }
break;
case 28:
//#line 63 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un identificador");}
break;
case 29:
//#line 64 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el tipo del parametro");}
break;
case 42:
//#line 91 "gramatica.y"
{agregarEstructura("Asignacion al identificador " + val_peek(2));}
break;
case 44:
//#line 94 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un identificador");}
break;
case 45:
//#line 95 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un signo igual");}
break;
case 46:
//#line 96 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una expresión aritmética");}
break;
case 47:
//#line 97 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un signo igual");}
break;
case 48:
//#line 100 "gramatica.y"
{agregarEstructura("Invocacion a la funcion " + val_peek(3));}
break;
case 49:
//#line 101 "gramatica.y"
{agregarEstructura("Invocacion a la funcion " + val_peek(2));}
break;
case 50:
//#line 102 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
break;
case 51:
//#line 103 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
break;
case 52:
//#line 108 "gramatica.y"
{agregarEstructura("Invocacion al metodo " + val_peek(3));}
break;
case 53:
//#line 109 "gramatica.y"
{agregarEstructura("Invocacion al metodo " + val_peek(2));}
break;
case 54:
//#line 110 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
break;
case 55:
//#line 111 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
break;
case 57:
//#line 117 "gramatica.y"
{agregarEstructura(val_peek(7));}
break;
case 58:
//#line 118 "gramatica.y"
{agregarEstructura(val_peek(5));}
break;
case 59:
//#line 119 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un bloque de sentencias ejecutables");}
break;
case 60:
//#line 120 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un bloque de sentencias ejecutables");}
break;
case 61:
//#line 121 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un bloque de sentencias ejecutables");}
break;
case 63:
//#line 125 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una expresión aritmética");}
break;
case 64:
//#line 126 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una expresión aritmética");}
break;
case 65:
//#line 127 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un comparador");}
break;
case 66:
//#line 128 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba dos expresión aritmética");}
break;
case 70:
//#line 136 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
break;
case 71:
//#line 137 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
break;
case 72:
//#line 138 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
break;
case 73:
//#line 140 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
break;
case 74:
//#line 142 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
break;
case 75:
//#line 143 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
break;
case 91:
//#line 183 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al comienzo del bloque de sentencias ejecutables");}
break;
case 92:
//#line 184 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al final del bloque de sentencias ejecutables");}
break;
case 93:
//#line 185 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera un bloque de sentencias ejecutables");}
break;
case 96:
//#line 193 "gramatica.y"
{agregarEstructura(val_peek(1));}
break;
case 97:
//#line 194 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una cadena para imprimir");}
break;
case 98:
//#line 197 "gramatica.y"
{agregarEstructura(val_peek(6));}
break;
case 99:
//#line 198 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un parentesis al comienzo de la condicion");}
break;
case 100:
//#line 199 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un parentesis al final de la condicion");}
break;
case 101:
//#line 200 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera que la condicion este encerrada entre parentesis");}
break;
case 102:
//#line 201 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un bloque de sentencias ejecutables");}
break;
case 103:
//#line 204 "gramatica.y"
{agregarClase(val_peek(2));}
break;
case 105:
//#line 206 "gramatica.y"
{agregarFDClase(val_peek(1));}
break;
case 106:
//#line 208 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al final de la clase");}
break;
//#line 945 "Parser.java"
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
