package com.solarexsoft.custombubblerevealanimation

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator

/**
 * <pre>
 *    Author: houruhou
 *    CreatAt: 19:57/2020/7/5
 *    Desc:
 * </pre>
 */

class BubbleRevealCompat private constructor() {

    companion object {
        fun createBubbleRevealListener(view: BubbleRevealWidget): Animator.AnimatorListener{
            return object : AnimatorListenerAdapter(){
                override fun onAnimationStart(animation: Animator?) {
                    view.buildBubbleRevealCache()
                }

                override fun onAnimationEnd(animation: Animator?, isReverse: Boolean) {
                    view.destroyBubbleRevealCache()
                }
            }
        }
        fun createBubbleReveal(view: BubbleRevealWidget, bubbleRevealInfo: BubbleRevealInfo): Animator {
            return ObjectAnimator.ofObject(
                view,
                BubbleRevealProperty.BUBBLE_REVEAL,
                BubbleRevealEvaluator.BUBBLE_REVEAL,
                bubbleRevealInfo
            )
        }
    }


}