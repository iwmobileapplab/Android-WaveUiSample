package com.android.example.waveuisample

import android.graphics.Canvas
import android.graphics.Shader
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.renderer.LineChartRenderer
import com.github.mikephil.charting.utils.ViewPortHandler

class LineChartRenderer2 constructor(
    val chart: LineDataProvider, animator: ChartAnimator,
    viewPortHandler: ViewPortHandler
) : LineChartRenderer(
    chart,
    animator,
    viewPortHandler
) {

    override fun drawData(c: Canvas?) {
        super.drawData(c)
    }

    override fun drawDataSet(c: Canvas?, dataSet: ILineDataSet?) {

        if (dataSet is LineDataSet2) {
            mRenderPaint.shader = dataSet.generateLinePaintShaderDelegate?.invoke(this)
        }

        super.drawDataSet(c, dataSet)

        mRenderPaint.shader = null
    }

}

class LineDataSet2 constructor(
    yVals: List<Entry>,
    label: String
) : LineDataSet(yVals, label) {

    var generateLinePaintShaderDelegate: (LineChartRenderer2.() -> Shader)? = null
}