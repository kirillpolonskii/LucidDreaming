package com.youngsophomore.luciddreaming.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.youngsophomore.luciddreaming.R

class MainMenuFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main_menu, container, false)
        val btnDreamsList = view.findViewById<Button>(R.id.btn_show_dreams_list)
        val btnDreamDetails = view.findViewById<Button>(R.id.btn_add_new_dream)
        val btnSettings = view.findViewById<Button>(R.id.btn_settings)

        val navController = findNavController()
        btnDreamsList.setOnClickListener {
            navController.navigate(R.id.action_main_menu_to_dreams_list)
        }
        btnDreamDetails.setOnClickListener {
            navController.navigate(R.id.action_main_menu_to_dream_details)
        }
        btnSettings.setOnClickListener {
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