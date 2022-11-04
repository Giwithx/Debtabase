package com.example.debtabase

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.debtabase.databinding.FragmentHomeBinding
import com.example.debtabase.databinding.FragmentProductsBinding


class ProductsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (activity as AppCompatActivity).supportActionBar?.title = "List a Debt"
        val binding = DataBindingUtil.inflate<FragmentProductsBinding>(
            inflater, R.layout.fragment_products, container, false
        )
        binding.btnCancelProd.setOnClickListener{ view: View ->
            view.findNavController().navigate(R.id.action_productsFragment2_to_homeFragment)
        }
        return binding.root
    }

}
