package com.youngsophomore.luciddreaming.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
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

    lateinit var vwDreamDetailsButtonStripe: View
    lateinit var ibtnDreamDetailsShowMeta: ImageButton
    lateinit var etDreamDetailsTitle: EditText
    lateinit var etDreamDetailsContent: EditText
    var moveWasCaptured = false
    var touchOutsideTopPanel = false
    var touchInsideBtnShowMeta = false
    val imm: InputMethodManager = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        vwDreamDetailsButtonStripe = this.getChildAt(1)
        ibtnDreamDetailsShowMeta = this.getChildAt(2) as ImageButton
        etDreamDetailsTitle = this.getChildAt(4) as EditText
        etDreamDetailsContent = this.getChildAt(5) as EditText
    }

    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        return when (event?.action){
            MotionEvent.ACTION_DOWN -> {
                Log.d("Gestures", "DreamDetailsMotionLayout.onInterceptTouchEvent, DOWN")
                Log.d("Gestures", "event.y = ${event.y}")
                Log.d("Gestures", "ibtnDreamDetailsShowMeta.y + ibtnDreamDetailsShowMeta.height / 2 = " +
                        "${ibtnDreamDetailsShowMeta.y + ibtnDreamDetailsShowMeta.height / 2}")
                if (isEventInsideTargetView(event, ibtnDreamDetailsShowMeta)){
                    Log.d("Gestures", "isEventInsideTargetView(event, ibtnDreamDetailsShowMeta)")
                    touchInsideBtnShowMeta = true
                    ibtnDreamDetailsShowMeta.isPressed = true
                    vwDreamDetailsButtonStripe.isPressed = true
                    if (this.currentState == R.id.dreamdetails_toppanel_hidden) {
                        etDreamDetailsContent.clearFocus()
                        imm.hideSoftInputFromWindow(etDreamDetailsContent.windowToken, 0)
                    }
                    super.onTouchEvent(event)
                }
                else when (this.currentState){
                    R.id.dreamdetails_toppanel_hidden -> {
                        Log.d("Gestures", " dreamdetails_toppanel_hidden")
                        //super.onInterceptTouchEvent(event)
                        //true
                        if (etDreamDetailsContent.hasFocus()){
                            etDreamDetailsContent.clearFocus()
                            imm.hideSoftInputFromWindow(etDreamDetailsContent.windowToken, 0)
                        }
                        false
                        /*if (event.y <= ibtnDreamDetailsShowMeta.y + ibtnDreamDetailsShowMeta.height / 2){
                            Log.d("Gestures", "dreamdetails_toppanel_hidden, event.rawY <= ibtnDreamDetailsShowMeta.top")
                            super.onTouchEvent(event)
                        }
                        else {
                            Log.d("Gestures", "dreamdetails_toppanel_hidden, event.rawY > ibtnDreamDetailsShowMeta.top")
                            super.onInterceptTouchEvent(event)
                        }*/
                    }
                    R.id.dreamdetails_toppanel_expanded -> {
                        if (event.y <= ibtnDreamDetailsShowMeta.y){
                            Log.d("Gestures", " dreamdetails_toppanel_expanded, event.rawY <= ibtnDreamDetailsShowMeta.y")
                            //super.onTouchEvent(event)
                            false
                        }
                        else {
                            Log.d("Gestures", " dreamdetails_toppanel_expanded, event.rawY > ibtnDreamDetailsShowMeta.top")
                            touchOutsideTopPanel = true
                            super.onInterceptTouchEvent(event)
                        }
                    }
                    else -> {
                        Log.d("Gestures", " state else")
                        true
                    }
                }

            }
            MotionEvent.ACTION_MOVE -> {
                Log.d("Gestures", "DreamDetailsMotionLayout.onInterceptTouchEvent, MOVE")
                moveWasCaptured = true
                super.onInterceptTouchEvent(event)
            }
            MotionEvent.ACTION_UP -> {
                Log.d("Gestures", "DreamDetailsMotionLayout.onInterceptTouchEvent, UP")
                if (!moveWasCaptured && touchOutsideTopPanel){
                    Log.d("Gestures", " !moveWasCaptured && touchOutsideTopPanel")
                    this.transitionToState(R.id.dreamdetails_toppanel_hidden, 100)
                    moveWasCaptured = false
                    touchOutsideTopPanel = false
                    touchInsideBtnShowMeta = false
                    ibtnDreamDetailsShowMeta.isPressed = false
                    vwDreamDetailsButtonStripe.isPressed = false
                    super.onInterceptTouchEvent(event)
                }
                when (this.currentState){
                    R.id.dreamdetails_toppanel_hidden -> {
                        if (touchInsideBtnShowMeta){
                            Log.d("Gestures", " dreamdetails_toppanel_hidden, touchInsideBtnShowMetal")
                            touchOutsideTopPanel = false
                            touchInsideBtnShowMeta = false
                            ibtnDreamDetailsShowMeta.isPressed = false
                            if (moveWasCaptured){
                                Log.d("Gestures", " moveWasCaptured")
                                moveWasCaptured = false
                                //this.transitionToState(R.id.dreamdetails_toppanel_expanded, 100)
                                super.onTouchEvent(event)
                            }
                            else{
                                Log.d("Gestures", " not moveWasCaptured")
                                moveWasCaptured = false

                                this.transitionToState(R.id.dreamdetails_toppanel_expanded, 100)
                                true
                            }
                        }
                        else {
                            touchInsideBtnShowMeta = false
                            Log.d("Gestures", " dreamdetails_toppanel_hidden, not touchInsideBtnShowMetal")
                            super.onInterceptTouchEvent(event)
                        }
                    }
                    R.id.dreamdetails_toppanel_expanded -> {
                        if (touchInsideBtnShowMeta){
                            Log.d("Gestures", " dreamdetails_toppanel_expanded, touchInsideBtnShowMetal")
                            if (moveWasCaptured){
                                Log.d("Gestures", " moveWasCaptured")
                                moveWasCaptured = false
                                touchOutsideTopPanel = false
                                //touchInsideBtnShowMeta = false
                                ibtnDreamDetailsShowMeta.isPressed = false
                                super.onTouchEvent(event)
                            }
                            else{
                                Log.d("Gestures", " not moveWasCaptured")
                                moveWasCaptured = false
                                touchOutsideTopPanel = false
                                touchInsideBtnShowMeta = false
                                ibtnDreamDetailsShowMeta.isPressed = false
                                this.transitionToState(R.id.dreamdetails_toppanel_hidden, 100)
                                super.onInterceptTouchEvent(event)
                            }
                        }
                        else {
                            Log.d("Gestures", " dreamdetails_toppanel_expanded, not touchInsideBtnShowMetal")
                            touchInsideBtnShowMeta = false
                            moveWasCaptured = false
                            if (event.y <= ibtnDreamDetailsShowMeta.y){
                                Log.d("Gestures", " event.rawY <= ibtnDreamDetailsShowMeta.top")
                                //super.onTouchEvent(event)
                                false
                            }
                            else {
                                Log.d("Gestures", " event.rawY > ibtnDreamDetailsShowMeta.top")
                                touchOutsideTopPanel = false
                                //super.onInterceptTouchEvent(event)
                                this.transitionToState(R.id.dreamdetails_toppanel_hidden, 100)
                                false
                                //true
                            }
                        }
                    }
                    else -> {
                        Log.d("Gestures", " UP, state else")
                        true
                    }
                }

            }
            else -> super.onInterceptTouchEvent(event)
        }

    }
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return when (event?.action){
            MotionEvent.ACTION_DOWN -> {
                Log.d("Gestures", "DreamDetailsMotionLayout.onTouchEvent, DOWN")
                super.onTouchEvent(event)
            }
            MotionEvent.ACTION_MOVE -> {
                Log.d("Gestures", "DreamDetailsMotionLayout.onTouchEvent, MOVE")
                moveWasCaptured = true
                super.onTouchEvent(event)
            }
            MotionEvent.ACTION_UP -> {
                Log.d("Gestures", "DreamDetailsMotionLayout.onTouchEvent, UP")
                ibtnDreamDetailsShowMeta.isPressed = false
                vwDreamDetailsButtonStripe.isPressed = false

                when (this.currentState){
                    R.id.dreamdetails_toppanel_hidden -> {
                        if (touchInsideBtnShowMeta){
                            Log.d("Gestures", "dreamdetails_toppanel_hidden, touchInsideBtnShowMetal")
                            if (moveWasCaptured){
                                Log.d("Gestures", "moveWasCaptured")
                                moveWasCaptured = false
                                touchOutsideTopPanel = false

                                touchInsideBtnShowMeta = false
                                //this.transitionToState(R.id.dreamdetails_toppanel_expanded, 100)
                                //super.onTouchEvent(event)
                            }
                            else{
                                Log.d("Gestures", "not moveWasCaptured")
                                moveWasCaptured = false
                                touchOutsideTopPanel = false
                                touchInsideBtnShowMeta = false
                                this.transitionToState(R.id.dreamdetails_toppanel_expanded, 100)
                                //super.onTouchEvent(event)
                            }
                        }
                        else {
                            Log.d("Gestures", "dreamdetails_toppanel_hidden, not touchInsideBtnShowMetal")
                            //super.onInterceptTouchEvent(event)
                            //super.onTouchEvent(event)
                        }
                    }
                    R.id.dreamdetails_toppanel_expanded -> {
                        if (touchInsideBtnShowMeta){
                            Log.d("Gestures", "dreamdetails_toppanel_expanded, touchInsideBtnShowMetal")
                            if (moveWasCaptured){
                                Log.d("Gestures", "moveWasCaptured")
                            }
                            else{
                                Log.d("Gestures", "not moveWasCaptured")
                                this.transitionToState(R.id.dreamdetails_toppanel_hidden, 100)
                            }
                        }
                        else {
                            Log.d("Gestures", "dreamdetails_toppanel_expanded, not touchInsideBtnShowMetal")
                        }
                        moveWasCaptured = false
                        touchOutsideTopPanel = false
                        touchInsideBtnShowMeta = false
                    }
                    else -> {
                        Log.d("Gestures", " else state")
                        moveWasCaptured = false
                        touchOutsideTopPanel = false
                        touchInsideBtnShowMeta = false
                        //super.onTouchEvent(event)
                        //true
                    }
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