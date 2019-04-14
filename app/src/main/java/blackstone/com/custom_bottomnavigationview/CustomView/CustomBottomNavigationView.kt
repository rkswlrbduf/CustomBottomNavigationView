package blackstone.com.custom_bottomnavigationview.CustomView

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.*
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.FloatingActionButton
import android.util.AttributeSet
import android.view.Gravity
import android.widget.FrameLayout
import blackstone.com.custom_bottomnavigationview.R


class CustomBottomNavigationView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    BottomNavigationView(context, attrs, defStyleAttr) {

    private var mPath = Path()
    private var mPaint = Paint()
    private var navWidth: Int = 0
    private var navHeight: Int = 0
    private var firstCurveStartPoint: Point = Point()
    private var firstCurveEndPoint: Point = Point()
    private var secondCurveStartPoint: Point = Point()
    private var secondCurveEndPoint: Point = Point()

    private var firstCurveControlPoint1: Point = Point()
    private var firstCurveControlPoint2: Point = Point()
    private var secondCurveControlPoint1: Point = Point()
    private var secondCurveControlPoint2: Point = Point()

    private lateinit var typedArray: TypedArray

    private var circleSize: Int = 100
    private var circleColor: Int = -1
    private var bgColor: Int = -1

    var fab: FloatingActionButton

    init {
        if (attrs != null) {
            typedArray = context.obtainStyledAttributes(attrs, blackstone.com.custom_bottomnavigationview.R.styleable.CustomBottomNavigationView)
            circleSize =
                typedArray.getDimensionPixelSize(blackstone.com.custom_bottomnavigationview.R.styleable.CustomBottomNavigationView_mFabSize, circleSize)
            circleColor =
                    typedArray.getColor(blackstone.com.custom_bottomnavigationview.R.styleable.CustomBottomNavigationView_mFabColor, Color.BLACK)
            bgColor =
                    typedArray.getColor(blackstone.com.custom_bottomnavigationview.R.styleable.CustomBottomNavigationView_mBgColor, Color.BLACK)
        }

        mPaint.style = Paint.Style.FILL_AND_STROKE
        mPaint.color = bgColor
        setBackgroundColor(Color.TRANSPARENT)

        fab = FloatingActionButton(context)
        fab.setImageDrawable(resources.getDrawable(R.drawable.share))
        val fabLp = FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        fabLp.gravity = Gravity.CENTER_HORIZONTAL
        fab.customSize = circleSize
        fab.backgroundTintList = ColorStateList.valueOf(circleColor)
        fab.isClickable = true
        addView(fab, fabLp)

        addPadding()

        typedArray.recycle()

    }

    fun addPadding() {
        for (i in 0 until childCount) {
            val item = getChildAt(i)
            if(i != 0) continue
            val lp = item.layoutParams as LayoutParams
            lp.topMargin = circleSize / 15 * 3
        }
    }



    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        navWidth = width
        navHeight = height

        firstCurveStartPoint.set((navWidth / 2) - circleSize - (circleSize / 2), circleSize / 2)

        firstCurveEndPoint.set(navWidth / 2, circleSize + circleSize / 4)
        secondCurveStartPoint = firstCurveEndPoint

        secondCurveEndPoint.set((navWidth / 2) + circleSize + (circleSize / 2), circleSize / 2)

        firstCurveControlPoint1.set(
            firstCurveStartPoint.x + circleSize - (circleSize / 4),
            firstCurveStartPoint.y
        )

        firstCurveControlPoint2.set(
            firstCurveEndPoint.x - circleSize + (circleSize / 4),
            firstCurveEndPoint.y
        )

        secondCurveControlPoint1.set(
            secondCurveStartPoint.x + circleSize - (circleSize / 4) ,
            secondCurveStartPoint.y
        )
        secondCurveControlPoint2.set(
            secondCurveEndPoint.x - circleSize + (circleSize / 4),
            secondCurveEndPoint.y
        )

        mPath.reset()
        mPath.moveTo(0f, (circleSize / 2).toFloat())
        mPath.lineTo(firstCurveStartPoint.x.toFloat(), firstCurveStartPoint.y.toFloat())

        mPath.cubicTo(
            firstCurveControlPoint1.x.toFloat(), firstCurveControlPoint1.y.toFloat(),
            firstCurveControlPoint2.x.toFloat(), firstCurveControlPoint2.y.toFloat(),
            firstCurveEndPoint.x.toFloat(), firstCurveEndPoint.y.toFloat()
        )

        mPath.cubicTo(
            secondCurveControlPoint1.x.toFloat(), secondCurveControlPoint1.y.toFloat(),
            secondCurveControlPoint2.x.toFloat(), secondCurveControlPoint2.y.toFloat(),
            secondCurveEndPoint.x.toFloat(), secondCurveEndPoint.y.toFloat()
        )

        mPath.lineTo(navWidth.toFloat(), (circleSize / 2).toFloat())
        mPath.lineTo(navWidth.toFloat(), navHeight.toFloat())
        mPath.lineTo(0f, navHeight.toFloat())
        mPath.close()

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawPath(mPath, mPaint)
    }

}