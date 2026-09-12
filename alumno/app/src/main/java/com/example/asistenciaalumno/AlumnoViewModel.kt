package com.example.asistenciaalumno
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AlumnoViewModel(private val repository: AlumnoRepository = AlumnoRepository()) : ViewModel() {

    // _uiState es privado para que solo el ViewModel pueda modificarlo
    private val _uiState = MutableStateFlow<RegistroState>(RegistroState.Idle)
    // uiState es público para que la Activity lo lea
    val uiState: StateFlow<RegistroState> = _uiState

    fun registrar(idAlumno: String, nombre: String, codigo: String) {
        // 1. Avisamos a la pantalla que empiece a cargar
        _uiState.value = RegistroState.Loading

        // 2. viewModelScope.launch asegura que esto se ejecute fuera del hilo principal (RNF-01)
        viewModelScope.launch {
            // Llamamos al repositorio que creamos antes
            val resultado = repository.registrarAsistencia(idAlumno, nombre, codigo)

            // 3. Revisamos el resultado y actualizamos la pantalla
            when (resultado) {
                is RegistroResult.Exito -> _uiState.value = RegistroState.Success(resultado.curso, resultado.codigo, resultado.hora)
                is RegistroResult.Error -> _uiState.value = RegistroState.Error(resultado.mensaje)
            }
        }
    }

    // Función para limpiar el mensaje de error/éxito después de mostrarlo
    fun resetState() {
        _uiState.value = RegistroState.Idle
    }
}

// Estos son los estados posibles de nuestra pantalla
sealed class RegistroState {
    object Idle : RegistroState()
    object Loading : RegistroState()
    data class Success(val curso: String, val codigo: String, val hora: String) : RegistroState()
    data class Error(val mensaje: String) : RegistroState()
}