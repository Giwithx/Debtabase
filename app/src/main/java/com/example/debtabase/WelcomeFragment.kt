package com.example.debtabase

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.debtabase.databinding.FragmentHomeBinding
import com.example.debtabase.databinding.FragmentWelcomeBinding


class WelcomeFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentWelcomeBinding>(
            inflater, R.layout.fragment_welcome, container, false
        )
        binding.btnHome.setOnClickListener{ view: View ->
            view.findNavController().navigate(R.id.action_welcomeFragment2_to_homeFragment)
        }
        binding.btnCustomerReg.setOnClickListener{ view: View ->
            view.findNavController().navigate(R.id.action_welcomeFragment2_to_registerFragment)
        }
        return binding.root
    }

}
