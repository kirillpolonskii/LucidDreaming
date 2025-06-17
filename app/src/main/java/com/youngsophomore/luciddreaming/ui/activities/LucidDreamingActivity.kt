package com.youngsophomore.luciddreaming.ui.activities

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.youngsophomore.luciddreaming.R
import com.youngsophomore.luciddreaming.databinding.ActivityLucidDreamingBinding
import com.youngsophomore.luciddreaming.ui.viewmodels.LucidDreamingViewModel
import com.youngsophomore.luciddreaming.ui.viewmodels.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LucidDreamingActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityLucidDreamingBinding
    private val lucidDreamingVM: LucidDreamingViewModel by viewModels()
    private val settingsVM: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLucidDreamingBinding.inflate(layoutInflater)
        val root = binding.root
        setContentView(root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.cnstLaytActivity) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.frgtActivityNavHost) as NavHostFragment
        navController = navHostFragment.navController
        lucidDreamingVM.initFromPrefs()
        lucidDreamingVM.initSettings()
        //Log.d("Gestures", " lucidDreamingVM = ${lucidDreamingVM}")
    }
}