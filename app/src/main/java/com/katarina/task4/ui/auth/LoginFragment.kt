package com.katarina.task4.ui.auth

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.katarina.task4.R
import com.katarina.task4.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? =null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
    }
    private fun initListener() {

        binding.buttonLogin.setOnClickListener {
            findNavController().navigate(R.id.action_global_homeFragment)
        }

        binding.btnRegistrar.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.btnRecover.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_recoverAccountFragment)
        }
    }
    private fun validateData() {
        val email = binding.edittextEmail.text.toString().trim()
        val senha = binding.edittextSenha.text.toString().trim()

        if (email.isNotBlank()) {
            if (senha.isNotBlank()) {
                // Comentário temporário somente para testar a validação dos dados
                findNavController().navigate(R.id.action_global_homeFragment)
            } else {
                Toast.makeText(requireContext(), "Preencha a senha!", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "Preencha seu email!", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
