package com.youngsophomore.luciddreaming.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.youngsophomore.luciddreaming.R
import com.youngsophomore.luciddreaming.databinding.FragmentMainMenuBinding
import com.youngsophomore.luciddreaming.ui.viewmodels.MainMenuViewModel
import com.youngsophomore.luciddreaming.utils.setBiases

class MainMenuFragment : Fragment() {
    private val viewModel : MainMenuViewModel by viewModels()
    private lateinit var navController: NavController
    private var _binding: FragmentMainMenuBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Debug_app", "in onCreate() in MainMenuFragment")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainMenuBinding.inflate(inflater, container, false)
        val view = binding.root
        setBiasesForButtons()

        navController = findNavController()
        binding.ibtnMainMenuAddNewDream.setOnClickListener {
            viewModel.setNewBiases()
            navController.navigate(R.id.action_main_menu_to_dream_details)
        }
        binding.ibtnMainMenuShowDreamsList.setOnClickListener {
            viewModel.setNewBiases()
            navController.navigate(R.id.action_main_menu_to_dreams_list)
        }
        binding.ibtnMainMenuSettings.setOnClickListener {
            viewModel.setNewBiases()
            navController.navigate(R.id.action_main_menu_to_settings)
        }


        return view
    }

    private fun setBiasesForButtons(){
        val constraintSet = ConstraintSet()
        constraintSet.setBiases(
            binding.cnstLaytMainMenu,
            mapOf(
                R.id.ibtnMainMenuAddNewDream to viewModel.curButtonsBiases[0],
                R.id.ibtnMainMenuShowDreamsList to viewModel.curButtonsBiases[1],
                R.id.ibtnMainMenuSettings to viewModel.curButtonsBiases[2]
            )
        )
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