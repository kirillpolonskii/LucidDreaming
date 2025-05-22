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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.Orientation
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.youngsophomore.luciddreaming.R
import com.youngsophomore.luciddreaming.databinding.DialogConfirmActionBinding
import com.youngsophomore.luciddreaming.databinding.DialogMetaItemAppendBinding
import com.youngsophomore.luciddreaming.databinding.DialogMetaItemChooseBinding
import com.youngsophomore.luciddreaming.databinding.ItemMetaBinding
import com.youngsophomore.luciddreaming.databinding.LayoutDreamdetailsPanelportraitBinding
import com.youngsophomore.luciddreaming.ui.adapters.MetaListAdapter
import com.youngsophomore.luciddreaming.ui.fragments.DreamDetailsFragment
import com.youngsophomore.luciddreaming.ui.interfaces.ConfirmActionListener
import com.youngsophomore.luciddreaming.ui.interfaces.MetaItemAppendListener
import com.youngsophomore.luciddreaming.ui.interfaces.MetaItemChooseListener
import com.youngsophomore.luciddreaming.ui.viewmodels.DreamDetailsViewModel
import com.youngsophomore.luciddreaming.ui.viewmodels.LucidDreamingViewModel

class MetaTopPanelPortrait @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0) :
    ConstraintLayout(context, attrs, defStyleAttr)
    , MetaItemChooseListener
{
    private var binding: LayoutDreamdetailsPanelportraitBinding =
        LayoutDreamdetailsPanelportraitBinding.inflate(LayoutInflater.from(context), this, true)
    private val dreamDetailsViewModel: DreamDetailsViewModel by lazy {
        ViewModelProvider(findViewTreeViewModelStoreOwner()!!).get(DreamDetailsViewModel::class.java)
    }
    lateinit var lucidDreamingViewModel: LucidDreamingViewModel
    lateinit var listener: MetaItemAppendListener
    private val dialogMetaItemChoose = Dialog(context)
    private val metaChooserBinding = DialogMetaItemChooseBinding.inflate(LayoutInflater.from(context))
    init {
        dialogMetaItemChoose.setContentView(metaChooserBinding.root)
        dialogMetaItemChoose.setOnDismissListener {
            dreamDetailsViewModel.isNewMetaItemFeeling = null
            binding.flowDreamDetailsFeelings.requestLayout()
            binding.flowDreamDetailsLocations.requestLayout()
        }

    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        Log.d("Gestures", "MetaTopPanelPortrait.onAttachedToWindow()")
        Log.d("Gestures", " lucidDreamingVM = ${lucidDreamingViewModel}")
        binding.ibtnDreamDetailsAddFeeling.setOnClickListener {
            Log.d("Gestures", " ibtnDreamDetailsAddFeeling.setOnClickListener")
            dreamDetailsViewModel.isNewMetaItemFeeling = true
            showMetaItemChooser(lucidDreamingViewModel.feelings.value!!)

        }
        binding.ibtnDreamDetailsAddLocation.setOnClickListener {
            dreamDetailsViewModel.isNewMetaItemFeeling = false
            Log.d("Gestures", " ibtnDreamDetailsAddLocation.setOnClickListener")
            showMetaItemChooser(lucidDreamingViewModel.locations.value!!)
        }
        dreamDetailsViewModel.initFeelingsAndLocations(binding.ibtnDreamDetailsAddFeeling.id,
            binding.ibtnDreamDetailsAddLocation.id)
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
        /*lucidDreamingViewModel.feelings.observe(findViewTreeLifecycleOwner()!!) { items ->
            metaAdapter.setMetaItems(items)
        }*/
        metaAdapter.setMetaItems(metaItems)
        metaChooserBinding.rvMetaItemChooser.adapter = metaAdapter
        val layoutManager = LinearLayoutManager(context)
        metaChooserBinding.rvMetaItemChooser.layoutManager = layoutManager
        metaChooserBinding.rvMetaItemChooser.setHasFixedSize(true)
        if (dreamDetailsViewModel.isNewMetaItemFeeling!!) {
            metaChooserBinding.tvMetaItemChooserTitle.text = "Выбрать настроение"
        }
        else {
            metaChooserBinding.tvMetaItemChooserTitle.text = "Выбрать место"
        }
        metaChooserBinding.ibtnMetaItemChooserClose.setOnClickListener {
            Log.d("Gestures", " ibtnMetaItemChooserClose.setOnClickListener")
            dialogMetaItemChoose.dismiss()
        }
        metaChooserBinding.ibtnDreamDetailsAddItem.setOnClickListener{
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
        if (dreamDetailsViewModel.isNewMetaItemFeeling!!) {
            metaAppenderBinding.tvAppendDialogTitle.text = "Добавить настроение"
        }
        else {
            metaAppenderBinding.tvAppendDialogTitle.text = "Добавить место"
        }
        metaAppenderBinding.ibtnAppendDialogConfirm.setOnClickListener {
            listener.onConfirmItem(metaAppenderBinding.etAppendDialogFilter.text.toString(),
                dreamDetailsViewModel.isNewMetaItemFeeling!!)
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
                if (dreamDetailsViewModel.isNewMetaItemFeeling!!)
                    lucidDreamingViewModel.deleteFeeling(item)
                else
                    lucidDreamingViewModel.deleteLocation(item)
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
                if (dreamDetailsViewModel.dreamFeelingsIds.contains(item.id)){
                    dreamDetailsViewModel.deleteDreamFeeling(item.text.toString(), item.id)
                    binding.flowDreamDetailsFeelings.referencedIds = dreamDetailsViewModel.dreamFeelingsIds.toIntArray()
                    binding.root.removeView(item)
                    binding.flowDreamDetailsFeelings.requestLayout()
                }
                else {
                    dreamDetailsViewModel.deleteDreamLocation(item.text.toString(), item.id)
                    binding.flowDreamDetailsLocations.referencedIds = dreamDetailsViewModel.dreamLocationsIds.toIntArray()
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
        if (dreamDetailsViewModel.isNewMetaItemFeeling!!){
            dreamDetailsViewModel.addDreamFeeling(item, View.generateViewId())
            newMetaItem.id = dreamDetailsViewModel.dreamFeelingsIds[dreamDetailsViewModel.dreamFeelingsIds.size - 2]
            binding.root.addView(newMetaItem)
            binding.flowDreamDetailsFeelings.referencedIds = dreamDetailsViewModel.dreamFeelingsIds.toIntArray()

        }
        else {
            dreamDetailsViewModel.addDreamLocation(item, View.generateViewId())
            newMetaItem.id = dreamDetailsViewModel.dreamLocationsIds[dreamDetailsViewModel.dreamLocationsIds.size - 2]
            binding.root.addView(newMetaItem)
            binding.flowDreamDetailsLocations.referencedIds = dreamDetailsViewModel.dreamLocationsIds.toIntArray()
        }
        newMetaItem.setOnLongClickListener {
            Log.d("Gestures", "newMetaItem.setOnLongClickListener, ${newMetaItem.text}")
            showDialogConfirmDreamMetaItemDelete("Удалить выбранное " +
                    if (dreamDetailsViewModel.dreamFeelingsIds.contains(newMetaItem.id)) "настроение" else "место" +
                    " \"$item\"?", newMetaItem)
            true
        }
        dialogMetaItemChoose.dismiss()
    }

    override fun onMetaItemDelete(item: String) {
        showDialogConfirmMetaItemDelete("Удалить " +
                if (dreamDetailsViewModel.isNewMetaItemFeeling!!) "настроение" else "место" +
                " \"${item}\"?", item)

    }

}