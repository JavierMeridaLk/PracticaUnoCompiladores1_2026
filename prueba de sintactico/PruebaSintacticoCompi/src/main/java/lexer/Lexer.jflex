package com.example.compiapp.logic;

import java.util.ArrayList;
import java.util.List;
import java_cup.runtime.Symbol;

%% 

%public
%unicode
%cup
%class AnalizadorLexico
%line
%column

//*****Definicion de las expresiones regulares*****
DIGITO = [0-9]
LETRA = [a-zA-Z]
IDENTIFICADOR = {LETRA}({LETRA}|{DIGITO}|_)*
ESPACIOS = [ \n\r\t]+
COLOR_HEX = H[0-9A-Fa-f]{6}
COMENTARIO = "#"[^\r\n]*
TEXTO = \"[^\"]*\"

%{
    private Symbol getToken(int type) {
        return new Symbol(type, yyline + 1, yycolumn + 1);
    }

    private Symbol getToken(int type, Object value) {
        return new Symbol(type, yyline + 1, yycolumn + 1, value);
    }

    private List<String> errors = new ArrayList<>();

    public List<String> getErroresLexicos() {
        return this.errors;
    }

    private void error(String message) {
        errors.add("Error en la linea: " + (yyline + 1) + ", columna: " + (yycolumn + 1) + " - " + message);
    }

    private reporteBacken reporteBacken;
    
    public void setContadorBackend(reporteBacken reporteBacken) {
        this.reporteBacken = reporteBacken;
    }
%}

%% 

//*****Palabras reservadas*****
"FIN SI"                        { return getToken(sym.FINSI); }
"FIN MIENTRAS"                  { return getToken(sym.FINMIENTRAS); }
"VAR"                           { return getToken(sym.VAR); }
"INICIO"                        { return getToken(sym.INICIO); }
"FIN"                           { return getToken(sym.FIN); }
"SI"                            { return getToken(sym.SI); }
"ENTONCES"                      { return getToken(sym.ENTONCES); }
"MIENTRAS"                      { return getToken(sym.MIENTRAS); }
"HACER"                         { return getToken(sym.HACER); }
"MOSTRAR"                       { return getToken(sym.MOSTRAR); }
"LEER"                          { return getToken(sym.LEER); }

//*****Configuración*****
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

//*****Figuras*****
"ELIPSE"                        { return getToken(sym.ELIPSE); }
"CIRCULO"                       { return getToken(sym.CIRCULO); }
"PARALELOGRAMO"                 { return getToken(sym.PARALELOGRAMO); }
"RECTANGULO"                    { return getToken(sym.RECTANGULO); }
"ROMBO"                         { return getToken(sym.ROMBO); }
"RECTANGULO_REDONDEADO"         { return getToken(sym.RECTANGULO_REDONDEADO); }

//*****Tipo de letras*****
"ARIAL"                         { return getToken(sym.ARIAL); }
"TIME_NEW_ROMAN"                { return getToken(sym.TIME_NEW_ROMAN); }
"COMIC_SANS"                    { return getToken(sym.COMIC_SANS); }
"VERDANA"                       { return getToken(sym.VERDANA); }

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

//*****Operadores Aritmeticos*****
"+"                             { if(reporteBacken!=null) reporteBacken.agregarSuma(); return getToken(sym.SUMA); }
"-"                             { if(reporteBacken!=null) reporteBacken.agregarResta(); return getToken(sym.RESTA); }
"*"                             { if(reporteBacken!=null) reporteBacken.agregarMultiplicacion(); return getToken(sym.MULTIPLICACION); }
"/"                             { if(reporteBacken!=null) reporteBacken.agregarDivision(); return getToken(sym.DIVISION); }
"("                             { return getToken(sym.PARENTESIS_IZQUIERDO); }
")"                             { return getToken(sym.PARENTESIS_DERECHO); }

//*****Separador y Signos*****
"%%%%"                           { return getToken(sym.SEPARADOR); }
"|"                             { return getToken(sym.PIPE); }
"="                             { return getToken(sym.ASIGNACION); }
","                             { return getToken(sym.COMA); }

//*****Valores*****
{COLOR_HEX}                     { return getToken(sym.COLOR_HEX, yytext()); }
{TEXTO}                         { 
                                    String contenido = yytext().substring(1, yytext().length()-1);
                                    return getToken(sym.TEXTO, contenido); 
                                }

{DIGITO}+\.{DIGITO}+            { return getToken(sym.DECIMAL, Double.parseDouble(yytext())); }
{DIGITO}+                       { return getToken(sym.ENTERO, Integer.parseInt(yytext())); }

{IDENTIFICADOR}                 { return getToken(sym.IDENTIFICADOR, yytext()); }

//*****Extras ignorados*****
{COMENTARIO}                    { /* Ignorar */ }
{ESPACIOS}                      { /* Ignorar */ }

.                               { error("lexema no reconocido: <" + yytext() + ">"); }
<<EOF>>                         { return getToken(sym.EOF); }