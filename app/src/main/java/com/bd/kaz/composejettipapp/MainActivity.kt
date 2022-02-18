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
                       TopHeader()
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
    BillForm(){billAmount ->
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
fun BillForm(modifier: Modifier = Modifier,onValChange :(String) -> Unit ={}){
    val totalBillState = remember {
        mutableStateOf("")
    }

    val validState = remember(totalBillState.value) {
        totalBillState.value.trim().isNotEmpty()
    }

    val sliderPositionState = remember {
        mutableStateOf(0f)
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        shape = RoundedCornerShape(corner = CornerSize(8.dp)),
        border = BorderStroke(width = 1.dp, color = Color.LightGray),
        elevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(6.dp),
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
                    keyboardController?.hide()
                }
            )
//            if (validState){
                Row(
                    modifier = Modifier.padding(3.dp),
                    horizontalArrangement = Arrangement.Start,
                ) {
                    Text(
                        text = "Split",
                        modifier = Modifier.align(
                            alignment = Alignment.CenterVertically
                        )

                        )
                    Spacer(modifier = Modifier.width(120.dp))
                    Row(modifier = Modifier.padding(horizontal = 3.dp),
                    horizontalArrangement = Arrangement.End) {
                        RoundedIconButton(imageVector = Icons.Default.Remove,
                            onClick = { /*TODO*/ })
                        Text(text = "1", modifier = Modifier
                            .align(alignment = Alignment.CenterVertically)
                            .padding(start = 9.dp, end = 9.dp))
                        RoundedIconButton(imageVector = Icons.Default.Add,
                            onClick = { /*TODO*/ })
                    }
                }

            Row(modifier = Modifier
                .padding(horizontal = 3.dp, vertical = 12.dp)
                .align(alignment = Alignment.Start)) {
                Text(text = "Tip", modifier = Modifier.align(alignment = Alignment.CenterVertically))
                Spacer(modifier = Modifier.width(200.dp))
                Text(text = "$33", modifier = Modifier.align(alignment = Alignment.CenterVertically))
            }
            
            Column(modifier = Modifier, horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
                Text(text = "%33")
                Spacer(modifier = Modifier.height(14.dp))
                Slider(value = sliderPositionState.value, onValueChange = { newVal ->
                    sliderPositionState.value = newVal
                    Log.e("Slider", "Slider Value: $newVal", )
                })
            }
//            }
//            else{
//                Box() {
//
//                }
//            }
        }
    }
}