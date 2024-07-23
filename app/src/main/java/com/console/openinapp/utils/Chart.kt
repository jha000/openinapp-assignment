package com.console.openinapp.utils

import android.content.Context
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.console.openinapp.R

object ChartUtils {

    fun setupChart(context: Context, lineChart: LineChart) {
        lineChart.apply {
            description.isEnabled = false
            axisRight.isEnabled = false
            legend.isEnabled = false

            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                setDrawGridLines(true)
                setDrawLabels(true)
                granularity = 1f
                isGranularityEnabled = true
                textSize = 12f
                gridColor = ContextCompat.getColor(context, R.color.grey1)
                gridLineWidth = 0.5f
                axisLineColor = ContextCompat.getColor(context, R.color.grey1)
                axisLineWidth = 0.5f
            }

            axisLeft.apply {
                setDrawGridLines(true)
                setDrawLabels(true)
                axisMinimum = 0f
                granularity = 1f
                labelCount = 6
                gridColor = ContextCompat.getColor(context, R.color.grey1)
                gridLineWidth = 0.5f
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return if (value == 0f) "" else value.toInt().toString()
                    }
                }
            }
        }
    }

    fun updateChart(context: Context, lineChart: LineChart, chartData: Map<String, Int>) {
        val filteredEntries = chartData.entries
            .filter { it.value > 0 }
            .sortedBy { it.key }
            .mapIndexed { index, entry ->
                Entry(index.toFloat(), entry.value.toFloat())
            }

        val xAxisLabels = chartData.filter { it.value > 0 }.keys.toList()

        val dataSet = LineDataSet(filteredEntries, "").apply {
            color = ContextCompat.getColor(context, R.color.blue)
            valueTextColor = ContextCompat.getColor(context, R.color.black)
            setDrawFilled(true)
            fillDrawable = ContextCompat.getDrawable(context, R.drawable.gradient_fill)
            fillAlpha = 60
            lineWidth = 2f
        }

        val lineData = LineData(dataSet)
        lineChart.data = lineData

        lineChart.xAxis.apply {
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return if (value.toInt() in xAxisLabels.indices) {
                        xAxisLabels[value.toInt()]
                    } else {
                        ""
                    }
                }
            }
        }

        lineChart.invalidate()
    }
}
