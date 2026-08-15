package com.example.ejerciciosdesafiopractico

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class SalarioActivity : AppCompatActivity() {

    private val model = SalarioModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.salario_activity)

        val btnVolver = findViewById<Button>(R.id.btnVolver)
        val etNombreEmpleado = findViewById<EditText>(R.id.etNombreEmpleado)
        val etSalarioBase = findViewById<EditText>(R.id.etSalarioBase)
        val btnCalcular = findViewById<Button>(R.id.btnCalcular)


        val tvResEmpleado = findViewById<TextView>(R.id.tvResEmpleado)
        val tvResSalarioBruto = findViewById<TextView>(R.id.tvResSalarioBruto)
        val tvResISSS = findViewById<TextView>(R.id.tvResISSS)
        val tvResAFP = findViewById<TextView>(R.id.tvResAFP)
        val tvResRenta = findViewById<TextView>(R.id.tvResRenta)
        val tvResTotalDescuentos = findViewById<TextView>(R.id.tvResTotalDescuentos)
        val tvResSalarioNeto = findViewById<TextView>(R.id.tvResSalarioNeto)

        btnVolver.setOnClickListener {
            finish()
        }

        btnCalcular.setOnClickListener {
            val nombre = etNombreEmpleado.text.toString().trim()
            val salarioStr = etSalarioBase.text.toString().trim()

            if (nombre.isEmpty()) {
                etNombreEmpleado.error = getString(R.string.lblNotNull)
                vibrarDispositivo()
                return@setOnClickListener
            }

            val salarioBase = salarioStr.toDoubleOrNull()
            if (!model.validarSalario(salarioBase)) {
                etSalarioBase.error = getString(R.string.errSalarioInvalido)
                vibrarDispositivo()
                return@setOnClickListener
            }

            val resultado = model.calcularLiq(salarioBase!!)

            val tagEmpleado = getString(R.string.lblResEmpleado)
            val tagBruto = getString(R.string.lblResBruto)
            val tagIsss = getString(R.string.lblResIsss)
            val tagAfp = getString(R.string.lblResAfp)
            val tagRenta = getString(R.string.lblResRenta)
            val tagTotalDesc = getString(R.string.lblResTotalDescuentos)
            val tagNeto = getString(R.string.lblResNeto)


            tvResEmpleado.text = "$tagEmpleado$nombre"
            tvResSalarioBruto.text = "$tagBruto${model.limpiar(resultado.salarioBruto)}"
            tvResISSS.text = "$tagIsss${model.limpiar(resultado.isss)}"
            tvResAFP.text = "$tagRenta${model.limpiar(resultado.renta)}"
            tvResRenta.text = "$tagRenta${model.limpiar(resultado.renta)}"
            tvResTotalDescuentos.text = "$tagTotalDesc${model.limpiar(resultado.totalDescuentos)}"
            tvResSalarioNeto.text = "$tagNeto${model.limpiar(resultado.salarioN)}"

        }


    }

    private fun vibrarDispositivo() {
        val duracionMs = 400L

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {

            val vibratorManager =
                getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            val vibrator = vibratorManager.defaultVibrator
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    duracionMs,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        } else {
            // Versiones anteriores
            @Suppress("DEPRECATION")
            val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        duracionMs,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(duracionMs)
            }
        }
    }
}