package com.example.compiapp.logic

import com.example.compiapp.logic.AnalizadorLexico
import com.example.compiapp.logic.Parser
import java.io.StringReader

class Analizador {

    fun analizar(text: String): String{

        val stringBuilder = StringBuilder()
        val reporteBacken = reporteBacken()
        val lexer = AnalizadorLexico(StringReader(text))
        lexer.setContadorBackend(reporteBacken)
        val parser = Parser(lexer)


        return try {

            parser.parse()

            val erroresLexicos = lexer.erroresLexicos  // solo funciona si Kotlin reconoce el getter
            val erroresSintacticos = parser.syntaxErrors

            stringBuilder.append(
                getErrors("ERRORES LEXICOS", erroresLexicos)
            )

            stringBuilder.append(
                getErrors("ERRORES SINTACTICOS", erroresSintacticos)
            )

            stringBuilder.append(
                getReporteOperadores(reporteBacken)
            )

            if (erroresLexicos.isEmpty() && erroresSintacticos.isEmpty()) {
                "No hay errores"
            } else {
                stringBuilder.toString()
            }

        } catch (e: Exception) {
            e.printStackTrace()
            "Error real: ${e.message}"
        }

    }

    private fun getErrors(
        title: String,
        errorsList: List<String>
    ): String {
        val builder = StringBuilder()
        builder.append(title)
        builder.append("\n")
        builder.append("---------------------------------------\n")

        if (errorsList.isEmpty()) {
            builder.append("No hay errores\n\n")
        } else {
            for (error in errorsList) {
                builder.append(error)
                builder.append("\n")
            }
        }
        return builder.toString()
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
