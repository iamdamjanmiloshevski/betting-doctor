package com.twoplaytech.drbetting.admin.ui.ticket.components

import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.twoplaytech.drbetting.R
import com.twoplaytech.drbetting.data.models.Ticket

/*
    Author: Damjan Miloshevski 
    Created on 16.2.22 14:00
    Project: Dr.Betting
    © 2Play Technologies  2022. All rights reserved
*/
@Preview
@Composable
fun TicketCard(
    modifier: Modifier = Modifier,
    ticket: Ticket? = null,
    onClick: (String) -> Unit = {}
) {
    ticket?.let {
        Card(
            modifier = modifier
                .padding(bottom = 10.dp)
                .clickable { onClick.invoke(ticket.id) },
            shape = RoundedCornerShape(20.dp),
            backgroundColor = Color.White
        ) {
            Column(modifier = modifier.fillMaxWidth()) {
                with(it) {
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
    hasBackNavigation:Boolean = true,
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
          if(hasBackNavigation){
              IconButton(onClick = {
                  if(activity != null) activity.onBackPressed()
                  else navController?.navigateUp()
              }) {
                  Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Arrow back")
              }
          }else Box(){}
        },actions = actions,
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
        FloatingActionButton(onClick = onClick, backgroundColor = colorResource(id = R.color.seagreen)) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                modifier = Modifier.size(25.dp)
            )
        }
    }
}

@Composable
fun MenuAction(icon:ImageVector, contentDescription: String?, onActionClick:()->Unit){
    IconButton(onClick = onActionClick) {
        Icon(imageVector = icon, contentDescription = contentDescription)
    }
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
    OutlinedTextField(value = valueState.value,
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
){
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