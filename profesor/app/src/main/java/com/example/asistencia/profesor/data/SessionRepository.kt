package com.example.asistencia.profesor.data

import androidx.lifecycle.LiveData

interface SessionRepository {
    fun createSession(curso: String, onSuccess: (String) -> Unit, onError: (Exception) -> Unit)
    fun closeSession(sessionId: String, onComplete: () -> Unit)
    fun getSession(sessionId: String): LiveData<Session?>
    fun getActiveSessionId(): String?
}
