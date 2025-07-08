package com.example.sampatika.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sampatika.R
import com.example.sampatika.databinding.FragmentMarketBinding

class MarketFragment : Fragment() {

    private lateinit var binding : FragmentMarketBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMarketBinding.inflate(layoutInflater)
        return binding.root
    }

}