package com.example.asistencia.profesor.data

data class Session(
    val id: String = "",
    val activa: Boolean = false,
    val codigo: String = "",
    val curso: String = "",
    val alumnos: Map<String, Student> = emptyMap()
)
