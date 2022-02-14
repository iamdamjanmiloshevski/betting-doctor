package com.twoplaytech.drbetting.sportsanalyst.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.twoplaytech.drbetting.sportsanalyst.ui.navigation.SportsAnalystNavigation
import com.twoplaytech.drbetting.sportsanalyst.ui.theme.DrBettingTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DrBettingTheme {
                // A surface container using the 'background' color from the theme
                SportsAnalystNavigation()
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DrBettingTheme {
        SportsAnalystNavigation()
    }
}