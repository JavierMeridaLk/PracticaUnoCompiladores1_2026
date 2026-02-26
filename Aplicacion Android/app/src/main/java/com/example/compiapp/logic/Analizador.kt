package com.example.compiapp.logic

import java.io.StringReader

class Analizador {

    fun analizar(text: String, onNodosGenerados: (List<NodoDelDiagrama>) -> Unit): String {
        val reporteBacken = reporteBacken()
        val lexer = AnalizadorLexico(StringReader(text))
        lexer.setContadorBackend(reporteBacken)

        val parser = Parser(lexer)

        return try {
            parser.parse()
            val erroresLexicos = lexer.erroresLexicos
            val erroresSintacticos = parser.syntaxErrors

            if (erroresLexicos.isEmpty() && erroresSintacticos.isEmpty()) {
                val programa = Programa()
                programa.separarBloques(text)

                val generador = GeneradorDeDiagrama()
                val listaNodos = generador.generarNodos(programa.sentencias)
                generador.aplicarConfiguracion(listaNodos, programa.configuracion)

                onNodosGenerados(listaNodos)

                buildString {
                    appendLine("No hay errores.")
                    appendLine()
                    append(obtenerReporte(reporteBacken))
                }
            } else {

                onNodosGenerados(emptyList())
                buildString {
                    append(getErrors("ERRORES LEXICOS", erroresLexicos))
                    append(getErrors("ERRORES SINTACTICOS", erroresSintacticos))
                }
            }
        } catch (e: Exception) {
            onNodosGenerados(emptyList())
            "❌ Error real: ${e.message}"
        }
    }

    private fun getErrors(
        title: String,
        errorsList: List<String>
    ): String {
        if (errorsList.isEmpty()) return "$title\n No hay errores detectados.\n"

        return buildString {
            append("❌ $title\n")
            append("═".repeat(30) + "\n")
            errorsList.forEachIndexed { index, error ->
                append("${index + 1}. $error\n")
            }
            append("═".repeat(30) + "\n")
            append("Total de errores: ${errorsList.size}.")
        }
    }
    fun obtenerReporte(backend: reporteBacken): String {
        return buildString {
            appendLine(" --- REPORTES ---")

            appendLine("\n OPERADORES MATEMÁTICOS")
            appendLine("------------------------------------------")
            if (backend.operadores.isEmpty()) {
                appendLine("  (No se encontraron operadores)")
            } else {
                backend.operadores.forEach { appendLine(it) }
            }
            appendLine("------------------------------------------")

            // --- ESTRUCTURAS ---
            appendLine("\n ESTRUCTURAS DE CONTROL")
            appendLine("------------------------------------------")
            if (backend.estructuras.isEmpty()) {
                appendLine("  (No se encontraron estructuras)")
            } else {
                backend.estructuras.forEach { appendLine(it) }
            }
            appendLine("------------------------------------------")
        }
    }

}
