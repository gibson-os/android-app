package de.wollis_page.gibsonos.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.PointF
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.OverScroller
import android.widget.Scroller
import androidx.appcompat.widget.AppCompatImageView
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

@Suppress("unused")
class TouchImageView : AppCompatImageView {
    var currentZoom: Float = 0f
        private set

    private var matrix: Matrix? = null
    private var prevMatrix: Matrix? = null

    private enum class State {
        NONE, DRAG, ZOOM, FLING, ANIMATE_ZOOM
    }

    private var state: State? = null

    private var minScale = 0f
    private var maxScale = 0f
    private var superMinScale = 0f
    private var superMaxScale = 0f
    private var m: FloatArray? = null

    private var context: Context? = null
    private var fling: Fling? = null

    private var mScaleType: ScaleType? = null

    private var imageRenderedAtLeastOnce = false
    private var onDrawReady = false

    private var delayedZoomVariables: ZoomVariables? = null

    private var viewWidth = 0
    private var viewHeight = 0
    private var prevViewWidth = 0
    private var prevViewHeight = 0

    private var matchViewWidth = 0f
    private var matchViewHeight = 0f
    private var prevMatchViewWidth = 0f
    private var prevMatchViewHeight = 0f

    private var mScaleDetector: ScaleGestureDetector? = null
    private var mGestureDetector: GestureDetector? = null
    private var doubleTapListener: GestureDetector.OnDoubleTapListener? = null
    private var userTouchListener: OnTouchListener? = null
    private var touchImageViewListener: OnTouchImageViewListener? = null

    var onSwipeLeft: (() -> Unit)? = null;
    var onSwipeRight: (() -> Unit)? = null;
    var onSwipeTop: (() -> Unit)? = null;
    var onSwipeBottom: (() -> Unit)? = null;

