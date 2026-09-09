package com.example.asistencia.profesor.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import kotlin.random.Random

class MockSessionRepository : SessionRepository {
    private val activeSessionData = MutableLiveData<Session?>()
    private var currentSessionId: String? = null

    override fun createSession(
        curso: String,
        onSuccess: (String) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val id = UUID.randomUUID().toString()
        val code = String.format("%04d", Random.nextInt(10000))
        val session = Session(
            id = id,
            activa = true,
            codigo = code,
            curso = curso
        )
        currentSessionId = id
        activeSessionData.postValue(session)
        onSuccess(id)
        
        // Simular alumnos que se conectan de a poco para probar la UI
        simulateStudentsJoining()
    }

    override fun closeSession(sessionId: String, onComplete: () -> Unit) {
        val currentSession = activeSessionData.value
        if (currentSession != null && currentSession.id == sessionId) {
            val updated = currentSession.copy(activa = false)
            activeSessionData.postValue(updated)
            currentSessionId = null
        }
        onComplete()
    }

    override fun getSession(sessionId: String): LiveData<Session?> {
        return activeSessionData
    }

    override fun getActiveSessionId(): String? {
        return currentSessionId
    }

    override fun getPastSessions(): LiveData<List<Session>> {
        val liveData = MutableLiveData<List<Session>>()
        liveData.value = emptyList() // Mock doesn't store past sessions for now
        return liveData
    }
    
    private fun simulateStudentsJoining() {
        Thread {
            try {
                val names = listOf("Juan Pérez", "María Silva", "Carlos Rojas", "Ana Gómez")
                for (i in names.indices) {
                    Thread.sleep(2000)
                    val session = activeSessionData.value
                    if (session != null && session.activa) {
                        val newStudent = Student(
                            id = "A00${i+1}",
                            nombre = names[i],
                            horaRegistro = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
                        )
                        val updatedStudents = session.alumnos.toMutableMap()
                        updatedStudents[newStudent.id] = newStudent
                        activeSessionData.postValue(session.copy(alumnos = updatedStudents))
                    } else {
                        break
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }
}
