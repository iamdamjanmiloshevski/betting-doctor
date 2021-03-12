package com.twoplaytech.drbetting.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.twoplaytech.drbetting.databinding.ViewNoDataBinding

/*
    Author: Damjan Miloshevski 
    Created on 3/12/21 10:54 AM
    Project: Dr.Betting
    © 2Play Tech  2021. All rights reserved
*/
class NoDataView(context: Context, attributeSet: AttributeSet) :
    ConstraintLayout(context, attributeSet) {
    private val binding: ViewNoDataBinding
    private val layoutInflater = LayoutInflater.from(context)

    init {
        binding = ViewNoDataBinding.inflate(layoutInflater, this, true)
    }

    fun setVisible(isVisible: Boolean) {
        binding.lytMain.visibility = if (isVisible) View.VISIBLE else View.GONE
    }
}