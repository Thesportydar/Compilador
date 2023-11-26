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
    2,    2,    2,    2,    2,    2,    2,    2,    2,    7,
    5,    5,    5,    5,    5,   10,   10,   10,   10,   10,
   10,   10,    6,    6,    8,    8,    8,    8,   11,   11,
   15,   12,   12,   12,   12,   13,    3,    3,    3,    3,
    3,    3,    3,    3,    3,    3,    3,    3,    3,    3,
    3,   16,   16,   16,   16,   16,   16,   18,   18,   18,
   18,   17,   17,   17,   22,   22,   14,   14,   14,   14,
   19,   19,   25,   26,   23,   28,   28,   28,   28,   28,
   21,   21,   21,   21,   21,   21,   21,   21,   21,   30,
   30,   30,   30,   30,   30,   30,   31,   31,   31,   31,
   31,   31,   29,   29,   29,   29,   29,   29,   29,   27,
   27,   27,   27,   27,   24,   24,   32,   32,   20,   20,
    4,    4,    4,   33,    9,    9,    9,    9,    9,    9,
   34,   35,   35,   35,   35,   36,
};
final static short yylen[] = {                            2,
    3,    2,    2,    2,    2,    1,    1,    1,    2,    3,
    4,    1,    1,    2,    2,    2,    2,    3,    2,    1,
    1,    1,    1,    1,    1,    1,    1,    1,    2,    2,
    2,    1,    3,    1,    5,    5,    4,    3,    5,    4,
    2,    2,    2,    1,    1,    2,    2,    2,    2,    2,
    2,    2,    2,    2,    2,    2,    1,    1,    1,    1,
    1,    3,    3,    4,    4,    2,    2,    4,    3,    3,
    2,    4,    3,    2,    3,    3,    7,    5,    5,    5,
    4,    6,    1,    1,    3,    3,    2,    2,    1,    1,
    3,    3,    1,    4,    4,    4,    4,    4,    4,    3,
    3,    1,    4,    4,    4,    4,    1,    2,    1,    1,
    2,    2,    1,    1,    1,    1,    1,    1,    1,    1,
    3,    2,    2,    2,    1,    4,    2,    1,    2,    2,
    6,    4,    5,    1,    3,    4,    5,    4,    3,    3,
    2,    2,    3,    2,    1,    2,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,  134,   21,
   22,   23,   20,    0,    0,    7,    6,    8,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    9,    0,   71,    0,    0,    0,    0,    0,  129,
  130,    0,    0,    0,    0,   26,   27,   28,    0,  109,
    0,    0,    0,  102,   25,    1,    3,    4,    5,   34,
    0,   24,    0,   16,   17,   14,   15,    0,   44,   45,
    0,   47,   52,   48,   53,   49,   54,   50,   55,   51,
   56,    0,   74,    0,    0,    0,    0,    0,    0,    0,
  145,    0,    0,   69,    0,    0,    0,   75,  115,  116,
  114,  113,  119,  117,  118,    0,    0,    0,    0,    0,
    0,    0,   83,    0,  139,    0,  112,  108,   29,   31,
   30,    0,    0,  111,    0,    0,   10,    0,    0,    0,
   38,   42,   43,   73,    0,    0,    0,   76,    0,  146,
  135,    0,    0,  140,  142,    0,  144,   68,    0,    0,
   85,    0,  124,  128,    0,    0,   81,  122,  127,   40,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  100,    0,    0,  101,   33,   11,    0,    0,
   37,    0,    0,   72,    0,    0,  132,  136,    0,  138,
  143,    0,  121,    0,   84,   41,   39,    0,    0,    0,
    0,    0,    0,  103,  104,  105,  106,    0,   46,   35,
   36,  133,    0,  137,   82,    0,    0,  125,    0,  131,
    0,    0,    0,    0,    0,    0,   79,    0,   80,    0,
   78,  126,    0,   77,
};
final static short yydgoto[] = {                          2,
   15,   69,  159,   18,   19,   61,   20,   21,   22,   50,
   23,   71,  218,  183,  162,   24,   25,   26,   27,   28,
   51,   52,   39,  219,  112,  194,  113,  107,  108,   53,
   54,  114,   30,   31,   92,   93,
};
final static short yysindex[] = {                      -119,
  717,    0,  -13,  -26,  -24,  -44, -210, -207,    0,    0,
    0,    0,    0,  299, 1089,    0,    0,    0, -193, -120,
  -23,  -21, 1138,   -7,   -5,   -4,   -3,   36, 1032,  -24,
  362,    0,  641,    0,  299,   10, -180,  644,  -10,    0,
    0,   44,   61, -170,  -40,    0,    0,    0, -117,    0,
   14,  -29,   11,    0,    0,    0,    0,    0,    0,    0,
   63,    0, -193,    0,    0,    0,    0, 1173,    0,    0,
 1158,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  738,    0,  299,   45, -152,   14, -150,   75,  337,
    0, 1188, 1278,    0,  151,   14,  299,    0,    0,    0,
    0,    0,    0,    0,    0,  -34,   82,  299,  -26,  415,
    0, -226,    0,  423,    0,  279,    0,    0,    0,    0,
    0,  759,  829,    0, 1021, 1049,    0, -132,   68, 1113,
    0,    0,    0,    0,  180,   14,  299,    0,  377,    0,
    0, 1203, 1240,    0,    0, 1278,    0,    0,   14,  299,
    0,   14,    0,    0,  519,  -10,    0,    0,    0,    0,
 -129,   88,  299,  299,  299,   11,  299,  299,  299,   11,
  299,  299,    0,  299,  299,    0,    0,    0,  -24,   95,
    0,   18,   19,    0,   14,  861,    0,    0, 1264,    0,
    0,   14,    0, -105,    0,    0,    0,   11,   11,   11,
   11,   11,   11,    0,    0,    0,    0,  -58,    0,    0,
    0,    0,  863,    0,    0, -118,  415,    0, -178,    0,
  253,  -99,  698, -111,  119,  229,    0,   43,    0,  -90,
    0,    0,  134,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,  -78,  -73,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  185,    0,    0,    0,    0,    0,
  945,  964,    0,  674,  782,  878,  902,  926,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  386,    0,    1,   25,    0,    0,    0,    0,    0,
  506,   49,   73,    0,    0,    0,    0,    0,    0,    0,
  991,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  530,    0,  -73,    0,
    0,    0,    0,    0,  554,  578,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  146,    0,  150,    0,    0,
  -42,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0, 1018,    0,
    0,    0,    0,    0,    0,  602,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  626,  154,
    0,  156,    0,    0, -161,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   97,    0,    0,    0,  121,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  847,    0,    0,    0,    0,    0,
    0,  161,    0,    0,    0,    0,    0,  145,  169,  193,
  434,  458,  482,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0, -161,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0, 1382, 1293,  194,  -12,  148,    0,    0,    0,    0,
    0,  140, -123,    0,    0,    0,    0,    0,    0,    0,
 1480, 1272,  -28, -206,    0,    0,   70,    0,  118,    8,
  -96,  -97,    0,    0,  137,  -68,
};
final static int YYTABLESIZE=1630;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         41,
   32,   88,   14,    1,  221,   37,  182,   63,  122,  222,
  123,  221,  155,   33,   34,   38,   86,  230,  128,   37,
   64,  143,   66,  146,  107,  104,  103,  105,  173,  176,
   32,   36,  156,  157,   35,   65,   72,   67,   74,   76,
   78,   32,   32,   32,   32,   32,   42,   32,  110,   43,
   14,   73,  125,   75,   77,   79,  122,  126,  123,   32,
   32,   32,   32,   60,  217,  107,  107,  107,  107,  107,
   97,  107,   93,  189,  204,  205,   98,  206,  207,   80,
  224,  225,  128,  107,  107,  107,  107,  115,  213,  110,
  110,  110,  110,  110,   81,  110,   91,  123,  123,  228,
  116,  117,  228,  161,  138,  137,  127,  110,  110,  110,
  110,  178,  110,   93,  139,   93,   93,   93,  140,  223,
   92,  128,  151,  226,  177,   32,  128,  196,  197,  166,
  170,   93,   93,   93,   93,   55,   62,   91,  209,   91,
   91,   91,  210,  211,   94,   10,   11,   12,  229,  107,
  208,  119,  120,  121,  215,   91,   91,   91,   91,  180,
  227,   92,  231,   92,   92,   92,  180,  232,   95,  233,
  198,  199,  200,  110,  201,  202,  203,  234,   25,   92,
   92,   92,   92,   24,    2,   94,   89,   94,   94,   94,
   90,  148,   96,  122,   87,  123,   88,   93,  109,    5,
  216,   86,    6,   94,   94,   94,   94,  130,   59,   95,
  129,   95,   95,   95,  128,  128,  120,  120,  128,  180,
  184,   91,  122,  150,  123,  195,  142,   95,   95,   95,
   95,  118,   40,   96,    0,   96,   96,   96,   99,  100,
  101,  102,  124,    0,    0,   92,  109,    5,    0,    0,
    6,   96,   96,   96,   96,    0,   32,   32,   32,   32,
   32,   32,   32,   32,   32,    0,   32,   32,   32,   94,
    0,    0,    0,   32,   32,   32,   32,    0,   32,   32,
  107,  107,  107,  107,  107,  107,  107,  107,  107,   14,
  107,  107,  107,   95,    0,    0,    0,  107,  107,  107,
  107,    0,  107,  107,  110,  110,  110,  110,  110,  110,
  110,  110,  110,   14,  110,  110,  110,   96,    0,  160,
    0,  110,  110,  110,  110,    0,  110,  110,   93,   93,
   93,   93,   93,   93,   93,   93,   93,    0,   93,   93,
   93,    0,    0,   49,    0,   93,   93,   93,   93,    0,
   93,   93,   91,   91,   91,   91,   91,   91,   91,   91,
   91,    0,   91,   91,   91,    0,    0,    0,    0,   91,
   91,   91,   91,    0,   91,   91,   92,   92,   92,   92,
   92,   92,   92,   92,   92,    0,   92,   92,   92,    0,
    0,    0,    0,   92,   92,   92,   92,    0,   92,   92,
   94,   94,   94,   94,   94,   94,   94,   94,   94,    0,
   94,   94,   94,    0,    0,    0,    0,   94,   94,   94,
   94,    0,   94,   94,   95,   95,   95,   95,   95,   95,
   95,   95,   95,   97,   95,   95,   95,   14,    0,    0,
    0,   95,   95,   95,   95,    0,   95,   95,   96,   96,
   96,   96,   96,   96,   96,   96,   96,   98,   96,   96,
   96,  141,    0,    0,    0,   96,   96,   96,   96,    0,
   96,   96,    0,    0,   97,   14,   97,   97,   97,    0,
    0,   99,    0,   14,   90,  109,    5,    0,    0,    6,
    0,    0,   97,   97,   97,   97,    0,    0,   98,  186,
   98,   98,   98,    0,    0,   66,  180,    0,  141,  109,
    5,    0,    0,    6,    0,    0,   98,   98,   98,   98,
    0,    0,   99,    0,   99,   99,   99,    0,    0,   67,
    0,    0,    0,    0,   55,   62,    0,    0,    0,  153,
   99,   99,   99,   99,   10,   11,   12,  158,    0,   66,
    0,    0,    0,   70,   44,   45,    0,    0,   97,    0,
    0,    0,    0,    0,   66,    0,   66,   46,   47,   48,
    0,    0,    0,   67,    0,    0,    0,   62,    0,   14,
    0,    0,   98,    0,    0,    0,    0,    0,   67,    0,
   67,    0,   55,   89,    0,    0,    0,   70,    7,    8,
    0,   63,   10,   11,   12,    0,   99,    0,    0,    0,
    0,    0,   70,    0,   70,   13,    0,   55,   89,    0,
    0,   62,    0,    7,    8,   64,    0,   10,   11,   12,
   66,    0,    0,  109,    5,    0,   62,    6,   62,    0,
   13,  141,  141,  193,    0,   63,    0,  141,  141,    0,
    0,  141,  141,  141,   67,    0,    0,    0,    0,    0,
   63,    0,   63,    0,  141,    0,    0,    0,    0,   64,
    0,  109,    5,   58,    0,    6,    0,    0,   70,  109,
    5,   94,    0,    6,   64,   49,   64,    0,   49,   97,
   97,   97,   97,   97,   97,   97,   97,   97,    0,   97,
   97,   97,   62,  104,  103,  105,   97,   97,   97,   97,
    0,   97,   97,   98,   98,   98,   98,   98,   98,   98,
   98,   98,    0,   98,   98,   98,   63,    0,    0,    0,
   98,   98,   98,   98,   58,   98,   98,   99,   99,   99,
   99,   99,   99,   99,   99,   99,    0,   99,   99,   99,
   64,    0,    0,    0,   99,   99,   99,   99,   14,   99,
   99,   66,   66,   66,   66,   66,   66,   66,   66,   66,
    0,   66,   66,   66,    0,  109,    5,   14,  134,    6,
    0,   57,   49,   66,   66,   67,   67,   67,   67,   67,
   67,   67,   67,   67,    0,   67,   67,   67,   58,    0,
  164,  163,    0,   49,    0,  165,    0,   67,   67,   70,
   70,   70,   70,   70,   70,   70,   70,   70,    0,   70,
   70,   70,  193,    0,    0,    0,    0,    0,    0,    0,
    0,   70,   70,   62,   62,   62,   62,   62,   62,   62,
   62,   62,   57,   62,   62,   62,   65,    0,    0,    0,
    0,    0,    0,    0,    0,   62,   62,   63,   63,   63,
   63,   63,   63,   63,   63,   63,    0,   63,   63,   63,
  168,  167,    0,   49,    0,  169,    0,   59,    0,   63,
   63,   64,   64,   64,   64,   64,   64,   64,   64,   64,
   65,   64,   64,   64,    0,    0,   44,   45,    0,   44,
   45,   60,    0,   64,   64,   65,   57,   65,    0,   46,
   47,   48,   46,   47,   48,    0,   99,  100,  101,  102,
    0,   14,    0,   14,    0,   61,    0,    0,    0,   58,
   58,   58,   58,   58,   58,   58,   58,   58,   59,   58,
   58,   58,    0,    0,   12,    0,    0,    0,    0,    0,
    0,   58,   58,    0,  109,    5,    0,    0,    6,    0,
    0,    0,   60,   13,    0,    0,    0,    0,    0,    0,
    0,   65,    3,    4,    5,  180,    0,    6,    7,    8,
    9,    0,   10,   11,   12,  212,   61,  220,    0,    0,
   19,    0,    0,   44,   45,   13,    0,    0,    0,    0,
    0,    0,   59,    0,    0,   12,   46,   47,   48,    0,
    0,    0,    0,    0,   44,   45,    0,   18,    0,    0,
    0,    0,    0,    0,   13,    0,   60,   46,   47,   48,
    0,    0,    0,    0,    0,    0,    0,   57,   57,   57,
   57,   57,   57,   57,   57,   57,    0,   57,   57,   57,
   61,   19,    0,    0,    0,    0,    0,    0,    0,   57,
   57,    0,  171,    0,    0,   49,    0,  172,    0,   12,
    0,   82,   83,    0,    0,    0,   49,   86,   18,    0,
    0,    0,    0,    0,   44,   45,    0,    0,   13,   85,
  174,    0,   84,   49,    0,  175,    0,   46,   47,   48,
    0,    0,   65,   65,   65,   65,   65,   65,   65,   65,
   65,    0,   65,   65,   65,   19,    0,  109,    5,  109,
    5,    6,    0,    6,   65,   65,    0,    0,    0,    0,
    0,    0,    0,   59,   59,   59,   59,   59,   59,   59,
   59,   59,   18,   59,   59,   59,    0,    0,    0,   14,
    0,    0,    0,    0,    0,   59,   59,   60,   60,   60,
   60,   60,   60,   60,   60,   60,    0,   60,   60,   60,
    0,    0,    0,   14,    0,    0,    0,    0,    0,   60,
   60,   61,   61,   61,   61,   61,   61,   61,   61,   61,
    0,   61,   61,   61,    0,    0,    0,    0,   14,    0,
   12,   12,   12,   61,   61,   12,   12,   12,   12,    0,
   12,   12,   12,   56,    0,    0,    0,    0,   14,   13,
   13,   13,   12,   12,   13,   13,   13,   13,    0,   13,
   13,   13,    0,   14,    0,    0,    0,  181,    0,    0,
    0,   13,   13,    0,    0,    0,   19,   19,   19,    0,
    0,   19,   19,   19,   19,    0,   19,   19,   19,    0,
   68,    0,    0,    0,    0,    0,    0,    0,   19,   19,
    0,    0,   29,   18,   18,   18,   44,   45,   18,   18,
   18,   18,  131,   18,   18,   18,   29,   44,   45,   46,
   47,   48,    0,   17,   29,   18,   18,    0,    0,    0,
   46,   47,   48,    0,   44,   45,    0,   58,    0,    0,
   29,    0,  144,    0,    0,   70,    0,   46,   47,   48,
    0,    0,    0,    0,    0,    0,    0,  188,    0,    0,
    0,  111,    0,    0,    0,    0,    0,    0,    0,   29,
    0,    0,   29,    0,   55,    4,    5,    0,    0,    6,
    7,    8,    9,    0,   10,   11,   12,    0,    0,    0,
   70,    0,    0,  133,  190,    0,    0,   13,   55,    4,
  179,    0,    0,    6,    7,    8,    0,    0,   10,   11,
   12,   29,   16,    0,    0,   29,    0,    0,  214,    0,
  180,   13,    0,   55,    4,    5,   57,    0,    6,    7,
    8,   29,  154,   10,   11,   12,    0,    0,    0,    0,
   29,    0,   91,   55,    4,    5,   13,    0,    6,    7,
    8,    0,  133,   10,   11,   12,   29,   29,   55,    4,
    5,  187,    0,    6,    7,    8,   13,    0,   10,   11,
   12,    0,    0,   55,   89,    0,    0,    0,  111,    7,
    8,   13,  132,   10,   11,   12,    0,   29,   55,   89,
    0,    0,    0,    0,    7,    8,   13,    0,   10,   11,
   12,   91,    0,  145,  147,    0,    0,    0,  154,   29,
    0,   13,    0,    0,   29,    0,    0,    0,   29,    0,
    0,    0,   29,    0,   29,   55,   62,   29,    0,    0,
  111,    7,    8,    0,    0,   10,   11,   12,   87,  154,
    0,  132,   95,  154,   96,    0,    0,  106,   13,   55,
   62,    0,    0,  145,  147,    7,    8,  191,    0,   10,
   11,   12,    0,   55,   62,    0,    0,    0,    0,    7,
    8,    0,   13,   10,   11,   12,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   13,    0,    0,    0,
    0,  135,    0,  136,    0,    0,    0,    0,    0,    0,
  191,    0,    0,    0,    0,    0,  149,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  152,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  185,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  192,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         44,
    0,   30,   61,  123,  123,   46,  130,   20,   43,  216,
   45,  123,  110,   40,   41,   40,   46,  224,   61,   46,
   44,   90,   44,   92,    0,   60,   61,   62,  125,  126,
   44,   58,  259,  260,   61,   59,   44,   59,   44,   44,
   44,   41,   42,   43,   44,   45,  257,   47,    0,  257,
   61,   59,   42,   59,   59,   59,   43,   47,   45,   59,
   60,   61,   62,  257,  123,   41,   42,   43,   44,   45,
   61,   47,    0,  142,  171,  172,  257,  174,  175,   44,
  259,  260,  125,   59,   60,   61,   62,   44,  186,   41,
   42,   43,   44,   45,   59,   47,    0,  259,  260,  223,
   40,  272,  226,  116,  257,   61,   44,   59,   60,   61,
   62,   44,  123,   41,  265,   43,   44,   45,   44,  217,
    0,   59,   41,  221,  257,  125,   59,  257,   41,  122,
  123,   59,   60,   61,   62,  256,  257,   41,   44,   43,
   44,   45,  125,  125,    0,  266,  267,  268,  260,  125,
  179,  269,  270,  271,  260,   59,   60,   61,   62,  278,
  260,   41,   44,   43,   44,   45,  278,  125,    0,  260,
  163,  164,  165,  125,  167,  168,  169,   44,  257,   59,
   60,   61,   62,  257,    0,   41,   41,   43,   44,   45,
   41,   41,    0,   43,   41,   45,   41,  125,  257,  258,
  259,   41,  261,   59,   60,   61,   62,   68,   15,   41,
   63,   43,   44,   45,  257,  258,  259,  260,  261,  278,
   41,  125,   43,  106,   45,  156,   90,   59,   60,   61,
   62,  272,  277,   41,   -1,   43,   44,   45,  273,  274,
  275,  276,  272,   -1,   -1,  125,  257,  258,   -1,   -1,
  261,   59,   60,   61,   62,   -1,  256,  257,  258,  259,
  260,  261,  262,  263,  264,   -1,  266,  267,  268,  125,
   -1,   -1,   -1,  273,  274,  275,  276,   -1,  278,  279,
  256,  257,  258,  259,  260,  261,  262,  263,  264,   61,
  266,  267,  268,  125,   -1,   -1,   -1,  273,  274,  275,
  276,   -1,  278,  279,  256,  257,  258,  259,  260,  261,
  262,  263,  264,   61,  266,  267,  268,  125,   -1,   41,
   -1,  273,  274,  275,  276,   -1,  278,  279,  256,  257,
  258,  259,  260,  261,  262,  263,  264,   -1,  266,  267,
  268,   -1,   -1,   45,   -1,  273,  274,  275,  276,   -1,
  278,  279,  256,  257,  258,  259,  260,  261,  262,  263,
  264,   -1,  266,  267,  268,   -1,   -1,   -1,   -1,  273,
  274,  275,  276,   -1,  278,  279,  256,  257,  258,  259,
  260,  261,  262,  263,  264,   -1,  266,  267,  268,   -1,
   -1,   -1,   -1,  273,  274,  275,  276,   -1,  278,  279,
  256,  257,  258,  259,  260,  261,  262,  263,  264,   -1,
  266,  267,  268,   -1,   -1,   -1,   -1,  273,  274,  275,
  276,   -1,  278,  279,  256,  257,  258,  259,  260,  261,
  262,  263,  264,    0,  266,  267,  268,   61,   -1,   -1,
   -1,  273,  274,  275,  276,   -1,  278,  279,  256,  257,
  258,  259,  260,  261,  262,  263,  264,    0,  266,  267,
  268,  125,   -1,   -1,   -1,  273,  274,  275,  276,   -1,
  278,  279,   -1,   -1,   41,   61,   43,   44,   45,   -1,
   -1,    0,   -1,   61,  123,  257,  258,   -1,   -1,  261,
   -1,   -1,   59,   60,   61,   62,   -1,   -1,   41,  123,
   43,   44,   45,   -1,   -1,    0,  278,   -1,  123,  257,
  258,   -1,   -1,  261,   -1,   -1,   59,   60,   61,   62,
   -1,   -1,   41,   -1,   43,   44,   45,   -1,   -1,    0,
   -1,   -1,   -1,   -1,  256,  257,   -1,   -1,   -1,  125,
   59,   60,   61,   62,  266,  267,  268,  125,   -1,   44,
   -1,   -1,   -1,    0,  256,  257,   -1,   -1,  125,   -1,
   -1,   -1,   -1,   -1,   59,   -1,   61,  269,  270,  271,
   -1,   -1,   -1,   44,   -1,   -1,   -1,    0,   -1,   61,
   -1,   -1,  125,   -1,   -1,   -1,   -1,   -1,   59,   -1,
   61,   -1,  256,  257,   -1,   -1,   -1,   44,  262,  263,
   -1,    0,  266,  267,  268,   -1,  125,   -1,   -1,   -1,
   -1,   -1,   59,   -1,   61,  279,   -1,  256,  257,   -1,
   -1,   44,   -1,  262,  263,    0,   -1,  266,  267,  268,
  125,   -1,   -1,  257,  258,   -1,   59,  261,   61,   -1,
  279,  256,  257,  125,   -1,   44,   -1,  262,  263,   -1,
   -1,  266,  267,  268,  125,   -1,   -1,   -1,   -1,   -1,
   59,   -1,   61,   -1,  279,   -1,   -1,   -1,   -1,   44,
   -1,  257,  258,    0,   -1,  261,   -1,   -1,  125,  257,
  258,   41,   -1,  261,   59,   45,   61,   -1,   45,  256,
  257,  258,  259,  260,  261,  262,  263,  264,   -1,  266,
  267,  268,  125,   60,   61,   62,  273,  274,  275,  276,
   -1,  278,  279,  256,  257,  258,  259,  260,  261,  262,
  263,  264,   -1,  266,  267,  268,  125,   -1,   -1,   -1,
  273,  274,  275,  276,   61,  278,  279,  256,  257,  258,
  259,  260,  261,  262,  263,  264,   -1,  266,  267,  268,
  125,   -1,   -1,   -1,  273,  274,  275,  276,   61,  278,
  279,  256,  257,  258,  259,  260,  261,  262,  263,  264,
   -1,  266,  267,  268,   -1,  257,  258,   61,   41,  261,
   -1,    0,   45,  278,  279,  256,  257,  258,  259,  260,
  261,  262,  263,  264,   -1,  266,  267,  268,  125,   -1,
   42,   43,   -1,   45,   -1,   47,   -1,  278,  279,  256,
  257,  258,  259,  260,  261,  262,  263,  264,   -1,  266,
  267,  268,  125,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  278,  279,  256,  257,  258,  259,  260,  261,  262,
  263,  264,   61,  266,  267,  268,    0,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  278,  279,  256,  257,  258,
  259,  260,  261,  262,  263,  264,   -1,  266,  267,  268,
   42,   43,   -1,   45,   -1,   47,   -1,    0,   -1,  278,
  279,  256,  257,  258,  259,  260,  261,  262,  263,  264,
   44,  266,  267,  268,   -1,   -1,  256,  257,   -1,  256,
  257,    0,   -1,  278,  279,   59,  125,   61,   -1,  269,
  270,  271,  269,  270,  271,   -1,  273,  274,  275,  276,
   -1,   61,   -1,   61,   -1,    0,   -1,   -1,   -1,  256,
  257,  258,  259,  260,  261,  262,  263,  264,   61,  266,
  267,  268,   -1,   -1,    0,   -1,   -1,   -1,   -1,   -1,
   -1,  278,  279,   -1,  257,  258,   -1,   -1,  261,   -1,
   -1,   -1,   61,    0,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  125,  256,  257,  258,  278,   -1,  261,  262,  263,
  264,   -1,  266,  267,  268,  125,   61,  125,   -1,   -1,
    0,   -1,   -1,  256,  257,  279,   -1,   -1,   -1,   -1,
   -1,   -1,  125,   -1,   -1,   61,  269,  270,  271,   -1,
   -1,   -1,   -1,   -1,  256,  257,   -1,    0,   -1,   -1,
   -1,   -1,   -1,   -1,   61,   -1,  125,  269,  270,  271,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  256,  257,  258,
  259,  260,  261,  262,  263,  264,   -1,  266,  267,  268,
  125,   61,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  278,
  279,   -1,   42,   -1,   -1,   45,   -1,   47,   -1,  125,
   -1,   40,   41,   -1,   -1,   -1,   45,   46,   61,   -1,
   -1,   -1,   -1,   -1,  256,  257,   -1,   -1,  125,   58,
   42,   -1,   61,   45,   -1,   47,   -1,  269,  270,  271,
   -1,   -1,  256,  257,  258,  259,  260,  261,  262,  263,
  264,   -1,  266,  267,  268,  125,   -1,  257,  258,  257,
  258,  261,   -1,  261,  278,  279,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  256,  257,  258,  259,  260,  261,  262,
  263,  264,  125,  266,  267,  268,   -1,   -1,   -1,   61,
   -1,   -1,   -1,   -1,   -1,  278,  279,  256,  257,  258,
  259,  260,  261,  262,  263,  264,   -1,  266,  267,  268,
   -1,   -1,   -1,   61,   -1,   -1,   -1,   -1,   -1,  278,
  279,  256,  257,  258,  259,  260,  261,  262,  263,  264,
   -1,  266,  267,  268,   -1,   -1,   -1,   -1,   61,   -1,
  256,  257,  258,  278,  279,  261,  262,  263,  264,   -1,
  266,  267,  268,  125,   -1,   -1,   -1,   -1,   61,  256,
  257,  258,  278,  279,  261,  262,  263,  264,   -1,  266,
  267,  268,   -1,   61,   -1,   -1,   -1,  125,   -1,   -1,
   -1,  278,  279,   -1,   -1,   -1,  256,  257,  258,   -1,
   -1,  261,  262,  263,  264,   -1,  266,  267,  268,   -1,
  123,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  278,  279,
   -1,   -1,    1,  256,  257,  258,  256,  257,  261,  262,
  263,  264,  125,  266,  267,  268,   15,  256,  257,  269,
  270,  271,   -1,    1,   23,  278,  279,   -1,   -1,   -1,
  269,  270,  271,   -1,  256,  257,   -1,   15,   -1,   -1,
   39,   -1,  125,   -1,   -1,   23,   -1,  269,  270,  271,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  125,   -1,   -1,
   -1,   39,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   68,
   -1,   -1,   71,   -1,  256,  257,  258,   -1,   -1,  261,
  262,  263,  264,   -1,  266,  267,  268,   -1,   -1,   -1,
   68,   -1,   -1,   71,  125,   -1,   -1,  279,  256,  257,
  258,   -1,   -1,  261,  262,  263,   -1,   -1,  266,  267,
  268,  110,    1,   -1,   -1,  114,   -1,   -1,  125,   -1,
  278,  279,   -1,  256,  257,  258,   15,   -1,  261,  262,
  263,  130,  110,  266,  267,  268,   -1,   -1,   -1,   -1,
  139,   -1,   31,  256,  257,  258,  279,   -1,  261,  262,
  263,   -1,  130,  266,  267,  268,  155,  156,  256,  257,
  258,  139,   -1,  261,  262,  263,  279,   -1,  266,  267,
  268,   -1,   -1,  256,  257,   -1,   -1,   -1,  156,  262,
  263,  279,   71,  266,  267,  268,   -1,  186,  256,  257,
   -1,   -1,   -1,   -1,  262,  263,  279,   -1,  266,  267,
  268,   90,   -1,   92,   93,   -1,   -1,   -1,  186,  208,
   -1,  279,   -1,   -1,  213,   -1,   -1,   -1,  217,   -1,
   -1,   -1,  221,   -1,  223,  256,  257,  226,   -1,   -1,
  208,  262,  263,   -1,   -1,  266,  267,  268,   29,  217,
   -1,  130,   33,  221,   35,   -1,   -1,   38,  279,  256,
  257,   -1,   -1,  142,  143,  262,  263,  146,   -1,  266,
  267,  268,   -1,  256,  257,   -1,   -1,   -1,   -1,  262,
  263,   -1,  279,  266,  267,  268,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  279,   -1,   -1,   -1,
   -1,   82,   -1,   84,   -1,   -1,   -1,   -1,   -1,   -1,
  189,   -1,   -1,   -1,   -1,   -1,   97,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  108,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  137,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  150,
};
}
final static short YYFINAL=2;
final static short YYMAXTOKEN=279;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'","'.'","'/'",null,null,null,null,null,null,null,null,null,null,"':'","';'",
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
"sentencias : sen_declarativa",
"sentencias : sen_control",
"sentencias : error ','",
"sen_declarativa : tipo list_var ','",
"sen_declarativa : sentencia_check tipo list_var ','",
"sen_declarativa : funcion",
"sen_declarativa : clase",
"sen_declarativa : clase ','",
"sen_declarativa : clase ';'",
"sen_declarativa : funcion ','",
"sen_declarativa : funcion ';'",
"sen_declarativa : sentencia_check tipo list_var",
"sen_declarativa : tipo list_var",
"sentencia_check : CHECK",
"tipo : SHORT",
"tipo : UINT",
"tipo : FLOAT",
"tipo : ID",
"tipo : error",
"CTE : CTE_SHORT",
"CTE : CTE_UINT",
"CTE : CTE_FLOAT",
"CTE : '-' CTE_SHORT",
"CTE : '-' CTE_FLOAT",
"CTE : '-' CTE_UINT",
"CTE : error",
"list_var : list_var ';' ID",
"list_var : ID",
"funcion : header_funcion '{' cuerpo_funcion sen_retorno '}'",
"funcion : header_funcion '{' cuerpo_funcion seleccion_func '}'",
"funcion : header_funcion '{' cuerpo_funcion '}'",
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
"sen_ejecutable : inv_metodo ','",
"sen_ejecutable : inv_funcion ','",
"sen_ejecutable : seleccion ','",
"sen_ejecutable : imprimir ','",
"sen_ejecutable : asignacion ';'",
"sen_ejecutable : inv_metodo ';'",
"sen_ejecutable : inv_funcion ';'",
"sen_ejecutable : seleccion ';'",
"sen_ejecutable : imprimir ';'",
"sen_ejecutable : inv_metodo",
"sen_ejecutable : asignacion",
"sen_ejecutable : inv_funcion",
"sen_ejecutable : seleccion",
"sen_ejecutable : imprimir",
"asignacion : ID '=' exp_aritmetica",
"asignacion : atributo_clase '=' exp_aritmetica",
"asignacion : ID ':' '=' exp_aritmetica",
"asignacion : atributo_clase ':' '=' exp_aritmetica",
"asignacion : '=' exp_aritmetica",
"asignacion : atributo_clase exp_aritmetica",
"inv_funcion : ID '(' exp_aritmetica ')'",
"inv_funcion : ID '(' ')'",
"inv_funcion : ID '(' exp_aritmetica",
"inv_funcion : ID ')'",
"inv_metodo : atributo_clase '(' exp_aritmetica ')'",
"inv_metodo : atributo_clase '(' ')'",
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
"factor : atributo_clase INCREMENT",
"factor : error INCREMENT",
"comparador : NOT_EQUAL",
"comparador : EQUAL",
"comparador : GREATER_EQUAL",
"comparador : LESS_EQUAL",
"comparador : '<'",
"comparador : '>'",
"comparador : '='",
"bloque_sen_ejecutable : sen_ejecutable",
"bloque_sen_ejecutable : '{' sen_ejecutable_r '}'",
"bloque_sen_ejecutable : sen_ejecutable_r '}'",
"bloque_sen_ejecutable : '{' sen_ejecutable_r",
"bloque_sen_ejecutable : '{' '}'",
"bloque_ejecutable_func : sen_retorno",
"bloque_ejecutable_func : '{' sen_ejecutable_r sen_retorno '}'",
"sen_ejecutable_r : sen_ejecutable_r sen_ejecutable",
"sen_ejecutable_r : sen_ejecutable",
"imprimir : PRINT STR_1LN",
"imprimir : PRINT ','",
"sen_control : inicio_while condicion DO '{' sen_ejecutable_r '}'",
"sen_control : inicio_while condicion DO sen_ejecutable",
"sen_control : inicio_while condicion DO '{' '}'",
"inicio_while : WHILE",
"clase : class_header '{' '}'",
"clase : class_header '{' cuerpo_clase '}'",
"clase : class_header '{' cuerpo_clase herencia_clase '}'",
"clase : class_header '{' herencia_clase '}'",
"clase : CLASS ID ','",
"clase : class_header cuerpo_clase '}'",
"class_header : CLASS ID",
"cuerpo_clase : cuerpo_clase sen_declarativa",
"cuerpo_clase : cuerpo_clase herencia_clase sen_declarativa",
"cuerpo_clase : herencia_clase sen_declarativa",
"cuerpo_clase : sen_declarativa",
"herencia_clase : ID ','",
};

