class ExprParser extends Parser;

options {
        buildAST=true;
}

expr returns [double value=0]
{double x;}
    :   value=mexpr
        ( PLUS x=mexpr  {value += x;}
        | MINUS x=mexpr {value -= x;} 
        )*
    ;

mexpr returns [double value=0]
{double x;}
    :   value=atom ( MULT x=atom {value *= x;} )*
    ;

atom returns [double value=0]
    :   i:INT {value=Integer.parseInt(i.getText());}
    |   LPAREN value=expr RPAREN
    ;
        
class ExprLexer extends Lexer;

options {
    k=2; // needed for newline junk
    charVocabulary='\u0000'..'\u007F'; // allow ascii
}

LPAREN: '(' ;
RPAREN: ')' ;
PLUS  : '+' ;
MINUS : '-' ;
MULT  : '*' ;
INT   : ('0'..'9')+ ;
// REAL  : ('0'..'9')+ '.' ('0'..'9')+ ('e' ('+'|'-') ('0'..'9')+)?;
WS    : ( ' '
        | '\r' '\n'
        | '\n'
        | '\t'
        )
        {$setType(Token.SKIP);}
      ;    
    