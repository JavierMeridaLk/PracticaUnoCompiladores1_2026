package com.example.compiapp.logic;

public class reporteBacken {

    private int sumas = 0;
    private int restas = 0;
    private int multiplicacion = 0;
    private int divisiones = 0;

    public int getSumas() {
        return sumas;
    }

    public int getRestas() {
        return restas;
    }

    public int getMultiplicacion() {
        return multiplicacion;
    }

    public int getDivisiones() {
        return divisiones;
    }

    public void setSumas(int sumas) {
        this.sumas = sumas;
    }

    public void setRestas(int restas) {
        this.restas = restas;
    }

    public void setMultiplicacion(int multiplicacion) {
        this.multiplicacion = multiplicacion;
    }

    public void setDivisiones(int divisiones) {
        this.divisiones = divisiones;
    }
    public void agregarSuma() {
        sumas++;
    }

    public void agregarResta() {
        restas++;
    }

    public void agregarMultiplicacion() {
        multiplicacion++;
    }

    public void agregarDivision() {
        divisiones++;
    }

}
