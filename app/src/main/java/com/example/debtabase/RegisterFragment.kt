package com.example.debtabase

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.debtabase.databinding.FragmentRegisterBinding
import com.example.debtabase.databinding.FragmentWelcomeBinding


class RegisterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (activity as AppCompatActivity).supportActionBar?.title = "Registration"
        val binding = DataBindingUtil.inflate<FragmentRegisterBinding>(
            inflater, R.layout.fragment_register, container, false
        )
        binding.btnSave.setOnClickListener{ view: View ->
            val firstIn = binding.txtFN.text.toString()
            val lastIn = binding.txtLN.text.toString()
            val contactIn = binding.txtPhoneNum.text.toString()
            if (firstIn.isNotEmpty() && lastIn.isNotEmpty() && contactIn.isNotEmpty()){
                Toast.makeText(this@RegisterFragment.requireActivity(), "Registered Successfully", Toast.LENGTH_SHORT).show()
                view.findNavController().navigate(R.id.action_registerFragment_to_homeFragment)
            }
            if (firstIn.isEmpty() && lastIn.isNotEmpty() && contactIn.isNotEmpty()){
                Toast.makeText(this@RegisterFragment.requireActivity(), "Please Input First Name", Toast.LENGTH_SHORT).show()
            }
            if(firstIn.isNotEmpty() && lastIn.isEmpty() && contactIn.isNotEmpty()){
                Toast.makeText(this@RegisterFragment.requireActivity(), "Please Input Last Name", Toast.LENGTH_SHORT).show()
            }
            if(firstIn.isNotEmpty() && lastIn.isNotEmpty() && contactIn.isEmpty()){
                Toast.makeText(this@RegisterFragment.requireActivity(), "Please Input Contact Number", Toast.LENGTH_SHORT).show()
            }
            if(firstIn.isEmpty() || lastIn.isEmpty() || contactIn.isEmpty()){
                Toast.makeText(this@RegisterFragment.requireActivity(), "Incorrect Credentials!", Toast.LENGTH_SHORT).show()
            }

        }
        binding.btnCancelReg.setOnClickListener{ view: View ->
            view.findNavController().navigate(R.id.action_registerFragment_to_welcomeFragment2)
        }
        return binding.root
    }

}
