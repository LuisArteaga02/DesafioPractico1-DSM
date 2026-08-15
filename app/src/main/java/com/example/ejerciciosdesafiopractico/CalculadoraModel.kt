package com.example.ejerciciosdesafiopractico

import java.text.DecimalFormat
import kotlin.math.pow
import kotlin.math.sqrt

class CalculadoraModel {

    fun sumar(a: Double, b: Double): Double = a + b

    fun restar(a: Double, b: Double): Double = a - b

    fun multiplicar(a: Double, b: Double): Double = a * b

    fun dividir(a: Double, b: Double): Double? {
        if (b == 0.0) return null
        return a / b
    }

    fun exponente(base: Double, exp: Double): Double {
        return base.pow(exp)
    }

    fun raizCuadrada(a: Double): Double? {
        if (a < 0.0) return null 
        return sqrt(a)
    }


    fun formatearResultado(valor: Double): String {
        val df = DecimalFormat("#.####")
        return df.format(valor)
    }

}