/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package pruebasintacticocompi.pruebasintacticocompi;

import java.io.StringReader;
import java.util.List;
import lexer.AnalizadorLexico;


import parser.Parser;

/**
 *
 * @author xavi
 */
public class PruebaSintacticoCompi {

    public static void main(String[] args) {
        
        

        String texto = """

        INICIO

        VAR x = 10
        VAR y = 20
        VAR resultado = 0

        MOSTRAR "Inicio del programa"
        LEER x

        resultado = x + 5
        resultado = resultado * y
        resultado = resultado / 2

        SI (x < y) ENTONCES

            MOSTRAR "x es menor que y"
            resultado = resultado + 1

        FINSI

        MIENTRAS (resultado < 100) HACER

            MOSTRAR "Dentro del ciclo"
            resultado = resultado + 10

        FINMIENTRAS

        SI ((x < y) && !(resultado == 0)) ENTONCES

            MOSTRAR "Condicion compleja verdadera"

        FINSI

        VAR = 50

        MOSTRAR

        SI (x < ) ENTONCES

            MOSTRAR "Error en condicion"

        FINSI

        MIENTRAS resultado < 50 HACER

            MOSTRAR "Error sintactico en mientras"

        FINMIENTRAS

        resultado = + 5

        resultado = x *

        VAR 123abc = 5

        MOSTRAR "Texto sin cerrar

        VAR z = 10 @ 5

        VAR color = HFF12G5

        VAR contador = 0

        MIENTRAS (contador < 5) HACER

            MOSTRAR "Contador"
            contador = contador + 1

        FINMIENTRAS

        FIN

        %%%%

        %DEFAULT = 1

        %COLOR_TEXTO_SI = 255,0,0 | 1

        %COLOR_SI = 0,255,0 | 1

        %FIGURA_SI = ROMBO | 1

        %LETRA_SI = ARIAL | 1

        %LETRA_SIZE_SI = 12 | 1

        %COLOR_TEXTO_MIENTRAS = HFF0000 | 2

        %COLOR_MIENTRAS = 0,0,255 | 2

        %FIGURA_MIENTRAS = RECTANGULO | 2

        %LETRA_MIENTRAS = VERDANA | 2

        %LETRA_SIZE_MIENTRAS = 14 | 2

        %COLOR_SI = | 1

        %FIGURA_SI = TRIANGULO | 1

        %LETRA_SI = |

        %LETRA_SIZE_SI = 12

        %COLOR_TEXTO_SI = HZZZZZZ | 1

        %COLOR_BLOQUE = 300,0,0 | 1

        %LETRA_BLOQUE = HELVETICA | 1

        %COLOR_TEXTO_BLOQUE = 10+5,20*2,30 | 3

        %FIGURA_BLOQUE = RECTANGULO_REDONDEADO | 3

        %LETRA_BLOQUE = COMIC_SANS | 3

        %LETRA_SIZE_BLOQUE = 18 | 3

        """;
     

        StringReader reader = new StringReader(texto);
        AnalizadorLexico lexer = new AnalizadorLexico(reader);
        Parser parser = new Parser(lexer);
        
        try {
            parser.parse();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        List<String> ll = lexer.getErroresLexicos();
        List<String> ls = parser.getSyntaxErrors();
        
        System.out.println("------------Errores Lexicos--------------");
                for (String l : ll) {
                System.out.println(l);
        }
        
        System.out.println("------------Errores Sintacticos--------------");
        for (String l : ls) {
                System.out.println(l);
        }


    }
}