//#line 323 "gramatica.y"
public static final String ERROR = "Error";
public static final List<String> errores_lexicos = new ArrayList<>();
public static final List<String> errores_sintacticos = new ArrayList<>();
public static final List<String> errores_semanticos = new ArrayList<>();
public static final List<String> estructuras = new ArrayList<>();
public static final float FLOAT_MIN = 1.17549435E-38f;

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

    if (claseActual != null) // agregarlo a la lista de atributos de la clase} 
        addAttToList(claseActual, ptr);

    if (declarandoInstancia)
        declararAtributos(ptr);
}

public void addAttToList(Integer cls, Integer att) {
    String attList = st.getAttribute(cls, "attList");
    if (attList == null)
        attList = "";
    else
        attList += ",";
    attList += att.toString();
    st.setAttribute(cls, "attList", attList);
}

public void declararAtributos(Integer ptr, Boolean heredando) {
    Integer tipo = Integer.parseInt(st.getAttribute(ptr, "tipo"));
    String attList = st.getAttribute(tipo, "attList");
    // declrar cada uno de los atributos como lexema.attx:ambito    
    if (attList != null) {
        String[] atts_cls = attList.split(","); // 3,4,6,8
        for (String att_cls: atts_cls) {
            String att0 = st.getLexema(Integer.parseInt(att_cls)).split(":")[0]; // var1:global:class1 -> var1
            String[] instance_parts = st.getLexema(ptr).split(":");

            String instance_att = "";
            if (!heredando) instance_att = instance_parts[0] + "."; // c1.
            instance_att += att0; // c1.var1 || var1

            for (int i = 1; i < instance_parts.length; i++)
                instance_att += ":" + instance_parts[i]; // c1.var1:global:main

            if (heredando)
                instance_att += ":" + instance_parts[0]; // var:global:class2

            Integer ptr_ins = st.addEntry(instance_att, ID);
            st.setAttribute(ptr_ins, "tipo", st.getAttribute(Integer.parseInt(att_cls), "tipo"));
            st.setAttribute(ptr_ins, "valid", "1");
            String uso = st.getAttribute(Integer.parseInt(att_cls), "uso");
            st.setAttribute(ptr_ins, "uso", uso);
            // si es funcion entonces indicar donde se implementa
            // como asi tambien la var self
            if (uso.equals("FUNCTION")) {
                String impl_cls = st.getAttribute(Integer.parseInt(att_cls), "impl");
                if (impl_cls == null)
                    impl_cls = att_cls;
                st.setAttribute(ptr_ins, "impl", impl_cls);
                st.setAttribute(ptr_ins, "self", ptr.toString());
                String parameter = st.getAttribute(Integer.parseInt(att_cls), "parameter");
                if (parameter != null)
                    st.setAttribute(ptr_ins, "parameter", parameter);
            }

            if (st.getAttribute(Integer.parseInt(att_cls), "check_rhs") != null) {
                st.setAttribute(ptr_ins, "check_rhs", "false");
                st.setAttribute(ptr_ins, "check_lhs", "false");
            }
            // si es una instancia, hay que declarar los atributos de la clase
            if (isInstancia(ptr_ins))
                declararAtributos(ptr_ins);

            // si esta heredando se lo agregamos a los attlist
            if (heredando)
                addAttToList(ptr, ptr_ins);
        }
    }
}

