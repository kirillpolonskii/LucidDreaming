package com.youngsophomore.luciddreaming.ui.custom

import android.app.Dialog
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.Orientation
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.youngsophomore.luciddreaming.R
import com.youngsophomore.luciddreaming.databinding.DialogMetaItemChooserBinding
import com.youngsophomore.luciddreaming.databinding.ItemMetaBinding
import com.youngsophomore.luciddreaming.databinding.LayoutDreamdetailsPanelportraitBinding
import com.youngsophomore.luciddreaming.ui.adapters.MetaListAdapter
import com.youngsophomore.luciddreaming.ui.fragments.DreamDetailsFragment
import com.youngsophomore.luciddreaming.ui.viewmodels.DreamDetailsViewModel

class MetaTopPanelPortrait @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr)
    //, View.OnClickListener
{
    private var binding: LayoutDreamdetailsPanelportraitBinding
    private val viewModel: DreamDetailsViewModel by lazy {
        ViewModelProvider(findViewTreeViewModelStoreOwner()!!).get(DreamDetailsViewModel::class.java)
    }
    init {
        // true - панель показывается, false - панель исчезает
        binding = LayoutDreamdetailsPanelportraitBinding.inflate(LayoutInflater.from(context), this, true)
        //addView(binding.root)
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
        val dialog = Dialog(context)
        val metaChooserBinding = DialogMetaItemChooserBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(metaChooserBinding.root);
        /*if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // this is optional
        }*/
        metaChooserBinding.tvMetaItemChooserTitle.text = "Выбрать настроение"
        val metaAdapter = MetaListAdapter()
        metaAdapter.setMetaItems(metaItems)
        metaChooserBinding.rvMetaItemChooser.adapter = metaAdapter
        val layoutManager = LinearLayoutManager(context)

        //layoutManager.isAutoMeasureEnabled = true
        metaChooserBinding.rvMetaItemChooser.layoutManager = layoutManager
        metaChooserBinding.rvMetaItemChooser.setHasFixedSize(true)
        metaChooserBinding.ibtnMetaItemChooserClose.setOnClickListener {
            Log.d("Gestures", " ibtnMetaItemChooserClose.setOnClickListener")
        }
        dialog.show();
    }

    /*fun onClick(listener: MetaPanelCallback){
        Log.d("Gestures", " MetaTopPanelPortrait.onClick")
        callback = listener
    }*/

}

interface MetaPanelCallback {
    fun onClick()
}