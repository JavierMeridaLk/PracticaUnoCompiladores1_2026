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
            VAR a = 10
            VAR b = 20
            SI (a < b) ENTONCES
                MOSTRAR "a es menor que b"
            FIN SI
            MIENTRAS (a < 15) HACER
                a = a + 1
                MOSTRAR a
            FIN MIENTRAS
            MOSTRAR "Fin del programa"
        FIN
        %%%%
        %DEFAULT=1
        %COLOR_TEXTO_SI=12,45-5,1|1
        %FIGURA_MIENTRAS=CIRCULO|1
        %DEFAULT=3

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
