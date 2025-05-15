package com.youngsophomore.luciddreaming.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.youngsophomore.luciddreaming.R
import com.youngsophomore.luciddreaming.databinding.LayoutDreamdetailsToppanelportraitBinding
import com.youngsophomore.luciddreaming.ui.fragments.DreamDetailsFragment
import com.youngsophomore.luciddreaming.ui.viewmodels.DreamDetailsViewModel

class MetaTopPanelPortrait @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr)
    //, View.OnClickListener
{
    private var callback: MetaPanelCallback? = null
    private var binding: LayoutDreamdetailsToppanelportraitBinding
    private val viewModel: DreamDetailsViewModel by lazy {
        parent as DreamDetailsFragment
        (context as DreamDetailsFragment).viewModel
    }
    init {
        //setOnClickListener(this)
        // true - панель показывается, false - панель исчезает
        binding = LayoutDreamdetailsToppanelportraitBinding.inflate(LayoutInflater.from(context), this, true)
        //addView(binding.root)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        Log.d("Gestures", "MetaTopPanelPortrait.onAttachedToWindow()")
        binding.tglgrDreamDetailsPOV.setOnClickListener {
            Log.d("Gestures", " tglgrDreamDetailsPOV.setOnClickListener")
        }
        binding.tvDreamDetailsPOV.setOnClickListener {
            Log.d("Gestures", " tvDreamDetailsPOV.setOnClickListener")
        }
        binding.tvDreamDetailsMoods.setOnTouchListener { v, event ->
            Log.d("Gestures", " tvDreamDetailsMoods.setOnTouchListener")
            performClick()
            true
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

    /*override fun onClick(v: View?) {
        Log.d("Gestures", "MetaTopPanelPortrait.onClick(), v.id=${v?.id.toString()}")
        when (v){
            binding.tglgrDreamDetailsPOV -> {
                Log.d("Gestures", " binding.tglgrDreamDetailsPOV")
            }
            binding.tvDreamDetailsMoods -> {
                Log.d("Gestures", " binding.tvDreamDetailsMoods")
            }
        }
    }*/
    /*fun onClick(listener: MetaPanelCallback){
        Log.d("Gestures", " MetaTopPanelPortrait.onClick")
        callback = listener
    }*/

}

interface MetaPanelCallback {
    fun onClick()
}