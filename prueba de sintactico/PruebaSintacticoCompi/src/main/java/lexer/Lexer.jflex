package lexer;
import java_cup.runtime.*;
import java.util.*;

%% //Separador de area

//**********Declaraciones de JFlex**********
%public
%unicode
%cup
%class AnalizadorLexico
%line
%column

//*****Definicion de las expresiones regulares*****
DIGITO = [0-9]
LETRA = [a-zA-Z]
IDENTIFICADOR = {LETRA}({LETRA}|{DIGITO})*
ESPACIOS = [ \n\r\t]
HEX_DIGIT = [0-9A-Fa-f]
COLOR_HEX = H{HEX_DIGIT}{6}
COMENTARIO = \#[^\r\n]*

//*****Constructor*****

errors = new ArrayList<>();
string = new StringBuffer();

//*****Codigo*****

%{

    private Symbol getToken(int type) {
        
        return new Symbol(type, yyline + 1, yycolumn + 1);

    }

    private Symbol getToken(int type, Object value) {
        
        return new Symbol(type, yyline + 1, yycolumn + 1, value);

    }

    private List<String> errors;

    public List<String> getErroresLexicos() {
        return this.errors;
    }

    StringBuffer string;

    private void error(String message) {
        errors.add("Error en la linea: " + (yyline + 1) + ", columna: " + (yycolumn + 1) + " - " + message);
    }

%}

%% //Separador de area

//**********Reglas lexicas**********
//*****Palabras reservadas*****
//**SeudoCodigo**
"VAR"                           { return getToken(sym.VAR); }
"INICIO"                        { return getToken(sym.INICIO); }
"FIN"                           { return getToken(sym.FIN); }
"SI"                            { return getToken(sym.SI); }
"ENTONCES"                      { return getToken(sym.ENTONCES); }
"MIENTRAS"                      { return getToken(sym.MIENTRAS); }
"HACER"                         { return getToken(sym.HACER); }
"MOSTRAR"                       { return getToken(sym.MOSTRAR); }
"LEER"                          { return getToken(sym.LEER); }
//**Configuración**
"%DEFAULT"                      { return getToken(sym.DEFAULT); }
"%COLOR_TEXTO_SI"               { return getToken(sym.COLOR_TEXTO_SI); }
"%COLOR_SI"                     { return getToken(sym.COLOR_SI); }
"%FIGURA_SI"                    { return getToken(sym.FIGURA_SI); }
"%LETRA_SI"                     { return getToken(sym.LETRA_SI); }
"%LETRA_SIZE_SI"                { return getToken(sym.LETRA_SIZE_SI); }
"%COLOR_TEXTO_MIENTRAS"         { return getToken(sym.COLOR_TEXTO_MIENTRAS); }
"%COLOR_MIENTRAS"               { return getToken(sym.COLOR_MIENTRAS); }
"%FIGURA_MIENTRAS"              { return getToken(sym.FIGURA_MIENTRAS); }
"%LETRA_MIENTRAS"               { return getToken(sym.LETRA_MIENTRAS); }
"%LETRA_SIZE_MIENTRAS"          { return getToken(sym.LETRA_SIZE_MIENTRAS); }
"%COLOR_TEXTO_BLOQUE"           { return getToken(sym.COLOR_TEXTO_BLOQUE); }
"%COLOR_BLOQUE"                 { return getToken(sym.COLOR_BLOQUE); }
"%FIGURA_BLOQUE"                { return getToken(sym.FIGURA_BLOQUE); }
"%LETRA_BLOQUE"                 { return getToken(sym.LETRA_BLOQUE); }
"%LETRA_SIZE_BLOQUE"            { return getToken(sym.LETRA_SIZE_BLOQUE); }
//**Figuras**
"ELIPSE"                        { return getToken(sym.ELIPSE); }
"CIRCULO"                       { return getToken(sym.CIRCULO); }
"PARALELOGRAMO"                 { return getToken(sym.PARALELOGRAMO); }
"RECTANGULO"                    { return getToken(sym.RECTANGULO); }
"ROMBO"                         { return getToken(sym.ROMBO); }
"RECTANGULO_REDONDEADO"         { return getToken(sym.RECTANGULO_REDONDEADO); }
//**Tipo de letras**
"ARIAL"                         { return getToken(sym.ARIAL); }
"TIME_NEW_ROMAN"                { return getToken(sym.TIME_NEW_ROMAN); }
"COMIC_SANS"                    { return getToken(sym.COMIC_SANS); }
"VERDANA"                       { return getToken(sym.VERDANA); }
//*****Colores*****
{COLOR_HEX}                     { return getToken(sym.COLOR_HEX); }
//*****Operadores relacionales*****
"=="                            { return getToken(sym.IGUALDAD); }
"!="                            { return getToken(sym.DIFERENCIA); }
">="                            { return getToken(sym.MAYOR_IGUAL); }
"<="                            { return getToken(sym.MENOR_IGUAL); }
">"                             { return getToken(sym.MAYOR); }
"<"                             { return getToken(sym.MENOR); }
//*****Operadores lógicos*****
"&&"                            { return getToken(sym.AND); }
"||"                            { return getToken(sym.OR); }
"!"                             { return getToken(sym.NOT); }
//*****Signos Arithmeticos*****
"+"                             { return getToken(sym.SUMA); }
"-"                             { return getToken(sym.RESTA); }
"*"                             { return getToken(sym.MULTIPLICACION); }
"/"                             { return getToken(sym.DIVISION); }
"("                             { return getToken(sym.PARENTESIS_IZQUIERDO); }
")"                             { return getToken(sym.PARENTESIS_DERECHO); }
//*****Separador*****
"%%%%"                          { return getToken(sym.SEPARADOR); }
//*****Comentarios*****
{COMENTARIO}                    { return getToken(sym.COMENTARIO); }
//*****Números*****
{DIGITO}+(\.{DIGITO}+)?         { return getToken(sym.NUMERO); }
//*****Extras*****
"="                             { return getToken(sym.ASIGNACION); }
","                             { return getToken(sym.COMA); }
{IDENTIFICADOR}                 { return getToken(sym.IDENTIFICADOR); }
{ESPACIOS}                      { /* Ignorar espacios en blanco */ }
//*****Tokens no aceptados*****
.                               { error("lexema: <" + yytext() + ">"); }
//*****EOF para avisar a cup que se detenga*****
<<EOF>>                          { return getToken(sym.EOF); }