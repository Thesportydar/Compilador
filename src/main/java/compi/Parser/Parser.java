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
    2,    5,    5,    5,    9,    9,    9,    9,    9,    6,
    6,    7,    7,    7,    7,   10,   10,   10,   11,   11,
   11,   11,   11,   11,   12,    3,    3,    3,    3,    3,
   13,   13,   13,   13,   13,   13,   14,   14,   14,   14,
   17,   17,   17,   17,   19,   15,   15,   15,   15,   15,
   20,   20,   20,   20,   20,   18,   18,   18,   18,   18,
   18,   18,   18,   18,   23,   23,   23,   24,   24,   24,
   24,   22,   22,   22,   22,   22,   22,   21,   21,   21,
   21,   21,   25,   25,   16,   16,    4,    4,    4,    4,
    4,    8,    8,    8,    8,   26,   26,
};
final static short yylen[] = {                            2,
    3,    2,    2,    2,    2,    1,    1,    1,    3,    1,
    1,    1,    1,    1,    1,    1,    1,    2,    2,    3,
    1,    8,    7,    7,    6,    2,    1,    1,    4,    4,
    2,    3,    3,    1,    1,    2,    2,    2,    2,    2,
    3,    3,    2,    2,    2,    2,    4,    3,    3,    3,
    4,    3,    2,    2,    3,    8,    6,    7,    7,    5,
    3,    2,    2,    1,    1,    3,    3,    1,    4,    4,
    4,    4,    4,    4,    3,    3,    1,    1,    2,    1,
    1,    1,    1,    1,    1,    1,    1,    1,    3,    2,
    2,    2,    2,    1,    2,    2,    7,    6,    6,    5,
    6,    4,    5,    3,    4,    2,    1,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,   12,   13,
   14,    0,    0,    8,    6,    7,    0,   10,   11,    0,
    0,    0,    0,    0,    0,    0,   15,   16,   17,    0,
    0,    0,    0,   80,    0,   81,    0,   77,    0,   95,
   96,    0,    0,   84,   85,   83,   82,    0,   86,   87,
    0,    0,    0,    0,    1,    3,    4,    5,   21,    0,
   36,   37,   38,   39,   40,    0,   54,    0,    0,   79,
   18,   19,   48,    0,    0,   55,    0,    0,   50,    0,
    0,    0,    0,  104,  107,    0,    0,    0,    0,    0,
    0,    0,    9,    0,   52,    0,    0,   47,    0,    0,
    0,    0,    0,    0,    0,    0,   75,   76,    0,  102,
    0,  105,  106,   28,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   20,   51,    0,    0,    0,
    0,    0,    0,    0,   60,    0,  103,    0,    0,    0,
    0,   26,    0,    0,    0,   92,   94,    0,  100,   90,
   93,    0,    0,    0,   57,    0,   35,    0,    0,   25,
    0,    0,    0,    0,   99,  101,    0,   89,   98,   58,
   59,    0,   23,   32,   33,    0,    0,    0,   24,   97,
   56,   29,   30,   22,
};
final static short yydgoto[] = {                          2,
   13,  139,  122,   16,   17,   60,   18,   19,   34,  117,
  141,  158,   20,   21,   22,   23,   24,   51,   36,   52,
  123,   53,   37,   38,  124,   86,
};
final static short yysindex[] = {                      -119,
  -55,    0,   86,  -21,  -37, -245, -195,  -40,    0,    0,
    0,  -43,  140,    0,    0,    0, -191,    0,    0,   54,
   60,   72,   73,   80,   89,  -28,    0,    0,    0, -234,
  104,  -43, -135,    0,   70,    0,  -33,    0,   78,    0,
    0,  100,    4,    0,    0,    0,    0,   78,    0,    0,
   58,  -20,  -43,   24,    0,    0,    0,    0,    0,  -36,
    0,    0,    0,    0,    0,  107,    0,  -43,   24,    0,
    0,    0,    0,   92,   24,    0,   29,  -32,    0,  -43,
  -43,   87, -108,    0,    0,  -91,  -25,    1,  -43,  352,
 -129,   24,    0, -118,    0,  153,   24,    0,  -43,  -43,
  -43,  -33,  -43,  -43,  -43,  -33,    0,    0,  206,    0,
  -84,    0,    0,    0,  154, -116,  105,  352, -114,   24,
  357,    0,  109,  365,  352,    0,    0,  -33,  -33,  -33,
  -33,  -33,  -33,  352,    0, -213,    0,  226, -122, -122,
  211,    0,  185,  113,  129,    0,    0,  370,    0,    0,
    0,  117,  -98,  276,    0,  240,    0,  119,  120,    0,
 -122, -122,  226,  263,    0,    0,  123,    0,    0,    0,
    0,  -90,    0,    0,    0,  125,  130,  284,    0,    0,
    0,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  180,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -17,    0,    0,    0,    0,
    0,    0,    0,    0,  137,    0,  -12,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   13,    0,   32,  142,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  151,    0,  155,  156,    0,
    0,    0,    0,  160,  161,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   37,    0,
    0,   38,    0,    0,    0,    0,  166,    0,    0,    0,
    0,   -5,    0,    0,    0,    8,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  175,    0,    0,    0,   46,
    0,  124,    0,    0,    0,    0,    0,   15,   20,   40,
   45,   52,   65,    0,    0,    0,    0,    0,    0,  308,
    0,    0,    0,    0,    0,    0,    0,  -41,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  330,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,  599,  559,  184,  133,    0,    0,    0,    0,    0,
  -44,  -19,    0,    0,    0,    0,    0,  536,  515,  -38,
  305,  170,   88,   11,  101,  141,
};
final static int YYTABLESIZE=777;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         48,
   82,   30,   91,    1,   30,   12,   41,   93,   80,   88,
  103,   42,   30,   81,  104,  115,  110,   33,   39,   49,
   91,   50,   94,   78,   78,   78,   78,   78,   68,   78,
   68,   68,   68,  112,   71,   66,   72,   66,   66,   66,
  137,  119,   78,   87,   78,  154,  155,   68,   67,   68,
   67,   67,   67,   64,   66,   69,   66,   69,   69,   69,
   70,   43,   70,   70,   70,   59,   77,   67,   78,   67,
  100,   99,   65,   30,   69,  101,   69,   62,   63,   70,
   71,   70,   71,   71,   71,   72,   61,   72,   72,   72,
  107,  108,   74,  156,   74,   74,   74,   61,  164,   71,
   77,   71,   78,   62,   72,   73,   72,   73,   73,   73,
   79,   74,   77,   74,   78,   63,   64,   49,  178,   50,
  159,   76,   30,   65,   73,   31,   73,  109,   66,   67,
   30,   33,   98,   30,   77,  125,   78,   49,  126,   50,
  142,  176,  177,   84,   73,  143,   32,   95,   30,   68,
  145,   30,  149,    6,    7,  157,  165,    9,   10,   11,
  169,  170,  174,  175,  102,  106,  180,   88,  182,  181,
    6,    7,  166,  183,    9,   10,   11,    6,    7,    2,
   44,    9,   10,   11,   94,   43,  128,  129,  130,   12,
  131,  132,  133,  127,   53,   77,   58,   78,   45,   46,
   12,    3,    4,   49,   41,    5,    6,    7,    8,   42,
    9,   10,   11,   26,   12,   27,   26,   91,   91,  116,
   89,  148,   83,  111,   26,   27,   28,   29,   27,   28,
   29,  114,   44,   45,   46,   47,   27,   28,   29,   40,
    9,   10,   11,   70,   90,   12,  105,   78,   94,    0,
    0,  121,   68,    0,    0,   78,   78,   78,   78,   66,
   68,   68,   68,   68,   55,  118,   12,   66,   66,   66,
   66,   12,   67,    0,    0,    0,  138,   64,    0,   69,
   67,   67,   67,   67,   70,   26,   12,   69,   69,   69,
   69,    0,   70,   70,   70,   70,   65,   27,   28,   29,
   12,   62,   63,    0,   71,    0,    0,  163,    0,   72,
   61,    0,   71,   71,   71,   71,   74,   72,   72,   72,
   72,    0,    0,   12,   74,   74,   74,   74,  121,   73,
   44,   45,   46,   47,   26,  160,   12,   73,   73,   73,
   73,    0,   26,    0,   12,   26,   27,   28,   29,    0,
   44,   45,   46,   47,   27,   28,   29,   27,   28,   29,
   26,    6,    7,   26,  173,    9,   10,   11,   34,    0,
    0,    0,   27,   28,   29,   27,   28,   29,    0,    0,
   94,   94,   88,   88,   94,    3,    4,  179,    0,    5,
   31,    0,    0,    0,    0,    0,    3,    4,  121,    0,
    5,    6,    7,    8,    0,    9,   10,   11,  184,    0,
    3,    4,   12,  136,    5,    6,    7,   12,    0,    9,
   10,   11,  144,    0,    0,   12,    0,    0,    0,  152,
   12,    0,   34,    0,    0,    0,    0,    0,  153,    0,
    0,    3,    4,    0,    0,    5,    6,    7,    0,  167,
    9,   10,   11,    0,   31,    0,    0,    0,  172,    0,
    0,    0,    3,    4,  134,  135,    5,    3,    4,    0,
    0,    5,    6,    7,  121,    0,    9,   10,   11,    0,
    0,  146,    3,    4,    0,    0,    5,    6,    7,  150,
    0,    9,   10,   11,  168,    0,    3,    4,    0,    0,
    5,    6,    7,    0,    0,    9,   10,   11,    0,    0,
    0,    0,    0,    0,    0,   25,    0,    0,    0,    3,
    4,    0,    0,    5,    6,    7,    0,   25,    9,   10,
   11,    0,    3,    4,    0,  171,    5,    0,   35,    0,
    3,    4,    0,    0,    5,    6,    7,   54,    0,    9,
   10,   11,    0,    0,    0,    0,    0,    0,    0,   15,
   69,    0,    0,    0,   34,   34,   74,   75,   34,   34,
   34,   57,    0,   34,   34,   34,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   31,   31,   92,    0,
   31,   31,   31,    0,    0,   31,   31,   31,    0,   14,
    0,   96,    0,   97,   25,    0,    0,    0,    3,    4,
    0,   56,    5,    3,    4,    0,    0,    5,    0,    0,
    0,    3,    4,   25,  120,    5,    3,    4,    0,   25,
    5,    0,   25,    0,    0,   25,    0,    0,   25,   25,
   85,    0,    0,    0,    0,    0,    0,    0,   25,    0,
    0,    0,   25,    0,    0,   25,    0,   25,    0,   25,
    0,    0,   25,    0,    0,    0,    0,    0,   25,    0,
   25,    0,    0,  140,    0,    0,    0,   25,   25,  147,
    0,   85,  151,    0,  113,    0,    0,    0,    0,    0,
    0,    0,   25,    0,    0,    0,  140,    0,    0,  162,
    0,  140,    0,    0,    0,    0,  151,    0,    0,  113,
    0,    0,    0,    0,  162,    0,    0,    0,    0,    0,
    0,  140,  162,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  162,    0,    0,  161,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  161,    0,    0,    0,    0,    0,
    0,    0,  161,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  161,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
   39,   45,   44,  123,   45,   61,   44,   44,   42,   48,
   43,  257,   45,   47,   47,   41,  125,   46,   40,   60,
   41,   62,   59,   41,   42,   43,   44,   45,   41,   47,
   43,   44,   45,  125,  269,   41,  271,   43,   44,   45,
  125,   41,   60,   40,   62,  259,  260,   60,   41,   62,
   43,   44,   45,   41,   60,   41,   62,   43,   44,   45,
   41,  257,   43,   44,   45,  257,   43,   60,   45,   62,
   42,   43,   41,   45,   60,   47,   62,   41,   41,   60,
   41,   62,   43,   44,   45,   41,   41,   43,   44,   45,
   80,   81,   41,  138,   43,   44,   45,   44,  143,   60,
   43,   62,   45,   44,   60,   41,   62,   43,   44,   45,
   41,   60,   43,   62,   45,   44,   44,   60,  163,   62,
  140,  257,   45,   44,   60,   40,   62,   41,   40,   41,
   45,   46,   41,   45,   43,  265,   45,   60,  257,   62,
  257,  161,  162,   44,   41,   41,   61,   41,   45,   61,
  265,   45,   44,  262,  263,  278,   44,  266,  267,  268,
   44,  260,   44,   44,   77,   78,   44,   44,   44,  260,
  262,  263,   44,   44,  266,  267,  268,  262,  263,    0,
   44,  266,  267,  268,   61,   44,   99,  100,  101,   61,
  103,  104,  105,   41,   44,   43,   13,   45,   44,   44,
   61,  257,  258,   44,   44,  261,  262,  263,  264,   44,
  266,  267,  268,  257,   61,   41,  257,  259,  260,   87,
   51,  121,  123,   83,  257,  269,  270,  271,  269,  270,
  271,  257,  273,  274,  275,  276,  269,  270,  271,  277,
  266,  267,  268,  272,  265,   61,  279,  265,  125,   -1,
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
  257,  258,   61,  109,  261,  262,  263,   61,   -1,  266,
  267,  268,  118,   -1,   -1,   61,   -1,   -1,   -1,  125,
   61,   -1,  125,   -1,   -1,   -1,   -1,   -1,  134,   -1,
   -1,  257,  258,   -1,   -1,  261,  262,  263,   -1,  145,
  266,  267,  268,   -1,  125,   -1,   -1,   -1,  154,   -1,
   -1,   -1,  257,  258,  259,  260,  261,  257,  258,   -1,
   -1,  261,  262,  263,  123,   -1,  266,  267,  268,   -1,
   -1,  125,  257,  258,   -1,   -1,  261,  262,  263,  125,
   -1,  266,  267,  268,  125,   -1,  257,  258,   -1,   -1,
  261,  262,  263,   -1,   -1,  266,  267,  268,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,    1,   -1,   -1,   -1,  257,
  258,   -1,   -1,  261,  262,  263,   -1,   13,  266,  267,
  268,   -1,  257,  258,   -1,  260,  261,   -1,    3,   -1,
  257,  258,   -1,   -1,  261,  262,  263,   12,   -1,  266,
  267,  268,   -1,   -1,   -1,   -1,   -1,   -1,   -1,    1,
   25,   -1,   -1,   -1,  257,  258,   31,   32,  261,  262,
  263,   13,   -1,  266,  267,  268,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  257,  258,   53,   -1,
  261,  262,  263,   -1,   -1,  266,  267,  268,   -1,    1,
   -1,   66,   -1,   68,   90,   -1,   -1,   -1,  257,  258,
   -1,   13,  261,  257,  258,   -1,   -1,  261,   -1,   -1,
   -1,  257,  258,  109,   89,  261,  257,  258,   -1,  115,
  261,   -1,  118,   -1,   -1,  121,   -1,   -1,  124,  125,
   42,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  134,   -1,
   -1,   -1,  138,   -1,   -1,  141,   -1,  143,   -1,  145,
   -1,   -1,  148,   -1,   -1,   -1,   -1,   -1,  154,   -1,
  156,   -1,   -1,  115,   -1,   -1,   -1,  163,  164,  121,
   -1,   83,  124,   -1,   86,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  178,   -1,   -1,   -1,  138,   -1,   -1,  141,
   -1,  143,   -1,   -1,   -1,   -1,  148,   -1,   -1,  111,
   -1,   -1,   -1,   -1,  156,   -1,   -1,   -1,   -1,   -1,
   -1,  163,  164,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  178,   -1,   -1,  141,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  156,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  164,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  178,
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

