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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.youngsophomore.luciddreaming.R
import com.youngsophomore.luciddreaming.data.model.Dream
import com.youngsophomore.luciddreaming.databinding.DialogConfirmActionBinding
import com.youngsophomore.luciddreaming.databinding.DialogMetaItemAppendBinding
import com.youngsophomore.luciddreaming.databinding.DialogMetaItemChooseBinding
import com.youngsophomore.luciddreaming.databinding.LayoutDreamdetailsPanelportraitBinding
import com.youngsophomore.luciddreaming.ui.adapters.MetaListAdapter
import com.youngsophomore.luciddreaming.ui.interfaces.MetaItemAppendListener
import com.youngsophomore.luciddreaming.ui.interfaces.MetaItemListener
import com.youngsophomore.luciddreaming.ui.viewmodels.DreamDetailsViewModel
import com.youngsophomore.luciddreaming.ui.viewmodels.LucidDreamingViewModel

class MetaPanelPortrait @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0) :
    ConstraintLayout(context, attrs, defStyleAttr)
    , MetaItemListener
{
    private val binding: LayoutDreamdetailsPanelportraitBinding =
        LayoutDreamdetailsPanelportraitBinding.inflate(LayoutInflater.from(context), this, true)
    private val dreamDetailsVM: DreamDetailsViewModel by lazy {
        ViewModelProvider(findViewTreeViewModelStoreOwner()!!).get(DreamDetailsViewModel::class.java)
    }
    lateinit var lucidDreamingVM: LucidDreamingViewModel
    lateinit var listener: MetaItemAppendListener
    private val dialogMetaItemChoose = Dialog(context)
    private val metaChooserBinding = DialogMetaItemChooseBinding.inflate(LayoutInflater.from(context))
    init {
        dialogMetaItemChoose.setContentView(metaChooserBinding.root)
        dialogMetaItemChoose.setOnDismissListener {
            dreamDetailsVM.isNewMetaItemFeeling = null
            binding.flowDreamDetailsFeelings.requestLayout()
            binding.flowDreamDetailsLocations.requestLayout()
        }

    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        
        binding.ibtnDreamDetailsAddFeeling.setOnClickListener {
            
            dreamDetailsVM.isNewMetaItemFeeling = true
            showMetaItemChooser(lucidDreamingVM.feelings?.value!!)

        }
        binding.ibtnDreamDetailsAddLocation.setOnClickListener {
            dreamDetailsVM.isNewMetaItemFeeling = false
            
            showMetaItemChooser(lucidDreamingVM.locations?.value!!)
        }

        binding.tglgrDreamDetailsPOV.addOnButtonCheckedListener { group, checkedId, isChecked ->
            dreamDetailsVM.dream = dreamDetailsVM.dream.copy(
                isFirstPerson = group.checkedButtonId == R.id.btnDreamDetailsFirstPerson
            )
        }

    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        super.onTouchEvent(event)
        return true
    }
    override fun onDeleteMetaItem(item: String) {
        showDialogConfirmMetaItemDelete("Удалить " +
                if (dreamDetailsVM.isNewMetaItemFeeling!!) "настроение" else "место" +
                        " \"${item}\"?", item)

    }
    private fun showMetaItemChooser(metaItems: List<String>){
        val metaAdapter = MetaListAdapter(this)
        metaAdapter.setMetaItems(metaItems)
        metaChooserBinding.rvMetaItemChooser.adapter = metaAdapter
        val layoutManager = LinearLayoutManager(context)
        metaChooserBinding.rvMetaItemChooser.layoutManager = layoutManager
        metaChooserBinding.rvMetaItemChooser.setHasFixedSize(true)
        if (dreamDetailsVM.isNewMetaItemFeeling!!) {
            metaChooserBinding.tvMetaItemChooserTitle.text = "Выбрать настроение"
        }
        else {
            metaChooserBinding.tvMetaItemChooserTitle.text = "Выбрать место"
        }
        metaChooserBinding.ibtnMetaItemChooserClose.setOnClickListener {
            
            dialogMetaItemChoose.dismiss()
        }
        metaChooserBinding.ibtnMetaItemChooserAddItem.setOnClickListener{
            showDialogMetaItemAppend()
        }
        metaChooserBinding.etMetaItemChooserFilter.addTextChangedListener { input ->
            
            metaAdapter.filter.filter(input)
        }
        dialogMetaItemChoose.show();
    }

    private fun showDialogMetaItemAppend(){
        val dialog = Dialog(context)
        val metaAppenderBinding = DialogMetaItemAppendBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(metaAppenderBinding.root)
        if (dreamDetailsVM.isNewMetaItemFeeling!!) {
            metaAppenderBinding.tvAppendDialogTitle.text = "Добавить настроение"
        }
        else {
            metaAppenderBinding.tvAppendDialogTitle.text = "Добавить место"
        }
        metaAppenderBinding.ibtnAppendDialogConfirm.setOnClickListener {
            listener.onConfirmItem(metaAppenderBinding.etAppendDialogFilter.text.toString(),
                dreamDetailsVM.isNewMetaItemFeeling!!)
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
                if (dreamDetailsVM.isNewMetaItemFeeling!!)
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
                if (dreamDetailsVM.dreamFeelingsIds.contains(item.id)){
                    dreamDetailsVM.deleteDreamFeeling(item.text.toString(), item.id)
                    binding.flowDreamDetailsFeelings.referencedIds = dreamDetailsVM.dreamFeelingsIds.toIntArray()
                    binding.root.removeView(item)
                    binding.flowDreamDetailsFeelings.requestLayout()
                }
                else {
                    dreamDetailsVM.deleteDreamLocation(item.text.toString(), item.id)
                    binding.flowDreamDetailsLocations.referencedIds = dreamDetailsVM.dreamLocationsIds.toIntArray()
                    binding.root.removeView(item)
                    binding.flowDreamDetailsLocations.requestLayout()
                }
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    fun setDreamMeta(dream: Dream) {
        if (dream.isFirstPerson) {
            binding.btnDreamDetailsFirstPerson.performClick()
        }
        else {
            binding.btnDreamDetailsThirdPerson.performClick()
        }

        dream.feelings.split("|").forEach { feeling ->
            val newMetaItem = TextView(context)
            newMetaItem.text = feeling
            dreamDetailsVM.addDreamFeeling(feeling, View.generateViewId())
            newMetaItem.id = dreamDetailsVM.dreamFeelingsIds[dreamDetailsVM.dreamFeelingsIds.size - 2]
            binding.root.addView(newMetaItem)
            binding.flowDreamDetailsFeelings.referencedIds = dreamDetailsVM.dreamFeelingsIds.toIntArray()
            newMetaItem.setOnLongClickListener {
                
                showDialogConfirmDreamMetaItemDelete("Удалить выбранное " +
                        if (dreamDetailsVM.dreamFeelingsIds.contains(newMetaItem.id)) "настроение" else "место" +
                                " \"$feeling\"?", newMetaItem)
                true
            }
        }

        dream.locations.split("|").forEach { location ->
            val newMetaItem = TextView(context)
            newMetaItem.text = location
            dreamDetailsVM.addDreamLocation(location, View.generateViewId())
            newMetaItem.id = dreamDetailsVM.dreamLocationsIds[dreamDetailsVM.dreamLocationsIds.size - 2]
            binding.root.addView(newMetaItem)
            binding.flowDreamDetailsLocations.referencedIds = dreamDetailsVM.dreamLocationsIds.toIntArray()
            newMetaItem.setOnLongClickListener {
                
                showDialogConfirmDreamMetaItemDelete("Удалить выбранное " +
                        if (dreamDetailsVM.dreamFeelingsIds.contains(newMetaItem.id)) "настроение" else "место" +
                                " \"$location\"?", newMetaItem)
                true
            }
        }
    }

    override fun onChooseMetaItem(item: String) {
        val newMetaItem = TextView(context)
        newMetaItem.text = item
        if (dreamDetailsVM.isNewMetaItemFeeling!!){
            dreamDetailsVM.addDreamFeeling(item, View.generateViewId())
            newMetaItem.id = dreamDetailsVM.dreamFeelingsIds[dreamDetailsVM.dreamFeelingsIds.size - 2]
            binding.root.addView(newMetaItem)
            binding.flowDreamDetailsFeelings.referencedIds = dreamDetailsVM.dreamFeelingsIds.toIntArray()

        }
        else {
            dreamDetailsVM.addDreamLocation(item, View.generateViewId())
            newMetaItem.id = dreamDetailsVM.dreamLocationsIds[dreamDetailsVM.dreamLocationsIds.size - 2]
            binding.root.addView(newMetaItem)
            binding.flowDreamDetailsLocations.referencedIds = dreamDetailsVM.dreamLocationsIds.toIntArray()
        }
        newMetaItem.setOnLongClickListener {
            
            showDialogConfirmDreamMetaItemDelete("Удалить выбранное " +
                    if (dreamDetailsVM.dreamFeelingsIds.contains(newMetaItem.id)) "настроение" else "место" +
                    " \"$item\"?", newMetaItem)
            true
        }
        dialogMetaItemChoose.dismiss()
    }



}