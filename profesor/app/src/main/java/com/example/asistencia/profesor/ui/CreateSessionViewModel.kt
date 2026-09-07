package com.example.asistencia.profesor.ui

import androidx.lifecycle.ViewModel
import com.example.asistencia.profesor.data.RepositoryProvider

class CreateSessionViewModel : ViewModel() {
    private val repository = RepositoryProvider.sessionRepository

    fun createSession(curso: String, onSuccess: (String) -> Unit, onError: (Exception) -> Unit) {
        if (curso.isBlank() || curso.length !in 3..60) {
            onError(Exception("El curso debe tener entre 3 y 60 caracteres"))
            return
        }
        repository.createSession(curso, onSuccess, onError)
    }
}
