package com.youngsophomore.luciddreaming.ui.fragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.youngsophomore.luciddreaming.databinding.DialogEnterPasswordBinding
import com.youngsophomore.luciddreaming.databinding.FragmentSettingsBinding
import com.youngsophomore.luciddreaming.ui.viewmodels.LucidDreamingViewModel

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
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.swchSettingsPassword.setOnCheckedChangeListener { buttonView, isChecked ->
            Log.d("Debug", "swchSettingsPassword.setOnCheckedChangeListener")
            if (isChecked) {
                showDialogSetPassword()
            }
            else {
                // TODO: добавить проверку пароля перед его снятием (т.к. для захода в настройки пароль не нужен)

            }
        }
    }


    private fun showDialogSetPassword(){
        val dialog = Dialog(requireContext())
        val setPasswordBinding = DialogEnterPasswordBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(setPasswordBinding.root)

        setPasswordBinding.ibtnSettingsConfirm.setOnClickListener {
            // показать диалоговое окно подтверждения, но это позже
            lucidDreamingVM.setPassword(setPasswordBinding.etSettingsPassword.text.toString())
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