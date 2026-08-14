package com.example.ejerciciosdesafiopractico

import java.text.DecimalFormat

class PromedioModel {
    private val ponderaciones = listOf(0.10, 0.15, 0.20,0.25, 0.30)

    fun notaValida(nota: Double?): Boolean{
        return nota != null && nota in 0.0..10.0
    }
    fun calcularPromedioPonderado(notas:List<Double>): Double{
        var acumulado = 0.0
        for (i in notas.indices){
        acumulado += notas[i] * ponderaciones[i]
        }
        return acumulado
    }
    fun esAprobado(promedio: Double): Boolean{
        return promedio >= 6.0
    }

    fun formatoDecimal(numero: Double): String{
        val formato = DecimalFormat("0.00")
        return formato.format(numero)
    }

}