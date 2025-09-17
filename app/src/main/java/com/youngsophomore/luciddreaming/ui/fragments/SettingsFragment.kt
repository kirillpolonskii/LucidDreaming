package com.youngsophomore.luciddreaming.ui.fragments

import android.app.AlarmManager
import android.app.Dialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.youngsophomore.luciddreaming.data.receivers.NotificationReceiver
import com.youngsophomore.luciddreaming.databinding.DialogConfirmActionBinding
import com.youngsophomore.luciddreaming.databinding.DialogEnterPasswordBinding
import com.youngsophomore.luciddreaming.databinding.DialogMetaItemAppendBinding
import com.youngsophomore.luciddreaming.databinding.FragmentSettingsBinding
import com.youngsophomore.luciddreaming.ui.viewmodels.LucidDreamingViewModel
import com.youngsophomore.luciddreaming.ui.viewmodels.SettingsViewModel
import com.youngsophomore.luciddreaming.utils.LDTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Calendar

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val settingsVM: SettingsViewModel by viewModels()
    private val lucidDreamingVM: LucidDreamingViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val backPressedCallback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                scheduleNotifs()
                findNavController().navigateUp()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, backPressedCallback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        lucidDreamingVM.initSettings()
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        with(binding){
            swchSettingsNotifs.isChecked = lucidDreamingVM.notifsIsEnabled
            swchSettingsPassword.isChecked = lucidDreamingVM.isPasswordEnabled
            sprSettingsNotifsFrequency.setSelection(lucidDreamingVM.notifsFrequency)
            hscrvwSettingsThemes.isHorizontalScrollBarEnabled = false
        }

        subscribeToObservers()

        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding){
            ibtnSettingsBack.setOnClickListener {
                scheduleNotifs()
                findNavController().navigateUp()
            }
            swchSettingsNotifs.setOnCheckedChangeListener { buttonView, isChecked ->
                lucidDreamingVM.updateNotifsEnabled(isChecked)
                if (isChecked){
                    notifsUIToEnabledState()
                }
                else {
                    notifsUIToDisabledState()
                }
            }
            tvSettingsNotifsActiveHoursStart.setOnClickListener {
                showTimeChooser(true)
            }
            tvSettingsNotifsActiveHoursEnd.setOnClickListener {
                showTimeChooser(false)
            }
            sprSettingsNotifsFrequency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (position > 0)
                        lucidDreamingVM.updateFrequency(position)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

            }

            ivSettingsThemeBlue.setOnClickListener {
                lucidDreamingVM.setSelectedTheme(LDTheme.Blue)
                showDialogConfirmTheme()
            }
            ivSettingsThemePurple.setOnClickListener {
                lucidDreamingVM.setSelectedTheme(LDTheme.Purple)
                showDialogConfirmTheme()
            }
            ivSettingsThemeGreen.setOnClickListener {
                lucidDreamingVM.setSelectedTheme(LDTheme.Green)
                showDialogConfirmTheme()
            }
            ivSettingsThemePink.setOnClickListener {
                lucidDreamingVM.setSelectedTheme(LDTheme.Pink)
                showDialogConfirmTheme()
            }

            swchSettingsPassword.setOnClickListener {
                showDialogSetPassword(binding.swchSettingsPassword.isChecked)
            }
        }

    }

    private fun subscribeToObservers(){
        lucidDreamingVM.notifsActiveHoursCalendarStart.observe(viewLifecycleOwner) { activeHoursStart ->
            binding.tvSettingsNotifsActiveHoursStart.text =
                "${activeHoursStart.get(Calendar.HOUR_OF_DAY)}:${activeHoursStart.get(Calendar.MINUTE)}"

        }
        lucidDreamingVM.notifsActiveHoursCalendarEnd.observe(viewLifecycleOwner) { activeHoursEnd ->
            binding.tvSettingsNotifsActiveHoursEnd.text =
                "${activeHoursEnd.get(Calendar.HOUR_OF_DAY)}:${activeHoursEnd.get(Calendar.MINUTE)}"
        }

        lucidDreamingVM.ivThemesSelectedState.observe(viewLifecycleOwner) { ivSelectedState ->
            binding.ivSettingsThemeBlue.isSelected = ivSelectedState[0]
            binding.ivSettingsThemePurple.isSelected = ivSelectedState[1]
            binding.ivSettingsThemeGreen.isSelected = ivSelectedState[2]
            binding.ivSettingsThemePink.isSelected = ivSelectedState[3]
        }
    }

    private fun showDialogConfirmTheme(){
        val dialog = Dialog(requireContext())
        val confirmThemeBinding = DialogConfirmActionBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(confirmThemeBinding.root)
        confirmThemeBinding.tvConfirmActionDialogTitle.text = "Подтверждение темы"
        confirmThemeBinding.tvConfirmActionDialogAction.text = "Для смены темы требуется перезапуск приложения. Сменить тему?"

        confirmThemeBinding.ibtnConfirmActionDialogConfirm.setOnClickListener {
            activity?.recreate()
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showDialogSetPassword(isSwitchChecked: Boolean){
        val dialog = Dialog(requireContext())
        val setPasswordBinding = DialogEnterPasswordBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(setPasswordBinding.root)

        setPasswordBinding.ibtnSettingsConfirm.setOnClickListener {
            if (isSwitchChecked) {
                // TODO: dialog for password confirmation
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

    private fun scheduleNotifs(){
        //Log.d("Debug", "SettingsFragment.scheduleNotifs()")
        val notifsReceiverIntent = Intent(context, NotificationReceiver::class.java)
        val notifsPendingIntent = PendingIntent.getBroadcast(
            context,
            1,
            notifsReceiverIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        lucidDreamingVM.notifsTimePoints.forEach {
            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                it,
                AlarmManager.INTERVAL_DAY,
                notifsPendingIntent
            )
            val cal = Calendar.getInstance()
            cal.timeInMillis = it
            //Log.d("Debug", " cur time point for alarm = ")
            //Log.d("Debug", " ${cal.get(Calendar.DATE)}, ${cal.get(Calendar.HOUR)}:${cal.get(Calendar.MINUTE)}:${cal.get(Calendar.SECOND)}")
        }

    }

    private fun showTimeChooser(isStart: Boolean){
        val timePicker = MaterialTimePicker.Builder()
            .setTitleText("Время начала")
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .build()
        timePicker.addOnPositiveButtonClickListener {
            lucidDreamingVM.updateActiveHours(timePicker.hour, timePicker.minute, isStart)
        }
        timePicker.show(childFragmentManager, "TIME_PICKER")
    }

    fun notifsUIToEnabledState(){
        // TODO: change styles of every related UI component
    }

    fun notifsUIToDisabledState(){
        // TODO: change styles of every related UI component
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