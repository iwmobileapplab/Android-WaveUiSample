package com.android.example.waveuisample

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import com.android.example.waveuisample.databinding.ActivityMainBinding
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity(), AppBarLayout.OnOffsetChangedListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        findViewById<View>(android.R.id.content).systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE

        binding.appBar.addOnOffsetChangedListener(this)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.notification -> {
                    return@setOnMenuItemClickListener true
                }
            }
            false
        }

        binding.wave.viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.wave.viewTreeObserver.removeOnGlobalLayoutListener(this)

                val lp = binding.wave.layoutParams as? CoordinatorLayout.LayoutParams
                lp?.also {
                    lp.height = lp.height + getStatusBarHeight()
                    binding.wave.layoutParams = lp
                }

                binding.scrollable.setPadding(
                        binding.scrollable.paddingLeft,
                        getStatusBarHeight(),
                        binding.scrollable.paddingRight,
                        binding.scrollable.paddingBottom
                )

            }
        })

        val chart: LineChart2 = findViewById(R.id.chart)
        chart.showData(StockData.google)
        binding.header.tabLayout.apply {
            val m1 = newTab().setText("1M").setId(R.id.m1)
            val m6 = newTab().setText("6M").setId(R.id.m6)
            val y1 = newTab().setText("1Y").setId(R.id.y1)
            val y5 = newTab().setText("5Y").setId(R.id.y5)
            val all = newTab().setText("ALL").setId(R.id.all)
            addTab(m1)
            addTab(m6)
            addTab(y1)
            addTab(y5)
            addTab(all)
            selectTab(all)
        }
        binding.header.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.id) {
                    R.id.all -> {
                        chart.showData(StockData.google)
                    }
                    R.id.y5 -> {
                        chart.showData(StockData.google.takeLast(365*5))
                    }
                    R.id.y1 -> {
                        chart.showData(StockData.google.takeLast(365))
                    }
                    R.id.m6 -> {
                        chart.showData(StockData.google.takeLast(180))
                    }
                    R.id.m1 -> {
                        chart.showData(StockData.google.takeLast(30))
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }

    var ts: Int? = null
    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        ts ?: run {
            ts = appBarLayout?.totalScrollRange
        }

        ts?.also {
            val appBarHeight = binding.appBar.height
            val barVisibleHeight = appBarHeight + verticalOffset
            binding.wave.y = -(appBarHeight - barVisibleHeight).toFloat()
        }
    }

    private fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

}