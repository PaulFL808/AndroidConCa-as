package com.example.asistencia.profesor.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.asistencia.profesor.data.RepositoryProvider
import com.example.asistencia.profesor.data.Session

class ActiveSessionViewModel : ViewModel() {
    private val repository = RepositoryProvider.sessionRepository

    fun getSession(sessionId: String): LiveData<Session?> {
        return repository.getSession(sessionId)
    }

    fun closeSession(sessionId: String, onComplete: () -> Unit) {
        repository.closeSession(sessionId, onComplete)
    }
}
