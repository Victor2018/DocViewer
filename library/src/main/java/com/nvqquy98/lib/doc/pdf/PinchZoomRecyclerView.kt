package com.nvqquy98.lib.doc.pdf

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.OnScaleGestureListener
import android.view.animation.DecelerateInterpolator
import androidx.core.view.GestureDetectorCompat
import androidx.recyclerview.widget.RecyclerView
import com.nvqquy98.lib.doc.R
import kotlin.math.max
import kotlin.math.min

/**
 * 默认缩放比只能为1
 * 缩放动画时长暂时没有根据缩放比例改动
 */
@SuppressLint("ClickableViewAccessibility")
class PinchZoomRecyclerView : RecyclerView {
    companion object {
        private const val TAG = "PinchZoomRecyclerView"
        // constant
        private const val DEFAULT_SCALE_DURATION = 300
        private const val DEFAULT_SCALE_FACTOR = 1f
        private const val DEFAULT_MAX_SCALE_FACTOR = 3.0f
        private const val DEFAULT_MIN_SCALE_FACTOR = 0.5f
        private const val PROPERTY_SCALE = "scale"
        private const val PROPERTY_TRANX = "tranX"
        private const val PROPERTY_TRANY = "tranY"
        private const val INVALID_TOUCH_POSITION = -1f
    }
    // touch detector
    var mScaleDetector: ScaleGestureDetector? = null
    var mGestureDetector: GestureDetectorCompat? = null

    // draw param
    var mViewWidth: Float = 0f // 宽度
    var mViewHeight: Float = 0f // 高度
    var mTranX: Float = 0f // x偏移量
    var mTranY: Float = 0f // y偏移量
    var mScaleFactor: Float = 0f // 缩放系数

    // touch param
    var mActivePointerId: Int = MotionEvent.INVALID_POINTER_ID // 有效的手指id
    var mLastTouchX: Float = 0f // 上一次触摸位置 X
    var mLastTouchY: Float = 0f // 上一次触摸位置 Y

    // control param
    var isScaling: Boolean = false // 是否正在缩放
    private var mIsEnableScaled: Boolean = false // 是否支持缩放

    // zoom param
    var mScaleAnimator: ValueAnimator? = null // 缩放动画
    var mScaleCenterX: Float = 0f // 缩放中心 X
    var mScaleCenterY: Float = 0f // 缩放中心 Y
    var mMaxTranX: Float = 0f // 当前缩放系数下最大的X偏移量
    var mMaxTranY: Float = 0f // 当前缩放系数下最大的Y偏移量

    // config param
    var mMaxScaleFactor: Float = 0f // 最大缩放系数
    var mMinScaleFactor: Float = 0f // 最小缩放系数
    var mDefaultScaleFactor: Float = 0f // 默认缩放系数 双击缩小后的缩放系数 暂不支持小于1
    var mScaleDuration: Int = 0 // 缩放时间 ms

