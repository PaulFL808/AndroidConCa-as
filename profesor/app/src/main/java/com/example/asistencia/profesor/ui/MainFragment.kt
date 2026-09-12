package com.example.asistencia.profesor.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.asistencia.profesor.databinding.FragmentMainBinding

import androidx.recyclerview.widget.LinearLayoutManager

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()
    private lateinit var adapter: PastSessionsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        adapter = PastSessionsAdapter { sessionId ->
            val bundle = Bundle().apply { putString("sessionId", sessionId) }
            findNavController().navigate(
                com.example.asistencia.profesor.R.id.action_mainFragment_to_activeSessionFragment,
                bundle
            )
        }
        
        binding.rvPastSessions.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPastSessions.adapter = adapter
        
        viewModel.pastSessions.observe(viewLifecycleOwner) { sessions ->
            adapter.setSessions(sessions)
        }
        
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
        
        binding.btnLogout.setOnClickListener {
            findNavController().navigate(com.example.asistencia.profesor.R.id.action_mainFragment_to_loginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
