grammar SPL;

COMMENT:'//' ~[\r\n]* -> skip;
WS:[ \t\r\n]+ -> skip;

TRUE:'true';
FALSE:'false';
AND:'and';
OR:'or';
VAR:'var';
PRINT:'print';
IF:'if';
ELSE:'else';
WHILE:'while';

SEMICOLON:';';
LPAREN:'(';
RPAREN:')';
LBRACE:'{';
RBRACE:'}';

STRING:'"'.*?'"';
NUMBER:[0-9]+('.'[0-9]+)?;
IDENTIFIER:[A-Za-z][A-Za-z0-9]*;

PLUS:'+';
MINUS:'-';
ASTERIKS:'*';
FSLASH:'/';
ASSIGN:'=';
EQUAL:'==';
NOTEQUAL:'!=';
GREATER:'>';
LESSER:'<';
GEQUAL:'>=';
LEQUAL:'<=';
NOT:'!';

program
        : declaration* EOF
        ;

declaration
        : varDecl
        | statement
        ;

varDecl
        : VAR IDENTIFIER (ASSIGN expression )? SEMICOLON
        ;

statement
        : exprStmt
        | ifStmt
        | printStmt
        | whileStmt
        | block
        ;

exprStmt
        : expression SEMICOLON
        ;

ifStmt
        : IF LPAREN expression RPAREN statement ( ELSE statement)?
        ;

printStmt
        : PRINT expression SEMICOLON
        ;

whileStmt
        : WHILE LPAREN expression RPAREN statement
        ;

block
        : LBRACE declaration* RBRACE
        ;

expression
        : assignment
        ;

assignment
        : IDENTIFIER ASSIGN assignment
        | logic_or
        ;

logic_or
        : logic_and ( OR logic_and )*
        ;

logic_and
        : equality ( AND equality )*
        ;

equality
        : comparison ( ( NOTEQUAL | EQUAL ) comparison )*
        ;

comparison
        : term ( ( GREATER | GEQUAL | LESSER | LEQUAL ) term )*
        ;

term
        : factor ( ( MINUS | PLUS ) factor )*
        ;

factor
        : unary ( ( FSLASH | ASTERIKS ) unary )*
        ;

unary
        : ( NOT | MINUS ) unary
        | primary
        ;

primary
        : TRUE
        | FALSE
        | NUMBER
        | STRING
        | LPAREN expression RPAREN
        | IDENTIFIER
        ;