    constructor(context: Context?) : super(context!!) {
        init(null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context!!, attrs, defStyle) {
        init(attrs)
    }

    private fun init(attr: AttributeSet?) {
        mScaleDetector = ScaleGestureDetector(context, ScaleListener())
        mGestureDetector = GestureDetectorCompat(context, GestureListener())
        if (attr != null) {
            val a = context
                .obtainStyledAttributes(attr, R.styleable.ZoomRecyclerView, 0, 0)
            mMinScaleFactor =
                a.getFloat(R.styleable.ZoomRecyclerView_min_scale, DEFAULT_MIN_SCALE_FACTOR)
            mMaxScaleFactor =
                a.getFloat(R.styleable.ZoomRecyclerView_max_scale, DEFAULT_MAX_SCALE_FACTOR)
            mDefaultScaleFactor = a
                .getFloat(R.styleable.ZoomRecyclerView_default_scale, DEFAULT_SCALE_FACTOR)
            mScaleFactor = mDefaultScaleFactor
            mScaleDuration = a.getInteger(
                R.styleable.ZoomRecyclerView_zoom_duration,
                DEFAULT_SCALE_DURATION
            )
            a.recycle()
        } else {
            // init param with default
            mMaxScaleFactor = DEFAULT_MAX_SCALE_FACTOR
            mMinScaleFactor = DEFAULT_MIN_SCALE_FACTOR
            mDefaultScaleFactor = DEFAULT_SCALE_FACTOR
            mScaleFactor = mDefaultScaleFactor
            mScaleDuration = DEFAULT_SCALE_DURATION
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        mViewWidth = MeasureSpec.getSize(widthMeasureSpec).toFloat()
        mViewHeight = MeasureSpec.getSize(heightMeasureSpec).toFloat()
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        if (!mIsEnableScaled) {
            return super.onTouchEvent(ev)
        }
        var retVal = mScaleDetector!!.onTouchEvent(ev)
        retVal = mGestureDetector!!.onTouchEvent(ev) || retVal
        val action = ev.actionMasked
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                val pointerIndex = ev.actionIndex
                val x = ev.getX(pointerIndex)
                val y = ev.getY(pointerIndex)
                // Remember where we started (for dragging)
                mLastTouchX = x
                mLastTouchY = y
                // Save the ID of this pointer (for dragging)
                mActivePointerId = ev.getPointerId(0)
            }

            MotionEvent.ACTION_MOVE -> {
                try {
                    // Find the index of the active pointer and fetch its position
                    val pointerIndex = ev.findPointerIndex(mActivePointerId)
                    val x = ev.getX(pointerIndex)
                    val y = ev.getY(pointerIndex)
                    if (!isScaling && mScaleFactor > 1) { // 缩放时不做处理
                        // Calculate the distance moved
                        val dx = x - mLastTouchX
                        val dy = y - mLastTouchY
                        setTranslateXY(mTranX + dx, mTranY + dy)
                        correctTranslateXY()
                    }
                    invalidate()
                    // Remember this touch position for the next move event
                    mLastTouchX = x
                    mLastTouchY = y
                } catch (e: Exception) {
                    val x = ev.x
                    val y = ev.y
                    if (!isScaling && mScaleFactor > 1 && mLastTouchX != INVALID_TOUCH_POSITION) { // 缩放时不做处理
                        // Calculate the distance moved
                        val dx = x - mLastTouchX
                        val dy = y - mLastTouchY
                        setTranslateXY(mTranX + dx, mTranY + dy)
                        correctTranslateXY()
                    }
                    invalidate()
                    // Remember this touch position for the next move event
                    mLastTouchX = x
                    mLastTouchY = y
                }
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                mActivePointerId = MotionEvent.INVALID_POINTER_ID
                mLastTouchX = INVALID_TOUCH_POSITION
                mLastTouchY = INVALID_TOUCH_POSITION
            }

            MotionEvent.ACTION_POINTER_UP -> {
                val pointerIndex = ev.actionIndex
                val pointerId = ev.getPointerId(pointerIndex)
                if (pointerId == mActivePointerId) {
                    // This was our active pointer going up. Choose a new
                    // active pointer and adjust accordingly.
                    val newPointerIndex = if (pointerIndex == 0) 1 else 0
                    mLastTouchX = ev.getX(newPointerIndex)
                    mLastTouchY = ev.getY(newPointerIndex)
                    mActivePointerId = ev.getPointerId(newPointerIndex)
                }
            }
        }
        return super.onTouchEvent(ev) || retVal
    }

    @SuppressLint("WrongConstant")
    override fun dispatchDraw(canvas: Canvas) {
        canvas.save()
        canvas.translate(mTranX, mTranY)
        canvas.scale(mScaleFactor, mScaleFactor)
        // 所有子view都会缩放和平移
        super.dispatchDraw(canvas)
        canvas.restore()
    }

    private fun setTranslateXY(tranX: Float, tranY: Float) {
        mTranX = tranX
        mTranY = tranY
    }

    // 当scale 大于 1 时修正action move的位置
    private fun correctTranslateXY() {
        val correctXY = correctTranslateXY(mTranX, mTranY)
        mTranX = correctXY[0]
        mTranY = correctXY[1]
    }

    private fun correctTranslateXY(x: Float, y: Float): FloatArray {
        var x = x
        var y = y
        if (mScaleFactor <= 1) {
            return floatArrayOf(x, y)
        }
        if (x > 0.0f) {
            x = 0.0f
        } else if (x < mMaxTranX) {
            x = mMaxTranX
        }
        if (y > 0.0f) {
            y = 0.0f
        } else if (y < mMaxTranY) {
            y = mMaxTranY
        }
        return floatArrayOf(x, y)
    }

