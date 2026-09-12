package com.example.asistenciaalumno

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class InicioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.asistenciaalumno.R.layout.activity_inicio)

        // Conectamos el botón
        val btnIrARegistro = findViewById<Button>(com.example.asistenciaalumno.R.id.btnIrARegistro)

        // Al presionarlo, abrimos el formulario (MainActivity)
        btnIrARegistro.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}