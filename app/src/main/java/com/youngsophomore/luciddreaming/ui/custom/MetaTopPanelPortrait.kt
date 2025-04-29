package com.youngsophomore.luciddreaming.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.youngsophomore.luciddreaming.R

class MetaTopPanelPortrait @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.layout_dreamdetails_toppanelportrait, this)
    }
}