    private fun zoom(startVal: Float, endVal: Float) {
        if (mScaleAnimator == null) {
            newZoomAnimation()
        }
        if (mScaleAnimator!!.isRunning) {
            return
        }
        // set Value
        mMaxTranX = mViewWidth - (mViewWidth * endVal)
        mMaxTranY = mViewHeight - (mViewHeight * endVal)
        val startTranX = mTranX
        val startTranY = mTranY
        var endTranX = mTranX - (endVal - startVal) * mScaleCenterX
        var endTranY = mTranY - (endVal - startVal) * mScaleCenterY
        val correct = correctTranslateXY(endTranX, endTranY)
        endTranX = correct[0]
        endTranY = correct[1]
        val scaleHolder = PropertyValuesHolder
            .ofFloat(PROPERTY_SCALE, startVal, endVal)
        val tranXHolder = PropertyValuesHolder
            .ofFloat(PROPERTY_TRANX, startTranX, endTranX)
        val tranYHolder = PropertyValuesHolder
            .ofFloat(PROPERTY_TRANY, startTranY, endTranY)
        mScaleAnimator!!.setValues(scaleHolder, tranXHolder, tranYHolder)
        mScaleAnimator!!.setDuration(mScaleDuration.toLong())
        mScaleAnimator!!.start()
    }

    private fun newZoomAnimation() {
        mScaleAnimator = ValueAnimator()
        mScaleAnimator!!.interpolator = DecelerateInterpolator()
        mScaleAnimator!!.addUpdateListener { animation -> // update scaleFactor & tranX & tranY
            mScaleFactor = animation.getAnimatedValue(PROPERTY_SCALE) as Float
            setTranslateXY(
                animation.getAnimatedValue(PROPERTY_TRANX) as Float,
                animation.getAnimatedValue(PROPERTY_TRANY) as Float
            )
            invalidate()
        }
        // set listener to update scale flag
        mScaleAnimator!!.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                isScaling = true
            }

            override fun onAnimationEnd(animation: Animator) {
                isScaling = false
            }

            override fun onAnimationCancel(animation: Animator) {
                isScaling = false
            }
        })
    }

    // handle scale event
    private inner class ScaleListener : OnScaleGestureListener {
        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            return true
        }

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            val mLastScale = mScaleFactor
            mScaleFactor *= detector.scaleFactor
            // 修正scaleFactor
            mScaleFactor =
                max(mMinScaleFactor.toDouble(), min(mScaleFactor.toDouble(), mMaxScaleFactor.toDouble()))
                    .toFloat()
            mMaxTranX = mViewWidth - (mViewWidth * mScaleFactor)
            mMaxTranY = mViewHeight - (mViewHeight * mScaleFactor)
            mScaleCenterX = detector.focusX
            mScaleCenterY = detector.focusY
            val offsetX = mScaleCenterX * (mLastScale - mScaleFactor)
            val offsetY = mScaleCenterY * (mLastScale - mScaleFactor)
            setTranslateXY(mTranX + offsetX, mTranY + offsetY)
            isScaling = true
            invalidate()
            return true
        }

        override fun onScaleEnd(detector: ScaleGestureDetector) {
            if (mScaleFactor <= mDefaultScaleFactor) {
                mScaleCenterX = -mTranX / (mScaleFactor - 1)
                mScaleCenterY = -mTranY / (mScaleFactor - 1)
                mScaleCenterX = if (java.lang.Float.isNaN(mScaleCenterX)) 0f else mScaleCenterX
                mScaleCenterY = if (java.lang.Float.isNaN(mScaleCenterY)) 0f else mScaleCenterY
                zoom(mScaleFactor, mDefaultScaleFactor)
            }
            isScaling = false
        }
    }

    private inner class GestureListener : SimpleOnGestureListener() {
        override fun onDoubleTap(e: MotionEvent): Boolean {
            val startFactor = mScaleFactor
            val endFactor: Float
            if (mScaleFactor == mDefaultScaleFactor) {
                mScaleCenterX = e.x
                mScaleCenterY = e.y
                endFactor = mMaxScaleFactor
            } else {
                mScaleCenterX = if (mScaleFactor == 1f) e.x else -mTranX / (mScaleFactor - 1)
                mScaleCenterY = if (mScaleFactor == 1f) e.y else -mTranY / (mScaleFactor - 1)
                endFactor = mDefaultScaleFactor
            }
            zoom(startFactor, endFactor)
            val retVal = super.onDoubleTap(e)
            return retVal
        }
    }

    // public method
    fun setEnableScale(enable: Boolean) {
        if (mIsEnableScaled == enable) {
            return
        }
        this.mIsEnableScaled = enable
        // 禁用了 恢复比例1
        if (!mIsEnableScaled && mScaleFactor != 1f) {
            zoom(mScaleFactor, 1f)
        }
    }

    fun isEnableScale(): Boolean {
        return mIsEnableScaled
    }


}
