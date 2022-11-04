package com.example.debtabase

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.debtabase.databinding.FragmentSMSBinding


class SMSFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (activity as AppCompatActivity).supportActionBar?.title = "Send SMS"
        val binding = DataBindingUtil.inflate<FragmentSMSBinding>(
            inflater, R.layout.fragment_s_m_s, container, false
        )
        binding.btnCancelSend.setOnClickListener{ view: View ->
            view.findNavController().navigate(R.id.action_SMSFragment_to_homeFragment)
        }
        return binding.root
    }

}