public void declararAtributos(Integer ptr) {
    declararAtributos(ptr, false);
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
    declararAtributos(claseActual, true);

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
    // si implementa una FDCLASS, borramos esta entrada
    if (desc.equals("CLASS")) {
        ptr = st.getPtr(st.getLexema(id), "FDCLASS");
        if (ptr != 0) {
            //agregarEstructura("Implementacion de FDCLASS: " + st.getLexema(id));
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
    String lexema = st.getLexema(ptr_lhs);
    if (lexema == null) return 0;
    if (!lexema.contains("."))
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
    Integer ptr = st.getPtr(st.getLexema(id), ambitoActual.copy(), "FUNCTION");

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
        crearTerceto("=", Integer.parseInt(st.getAttribute(ptr, "parameter")), param.ival, "st", "st");
    }
    else if (param != null) {
        agregarError(errores_semanticos, Parser.ERROR,
                String.format(ERROR_PARAMETRO, st.getLexema(ptr), "void"));
        return;
    }
    crearTerceto("CALL",ptr, -1,"","");

    if (param != null) 
        crearTerceto("=", param.ival, Integer.parseInt(st.getAttribute(ptr, "parameter")), "st", "st");
}

public void invocacionFuncion(Integer id) {
    invocacionFuncion(id, null);
}

public void invocacionMetodo(Integer ptr, ParserVal param) {
    if (param != null && param.ival == 0)
        return;

    String param_formal = st.getAttribute(ptr, "parameter");
    if (param_formal != null) {
        String param_tipo = st.getAttribute(Integer.parseInt(param_formal), "tipo");
        if (param == null || Integer.parseInt(param_tipo) != (int)param.dval) {
            agregarError(errores_semanticos, Parser.ERROR,
                    String.format(ERROR_PARAMETRO, st.getLexema(ptr), param_tipo));
            return;
        }
        crearTerceto("=", Integer.parseInt(param_formal), param.ival, "st", "st");
    }
    else if (param != null) {
        agregarError(errores_semanticos, Parser.ERROR,
                String.format(ERROR_PARAMETRO, st.getLexema(ptr), "void"));
        return;
    }
    // ptr -> cls4.fun1:global:main
    // tiene att impl==null si implementa la funcion sino impl apunta a la funcion
    Integer impl = Integer.parseInt(st.getAttribute(ptr, "impl"));
    copiaAtributos(ptr, impl, true);

    crearTerceto("CALL",impl, -1,"","");

    if (param != null) 
        crearTerceto("=", param.ival, Integer.parseInt(param_formal), "st", "st");

    copiaAtributos(ptr, impl, false);
}

public void invocacionMetodo(Integer id) {
    invocacionMetodo(id, null);
}

public void copiaAtributos(Integer met, Integer impl, Boolean copiaValor) {
    Integer cls = Integer.parseInt(st.getAttribute(impl, "cls"));
    // met tiene att self = cls4:global:main
    // self tiene tipo = global:main:class4 que a su vez tiene attList
    // copiar creando tercetos los att de self a impl
    // cls 12 self 21
    Integer self = Integer.parseInt(st.getAttribute(met, "self"));
    String[] atts = st.getAttribute(cls, "attList").split(",");
    for (String at : atts) {
        if (!st.getAttribute(Integer.parseInt(at), "uso").equals("identificador"))
            continue;
        if (isInstancia(Integer.parseInt(at))){
            String carry= "";
            String[] ambito = st.getLexema(Integer.parseInt(at)).split(":");
            for (int i = 1; i < ambito.length; i++)
                carry += ":" + ambito[i];
            copiaAtributosInstancia(self, Integer.parseInt(at),carry, copiaValor);
            continue;
        }
        // at -> var1:global:class1 -> var1
        // self -> cls4:global:main -> cls4, global:main
        // met -> cls4.fun1:global:main -> cls4, global:main
        // result -> cls4.var1:global:main
        
        String[] self_parts = st.getLexema(self).split(":");
        // quitarle el ambito a at
        String at_lex = st.getLexema(Integer.parseInt(at)).split(":")[0];
        String result = self_parts[0] + "." + at_lex;
        for (int i = 1; i < self_parts.length; i++)
            result += ":" + self_parts[i];

        Integer ptr = st.getPtr(result);
        // copiar ptr en at
        if (copiaValor)
            crearTerceto("=", Integer.parseInt(at), ptr, "st", "st");
        else
            crearTerceto("=", ptr,Integer.parseInt(at), "st", "st");
    }
}

public void copiaAtributosInstancia(Integer self, Integer attribute,String carry, Boolean copiaValor){
    Integer cls = Integer.parseInt(st.getAttribute(attribute, "tipo"));
    String[] atts = st.getAttribute(cls, "attList").split(",");
    for (String at : atts) {
        if (!st.getAttribute(Integer.parseInt(at), "uso").equals("identificador"))
            continue;
        if (isInstancia(Integer.parseInt(at))){
            String[] ambito = carry.split(":");
            String aux;
            if (carry.contains("."))
                aux = ambito[0] + st.getLexema(Integer.parseInt(at)).split(":")[0] + ".";
            else
                aux = st.getLexema(Integer.parseInt(at)).split(":")[0] + ".";
            for (int i = 1; i < ambito.length; i++)
                aux += ":" + ambito[i];
            copiaAtributosInstancia(self, Integer.parseInt(at),aux, copiaValor);
            continue;
        }
        // at -> var1:global:class1 -> var1
        // self -> cls4:global:main -> cls4, global:main
        // met -> cls4.fun1:global:main -> cls4, global:main
        // result -> cls4.var1:global:main
        String[]carry_parts = carry.split(":");
        String at_lex = carry_parts[0]+st.getLexema(attribute).split(":")[0] +"." +st.getLexema(Integer.parseInt(at)).split(":")[0];
        String[] self_parts = st.getLexema(self).split(":");
        // quitarle el ambito a at
        String result = self_parts[0] + "." + at_lex;
        for (int i = 1; i < self_parts.length; i++)
            result += ":" + self_parts[i];
        for (int i = 1; i < carry_parts.length; i++)
            at_lex += ":" + carry_parts[i];
        System.out.println("Result: "+ result + " At_lex: "+ at_lex);
        Integer ptr = st.getPtr(result);
        Integer atr = st.getPtr(at_lex);
        // copiar ptr en at
        if (copiaValor)
            crearTerceto("=", atr, ptr, "st", "st");
        else
            crearTerceto("=", ptr,atr, "st", "st");
    }
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

    if (i != 0) {
        agregarError(errores_semanticos, Parser.ERROR,
                String.format(ERROR_REDECLARACION, st.getLexema(id).split("@")[0], ambitoActual.toString()));
        return;
    }

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

    if (claseActual != null) {
        // es un metodo, agregarlo a la lista de metodos de la clase
        addAttToList(claseActual, id);
        st.setAttribute(id, "cls", claseActual.toString()); // agregar el ptr a la clase
    }

    //agregarEstructura("FUNCTION :" + st.getLexema(id));
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
                if (check_rhs.equals("true")){
                    System.out.println("LOG: " + String.format(ESTRUCTURA_CHECK,st.getLexema(ptr)));
                    Integer aux = st.addEntry("CHECK "+st.getLexema(ptr)+" CUMPLIDO", STR_1LN);
                    st.setAttribute(aux, "valid", "1");
                    st.setAttribute(aux, "tipo", ""+STR_1LN);
                    Terceto terceto = new Terceto("PRINT", aux, -1, false, false);
                    pilaTercetosCHECK.apilar(terceto);
                }
            }
        } else // si es false(primer uso del lhs) le seteo ambito actual
            st.setAttribute(ptr, "check_lhs", ambitoActual.toString());
    }
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
      if (check_lhs.equals("true")){
        System.out.println("LOG: " + String.format(ESTRUCTURA_CHECK,st.getLexema(id)));
        Integer aux = st.addEntry("CHECK "+st.getLexema(id)+" CUMPLIDO", STR_1LN);
        st.setAttribute(aux, "valid", "1");
        st.setAttribute(aux, "tipo", ""+STR_1LN);
        Terceto terceto = new Terceto("PRINT", aux, -1, false, false);
        pilaTercetosCHECK.apilar(terceto);
      }
    }

    Integer ptr_aux = crearTerceto("+", ptr, newEntryCte(tipo), "st", "st");
    crearTerceto("=", ptr, ptr_aux, "st", "terceto");
    val.ival = ptr_aux;
    val.dval = tipo;
    val.sval = "terceto";
    return val;
}

