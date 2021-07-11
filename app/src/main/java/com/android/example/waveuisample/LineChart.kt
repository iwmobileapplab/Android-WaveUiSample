package com.android.example.waveuisample

import android.content.Context
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

class LineChart2 @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LineChart(context, attrs, defStyleAttr) {

    override fun init() {
        super.init()

        renderer = LineChartRenderer2(this, animator, viewPortHandler)
    }

    fun showData(_data: List<Float>) {
        val entries = mutableListOf<Entry>()
        _data.forEachIndexed { index, data ->
            entries.add(Entry(index.toFloat(), data))
        }

        val lineDataSet = LineDataSet2(entries, "")
        lineDataSet.apply {
            disableDashedLine()
            disableScroll()
            setDrawValues(false)
            setDrawCircleHole(false)
            setDrawCircles(false)
            setDrawFilled(true)
            animateX(700)
            fillDrawable =
                ContextCompat.getDrawable(context, R.drawable.line_chart_fill_gradient_orenge)
            color = Color.parseColor("#ffee00")

            generateLinePaintShaderDelegate = {
                // gradient color shader from left(RED) to right(BLUE)
                LinearGradient(
                    leftPaddingOffset.toFloat() + extraLeftOffset,
                    0f,
                    chart.width.toFloat(),
                    0f,
                    intArrayOf(
                        Color.RED,
                        Color.RED
                    ),
                    null,
                    Shader.TileMode.CLAMP
                )
            }
        }

        val dataSets = mutableListOf<ILineDataSet>()
        dataSets.add(lineDataSet)

        val d = LineData(dataSets)

        this.setDrawGridBackground(false)
        this.setDrawMarkers(false)

        xAxis.apply {
            setCenterAxisLabels(false)
            setDrawGridLines(false)
            setDrawLabels(false)
            setDrawAxisLine(false)
            setDrawBorders(false)
            setDrawMarkers(false)
            setTouchEnabled(false)
            setScaleEnabled(false)
        }
        axisLeft.apply {
            axisMinimum = 0f
            isDragEnabled = false
            setDrawGridLines(false)
            setDrawZeroLine(false)
            setDrawAxisLine(false)
            textColor = Color.parseColor("#88ffffff")
            valueFormatter = object : DefaultAxisValueFormatter(mDecimals) {
                override fun getFormattedValue(value: Float): String {
                    return "\$ ${super.getFormattedValue(value)}"
                }
            }

            isEnabled = false
        }
        axisRight.apply {
            isEnabled = false
        }

        description = null
        legend.isEnabled = false

        data = d
        invalidate()
    }

}