package com.katarina.task4.ui.auth

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.katarina.task4.R
import com.katarina.task4.databinding.FragmentLoginBinding
import com.katarina.task4.util.showBottomSheet

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? =null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

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

        auth = FirebaseAuth.getInstance()
        initListener()
    }
    private fun initListener() {
        binding.buttonLogin.setOnClickListener {
            validateData() // Chama a função que verifica os campos
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
                binding.progressBar.isVisible = true
                loginUser(email, senha) // ← chama o login de verdade!
            } else {
                showBottomSheet(message = getString(R.string.password_empty))
            }
        } else {
            showBottomSheet(message = getString(R.string.email_empty))
        }
    }
    private fun loginUser(email: String, password: String) {
        try {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Conseguiu autenticar com sucesso
                        findNavController().navigate(R.id.action_global_homeFragment)
                    } else {
                        // Ocorreu falha na autenticação
                        binding.progressBar.isVisible = false
                        Toast.makeText(
                            requireContext(),
                            task.exception?.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        } catch (e: Exception) {
            Toast.makeText(
                requireContext(),
                e.message.toString(),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