public Integer newEntryCte(Integer tipo) {
    Integer ptr;
    if (tipo == FLOAT)
        ptr = st.addEntry("1.0", tipo);
    else
        ptr = st.addEntry("1", tipo);

    st.setAttribute(ptr, "uso", "cte");
    st.setAttribute(ptr, "valid", "1");
    st.setAttribute(ptr, "tipo", ""+tipo);
    return ptr;
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
            return "!!";
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
    Terceto terceto;
    PilaTercetos aux = new PilaTercetos();
    Terceto bi = pilaTercetos.pop();

    while (pilaTercetos.getContador() > 0) {
        terceto = pilaTercetos.pop();
        if (terceto.getOperador().equals("START_WHILE"+countWHILE)) {
            bi.setOperando1(pilaTercetos.getContador()+1, true);
            pilaTercetos.apilar(terceto);
            break;
        }
        aux.apilar(terceto);
    }

    while (aux.getContador() > 0) {
        pilaTercetos.apilar(aux.pop());
    }
    pilaTercetos.apilar(bi);
}

LexicalAnalyzer lexicalAnalyzer;
SymbolTable st;
PilaTercetos pilaTercetos, pilaTercetosCHECK;
Integer tipo, claseActual;
int countIF, countWHILE = 0;
Ambito ambitoActual = new Ambito("global");
boolean check, declarandoInstancia = false;

