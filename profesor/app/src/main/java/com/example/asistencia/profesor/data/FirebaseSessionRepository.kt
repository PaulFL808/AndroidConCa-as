package com.example.asistencia.profesor.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.UUID
import kotlin.random.Random

class FirebaseSessionRepository(databaseUrl: String) : SessionRepository {

    private val database = FirebaseDatabase.getInstance(databaseUrl).reference
    private var currentSessionId: String? = null
    
    // Almacena el listener actual para poder removerlo
    private var activeSessionListener: ValueEventListener? = null
    private var activeSessionRef: com.google.firebase.database.DatabaseReference? = null

    override fun createSession(
        curso: String,
        onSuccess: (String) -> Unit,
        onError: (Exception) -> Unit
    ) {
        val id = UUID.randomUUID().toString()
        val code = String.format("%04d", Random.nextInt(10000))
        
        val sessionData = mapOf(
            "activa" to true,
            "codigo" to code,
            "curso" to curso
        )

        database.child("sesiones").child(id).setValue(sessionData)
            .addOnSuccessListener {
                currentSessionId = id
                onSuccess(id)
            }
            .addOnFailureListener { e ->
                onError(e)
            }
    }

    override fun closeSession(sessionId: String, onComplete: () -> Unit) {
        database.child("sesiones").child(sessionId).child("activa").setValue(false)
            .addOnCompleteListener {
                currentSessionId = null
                onComplete()
            }
    }

    override fun getSession(sessionId: String): LiveData<Session?> {
        val liveData = MutableLiveData<Session?>()
        
        // Limpiamos listener anterior si existía
        activeSessionListener?.let { activeSessionRef?.removeEventListener(it) }
        
        activeSessionRef = database.child("sesiones").child(sessionId)
        activeSessionListener = activeSessionRef!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val activa = snapshot.child("activa").getValue(Boolean::class.java) ?: false
                    val codigo = snapshot.child("codigo").getValue(String::class.java) ?: ""
                    val curso = snapshot.child("curso").getValue(String::class.java) ?: ""
                    
                    val alumnosMap = mutableMapOf<String, Student>()
                    val alumnosSnapshot = snapshot.child("alumnos")
                    for (child in alumnosSnapshot.children) {
                        val id = child.key ?: continue
                        val nombre = child.child("nombre").getValue(String::class.java) ?: ""
                        val horaRegistro = child.child("horaRegistro").getValue(String::class.java) ?: ""
                        alumnosMap[id] = Student(id, nombre, horaRegistro)
                    }
                    
                    val session = Session(sessionId, activa, codigo, curso, alumnosMap)
                    liveData.postValue(session)
                } else {
                    liveData.postValue(null)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejo de error si es necesario
            }
        })

        return liveData
    }

    override fun getActiveSessionId(): String? {
        return currentSessionId
    }
}
