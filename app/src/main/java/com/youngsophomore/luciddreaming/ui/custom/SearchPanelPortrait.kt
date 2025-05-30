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
import com.youngsophomore.luciddreaming.databinding.DialogConfirmActionBinding
import com.youngsophomore.luciddreaming.databinding.DialogMetaItemAppendBinding
import com.youngsophomore.luciddreaming.databinding.DialogMetaItemChooseBinding
import com.youngsophomore.luciddreaming.databinding.LayoutDreamslistPanelportraitBinding
import com.youngsophomore.luciddreaming.ui.adapters.MetaListAdapter
import com.youngsophomore.luciddreaming.ui.interfaces.MetaItemAppendListener
import com.youngsophomore.luciddreaming.ui.interfaces.MetaItemChooseListener
import com.youngsophomore.luciddreaming.ui.viewmodels.DreamsListViewModel
import com.youngsophomore.luciddreaming.ui.viewmodels.LucidDreamingViewModel

class SearchTopPanelPortrait : ConstraintLayout, MetaItemChooseListener {
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
    private val dialogMetaItemChoose = Dialog(context)
    private val metaChooserBinding = DialogMetaItemChooseBinding.inflate(LayoutInflater.from(context))
    init {
        dialogMetaItemChoose.setContentView(metaChooserBinding.root)
        dialogMetaItemChoose.setOnDismissListener {
            //dreamsListVM.isNewMetaItemFeeling = null
            binding.flowDreamsListFeelings.requestLayout()
            binding.flowDreamsListLocations.requestLayout()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        Log.d("Gestures", "SearchTopPanelPortrait.onAttachedToWindow()")
        binding.ibtnDreamsListAddFeeling.setOnClickListener {
            Log.d("Gestures", " ibtnDreamsListAddFeeling.setOnClickListener")
            dreamsListVM.isNewSearchItemFeeling = true
            showMetaItemChooser(lucidDreamingVM.feelings?.value!!)

        }
        binding.ibtnDreamsListAddLocation.setOnClickListener {
            dreamsListVM.isNewSearchItemFeeling = false
            Log.d("Gestures", " ibtnDreamsListAddLocation.setOnClickListener")
            showMetaItemChooser(lucidDreamingVM.locations?.value!!)
        }

        binding.tglgrDreamsListPOV.addOnButtonCheckedListener { group, checkedId, isChecked ->
            Log.d("Gestures", " tglgrDreamsListPOV.addOnButtonCheckedListener " + dreamsListVM.isDreamFirstPerson)
            dreamsListVM.isDreamFirstPerson = group.checkedButtonId == R.id.btnDreamsListFirstPerson
        }

    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        Log.d("Gestures", "SearchTopPanelPortrait.onInterceptTouchEvent()")
        return false
    }
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.d("Gestures", "SearchTopPanelPortrait.onTouchEvent()")
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
        if (dreamsListVM.isNewSearchItemFeeling!!) {
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
        if (dreamsListVM.isNewSearchItemFeeling!!) {
            metaAppenderBinding.tvAppendDialogTitle.text = "Добавить настроение"
        }
        else {
            metaAppenderBinding.tvAppendDialogTitle.text = "Добавить место"
        }
        metaAppenderBinding.ibtnAppendDialogConfirm.setOnClickListener {
            listener.onConfirmItem(metaAppenderBinding.etAppendDialogFilter.text.toString(),
                dreamsListVM.isNewSearchItemFeeling!!)
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
                if (dreamsListVM.isNewSearchItemFeeling!!)
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
                if (dreamsListVM.searchFeelingsIds.contains(item.id)){
                    dreamsListVM.deleteSearchFeeling(item.text.toString(), item.id)
                    binding.flowDreamsListFeelings.referencedIds = dreamsListVM.searchFeelingsIds.toIntArray()
                    binding.root.removeView(item)
                    binding.flowDreamsListFeelings.requestLayout()
                }
                else {
                    dreamsListVM.deleteSearchLocation(item.text.toString(), item.id)
                    binding.flowDreamsListLocations.referencedIds = dreamsListVM.searchLocationsIds.toIntArray()
                    binding.root.removeView(item)
                    binding.flowDreamsListLocations.requestLayout()
                }
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    override fun onMetaItemChoose(item: String) {
        Log.d("Gestures", "SearchTopPanelPortrait.onMetaItemChoose()")
        // здесь добавить текст с нажатой в диалоге кнопке в Flow, т. е. создать Button и добавить id
        val newMetaItem = TextView(context)
        newMetaItem.text = item
        if (dreamsListVM.isNewSearchItemFeeling!!){
            dreamsListVM.addSearchFeeling(item, View.generateViewId())
            newMetaItem.id = dreamsListVM.searchFeelingsIds[dreamsListVM.searchFeelingsIds.size - 2]
            binding.root.addView(newMetaItem)
            binding.flowDreamsListFeelings.referencedIds = dreamsListVM.searchFeelingsIds.toIntArray()

        }
        else {
            dreamsListVM.addSearchLocation(item, View.generateViewId())
            newMetaItem.id = dreamsListVM.searchLocationsIds[dreamsListVM.searchLocationsIds.size - 2]
            binding.root.addView(newMetaItem)
            binding.flowDreamsListLocations.referencedIds = dreamsListVM.searchLocationsIds.toIntArray()
        }
        newMetaItem.setOnLongClickListener {
            Log.d("Gestures", "newMetaItem.setOnLongClickListener, ${newMetaItem.text}")
            showDialogConfirmDreamMetaItemDelete("Удалить выбранное " +
                    if (dreamsListVM.searchFeelingsIds.contains(newMetaItem.id)) "настроение" else "место" +
                            " \"$item\"?", newMetaItem)
            true
        }
        dialogMetaItemChoose.dismiss()
    }

    override fun onMetaItemDelete(item: String) {
        showDialogConfirmMetaItemDelete("Удалить " +
                if (dreamsListVM.isNewSearchItemFeeling!!) "настроение" else "место" +
                        " \"${item}\"?", item)

    }
}