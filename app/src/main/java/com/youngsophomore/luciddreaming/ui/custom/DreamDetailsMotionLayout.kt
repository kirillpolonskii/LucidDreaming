package com.youngsophomore.luciddreaming.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import androidx.constraintlayout.motion.widget.MotionLayout
import com.youngsophomore.luciddreaming.R

class DreamDetailsMotionLayout: MotionLayout {
    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet? = null): super(context, attrs)
    constructor(context: Context,
                attrs: AttributeSet? = null,
                defStyleAttr: Int = 0): super(context, attrs, defStyleAttr)

    lateinit var ibtnDreamDetailsShowMeta: ImageButton
    lateinit var etDreamDetailsTitle: EditText
    lateinit var etDreamDetailsContent: EditText
    var moveWasCaptured = false
    var touchOutsideTopPanel = false
    var touchInsideBtnShowMeta = false

    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        ibtnDreamDetailsShowMeta = this.getChildAt(3) as ImageButton
        etDreamDetailsTitle = this.getChildAt(4) as EditText
        etDreamDetailsContent = this.getChildAt(4) as EditText
        return when (event?.action){
            MotionEvent.ACTION_DOWN -> {
                Log.d("Gestures", "MotionLayoutTopPanel.onInterceptTouchEvent, DOWN")
                Log.d("Gestures", "event.y = ${event.y}")
                Log.d("Gestures", "ibtnDreamDetailsShowMeta.y + ibtnDreamDetailsShowMeta.height / 2 = " +
                        "${ibtnDreamDetailsShowMeta.y + ibtnDreamDetailsShowMeta.height / 2}")
                if (isEventInsideTargetView(event, ibtnDreamDetailsShowMeta)){
                    Log.d("Gestures", "isEventInsideTargetView(event, ibtnDreamDetailsShowMeta)")
                    touchInsideBtnShowMeta = true
                    ibtnDreamDetailsShowMeta.isPressed = true
                    super.onTouchEvent(event)
                }
                else when (this.currentState){
                    R.id.toppanel_hidden -> {
                        if (event.y <= ibtnDreamDetailsShowMeta.y + ibtnDreamDetailsShowMeta.height / 2){
                            Log.d("Gestures", "toppanel_hidden, event.rawY <= ibtnDreamDetailsShowMeta.top")
                            super.onTouchEvent(event)
                        }
                        else {
                            Log.d("Gestures", "toppanel_hidden, event.rawY > ibtnDreamDetailsShowMeta.top")
                            super.onInterceptTouchEvent(event)
                        }
                    }
                    R.id.toppanel_expanded -> {
                        if (event.y <= ibtnDreamDetailsShowMeta.y + ibtnDreamDetailsShowMeta.height / 2){
                            Log.d("Gestures", "toppanel_expanded, event.rawY <= ibtnDreamDetailsShowMeta.top")
                            super.onTouchEvent(event)
                        }
                        else {
                            Log.d("Gestures", "toppanel_expanded, event.rawY > ibtnDreamDetailsShowMeta.top")
                            touchOutsideTopPanel = true
                            super.onInterceptTouchEvent(event)
                        }
                    }
                    else -> true
                }



            }
            MotionEvent.ACTION_MOVE -> {
                Log.d("Gestures", "MotionLayoutTopPanel.onInterceptTouchEvent, MOVE")
                moveWasCaptured = true
                super.onInterceptTouchEvent(event)
            }
            MotionEvent.ACTION_UP -> {
                Log.d("Gestures", "MotionLayoutTopPanel.onInterceptTouchEvent, UP")
                if (!moveWasCaptured && touchOutsideTopPanel){
                    Log.d("Gestures", "!moveWasCaptured && touchOutsideTopPanel")
                    this.transitionToState(R.id.toppanel_hidden, 100)
                    moveWasCaptured = false
                    touchOutsideTopPanel = false
                    touchInsideBtnShowMeta = false
                    ibtnDreamDetailsShowMeta.isPressed = false
                    super.onInterceptTouchEvent(event)
                }
                when (this.currentState){
                    R.id.toppanel_hidden -> {
                        if (touchInsideBtnShowMeta){
                            Log.d("Gestures", "toppanel_hidden, touchInsideBtnShowMetal")
                            if (moveWasCaptured){
                                Log.d("Gestures", "moveWasCaptured")
                                moveWasCaptured = false
                                touchOutsideTopPanel = false
                                ibtnDreamDetailsShowMeta.isPressed = false
                                this.transitionToState(R.id.toppanel_expanded, 100)
                                super.onTouchEvent(event)
                            }
                            else{
                                Log.d("Gestures", "not moveWasCaptured")
                                moveWasCaptured = false
                                touchOutsideTopPanel = false
                                ibtnDreamDetailsShowMeta.isPressed = false
                                this.transitionToState(R.id.toppanel_expanded, 100)
                                true
                            }
                        }
                        else {
                            Log.d("Gestures", "toppanel_hidden, not touchInsideBtnShowMetal")
                            super.onInterceptTouchEvent(event)
                        }
                    }
                    R.id.toppanel_expanded -> {
                        if (touchInsideBtnShowMeta){
                            Log.d("Gestures", "toppanel_expanded, touchInsideBtnShowMetal")
                            if (moveWasCaptured){
                                Log.d("Gestures", "moveWasCaptured")
                                moveWasCaptured = false
                                touchOutsideTopPanel = false
                                ibtnDreamDetailsShowMeta.isPressed = false
                                super.onTouchEvent(event)
                            }
                            else{
                                Log.d("Gestures", "not moveWasCaptured")
                                moveWasCaptured = false
                                touchOutsideTopPanel = false
                                ibtnDreamDetailsShowMeta.isPressed = false
                                this.transitionToState(R.id.toppanel_hidden, 100)
                                super.onInterceptTouchEvent(event)
                            }
                        }
                        else {
                            Log.d("Gestures", "toppanel_expanded, not touchInsideBtnShowMetal")
                            super.onInterceptTouchEvent(event)
                        }
                    }
                    else -> true
                }

            }
            else -> super.onInterceptTouchEvent(event)
        }

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return when (event?.action){
            MotionEvent.ACTION_DOWN -> {
                Log.d("Gestures", "MotionLayoutTopPanel.onTouchEvent, DOWN")
                super.onTouchEvent(event)
            }
            MotionEvent.ACTION_MOVE -> {
                Log.d("Gestures", "MotionLayoutTopPanel.onTouchEvent, MOVE")
                moveWasCaptured = true
                super.onTouchEvent(event)
            }
            MotionEvent.ACTION_UP -> {
                Log.d("Gestures", "MotionLayoutTopPanel.onTouchEvent, UP")
                ibtnDreamDetailsShowMeta.isPressed = false
                when (this.currentState){
                    R.id.toppanel_hidden -> {
                        if (touchInsideBtnShowMeta){
                            Log.d("Gestures", "toppanel_hidden, touchInsideBtnShowMetal")
                            if (moveWasCaptured){
                                Log.d("Gestures", "moveWasCaptured")
                                moveWasCaptured = false
                                touchOutsideTopPanel = false
                                ibtnDreamDetailsShowMeta.isPressed = false
                                //this.transitionToState(R.id.toppanel_expanded, 100)
                                super.onTouchEvent(event)
                            }
                            else{
                                Log.d("Gestures", "not moveWasCaptured")
                                moveWasCaptured = false
                                touchOutsideTopPanel = false
                                ibtnDreamDetailsShowMeta.isPressed = false
                                this.transitionToState(R.id.toppanel_expanded, 100)
                                true
                            }
                        }
                        else {
                            Log.d("Gestures", "toppanel_hidden, not touchInsideBtnShowMetal")
                            super.onInterceptTouchEvent(event)
                        }
                    }
                    R.id.toppanel_expanded -> {
                        if (touchInsideBtnShowMeta){
                            Log.d("Gestures", "toppanel_expanded, touchInsideBtnShowMetal")
                            if (moveWasCaptured){
                                Log.d("Gestures", "moveWasCaptured")
                                moveWasCaptured = false
                                touchOutsideTopPanel = false
                                ibtnDreamDetailsShowMeta.isPressed = false
                                super.onTouchEvent(event)
                            }
                            else{
                                Log.d("Gestures", "not moveWasCaptured")
                                moveWasCaptured = false
                                touchOutsideTopPanel = false
                                ibtnDreamDetailsShowMeta.isPressed = false
                                this.transitionToState(R.id.toppanel_hidden, 100)
                                super.onInterceptTouchEvent(event)
                            }
                        }
                        else {
                            Log.d("Gestures", "toppanel_expanded, not touchInsideBtnShowMetal")
                            super.onInterceptTouchEvent(event)
                        }
                    }
                    else -> true
                }
                super.onTouchEvent(event)
            }
            else -> super.onTouchEvent(event)
        }

    }

    private fun isEventInsideTargetView(ev: MotionEvent, v: View): Boolean {
        return ev.x >= v.left && ev.x <= v.left + v.width
                && ev.y >= v.top && ev.y <= v.top + v.height
    }
}