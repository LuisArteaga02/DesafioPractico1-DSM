package com.example.ejerciciosdesafiopractico
import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.io.OutputStream

class CalculadoraActivity : AppCompatActivity() {

    private val model = CalculadoraModel()
    private val NOMBRE_ARCHIVO_INTERNO = "historial_calculadora.txt"


    private val requestStorageLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            exportarHistorialADescargas()
        } else {
            Toast.makeText(this, "Permiso denegado para exportar a Descargas", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.calculadora_activity)


        val btnVolver = findViewById<Button>(R.id.btnVolver)
        val etNum1 = findViewById<EditText>(R.id.etNumero1)
        val etNum2 = findViewById<EditText>(R.id.etNumero2)
        val tvResultado = findViewById<TextView>(R.id.tvResultado)
        val tvHistorial = findViewById<TextView>(R.id.tvHistorialContenido)


        val btnSumar = findViewById<Button>(R.id.btnSumar)
        val btnRestar = findViewById<Button>(R.id.btnRestar)
        val btnMultiplicar = findViewById<Button>(R.id.btnMultiplicar)
        val btnDividir = findViewById<Button>(R.id.btnDividir)
        val btnPotencia = findViewById<Button>(R.id.btnPotencia)
        val btnRaiz = findViewById<Button>(R.id.btnRaiz)


        val btnVerHistorial = findViewById<Button>(R.id.btnVerHistorial)
        val btnExportarDescargas = findViewById<Button>(R.id.btnExportarDescargas)

        btnVolver.setOnClickListener {
            finish()
        }


        btnSumar.setOnClickListener {
            val (n1, n2) = obtenerDosNumeros(etNum1, etNum2) ?: return@setOnClickListener
            val res = model.sumar(n1, n2)
            procesarExito("$n1 + $n2 = ${model.formatearResultado(res)}", tvResultado)
        }


        btnRestar.setOnClickListener {
            val (n1, n2) = obtenerDosNumeros(etNum1, etNum2) ?: return@setOnClickListener
            val res = model.restar(n1, n2)
            procesarExito("$n1 - $n2 = ${model.formatearResultado(res)}", tvResultado)
        }


        btnMultiplicar.setOnClickListener {
            val (n1, n2) = obtenerDosNumeros(etNum1, etNum2) ?: return@setOnClickListener
            val res = model.multiplicar(n1, n2)
            procesarExito("$n1 × $n2 = ${model.formatearResultado(res)}", tvResultado)
        }


        btnDividir.setOnClickListener {
            val (n1, n2) = obtenerDosNumeros(etNum1, etNum2) ?: return@setOnClickListener
            val res = model.dividir(n1, n2)
            if (res == null) {
                etNum2.error = getString(R.string.errDivisionCero)
            } else {
                procesarExito("$n1 ÷ $n2 = ${model.formatearResultado(res)}", tvResultado)
            }
        }


        btnPotencia.setOnClickListener {
            val (base, exp) = obtenerDosNumeros(etNum1, etNum2) ?: return@setOnClickListener
            val res = model.exponente(base, exp)
            procesarExito("$base ^ $exp = ${model.formatearResultado(res)}", tvResultado)
        }


        btnRaiz.setOnClickListener {
            val n1 = etNum1.text.toString().toDoubleOrNull()
            if (n1 == null) {
                etNum1.error = getString(R.string.errCampoVacio)
                return@setOnClickListener
            }

            val res = model.raizCuadrada(n1)
            if (res == null) {
                etNum1.error = getString(R.string.errRaizNegativa)
            } else {
                procesarExito("√$n1 = ${model.formatearResultado(res)}", tvResultado)
            }
        }


        btnVerHistorial.setOnClickListener {
            val contenido = leerHistorialInterno()
            tvHistorial.text = if (contenido.isEmpty()) {
                "No hay operaciones registradas aún."
            } else {
                "${getString(R.string.lblHistorialTitulo)}\n\n$contenido"
            }
        }


        btnExportarDescargas.setOnClickListener {
            verificarPermisoYExportar()
        }
    }



    private fun obtenerDosNumeros(et1: EditText, et2: EditText): Pair<Double, Double>? {
        val n1 = et1.text.toString().toDoubleOrNull()
        val n2 = et2.text.toString().toDoubleOrNull()

        if (n1 == null) {
            et1.error = getString(R.string.errNumeroVacio)
            return null
        }
        if (n2 == null) {
            et2.error = getString(R.string.errNumeroVacio)
            return null
        }
        return Pair(n1, n2)
    }

    private fun procesarExito(lineaOperacion: String, tvResultado: TextView) {
        val tagResultado = getString(R.string.lblResultadoCalc)
        tvResultado.text = "$tagResultado $lineaOperacion"
        guardarOperacionInterna(lineaOperacion)
    }



    private fun guardarOperacionInterna(registro: String) {
        try {

            openFileOutput(NOMBRE_ARCHIVO_INTERNO, Context.MODE_APPEND).use { fos ->
                fos.write("$registro\n".toByteArray())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun leerHistorialInterno(): String {
        return try {
            openFileInput(NOMBRE_ARCHIVO_INTERNO).use { fis ->
                BufferedReader(InputStreamReader(fis)).readText()
            }
        } catch (e: Exception) {
            ""
        }
    }



    private fun verificarPermisoYExportar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            exportarHistorialADescargas()
        } else {

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                exportarHistorialADescargas()
            } else {
                requestStorageLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }
    }

    private fun exportarHistorialADescargas() {
        val contenido = leerHistorialInterno()
        if (contenido.isEmpty()) {
            Toast.makeText(this, "El historial está vacío. Realiza cálculos primero.", Toast.LENGTH_SHORT).show()
            return
        }

        val nombreArchivoExportado = "historial_calculadora_${System.currentTimeMillis()}.txt"

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, nombreArchivoExportado)
                    put(MediaStore.MediaColumns.MIME_TYPE, "text/plain")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                }

                val uri = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                if (uri != null) {
                    contentResolver.openOutputStream(uri)?.use { outputStream ->
                        outputStream.write(contenido.toByteArray())
                    }
                    Toast.makeText(this, getString(R.string.msgExportadoOk), Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, getString(R.string.errExportar), Toast.LENGTH_SHORT).show()
                }
            } else {

                @Suppress("DEPRECATION")
                val descargasDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                if (!descargasDir.exists()) descargasDir.mkdirs()

                val archivoDestino = File(descargasDir, nombreArchivoExportado)
                FileOutputStream(archivoDestino).use { fos ->
                    fos.write(contenido.toByteArray())
                }
                Toast.makeText(this, getString(R.string.msgExportadoOk), Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "${getString(R.string.errExportar)}: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}