package com.youngsophomore.luciddreaming.ui.fragments

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.youngsophomore.luciddreaming.R
import com.youngsophomore.luciddreaming.databinding.DialogEnterPasswordBinding
import com.youngsophomore.luciddreaming.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }


    fun showDialogSetPassword(){
        val dialog = Dialog(requireContext())
        val setPasswordBinding = DialogEnterPasswordBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(setPasswordBinding.root)

        setPasswordBinding.ibtnSettingsConfirm.setOnClickListener {
            // показать диалоговое окно подтверждения, но это позже

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