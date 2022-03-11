package com.twoplaytech.drbetting.admin.ui.ticket

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.twoplaytech.drbetting.R
import com.twoplaytech.drbetting.admin.ui.common.BaseActivity
import com.twoplaytech.drbetting.admin.ui.ticket.navigation.TicketNavigation
import com.twoplaytech.drbetting.admin.ui.ticket.ui.theme.DrBettingTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TicketActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.seagreen)
        setContent {
            DrBettingTheme {
                // A surface container using the 'background' color from the theme
                TicketNavigation(this)
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DrBettingTheme {
        TicketNavigation()
    }
}