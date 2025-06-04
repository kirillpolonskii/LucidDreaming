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
        //this.isInteractionEnabled = false

    }

    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        // место расхождения
        //super.onInterceptTouchEvent(event) // - ничего не добавляет
        //super.onTouchEvent(event) // - ничего не добавляет
        //return true
        //return false - сразу в onTouch в RV
        /*event?.let {
            if (it.y > ibtnDreamsListSearch.y *//*&& it.x < ibtnDreamsListSearch.x*//*){
                Log.d("Gestures", "DreamsListMotionLayout.onInterceptTouchEvent, event.rawY > ibtnDreamsListSearch.top")
                //this.isInteractionEnabled = false
                //return super.onInterceptTouchEvent(event) // - идёт в rv.onTouch, но запускает анимацию
                //return super.onTouchEvent(event) //
                //return true - сразу идёт в onTouchEvent, rv не двигается
                return false // - обработка события продолжается и доходит до rv, который скролится вместе с панелью
            }
        }*/
        return when (event?.action){
            MotionEvent.ACTION_DOWN -> {
                Log.d("Gestures", "DreamsListMotionLayout.onInterceptTouchEvent, DOWN")
                Log.d("Gestures", "event.y = ${event.y}")
                // место расхождения
                //super.onInterceptTouchEvent(event)
                //super.onTouchEvent(event)
                //true
                //false
                if (isEventInsideTargetView(event, ibtnDreamsListSearch)){
                    Log.d("Gestures", " isEventInsideTargetView(event, ibtnDreamsListSearch)")
                    touchInsideBtnSearch = true
                    ibtnDreamsListSearch.isPressed = true
                    vwDreamsListButtonStripe.isPressed = true
                    // место расхождения
                    //super.onInterceptTouchEvent(event)
                    super.onTouchEvent(event)
                    //true
                    //false
                }
                else when (this.currentState){
                    R.id.dreamslist_toppanel_hidden -> {
                        Log.d("Gestures", " dreamslist_toppanel_hidden")
                        false
                    }
                    R.id.dreamslist_toppanel_expanded -> {
                        if (event.y <= ibtnDreamsListSearch.y + ibtnDreamsListSearch.height){
                            Log.d("Gestures", " dreamslist_toppanel_expanded, event.rawY <= ibtnDreamsListSearch.top")
                            // место расхождения
                            //super.onInterceptTouchEvent(event) // - так вообще не работало
                            //super.onTouchEvent(event)
                            //true
                            false
                        }
                        else {
                            Log.d("Gestures", " dreamslist_toppanel_expanded, event.rawY > ibtnDreamsListSearch.top")
                            touchOutsideTopPanel = true
                            // место расхождения
                            //super.onInterceptTouchEvent(event)
                            //super.onTouchEvent(event)
                            //true
                            false
                        }
                    }
                    else -> {
                        Log.d("Gestures", "state else")
                        // место расхождения
                        //super.onInterceptTouchEvent(event)
                        //super.onTouchEvent(event)
                        true
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
                super.onInterceptTouchEvent(event)
                /*if (event.y <= ibtnDreamsListSearch.y){
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
                }*/

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
                    vwDreamsListButtonStripe.isPressed = false
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
                            touchOutsideTopPanel = false
                            touchInsideBtnSearch = false
                            ibtnDreamsListSearch.isPressed = false
                            vwDreamsListButtonStripe.isPressed = false
                            if (moveWasCaptured){
                                Log.d("Gestures", "moveWasCaptured")
                                moveWasCaptured = false
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
                            touchInsideBtnSearch = false
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
                            ibtnDreamsListSearch.isPressed = false
                            vwDreamsListButtonStripe.isPressed = false
                            touchOutsideTopPanel = false
                            if (moveWasCaptured){
                                Log.d("Gestures", "moveWasCaptured")
                                moveWasCaptured = false
                                // место расхождения
                                //super.onInterceptTouchEvent(event)
                                super.onTouchEvent(event)
                                //true
                                //false
                            }
                            else{
                                Log.d("Gestures", "not moveWasCaptured")
                                moveWasCaptured = false
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
                            moveWasCaptured = false
                            touchInsideBtnSearch = false
                            if (event.y <= ibtnDreamsListSearch.y){
                                Log.d("Gestures", " event.rawY <= ibtnDreamsListSearch.top")
                                //super.onTouchEvent(event)
                                false
                            }
                            else {
                                Log.d("Gestures", " event.rawY > ibtnDreamsListSearch.top")
                                touchOutsideTopPanel = false
                                //super.onInterceptTouchEvent(event)
                                this.transitionToState(R.id.dreamslist_toppanel_hidden, 100)
                                false
                                //true
                            }
                            // место расхождения
                            //super.onInterceptTouchEvent(event)
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
                if (event.y > ibtnDreamsListSearch.y + ibtnDreamsListSearch.height){
                    Log.d("Gestures", " event.rawY > ibtnDreamsListSearch.top")
                    //return super.onInterceptTouchEvent(event) // -
                    //return super.onTouchEvent(event) // - ничего не происходит, rv не скроллится
                    //return true // - ничего не происходит, rv не скроллится
                    //return false // - ничего не происходит, rv не скроллится
                    this.transitionToState(R.id.dreamslist_toppanel_hidden, 100)
                    false
                }
                else {
                    Log.d("Gestures", " event.rawY <= ibtnDreamsListSearch.top + height")
                    super.onTouchEvent(event)
                }
                // место расхождения
                //super.onInterceptTouchEvent(event)

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
                vwDreamsListButtonStripe.isPressed = false
                when (this.currentState){
                    R.id.dreamslist_toppanel_hidden -> {
                        if (touchInsideBtnSearch){
                            Log.d("Gestures", "dreamslist_toppanel_hidden, touchInsideBtnSearchl")
                            if (moveWasCaptured){
                                Log.d("Gestures", "moveWasCaptured")
                                moveWasCaptured = false
                                touchOutsideTopPanel = false
                                touchInsideBtnSearch = false
                                //this.transitionToState(R.id.dreamslist_toppanel_expanded, 100)
                                // место расхождения
                                //super.onInterceptTouchEvent(event)
                                //super.onTouchEvent(event)
                                //true
                                //false
                            }
                            else{
                                Log.d("Gestures", "not moveWasCaptured")
                                moveWasCaptured = false
                                touchOutsideTopPanel = false
                                touchInsideBtnSearch = false
                                this.transitionToState(R.id.dreamslist_toppanel_expanded, 100)
                                // место расхождения
                                //super.onInterceptTouchEvent(event)
                                //super.onTouchEvent(event)
                                //true
                                //false
                            }
                        }
                        else {
                            Log.d("Gestures", "dreamslist_toppanel_hidden, not touchInsideBtnSearchl")
                            // место расхождения
                            //super.onInterceptTouchEvent(event)
                            //super.onTouchEvent(event)
                            //true
                            //false
                        }
                    }
                    R.id.dreamslist_toppanel_expanded -> {
                        if (touchInsideBtnSearch){
                            Log.d("Gestures", " dreamslist_toppanel_expanded, touchInsideBtnSearchl")
                            touchOutsideTopPanel = false
                            touchInsideBtnSearch = false
                            if (moveWasCaptured){
                                Log.d("Gestures", " moveWasCaptured")
                                // место расхождения
                                //super.onInterceptTouchEvent(event)
                                //super.onTouchEvent(event)
                                //true
                                //false
                            }
                            else{
                                Log.d("Gestures", " not moveWasCaptured")
                                this.transitionToState(R.id.dreamslist_toppanel_hidden, 100)
                                // место расхождения
                                //super.onInterceptTouchEvent(event)
                                //super.onTouchEvent(event)
                                //true
                                //false
                            }
                            moveWasCaptured = false
                        }
                        else {
                            Log.d("Gestures", "dreamslist_toppanel_expanded, not touchInsideBtnSearchl")
                            if (touchOutsideTopPanel){
                                this.transitionToState(R.id.dreamslist_toppanel_hidden, 100)
                                touchOutsideTopPanel = false
                            }
                            // место расхождения
                            //super.onInterceptTouchEvent(event)
                            //super.onTouchEvent(event)
                            //true
                            //false
                        }
                    }
                    else -> {
                        Log.d("Gestures", "state else, touchInsideBtnSearch=$touchInsideBtnSearch")
                        moveWasCaptured = false
                        touchOutsideTopPanel = false
                        touchInsideBtnSearch = false
                        //true
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