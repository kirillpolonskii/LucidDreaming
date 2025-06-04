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
import com.youngsophomore.luciddreaming.databinding.LayoutDreamdetailsPanelportraitBinding
import com.youngsophomore.luciddreaming.ui.adapters.MetaListAdapter
import com.youngsophomore.luciddreaming.ui.interfaces.MetaItemAppendListener
import com.youngsophomore.luciddreaming.ui.interfaces.MetaItemChooseListener
import com.youngsophomore.luciddreaming.ui.viewmodels.DreamDetailsViewModel
import com.youngsophomore.luciddreaming.ui.viewmodels.LucidDreamingViewModel

class MetaPanelPortrait @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0) :
    ConstraintLayout(context, attrs, defStyleAttr)
    , MetaItemChooseListener
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
        Log.d("Gestures", "MetaTopPanelPortrait.onAttachedToWindow()")
        binding.ibtnDreamDetailsAddFeeling.setOnClickListener {
            Log.d("Gestures", " ibtnDreamDetailsAddFeeling.setOnClickListener")
            dreamDetailsVM.isNewMetaItemFeeling = true
            showMetaItemChooser(lucidDreamingVM.feelings?.value!!)

        }
        binding.ibtnDreamDetailsAddLocation.setOnClickListener {
            dreamDetailsVM.isNewMetaItemFeeling = false
            Log.d("Gestures", " ibtnDreamDetailsAddLocation.setOnClickListener")
            showMetaItemChooser(lucidDreamingVM.locations?.value!!)
        }

        binding.tglgrDreamDetailsPOV.addOnButtonCheckedListener { group, checkedId, isChecked ->
            Log.d("Gestures", " tglgrDreamDetailsPOV.addOnButtonCheckedListener " +
                    dreamDetailsVM.isDreamFirstPerson)
            dreamDetailsVM.isDreamFirstPerson = group.checkedButtonId == R.id.btnDreamDetailsFirstPerson
        }

    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        Log.d("Gestures", "MetaTopPanelPortrait.onInterceptTouchEvent()")
        //return super.onInterceptTouchEvent(ev)
        return when (ev?.action){
            MotionEvent.ACTION_DOWN -> {
                Log.d("Gestures", " ACTION_DOWN, ev.x=${ev?.x}")
                //true
                false
            }
            MotionEvent.ACTION_UP -> {
                Log.d("Gestures", " ACTION_UP")
                //true
                false
            }
            MotionEvent.ACTION_BUTTON_PRESS -> {
                Log.d("Gestures", " ACTION_BUTTON_PRESS, ev.x=${ev?.x}")
                //true
                false
            }
            else -> {
                Log.d("Gestures", " else")
                //true
                false
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.d("Gestures", "MetaTopPanelPortrait.onTouchEvent()")
        //return super.onTouchEvent(event)
        //return false
        super.onTouchEvent(event)
        //performClick()
        return when (event?.action){
            MotionEvent.ACTION_DOWN -> {
                Log.d("Gestures", " ACTION_DOWN, ev.x=${event?.x}")
                true
            }
            MotionEvent.ACTION_UP -> {
                Log.d("Gestures", " ACTION_UP")
                //binding.tglgrDreamDetailsPOV.check(0)
                //binding.tvDreamDetailsFeelings.text = "WHAT"
                true
            }
            MotionEvent.ACTION_BUTTON_PRESS -> {
                Log.d("Gestures", " ACTION_BUTTON_PRESS, ev.x=${event?.x}")
                true
            }
            else -> {
                Log.d("Gestures", " else")
                true
            }
        }
    }

    override fun performClick(): Boolean {
        Log.d("Gestures", "MetaTopPanelPortrait.performClick()")
        return super.performClick()
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
        if (dreamDetailsVM.isNewMetaItemFeeling!!) {
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

    override fun onMetaItemChoose(item: String) {
        Log.d("Gestures", "MetaTopPanelPortrait.onMetaItemChoose()")
        // здесь добавить текст с нажатой в диалоге кнопке в Flow, т. е. создать Button и добавить id
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
            Log.d("Gestures", "newMetaItem.setOnLongClickListener, ${newMetaItem.text}")
            showDialogConfirmDreamMetaItemDelete("Удалить выбранное " +
                    if (dreamDetailsVM.dreamFeelingsIds.contains(newMetaItem.id)) "настроение" else "место" +
                    " \"$item\"?", newMetaItem)
            true
        }
        dialogMetaItemChoose.dismiss()
    }

    override fun onMetaItemDelete(item: String) {
        showDialogConfirmMetaItemDelete("Удалить " +
                if (dreamDetailsVM.isNewMetaItemFeeling!!) "настроение" else "место" +
                " \"${item}\"?", item)

    }

}