package com.twoplaytech.drbetting.admin.ui.ticket.screens

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.navigation.NavController
import com.twoplaytech.drbetting.R
import com.twoplaytech.drbetting.admin.ui.common.BettingTipUiState
import com.twoplaytech.drbetting.admin.ui.common.FieldValidator
import com.twoplaytech.drbetting.admin.ui.ticket.components.BettingTipForm
import com.twoplaytech.drbetting.admin.ui.ticket.components.MenuAction
import com.twoplaytech.drbetting.admin.ui.ticket.components.TicketsAppBar
import com.twoplaytech.drbetting.admin.ui.viewmodels.TicketsViewModel
import com.twoplaytech.drbetting.data.models.BettingTip
import com.twoplaytech.drbetting.data.models.Sport.Companion.toSport
import com.twoplaytech.drbetting.data.models.Team
import com.twoplaytech.drbetting.ui.common.CenteredItem
import com.twoplaytech.drbetting.util.getSportPlaceHolder
import com.twoplaytech.drbetting.util.getStatusResource
import com.twoplaytech.drbetting.util.toStatus

/*
    Author: Damjan Miloshevski 
    Created on 15.2.22 15:06
    Project: Dr.Betting
    © 2Play Technologies  2022. All rights reserved
*/
@Composable
fun AddBettingTip(
    ticketId: String? = null,
    tipId: String?,
    activity: AppCompatActivity? = null,
    navController: NavController = NavController(LocalContext.current),
    viewModel: TicketsViewModel
) {
    tipId?.let { viewModel.getBettingTipById(it) }
    val leagueName = rememberSaveable() {
        mutableStateOf("")
    }
    val homeTeam = rememberSaveable() {
        mutableStateOf("")
    }
    val awayTeam = rememberSaveable() {
        mutableStateOf("")
    }
    val bettingTip = rememberSaveable() {
        mutableStateOf("")
    }
    val result = rememberSaveable() {
        mutableStateOf("")
    }
    val gameTime = rememberSaveable() {
        mutableStateOf("")
    }
    val sport = rememberSaveable() {
        mutableStateOf("Football")
    }
    val status = rememberSaveable() {
        mutableStateOf("Unknown")
    }
    val coefficient = rememberSaveable() {
        mutableStateOf("")
    }
    val isVisible = remember {
        mutableStateOf(true)
    }
    val leagueNameValidator = FieldValidator(stringResource(id = R.string.error_league_name))
    val homeTeamValidator = FieldValidator(stringResource(id = R.string.error_home_team))
    val awayTeamValidator = FieldValidator(stringResource(id = R.string.error_away_team))
    val bettingTipValidator = FieldValidator(stringResource(id = R.string.error_betting_tip))
    val oddsValidator = FieldValidator(stringResource(id = R.string.error_odds))
    val gameTimeValidator = FieldValidator(stringResource(id = R.string.error_game_time))

    Scaffold(topBar = {
        TicketsAppBar(title = "Add new Betting tip", navController = navController, actions = {
            MenuAction(
                icon = Icons.Default.Check,
                contentDescription = "Save icon",
                isVisible
            ) {
                when {
                    leagueName.value.isEmpty() -> {
                        leagueNameValidator.hasError.value = true
                    }
                    homeTeam.value.isEmpty() -> {
                        homeTeamValidator.hasError.value = true
                    }
                    awayTeam.value.isEmpty() -> {
                        awayTeamValidator.hasError.value = true
                    }
                    bettingTip.value.isEmpty() -> {
                        bettingTipValidator.hasError.value = true
                    }
                    coefficient.value.isEmpty() -> {
                        oddsValidator.hasError.value = true
                    }
                    gameTime.value.isEmpty() -> {
                        gameTimeValidator.hasError.value = true
                    }
                    else -> {
                        val bTip = BettingTip(
                            leagueName.value,
                            Team(homeTeam.value),
                            Team(awayTeam.value),
                            gameTime.value,
                            bettingTip.value,
                            status.value.uppercase().toStatus(),
                            result.value,
                            _id = tipId,
                            sport = sport.value.toSport(),
                            ticketId = ticketId,
                            coefficient = coefficient.value
                        )
                        tipId?.let { viewModel.updateBettingTip(bTip) }
                            ?: viewModel.bettingTips.add(bTip)
                        navController.navigateUp()
                    }
                }
            }
        })
    }) {
        val chosenSportIcon = rememberSaveable {
            mutableStateOf(R.drawable.soccer_ball)
        }
        val chosenStatusIcon = rememberSaveable {
            mutableStateOf(R.drawable.tip_unknown)
        }
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White
        ) {
            val isOpenSpinnerSport = remember { mutableStateOf(false) } // initial value
            val onOpenCloseSportSpinner: (Boolean) -> Unit = {
                isOpenSpinnerSport.value = it
            }
            val isOpenSpinnerStatus = remember { mutableStateOf(false) } // initial value
            val onOpenCloseStatusSpinner: (Boolean) -> Unit = {
                isOpenSpinnerStatus.value = it
            }
            if (tipId != null && tipId != "null") {
                when (val state = viewModel.bettingTipUiState.collectAsState().value) {
                    is BettingTipUiState.Error -> {
                        CenteredItem {
                            Text(text = state.exception.localizedMessage ?: "Something went wrong")
                        }
                    }
                    BettingTipUiState.Loading -> {
                        CenteredItem {
                            CircularProgressIndicator()
                        }
                    }
                    is BettingTipUiState.Success -> {
                        val bTip = state.bettingTip
                        with(bTip) {
                            leagueName.value = this.leagueName
                            homeTeam.value = this.teamHome?.name ?: ""
                            awayTeam.value = this.teamAway?.name ?: ""
                            bettingTip.value = this.bettingType
                            result.value = this.result
                            coefficient.value = this.coefficient ?: ""
                            gameTime.value = this.gameTime
                            status.value = this.status.name.lowercase().capitalize(Locale.current)
                            sport.value = this.sport.name
                            chosenSportIcon.value = this.sport.getSportPlaceHolder()
                            chosenStatusIcon.value = this.status.getStatusResource()
                        }
                    }
                    else -> {}
                }
            }
            BettingTipForm(
                leagueName,
                homeTeam,
                awayTeam,
                bettingTip,
                result,
                gameTime,
                activity,
                isOpenSpinnerSport,
                sport,
                chosenSportIcon,
                onOpenCloseSportSpinner,
                isOpenSpinnerStatus,
                status,
                leagueNameValidator,
                homeTeamValidator,
                awayTeamValidator,
                bettingTipValidator,
                oddsValidator,
                gameTimeValidator,
                coefficient,
                chosenStatusIcon,
                onOpenCloseStatusSpinner
            )
        }
    }
}