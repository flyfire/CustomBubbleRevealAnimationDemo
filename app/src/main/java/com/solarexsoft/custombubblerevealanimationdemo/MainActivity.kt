package com.solarexsoft.custombubblerevealanimationdemo

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.graphics.Point
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.solarexsoft.custombubblerevealanimation.BubbleRevealCompat
import com.solarexsoft.custombubblerevealanimation.BubbleRevealFrameLayout
import com.solarexsoft.custombubblerevealanimation.BubbleRevealInfo

class MainActivity : AppCompatActivity() {
    var openOrClose = true // true open false close
    var animator: Animator? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val frameLayout = findViewById<BubbleRevealFrameLayout>(R.id.bubbleRevealFrameLayout)
        val tv = findViewById<TextView>(R.id.tv)
        val point = Point()
        windowManager.defaultDisplay.getSize(point)
        val startInfo = BubbleRevealInfo(
            (tv.y + tv.height/2).toInt(),
            tv.height,
            tv.width,
            point.x.toFloat(),
            tv.y,
            point.x.toFloat(),
            (tv.y + tv.height).toFloat(),
            (point.x - tv.width/4).toFloat(),
            (point.x - tv.width).toFloat()
        )
        val endInfo = BubbleRevealInfo(
            (tv.y + tv.height/2).toInt(),
            point.y * 2,
            point.x * 2,
            point.x.toFloat(),
            (tv.y + tv.height/2 - point.y).toFloat(),
            point.x.toFloat(),
            (point.x - (point.x * 2/4)).toFloat(),
            (point.x - (point.x * 2)).toFloat()
        )
        val animatorListener = BubbleRevealCompat.createBubbleRevealListener(frameLayout)
        tv.setOnClickListener{
            if (openOrClose) {
                frameLayout.setBubbleRevealInfo(startInfo)
                animator?.cancel()
                animator = BubbleRevealCompat.createBubbleReveal(frameLayout, startInfo, endInfo)
                animator?.addListener(animatorListener)
                frameLayout.visibility = View.VISIBLE
                animator?.start()
            } else {
                frameLayout.setBubbleRevealInfo(endInfo)
                animator?.cancel()
                animator = BubbleRevealCompat.createBubbleReveal(frameLayout, endInfo, startInfo)
                animator?.addListener(animatorListener)
                animator?.addListener(object : AnimatorListenerAdapter(){
                    override fun onAnimationEnd(animation: Animator?) {
                        frameLayout.visibility = View.GONE
                    }
                })
                animator?.start()
            }
        }
    }
}