    constructor(context: Context) : super(context) {
        sharedConstructing(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        sharedConstructing(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        sharedConstructing(context)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun sharedConstructing(context: Context) {
        super.setClickable(true)
        this.context = context
        mScaleDetector = ScaleGestureDetector(context, ScaleListener())
        mGestureDetector = GestureDetector(context, GestureListener())
        matrix = Matrix()
        prevMatrix = Matrix()
        m = FloatArray(9)
        currentZoom = 1f
        if (mScaleType == null) {
            mScaleType = ScaleType.FIT_CENTER
        }
        minScale = 1f
        maxScale = 100f
        superMinScale = SUPER_MIN_MULTIPLIER * minScale
        superMaxScale = SUPER_MAX_MULTIPLIER * maxScale
        imageMatrix = matrix
        scaleType = ScaleType.MATRIX
        setState(State.NONE)
        onDrawReady = false
        super.setOnTouchListener(PrivateOnTouchListener())
    }

    override fun setOnTouchListener(l: OnTouchListener) {
        userTouchListener = l
    }

    fun setOnTouchImageViewListener(l: OnTouchImageViewListener?) {
        touchImageViewListener = l
    }

    fun setOnDoubleTapListener(l: GestureDetector.OnDoubleTapListener?) {
        doubleTapListener = l
    }

    override fun setImageResource(resId: Int) {
        super.setImageResource(resId)
        savePreviousImageValues()
        fitImageToView()
    }

    override fun setImageBitmap(bm: Bitmap) {
        super.setImageBitmap(bm)
        savePreviousImageValues()
        fitImageToView()
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        savePreviousImageValues()
        fitImageToView()
    }

    override fun setImageURI(uri: Uri?) {
        super.setImageURI(uri)
        savePreviousImageValues()
        fitImageToView()
    }

    override fun setScaleType(type: ScaleType) {
        if (type == ScaleType.FIT_START || type == ScaleType.FIT_END) {
            throw UnsupportedOperationException("TouchImageView does not support FIT_START or FIT_END")
        }
        if (type == ScaleType.MATRIX) {
            super.setScaleType(ScaleType.MATRIX)
        } else {
            mScaleType = type
            if (onDrawReady) {
                setZoom(this)
            }
        }
    }

    override fun getScaleType(): ScaleType {
        return mScaleType!!
    }

    val isZoomed: Boolean
        get() = currentZoom != 1f

    val zoomedRect: RectF
        get() {
            if (mScaleType == ScaleType.FIT_XY) {
                throw UnsupportedOperationException("getZoomedRect() not supported with FIT_XY")
            }
            val topLeft = transformCoordTouchToBitmap(0f, 0f, true)
            val bottomRight =
                transformCoordTouchToBitmap(viewWidth.toFloat(), viewHeight.toFloat(), true)

            val w = drawable.intrinsicWidth.toFloat()
            val h = drawable.intrinsicHeight.toFloat()
            return RectF(topLeft.x / w, topLeft.y / h, bottomRight.x / w, bottomRight.y / h)
        }

    private fun savePreviousImageValues() {
        if (matrix != null && viewHeight != 0 && viewWidth != 0) {
            matrix!!.getValues(m)
            prevMatrix!!.setValues(m)
            prevMatchViewHeight = matchViewHeight
            prevMatchViewWidth = matchViewWidth
            prevViewHeight = viewHeight
            prevViewWidth = viewWidth
        }
    }

    public override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle()
        bundle.putParcelable("instanceState", super.onSaveInstanceState())
        bundle.putFloat("saveScale", currentZoom)
        bundle.putFloat("matchViewHeight", matchViewHeight)
        bundle.putFloat("matchViewWidth", matchViewWidth)
        bundle.putInt("viewWidth", viewWidth)
        bundle.putInt("viewHeight", viewHeight)
        matrix!!.getValues(m)
        bundle.putFloatArray("matrix", m)
        bundle.putBoolean("imageRendered", imageRenderedAtLeastOnce)
        return bundle
    }

    public override fun onRestoreInstanceState(state: Parcelable) {
        if (state is Bundle) {
            val bundle = state
            currentZoom = bundle.getFloat("saveScale")
            m = bundle.getFloatArray("matrix")
            prevMatrix!!.setValues(m)
            prevMatchViewHeight = bundle.getFloat("matchViewHeight")
            prevMatchViewWidth = bundle.getFloat("matchViewWidth")
            prevViewHeight = bundle.getInt("viewHeight")
            prevViewWidth = bundle.getInt("viewWidth")
            imageRenderedAtLeastOnce = bundle.getBoolean("imageRendered")
            super.onRestoreInstanceState(bundle.getParcelable("instanceState"))
            return
        }

        super.onRestoreInstanceState(state)
    }

    override fun onDraw(canvas: Canvas) {
        onDrawReady = true
        imageRenderedAtLeastOnce = true
        if (delayedZoomVariables != null) {
            setZoom(
                delayedZoomVariables!!.scale,
                delayedZoomVariables!!.focusX,
                delayedZoomVariables!!.focusY,
                delayedZoomVariables!!.scaleType
            )
            delayedZoomVariables = null
        }
        super.onDraw(canvas)
    }

    public override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        savePreviousImageValues()
    }

    var maxZoom: Float
        get() = maxScale
        set(max) {
            maxScale = max
            superMaxScale = SUPER_MAX_MULTIPLIER * maxScale
        }

    var minZoom: Float
        get() = minScale
        set(min) {
            minScale = min
            superMinScale = SUPER_MIN_MULTIPLIER * minScale
        }

    fun resetZoom() {
        currentZoom = 1f
        fitImageToView()
    }

    fun setZoom(scale: Float) {
        setZoom(scale, 0.5f, 0.5f)
    }

    fun setZoom(scale: Float, focusX: Float, focusY: Float) {
        setZoom(scale, focusX, focusY, mScaleType)
    }

    fun setZoom(scale: Float, focusX: Float, focusY: Float, scaleType: ScaleType?) {
        if (!onDrawReady) {
            delayedZoomVariables = ZoomVariables(scale, focusX, focusY, scaleType)
            return
        }

        if (scaleType != mScaleType) {
            setScaleType(scaleType!!)
        }
        resetZoom()
        scaleImage(scale.toDouble(), viewWidth / 2f, viewHeight / 2f, true)
        matrix!!.getValues(m)
        m!![Matrix.MTRANS_X] = -((focusX * imageWidth) - (viewWidth * 0.5f))
        m!![Matrix.MTRANS_Y] = -((focusY * imageHeight) - (viewHeight * 0.5f))
        matrix!!.setValues(m)
        fixTrans()
        imageMatrix = matrix
    }

    fun setZoom(img: TouchImageView) {
        val center = img.scrollPosition
        setZoom(img.currentZoom, center!!.x, center.y, img.scaleType)
    }

    val scrollPosition: PointF?
        get() {
            val drawable = drawable ?: return null
            val drawableWidth = drawable.intrinsicWidth
            val drawableHeight = drawable.intrinsicHeight

            val point = transformCoordTouchToBitmap(viewWidth / 2f, viewHeight / 2f, true)
            point.x /= drawableWidth.toFloat()
            point.y /= drawableHeight.toFloat()
            return point
        }

    fun setScrollPosition(focusX: Float, focusY: Float) {
        setZoom(currentZoom, focusX, focusY)
    }

    private fun fixTrans() {
        matrix!!.getValues(m)
        val transX = m!![Matrix.MTRANS_X]
        val transY = m!![Matrix.MTRANS_Y]

        val fixTransX = getFixTrans(transX, viewWidth.toFloat(), imageWidth)
        val fixTransY = getFixTrans(transY, viewHeight.toFloat(), imageHeight)

        if (fixTransX != 0f || fixTransY != 0f) {
            matrix!!.postTranslate(fixTransX, fixTransY)
        }
    }

    private fun fixScaleTrans() {
        fixTrans()
        matrix!!.getValues(m)
        if (imageWidth < viewWidth) {
            m!![Matrix.MTRANS_X] = (viewWidth - imageWidth) / 2
        }

        if (imageHeight < viewHeight) {
            m!![Matrix.MTRANS_Y] = (viewHeight - imageHeight) / 2
        }
        matrix!!.setValues(m)
    }

    private fun getFixTrans(trans: Float, viewSize: Float, contentSize: Float): Float {
        val minTrans: Float
        val maxTrans: Float

        if (contentSize <= viewSize) {
            minTrans = 0f
            maxTrans = viewSize - contentSize
        } else {
            minTrans = viewSize - contentSize
            maxTrans = 0f
        }

        if (trans < minTrans) return -trans + minTrans
        if (trans > maxTrans) return -trans + maxTrans

        return 0f
    }

    private fun getFixDragTrans(delta: Float, viewSize: Float, contentSize: Float): Float {
        if (contentSize <= viewSize) {
            return 0f
        }

        return delta
    }

    private val imageWidth: Float
        get() = matchViewWidth * currentZoom

    private val imageHeight: Float
        get() = matchViewHeight * currentZoom

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val drawable = drawable
        if (drawable == null || drawable.intrinsicWidth == 0 || drawable.intrinsicHeight == 0) {
            setMeasuredDimension(0, 0)
            return
        }

        val drawableWidth = drawable.intrinsicWidth
        val drawableHeight = drawable.intrinsicHeight
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        viewWidth = setViewSize(widthMode, widthSize, drawableWidth)
        viewHeight = setViewSize(heightMode, heightSize, drawableHeight)

        setMeasuredDimension(viewWidth, viewHeight)

        fitImageToView()
    }

