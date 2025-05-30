package com.youngsophomore.luciddreaming.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.youngsophomore.luciddreaming.R
import com.youngsophomore.luciddreaming.databinding.FragmentDreamDetailsBinding
import com.youngsophomore.luciddreaming.databinding.FragmentDreamsListBinding
import com.youngsophomore.luciddreaming.databinding.FragmentMainMenuBinding
import com.youngsophomore.luciddreaming.ui.adapters.DreamsListAdapter
import com.youngsophomore.luciddreaming.ui.viewmodels.DreamsListViewModel
import com.youngsophomore.luciddreaming.ui.viewmodels.MainMenuViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DreamsListFragment : Fragment() {
    private val dreamsListVM : DreamsListViewModel by viewModels()
    private var _binding: FragmentDreamsListBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDreamsListBinding.inflate(inflater, container, false)
        val view = binding.root
        val adapter = DreamsListAdapter()
        binding.rvDreamsList.layoutManager = LinearLayoutManager(context)
        dreamsListVM
            //.fetchAllDreams()
            .allDreams
            .observe(viewLifecycleOwner) { dreams ->
                Log.d("Debug", "observe, dreams = ${dreams.joinToString()}")
                adapter.setDreams(dreams)
            }
        binding.rvDreamsList.adapter = adapter
        //binding.mtnLaytDreamsList.isInteractionEnabled = true
        //binding.rvDreamsList.parent.requestDisallowInterceptTouchEvent(false)
        binding.rvDreamsList.setOnTouchListener { v, event ->
            v.getParent().requestDisallowInterceptTouchEvent(true)
            when (event?.action){
                MotionEvent.ACTION_DOWN -> {
                    Log.d("Gestures", "DreamsListFragment.rvDreamsList.setOnTouchListener, DOWN")

                    v.onTouchEvent(event)
                    //false
                    //true // - так rv не двигался
                }
                MotionEvent.ACTION_MOVE -> {
                    Log.d("Gestures", "DreamsListFragment.rvDreamsList.setOnTouchListener, MOVE")
                    v.onTouchEvent(event)
                    //false
                    //true //- так rv не двигался
                }
                MotionEvent.ACTION_UP -> {
                    Log.d("Gestures", "DreamsListFragment.rvDreamsList.setOnTouchListener, UP")
                    v.onTouchEvent(event)

                    //false
                    //true //- так rv не двигался
                }
                else -> {
                    Log.d("Gestures", "DreamsListFragment.rvDreamsList.setOnTouchListener, else")
                    v.onTouchEvent(event)

                    //false
                    //true //- так rv не двигался
                }
            }
        }

        return view
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DreamsListFragment()
    }
}