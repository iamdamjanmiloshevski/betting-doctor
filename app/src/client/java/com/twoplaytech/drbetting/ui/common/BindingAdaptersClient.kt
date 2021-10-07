package com.twoplaytech.drbetting.ui.common

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.twoplaytech.drbetting.R
import com.twoplaytech.drbetting.ui.settings.ISettingsItem

/*
    Author: Damjan Miloshevski 
    Created on 7.10.21 17:19
    Project: Dr.Betting
    © 2Play Tech  2021. All rights reserved
*/
@BindingAdapter("itemIcon")
fun loadItemIcon(view: ImageView, item: ISettingsItem) {
    Glide.with(view)
        .load(item.icon)
        .fitCenter()
        .placeholder(R.drawable.progress_animation)
        .into(view)
}