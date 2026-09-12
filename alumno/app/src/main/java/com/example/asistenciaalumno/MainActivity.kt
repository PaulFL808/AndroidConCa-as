package com.example.asistenciaalumno

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: AlumnoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Forzamos la ruta absoluta al layout
        setContentView(com.example.asistenciaalumno.R.layout.activity_main)

        // 1. Inicializamos nuestro ViewModel
        viewModel = ViewModelProvider(this)[AlumnoViewModel::class.java]

        // 2. Conectamos los elementos visuales (XML) con la ruta absoluta
        val etIdentificador = findViewById<EditText>(com.example.asistenciaalumno.R.id.etIdentificador)
        val etNombre = findViewById<EditText>(com.example.asistenciaalumno.R.id.etNombre)
        val etCodigo = findViewById<EditText>(com.example.asistenciaalumno.R.id.etCodigo)
        val btnRegistrar = findViewById<Button>(com.example.asistenciaalumno.R.id.btnRegistrar)
        val progressBar = findViewById<ProgressBar>(com.example.asistenciaalumno.R.id.progressBar)

        // 3. ¿Qué pasa al hacer clic en el botón?
        btnRegistrar.setOnClickListener {
            val id = etIdentificador.text.toString().trim()
            val nombre = etNombre.text.toString().trim()
            val codigo = etCodigo.text.toString().trim()

            // -- VALIDACIONES OBLIGATORIAS (ERR-03, ERR-06) --
            // Validación de tu compañero: ID exacto de 10 caracteres
            if (id.length != 10) {
                etIdentificador.error = "El ID debe tener exactamente 10 caracteres"
                return@setOnClickListener // Corta la ejecución si hay error
            }
            if (nombre.isEmpty()) {
                etNombre.error = "El nombre es obligatorio"
                return@setOnClickListener
            }
            if (codigo.length != 4 || !codigo.all { it.isDigit() }) {
                etCodigo.error = "El código debe ser de 4 dígitos numéricos"
                return@setOnClickListener
            }

            // Si todo está bien, le mandamos los datos al ViewModel para ir a Firebase
            viewModel.registrar(id, nombre, codigo)
        }

        // 4. Observamos las respuestas del ViewModel
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state) {
                    is RegistroState.Idle -> {
                        progressBar.visibility = View.GONE
                        btnRegistrar.isEnabled = true
                    }
                    is RegistroState.Loading -> {
                        progressBar.visibility = View.VISIBLE
                        btnRegistrar.isEnabled = false // Desactiva el botón para evitar clics dobles
                    }
                    is RegistroState.Success -> {
                        progressBar.visibility = View.GONE
                        btnRegistrar.isEnabled = true

                        // RF-14, P-AL03: Navegamos a la pantalla de confirmación enviando los datos
                        val intent = Intent(this@MainActivity, ConfirmacionActivity::class.java).apply {
                            putExtra("CURSO", state.curso)
                            putExtra("CODIGO", state.codigo)
                            putExtra("HORA", state.hora)
                        }
                        startActivity(intent)

                        // Limpiamos los campos para cuando el usuario presione "Atrás"
                        etIdentificador.text.clear()
                        etNombre.text.clear()
                        etCodigo.text.clear()

                        viewModel.resetState()
                    }
                    is RegistroState.Error -> {
                        progressBar.visibility = View.GONE
                        btnRegistrar.isEnabled = true

                        // RF-17: Retroalimentación visual de errores de Firebase
                        Toast.makeText(this@MainActivity, state.mensaje, Toast.LENGTH_LONG).show()

                        viewModel.resetState()
                    }
                }
            }
        }
    }
}