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

class MetaTopPanelPortrait @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0) :
    ConstraintLayout(context, attrs, defStyleAttr)
    , MetaItemChooseListener
{
    // true - панель показывается, false - панель исчезает
    private var binding: LayoutDreamdetailsPanelportraitBinding =
        LayoutDreamdetailsPanelportraitBinding.inflate(LayoutInflater.from(context), this, true)
    private val viewModel: DreamDetailsViewModel by lazy {
        ViewModelProvider(findViewTreeViewModelStoreOwner()!!).get(DreamDetailsViewModel::class.java)
    }
    lateinit var listener: MetaItemAppendListener
    private val dialogMetaItemChoose = Dialog(context)
    private val metaChooserBinding = DialogMetaItemChooseBinding.inflate(LayoutInflater.from(context))
    init {
        //addView(binding.root)
        dialogMetaItemChoose.setContentView(metaChooserBinding.root)
        dialogMetaItemChoose.setOnDismissListener {
            viewModel.isNewMetaItemMood = null
        }

    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        Log.d("Gestures", "MetaTopPanelPortrait.onAttachedToWindow()")
        binding.ibtnDreamDetailsAddMood.setOnClickListener {
            Log.d("Gestures", " ibtnDreamDetailsAddMood.setOnClickListener")
            showMetaItemChooser(viewModel.moods)

        }
        binding.ibtnDreamDetailsAddPlace.setOnClickListener {
            Log.d("Gestures", " ibtnDreamDetailsAddPlace.setOnClickListener")
        }
        viewModel.initMoods(binding.ibtnDreamDetailsAddMood.id)
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
                //binding.tvDreamDetailsMoods.text = "WHAT"
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

    fun showMetaItemChooser(metaItems: List<String>){
        viewModel.isNewMetaItemMood = true

        /*if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // this is optional
        }*/
        metaChooserBinding.tvMetaItemChooserTitle.text = "Выбрать настроение"
        val metaAdapter = MetaListAdapter(this)
        metaAdapter.setMetaItems(metaItems)

        metaChooserBinding.rvMetaItemChooser.adapter = metaAdapter
        val layoutManager = LinearLayoutManager(context)
        metaChooserBinding.rvMetaItemChooser.layoutManager = layoutManager
        metaChooserBinding.rvMetaItemChooser.setHasFixedSize(true)
        metaChooserBinding.ibtnMetaItemChooserClose.setOnClickListener {
            Log.d("Gestures", " ibtnMetaItemChooserClose.setOnClickListener")
            dialogMetaItemChoose.dismiss()
        }
        metaChooserBinding.ibtnDreamDetailsAddItem.setOnClickListener{
            showDialogMetaItemAppend()
        }
        metaChooserBinding.etMetaItemChooserFilter.addTextChangedListener { input ->
            Log.d("Gestures", " etMetaItemChooserFilter.addTextChangedListener, ${input}")
            metaAdapter.filter.filter(input)
        }
        dialogMetaItemChoose.show();

    }

    fun showDialogMetaItemAppend(){
        val dialog = Dialog(context)
        val metaAppenderBinding = DialogMetaItemAppendBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(metaAppenderBinding.root)
        metaAppenderBinding.tvAppendDialogTitle.text = "Добавить настроение"
        metaAppenderBinding.ibtnAppendDialogConfirm.setOnClickListener {
            listener.onConfirmItem(metaAppenderBinding.etAppendDialogFilter.text.toString())
            dialog.dismiss()
        }
        dialog.show()
    }

    fun showDialogConfirmMetaItemDelete(action: String, item: String){
        val dialog = Dialog(context)
        val confirmActionBinding = DialogConfirmActionBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(confirmActionBinding.root)
        confirmActionBinding.apply {
            tvConfirmActionDialogTitle.text = "Подтвердить удаление"
            tvConfirmActionDialogAction.text = action
            ibtnConfirmActionDialogConfirm.setOnClickListener {
                viewModel.deleteMood(item)
                dialog.dismiss()
            }
        }
        dialog.show()
    }
    private fun showDialogConfirmDreamMoodDelete(action: String, item: TextView){
        val dialog = Dialog(context)
        val confirmActionBinding = DialogConfirmActionBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(confirmActionBinding.root)
        confirmActionBinding.apply {
            tvConfirmActionDialogTitle.text = "Подтвердить удаление"
            tvConfirmActionDialogAction.text = action
            ibtnConfirmActionDialogConfirm.setOnClickListener {
                viewModel.deleteDreamMood(item.text.toString(), item.id)
                binding.flowDreamDetailsMoods.referencedIds = viewModel.dreamMoodsIds.toIntArray()
                binding.root.removeView(item)
                binding.flowDreamDetailsMoods.requestLayout()
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    override fun onMetaItemChoose(item: String) {
        Log.d("Gestures", "MetaTopPanelPortrait.onMetaItemChoose()")
        // здесь добавить текст с нажатой в диалоге кнопке в Flow, т. е. создать Button и добавить id
        if (viewModel.isNewMetaItemMood != null && viewModel.isNewMetaItemMood!!){
            viewModel.addDreamMood(item, View.generateViewId())
            val newMetaItem = TextView(context)
            newMetaItem.text = item
            newMetaItem.id = viewModel.dreamMoodsIds[viewModel.dreamMoodsIds.size - 2]
            newMetaItem.setOnLongClickListener {
                Log.d("Gestures", "newMetaItem.setOnLongClickListener, ${newMetaItem.text}")
                showDialogConfirmDreamMoodDelete("Удалить выбранное настроение \"$item\"?", newMetaItem)
                true
            }
            binding.root.addView(newMetaItem)
            //binding.flowDreamDetailsMoods.addView(newMetaItem)
            binding.flowDreamDetailsMoods.referencedIds = viewModel.dreamMoodsIds.toIntArray()
            binding.flowDreamDetailsMoods.requestLayout()
            dialogMetaItemChoose.dismiss()
        }
    }

    override fun onMetaItemDelete(item: String) {
        showDialogConfirmMetaItemDelete("Удалить настроение \"${item}\"?", item)

    }

}