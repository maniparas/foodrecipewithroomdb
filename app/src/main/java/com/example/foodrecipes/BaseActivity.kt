package com.example.foodrecipes

import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

abstract class BaseActivity : AppCompatActivity() {

    open lateinit var mProgressBar: ProgressBar

    override fun setContentView(layoutResID: Int) {
        val constraintLayout: ConstraintLayout =
            layoutInflater.inflate(R.layout.activity_base, null) as ConstraintLayout

        val frameLayout: FrameLayout = constraintLayout.findViewById(R.id.activity_content)
        mProgressBar = constraintLayout.findViewById(R.id.progressBar)

        layoutInflater.inflate(layoutResID, frameLayout, true)
        super.setContentView(constraintLayout)
    }

    fun showProgressbar(visibility: Boolean) {
        mProgressBar.visibility = if (visibility) View.VISIBLE else View.GONE
    }
}