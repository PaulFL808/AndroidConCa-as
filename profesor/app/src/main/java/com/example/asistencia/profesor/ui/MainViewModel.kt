package com.example.asistencia.profesor.ui

import androidx.lifecycle.ViewModel
import com.example.asistencia.profesor.data.RepositoryProvider

class MainViewModel : ViewModel() {
    private val repository = RepositoryProvider.sessionRepository

    val pastSessions = repository.getPastSessions()
    
    fun getActiveSessionId(): String? {
        return repository.getActiveSessionId()
    }
}
