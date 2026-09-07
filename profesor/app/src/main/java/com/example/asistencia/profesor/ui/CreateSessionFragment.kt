package com.example.asistencia.profesor.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.asistencia.profesor.databinding.FragmentCreateSessionBinding

class CreateSessionFragment : Fragment() {

    private var _binding: FragmentCreateSessionBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CreateSessionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateSessionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCancel.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnCreate.setOnClickListener {
            val curso = binding.etCourseName.text.toString()
            viewModel.createSession(
                curso = curso,
                onSuccess = { sessionId ->
                    Toast.makeText(context, "Sesión creada exitosamente", Toast.LENGTH_SHORT).show()
                    val bundle = Bundle().apply { putString("sessionId", sessionId) }
                    findNavController().navigate(
                        com.example.asistencia.profesor.R.id.action_createSessionFragment_to_activeSessionFragment,
                        bundle
                    )
                },
                onError = { e ->
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