//#line 216 "gramatica.y"

public static final String ERROR = "Error";
public static final List<String> errores_lexicos = new ArrayList<>();
public static final List<String> errores_sintacticos = new ArrayList<>();
public static final List<String> estructuras = new ArrayList<>();
public static final float FLOAT_MIN = 1.17549435E-38f;

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

public void agregarEstructura(String s) {
    estructuras.add("Linea(" + lexicalAnalyzer.getLine() + "): " + s);
}

public void agregarEstructura(String s, Integer ptr) {
    estructuras.add("Linea(" + lexicalAnalyzer.getLine() + "): " + s + st.getLexema(ptr));
}

public void agregarClase(Integer id, String description) {
    st.setAttribute(id, "description", description);
    agregarEstructura("CLASS :" + st.getLexema(id));
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
    if (parametro != null) {
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
//#line 659 "Parser.java"
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
case 15:
//#line 42 "gramatica.y"
{verificarRango(val_peek(0).ival);}
break;
case 17:
//#line 44 "gramatica.y"
{verificarRango(val_peek(0).ival);}
break;
case 18:
//#line 45 "gramatica.y"
{resolverSigno(val_peek(0).ival); verificarRango(val_peek(0).ival);}
break;
case 19:
//#line 46 "gramatica.y"
{resolverSigno(val_peek(0).ival); verificarRango(val_peek(0).ival);}
break;
case 22:
//#line 53 "gramatica.y"
{ agregarFuncion(val_peek(6).ival, "VOID", val_peek(4).ival); }
break;
case 23:
//#line 54 "gramatica.y"
{ agregarFuncion(val_peek(5).ival, "VOID", -1); }
break;
case 24:
//#line 57 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al comienzo de la función");}
break;
case 25:
//#line 58 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al comienzo de la función");}
break;
case 26:
//#line 61 "gramatica.y"
{ agregarParametro(val_peek(0).ival, val_peek(1).ival); }
break;
case 27:
//#line 62 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un identificador");}
break;
case 28:
//#line 63 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el tipo del parametro");}
break;
case 41:
//#line 90 "gramatica.y"
{agregarEstructura("Asignacion al identificador ", val_peek(2).ival);}
break;
case 42:
//#line 91 "gramatica.y"
{agregarEstructura("Asignacion al identificador ", val_peek(2).ival);}
break;
case 43:
//#line 93 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un identificador");}
break;
case 44:
//#line 94 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un signo igual");}
break;
case 45:
//#line 95 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una expresión aritmética");}
break;
case 46:
//#line 96 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un signo igual");}
break;
case 47:
//#line 99 "gramatica.y"
{agregarEstructura("Invocacion a la funcion ", val_peek(3).ival);}
break;
case 48:
//#line 100 "gramatica.y"
{agregarEstructura("Invocacion a la funcion ", val_peek(2).ival);}
break;
case 49:
//#line 101 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
break;
case 50:
//#line 102 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
break;
case 51:
//#line 107 "gramatica.y"
{agregarEstructura("Invocacion al metodo ", val_peek(3).ival);}
break;
case 52:
//#line 108 "gramatica.y"
{agregarEstructura("Invocacion al metodo ", val_peek(2).ival);}
break;
case 53:
//#line 109 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
break;
case 54:
//#line 110 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
break;
case 56:
//#line 116 "gramatica.y"
{agregarEstructura("IF");}
break;
case 57:
//#line 117 "gramatica.y"
{agregarEstructura("IF");}
break;
case 58:
//#line 118 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un bloque de sentencias ejecutables");}
break;
case 59:
//#line 119 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un bloque de sentencias ejecutables");}
break;
case 60:
//#line 120 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un bloque de sentencias ejecutables");}
break;
case 62:
//#line 124 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una expresión aritmética");}
break;
case 63:
//#line 125 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una expresión aritmética");}
break;
case 64:
//#line 126 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un comparador");}
break;
case 65:
//#line 127 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba dos expresión aritmética");}
break;
case 69:
//#line 135 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
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
//#line 139 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
break;
case 73:
//#line 141 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
break;
case 74:
//#line 142 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
break;
case 90:
//#line 182 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al comienzo del bloque de sentencias ejecutables");}
break;
case 91:
//#line 183 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al final del bloque de sentencias ejecutables");}
break;
case 92:
//#line 184 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera un bloque de sentencias ejecutables");}
break;
case 95:
//#line 192 "gramatica.y"
{agregarEstructura("PRINT");}
break;
case 96:
//#line 193 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una cadena para imprimir");}
break;
case 97:
//#line 196 "gramatica.y"
{agregarEstructura("WHILE");}
break;
case 98:
//#line 197 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un parentesis al comienzo de la condicion");}
break;
case 99:
//#line 198 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un parentesis al final de la condicion");}
break;
case 100:
//#line 199 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera que la condicion este encerrada entre parentesis");}
break;
case 101:
//#line 200 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un bloque de sentencias ejecutables");}
break;
case 102:
//#line 203 "gramatica.y"
{agregarClase(val_peek(2).ival, "CLASS");}
break;
case 104:
//#line 205 "gramatica.y"
{agregarClase(val_peek(1).ival, "FDCLASS");}
break;
case 105:
//#line 207 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al final de la clase");}
break;
//#line 1024 "Parser.java"
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
