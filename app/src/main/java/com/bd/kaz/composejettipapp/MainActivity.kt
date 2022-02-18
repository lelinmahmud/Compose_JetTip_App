package com.bd.kaz.composejettipapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bd.kaz.composejettipapp.components.InputField
import com.bd.kaz.composejettipapp.ui.theme.ComposeJetTipAppTheme
import com.bd.kaz.composejettipapp.util.calculateTotalPerPerson
import com.bd.kaz.composejettipapp.util.calculateTotalTip
import com.bd.kaz.composejettipapp.widgets.RoundedIconButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeJetTipAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colors.background
                ) {
                   Column {
                       MainContent()
                   }
                }
            }
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
fun MainContent(){
    val splitCounterState = remember {
        mutableStateOf(1)
    }

    val tipAmountState = remember {
        mutableStateOf(0.0)
    }

    val totalPerPersonState = remember {
        mutableStateOf(0.0)
    }
    BillForm(
        splitCounterState = splitCounterState,
        totalPerPersonState = totalPerPersonState,
        tipAmountState = tipAmountState
    ){billAmount ->
        Log.e("TAG", "MainContent: $billAmount" )
    }
}

@Preview(showBackground = false)
@Composable
fun TopHeader(totalPerPerson :Double =0.0){
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(20.dp)
            .clip(shape = RoundedCornerShape(corner = CornerSize(12.dp))),
        color = Color(0xFFE9D7F7)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val total ="%.2f".format(totalPerPerson)
            Text(text = "Total Per Person", style = MaterialTheme.typography.h4)
            Text(text = "$$total", style = MaterialTheme.typography.h4, fontWeight = FontWeight.ExtraBold)
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BillForm(
    modifier: Modifier = Modifier,
    range: IntRange = 1..10,
    splitCounterState: MutableState<Int>,
    tipAmountState: MutableState<Double>,
    totalPerPersonState: MutableState<Double>,
    onValChange :(String) -> Unit ={}){
    val totalBillState = remember {
        mutableStateOf("")
    }

    val validState = remember(totalBillState.value) {
        totalBillState.value.trim().isNotEmpty()
    }

    val sliderPositionState = remember {
        mutableStateOf(0f)
    }

    val tipPercentage = (sliderPositionState.value * 100).toInt()


    val keyboardController = LocalSoftwareKeyboardController.current
    TopHeader(totalPerPersonState.value)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(5.dp),
        shape = RoundedCornerShape(corner = CornerSize(8.dp)),
        border = BorderStroke(width = 1.dp, color = Color.LightGray),
        elevation = 2.dp
    ) {
        Column(modifier = modifier.padding(6.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start) {
            InputField(
                valueState = totalBillState,
                labelId = "Enter Bill",
                enabled =true ,
                isSingleLine = true,
                onAction = KeyboardActions{
                    if (!validState) return@KeyboardActions
                    onValChange(totalBillState.value.trim())
                    totalPerPersonState.value = calculateTotalPerPerson(totalBillState.value.toDouble(),splitCounterState.value,tipPercentage)
                    keyboardController?.hide()
                }
            )
            if (validState){
                Row(
                    modifier = modifier.padding(3.dp),
                    horizontalArrangement = Arrangement.Start,
                ) {
                    Text(
                        text = "Split",
                        modifier = modifier.align(
                            alignment = Alignment.CenterVertically
                        )

                        )
                    Spacer(modifier = modifier.width(120.dp))
                    Row(modifier = modifier.padding(horizontal = 3.dp),
                    horizontalArrangement = Arrangement.End) {
                        RoundedIconButton(imageVector = Icons.Default.Remove,
                            onClick = {
                                splitCounterState.value = if (splitCounterState.value >1) splitCounterState.value-1 else 1
                                totalPerPersonState.value = calculateTotalPerPerson(totalBillState.value.toDouble(),splitCounterState.value,tipPercentage)

                            })
                        Text(text = "${splitCounterState.value}", modifier = modifier
                            .align(alignment = Alignment.CenterVertically)
                            .padding(start = 9.dp, end = 9.dp))
                        RoundedIconButton(imageVector = Icons.Default.Add,
                            onClick = {
                                if (splitCounterState.value<range.last)
                                splitCounterState.value = splitCounterState.value+1
                                totalPerPersonState.value = calculateTotalPerPerson(totalBillState.value.toDouble(),splitCounterState.value,tipPercentage)

                            })
                    }
                }

            Row(modifier = modifier
                .padding(horizontal = 3.dp, vertical = 12.dp)
                .align(alignment = Alignment.Start)) {
                Text(text = "Tip", modifier = modifier.align(alignment = Alignment.CenterVertically))
                Spacer(modifier = modifier.width(200.dp))
                Text(text = "$ ${tipAmountState.value}", modifier = modifier.align(alignment = Alignment.CenterVertically))
            }
            
            Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
                Text(text = "$tipPercentage %")
                Spacer(modifier = modifier.height(14.dp))
                Slider(value = sliderPositionState.value, steps = 5,onValueChange = { newVal ->
                    sliderPositionState.value = newVal
                   tipAmountState.value = calculateTotalTip(totalBillState.value.toDouble(),tipPercentage)
                    totalPerPersonState.value = calculateTotalPerPerson(totalBillState.value.toDouble(),splitCounterState.value,tipPercentage)
                }, modifier = modifier.padding(start = 16.dp, end = 16.dp)
                )
            }
            }
            else{
                Box() {

                }
            }
        }
    }
}


