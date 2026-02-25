package com.example.compiapp.ui.theme.viewModel

import androidx.lifecycle.ViewModel
import com.example.compiapp.logic.Analizador
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import android.content.Context
import android.net.Uri
import java.io.BufferedReader
import java.io.InputStreamReader
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainViewModel : ViewModel() {

    private val analizador = Analizador()

    private val _text = MutableStateFlow("")
    val text: StateFlow<String> = _text


    private val _result = MutableStateFlow("")
    val result: StateFlow<String> = _result
    fun updateText(newText: String) {
        _text.value = newText
    }
    fun analyzeText(input: String) {
        _result.value = analizador.analizar(input)
    }

    fun leerArchivo(context: Context, uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val reader = BufferedReader(InputStreamReader(inputStream))
                val content = StringBuilder()

                reader?.forEachLine {
                    content.append(it).append("\n")
                }

                reader?.close()

                _text.value = content.toString()

            } catch (e: Exception) {
                _text.value = "Error al leer archivo: ${e.message}"
            }
        }
    }
}