package com.example.asistencia.profesor.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.asistencia.profesor.data.Session
import com.example.asistencia.profesor.databinding.ItemPastSessionBinding

class PastSessionsAdapter(
    private val onSessionClick: (String) -> Unit
) : RecyclerView.Adapter<PastSessionsAdapter.ViewHolder>() {

    private var sessions = listOf<Session>()

    fun setSessions(newSessions: List<Session>) {
        sessions = newSessions
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPastSessionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val session = sessions[position]
        holder.bind(session)
        holder.itemView.setOnClickListener {
            onSessionClick(session.id)
        }
    }

    override fun getItemCount(): Int = sessions.size

    class ViewHolder(private val binding: ItemPastSessionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(session: Session) {
            binding.tvCourseName.text = session.curso
            binding.tvSessionCode.text = "Código: ${session.codigo}"
            binding.tvStudentsCount.text = "${session.alumnos.size} alumnos"
        }
    }
}
