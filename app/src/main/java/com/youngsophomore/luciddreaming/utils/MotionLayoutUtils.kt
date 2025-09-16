package com.youngsophomore.luciddreaming.utils

import android.view.MotionEvent
import android.view.View

fun isEventInsideTargetView(ev: MotionEvent, v: View): Boolean {
    return ev.x >= v.left && ev.x <= v.left + v.width
            && ev.y >= v.top && ev.y <= v.top + v.height
}