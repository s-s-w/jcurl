
header {
  package org.jcurl;
}  

class ExprLexer extends Lexer;

options {
    k=2; // needed for newline junk
    charVocabulary='\u0000'..'\u007F'; // allow ascii
}

LPAREN: '(' ;
RPAREN: ')' ;
PLUS  : '+' ;
MINUS : '-' ;
STAR  : '*' ;
INT   : ('0'..'9')+ ;
WS    : ( ' '
        | '\r' '\n'
        | '\n'
        | '\t'
        )
        {$setType(Token.SKIP);}
      ;   
      
class ExprParser extends Parser;

expr returns [int value=0]
{int x;}
    :   value=mexpr
        ( PLUS x=mexpr  {value += x;}
        | MINUS x=mexpr {value -= x;} 
        )*
    ;

mexpr returns [int value=0]
{int x;}
    :   value=atom ( STAR x=atom {value *= x;} )*
    ;

atom returns [int value=0]
    :   i:INT {value=Integer.parseInt(i.getText());}
    |   LPAREN value=expr RPAREN
    ;
        
