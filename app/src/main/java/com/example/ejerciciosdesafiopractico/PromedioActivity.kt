package com.example.ejerciciosdesafiopractico

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import android.app.NotificationManager
import android.widget.TextView
import android.Manifest
import android.app.NotificationChannel
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.activity.result.contract.ActivityResultContracts
import android.widget.Toast
import androidx.core.content.ContextCompat
import android.annotation.SuppressLint
class PromedioActivity : ComponentActivity() {

    private val model = PromedioModel()
    private val CHANNEL_ID = "canal_promedio_estudiantes"
    private var nombrePendiente: String? = null
    private var promedioPendiente: String? = null
    private var estadoPendiente: String? = null

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            if (nombrePendiente != null && promedioPendiente != null && estadoPendiente != null) {
                enviarNotificacion(nombrePendiente!!, promedioPendiente!!, estadoPendiente!!)
            }
        } else {
            Toast.makeText(this, "Permiso de notificaciones denegado", Toast.LENGTH_SHORT).show()
        }
    }

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
        btnResultado.setOnClickListener {
            val nombre = etNombre.text.toString().trim()

            if (nombre.isEmpty()){
                etNombre.error = getString(R.string.errCampoVacio)
                return@setOnClickListener
            }
            val listadoNotas = mutableListOf<Double>()
            var HayError = false


            for(i in etNotas.indices){
                val inputStr = etNotas[i].text.toString()
                val notaDouble = inputStr.toDoubleOrNull()

                if(model.notaValida(notaDouble)){
                    listadoNotas.add(notaDouble!!)
                }else{
                    etNotas[i].error = getString(R.string.errNotaNoValida)
                    HayError = true
                }
            }
            if(HayError) return@setOnClickListener

            val promedio = model.calcularPromedioPonderado(listadoNotas)
            val promedioFormateado = model.formatoDecimal(promedio)
            val aprobo = model.esAprobado(promedio)

            val estadoTexto = if(aprobo){
                getString(R.string.lblAprobado)
            }else
                getString(R.string.lblReprobado)

            val resultadoFinal = "$nombre : $promedioFormateado ($estadoTexto)"
            tvResultado.text = resultadoFinal


            nombrePendiente = nombre
            promedioPendiente = promedioFormateado
            estadoPendiente = estadoTexto


            verificarPermisoYEnviarNotificacion()
        }






    }
    private fun verificarPermisoYEnviarNotificacion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    enviarNotificacion(nombrePendiente!!, promedioPendiente!!, estadoPendiente!!)
                }
                else -> {
                    // Solicitar permiso usando el launcher moderno
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        } else {
            // Android 12 o inferior no requiere el permiso POST_NOTIFICATIONS
            enviarNotificacion(nombrePendiente!!, promedioPendiente!!, estadoPendiente!!)
        }
    }

    private fun crearCanalNotificaciones(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notificaciones de Notas"
            val descriptionText = "Canal para Notificar sobre tu promedio"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }


    }
    @SuppressLint("MissingPermission")
    private fun enviarNotificacion(nombre: String, promedio: String, estado: String) {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Resultado Final: $nombre")
            .setContentText("Promedio: $promedio - Estado: $estado")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            notify(1, builder.build())
        }
    }
}
