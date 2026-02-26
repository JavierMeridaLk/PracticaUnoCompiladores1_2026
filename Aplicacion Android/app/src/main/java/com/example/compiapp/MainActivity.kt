package com.example.compiapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.compiapp.ui.theme.CompiAppTheme
import com.example.compiapp.ui.theme.viewModel.MainViewModel
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Button
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.activity.result.contract.ActivityResultContracts
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.ui.platform.LocalContext
import android.content.Intent
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.viewinterop.AndroidView
import com.example.compiapp.ui.theme.viewModel.DiagramaCanvas


class MainActivity : ComponentActivity() {

    private val viewModel : MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CompiAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {

    val text by viewModel.text.collectAsState()
    val result by viewModel.result.collectAsState()
    val nodos by viewModel.nodos.collectAsState()
    val context = LocalContext.current
    //para poder cargar archivos
    val filePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            context.contentResolver.takePersistableUriPermission(
                it,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            viewModel.leerArchivo(context, it)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()) //
            .padding(24.dp),
        verticalArrangement = Arrangement.Top
    ) {

        Text(
            text = "Generador de Diagramas de Flujo",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(24.dp))

        // TEXT FIELD
        OutlinedTextField(
            value = text,
            onValueChange = { viewModel.updateText(it) },
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            maxLines = Int.MAX_VALUE,
            label = { Text("Código fuente") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // BOTÓN SUBIR ARCHIVO
        OutlinedButton(
            onClick = {
                filePicker.launch(arrayOf("text/plain"))
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.UploadFile,
                contentDescription = "Subir archivo"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Subir archivo de texto")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // BOTÓN ANALIZAR
        Button(
            onClick = { viewModel.analyzeText(text) },
            enabled = text.isNotBlank(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Analizar")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // RESULTADO
        if (result.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = result,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        if (nodos.isNotEmpty()) {
            Text("Diagrama Generado:", style = MaterialTheme.typography.titleMedium)

            //puente a canvas
            AndroidView(
                factory = { ctx ->
                    //vuista general
                    DiagramaCanvas(ctx).apply {
                    }
                },
                update = { view ->
                    // update cada que cambie un nodo
                    view.actualizarDiagrama(nodos)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1000.dp)
            )
        }
    }
}
/*
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CompiAppTheme {
        Greeting("Android")
    }
}
*/
