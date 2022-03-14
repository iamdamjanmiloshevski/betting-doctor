package com.twoplaytech.drbetting.sportsanalyst.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.twoplaytech.drbetting.sportsanalyst.notifications.NotificationsManager
import com.twoplaytech.drbetting.sportsanalyst.ui.navigation.SportsAnalystNavigation
import com.twoplaytech.drbetting.sportsanalyst.ui.screens.settings.SettingsViewModel
import com.twoplaytech.drbetting.sportsanalyst.ui.theme.DrBettingTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel:SettingsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fetchPushNotificationsStatus()
        setContent {
            DrBettingTheme {
                // A surface container using the 'background' color from the theme
                SportsAnalystNavigation()
            }
        }
    }

    private fun fetchPushNotificationsStatus() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.pushNotificationsState.collect {
                    NotificationsManager.toggleNotifications(it)
                }
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