package com.katarina.task4.ui.auth

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.katarina.task4.R
import com.katarina.task4.databinding.FragmentRegisterBinding
import com.katarina.task4.util.initToolbar
import com.katarina.task4.util.showBottomSheet
import androidx.navigation.fragment.findNavController
import com.katarina.task4.util.FirebaseHelper


class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar(binding.toolbar)
        initListener()
    }

    private fun initListener() {
        binding.buttonRegister.setOnClickListener {
            validateData()
        }
    }

    private fun validateData() {
        val email = binding.edittextEmail.text.toString().trim()
        val senha = binding.edittextSenha.text.toString().trim()

        if (email.isNotBlank()) {
            if (senha.isNotBlank()) {
                binding.progressBar.isVisible = true
                registerUser(email, senha)
            } else {
                showBottomSheet(message = getString(R.string.password_empty_register_fragment))
            }
        } else {
            showBottomSheet(message = getString(R.string.email_empty_register_fragment))
        }
    }

    private fun registerUser(email: String, password: String) {

        try {
            val auth = FirebaseAuth.getInstance()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        findNavController().navigate(R.id.action_global_homeFragment)
                    } else {
                        showBottomSheet(message = getString(FirebaseHelper.validError(task.exception?.message.toString())))
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
