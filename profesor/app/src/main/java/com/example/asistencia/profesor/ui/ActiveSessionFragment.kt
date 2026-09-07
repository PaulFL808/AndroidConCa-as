package com.example.asistencia.profesor.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.asistencia.profesor.databinding.FragmentActiveSessionBinding

class ActiveSessionFragment : Fragment() {

    private var _binding: FragmentActiveSessionBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ActiveSessionViewModel by viewModels()
    private lateinit var adapter: StudentsAdapter
    private var sessionId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActiveSessionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        sessionId = arguments?.getString("sessionId")
        if (sessionId == null) {
            findNavController().navigateUp()
            return
        }

        setupRecyclerView()

        viewModel.getSession(sessionId!!).observe(viewLifecycleOwner) { session ->
            if (session != null) {
                binding.tvCourseName.text = session.curso
                binding.tvSessionCode.text = session.codigo
                
                val studentsList = session.alumnos.values.toList()
                binding.tvStudentsCount.text = getString(com.example.asistencia.profesor.R.string.students_count, studentsList.size)
                adapter.submitList(studentsList)
                
                if (!session.activa) {
                    binding.btnCloseSession.isEnabled = false
                    binding.btnCloseSession.text = "Sesión Finalizada"
                }
            }
        }

        binding.btnCloseSession.setOnClickListener {
            viewModel.closeSession(sessionId!!) {
                Toast.makeText(context, "Sesión cerrada", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = StudentsAdapter()
        binding.rvStudents.layoutManager = LinearLayoutManager(context)
        binding.rvStudents.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
