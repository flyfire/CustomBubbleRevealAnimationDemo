package com.solarexsoft.custombubblerevealanimationdemo

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.graphics.Point
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.android.material.circularreveal.CircularRevealCompat
import com.solarexsoft.custombubblerevealanimation.BubbleRevealCompat
import com.solarexsoft.custombubblerevealanimation.BubbleRevealFrameLayout
import com.solarexsoft.custombubblerevealanimation.BubbleRevealInfo

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val frameLayout = findViewById<BubbleRevealFrameLayout>(R.id.bubbleRevealFrameLayout)
        val tv = findViewById<TextView>(R.id.tv)
        val point = Point()
        windowManager.defaultDisplay.getSize(point)
        tv.setOnClickListener {
            val bubbleRevealInfo = BubbleRevealInfo(
                (tv.y + tv.height/2).toInt(),
                point.y * 3 / 4,
                point.x,
                point.x.toFloat(),
                tv.y,
                point.x.toFloat(),
                (tv.y + tv.height).toFloat(),
                (point.x - tv.width/2).toFloat(),
                (point.x - tv.width).toFloat()
            )
            val animator = BubbleRevealCompat.createBubbleReveal(frameLayout, bubbleRevealInfo)
            val animatorListener = BubbleRevealCompat.createBubbleRevealListener(frameLayout)
            animator.addListener(animatorListener)
            animator.start()
        }
    }
}
