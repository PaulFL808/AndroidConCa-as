package com.example.asistencia.profesor.data

object RepositoryProvider {
    val sessionRepository: SessionRepository by lazy {
        FirebaseSessionRepository("https://android65-d7980-default-rtdb.firebaseio.com/")
    }
}
