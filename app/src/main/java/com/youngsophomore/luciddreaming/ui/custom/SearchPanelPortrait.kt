package com.youngsophomore.luciddreaming.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.youngsophomore.luciddreaming.R

class SearchTopPanelPortrait : ConstraintLayout {

    init {
        View.inflate(context, R.layout.layout_dreamslist_panelportrait, this)
    }
    constructor(context: Context): super(context)
    constructor(context: Context, attrs: AttributeSet? = null): super(context, attrs)
    constructor(context: Context,
                attrs: AttributeSet? = null,
                defStyleAttr: Int = 0): super(context, attrs, defStyleAttr)
}