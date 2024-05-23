package com.example.lokalin.ui.recycle

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.lokalin.R
import com.example.lokalin.databinding.FragmentProfileBinding
import com.example.lokalin.databinding.FragmentRecycleBinding

class RecycleFragment : Fragment() {

    private var _binding: FragmentRecycleBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRecycleBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.backButton.setOnClickListener(){
            findNavController().navigateUp()
        }

    return root
    }

}