package com.example.asistenciaalumno

import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AlumnoRepository {

    // Referencia a la raíz de la base de datos RTDB
    private val database = FirebaseDatabase.getInstance().reference

    suspend fun registrarAsistencia(idAlumno: String, nombre: String, codigoStr: String): RegistroResult {
        return try {
            // 1. Buscar la sesión por el código (RF-05, RF-08)
            val sesionQuery = database.child("sesiones")
                .orderByChild("codigo")
                .equalTo(codigoStr)
                .get()
                .await()

            // ERR-01: Código no existe
            if (!sesionQuery.exists() || sesionQuery.children.count() == 0) {
                return RegistroResult.Error("Sesión no encontrada")
            }

            val sesionSnapshot = sesionQuery.children.first()

            // Obtenemos el nombre del curso directamente de Firebase
            val curso = sesionSnapshot.child("curso").getValue(String::class.java) ?: "Curso desconocido"

            // Verificamos que la sesión esté activa (activa == true)
            val activa = sesionSnapshot.child("activa").getValue(Boolean::class.java) ?: false
            val sesionId = sesionSnapshot.key ?: return RegistroResult.Error("Error interno: ID de sesión nulo")

            // ERR-02 / RF-05: Si no está activa, mostramos error
            if (!activa) {
                return RegistroResult.Error("La sesión está cerrada")
            }

            // RF-15 / ERR-05: Evitar duplicados revisando si el ID ya existe como llave
            val alumnoSnapshot = database.child("sesiones").child(sesionId)
                .child("alumnos").child(idAlumno)
                .get()
                .await()

            if (alumnoSnapshot.exists()) {
                return RegistroResult.Error("Ya registró asistencia en esta sesión")
            }

            // 2. Preparar los datos exactos en formato ISO 8601
            val formatoHora = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
            val horaActual = formatoHora.format(Date())

            val datosAlumno = mapOf(
                "id" to idAlumno,
                "nombre" to nombre,
                "horaRegistro" to horaActual
            )

            // 3. Escribir en la ruta: sesiones/{sessionId}/alumnos/{idAlumno}
            database.child("sesiones").child(sesionId)
                .child("alumnos").child(idAlumno)
                .setValue(datosAlumno)
                .await()

            // Retornamos el éxito incluyendo los datos para la pantalla de confirmación
            RegistroResult.Exito(curso = curso, codigo = codigoStr, hora = horaActual)

        } catch (e: Exception) {
            // ERR-04: RTDB sin conexión u otros errores
            RegistroResult.Error("Fallo: ${e.message}")
        }
    }
}

// Estados de respuesta actualizados
sealed class RegistroResult {
    data class Exito(val curso: String, val codigo: String, val hora: String) : RegistroResult()
    data class Error(val mensaje: String) : RegistroResult()
}