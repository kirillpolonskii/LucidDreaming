package com.youngsophomore.luciddreaming.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import com.youngsophomore.luciddreaming.R

import com.youngsophomore.luciddreaming.databinding.FragmentDreamDetailsBinding
import com.youngsophomore.luciddreaming.ui.custom.MetaPanelCallback
import com.youngsophomore.luciddreaming.ui.interfaces.MetaItemAppendListener
import com.youngsophomore.luciddreaming.ui.viewmodels.DreamDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DreamDetailsFragment : Fragment(), MetaItemAppendListener {
    val viewModel : DreamDetailsViewModel by viewModels()
    private var _binding: FragmentDreamDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("Gestures", "DreamDetailsFragment.onCreateView")
        _binding = FragmentDreamDetailsBinding.inflate(inflater, container, false)
        binding.tpDreamDetailsMeta.listener = this
        val view = binding.root
        viewModel.initMoods()
        //viewModel.addDream()

        return view
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DreamDetailsFragment()
    }

    override fun onConfirmItem(item: String) {
        Log.d("Gestures", "DreamDetailsFragment.onConfirmItem, $item")
        viewModel.updateMoods(item)
    }
}