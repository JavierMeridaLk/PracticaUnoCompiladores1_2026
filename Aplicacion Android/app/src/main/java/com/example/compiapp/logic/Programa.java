package com.example.compiapp.logic;
import java.util.ArrayList;
import java.util.List;
public class Programa {
    public List<String> sentencias;
    public List<String> configuracion;
    public Programa() {
        this.configuracion = new ArrayList<>();
        this.sentencias = new ArrayList<>();
        GeneradorDeDiagrama generador = new GeneradorDeDiagrama();

        List<NodoDelDiagrama> nodos = generador.generarNodos(this.sentencias);
        generador.aplicarConfiguracion(nodos, this.configuracion);
    }

    public void separarBloques(String texto){
        separarTextoPorTipo(texto);

    }

    public void separarTextoPorTipo (String texto){

        String[] lineas = texto.split("\\r?\\n");

        boolean esConfiguracion = false;

        for (String linea : lineas) {

            linea = linea.trim();

            if (!linea.startsWith("#")) {

                if (linea.equals("%%%%")) {
                    esConfiguracion = true;
                    continue;
                }
                if (!esConfiguracion) {
                    sentencias.add(linea);
                } else {
                    configuracion.add(linea);
                }
            }
        }
    }
}
