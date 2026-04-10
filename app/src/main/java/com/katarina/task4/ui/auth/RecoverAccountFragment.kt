package com.katarina.task4.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.katarina.task4.R
import com.katarina.task4.databinding.FragmentRecoverAccountBinding
import com.katarina.task4.util.initToolbar
import com.katarina.task4.util.showBottomSheet


class RecoverAccountFragment : Fragment() {

    private var _binding: FragmentRecoverAccountBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecoverAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(binding.toolbar)

        binding.btnSend.setOnClickListener { validateData() }
    }

    private fun validateData() {
        val email = binding.edittextEmail.text.toString().trim()

        if (email.isNotBlank()) {
            Toast.makeText(requireContext(), "Recuperação enviada!", Toast.LENGTH_SHORT).show()
        } else {
            showBottomSheet(message=R.string.email_empty)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
