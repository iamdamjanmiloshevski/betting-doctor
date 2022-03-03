package com.twoplaytech.drbetting.admin.ui.ticket.components

import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.twoplaytech.drbetting.R
import com.twoplaytech.drbetting.admin.ui.ticket.widgets.DropDownList
import com.twoplaytech.drbetting.admin.ui.ticket.widgets.DropdownType
import com.twoplaytech.drbetting.admin.ui.ticket.widgets.showDateTimePicker
import com.twoplaytech.drbetting.data.models.Ticket
import com.twoplaytech.drbetting.util.toStringDate

/*
    Author: Damjan Miloshevski 
    Created on 16.2.22 14:00
    Project: Dr.Betting
    © 2Play Technologies  2022. All rights reserved
*/
@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun TicketCard(
    modifier: Modifier = Modifier,
    ticket: Ticket? = null,
    onLongPress: (id: String) -> Unit = {},
    onClick: (Ticket) -> Unit = {}
) {
    ticket?.let { ticket1 ->
        Card(
            modifier = modifier
                .padding(bottom = 10.dp)
                .combinedClickable(
                    onLongClick = { ticket1.id?.let { onLongPress.invoke(it) } },
                    onClick = { onClick.invoke(ticket1) }),
            shape = RoundedCornerShape(20.dp),
            backgroundColor = Color.White
        ) {
            Column(modifier = modifier.fillMaxWidth()) {
                with(ticket1) {
                    Text(
                        text = "Ticket ${this.id}",
                        modifier = modifier.padding(5.dp),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Date: ${this.date}", modifier = modifier.padding(5.dp),
                        fontSize = 16.sp,
                        fontStyle = FontStyle.Italic
                    )
                    Text(
                        text = "Tips available: ${this.tips.size}",
                        modifier = modifier.padding(5.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun TicketsAppBar(
    modifier: Modifier = Modifier,
    title: String,
    hasBackNavigation: Boolean = true,
    navController: NavController? = null,
    activity: ComponentActivity? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        title = {
            Text(text = title, overflow = TextOverflow.Clip, softWrap = true)
        },
        modifier = modifier,
        navigationIcon = {
            if (hasBackNavigation) {
                IconButton(onClick = {
                    if (activity != null) activity.onBackPressed()
                    else navController?.navigateUp()
                }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Arrow back")
                }
            } else Box() {}
        }, actions = actions,
        backgroundColor = colorResource(id = R.color.seagreen)
    )
}

@Composable
fun TicketFloatingActionButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.Bottom
    ) {
        FloatingActionButton(
            onClick = onClick,
            backgroundColor = colorResource(id = R.color.seagreen)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                modifier = Modifier.size(25.dp)
            )
        }
    }
}

@Composable
fun MenuAction(
    icon: ImageVector,
    contentDescription: String?,
    isVisible: MutableState<Boolean>,
    onActionClick: () -> Unit
) {
    if (isVisible.value) {
        IconButton(onClick = onActionClick) {
            Icon(imageVector = icon, contentDescription = contentDescription)
        }
    } else Box() {}
}

@Composable
fun InputField(
    modifier: Modifier = Modifier,
    valueState: MutableState<String>,
    labelId: String,
    enabled: Boolean = true,
    isSingleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default,
    onClick: () -> Unit = {}
) {
    OutlinedTextField(
        value = valueState.value,
        onValueChange = { valueState.value = it },
        singleLine = isSingleLine,
        textStyle = TextStyle(fontSize = 18.sp, color = MaterialTheme.colors.onBackground),
        modifier = modifier
            .fillMaxWidth()
            .clickable { if (!enabled) onClick.invoke() },
        enabled = enabled,
        label = { Text(text = labelId) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardActions = onAction
    )
}

@Composable
fun TextInput(
    modifier: Modifier = Modifier,
    title: String,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(text = title)
        content()
    }
}

@Composable
fun BettingTipForm(
    leagueName: MutableState<String>,
    homeTeam: MutableState<String>,
    awayTeam: MutableState<String>,
    bettingTip: MutableState<String>,
    result: MutableState<String>,
    gameTime: MutableState<String>,
    activity: AppCompatActivity?,
    isOpenSpinnerSport: MutableState<Boolean>,
    sport: MutableState<String>,
    chosenSportIcon: MutableState<Int>,
    onOpenCloseSportSpinner: (Boolean) -> Unit,
    isOpenSpinnerStatus: MutableState<Boolean>,
    status: MutableState<String>,
    coefficient: MutableState<String>,
    chosenStatusIcon: MutableState<Int>,
    onOpenCloseStatusSpinner: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(10.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.Start
    ) {
        TextInput(title = stringResource(id = R.string.hint_league_title)) {
            InputField(valueState = leagueName, labelId = stringResource(id = R.string.hint_league))
        }
        TextInput(title = stringResource(id = R.string.team_home)) {
            InputField(
                valueState = homeTeam,
                labelId = stringResource(id = R.string.team_home_hint)
            )
        }
        TextInput(title = stringResource(id = R.string.team_away)) {
            InputField(
                valueState = awayTeam,
                labelId = stringResource(id = R.string.team_away_hint)
            )
        }
        TextInput(title = stringResource(id = R.string.hint_betting_tip_heading)) {
            InputField(
                valueState = bettingTip,
                labelId = stringResource(id = R.string.hint_betting_tip)
            )
        }
        TextInput(title = stringResource(R.string.result)) {
            InputField(valueState = result, labelId = stringResource(R.string.result_hint))
        }
        TextInput(title = stringResource(R.string.coefficient)) {
            InputField(
                valueState = coefficient,
                labelId = stringResource(R.string.coefficient_hint),
                keyboardType = KeyboardType.Number
            )
        }
        TextInput(title = stringResource(id = R.string.hint_game_time_heading)) {
            InputField(
                valueState = gameTime,
                labelId = stringResource(id = R.string.hint_game_time),
                enabled = false,
                imeAction = ImeAction.Done
            ) {
                activity?.let {
                    showDateTimePicker(activity) { calendar ->
                        calendar?.let {
                            gameTime.value = it.time.toStringDate()
                        }
                    }
                }
            }
            val sports = stringArrayResource(id = R.array.sports)
            DropDownSpinner(
                stringResource(id = R.string.sport),
                sports.toList(),
                DropdownType.SPORT,
                isOpenSpinnerSport,
                sport,
                chosenSportIcon,
                onOpenCloseSportSpinner
            )
            val statuses = stringArrayResource(id = R.array.statuses)
            DropDownSpinner(
                stringResource(id = R.string.status),
                statuses.toList(),
                DropdownType.STATUS,
                isOpenSpinnerStatus,
                status,
                chosenStatusIcon,
                onOpenCloseStatusSpinner
            )
        }

    }
}

@Composable
fun DropDownSpinner(
    title: String,
    items: List<String>,
    type: DropdownType,
    isOpen: MutableState<Boolean>,
    chosenValue: MutableState<String>,
    chosenValueIcon: MutableState<Int>,
    openCloseOfDropDownList: (Boolean) -> Unit
) {
    Surface(modifier = Modifier
        .padding(
            top = 10.dp,
            bottom = 10.dp
        )
        .clickable { isOpen.value = true }) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(text = title, modifier = Modifier.padding(bottom = 5.dp))
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(5.dp),
                border = BorderStroke(1.5.dp, Color.LightGray)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = chosenValueIcon.value),
                        contentDescription = "Spinner icon",
                        modifier = Modifier.size(30.dp)
                    )
                    Spacer(modifier = Modifier.size(5.dp))
                    Text(
                        text = chosenValue.value,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Start
                    )
                    Spacer(modifier = Modifier.width(190.dp))
                    Icon(
                        imageVector = if (isOpen.value) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Arrow dropdown"
                    )
                }
            }
            DropDownList(
                requestToOpen = isOpen.value, list = items, type, openCloseOfDropDownList
            ) { chosenItem, icon ->
                chosenValue.value = chosenItem
                chosenValueIcon.value = icon
            }
        }
    }
}