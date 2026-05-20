package com.katarina.task4.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.katarina.task4.R
import com.katarina.task4.data.model.Status
import com.katarina.task4.data.model.Task
import com.katarina.task4.databinding.FragmentFormTaskBinding
import com.katarina.task4.util.initToolbar
import com.katarina.task4.util.showBottomSheet
import java.text.Normalizer


class FormTaskFragment : Fragment() {

    private var _binding: FragmentFormTaskBinding? = null
    private val binding get() = _binding!!

    private lateinit var task: Task
    private var newTask: Boolean = true
    private var status: Status = Status.TODO
    private lateinit var reference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    private val viewModel: TaskViewModel by activityViewModels()
    private val args: FormTaskFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFormTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(binding.toolbar)

        reference = Firebase.database.reference
        auth = FirebaseAuth.getInstance()

        // Log para debugar
        android.util.Log.d("FormTask", "currentUser no onViewCreated: ${auth.currentUser}")
        android.util.Log.d("FormTask", "currentUser UID: ${auth.currentUser?.uid}")
        android.util.Log.d("FormTask", "currentUser email: ${auth.currentUser?.email}")

        getArgs()
        initListener()
    }
    private fun getArgs(){
        args.task.let {
            if(it != null){
                this.task = it
                configTask()
            }
        }

    }
    private fun configTask(){
        newTask = false
        status = task.status
        binding.textToolbar.setText(R.string.text_toolbar_form_task_fragment)
        binding.editTextDescricao.setText(task.description)
        setStatus()
    }
    private fun setStatus(){
        val id = when(task.status){
            Status.TODO -> R.id.rbTodo
            Status.DOING -> R.id.rbDoing
            else -> R.id.rbDone

        }
        binding.radioGroup.check(id)
    }

    private fun initListener(){
        binding.buttonSave.setOnClickListener {
            validateData()
        }

        //Evento que monitora a mudança de escolha do radioGroup
        binding.radioGroup.setOnCheckedChangeListener { _, id ->
            status = when(id){
                R.id.rbTodo -> Status.TODO
                R.id.rbDoing -> Status.DOING
                else -> Status.DONE
            }
        }
    }

    private fun validateData(){
        val description = binding.editTextDescricao.text.toString().trim()

        if (description.isNotBlank()){
            binding.progressBar.isVisible = true

            if (newTask) {
                task = Task()
                task.id = reference.database.reference.push().key ?: ""

            }
            task.description = description
            task.status = status

            saveTask()
        }else{
            showBottomSheet(message = getString(R.string.description_empty_form_task_fragment))
        }
    }
    private fun saveTask(){
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        android.util.Log.d("FormTask", "UID direto: $uid")

        if (uid == null) {
            binding.progressBar.isVisible = false
            Toast.makeText(requireContext(), "Usuário não logado!", Toast.LENGTH_LONG).show()
            return
        }

        android.util.Log.d("FormTask", "UID ao salvar: $uid")
        android.util.Log.d("FormTask", "Task: ${task.id} - ${task.description}")

        reference
            .child("task")
            .child(uid)
            .child(task.id)
            .setValue(task).addOnCompleteListener { result ->
                if(result.isSuccessful){
                    android.util.Log.d("FormTask", "Salvo com sucesso!")
                    Toast.makeText(
                        requireContext(),
                        R.string.text_save_sucess_form_task_fragment,
                        Toast.LENGTH_SHORT
                    ).show()

                    if (newTask){
                        findNavController().popBackStack()
                    }else{
                        Toast.makeText(
                            requireContext(),
                            R.string.text_update_sucess_form_task_fragment,
                            Toast.LENGTH_SHORT

                        ).show()
                        viewModel.setUpdateTask(task)
                        binding.progressBar.isVisible = false
                    }
                } else{
                    binding.progressBar.isVisible = false
                    val errorMessage = result.exception?.message
                        ?: getString(R.string.error_generic)
                    showBottomSheet(message = getString(R.string.error_generic))
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
