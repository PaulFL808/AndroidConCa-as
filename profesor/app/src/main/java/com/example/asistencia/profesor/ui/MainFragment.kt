package com.example.asistencia.profesor.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.asistencia.profesor.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        binding.fabNewSession.setOnClickListener {
            val activeId = viewModel.getActiveSessionId()
            if (activeId != null) {
                val bundle = Bundle().apply { putString("sessionId", activeId) }
                findNavController().navigate(
                    com.example.asistencia.profesor.R.id.action_mainFragment_to_activeSessionFragment, 
                    bundle
                )
            } else {
                findNavController().navigate(com.example.asistencia.profesor.R.id.action_mainFragment_to_createSessionFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
