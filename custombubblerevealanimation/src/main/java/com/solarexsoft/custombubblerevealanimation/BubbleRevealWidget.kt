package com.solarexsoft.custombubblerevealanimation

import android.graphics.Canvas
import android.util.Property

/**
 * <pre>
 *    Author: houruhou
 *    CreatAt: 18:44/2020/7/5
 *    Desc:
 * </pre>
 */

interface BubbleRevealWidget : BubbleRevealHelper.Delegate {
    fun draw(canvas: Canvas)
    fun isOpaque(): Boolean
    fun buildBubbleRevealCache()
    fun destroyBubbleRevealCache()
    fun getBubbleRevealInfo(): BubbleRevealInfo?
    fun setBubbleRevealInfo(bubbleRevealInfo: BubbleRevealInfo)
}

class BubbleRevealProperty(name: String) : Property<BubbleRevealWidget, BubbleRevealInfo>(BubbleRevealInfo::class.java, name) {

    companion object {
        val BUBBLE_REVEAL: Property<BubbleRevealWidget, BubbleRevealInfo> = BubbleRevealProperty("BubbleReveal")
    }

    override fun get(`object`: BubbleRevealWidget): BubbleRevealInfo? {
        return `object`.getBubbleRevealInfo()
    }

    override fun set(`object`: BubbleRevealWidget, value: BubbleRevealInfo) {
        `object`.setBubbleRevealInfo(value)
    }
}