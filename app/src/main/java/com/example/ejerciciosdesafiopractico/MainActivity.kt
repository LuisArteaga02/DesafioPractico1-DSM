package com.example.ejerciciosdesafiopractico

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent


class MainActivity : AppCompatActivity() {
    private lateinit var btnJ1 : Button
    private lateinit var btnJ2 : Button
    private lateinit var btnJ3 : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        btnJ1 = findViewById<Button>(R.id.btnJ1)
        btnJ2 = findViewById<Button>(R.id.btnJ2)
        btnJ3 = findViewById<Button>(R.id.btnJ3)

        btnJ1.setOnClickListener {
            val intent = Intent(this, PromedioActivity::class.java)
            startActivity(intent)
        }
        btnJ2.setOnClickListener {
            val intent = Intent(this, SalarioActivity::class.java)
            startActivity(intent)
        }
        btnJ3.setOnClickListener {
            val intent = Intent(this, CalculadoraActivity::class.java)
            startActivity(intent)
        }





    }
}