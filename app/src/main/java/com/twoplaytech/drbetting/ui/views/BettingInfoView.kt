package com.twoplaytech.drbetting.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.twoplaytech.drbetting.R
import com.twoplaytech.drbetting.data.models.TypeStatus
import com.twoplaytech.drbetting.databinding.ViewBettingInfoBinding
import com.twoplaytech.drbetting.ui.common.CustomViewArgumentsExtractor
import com.twoplaytech.drbetting.ui.common.ICustomView
import com.twoplaytech.drbetting.util.getStatusResource

/*
    Author: Damjan Miloshevski 
    Created on 7.10.21 14:20
    Project: Dr.Betting
    © 2Play Tech  2021. All rights reserved
*/
class BettingInfoView(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs),ICustomView,CustomViewArgumentsExtractor {
    private lateinit var binding:ViewBettingInfoBinding
    private var titleRes:Int = 0
    private var type: Int = 0
    init {
        initBinding(LayoutInflater.from(context))
        extractAttributes(attrs)
        binding.title.text = context.getString(titleRes)
    }

    fun setData(text:String? = null, status:TypeStatus? = null){
        when(type){
            0 -> {
                binding.icon.visibility = View.GONE
                binding.info.visibility = View.VISIBLE
                binding.info.text = text
            }
            1 -> {
                binding.icon.visibility = View.VISIBLE
                binding.info.visibility = View.GONE
                status?.let {
                    binding.icon.setImageResource(it.getStatusResource())
                }?: binding.icon.setImageResource(R.drawable.tip_unknown)

            }
        }
        requestLayout()
        invalidate()
    }

    override fun extractAttributes(attrs: AttributeSet) {
        attrs?.let {
            val styledAttributes =
                context.obtainStyledAttributes(it, R.styleable.BettingInfoView)
            titleRes = styledAttributes.getResourceId(R.styleable.BettingInfoView_infoTitle,0)
            type = styledAttributes.getInteger(R.styleable.BettingInfoView_infoType, 0)
            styledAttributes.recycle()
        }
    }


    override fun initBinding(inflater: LayoutInflater) {
        binding = ViewBettingInfoBinding.inflate(inflater,this,true)
    }

}