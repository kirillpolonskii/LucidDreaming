package com.youngsophomore.luciddreaming.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.recyclerview.widget.RecyclerView
import com.youngsophomore.luciddreaming.R
import com.youngsophomore.luciddreaming.utils.isEventInsideTargetView

class DreamsListMotionLayout: MotionLayout {
    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet? = null): super(context, attrs)
    constructor(context: Context,
                attrs: AttributeSet? = null,
                defStyleAttr: Int = 0): super(context, attrs, defStyleAttr)

    lateinit var vwDreamsListButtonStripe: View
    lateinit var ibtnDreamsListSearch: ImageButton
    lateinit var rvDreamsList: RecyclerView
    var moveWasCaptured = false
    var touchOutsideTopPanel = false
    var touchInsideBtnSearch = false

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        vwDreamsListButtonStripe = this.getChildAt(2) as View
        ibtnDreamsListSearch = this.getChildAt(3) as ImageButton
        rvDreamsList = this.getChildAt(1) as RecyclerView

    }

    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        return when (event?.action){
            MotionEvent.ACTION_DOWN -> {
                if (isEventInsideTargetView(event, ibtnDreamsListSearch)){
                    touchInsideBtnSearch = true
                    ibtnDreamsListSearch.isPressed = true
                    vwDreamsListButtonStripe.isPressed = true
                    super.onTouchEvent(event)
                }
                else when (this.currentState){
                    R.id.dreamslist_toppanel_hidden -> {
                        false
                    }
                    R.id.dreamslist_toppanel_expanded -> {
                        if (event.y <= ibtnDreamsListSearch.y + ibtnDreamsListSearch.height){
                            false
                        }
                        else {
                            touchOutsideTopPanel = true
                            false
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
                    this.transitionToState(R.id.dreamslist_toppanel_hidden, 100)
                    moveWasCaptured = false
                    touchOutsideTopPanel = false
                    touchInsideBtnSearch = false
                    ibtnDreamsListSearch.isPressed = false
                    vwDreamsListButtonStripe.isPressed = false
                    super.onInterceptTouchEvent(event)
                }
                when (this.currentState){
                    R.id.dreamslist_toppanel_hidden -> {
                        if (touchInsideBtnSearch){
                            touchOutsideTopPanel = false
                            touchInsideBtnSearch = false
                            ibtnDreamsListSearch.isPressed = false
                            vwDreamsListButtonStripe.isPressed = false
                            if (moveWasCaptured){
                                moveWasCaptured = false
                                this.transitionToState(R.id.dreamslist_toppanel_expanded, 100)
                                super.onTouchEvent(event)
                            }
                            else{
                                moveWasCaptured = false
                                this.transitionToState(R.id.dreamslist_toppanel_expanded, 100)
                                true
                            }
                        }
                        else {
                            touchInsideBtnSearch = false
                            super.onInterceptTouchEvent(event)
                        }
                    }
                    R.id.dreamslist_toppanel_expanded -> {
                        if (touchInsideBtnSearch){
                            ibtnDreamsListSearch.isPressed = false
                            vwDreamsListButtonStripe.isPressed = false
                            touchOutsideTopPanel = false
                            if (moveWasCaptured){
                                moveWasCaptured = false
                                super.onTouchEvent(event)
                            }
                            else{
                                moveWasCaptured = false
                                this.transitionToState(R.id.dreamslist_toppanel_hidden, 100)
                                super.onInterceptTouchEvent(event)
                            }
                        }
                        else {
                            moveWasCaptured = false
                            touchInsideBtnSearch = false
                            if (event.y <= ibtnDreamsListSearch.y){
                                false
                            }
                            else {
                                touchOutsideTopPanel = false
                                this.transitionToState(R.id.dreamslist_toppanel_hidden, 100)
                                false
                            }
                        }
                    }
                    else -> {
                        super.onInterceptTouchEvent(event)
                    }
                }

            }
            else -> {
                super.onInterceptTouchEvent(event)
            }
        }
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return when (event?.action){
            MotionEvent.ACTION_DOWN -> {
                
                if (event.y > ibtnDreamsListSearch.y + ibtnDreamsListSearch.height){
                    this.transitionToState(R.id.dreamslist_toppanel_hidden, 100)
                    false
                }
                else {
                    super.onTouchEvent(event)
                }
            }
            MotionEvent.ACTION_MOVE -> {
                moveWasCaptured = true
                super.onTouchEvent(event)
            }
            MotionEvent.ACTION_UP -> {
                ibtnDreamsListSearch.isPressed = false
                vwDreamsListButtonStripe.isPressed = false
                when (this.currentState){
                    R.id.dreamslist_toppanel_hidden -> {
                        if (touchInsideBtnSearch){
                            if (moveWasCaptured){
                                moveWasCaptured = false
                                touchOutsideTopPanel = false
                                touchInsideBtnSearch = false
                            }
                            else{
                                
                                moveWasCaptured = false
                                touchOutsideTopPanel = false
                                touchInsideBtnSearch = false
                                this.transitionToState(R.id.dreamslist_toppanel_expanded, 100)
                            }
                        }
                        else {
                        }
                    }
                    R.id.dreamslist_toppanel_expanded -> {
                        if (touchInsideBtnSearch){
                            
                            touchOutsideTopPanel = false
                            touchInsideBtnSearch = false
                            if (moveWasCaptured){
                            }
                            else{
                                this.transitionToState(R.id.dreamslist_toppanel_hidden, 100)
                            }
                            moveWasCaptured = false
                        }
                        else {
                            
                            if (touchOutsideTopPanel){
                                this.transitionToState(R.id.dreamslist_toppanel_hidden, 100)
                                touchOutsideTopPanel = false
                            }
                        }
                    }
                    else -> {
                        moveWasCaptured = false
                        touchOutsideTopPanel = false
                        touchInsideBtnSearch = false
                    }
                }
                super.onTouchEvent(event)
            }
            else -> {
                super.onTouchEvent(event)
            }
        }

    }
}