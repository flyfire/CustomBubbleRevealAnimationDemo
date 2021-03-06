package com.solarexsoft.custombubblerevealanimation

import android.animation.TypeEvaluator
import android.graphics.*
import android.os.Build.*
import android.util.Log
import android.view.View

/**
 * <pre>
 *    Author: houruhou
 *    CreatAt: 16:58/2020/7/5
 *    Desc:
 * </pre>
 */
data class BubbleRevealInfo(
    var startY: Int = 0,
    var curHeight: Int = 0,
    var curWidth: Int = 0,
    var curTopX: Float = 0.0f,
    var curTopY: Float = 0.0f,
    var curBottomX: Float = 0.0f,
    var curBottomY: Float = 0.0f,
    var curCenterX: Float = 0.0f,
    var curLeftX: Float = 0.0f
) {
    constructor(revealInfo: BubbleRevealInfo) : this(revealInfo.startY, revealInfo.curHeight, revealInfo.curWidth,
        revealInfo.curTopX, revealInfo.curTopY,
        revealInfo.curBottomX, revealInfo.curBottomY,
        revealInfo.curCenterX, revealInfo.curLeftX)
    fun set(revealInfo: BubbleRevealInfo) {
        set(revealInfo.startY, revealInfo.curHeight, revealInfo.curWidth,
            revealInfo.curTopX, revealInfo.curTopY,
            revealInfo.curBottomX, revealInfo.curBottomY,
            revealInfo.curCenterX, revealInfo.curLeftX)
    }

    fun set(
        startY: Int,
        curHeight: Int,
        curWidth: Int,
        curTopX: Float,
        curTopY: Float,
        curBottomX: Float,
        curBottomY: Float,
        curCenterX: Float,
        curLeftX: Float
    ) {
        this.startY = startY
        this.curHeight = curHeight
        this.curWidth = curWidth
        this.curTopX = curTopX
        this.curTopY = curTopY
        this.curBottomX = curBottomX
        this.curBottomY = curBottomY
        this.curCenterX = curCenterX
        this.curLeftX = curLeftX
    }
}
class BubbleRevealEvaluator : TypeEvaluator<BubbleRevealInfo> {
    companion object {
        const val TAG = "BubbleRevealEvaluator"
        val BUBBLE_REVEAL = BubbleRevealEvaluator()
    }
    private val bubbleRevealInfo: BubbleRevealInfo = BubbleRevealInfo()
    override fun evaluate(
        fraction: Float,
        startValue: BubbleRevealInfo?,
        endValue: BubbleRevealInfo?
    ): BubbleRevealInfo {
        startValue?.let {start ->
            endValue?.let { end ->
                val nowWidth = (end.curWidth - start.curWidth) * fraction + start.curWidth
                val nowHeight = (end.curHeight - start.curHeight) * fraction + start.curHeight
                val curTopY = start.startY - (nowHeight/2).toFloat()
                val curBottomY = start.startY + (nowHeight/2).toFloat()
                val curCenterX = start.curTopX - (nowWidth/4).toFloat()
                val curLeftX = start.curTopX - nowWidth.toFloat()
                bubbleRevealInfo.set(
                    start.startY,
                    nowHeight.toInt(),
                    nowWidth.toInt(),
                    start.curTopX,
                    curTopY,
                    start.curBottomX,
                    curBottomY,
                    curCenterX,
                    curLeftX
                )
            }
        }
        return bubbleRevealInfo
    }

}

class BubbleRevealHelper(private val delegate: Delegate, private val bubbleRevealPath: Path = Path(), private val bubbleRevealPaint: Paint = Paint(
    Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG or Paint.DITHER_FLAG)) {

    interface Delegate {
        fun actualDraw(canvas: Canvas)
        fun actualIsOpaque(): Boolean
    }
    companion object {
        const val TAG = "BubbleRevealHelper"
        const val BITMAP_SHADER = 0
        const val CLIP_PATH = 1
        val STRATEGY = if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR2) CLIP_PATH else BITMAP_SHADER
    }
    private val view : View = delegate as View
    private var buildingBubbleRevealCache: Boolean = false
    private var hasBubbleRevealCache: Boolean = false
    private var bubbleRevealInfo: BubbleRevealInfo? = null
    init {
        view.setWillNotDraw(false)
    }

    fun buildBubbleRevealCache() {
        if (STRATEGY == BITMAP_SHADER) {
            buildingBubbleRevealCache = true
            hasBubbleRevealCache = false

            view.buildDrawingCache()
            var bitmap = view.drawingCache
            if (bitmap == null && view.width != 0 && view.height != 0) {
                bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)
                view.draw(canvas)
            }
            if (bitmap != null) {
                bubbleRevealPaint.shader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            }
            buildingBubbleRevealCache = false
            hasBubbleRevealCache = true
        }
    }

    fun destroyBubbleRevealCache() {
        if (STRATEGY == BITMAP_SHADER) {
            hasBubbleRevealCache = false
            view.destroyDrawingCache()
            bubbleRevealPaint.shader = null
            view.invalidate()
        }
    }

    fun setBubbleRevealInfo(bubbleRevealInfo: BubbleRevealInfo?) {
        if (bubbleRevealInfo == null) {
            this.bubbleRevealInfo = null
        } else {
            if (this.bubbleRevealInfo == null) {
                this.bubbleRevealInfo = BubbleRevealInfo(bubbleRevealInfo)
            } else {
                this.bubbleRevealInfo!!.set(bubbleRevealInfo)
            }
        }
        invalidateBubbleRevealInfo()
    }

    private fun invalidateBubbleRevealInfo() {
        if (STRATEGY == CLIP_PATH) {
            bubbleRevealPath.rewind()
            bubbleRevealInfo?.let {
                Log.d(TAG, "$it")
                bubbleRevealPath.moveTo(it.curTopX, it.curTopY)
                bubbleRevealPath.lineTo(it.curCenterX, it.curTopY)
                bubbleRevealPath.cubicTo(it.curLeftX, it.curTopY, it.curLeftX, it.curBottomY, it.curCenterX, it.curBottomY)
                bubbleRevealPath.lineTo(it.curBottomX, it.curBottomY)
                bubbleRevealPath.close()
            }
        }
        view.invalidate()
    }

    fun draw(canvas: Canvas) {
        if (shouldDrawBubbleReveal()) {
            when(STRATEGY) {
                CLIP_PATH -> {
                    val count = canvas.save()
                    canvas.clipPath(bubbleRevealPath)
                    delegate.actualDraw(canvas)
                    canvas.restoreToCount(count)
                }
                BITMAP_SHADER -> {
                    canvas.drawPath(bubbleRevealPath, bubbleRevealPaint)
                }
            }
        } else {
            delegate.actualDraw(canvas)
        }
    }

    private fun shouldDrawBubbleReveal(): Boolean {
        val invalidateBubbleRevealInfo = bubbleRevealInfo == null
        Log.d(TAG, "invalidateInfo = $invalidateBubbleRevealInfo")
        return if (STRATEGY == BITMAP_SHADER) {
            !invalidateBubbleRevealInfo && hasBubbleRevealCache
        } else {
            !invalidateBubbleRevealInfo
        }
    }

    fun getBubbleRevealInfo(): BubbleRevealInfo? {
        if (this.bubbleRevealInfo == null) {
            return null
        }
        return BubbleRevealInfo(bubbleRevealInfo!!)
    }

    fun isOpaque(): Boolean {
        return delegate.actualIsOpaque() && !shouldDrawBubbleReveal()
    }
}