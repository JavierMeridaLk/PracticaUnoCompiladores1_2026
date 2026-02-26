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
                    append(getReporteOperadores(reporteBacken))
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
    fun getReporteOperadores(backend: reporteBacken): String {
        return buildString {
            appendLine("===== REPORTE DE OPERADORES =====")
            appendLine("Sumas: ${backend.sumas}")
            appendLine("Restas: ${backend.restas}")
            appendLine("Multiplicaciones: ${backend.multiplicacion}")
            appendLine("Divisiones: ${backend.divisiones}")
            appendLine("=================================")
        }
    }

}
