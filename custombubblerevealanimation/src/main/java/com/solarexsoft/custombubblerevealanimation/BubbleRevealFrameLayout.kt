package com.solarexsoft.custombubblerevealanimation

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.FrameLayout

/**
 * <pre>
 *    Author: houruhou
 *    CreatAt: 19:43/2020/7/5
 *    Desc:
 * </pre>
 */

class BubbleRevealFrameLayout(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs), BubbleRevealWidget {
    private val helper = BubbleRevealHelper(this)

    override fun buildBubbleRevealCache() {
        helper.buildBubbleRevealCache()
    }

    override fun destroyBubbleRevealCache() {
        helper.destroyBubbleRevealCache()
    }

    override fun getBubbleRevealInfo(): BubbleRevealInfo? {
        return helper.getBubbleRevealInfo()
    }

    override fun setBubbleRevealInfo(bubbleRevealInfo: BubbleRevealInfo) {
        helper.setBubbleRevealInfo(bubbleRevealInfo)
    }

    override fun draw(canvas: Canvas) {
        if (helper != null) {
            helper.draw(canvas)
        } else {
            super.draw(canvas)
        }
    }

    override fun isOpaque(): Boolean {
        return if (helper != null) {
            helper.isOpaque()
        } else {
            super.isOpaque()
        }
    }

    override fun actualDraw(canvas: Canvas) {
        super.draw(canvas)
    }

    override fun actualIsOpaque(): Boolean {
        return super.isOpaque()
    }

}