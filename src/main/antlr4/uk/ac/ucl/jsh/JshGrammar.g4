grammar JshGrammar;

/*
 * Parser Rules
 */

command : atomicCommand (';' atomicCommand)*;

atomicCommand : (NONSPECIAL | DOUBLEQUOTED | SINGLEQUOTED)+;

/*
 * Lexer Rules
 */

NONSPECIAL : ~['";]+;
DOUBLEQUOTED : '"' (~'"')* '"';
SINGLEQUOTED : '\'' (~'\'')* '\'';