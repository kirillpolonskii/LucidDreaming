package com.youngsophomore.luciddreaming.ui.custom

import android.app.Dialog
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.youngsophomore.luciddreaming.R
import com.youngsophomore.luciddreaming.databinding.DialogConfirmActionBinding
import com.youngsophomore.luciddreaming.databinding.DialogMetaItemAppendBinding
import com.youngsophomore.luciddreaming.databinding.DialogMetaItemChooseBinding
import com.youngsophomore.luciddreaming.databinding.LayoutDreamslistPanelportraitBinding
import com.youngsophomore.luciddreaming.ui.adapters.DreamsListAdapter
import com.youngsophomore.luciddreaming.ui.adapters.MetaListAdapter
import com.youngsophomore.luciddreaming.ui.interfaces.MetaItemAppendListener
import com.youngsophomore.luciddreaming.ui.interfaces.MetaItemChooseListener
import com.youngsophomore.luciddreaming.ui.viewmodels.DreamsListViewModel
import com.youngsophomore.luciddreaming.ui.viewmodels.LucidDreamingViewModel
import java.time.Instant
import java.time.LocalDateTime

import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class FilterPanelPortrait : ConstraintLayout, MetaItemChooseListener {
    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet? = null): super(context, attrs)
    constructor(context: Context,
                attrs: AttributeSet? = null,
                defStyleAttr: Int = 0): super(context, attrs, defStyleAttr)
    
    private val binding: LayoutDreamslistPanelportraitBinding = 
        LayoutDreamslistPanelportraitBinding.inflate(LayoutInflater.from(context), this, true)
    private val dreamsListVM: DreamsListViewModel by lazy {
        ViewModelProvider(findViewTreeViewModelStoreOwner()!!).get(DreamsListViewModel::class.java)
    }
    lateinit var lucidDreamingVM: LucidDreamingViewModel
    lateinit var listener: MetaItemAppendListener
    lateinit var fragmentManager: FragmentManager
    lateinit var dreamsAdapter: DreamsListAdapter
    private val dialogMetaItemChoose = Dialog(context)
    private val metaChooserBinding = DialogMetaItemChooseBinding.inflate(LayoutInflater.from(context))
    init {
        dialogMetaItemChoose.setContentView(metaChooserBinding.root)
        dialogMetaItemChoose.setOnDismissListener {
            dreamsListVM.isNewFilterItemFeeling = null
            binding.flowDreamsListFeelings.requestLayout()
            binding.flowDreamsListLocations.requestLayout()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        Log.d("Gestures", "FilterTopPanelPortrait.onAttachedToWindow()")
        binding.ibtnDreamsListAddFeeling.setOnClickListener {
            Log.d("Gestures", " ibtnDreamsListAddFeeling.setOnClickListener")
            dreamsListVM.isNewFilterItemFeeling = true
            showMetaItemChooser(lucidDreamingVM.feelings?.value!!)

        }
        binding.ibtnDreamsListAddLocation.setOnClickListener {
            dreamsListVM.isNewFilterItemFeeling = false
            Log.d("Gestures", " ibtnDreamsListAddLocation.setOnClickListener")
            showMetaItemChooser(lucidDreamingVM.locations?.value!!)
        }

        binding.tglgrDreamsListPOV.addOnButtonCheckedListener { group, checkedId, isChecked ->
            Log.d("Gestures", " tglgrDreamsListPOV.addOnButtonCheckedListener " + dreamsListVM.isDreamFirstPerson)
            if (group.checkedButtonId == R.id.btnDreamsListFirstPerson &&
                !binding.btnDreamsListThirdPerson.isPressed)
                dreamsListVM.isDreamFirstPerson = true
            else if (group.checkedButtonId == R.id.btnDreamsListThirdPerson &&
                !binding.btnDreamsListFirstPerson.isPressed)
                dreamsListVM.isDreamFirstPerson = false
            else
                dreamsListVM.isDreamFirstPerson = null
        }

        binding.ibtnDreamsListAddKeyword.setOnClickListener {
            val tvNewKeyword = TextView(context)
            tvNewKeyword.text = binding.etDreamsListKeyword.text
            tvNewKeyword.id = View.generateViewId()
            dreamsListVM.addFilterKeyword(tvNewKeyword.text.toString(), tvNewKeyword.id)
            binding.root.addView(tvNewKeyword)
            binding.flowDreamsListKeywords.referencedIds = dreamsListVM.filterKeywordsIds.toIntArray()
            binding.flowDreamsListKeywords.requestLayout()

            tvNewKeyword.setOnLongClickListener {
                Log.d("Gestures", "tvNewKeyword.setOnLongClickListener, ${tvNewKeyword.text}")
                dreamsListVM.deleteFilterKeyword(tvNewKeyword.text.toString(), tvNewKeyword.id)
                binding.flowDreamsListKeywords.referencedIds = dreamsListVM.filterKeywordsIds.toIntArray()
                binding.root.removeView(tvNewKeyword)
                binding.flowDreamsListKeywords.requestLayout()
                true
            }
        }

        binding.ibtnDreamsListAddCreatedDateRange.setOnClickListener {
            Log.d("Gestures", " ibtnDreamsListAddCreatedDateRange.setOnClickListener")
            showDateCreatedChooser("Интервал даты создания", binding.tvDreamsListCreatedDateRange)

        }
        binding.ibtnDreamsListAddEditedDateRange.setOnClickListener {
            Log.d("Gestures", " ibtnDreamsListAddEditedDateRange.setOnClickListener")
            showDateCreatedChooser("Интервал даты изменения", binding.tvDreamsListEditedDateRange)
        }

        binding.ibtnDreamsListApplyFilter.setOnClickListener {
            dreamsListVM.applyFilters()
            binding.etDreamsListKeyword.text.clear()
            binding.etDreamsListKeyword.clearFocus()

            dreamsAdapter.setDreams(dreamsListVM.filteredDreams)
        }

    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        Log.d("Gestures", "FilterTopPanelPortrait.onInterceptTouchEvent()")
        return false
    }
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.d("Gestures", "FilterTopPanelPortrait.onTouchEvent()")
        //return super.onTouchEvent(event)
        //return false
        super.onTouchEvent(event)
        //performClick()
        return true
    }

    private fun showMetaItemChooser(metaItems: List<String>){
        /*if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // this is optional
        }*/
        val metaAdapter = MetaListAdapter(this)
        /*lucidDreamingVM.feelings.observe(findViewTreeLifecycleOwner()!!) { items ->
            metaAdapter.setMetaItems(items)
        }*/
        metaAdapter.setMetaItems(metaItems)
        metaChooserBinding.rvMetaItemChooser.adapter = metaAdapter
        val layoutManager = LinearLayoutManager(context)
        metaChooserBinding.rvMetaItemChooser.layoutManager = layoutManager
        metaChooserBinding.rvMetaItemChooser.setHasFixedSize(true)
        if (dreamsListVM.isNewFilterItemFeeling!!) {
            metaChooserBinding.tvMetaItemChooserTitle.text = "Выбрать настроение"
        }
        else {
            metaChooserBinding.tvMetaItemChooserTitle.text = "Выбрать место"
        }
        metaChooserBinding.ibtnMetaItemChooserClose.setOnClickListener {
            Log.d("Gestures", " ibtnMetaItemChooserClose.setOnClickListener")
            dialogMetaItemChoose.dismiss()
        }
        metaChooserBinding.ibtnMetaItemChooserAddItem.setOnClickListener{
            showDialogMetaItemAppend()
        }
        metaChooserBinding.etMetaItemChooserFilter.addTextChangedListener { input ->
            Log.d("Gestures", " etMetaItemChooserFilter.addTextChangedListener, $input")
            metaAdapter.filter.filter(input)
        }
        dialogMetaItemChoose.show();
    }

    private fun showDialogMetaItemAppend(){
        val dialog = Dialog(context)
        val metaAppenderBinding = DialogMetaItemAppendBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(metaAppenderBinding.root)
        if (dreamsListVM.isNewFilterItemFeeling!!) {
            metaAppenderBinding.tvAppendDialogTitle.text = "Добавить настроение"
        }
        else {
            metaAppenderBinding.tvAppendDialogTitle.text = "Добавить место"
        }
        metaAppenderBinding.ibtnAppendDialogConfirm.setOnClickListener {
            listener.onConfirmItem(metaAppenderBinding.etAppendDialogFilter.text.toString(),
                dreamsListVM.isNewFilterItemFeeling!!)
            metaChooserBinding.rvMetaItemChooser.adapter?.notifyDataSetChanged()
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showDialogConfirmMetaItemDelete(action: String, item: String){
        val dialog = Dialog(context)
        val confirmActionBinding = DialogConfirmActionBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(confirmActionBinding.root)
        confirmActionBinding.apply {
            tvConfirmActionDialogTitle.text = "Подтвердить удаление"
            tvConfirmActionDialogAction.text = action
            ibtnConfirmActionDialogConfirm.setOnClickListener {
                if (dreamsListVM.isNewFilterItemFeeling!!)
                    lucidDreamingVM.deleteFeeling(item)
                else
                    lucidDreamingVM.deleteLocation(item)
                dialog.dismiss()
            }
        }
        dialog.show()
    }
    
    private fun showDialogConfirmDreamMetaItemDelete(action: String, item: TextView){
        val dialog = Dialog(context)
        val confirmActionBinding = DialogConfirmActionBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(confirmActionBinding.root)
        confirmActionBinding.apply {
            tvConfirmActionDialogTitle.text = "Подтвердить удаление"
            tvConfirmActionDialogAction.text = action
            ibtnConfirmActionDialogConfirm.setOnClickListener {
                if (dreamsListVM.filterFeelingsIds.contains(item.id)){
                    dreamsListVM.deleteFilterFeeling(item.text.toString(), item.id)
                    binding.flowDreamsListFeelings.referencedIds = dreamsListVM.filterFeelingsIds.toIntArray()
                    binding.root.removeView(item)
                    binding.flowDreamsListFeelings.requestLayout()
                }
                else {
                    dreamsListVM.deleteFilterLocation(item.text.toString(), item.id)
                    binding.flowDreamsListLocations.referencedIds = dreamsListVM.filterLocationsIds.toIntArray()
                    binding.root.removeView(item)
                    binding.flowDreamsListLocations.requestLayout()
                }
                dialog.dismiss()
            }
        }
        dialog.show()
    }
    // здесь можно в функцию передавать лямбду и уже на уровне вызова метода управлять,
    // в какой именно tv установится выбранное значение
    private fun showDateCreatedChooser(title: String, tvDateRange: TextView){
        val builder = MaterialDatePicker.Builder.dateRangePicker()

        val dateRangePicker = builder
            .setTitleText(title)
            .build()
        dateRangePicker.addOnPositiveButtonClickListener { selection ->
            val dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.getDefault())
            val startDate = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(selection.first), ZoneId.of("UTC"))
            val endDate = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(selection.second), ZoneId.of("UTC"))
            val startDateStr = startDate.format(dtf)
            val endDateStr = endDate.format(dtf)
            tvDateRange.text = "$startDateStr - $endDateStr"
        }
        dateRangePicker.show(fragmentManager, "DATE_RANGE_PICKER")

    }

    override fun onMetaItemChoose(item: String) {
        Log.d("Gestures", "FilterTopPanelPortrait.onMetaItemChoose()")
        // здесь добавить текст с нажатой в диалоге кнопке в Flow, т. е. создать Button и добавить id
        val newMetaItem = TextView(context)
        newMetaItem.text = item
        if (dreamsListVM.isNewFilterItemFeeling!!){
            dreamsListVM.addFilterFeeling(item, View.generateViewId())
            newMetaItem.id = dreamsListVM.filterFeelingsIds[dreamsListVM.filterFeelingsIds.size - 2]
            binding.root.addView(newMetaItem)
            binding.flowDreamsListFeelings.referencedIds = dreamsListVM.filterFeelingsIds.toIntArray()

        }
        else {
            dreamsListVM.addFilterLocation(item, View.generateViewId())
            newMetaItem.id = dreamsListVM.filterLocationsIds[dreamsListVM.filterLocationsIds.size - 2]
            binding.root.addView(newMetaItem)
            binding.flowDreamsListLocations.referencedIds = dreamsListVM.filterLocationsIds.toIntArray()
        }
        newMetaItem.setOnLongClickListener {
            Log.d("Gestures", "newMetaItem.setOnLongClickListener, ${newMetaItem.text}")
            showDialogConfirmDreamMetaItemDelete("Удалить выбранное " +
                    if (dreamsListVM.filterFeelingsIds.contains(newMetaItem.id)) "настроение" else "место" +
                            " \"$item\"?", newMetaItem)
            true
        }
        dialogMetaItemChoose.dismiss()
    }

    override fun onMetaItemDelete(item: String) {
        showDialogConfirmMetaItemDelete("Удалить " +
                if (dreamsListVM.isNewFilterItemFeeling!!) "настроение" else "место" +
                        " \"${item}\"?", item)

    }


}
