package com.direct.attendance.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

import androidx.core.view.GestureDetectorCompat
import kotlin.math.abs

open class OnCustomTouchListener(context: Context) : View.OnTouchListener {

    private val mDetector: GestureDetectorCompat

    init {
        mDetector = GestureDetectorCompat(context, GestureListener())
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        return mDetector.onTouchEvent(event)
    }

    open fun onSwipeRight() {
        // "onSwipeRight: Swiped to the RIGHT"
    }

    open fun onSwipeLeft() {
        // "onSwipeLeft: Swiped to the LEFT"
    }

    open fun onSwipeTop() {
        // "onSwipeTop: Swiped to the TOP"
    }

    open fun onSwipeBottom() {
        // "onSwipeBottom: Swiped to the BOTTOM"
    }

    open fun onClick() {
        // "onClick: Clicking in the screen"
    }

    open fun onDoubleClick() {
        // "onClick: Clicking TWO TIMES in the screen"
    }

    open fun onLongClick() {
        // "onLongClick: LONG click in the screen"
    }

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {

        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
            onClick()
            return super.onSingleTapConfirmed(e)
        }

        override fun onDoubleTap(e: MotionEvent): Boolean {
            onDoubleClick()
            return super.onDoubleTap(e)
        }

        override fun onLongPress(e: MotionEvent) {
            onLongClick()
            super.onLongPress(e)
        }

        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            var result = false
            try {
                val diffY = e2.y - e1.y
                val diffX = e2.x - e1.x
                if (abs(diffX) > abs(diffY)) {
                    if (abs(diffX) > SWIPE_THRESHOLD && abs(velocityX) > SWIPE_VELOCITY) {
                        if (diffX > 0) {
                            onSwipeRight()
                        } else {
                            onSwipeLeft()
                        }
                        result = true
                    }
                } else if (abs(diffY) > SWIPE_THRESHOLD && abs(velocityY) > SWIPE_VELOCITY) {
                    if (diffY > 0) {
                        onSwipeBottom()
                    } else {
                        onSwipeTop()
                    }
                    result = true
                }
            } catch (e: Exception) {}

            return result
        }
    }

    companion object {
        private const val SWIPE_THRESHOLD = 100
        private const val SWIPE_VELOCITY = 100
    }
}