public static void main(String[] args) {
    if (args.length != 1) {
        System.out.println("Uso: java Parser <archivo>");
        return;
    }
    TransitionMatrix<Integer> mI = new TransitionMatrix<>(19, 28);
    TransitionMatrix<AccionSemantica> mA = new TransitionMatrix<>(19, 28);
    SymbolTable sttemp = new SymbolTable();

    FuncionesAuxiliares.loadMatrixs(mI, mA, "test.csv", sttemp, errores_lexicos);
    Parser parser = new Parser(new LexicalAnalyzer("./tests/" + args[0] + ".txt", mI, mA, errores_lexicos), sttemp);
    parser.run();
    
    Parser.imprimirErrores(errores_lexicos, "Errores Lexicos");
    Parser.imprimirErrores(errores_sintacticos, "Errores Sintacticos");
    Parser.imprimirErrores(estructuras, "Estructuras Sintacticas");
    Parser.imprimirErrores(errores_semanticos, "Errores Semanticos");
    
    parser.st.print();
    while (parser.pilaTercetosCHECK.getContador() > 0)
        parser.pilaTercetos.apilar(parser.pilaTercetosCHECK.pop());
    parser.pilaTercetos.print();

    if (errores_lexicos.isEmpty() && errores_sintacticos.isEmpty() && errores_semanticos.isEmpty()) {
        AssemblyGenerator asm = new AssemblyGenerator(parser.pilaTercetos, parser.st, "output.asm");
        try {
            asm.generarAssembler();
            System.out.println("Assembler generado con exito");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

public Parser(LexicalAnalyzer lexicalAnalyzer, SymbolTable st) {
    this.lexicalAnalyzer = lexicalAnalyzer;
    this.st = st;
    this.pilaTercetos = new PilaTercetos();
    this.pilaTercetosCHECK = new PilaTercetos();
    this.declarandoInstancia = false;
    //yydebug = true;
}
//#line 1588 "Parser.java"
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
case 14:
//#line 39 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Las clases no se declaran con ,");}
break;
case 15:
//#line 40 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Las clases no se declaran con ;");}
break;
case 16:
//#line 41 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Las funciones no se declaran con ,");}
break;
case 17:
//#line 42 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Las funciones no se declaran con ;");}
break;
case 18:
//#line 43 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una ,");}
break;
case 19:
//#line 44 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una ,");}
break;
case 20:
//#line 47 "gramatica.y"
{ check=true; }
break;
case 21:
//#line 50 "gramatica.y"
{ yyval.ival = SHORT; tipo = (int)SHORT; }
break;
case 22:
//#line 51 "gramatica.y"
{ yyval.ival = UINT; tipo = (int)UINT; }
break;
case 23:
//#line 52 "gramatica.y"
{ yyval.ival = FLOAT; tipo = (int)FLOAT; }
break;
case 24:
//#line 53 "gramatica.y"
{ yyval.ival = getTipoClase(val_peek(0).ival, ambitoActual.copy()); tipo = yyval.ival; declarandoInstancia = true; }
break;
case 25:
//#line 54 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "El tipo ingresado no es valido");}
break;
case 26:
//#line 57 "gramatica.y"
{verificarRango(val_peek(0).ival); yyval.ival = val_peek(0).ival; yyval.dval = SHORT;}
break;
case 27:
//#line 58 "gramatica.y"
{yyval.ival = val_peek(0).ival; yyval.dval = UINT;}
break;
case 28:
//#line 59 "gramatica.y"
{verificarRango(val_peek(0).ival); yyval.ival = val_peek(0).ival; yyval.dval = FLOAT;}
break;
case 29:
//#line 60 "gramatica.y"
{resolverSigno(val_peek(0).ival); verificarRango(val_peek(0).ival); yyval.ival = val_peek(0).ival; yyval.dval = SHORT; }
break;
case 30:
//#line 61 "gramatica.y"
{resolverSigno(val_peek(0).ival); verificarRango(val_peek(0).ival); yyval.ival = val_peek(0).ival; yyval.dval = FLOAT; }
break;
case 31:
//#line 62 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "No se puede negar un unsigned int");}
break;
case 32:
//#line 63 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "La constante ingresada no es valida"); }
break;
case 33:
//#line 66 "gramatica.y"
{ declararVariable(val_peek(0).ival); }
break;
case 34:
//#line 67 "gramatica.y"
{ declararVariable(val_peek(0).ival); }
break;
case 35:
//#line 70 "gramatica.y"
{ ambitoActual.pop(); crearTerceto("RET", val_peek(4).ival, -1, null, null); }
break;
case 36:
//#line 71 "gramatica.y"
{ ambitoActual.pop(); crearTerceto("RET", val_peek(4).ival, -1, null, null); }
break;
case 37:
//#line 72 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "La funcion debe contener la sentencia RETURN,"); }
break;
case 38:
//#line 73 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al comienzo de la función");}
break;
case 39:
//#line 76 "gramatica.y"
{  agregarFuncion(val_peek(3).ival, VOID, val_peek(1).ival);
                                                    yyval.ival = val_peek(3).ival;
                                                    if (val_peek(1).ival != 0)
                                                        st.setLexema(val_peek(1).ival, st.getLexema(val_peek(1).ival) + ":" + ambitoActual.toString());
                                                 }
break;
case 40:
//#line 81 "gramatica.y"
{ agregarFuncion(val_peek(2).ival, VOID, null); yyval.ival = val_peek(2).ival;}
break;
case 41:
//#line 84 "gramatica.y"
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
case 52:
//#line 110 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una ,");}
break;
case 53:
//#line 111 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una ,");}
break;
case 54:
//#line 112 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una ,");}
break;
case 55:
//#line 113 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una ,");}
break;
case 56:
//#line 114 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una ,");}
break;
case 57:
//#line 115 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una ,");}
break;
case 58:
//#line 116 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una ,");}
break;
case 59:
//#line 117 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una ,");}
break;
case 60:
//#line 118 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una ,");}
break;
case 61:
//#line 119 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una ,");}
break;
case 62:
//#line 122 "gramatica.y"
{ yyval.ival = crearTercetoAsignacion(val_peek(2).ival, val_peek(0)); yyval.sval = "terceto"; if (yyval.ival != 0) yyval.dval = val_peek(2).dval; }
break;
case 63:
//#line 123 "gramatica.y"
{ yyval.ival = crearTercetoAsignacion(val_peek(2).ival, val_peek(0)); yyval.sval = "terceto"; if (yyval.ival != 0) yyval.dval = val_peek(2).dval; }
break;
case 64:
//#line 124 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "La asignacion debe ser unicamente con el ="); }
break;
case 65:
//#line 125 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "La asignacion debe ser unicamente con el ="); }
break;
case 66:
//#line 126 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un identificador");}
break;
case 67:
//#line 127 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un signo igual");}
break;
case 68:
//#line 130 "gramatica.y"
{ invocacionFuncion(val_peek(3).ival, val_peek(1)); }
break;
case 69:
//#line 131 "gramatica.y"
{ invocacionFuncion(val_peek(2).ival); }
break;
case 70:
//#line 132 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
break;
case 71:
//#line 133 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
break;
case 72:
//#line 136 "gramatica.y"
{ if (val_peek(3).ival != 0) invocacionMetodo(val_peek(3).ival, val_peek(1)); }
break;
case 73:
//#line 137 "gramatica.y"
{ if (val_peek(2).ival != 0) invocacionMetodo(val_peek(2).ival); }
break;
case 74:
//#line 138 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba el cierre entre parentesis al final de la invocacion");}
break;
case 75:
//#line 141 "gramatica.y"
{ yyval.ival = agregarAtributo(val_peek(2).ival, val_peek(0).ival, ambitoActual.copy()); }
break;
case 76:
//#line 142 "gramatica.y"
{ yyval.ival = agregarAtributo(val_peek(2).ival, val_peek(0).ival, ambitoActual.copy()); }
break;
case 79:
//#line 147 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un bloque de sentencias ejecutables");}
break;
case 80:
//#line 148 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un bloque de sentencias ejecutables");}
break;
case 81:
//#line 151 "gramatica.y"
{ completarB("BI", pilaTercetos.getContador()); }
break;
case 82:
//#line 152 "gramatica.y"
{ completarB("BI", pilaTercetos.getContador()+1);
                                                                          crearTerceto("END_IF"+countIF++, -1, -1, "", ""); }
break;
case 83:
//#line 156 "gramatica.y"
{ crearTerceto("BI", -1, -1, "", "");
                                               completarB("BF", pilaTercetos.getContador()+1);
                                               crearTerceto("END_IF"+countIF++, -1, -1, "", ""); }
break;
case 85:
//#line 164 "gramatica.y"
{ yyval.ival = crearTerceto("BF", val_peek(1).ival, -1, val_peek(1).sval, ""); }
break;
case 86:
//#line 167 "gramatica.y"
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
case 87:
//#line 177 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una expresión aritmética");}
break;
case 88:
//#line 178 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una expresión aritmética");}
break;
case 89:
//#line 179 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un comparador");}
break;
case 90:
//#line 180 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba dos expresión aritmética");}
break;
case 91:
//#line 185 "gramatica.y"
{ yyval.ival = crearTercetoExp(val_peek(2), val_peek(0), "+"); yyval.sval = "terceto"; if (yyval.ival != 0) yyval.dval = val_peek(2).dval; }
break;
case 92:
//#line 186 "gramatica.y"
{ yyval.ival = crearTercetoExp(val_peek(2), val_peek(0), "-"); yyval.sval = "terceto"; if (yyval.ival != 0) yyval.dval = val_peek(2).dval; }
break;
case 93:
//#line 187 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 94:
//#line 188 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
break;
case 95:
//#line 189 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
break;
case 96:
//#line 190 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
break;
case 97:
//#line 191 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
break;
case 98:
//#line 192 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
break;
case 99:
//#line 193 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
break;
case 100:
//#line 196 "gramatica.y"
{ yyval.ival = crearTercetoTermino(val_peek(2), val_peek(0), "*"); yyval.sval = "terceto"; if (yyval.ival != 0) yyval.dval = val_peek(2).dval; }
break;
case 101:
//#line 197 "gramatica.y"
{ yyval.ival = crearTercetoTermino(val_peek(2), val_peek(0), "/"); yyval.sval = "terceto"; if (yyval.ival != 0) yyval.dval = val_peek(2).dval; }
break;
case 102:
//#line 198 "gramatica.y"
{ yyval = val_peek(0); }
break;
case 103:
//#line 199 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
break;
case 104:
//#line 200 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
break;
case 105:
//#line 201 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
break;
case 106:
//#line 202 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Error dos operadores juntos");}
break;
case 107:
//#line 206 "gramatica.y"
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
                                                        if (check_lhs.equals("true")){
                                                            System.out.println("LOG: " + String.format(ESTRUCTURA_CHECK,st.getLexema(yyval.ival)));
                                                            Integer aux = st.addEntry("CHECK "+st.getLexema(yyval.ival)+" CUMPLIDO", STR_1LN);
                                                            st.setAttribute(aux, "valid", "1");
                                                            st.setAttribute(aux, "tipo", ""+STR_1LN);
                                                            Terceto terceto = new Terceto("PRINT", aux, -1, false, false);
                                                            pilaTercetosCHECK.apilar(terceto);
                                                        }
                                                    }
                                                    yyval.sval = "st";
                                                    yyval.dval = Integer.parseInt(st.getAttribute(yyval.ival, "tipo")); /*chequear*/
                                                }
break;
case 108:
//#line 228 "gramatica.y"
{ yyval = crearTercetoIncrement(val_peek(1).ival); }
break;
case 109:
//#line 229 "gramatica.y"
{ yyval.ival = val_peek(0).ival; yyval.sval = "st"; yyval.dval = val_peek(0).dval; }
break;
case 110:
//#line 230 "gramatica.y"
{   yyval.ival = st.getPtr(st.getLexema(val_peek(0).ival), ambitoActual.copy());
                                                    if (yyval.ival != 0) {
                                                        yyval.sval = "st";
                                                        yyval.dval = Integer.parseInt(st.getAttribute(yyval.ival, "tipo"));
                                                        String check_lhs = st.getAttribute(yyval.ival,"check_lhs");
                                                        String check_rhs = st.getAttribute(yyval.ival,"check_rhs");
                                                        if (check_rhs != null && check_rhs.equals("false")){
                                                            st.setAttribute(yyval.ival, "check_rhs", "true");
                                                            if (check_lhs.equals("true")){
                                                                System.out.println("LOG: " + String.format(ESTRUCTURA_CHECK,st.getLexema(yyval.ival)));
                                                                Integer aux = st.addEntry("CHECK "+st.getLexema(yyval.ival)+" CUMPLIDO", STR_1LN);
                                                                st.setAttribute(aux, "valid", "1");
                                                                st.setAttribute(aux, "tipo", ""+STR_1LN);
                                                                Terceto terceto = new Terceto("PRINT", aux, -1, false, false);
                                                                pilaTercetosCHECK.apilar(terceto);
                                                            }
                                                        }
                                                    }
                                                }
break;
case 111:
//#line 249 "gramatica.y"
{ yyval = crearTercetoIncrement(val_peek(1).ival); }
break;
case 112:
//#line 250 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Variable o constante invalida");}
break;
case 113:
//#line 253 "gramatica.y"
{ yyval.ival = NOT_EQUAL; }
break;
case 114:
//#line 254 "gramatica.y"
{ yyval.ival = EQUAL; }
break;
case 115:
//#line 255 "gramatica.y"
{ yyval.ival = GREATER_EQUAL; }
break;
case 116:
//#line 256 "gramatica.y"
{ yyval.ival = LESS_EQUAL; }
break;
case 117:
//#line 257 "gramatica.y"
{ yyval.ival = 60; }
break;
case 118:
//#line 258 "gramatica.y"
{ yyval.ival = 62; }
break;
case 119:
//#line 259 "gramatica.y"
{ yyval.ival = EQUAL;
                                        agregarError(errores_sintacticos, Parser.ERROR, "Error en el comparador igual, se esperaba ==");}
break;
case 122:
//#line 266 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al comienzo del bloque de sentencias ejecutables");}
break;
case 123:
//#line 267 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al final del bloque de sentencias ejecutables");}
break;
case 124:
//#line 268 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera un bloque de sentencias ejecutables");}
break;
case 129:
//#line 279 "gramatica.y"
{ crearTerceto("PRINT", val_peek(0).ival, -1, "st", null);
                                                   st.setAttribute(val_peek(0).ival, "valid", "1"); }
break;
case 130:
//#line 281 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una cadena para imprimir");}
break;
case 131:
//#line 284 "gramatica.y"
{   crearTerceto("BI", -1, -1, "", "");
                                                                                completarB("BF", pilaTercetos.getContador()+1);
                                                                                completarWhile(); 
                                                                                crearTerceto("END_WHILE"+countWHILE++, -1, -1, "", "");
                                                                            }
break;
case 132:
//#line 289 "gramatica.y"
{   crearTerceto("BI", -1, -1, "", "");
                                                                                completarB("BF", pilaTercetos.getContador()+1);
                                                                                completarWhile(); 
                                                                                crearTerceto("END_WHILE"+countWHILE++, -1, -1, "", "");
                                                                            }
break;
case 133:
//#line 294 "gramatica.y"
{  agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una o mas sentencias ejecutables"); }
break;
case 134:
//#line 297 "gramatica.y"
{ crearTerceto("START_WHILE"+countWHILE, -1, -1, "", ""); }
break;
case 135:
//#line 300 "gramatica.y"
{ ambitoActual.pop(); claseActual = null; }
break;
case 136:
//#line 301 "gramatica.y"
{ ambitoActual.pop(); claseActual = null; }
break;
case 137:
//#line 302 "gramatica.y"
{ ambitoActual.pop(); claseActual = null; }
break;
case 138:
//#line 303 "gramatica.y"
{ ambitoActual.pop(); claseActual = null; }
break;
case 139:
//#line 304 "gramatica.y"
{ agregarClase(val_peek(1).ival, "FDCLASS"); ambitoActual.pop(); claseActual = null; }
break;
case 140:
//#line 305 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba una llave al final de la clase");
                                                             ambitoActual.pop(); }
break;
case 141:
//#line 309 "gramatica.y"
{ agregarClase(val_peek(0).ival, "CLASS"); }
break;
case 143:
//#line 313 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "La declaracion de herencia debe ser la ultima sentencia de la clase");}
break;
case 144:
//#line 314 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "La declaracion de herencia debe ser la ultima sentencia de la clase");}
break;
case 146:
//#line 319 "gramatica.y"
{ heredar(val_peek(1).ival); }
break;
//#line 2275 "Parser.java"
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