    private fun fitImageToView() {
        val drawable = drawable
        if (drawable == null || drawable.intrinsicWidth == 0 || drawable.intrinsicHeight == 0) {
            return
        }
        if (matrix == null || prevMatrix == null) {
            return
        }

        val drawableWidth = drawable.intrinsicWidth
        val drawableHeight = drawable.intrinsicHeight

        var scaleX = viewWidth.toFloat() / drawableWidth
        var scaleY = viewHeight.toFloat() / drawableHeight

        when (mScaleType) {
            ScaleType.CENTER -> {
                scaleY = 1f
                scaleX = scaleY
            }

            ScaleType.CENTER_CROP -> {
                scaleY = max(scaleX.toDouble(), scaleY.toDouble()).toFloat()
                scaleX = scaleY
            }

            ScaleType.CENTER_INSIDE -> {
                run {
                    scaleY =
                        min(1.0, min(scaleX.toDouble(), scaleY.toDouble())).toFloat()
                    scaleX = scaleY
                }
                run {
                    scaleY = min(scaleX.toDouble(), scaleY.toDouble()).toFloat()
                    scaleX = scaleY
                }
            }

            ScaleType.FIT_CENTER -> {
                scaleY = min(scaleX.toDouble(), scaleY.toDouble()).toFloat()
                scaleX = scaleY
            }

            ScaleType.FIT_XY -> {}
            else -> throw UnsupportedOperationException("TouchImageView does not support FIT_START or FIT_END")

        }

        val redundantXSpace = viewWidth - (scaleX * drawableWidth)
        val redundantYSpace = viewHeight - (scaleY * drawableHeight)
        matchViewWidth = viewWidth - redundantXSpace
        matchViewHeight = viewHeight - redundantYSpace
        if (!isZoomed && !imageRenderedAtLeastOnce) {
            matrix!!.setScale(scaleX, scaleY)
            matrix!!.postTranslate(redundantXSpace / 2, redundantYSpace / 2)
            currentZoom = 1f
        } else {
            if (prevMatchViewWidth == 0f || prevMatchViewHeight == 0f) {
                savePreviousImageValues()
            }

            prevMatrix!!.getValues(m)

            m!![Matrix.MSCALE_X] = matchViewWidth / drawableWidth * currentZoom
            m!![Matrix.MSCALE_Y] = matchViewHeight / drawableHeight * currentZoom

            val transX = m!![Matrix.MTRANS_X]
            val transY = m!![Matrix.MTRANS_Y]

            val prevActualWidth = prevMatchViewWidth * currentZoom
            val actualWidth = imageWidth
            translateMatrixAfterRotate(
                Matrix.MTRANS_X,
                transX,
                prevActualWidth,
                actualWidth,
                prevViewWidth,
                viewWidth,
                drawableWidth
            )

            val prevActualHeight = prevMatchViewHeight * currentZoom
            val actualHeight = imageHeight
            translateMatrixAfterRotate(
                Matrix.MTRANS_Y,
                transY,
                prevActualHeight,
                actualHeight,
                prevViewHeight,
                viewHeight,
                drawableHeight
            )

            matrix!!.setValues(m)
        }
        fixTrans()
        imageMatrix = matrix
    }

