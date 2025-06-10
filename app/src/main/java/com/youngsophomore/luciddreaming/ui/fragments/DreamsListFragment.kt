package com.youngsophomore.luciddreaming.ui.fragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.youngsophomore.luciddreaming.R
import com.youngsophomore.luciddreaming.data.model.Dream
import com.youngsophomore.luciddreaming.databinding.DialogConfirmActionBinding
import com.youngsophomore.luciddreaming.databinding.FragmentDreamDetailsBinding
import com.youngsophomore.luciddreaming.databinding.FragmentDreamsListBinding
import com.youngsophomore.luciddreaming.databinding.FragmentMainMenuBinding
import com.youngsophomore.luciddreaming.ui.adapters.DreamsListAdapter
import com.youngsophomore.luciddreaming.ui.interfaces.MetaItemAppendListener
import com.youngsophomore.luciddreaming.ui.viewmodels.DreamsListViewModel
import com.youngsophomore.luciddreaming.ui.viewmodels.LucidDreamingViewModel
import com.youngsophomore.luciddreaming.ui.viewmodels.MainMenuViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DreamsListFragment : Fragment(), MetaItemAppendListener, DreamsListAdapter.DreamClickListener {
    private val dreamsListVM : DreamsListViewModel by viewModels()
    private val lucidDreamingVM: LucidDreamingViewModel by activityViewModels()
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
        binding.tpDreamsListFilter.listener = this
        binding.tpDreamsListFilter.lucidDreamingVM = lucidDreamingVM
        binding.tpDreamsListFilter.fragmentManager = childFragmentManager
        dreamsListVM.initFeelingsAndLocations(R.id.ibtnDreamsListAddFeeling, R.id.ibtnDreamsListAddLocation)
        val adapter = DreamsListAdapter()
        adapter.listener = this
        binding.rvDreamsList.layoutManager = LinearLayoutManager(context)
        dreamsListVM
            //.fetchAllDreams()
            //.filteredDreams
            .allDreams
            .observe(viewLifecycleOwner) { dreams ->
                Log.d("Debug", "observe, dreams = ${dreams?.joinToString()}")
                adapter.setDreams(dreams)
            }
        binding.rvDreamsList.adapter = adapter
        binding.tpDreamsListFilter.dreamsAdapter = adapter
        binding.ibtnDreamsListCancelFilter.setOnClickListener {
            adapter.setDreams(dreamsListVM.allDreams.value!!)
        }
        //binding.mtnLaytDreamsList.isInteractionEnabled = true
        //binding.rvDreamsList.parent.requestDisallowInterceptTouchEvent(false)
        binding.rvDreamsList.setOnTouchListener { v, event ->
            v.getParent().requestDisallowInterceptTouchEvent(true)
            when (event?.action){
                MotionEvent.ACTION_DOWN -> {
                    Log.d("Gestures", "DreamsListFragment.rvDreamsList.setOnTouchListener, DOWN")
                    binding.mtnLaytDreamsList.transitionToState(R.id.dreamslist_toppanel_hidden, 100)
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
        binding.ibtnDreamsListAddNewDream.setOnClickListener {
            findNavController().navigate(R.id.action_dreams_list_to_dream_details)
        }

        return view
    }

    override fun onConfirmItem(item: String, isItemFeeling: Boolean) {
        Log.d("Gestures", "DreamDetailsFragment.onConfirmItem, $item")
        if (isItemFeeling)
            lucidDreamingVM.appendFeeling(item)
        else
            lucidDreamingVM.appendLocation(item)
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

    override fun onDreamClick(id: Int) {
        val action = DreamsListFragmentDirections.actionDreamsListToDreamDetails(dreamId = id)
        findNavController().navigate(action)
    }

    override fun onDreamLongClick(dream: Dream) {
        val dialog = Dialog(requireContext())
        val confirmActionBinding = DialogConfirmActionBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(confirmActionBinding.root)
        confirmActionBinding.apply {
            tvConfirmActionDialogTitle.text = "Подтвердить удаление"
            tvConfirmActionDialogAction.text = "Удалить сон из списка?"
            ibtnConfirmActionDialogConfirm.setOnClickListener {
                dreamsListVM.deleteDream(dream)
                dialog.dismiss()
            }
            ibtnConfirmActionDialogClose.setOnClickListener {
                dialog.dismiss()
            }
        }
        dialog.show()
    }
}