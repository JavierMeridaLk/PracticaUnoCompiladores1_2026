package com.example.compiapp.logic

    data class Estilo(
        var colorFondo: String? = null,
        var colorTexto: String? = null,
        var figura: String? = null,
        var fuente: String? = null,
        var sizeLetra: Float?= null
    )
    sealed class NodoDelDiagrama{
        var estilo: Estilo = Estilo()
        class Inicio : NodoDelDiagrama()
        class Fin : NodoDelDiagrama()
        data class Bloque(val instrucciones: MutableList<String>) : NodoDelDiagrama()
        data class CondicionSi(val condicion: String) : NodoDelDiagrama()
        data class CicloMientras(val condicion: String) : NodoDelDiagrama()
    }
class GeneradorDeDiagrama {

    fun generarNodos(sentencias:List<String>): List<NodoDelDiagrama>{
        val nodos = mutableListOf<NodoDelDiagrama>()
        var bloqueActual: NodoDelDiagrama.Bloque? = null
        for (linea in sentencias) {
            val lineaLimpia = linea.trim()
            if (lineaLimpia.isEmpty()) continue
            //****EValuamos wue tipo de instruccion viene
            when {
                //Si es alguna sentencia o instruccion que no pertenece al bloque entonces cortamos el bloque
                lineaLimpia.startsWith("INICIO") -> { /* ... */ }
                lineaLimpia.startsWith("FIN SI") || lineaLimpia.startsWith("FIN MIENTRAS") -> {
                    bloqueActual = null
                }
                lineaLimpia.startsWith("FIN") -> { /* ... */ }
                lineaLimpia.startsWith("FIN") -> {
                    bloqueActual = null
                    nodos.add(NodoDelDiagrama.Fin())
                }
                lineaLimpia.startsWith("SI") -> {
                    bloqueActual = null
                    nodos.add(NodoDelDiagrama.CondicionSi(lineaLimpia))
                }
                lineaLimpia.startsWith("MIENTRAS") -> {
                    bloqueActual = null
                    nodos.add(NodoDelDiagrama.CicloMientras(lineaLimpia))
                }
                else -> {
                    // Si foeman parte de los bloues los agrupamos
                    if (bloqueActual == null) {
                        bloqueActual = NodoDelDiagrama.Bloque(mutableListOf())
                        nodos.add(bloqueActual)
                    }
                    bloqueActual.instrucciones.add(lineaLimpia)
                }
            }
        }
        return nodos
    }
    fun aplicarConfiguracion(nodos: List<NodoDelDiagrama>, confi: List<String>){

        for (linea in confi) {
            //Limpiamos la sentenia de configuracion
            val partes = linea.split("=")
            if (partes.size < 2) continue

            val clave = partes[0].trim()
            val valorYId = partes[1].trim().split("|")

            val valor = valorYId[0].trim()
            val indice = if (valorYId.size > 1) valorYId[1].trim().toIntOrNull() else null

            // aplicar segun el indice
            if (indice != null && indice < nodos.size) {
                asignarConfi(nodos[indice], clave, valor)
            } else {
                nodos.forEach { nodo ->
                    if (correspondeTipo(nodo, clave)) {
                        asignarConfi(nodo, clave, valor)
                    }
                }
            }
        }

    }
    private fun correspondeTipo(nodo: NodoDelDiagrama, clave: String): Boolean {
        return when {
            clave.contains("_SI") && nodo is NodoDelDiagrama.CondicionSi -> true
            clave.contains("_MIENTRAS") && nodo is NodoDelDiagrama.CicloMientras -> true
            clave.contains("_BLOQUE") && nodo is NodoDelDiagrama.Bloque -> true
            else -> false
        }
    }
    private fun asignarConfi(nodo: NodoDelDiagrama, clave: String, valor: String) {
        when {
            clave.contains("COLOR_TEXTO") -> nodo.estilo.colorTexto = valor
            clave.contains("COLOR") -> nodo.estilo.colorFondo = valor
            clave.contains("FIGURA") -> nodo.estilo.figura = valor
            clave.contains("LETRA_SIZE") -> nodo.estilo.sizeLetra = valor.toFloatOrNull() ?: 12f
            clave.contains("LETRA") -> nodo.estilo.fuente = valor
        }
    }
}