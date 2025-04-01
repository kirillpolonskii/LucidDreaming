package com.youngsophomore.luciddreaming.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.youngsophomore.luciddreaming.R
import com.youngsophomore.luciddreaming.data.local.DreamDatabase
import com.youngsophomore.luciddreaming.data.repository.DreamRepository
import com.youngsophomore.luciddreaming.ui.viewmodels.DreamDetailsViewModel
import com.youngsophomore.luciddreaming.ui.viewmodels.MainMenuViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DreamDetailsFragment : Fragment() {
    private val viewModel : DreamDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel.addDream()
        return inflater.inflate(R.layout.fragment_dream_details, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DreamDetailsFragment()
    }
}