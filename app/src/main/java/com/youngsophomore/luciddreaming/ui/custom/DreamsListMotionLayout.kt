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

class DreamsListMotionLayout: MotionLayout {
    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet? = null): super(context, attrs)
    constructor(context: Context,
                attrs: AttributeSet? = null,
                defStyleAttr: Int = 0): super(context, attrs, defStyleAttr)
    lateinit var ibtnDreamsListSearch: ImageButton
    lateinit var rvDreamsList: RecyclerView
    var moveWasCaptured = false
    var touchOutsideTopPanel = false
    var touchInsideBtnSearch = false

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        //this.isInteractionEnabled = false

    }

    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        ibtnDreamsListSearch = this.getChildAt(2) as ImageButton
        rvDreamsList = this.getChildAt(3) as RecyclerView
        // место расхождения
        //super.onInterceptTouchEvent(event) // - ничего не добавляет
        //super.onTouchEvent(event) // - ничего не добавляет
        //return true
        //return false - сразу в onTouch в RV
        event?.let {
            if (it.y > ibtnDreamsListSearch.y + ibtnDreamsListSearch.height / 2 &&
                it.x < ibtnDreamsListSearch.x){
                Log.d("Gestures", "DreamsListMotionLayout.onInterceptTouchEvent, event.rawY > ibtnDreamsListSearch.top")
                //this.isInteractionEnabled = false
                //return super.onInterceptTouchEvent(event) // - идёт в rv.onTouch, но запускает анимацию
                //return super.onTouchEvent(event) //
                //return true - сразу идёт в onTouchEvent, rv не двигается
                return false // - обработка события продолжается и доходит до rv, который скролится вместе с панелью
            }
        }
        return when (event?.action){
            MotionEvent.ACTION_DOWN -> {
                Log.d("Gestures", "DreamsListMotionLayout.onInterceptTouchEvent, DOWN")
                Log.d("Gestures", "event.y = ${event.y}")
                // место расхождения
                super.onInterceptTouchEvent(event)
                //super.onTouchEvent(event)
                //true
                //false
                if (isEventInsideTargetView(event, ibtnDreamsListSearch)){
                    Log.d("Gestures", "isEventInsideTargetView(event, ibtnDreamsListSearch)")
                    touchInsideBtnSearch = true
                    ibtnDreamsListSearch.isPressed = true
                    // место расхождения
                    //super.onInterceptTouchEvent(event)
                    //super.onTouchEvent(event)
                    true
                    //false
                }
                else when (this.currentState){
                    R.id.dreamslist_toppanel_hidden -> {
                        if (event.y <= ibtnDreamsListSearch.y + ibtnDreamsListSearch.height / 2){
                            Log.d("Gestures", "dreamslist_toppanel_hidden, event.rawY <= ibtnDreamsListSearch.top")
                            // место расхождения
                            //super.onInterceptTouchEvent(event)
                            //super.onTouchEvent(event)
                            //true
                            false
                        }
                        else {
                            Log.d("Gestures", "dreamslist_toppanel_hidden, event.rawY > ibtnDreamsListSearch.top")
                            // место расхождения
                            //super.onInterceptTouchEvent(event)
                            //super.onTouchEvent(event)
                            //true
                            false
                        }
                    }
                    R.id.dreamslist_toppanel_expanded -> {
                        if (event.y <= ibtnDreamsListSearch.y + ibtnDreamsListSearch.height / 2){
                            Log.d("Gestures", "dreamslist_toppanel_expanded, event.rawY <= ibtnDreamsListSearch.top")
                            // место расхождения
                            //super.onInterceptTouchEvent(event) // - так вообще не работало
                            //super.onTouchEvent(event)
                            true
                            //false
                        }
                        else {
                            Log.d("Gestures", "dreamslist_toppanel_expanded, event.rawY > ibtnDreamsListSearch.top")
                            touchOutsideTopPanel = true
                            // место расхождения
                            super.onInterceptTouchEvent(event)
                            //super.onTouchEvent(event)
                            //true
                            //false
                        }
                    }
                    else -> {
                        Log.d("Gestures", "state else")
                        // место расхождения
                        super.onInterceptTouchEvent(event)
                        //super.onTouchEvent(event)
                        //true
                        //false
                    }
                }

            }
            MotionEvent.ACTION_MOVE -> {
                Log.d("Gestures", "DreamsListMotionLayout.onInterceptTouchEvent, MOVE")
                // место расхождения
                //super.onInterceptTouchEvent(event)
                //super.onTouchEvent(event)
                //true
                //false
                moveWasCaptured = true
                if (event.y <= ibtnDreamsListSearch.y + ibtnDreamsListSearch.height / 2){
                    Log.d("Gestures", "event.rawY <= ibtnDreamsListSearch.top")
                    // место расхождения
                    //super.onInterceptTouchEvent(event)
                    //super.onTouchEvent(event)
                    true
                    //false
                }
                else {
                    Log.d("Gestures", "event.rawY > ibtnDreamsListSearch.top")
                    // место расхождения
                    //super.onInterceptTouchEvent(event)
                    //super.onTouchEvent(event) // - так ничего не двигалось
                    //true
                    false// - так панель выдвигалась вместе с прокруткой rv
                }

            }
            MotionEvent.ACTION_UP -> {
                Log.d("Gestures", "DreamsListMotionLayout.onInterceptTouchEvent, UP")
                // место расхождения
                //super.onInterceptTouchEvent(event)
                //super.onTouchEvent(event)
                //true
                //false
                if (!moveWasCaptured && touchOutsideTopPanel){
                    Log.d("Gestures", "!moveWasCaptured && touchOutsideTopPanel")
                    this.transitionToState(R.id.dreamslist_toppanel_hidden, 100)
                    moveWasCaptured = false
                    touchOutsideTopPanel = false
                    touchInsideBtnSearch = false
                    ibtnDreamsListSearch.isPressed = false
                    // место расхождения
                    super.onInterceptTouchEvent(event)
                    //super.onTouchEvent(event)
                    //true
                    //false
                }
                when (this.currentState){
                    R.id.dreamslist_toppanel_hidden -> {
                        if (touchInsideBtnSearch){
                            Log.d("Gestures", "dreamslist_toppanel_hidden, touchInsideBtnSearchl")
                            if (moveWasCaptured){
                                Log.d("Gestures", "moveWasCaptured")
                                moveWasCaptured = false
                                touchOutsideTopPanel = false
                                ibtnDreamsListSearch.isPressed = false
                                this.transitionToState(R.id.dreamslist_toppanel_expanded, 100)
                                // место расхождения
                                //super.onInterceptTouchEvent(event)
                                super.onTouchEvent(event)
                                //true
                                //false
                            }
                            else{
                                Log.d("Gestures", "not moveWasCaptured")
                                moveWasCaptured = false
                                touchOutsideTopPanel = false
                                ibtnDreamsListSearch.isPressed = false
                                this.transitionToState(R.id.dreamslist_toppanel_expanded, 100)
                                // место расхождения
                                //super.onInterceptTouchEvent(event)
                                //super.onTouchEvent(event)
                                true
                                //false
                            }
                        }
                        else {
                            Log.d("Gestures", "dreamslist_toppanel_hidden, not touchInsideBtnSearchl")
                            // место расхождения
                            super.onInterceptTouchEvent(event)
                            //super.onTouchEvent(event)
                            //true
                            //false
                        }
                    }
                    R.id.dreamslist_toppanel_expanded -> {
                        if (touchInsideBtnSearch){
                            Log.d("Gestures", "dreamslist_toppanel_expanded, touchInsideBtnSearchl")
                            if (moveWasCaptured){
                                Log.d("Gestures", "moveWasCaptured")
                                moveWasCaptured = false
                                touchOutsideTopPanel = false
                                ibtnDreamsListSearch.isPressed = false
                                // место расхождения
                                //super.onInterceptTouchEvent(event)
                                super.onTouchEvent(event)
                                //true
                                //false
                            }
                            else{
                                Log.d("Gestures", "not moveWasCaptured")
                                moveWasCaptured = false
                                touchOutsideTopPanel = false
                                ibtnDreamsListSearch.isPressed = false
                                this.transitionToState(R.id.dreamslist_toppanel_hidden, 100)
                                // место расхождения
                                super.onInterceptTouchEvent(event)
                                //super.onTouchEvent(event)
                                //true
                                //false
                            }
                        }
                        else {
                            Log.d("Gestures", "dreamslist_toppanel_expanded, not touchInsideBtnSearchl")
                            // место расхождения
                            super.onInterceptTouchEvent(event)
                            //super.onTouchEvent(event)
                            //true
                            //false
                        }
                    }
                    else -> {
                        Log.d("Gestures", "state else")
                        // место расхождения
                        super.onInterceptTouchEvent(event)
                        //super.onTouchEvent(event)
                        //true
                        //false
                    }
                }

            }
            else -> {
                Log.d("Gestures", "DreamsListMotionLayout.onInterceptTouchEvent, else")
                // место расхождения
                super.onInterceptTouchEvent(event)
                //super.onTouchEvent(event)
                //true
                //false
            }
        }
    }

    /*override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action){
            MotionEvent.ACTION_DOWN -> {
                Log.d("Gestures", "DreamsListMotionLayout.dispatchTouchEvent, DOWN")
                super.dispatchTouchEvent(event)
            }
            MotionEvent.ACTION_MOVE -> {
                Log.d("Gestures", "DreamsListMotionLayout.dispatchTouchEvent, MOVE")
                moveWasCaptured = true
                super.dispatchTouchEvent(event)
            }
            MotionEvent.ACTION_UP -> {
                Log.d("Gestures", "DreamsListMotionLayout.dispatchTouchEvent, UP")
                super.dispatchTouchEvent(event)
            }
        }
        return super.dispatchTouchEvent(event)
    }*/

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        // место расхождения
        //super.onInterceptTouchEvent(event)
        //super.onTouchEvent(event)
        //true
        //false
        /*event?.let {
            if (it.y > ibtnDreamsListSearch.y + ibtnDreamsListSearch.height / 2){
                Log.d("Gestures", "DreamsListMotionLayout.MotionEvent, event.rawY > ibtnDreamsListSearch.top")
                //return super.onInterceptTouchEvent(event) // -
                //return super.onTouchEvent(event) //
                //return true // -
                return false // -
            }
        }*/
        return when (event?.action){
            MotionEvent.ACTION_DOWN -> {
                Log.d("Gestures", "DreamsListMotionLayout.onTouchEvent, DOWN")
                if (event.y > ibtnDreamsListSearch.y + ibtnDreamsListSearch.height / 2 &&
                    event.x < ibtnDreamsListSearch.x){
                    Log.d("Gestures", " event.rawY > ibtnDreamsListSearch.top")
                    //return super.onInterceptTouchEvent(event) // -
                    //return super.onTouchEvent(event) // - ничего не происходит, rv не скроллится
                    //return true // - ничего не происходит, rv не скроллится
                    //return false // - ничего не происходит, rv не скроллится

                }
                // место расхождения
                //super.onInterceptTouchEvent(event)
                super.onTouchEvent(event)
                //true
                //false
            }
            MotionEvent.ACTION_MOVE -> {
                Log.d("Gestures", "DreamsListMotionLayout.onTouchEvent, MOVE")
                moveWasCaptured = true
                // место расхождения
                //super.onInterceptTouchEvent(event)
                super.onTouchEvent(event)
                //true
                //false
            }
            MotionEvent.ACTION_UP -> {
                Log.d("Gestures", "DreamsListMotionLayout.onTouchEvent, UP")
                // место расхождения
                //super.onInterceptTouchEvent(event)
                //super.onTouchEvent(event)
                //true
                //false
                ibtnDreamsListSearch.isPressed = false
                when (this.currentState){
                    R.id.dreamslist_toppanel_hidden -> {
                        if (touchInsideBtnSearch){
                            Log.d("Gestures", "dreamslist_toppanel_hidden, touchInsideBtnSearchl")
                            if (moveWasCaptured){
                                Log.d("Gestures", "moveWasCaptured")
                                moveWasCaptured = false
                                touchOutsideTopPanel = false
                                touchInsideBtnSearch = false
                                ibtnDreamsListSearch.isPressed = false
                                //this.transitionToState(R.id.dreamslist_toppanel_expanded, 100)
                                // место расхождения
                                //super.onInterceptTouchEvent(event)
                                super.onTouchEvent(event)
                                //true
                                //false
                            }
                            else{
                                Log.d("Gestures", "not moveWasCaptured")
                                moveWasCaptured = false
                                touchOutsideTopPanel = false
                                touchInsideBtnSearch = false
                                ibtnDreamsListSearch.isPressed = false
                                this.transitionToState(R.id.dreamslist_toppanel_expanded, 100)
                                // место расхождения
                                //super.onInterceptTouchEvent(event)
                                super.onTouchEvent(event)
                                //true
                                //false
                            }
                        }
                        else {
                            Log.d("Gestures", "dreamslist_toppanel_hidden, not touchInsideBtnSearchl")
                            // место расхождения
                            //super.onInterceptTouchEvent(event)
                            super.onTouchEvent(event)
                            //true
                            //false
                        }
                    }
                    R.id.dreamslist_toppanel_expanded -> {
                        if (touchInsideBtnSearch){
                            Log.d("Gestures", "dreamslist_toppanel_expanded, touchInsideBtnSearchl")
                            if (moveWasCaptured){
                                Log.d("Gestures", "moveWasCaptured")
                                moveWasCaptured = false
                                touchOutsideTopPanel = false
                                touchInsideBtnSearch = false
                                ibtnDreamsListSearch.isPressed = false
                                // место расхождения
                                //super.onInterceptTouchEvent(event)
                                //super.onTouchEvent(event)
                                true
                                //false
                            }
                            else{
                                Log.d("Gestures", "not moveWasCaptured")
                                moveWasCaptured = false
                                touchOutsideTopPanel = false
                                touchInsideBtnSearch = false
                                ibtnDreamsListSearch.isPressed = false
                                this.transitionToState(R.id.dreamslist_toppanel_hidden, 100)
                                // место расхождения
                                //super.onInterceptTouchEvent(event)
                                //super.onTouchEvent(event)
                                true
                                //false
                            }
                        }
                        else {
                            Log.d("Gestures", "dreamslist_toppanel_expanded, not touchInsideBtnSearchl")
                            // место расхождения
                            //super.onInterceptTouchEvent(event)
                            //super.onTouchEvent(event)
                            true
                            //false
                        }
                    }
                    else -> {
                        Log.d("Gestures", "state else, touchInsideBtnSearch=$touchInsideBtnSearch")
                        true
                    }
                }
                super.onTouchEvent(event)
            }
            else -> {
                Log.d("Gestures", "DreamsListMotionLayout.onTouchEvent, else")
                // место расхождения
                //super.onInterceptTouchEvent(event)
                super.onTouchEvent(event)
                //true
                //false
            }
        }

    }
    private fun isEventInsideTargetView(ev: MotionEvent, v: View): Boolean {
        return ev.x >= v.left && ev.x <= v.left + v.width
                && ev.y >= v.top && ev.y <= v.top + v.height
    }
}