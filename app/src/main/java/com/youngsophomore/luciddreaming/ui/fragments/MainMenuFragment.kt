package com.youngsophomore.luciddreaming.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.youngsophomore.luciddreaming.R
import com.youngsophomore.luciddreaming.ui.viewmodels.MainMenuViewModel

class MainMenuFragment : Fragment() {
    private val viewModel : MainMenuViewModel by viewModels()
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Debug_app", "in onCreate() in MainMenuFragment")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("Debug_app", "in onCreateView() in MainMenuFragment")
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main_menu, container, false)
        val btnDreamDetails = view.findViewById<Button>(R.id.btn_add_new_dream)
        val btnDreamsList = view.findViewById<Button>(R.id.btn_show_dreams_list)
        val btnSettings = view.findViewById<Button>(R.id.btn_settings)
        val cnstLaytMainMenu = view.findViewById<ConstraintLayout>(R.id.cnst_layt_main_menu)
        val constraintSet = ConstraintSet()
        constraintSet.clone(cnstLaytMainMenu)
        constraintSet.setHorizontalBias(btnDreamDetails.id, viewModel.curButtonsBiases[0])
        constraintSet.setVerticalBias(btnDreamDetails.id, viewModel.curButtonsBiases[1])
        constraintSet.setHorizontalBias(btnDreamsList.id, viewModel.curButtonsBiases[2])
        constraintSet.setVerticalBias(btnDreamsList.id, viewModel.curButtonsBiases[3])
        constraintSet.setHorizontalBias(btnSettings.id, viewModel.curButtonsBiases[4])
        constraintSet.setVerticalBias(btnSettings.id, viewModel.curButtonsBiases[5])
        constraintSet.applyTo(cnstLaytMainMenu)

        navController = findNavController()
        btnDreamsList.setOnClickListener {
            viewModel.setNewBiases()
            navController.navigate(R.id.action_main_menu_to_dreams_list)
        }
        btnDreamDetails.setOnClickListener {
            viewModel.setNewBiases()
            navController.navigate(R.id.action_main_menu_to_dream_details)
        }
        btnSettings.setOnClickListener {
            viewModel.setNewBiases()
            navController.navigate(R.id.action_main_menu_to_settings)
        }


        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MainMenuFragment()
    }
}