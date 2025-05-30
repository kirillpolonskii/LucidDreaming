package com.youngsophomore.luciddreaming.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.room.util.toSQLiteConnection
import com.youngsophomore.luciddreaming.R

import com.youngsophomore.luciddreaming.databinding.FragmentDreamDetailsBinding
import com.youngsophomore.luciddreaming.ui.interfaces.ConfirmActionListener
import com.youngsophomore.luciddreaming.ui.interfaces.MetaItemAppendListener
import com.youngsophomore.luciddreaming.ui.viewmodels.DreamDetailsViewModel
import com.youngsophomore.luciddreaming.ui.viewmodels.LucidDreamingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DreamDetailsFragment : Fragment(), MetaItemAppendListener {
    val viewModel : DreamDetailsViewModel by viewModels()
    private val lucidDreamingViewModel: LucidDreamingViewModel by activityViewModels()
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
        binding.tpDreamDetailsMeta.lucidDreamingVM = lucidDreamingViewModel
        viewModel.initFeelingsAndLocations(R.id.ibtnDreamDetailsAddFeeling, R.id.ibtnDreamDetailsAddLocation)
        viewModel.isDreamEditable.observe(viewLifecycleOwner) { isDreamEditable ->
            binding.ibtnDreamDetailsSaveEdit.setImageDrawable(AppCompatResources.getDrawable(requireContext(),
                if (isDreamEditable) R.drawable.all_save_24
            else R.drawable.all_edit_24))
        }
        binding.ibtnDreamDetailsSaveEdit.setOnClickListener {
            Log.d("Gestures", " ibtnDreamDetailSaveEdit.setOnClickListener")
            if (viewModel.isDreamEditable.value!!) {
                with(binding){
                    viewModel.addUpdateDream(
                        title = etDreamDetailsTitle.text.toString(),
                        content = etDreamDetailsContent.text.toString()
                    )
                }

            }
            viewModel.isDreamEditable.value = !viewModel.isDreamEditable.value!!
        }
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

    override fun onConfirmItem(item: String, isItemFeeling: Boolean) {
        Log.d("Gestures", "DreamDetailsFragment.onConfirmItem, $item")
        if (isItemFeeling)
            lucidDreamingViewModel.appendFeeling(item)
        else
            lucidDreamingViewModel.appendLocation(item)
    }


}