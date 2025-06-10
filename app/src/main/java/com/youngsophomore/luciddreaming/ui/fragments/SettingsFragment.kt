package com.youngsophomore.luciddreaming.ui.fragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.youngsophomore.luciddreaming.R
import com.youngsophomore.luciddreaming.databinding.DialogEnterPasswordBinding
import com.youngsophomore.luciddreaming.databinding.FragmentSettingsBinding
import com.youngsophomore.luciddreaming.ui.viewmodels.LucidDreamingViewModel
import kotlinx.coroutines.launch

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val lucidDreamingVM: LucidDreamingViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        binding.swchSettingsPassword.isChecked = lucidDreamingVM.isPasswordEnabled
        binding.ibtnSettingsBack.setOnClickListener {
            findNavController().navigateUp()
        }

        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.swchSettingsPassword.setOnClickListener {
            Log.d("Debug", "swchSettingsPassword.setOnCheckedChangeListener")
            showDialogSetPassword(binding.swchSettingsPassword.isChecked)
        }
    }


    private fun showDialogSetPassword(isSwitchChecked: Boolean){
        val dialog = Dialog(requireContext())
        val setPasswordBinding = DialogEnterPasswordBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(setPasswordBinding.root)

        setPasswordBinding.ibtnSettingsConfirm.setOnClickListener {
            if (isSwitchChecked) {
                // показать диалоговое окно подтверждения, но это позже
                lucidDreamingVM.setPassword(setPasswordBinding.etSettingsPassword.text.toString())
            }
            else {
                lifecycleScope.launch {
                    if (lucidDreamingVM.checkPassword(setPasswordBinding.etSettingsPassword.text.toString())) {
                        lucidDreamingVM.disablePassword()
                    } else {
                        Toast.makeText(context, "Wrong password", Toast.LENGTH_LONG).show()
                        binding.swchSettingsPassword.isChecked = true
                    }
                }
            }

            dialog.dismiss()
        }
        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SettingsFragment()
    }
}