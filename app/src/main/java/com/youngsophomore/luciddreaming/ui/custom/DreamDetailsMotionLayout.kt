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
import com.youngsophomore.luciddreaming.utils.isEventInsideTargetView

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
                if (isEventInsideTargetView(event, ibtnDreamDetailsShowMeta)){
                    setBtnShowMetaPressed(true)
                    if (this.currentState == R.id.dreamdetails_toppanel_hidden) {
                        etDreamDetailsContent.clearFocus()
                        imm.hideSoftInputFromWindow(etDreamDetailsContent.windowToken, 0)
                    }
                    super.onTouchEvent(event)
                }
                else when (this.currentState){
                    R.id.dreamdetails_toppanel_hidden -> {
                        if (etDreamDetailsContent.hasFocus()){
                            etDreamDetailsContent.clearFocus()
                            imm.hideSoftInputFromWindow(etDreamDetailsContent.windowToken, 0)
                        }
                        false
                    }
                    R.id.dreamdetails_toppanel_expanded -> {
                        if (event.y <= ibtnDreamDetailsShowMeta.y){
                            false
                        }
                        else {
                            touchOutsideTopPanel = true
                            super.onInterceptTouchEvent(event)
                        }
                    }
                    else -> {
                        true
                    }
                }

            }
            MotionEvent.ACTION_MOVE -> {
                moveWasCaptured = true
                super.onInterceptTouchEvent(event)
            }
            MotionEvent.ACTION_UP -> {
                if (!moveWasCaptured && touchOutsideTopPanel){
                    this.transitionToState(R.id.dreamdetails_toppanel_hidden, 100)
                    moveWasCaptured = false
                    touchOutsideTopPanel = false
                    setBtnShowMetaPressed(false)
                    super.onInterceptTouchEvent(event)
                }
                when (this.currentState){
                    R.id.dreamdetails_toppanel_hidden -> {
                        if (ibtnDreamDetailsShowMeta.isPressed){
                            touchOutsideTopPanel = false
                            setBtnShowMetaPressed(false)
                            if (moveWasCaptured){
                                moveWasCaptured = false
                                super.onTouchEvent(event)
                            }
                            else{
                                moveWasCaptured = false
                                this.transitionToState(R.id.dreamdetails_toppanel_expanded, 100)
                                true
                            }
                        }
                        else {
                            super.onInterceptTouchEvent(event)
                        }
                    }
                    R.id.dreamdetails_toppanel_expanded -> {
                        if (ibtnDreamDetailsShowMeta.isPressed){
                            setBtnShowMetaPressed(false)
                            if (moveWasCaptured){
                                moveWasCaptured = false
                                touchOutsideTopPanel = false
                                super.onTouchEvent(event)
                            }
                            else{
                                moveWasCaptured = false
                                touchOutsideTopPanel = false
                                this.transitionToState(R.id.dreamdetails_toppanel_hidden, 100)
                                super.onInterceptTouchEvent(event)
                            }
                        }
                        else {
                            moveWasCaptured = false
                            if (event.y <= ibtnDreamDetailsShowMeta.y){
                                false
                            }
                            else {
                                touchOutsideTopPanel = false
                                this.transitionToState(R.id.dreamdetails_toppanel_hidden, 100)
                                false
                            }
                        }
                    }
                    else -> {
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
                super.onTouchEvent(event)
            }
            MotionEvent.ACTION_MOVE -> {
                moveWasCaptured = true
                super.onTouchEvent(event)
            }
            MotionEvent.ACTION_UP -> {
                when (this.currentState){
                    R.id.dreamdetails_toppanel_hidden -> {
                        if (ibtnDreamDetailsShowMeta.isPressed){
                            setBtnShowMetaPressed(false)
                            if (moveWasCaptured){
                                moveWasCaptured = false
                                touchOutsideTopPanel = false
                            }
                            else{
                                moveWasCaptured = false
                                touchOutsideTopPanel = false
                                this.transitionToState(R.id.dreamdetails_toppanel_expanded, 100)
                            }
                        }
                        else {
                            //super.onInterceptTouchEvent(event)
                            //super.onTouchEvent(event)
                        }
                    }
                    R.id.dreamdetails_toppanel_expanded -> {
                        if (ibtnDreamDetailsShowMeta.isPressed){
                            if (!moveWasCaptured){
                                this.transitionToState(R.id.dreamdetails_toppanel_hidden, 100)
                            }
                        }
                        setBtnShowMetaPressed(false)
                        moveWasCaptured = false
                        touchOutsideTopPanel = false
                    }
                    else -> {
                        moveWasCaptured = false
                        touchOutsideTopPanel = false
                        setBtnShowMetaPressed(false)
                    }
                }
                super.onTouchEvent(event)
            }
            else -> super.onTouchEvent(event)
        }
    }

    private fun setBtnShowMetaPressed(isPressed: Boolean){
        ibtnDreamDetailsShowMeta.isPressed = isPressed
        vwDreamDetailsButtonStripe.isPressed = isPressed
    }
}