    private fun setViewSize(mode: Int, size: Int, drawableWidth: Int) = when (mode) {
        MeasureSpec.AT_MOST -> min(drawableWidth.toDouble(), size.toDouble()).toInt()
        MeasureSpec.UNSPECIFIED -> drawableWidth
        MeasureSpec.EXACTLY -> size
        else -> size
    }

    private fun translateMatrixAfterRotate(
        axis: Int,
        trans: Float,
        prevImageSize: Float,
        imageSize: Float,
        prevViewSize: Int,
        viewSize: Int,
        drawableSize: Int
    ) {
        if (imageSize < viewSize) {
            m!![axis] = (viewSize - (drawableSize * m!![Matrix.MSCALE_X])) * 0.5f
        } else if (trans > 0) {
            m!![axis] = -((imageSize - viewSize) * 0.5f)
        } else {
            val percentage =
                ((abs(trans.toDouble()) + (0.5f * prevViewSize)) / prevImageSize).toFloat()
            m!![axis] = -((percentage * imageSize) - (viewSize * 0.5f))
        }
    }

    private fun setState(state: State) {
        this.state = state
    }

    fun canScrollHorizontallyFroyo(direction: Int): Boolean {
        return canScrollHorizontally(direction)
    }

    override fun canScrollHorizontally(direction: Int): Boolean {
        matrix!!.getValues(m)
        val x = m!![Matrix.MTRANS_X]

        return if (imageWidth < viewWidth) {
            false
        } else if (x >= -1 && direction < 0) {
            false
        } else !(abs(x.toDouble()) + viewWidth + 1 >= imageWidth) || direction <= 0
    }

    private inner class GestureListener : SimpleOnGestureListener() {
        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            if (doubleTapListener != null) {
                return doubleTapListener!!.onSingleTapConfirmed(e)
            }
            return performClick()
        }

        override fun onLongPress(e: MotionEvent) {
            performLongClick()
        }

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            if (fling != null) {
                //
                // If a previous fling is still active, it should be cancelled so that two flings
                // are not run simultaenously.
                //
                fling!!.cancelFling()
            }

