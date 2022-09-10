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
<<<<<<< Updated upstream
InvalidCharacter = [^a-zA-z0-9<>:,@\%\+\*\-\.\[\];\(\)=?!]
=======
InvalidCharacter = [^a-zA-z0-9<>:,@/\%\+\*\-\.\[\];\(\)=?!]
>>>>>>> Stashed changes

TraditionalComment   = "/*" [^*] ~"*/" | "/*" "*"+ "/"
EndOfLineComment     = "//" {InputCharacter}* {LineTerminator}?
DocumentationComment = "/**" {CommentContent} "*"+ "/"
CommentContent       = ( [^*] | \*+ [^/*] )*
Comment = {TraditionalComment} | {EndOfLineComment} | {DocumentationComment}

WhiteSpace = {LineTerminator} | {Identation}

<<<<<<< Updated upstream
Identifier = {Letter} ({Letter}|{Digit})*
=======
Identifier = {Letter} ({Letter}|{Digit}|_)*
>>>>>>> Stashed changes

//IntegerConstant = {Digit}+ | {Digit19}+{Digit}+
IntegerConstant = {Digit}+
InvalidIntegerConstant = 0+{Digit19}+
<<<<<<< Updated upstream
FloatConstant = {Digit}+\.{Digit}+ | \.{Digit}+
=======
//FloatConstant = (({Digit}|{Digit19}{Digit}+)\.{Digit}+) | \.{Digit}+
FloatConstant = (({Digit}|{Digit19}{Digit}+)? \. {Digit}+) //otra opcion
>>>>>>> Stashed changes
StringConstant = \"(([^\"\n]*)\")

Init = "init"

Int = "int"
Float = "float"
String = "string"

If = "if"
While = "while"
Else = "else"

Write = "write"
Read = "read"
%%


/* keywords */

<YYINITIAL> {
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

<<<<<<< Updated upstream
  {FloatConstant}                          { return symbol(ParserSym.FLOAT_CONSTANT, yytext()); }
  {StringConstant}                         {
                                                sb = new StringBuffer(yytext());
                                                if(sb.length() > 41)
=======
  {FloatConstant}                          {
                                                String[] num = yytext().split("\\.");
                                                String exp = num[0];
                                                String mantissa = num[1];

                                                if(exp.length() > 3 || Integer.parseInt(exp) > 256 )
                                                    throw new InvalidFloatException("Exponent out of range");

                                                if(mantissa.length() > 8 || Integer.parseInt(mantissa) > 16777216)
                                                    throw new InvalidFloatException("Mantissa out of range");

                                                return symbol(ParserSym.FLOAT_CONSTANT, yytext());
                                            }

  {StringConstant}                         {
                                                sb = new StringBuffer(yytext());
                                                if(sb.length() > 42) //quotes add 2 to max length
>>>>>>> Stashed changes
                                                    throw new InvalidLengthException(yytext());
                                                else
                                                    return symbol(ParserSym.STRING_CONSTANT, yytext());
                                            }

<<<<<<< Updated upstream
=======
  /*Declaration*/
  {Init}                                    { return symbol(ParserSym.INIT); }

>>>>>>> Stashed changes
  /* Operators */
  {Plus}                                    { return symbol(ParserSym.PLUS); }
  {Sub}                                     { return symbol(ParserSym.SUB); }
  {Mult}                                    { return symbol(ParserSym.MULT); }
  {Div}                                     { return symbol(ParserSym.DIV); }
  {Rest}                                    { return symbol(ParserSym.REST); }
  {Assig}                                   { return symbol(ParserSym.ASSIG); }
  {OpenBracket}                             { return symbol(ParserSym.OPEN_BRACKET); }
  {CloseBracket}                            { return symbol(ParserSym.CLOSE_BRACKET); }
  {OpenCurlyBrace}                          { return symbol(ParserSym.OPEN_CURLY_BRACE); }
  {CloseCurlyBrace}                         { return symbol(ParserSym.CLOSE_CURLY_BRACE); }

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

   /* Logical */
   {If}                                     { return symbol(ParserSym.IF); }
   {Else}                                   { return symbol(ParserSym.ELSE); }
   {While}                                  { return symbol(ParserSym.WHILE); }

   /* I/O */
   {Write}                                  { return symbol(ParserSym.WRITE); }
   {Read}                                   { return symbol(ParserSym.READ); }

   /* Whitespace */
   {WhiteSpace}                             { /* ignore */ }
}

   /* Comments */
   {Comment}                                { /* ignore */ }

   /* Error fallback */
   ^[]                                      { throw new UnknownCharacterException(yytext()); }
   {InvalidCharacter}                       { throw new UnknownCharacterException(yytext()); }
