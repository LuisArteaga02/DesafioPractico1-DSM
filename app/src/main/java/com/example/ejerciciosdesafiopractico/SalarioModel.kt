package com.example.ejerciciosdesafiopractico

import org.jetbrains.annotations.Debug
import java.text.DecimalFormat

class SalarioModel {

    data class ResultadoTotal(
        val salarioBruto: Double,
        val isss: Double,
        val afp: Double,
        val renta: Double,
        val totalDescuentos: Double,
        val salarioN: Double
    )
    fun validarSalario(salario: Double?): Boolean{
        return salario != null && salario > 0.0
    }

    fun calcularISSS(salarioBase: Double): Double{
        val isssCalculado = salarioBase * 0.03
        return if(isssCalculado > 30.0) 30.0 else isssCalculado
    }

    fun calcularAFP(salarioBase: Double): Double{
        return salarioBase * 0.0725
    }

    fun RentaCalc(SalarioG: Double): Double{
        return when{
            SalarioG <= 550.00 ->{
                0.0
            }
            SalarioG in 550.01..895.24->{
                ((SalarioG - 550.00) * 0.10) +17.67
            }
            SalarioG in 895.25..2038.10 ->{
                ((SalarioG - 895.24) * 0.20) + 60.00
            }else ->{
                ((SalarioG - 2038.10) * 0.30) + 288.57
            }
        }
    }
    fun calcularLiq(salarioBase: Double): ResultadoTotal{
        val isss = calcularISSS(salarioBase)
        val afp = calcularAFP(salarioBase)
        val SalarioG = salarioBase - isss - afp
        val renta = RentaCalc(SalarioG)
        val totalDescuentos = isss + afp + renta
        val salarioN = salarioBase - totalDescuentos

        return ResultadoTotal(
            salarioBruto = salarioBase,
            isss = isss,
            afp = afp,
            renta = renta,
            totalDescuentos = totalDescuentos,
            salarioN = salarioN
        )
    }

    fun limpiar(monto: Double): String{
        val df = DecimalFormat("0.00")
        return df.format(monto)
    }
}