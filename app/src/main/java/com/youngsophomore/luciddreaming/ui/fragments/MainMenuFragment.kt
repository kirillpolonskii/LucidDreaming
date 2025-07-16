package com.youngsophomore.luciddreaming.ui.fragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.youngsophomore.luciddreaming.R
import com.youngsophomore.luciddreaming.databinding.DialogEnterPasswordBinding
import com.youngsophomore.luciddreaming.databinding.FragmentMainMenuBinding
import com.youngsophomore.luciddreaming.ui.viewmodels.LucidDreamingViewModel
import com.youngsophomore.luciddreaming.ui.viewmodels.MainMenuViewModel
import com.youngsophomore.luciddreaming.utils.setBiases
import kotlinx.coroutines.launch

class MainMenuFragment : Fragment() {
    private val mainMenuVM : MainMenuViewModel by viewModels()
    private val lucidDreamingVM: LucidDreamingViewModel by activityViewModels()
    private lateinit var navController: NavController
    private var _binding: FragmentMainMenuBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainMenuBinding.inflate(inflater, container, false)
        val view = binding.root
        setBiasesForButtons()

        navController = findNavController()
        binding.ibtnMainMenuAddNewDream.setOnClickListener {
            mainMenuVM.setNewBiases()
            navController.navigate(R.id.action_main_menu_to_dream_details)
        }
        binding.ibtnMainMenuShowDreamsList.setOnClickListener {
            if (lucidDreamingVM.isPasswordEnabled) {
                showDialogEnterPassword()
            }
            else {
                navController.navigate(R.id.action_main_menu_to_dreams_list)
                mainMenuVM.setNewBiases()
            }
        }
        binding.ibtnMainMenuSettings.setOnClickListener {
            mainMenuVM.setNewBiases()
            navController.navigate(R.id.action_main_menu_to_settings)
        }

        return view
    }

    private fun setBiasesForButtons(){
        val constraintSet = ConstraintSet()
        constraintSet.setBiases(
            binding.cnstLaytMainMenu,
            mapOf(
                R.id.ibtnMainMenuAddNewDream to mainMenuVM.curButtonsBiases[0],
                R.id.ibtnMainMenuShowDreamsList to mainMenuVM.curButtonsBiases[1],
                R.id.ibtnMainMenuSettings to mainMenuVM.curButtonsBiases[2]
            )
        )
    }

    private fun showDialogEnterPassword(){
        val dialog = Dialog(requireContext())
        val setPasswordBinding = DialogEnterPasswordBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(setPasswordBinding.root)

        setPasswordBinding.ibtnSettingsConfirm.setOnClickListener {
            lifecycleScope.launch {
                if (lucidDreamingVM.checkPassword(setPasswordBinding.etSettingsPassword.text.toString())) {
                    navController.navigate(R.id.action_main_menu_to_dreams_list)
                    mainMenuVM.setNewBiases()
                }
                else {
                    Toast.makeText(context, "Wrong password", Toast.LENGTH_LONG).show()
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
        fun newInstance() =
            MainMenuFragment()
    }
}