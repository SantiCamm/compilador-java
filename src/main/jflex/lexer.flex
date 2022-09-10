package lyc.compiler;

import java_cup.runtime.Symbol;
import lyc.compiler.ParserSym;
import lyc.compiler.model.*;
import static lyc.compiler.constants.Constants.*;

%%

%public
%class Lexer
%unicode
%cup
%line
%column
%throws CompilerException
%eofval{
  return symbol(ParserSym.EOF);
%eofval}


%{
  private Symbol symbol(int type) {
    return new Symbol(type, yyline, yycolumn);
  }
  private Symbol symbol(int type, Object value) {
    return new Symbol(type, yyline, yycolumn, value);
  }
  StringBuffer sb;
%}


LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
Identation =  [ \t\f]

Init = "init"

Int = "Int"
Float = "Float"
String = "String"

If = "if"
While = "while"
Else = "else"

Write = "write"
Read = "read"

Plus = "+"
Mult = "*"
Sub = "-"
Div = "/"
Assig = "="
Rest = "%"

Mayor = ">"
Lower = "<"
MayorI = ">="
LowerI = "<="
Equal = "=="
NotEqual = "!="

AndCond = "&&"
OrCond = "||"
NotCond = "!"

OpenBracket = "("
CloseBracket = ")"
OpenCurlyBrace = "{"
CloseCurlyBrace = "}"

Comma = ","
SemiColon = ";"
Dot = "."
DoubleDot = ":"

Letter = [a-zA-Z]
Digit = [0-9]
Digit19 = [1-9]
InvalidCharacter = [^a-zA-z0-9<>:,@/\%\+\*\-\.\[\];\(\)=?!]

TraditionalComment   = "/*" [^*] ~"*/" | "/*" "*"+ "/"
EndOfLineComment     = "//" {InputCharacter}* {LineTerminator}?
DocumentationComment = "/**" {CommentContent} "*"+ "/"
CommentContent       = ( [^*] | \*+ [^/*] )*
Comment = {TraditionalComment} | {EndOfLineComment} | {DocumentationComment}

WhiteSpace = {LineTerminator} | {Identation}

Identifier = {Letter} ({Letter}|{Digit}|_)*

//IntegerConstant = {Digit}+ | {Digit19}+{Digit}+
IntegerConstant = {Digit}+
InvalidIntegerConstant = 0+{Digit19}+
//FloatConstant = (({Digit}|{Digit19}{Digit}+)\.{Digit}+) | \.{Digit}+
FloatConstant = (({Digit}|{Digit19}{Digit}+)?\.{Digit}+) //otra opcion
StringConstant = \"(([^\"\n]*)\")
%%


/* keywords */

<YYINITIAL> {
  /* Declaration */
  {Init}                                    { return symbol(ParserSym.INIT); }

  /* Logical */
  {If}                                     { return symbol(ParserSym.IF); }
  {Else}                                   { return symbol(ParserSym.ELSE); }
  {While}                                  { return symbol(ParserSym.WHILE); }


  /* Data types */
  {Int}                                     { return symbol(ParserSym.INT); }
  {Float}                                   { return symbol(ParserSym.FLOAT); }
  {String}                                  { return symbol(ParserSym.STRING); }


  /* I/O */
  {Write}                                  { return symbol(ParserSym.WRITE); }
  {Read}                                   { return symbol(ParserSym.READ); }

  /* Identifiers */
  {Identifier}                             { return symbol(ParserSym.IDENTIFIER, yytext()); }

  /* Constants */
  {IntegerConstant}                        {
                                                if(yytext().length() > 5 ) {
                                                    throw new InvalidIntegerException(yytext());
                                                }
                                                else if(Integer.valueOf(yytext()) > 65535)
                                                {
                                                    throw new InvalidIntegerException(yytext());
                                                }
                                                 else {
                                                     return symbol(ParserSym.INTEGER_CONSTANT, yytext());
                                                 }
                                            }

  {FloatConstant}                          {
                                                String[] num = yytext().split("\\.");
                                                String exp = num[0];
                                                String mantissa = num[1];

                                                if(exp.length() > 0)
                                                    {
                                                       if(exp.length() > 3 || Integer.parseInt(exp) > 256 )
                                                           throw new InvalidFloatException("Exponent out of range");
                                                    }

                                                if(mantissa.length() > 0) {
                                                  if(mantissa.length() > 8 || Integer.parseInt(mantissa) > 16777216)
                                                      throw new InvalidFloatException("Mantissa out of range");
                                                }

                                                return symbol(ParserSym.FLOAT_CONSTANT, yytext());
                                            }

  {StringConstant}                         {
                                                sb = new StringBuffer(yytext());
                                                if(sb.length() > 42) //quotes add 2 to max length
                                                    throw new InvalidLengthException(yytext());
                                                else
                                                    return symbol(ParserSym.STRING_CONSTANT, yytext());
                                            }

  /* Operators */
  {Plus}                                    { return symbol(ParserSym.PLUS); }
  {Sub}                                     { return symbol(ParserSym.SUB); }
  {Mult}                                    { return symbol(ParserSym.MULT); }
  {Div}                                     { return symbol(ParserSym.DIV); }
  {Rest}                                    { return symbol(ParserSym.REST); }
  {Assig}                                   { return symbol(ParserSym.ASSIG); }
  {OpenBracket}                             { return symbol(ParserSym.OPEN_BRACKET); }
  {CloseBracket}                            { return symbol(ParserSym.CLOSE_BRACKET); }
  {OpenCurlyBrace}                          { return symbol(ParserSym.OPEN_CURLY_BRACKET); }
  {CloseCurlyBrace}                         { return symbol(ParserSym.CLOSE_CURLY_BRACKET); }

   /* Comparators */
   {Mayor}                                  { return symbol(ParserSym.MAYOR); }
   {Lower}                                  { return symbol(ParserSym.LOWER); }
   {MayorI}                                 { return symbol(ParserSym.MAYOR_I); }
   {LowerI}                                 { return symbol(ParserSym.LOWER_I); }
   {Equal}                                  { return symbol(ParserSym.EQUAL); }
   {NotEqual}                               { return symbol(ParserSym.NOT_EQUAL); }

   /* Conditionals */
   {AndCond}                                { return symbol(ParserSym.AND_COND); }
   {OrCond}                                 { return symbol(ParserSym.OR_COND); }
   {NotCond}                                { return symbol(ParserSym.NOT_COND); }

   /* Misc */
   {Comma}                                  { return symbol(ParserSym.COMMA); }
   {SemiColon}                              { return symbol(ParserSym.SEMI_COLON); }
   {Dot}                                    { return symbol(ParserSym.DOT); }
   {DoubleDot}                              { return symbol(ParserSym.DOUBLE_DOT); }

   /* Whitespace */
   {WhiteSpace}                             { /* ignore */ }
}

   /* Comments */
   {Comment}                                { /* ignore */ }

   /* Error fallback */
   ^[]                                      { throw new UnknownCharacterException(yytext()); }
   {InvalidCharacter}                       { throw new UnknownCharacterException(yytext()); }
