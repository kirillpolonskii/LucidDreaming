package com.youngsophomore.luciddreaming.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.room.util.toSQLiteConnection
import com.youngsophomore.luciddreaming.R
import com.youngsophomore.luciddreaming.data.model.Dream

import com.youngsophomore.luciddreaming.databinding.FragmentDreamDetailsBinding
import com.youngsophomore.luciddreaming.ui.interfaces.ConfirmActionListener
import com.youngsophomore.luciddreaming.ui.interfaces.MetaItemAppendListener
import com.youngsophomore.luciddreaming.ui.viewmodels.DreamDetailsViewModel
import com.youngsophomore.luciddreaming.ui.viewmodels.LucidDreamingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DreamDetailsFragment : Fragment(), MetaItemAppendListener, DreamDetailsViewModel.DreamArgsListener {
    val dreamDetailsVM : DreamDetailsViewModel by viewModels()
    private val lucidDreamingViewModel: LucidDreamingViewModel by activityViewModels()
    private var _binding: FragmentDreamDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        
        _binding = FragmentDreamDetailsBinding.inflate(inflater, container, false)
        binding.tpDreamDetailsMeta.listener = this
        val view = binding.root
        binding.tpDreamDetailsMeta.lucidDreamingVM = lucidDreamingViewModel
        dreamDetailsVM.initFeelingsAndLocations(R.id.ibtnDreamDetailsAddFeeling, R.id.ibtnDreamDetailsAddLocation)
        dreamDetailsVM.dreamListener = this
        dreamDetailsVM.isDreamEditable.observe(viewLifecycleOwner) { isDreamEditable ->
            binding.ibtnDreamDetailsSaveEdit.setImageDrawable(AppCompatResources.getDrawable(requireContext(),
                if (isDreamEditable) R.drawable.all_save_24
            else R.drawable.all_edit_24))
        }
        binding.ibtnDreamDetailsSaveEdit.setOnClickListener {
            
            if (dreamDetailsVM.isDreamEditable.value!!) {
                with(binding){
                    dreamDetailsVM.addUpdateDream(
                        title = etDreamDetailsTitle.text.toString(),
                        content = etDreamDetailsContent.text.toString()
                    )
                }

            }
            dreamDetailsVM.isDreamEditable.value = !dreamDetailsVM.isDreamEditable.value!!
        }
        binding.ibtnDreamDetailsBack.setOnClickListener {
            findNavController().navigateUp()
        }
        //dreamDetailsVM.addDream()


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
        
        if (isItemFeeling)
            lucidDreamingViewModel.appendFeeling(item)
        else
            lucidDreamingViewModel.appendLocation(item)
    }

    override fun onDreamCollected(dream: Dream) {
        
        val editableTitle = Editable.Factory.getInstance().newEditable(dream.title)
        binding.etDreamDetailsTitle.text = editableTitle
        val editableContent = Editable.Factory.getInstance().newEditable(dream.content)
        binding.etDreamDetailsContent.text = editableContent
        binding.tpDreamDetailsMeta.setDreamMeta(dream)
    }


}