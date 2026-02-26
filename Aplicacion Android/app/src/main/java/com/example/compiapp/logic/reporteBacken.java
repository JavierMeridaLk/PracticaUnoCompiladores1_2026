package com.example.compiapp.logic;

import java.util.ArrayList;
import java.util.List;

public class reporteBacken {

        private List<String> operadores = new ArrayList<>();
        private List<String> estructuras = new ArrayList<>();
        //Formatear la entrada obtenida en el lexer
        public void agregarOperador(String tipo, int linea, int columna, String lexema) {
            // Formato: TIPO | L: línea | C: columna | Lexema
            String info = String.format("%-10s | LINEA: %-3d | COLUMNA: %-3d | LEXEMA [%s]", tipo, linea, columna, lexema);
            operadores.add(info);
        }

        public void agregarEstructura(String tipo, int linea, int columna, String lexema) {
            // Formato: TIPO | L: línea | C: columna | Lexema
            String info = String.format("%-12s | LINEA: %-3d | COLUMNA: %-3d | LEXEMA [%s]", tipo, linea, columna, lexema);
            estructuras.add(info);
        }
        // Getters
        public List<String> getOperadores() {
            return operadores;
        }
        public List<String> getEstructuras() {
            return estructuras;
        }
}

