package com.example.ejerciciosdesafiopractico

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.ejerciciosdesafiopractico.ui.theme.EjerciciosDesafioPracticoTheme
import org.w3c.dom.Text
import android.widget.TextView

class PromedioActivity : ComponentActivity() {

    private val model = PromedioModel()
    private val CHANNEL_ID = "canal_promedio_estudiantes"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.promedio_activity)

        crearCanalNotificaciones()

        val btnVolver = findViewById<Button>(R.id.btnVolver)
        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etNotas = listOf(
            findViewById<EditText>(R.id.etNota1),
            findViewById<EditText>(R.id.etNota2),
            findViewById<EditText>(R.id.etNota3),
            findViewById<EditText>(R.id.etNota4),
            findViewById<EditText>(R.id.etNota5),
        )
        val btnResultado = findViewById<Button>(R.id.btnResultado)
        val tvResultado = findViewById<TextView>(R.id.tvResultado)

        btnVolver.setOnClickListener {
            finish()
        }
        


    }
}
