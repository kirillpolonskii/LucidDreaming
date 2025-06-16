package com.youngsophomore.luciddreaming.ui.fragments

import android.Manifest
import android.app.AlarmManager
import android.app.Dialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.navigation.NavigationBarView.OnItemSelectedListener
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.youngsophomore.luciddreaming.LucidDreamingApplication
import com.youngsophomore.luciddreaming.R
import com.youngsophomore.luciddreaming.data.receivers.WeakNotificationReceiver
import com.youngsophomore.luciddreaming.databinding.DialogEnterPasswordBinding
import com.youngsophomore.luciddreaming.databinding.FragmentSettingsBinding
import com.youngsophomore.luciddreaming.ui.viewmodels.LucidDreamingViewModel
import com.youngsophomore.luciddreaming.ui.viewmodels.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Calendar

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val settingsVM: SettingsViewModel by viewModels()
    private val lucidDreamingVM: LucidDreamingViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("Lifecycle", "SettingsFragment.onAttach()")
        val backPressedCallback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                Log.d("Lifecycle", " handleOnBackPressed()")
                scheduleWeakNotifs()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, backPressedCallback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        settingsVM.initSettings()
        val weakNotif = context?.let { context ->
            // тег канала потом нужно переместить в LucidDreamingApplication,
            // где будет создаваться канал для слабых уведомлений
            NotificationCompat.Builder(context, LucidDreamingApplication.WEAK_NOTIFS_CHANNEL_ID)
                .setContentTitle("Проверка реальности")
                .setContentText("Тестовое")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .build()
        }
        val notifManager = context.let { NotificationManagerCompat.from(it!!) }
        if (ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED){
            Log.d("Debug", " BLYAT! Gde razreshenie???")
        }
        notifManager.notify(1, weakNotif!!)
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        binding.swchSettingsPassword.isChecked = lucidDreamingVM.isPasswordEnabled

        with(binding){
            swchSettingsWeakNotifs.isChecked = settingsVM.weakNotifsIsEnabled.value ?: false

        }

        settingsVM.weakNotifsIsEnabled.observe(viewLifecycleOwner) { enabled ->
            if (enabled){
                weakNotifsUIToEnabledState()
            }
            else {
                weakNotifsUIToDisabledState()
            }
        }
        settingsVM.weakNotifsActiveHoursCalendarStart.observe(viewLifecycleOwner) { activeHoursStart ->
            Log.d("Debug", " weakNotifsActiveHoursCalendarStart.observe")
            Log.d("Debug", " " + activeHoursStart.toString())
            binding.tvSettingsWeakNotifsActiveHoursStart.text =
                "${activeHoursStart.get(Calendar.HOUR_OF_DAY)}-${activeHoursStart.get(Calendar.MINUTE)}"

        }
        settingsVM.weakNotifsActiveHoursCalendarEnd.observe(viewLifecycleOwner) { activeHoursEnd ->
            Log.d("Debug", " weakNotifsActiveHoursCalendarEnd.observe")
            Log.d("Debug", " " + activeHoursEnd.toString())
            binding.tvSettingsWeakNotifsActiveHoursEnd.text =
                "${activeHoursEnd.get(Calendar.HOUR_OF_DAY)}-${activeHoursEnd.get(Calendar.MINUTE)}"
        }

        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding){
            swchSettingsPassword.setOnClickListener {
                Log.d("Debug", "swchSettingsPassword.setOnCheckedChangeListener")
                showDialogSetPassword(binding.swchSettingsPassword.isChecked)
            }
            ibtnSettingsBack.setOnClickListener {
                scheduleWeakNotifs()
                findNavController().navigateUp()
            }
            tvSettingsWeakNotifsActiveHoursStart.setOnClickListener {
                showTimeChooser(true)
            }
            tvSettingsWeakNotifsActiveHoursEnd.setOnClickListener {
                showTimeChooser(false)
            }
            sprSettingsWeakNotifsFrequency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (position > 0)
                        settingsVM.updateFrequency(position)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

            }

            etSettingsWeakNotifsMessage.addTextChangedListener { text ->
                settingsVM.weakNotifsMessage = text.toString()
            }
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

    private fun scheduleWeakNotifs(){
        Log.d("Debug", "SettingsFragment.scheduleWeakNotifs()")
        val weakNotifsReceiverIntent = Intent(context, WeakNotificationReceiver::class.java)
        val weakNotifsPendingIntent = PendingIntent.getBroadcast(
            context,
            1,
            weakNotifsReceiverIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        settingsVM.weakNotifsTimePoints.forEach {
            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                it,
                AlarmManager.INTERVAL_DAY,
                weakNotifsPendingIntent
            )
            val cal = Calendar.getInstance()
            cal.timeInMillis = it
            Log.d("Debug", " cur time point for alarm = ")
            Log.d("Debug", " ${cal.get(Calendar.DATE)}, ${cal.get(Calendar.HOUR)}:${cal.get(Calendar.MINUTE)}:${cal.get(Calendar.SECOND)}")
        }

    }

    private fun showTimeChooser(isStart: Boolean){
        Log.d("Debug", "SettingFragment.showTimeChooser()")
        val timePicker = MaterialTimePicker.Builder()
            .setTitleText("Время начала")
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .build()
        timePicker.addOnPositiveButtonClickListener {
            Log.d("Debug", " timePicker.addOnPositiveButtonClickListener")
            settingsVM.updateActiveHours(timePicker.hour, timePicker.minute, isStart)
        }
        timePicker.show(childFragmentManager, "TIME_PICKER")
    }

    fun weakNotifsUIToEnabledState(){
        // поменять стили всех text view, edit text, spinner
    }

    fun weakNotifsUIToDisabledState(){
        // поменять стили всех text view, edit text, spinner
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