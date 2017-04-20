package `fun`.sombrero.app.flow.photopicker

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

class SquareContainer : ViewGroup {

    private var childView: View? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        this.childView = this.childView?: getChildAt(0)

        this.childView?.let {
            it.measure(widthMeasureSpec, widthMeasureSpec)
            val width = View.resolveSize(it.measuredWidth, widthMeasureSpec)
            it.measure(width, width)
            setMeasuredDimension(width, width)
        }
    }

    override fun onLayout(changed: Boolean,
                          leftPosition: Int,
                          topPosition: Int,
                          rightPosition: Int,
                          bottomPosition: Int) {

        if (changed)
            translateChildPosition(leftPosition, topPosition, rightPosition, bottomPosition)
    }

    private fun translateChildPosition(leftPosition: Int, topPosition: Int, rightPosition: Int, bottomPosition: Int) {
        getChildAt(0).layout(0, 0, rightPosition - leftPosition, bottomPosition - topPosition)
    }
}