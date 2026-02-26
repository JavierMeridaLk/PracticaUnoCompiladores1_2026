package com.example.compiapp.ui.theme.viewModel

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.example.compiapp.logic.NodoDelDiagrama
import android.graphics.Typeface

class DiagramaCanvas @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var nodos: List<NodoDelDiagrama> = emptyList()
    private val paintFondo = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.FILL }
    private val paintBorde = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 5f
        color = Color.BLACK
    }
    private val paintTexto = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        textSize = 40f
        textAlign = Paint.Align.CENTER
    }
    fun actualizarDiagrama(nuevosNodos: List<NodoDelDiagrama>) {
        this.nodos = nuevosNodos
        invalidate()
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (nodos.isEmpty()) return
        var yActual = 100f
        val centroX = width / 2f

        nodos.forEach { nodo ->
            prepararPinceles(nodo)

            // se calcula el tamaño, se toma en cuenta el texto más largo de cada nodo
            val textoReferencia = when (nodo) {
                is NodoDelDiagrama.Bloque -> nodo.instrucciones.maxByOrNull { it.length } ?: ""
                is NodoDelDiagrama.CondicionSi -> nodo.condicion
                is NodoDelDiagrama.CicloMientras -> nodo.condicion
                is NodoDelDiagrama.Inicio -> "INICIO"
                is NodoDelDiagrama.Fin -> "FIN"
            }

            val anchoTexto = paintTexto.measureText(textoReferencia)
            val anchoFigura = Math.max(550f, anchoTexto + 120f) //
            val altoFigura = if (nodo is NodoDelDiagrama.Bloque) {
                (nodo.instrucciones.size * 65f) + 60f
            } else {
                180f
            }

            //Dibujo de lA figura en si
            dibujarFiguraGeometrica(canvas, nodo, centroX, yActual, anchoFigura, altoFigura)

            val fontMetrics = paintTexto.fontMetrics
            val baselineOffset = (fontMetrics.ascent + fontMetrics.descent) / 2

            when (nodo) {
                is NodoDelDiagrama.Bloque -> {
                    var yTexto = yActual + 65f
                    nodo.instrucciones.forEach {
                        canvas.drawText(it, centroX, yTexto, paintTexto)
                        yTexto += 60f
                    }
                }
                else -> {
                    val textoAMostrar = when(nodo) {
                        is NodoDelDiagrama.CondicionSi -> nodo.condicion
                        is NodoDelDiagrama.CicloMientras -> nodo.condicion
                        is NodoDelDiagrama.Inicio -> "INICIO"
                        is NodoDelDiagrama.Fin -> "FIN"
                        else -> ""
                    }
                    //Se llama al metodo de canvas ya con todos los parametors anteriormente preparados
                    canvas.drawText(textoAMostrar, centroX, (yActual + altoFigura / 2) - baselineOffset, paintTexto)
                }
            }

            // se agrega flechas
            yActual += altoFigura + 100f
            if (nodo != nodos.last()) {
                dibujarFlecha(canvas, centroX, yActual - 100f, yActual)
            }
        }
    }

    private fun dibujarFiguraGeometrica(canvas: Canvas, nodo: NodoDelDiagrama, cx: Float, cy: Float, w: Float, h: Float) {
        val rect = RectF(cx - w / 2, cy, cx + w / 2, cy + h)

        val forma = nodo.estilo.figura ?: when(nodo) {
            is NodoDelDiagrama.Inicio, is NodoDelDiagrama.Fin -> "ELIPSE" // Cambio a Elipse para que rodee el texto
            is NodoDelDiagrama.CondicionSi, is NodoDelDiagrama.CicloMientras -> "ROMBO"
            else -> "RECTANGULO"
        }

        when (forma) {
            "CIRCULO", "ELIPSE" -> {
                canvas.drawOval(rect, paintFondo)
                canvas.drawOval(rect, paintBorde)
            }
            "ROMBO" -> {
                val path = Path().apply {
                    moveTo(cx, cy)
                    lineTo(cx + w/2, cy + h/2)
                    lineTo(cx, cy + h)
                    lineTo(cx - w/2, cy + h/2)
                    close()
                }
                canvas.drawPath(path, paintFondo)
                canvas.drawPath(path, paintBorde)
            }
            "PARALELOGRAMO" -> {
                val path = Path().apply {
                    moveTo(cx - w/2 + 50f, cy)
                    lineTo(cx + w/2 + 50f, cy)
                    lineTo(cx + w/2 - 50f, cy + h)
                    lineTo(cx - w/2 - 50f, cy + h)
                    close()
                }
                canvas.drawPath(path, paintFondo)
                canvas.drawPath(path, paintBorde)
            }
            "RECTANGULO_REDONDEADO" -> {
                canvas.drawRoundRect(rect, 40f, 40f, paintFondo)
                canvas.drawRoundRect(rect, 40f, 40f, paintBorde)
            }
            else -> {
                canvas.drawRect(rect, paintFondo)
                canvas.drawRect(rect, paintBorde)
            }
        }
    }

    private fun dibujarFlecha(canvas: Canvas, x: Float, yInicio: Float, yFin: Float) {
        canvas.drawLine(x, yInicio, x, yFin, paintBorde)
        canvas.drawLine(x, yFin, x - 20f, yFin - 20f, paintBorde)
        canvas.drawLine(x, yFin, x + 20f, yFin - 20f, paintBorde)
    }

    private fun prepararPinceles(nodo: NodoDelDiagrama) {
        //agrega el formato de las config
        paintFondo.color = nodo.estilo.colorFondo?.let { parseColor(it) } ?: Color.WHITE
        paintTexto.color = nodo.estilo.colorTexto?.let { parseColor(it) } ?: Color.BLACK

        val size = nodo.estilo.sizeLetra ?: 22f
        paintTexto.textSize = size * 2.5f
        paintTexto.typeface = when (nodo.estilo.fuente) {
            "ARIAL" -> Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
            "TIME_NEW_ROMAN" -> Typeface.create(Typeface.SERIF, Typeface.NORMAL)
            "COMIC_SANS" -> Typeface.create("casual", Typeface.NORMAL)
            "VERDANA" -> Typeface.create("sans-serif-light", Typeface.NORMAL)
            else -> Typeface.DEFAULT
        }
    }

    private fun parseColor(colorStr: String): Int {
        return try {
            if (colorStr.startsWith("H")) {
                Color.parseColor("#" + colorStr.substring(1))
            } else if (colorStr.contains(",")) {
                val rgb = colorStr.split(",").map { it.trim().toInt() }
                Color.rgb(rgb[0], rgb[1], rgb[2])
            } else {
                Color.WHITE
            }
        } catch (e: Exception) {
            Color.WHITE
        }
    }
}