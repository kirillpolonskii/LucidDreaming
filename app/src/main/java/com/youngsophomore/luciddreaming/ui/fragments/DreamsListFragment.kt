package com.youngsophomore.luciddreaming.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.youngsophomore.luciddreaming.R
import com.youngsophomore.luciddreaming.ui.viewmodels.DreamsListViewModel
import com.youngsophomore.luciddreaming.ui.viewmodels.MainMenuViewModel

class DreamsListFragment : Fragment() {
    private val viewModel : DreamsListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dreams_list, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DreamsListFragment()
    }
}