            fling = Fling(velocityX.toInt(), velocityY.toInt())
            compatPostOnAnimation(fling!!)

            return !this.detectSwipe(e1, e2, velocityX, velocityY) || super.onFling(e1, e2, velocityX, velocityY)
        }

        private fun detectSwipe(
            e1: MotionEvent?,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float,
        ): Boolean {
            if (currentZoom != minScale) {
                return false
            }

            try {
                val diffY = e2.y - e1!!.y
                val diffX = e2.x - e1.x

                if (abs(diffX) > abs(diffY)) {
                    if (abs(diffX) > SWIPE_THRESHOLD && abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight?.invoke()
                        } else {
                            onSwipeLeft?.invoke()
                        }

                        return true
                    }
                } else if (abs(diffY) > SWIPE_THRESHOLD && abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeBottom?.invoke()
                    } else {
                        onSwipeTop?.invoke()
                    }

                    return true
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            }

            return false
        }

        override fun onDoubleTap(e: MotionEvent): Boolean {
            var consumed = false
            if (doubleTapListener != null) {
                consumed = doubleTapListener!!.onDoubleTap(e)
            }
            if (state == State.NONE) {
                val targetZoom = if ((currentZoom == minScale)) 3f else minScale
                val doubleTap = DoubleTapZoom(targetZoom, e.x, e.y, false)
                compatPostOnAnimation(doubleTap)
                consumed = true
            }
            return consumed
        }

        override fun onDoubleTapEvent(e: MotionEvent): Boolean {
            return doubleTapListener != null && doubleTapListener!!.onDoubleTapEvent(e)
        }
    }

    interface OnTouchImageViewListener {
        fun onMove()
    }

    private inner class PrivateOnTouchListener : OnTouchListener {
        private val last = PointF()

        @SuppressLint("ClickableViewAccessibility")
        override fun onTouch(v: View, event: MotionEvent): Boolean {
            mScaleDetector!!.onTouchEvent(event)
            mGestureDetector!!.onTouchEvent(event)
            val curr = PointF(event.x, event.y)

            if (state == State.NONE || state == State.DRAG || state == State.FLING) {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        last.set(curr)
                        if (fling != null) fling!!.cancelFling()
                        setState(State.DRAG)
                    }

                    MotionEvent.ACTION_MOVE -> if (state == State.DRAG) {
                        val deltaX = curr.x - last.x
                        val deltaY = curr.y - last.y
                        val fixTransX = getFixDragTrans(
                            deltaX, viewWidth.toFloat(),
                            imageWidth
                        )
                        val fixTransY = getFixDragTrans(
                            deltaY, viewHeight.toFloat(),
                            imageHeight
                        )
                        matrix!!.postTranslate(fixTransX, fixTransY)
                        fixTrans()
                        last[curr.x] = curr.y
                    }

                    MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> setState(State.NONE)
                }
            }

            imageMatrix = matrix

            if (userTouchListener != null) {
                userTouchListener!!.onTouch(v, event)
            }

            if (touchImageViewListener != null) {
                touchImageViewListener!!.onMove()
            }

            return true
        }
    }

    private inner class ScaleListener : SimpleOnScaleGestureListener() {
        override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
            setState(State.ZOOM)
            return true
        }

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            scaleImage(detector.scaleFactor.toDouble(), detector.focusX, detector.focusY, true)

            if (touchImageViewListener != null) {
                touchImageViewListener!!.onMove()
            }
            return true
        }

        override fun onScaleEnd(detector: ScaleGestureDetector) {
            super.onScaleEnd(detector)
            setState(State.NONE)
            var animateToZoomBoundary = false
            var targetZoom: Float = currentZoom

            if (currentZoom > maxScale) {
                targetZoom = maxScale
                animateToZoomBoundary = true
            } else if (currentZoom < minScale) {
                targetZoom = minScale
                animateToZoomBoundary = true
            }

            if (animateToZoomBoundary) {
                val doubleTap = DoubleTapZoom(targetZoom, viewWidth / 2f, viewHeight / 2f, true)
                compatPostOnAnimation(doubleTap)
            }
        }
    }

    private fun scaleImage(
        deltaScale: Double,
        focusX: Float,
        focusY: Float,
        stretchImageToSuper: Boolean
    ) {
        var newDeltaScale = deltaScale
        val lowerScale: Float
        val upperScale: Float
        if (stretchImageToSuper) {
            lowerScale = superMinScale
            upperScale = superMaxScale
        } else {
            lowerScale = minScale
            upperScale = maxScale
        }

        val origScale = currentZoom
        currentZoom *= newDeltaScale.toFloat()

        if (currentZoom > upperScale) {
            currentZoom = upperScale
            newDeltaScale = (upperScale / origScale).toDouble()
        } else if (currentZoom < lowerScale) {
            currentZoom = lowerScale
            newDeltaScale = (lowerScale / origScale).toDouble()
        }

        matrix!!.postScale(newDeltaScale.toFloat(), newDeltaScale.toFloat(), focusX, focusY)
        fixScaleTrans()
    }

    private inner class DoubleTapZoom(
        targetZoom: Float,
        focusX: Float,
        focusY: Float,
        stretchImageToSuper: Boolean
    ) :
        Runnable {
        private val startTime: Long
        private val startZoom: Float
        private val targetZoom: Float
        private val bitmapX: Float
        private val bitmapY: Float
        private val stretchImageToSuper: Boolean
        private val interpolator = AccelerateDecelerateInterpolator()
        private val startTouch: PointF
        private val endTouch: PointF

        init {
            setState(State.ANIMATE_ZOOM)
            startTime = System.currentTimeMillis()
            this.startZoom = currentZoom
            this.targetZoom = targetZoom
            this.stretchImageToSuper = stretchImageToSuper
            val bitmapPoint = transformCoordTouchToBitmap(focusX, focusY, false)
            this.bitmapX = bitmapPoint.x
            this.bitmapY = bitmapPoint.y

            startTouch = transformCoordBitmapToTouch(bitmapX, bitmapY)
            endTouch = PointF(viewWidth / 2f, viewHeight / 2f)
        }

        override fun run() {
            val t = interpolate()
            val deltaScale = calculateDeltaScale(t)
            scaleImage(deltaScale, bitmapX, bitmapY, stretchImageToSuper)
            translateImageToCenterTouchPosition(t)
            fixScaleTrans()
            imageMatrix = matrix

            if (touchImageViewListener != null) {
                touchImageViewListener!!.onMove()
            }

            if (t < 1f) {
                compatPostOnAnimation(this)
            } else {
                setState(State.NONE)
            }
        }

        fun translateImageToCenterTouchPosition(t: Float) {
            val targetX = startTouch.x + t * (endTouch.x - startTouch.x)
            val targetY = startTouch.y + t * (endTouch.y - startTouch.y)
            val curr = transformCoordBitmapToTouch(bitmapX, bitmapY)
            matrix!!.postTranslate(targetX - curr.x, targetY - curr.y)
        }

        fun interpolate(): Float {
            val currTime = System.currentTimeMillis()
            var elapsed = (currTime - startTime) / 500f
            elapsed = min(1.0, elapsed.toDouble()).toFloat()
            return interpolator.getInterpolation(elapsed)
        }

        fun calculateDeltaScale(t: Float): Double {
            val zoom = (startZoom + t * (targetZoom - startZoom)).toDouble()
            return zoom / currentZoom
        }
    }

    private fun transformCoordTouchToBitmap(x: Float, y: Float, clipToBitmap: Boolean): PointF {
        matrix!!.getValues(m)
        val origW = drawable.intrinsicWidth.toFloat()
        val origH = drawable.intrinsicHeight.toFloat()
        val transX = m!![Matrix.MTRANS_X]
        val transY = m!![Matrix.MTRANS_Y]
        var finalX = ((x - transX) * origW) / imageWidth
        var finalY = ((y - transY) * origH) / imageHeight

        if (clipToBitmap) {
            finalX = min(max(finalX.toDouble(), 0.0), origW.toDouble()).toFloat()
            finalY = min(max(finalY.toDouble(), 0.0), origH.toDouble()).toFloat()
        }

        return PointF(finalX, finalY)
    }

    private fun transformCoordBitmapToTouch(bx: Float, by: Float): PointF {
        matrix!!.getValues(m)
        val origW = drawable.intrinsicWidth.toFloat()
        val origH = drawable.intrinsicHeight.toFloat()
        val px = bx / origW
        val py = by / origH
        val finalX = m!![Matrix.MTRANS_X] + imageWidth * px
        val finalY = m!![Matrix.MTRANS_Y] + imageHeight * py
        return PointF(finalX, finalY)
    }

    private inner class Fling(velocityX: Int, velocityY: Int) : Runnable {
        var scroller: CompatScroller?
        var currX: Int
        var currY: Int

        init {
            setState(State.FLING)
            scroller = CompatScroller(context)
            matrix!!.getValues(m)

            val startX = m!![Matrix.MTRANS_X].toInt()
            val startY = m!![Matrix.MTRANS_Y].toInt()
            val minX: Int
            val maxX: Int
            val minY: Int
            val maxY: Int

            if (imageWidth > viewWidth) {
                minX = viewWidth - imageWidth.toInt()
                maxX = 0
            } else {
                maxX = startX
                minX = maxX
            }

            if (imageHeight > viewHeight) {
                minY = viewHeight - imageHeight.toInt()
                maxY = 0
            } else {
                maxY = startY
                minY = maxY
            }

            scroller!!.fling(
                startX, startY, velocityX, velocityY, minX,
                maxX, minY, maxY
            )
            currX = startX
            currY = startY
        }

        fun cancelFling() {
            if (scroller != null) {
                setState(State.NONE)
                scroller!!.forceFinished()
            }
        }

        override fun run() {
            if (touchImageViewListener != null) {
                touchImageViewListener!!.onMove()
            }

            if (scroller!!.isFinished) {
                scroller = null
                return
            }

            if (scroller!!.computeScrollOffset()) {
                val newX = scroller!!.currX
                val newY = scroller!!.currY
                val transX = newX - currX
                val transY = newY - currY
                currX = newX
                currY = newY
                matrix!!.postTranslate(transX.toFloat(), transY.toFloat())
                fixTrans()
                imageMatrix = matrix
                compatPostOnAnimation(this)
            }
        }
    }

    private class CompatScroller(context: Context?) {
        var scroller: Scroller? = null
        var overScroller: OverScroller = OverScroller(context)
        var isPreGingerbread: Boolean = false

        fun fling(
            startX: Int,
            startY: Int,
            velocityX: Int,
            velocityY: Int,
            minX: Int,
            maxX: Int,
            minY: Int,
            maxY: Int
        ) {
            if (isPreGingerbread) {
                scroller!!.fling(startX, startY, velocityX, velocityY, minX, maxX, minY, maxY)
            } else {
                overScroller.fling(startX, startY, velocityX, velocityY, minX, maxX, minY, maxY)
            }
        }

        fun forceFinished() {
            if (isPreGingerbread) {
                scroller!!.forceFinished(true)
            } else {
                overScroller.forceFinished(true)
            }
        }

        val isFinished: Boolean
            get() = if (isPreGingerbread) {
                scroller!!.isFinished
            } else {
                overScroller.isFinished
            }

        fun computeScrollOffset(): Boolean {
            if (isPreGingerbread) {
                return scroller!!.computeScrollOffset()
            } else {
                overScroller.computeScrollOffset()
                return overScroller.computeScrollOffset()
            }
        }

        val currX: Int
            get() {
                return if (isPreGingerbread) {
                    scroller!!.currX
                } else {
                    overScroller.currX
                }
            }

        val currY: Int
            get() {
                return if (isPreGingerbread) {
                    scroller!!.currY
                } else {
                    overScroller.currY
                }
            }
    }

    private fun compatPostOnAnimation(runnable: Runnable) {
        postOnAnimation(runnable)
    }

    private class ZoomVariables(
        var scale: Float,
        var focusX: Float,
        var focusY: Float,
        var scaleType: ScaleType?
    )

    private fun printMatrixInfo() {
        val n = FloatArray(9)
        matrix!!.getValues(n)
    }

    companion object {
        private const val SUPER_MIN_MULTIPLIER = .75f
        private const val SUPER_MAX_MULTIPLIER = 1.25f
        private const val SWIPE_THRESHOLD = 100
        private const val SWIPE_VELOCITY_THRESHOLD = 100
    }
}