package com.example.asistenciaalumno

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ConfirmacionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Forzamos la ruta absoluta al layout
        setContentView(com.example.asistenciaalumno.R.layout.activity_confirmacion)

        // Usamos la ruta absoluta para los IDs
        val tvCurso = findViewById<TextView>(com.example.asistenciaalumno.R.id.tvCurso)
        val tvCodigo = findViewById<TextView>(com.example.asistenciaalumno.R.id.tvCodigo)
        val tvHora = findViewById<TextView>(com.example.asistenciaalumno.R.id.tvHora)
        val btnVolver = findViewById<Button>(com.example.asistenciaalumno.R.id.btnVolver)

        // Recuperamos los datos enviados por MainActivity
        val curso = intent.getStringExtra("CURSO") ?: ""
        val codigo = intent.getStringExtra("CODIGO") ?: ""
        val hora = intent.getStringExtra("HORA") ?: ""

        // Mostramos los datos en pantalla
        tvCurso.text = "Curso: $curso"
        tvCodigo.text = "Código: $codigo"
        tvHora.text = "Hora de registro: $hora"

        // Botón para cerrar esta pantalla y volver al registro
        btnVolver.setOnClickListener {
            finish()
        }
    }
}