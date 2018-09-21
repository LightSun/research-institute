grammar JavaExpre;

//contains all boolean expre
surroundSimpleExpre
    : simpleExpre | ('(' simpleExpre ')')
    ;

simpleExpre
    : ternaryExpre | expre | doubeAndExpre | doubleOrExpre | equalExpre | notEqualExpre
    ;

surroundExpre
    : expre | ('(' expre ')')
    ;

// except BooleanExpre there are no boolean expre.
expre
    :varNameExpre | arrayInvoke | fieldInvoke | methodInvoke | StringLiteral | IntNumber | FloatNumber | booleanConstantExpre
    ;

simpleExpreList
    :   surroundSimpleExpre (',' surroundSimpleExpre)*
    ;

arrayInvoke
    : varNameExpre arrayExpre
    ;
fieldInvoke
    : fieldExpre ((DOT methodExpre) | arrayExpre)?
    ;

//method can own any simpleExpre
methodInvoke
   // : qualifiedName ('.' V '('')')
    : (classNameExpre | varNameExpre) (DOT methodExpre)+
    ;

arrayExpre
    : LBRACK surroundSimpleExpre RBRACK
    ;
fieldExpre
    :
    (classNameExpre DOT) ? varNameExpre(DOT varNameExpre)*
    ;
methodExpre
    :varNameExpre ('('')' | ('(' simpleExpreList ')'))
    ;

//----------------------------------------------------------

ternaryExpre
    : surroundDoubleOrExpre (QUESTION surroundExpre COLON surroundTernaryExpre) ?
    ;
surroundTernaryExpre
    : ternaryExpre | '(' ternaryExpre ')'
    ;

doubleOrExpre
    : surroundDoubeAndExpre ( '||' surroundDoubeAndExpre)*
    ;
surroundDoubleOrExpre
    : doubleOrExpre | '(' doubleOrExpre ')'
    ;

doubeAndExpre
    : (surroundExpre | surroundEqualExpre | surroundNotEqualExpre) ('&&' (surroundExpre | surroundEqualExpre | surroundNotEqualExpre))*
    ;
surroundDoubeAndExpre
    : doubeAndExpre | '(' doubeAndExpre ')'
    ;

equalExpre
    : surroundExpre '==' surroundExpre
    ;
surroundEqualExpre
    : equalExpre | '(' equalExpre ')'
    ;

notEqualExpre
    : surroundExpre '!=' surroundExpre
    ;
surroundNotEqualExpre
    : notEqualExpre | '(' notEqualExpre ')'
    ;

booleanConstantExpre
    : 'true' | 'false'
    ;

classNameExpre
     :ClassName
     ;
varNameExpre
     :VarName
     ;

ClassName
    :[A-Z] JavaLetterOrDigit*
    ;

VarName
    : [a-z$_] JavaLetterOrDigit *
    ;

StringLiteral
	:	'"' StringCharacters? '"'
	;

IntNumber
   : (Digit+)
   ;
FloatNumber
   :(Digit+) DOT (Digit+)
   ;
//==========================================================

fragment
StringCharacters
	:	StringCharacter+
	;

fragment
StringCharacter
	:	~["\\]
	|	EscapeSequence
	;

fragment
EscapeSequence
	:	'\\' [btnfr"'\\]
	|	OctalEscape
	;

fragment
OctalEscape
	:	'\\' OctalDigit
	|	'\\' OctalDigit OctalDigit
	|	'\\' ZeroToThree OctalDigit OctalDigit
	;

fragment
OctalDigit
	:	[0-7]
	;

fragment
ZeroToThree
	:	[0-3]
	;

// §3.10.7 The Null Literal

NullLiteral
	:	'null'
	;

// §3.11 Separators

LPAREN : '(';
RPAREN : ')';
//LBRACE : '{';
//RBRACE : '}';
LBRACK : '[';
RBRACK : ']';
//SEMI : ';';
COMMA : ',';
DOT : '.';


// §3.8 Identifiers (must appear after all keywords in the grammar)

fragment
JavaLetterOrDigit
	:	[a-zA-Z0-9$_] // these are the "java letters or digits" below 0xFF
	|	// covers all characters above 0xFF which are not a surrogate
		~[\u0000-\u00FF\uD800-\uDBFF]
		{Character.isJavaIdentifierPart(_input.LA(-1))}?
	|	// covers UTF-16 surrogate pairs encodings for U+10000 to U+10FFFF
		[\uD800-\uDBFF] [\uDC00-\uDFFF]
		{Character.isJavaIdentifierPart(Character.toCodePoint((char)_input.LA(-2), (char)_input.LA(-1)))}?
	;


fragment
Digit
    : [0-9]
    ;
// §3.12 Operators
AND : '&&';
OR : '||';
NOTEQUAL : '!=';
EQUAL : '==';
QUESTION : '?';
COLON : ':';

// Whitespace and comments
WS  :  [ \t\r\n\u000C]+ -> skip
    ;

COMMENT
    :   '/*' .*? '*/' -> skip
    ;
LINE_COMMENT
    :   '//' ~[\r\n]* -> skip
    ;