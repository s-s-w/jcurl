class ExprParser extends Parser;

options {
        buildAST=true;
}

expr:   mexpr ((PLUS|MINUS) mexpr)*
    ;      

mexpr      
    :   atom (STAR atom)*
    ;    

atom:   INT
    |   LPAREN expr RPAREN 
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
STAR  : '*' ;
INT   : ('0'..'9')+ ;
// REAL  : ('0'..'9')+ '.' ('0'..'9')+ ('e' ('+'|'-') ('0'..'9')+)?;
WS    : ( ' '
        | '\r' '\n'
        | '\n'
        | '\t'
        )
        {$setType(Token.SKIP);}
      ;    
    