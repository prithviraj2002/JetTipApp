package com.example.jettipapp

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
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jettipapp.component.InputField
import com.example.jettipapp.ui.theme.JetTipAppTheme
import com.example.jettipapp.ui.theme.widgets.RoundIconButton
import com.example.jettipapp.util.calculateTip
import com.example.jettipapp.util.calculateTotalPerPerson
import com.example.jettipapp.TopHeader as TopHeader

@ExperimentalComposeUiApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
                MyApp{
                        TopHeader()
                        MainContent()
                }
            }
        }
    }



@Composable
fun MyApp(content: @Composable () -> Unit){
    JetTipAppTheme {
        // A surface container using the 'background' color from the theme
        Surface(color = MaterialTheme.colors.background) {
                content()
        }
    }
}


/*This is the top header function which displays
the top element of the ui
It also displays amount to be paid per person, and accepts
a totalperperson parameter to display*/
@Preview
@Composable
fun TopHeader(TotalPerPerson :Double = 134.0){
    Surface(modifier = Modifier
        .fillMaxWidth()
        .padding(15.dp)
        .height(150.dp)
        .clip(shape = RoundedCornerShape(corner = CornerSize(12.dp))),
        color = Color(0xFFE9D7F7)
    ) {
            Column(modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
                val total = "%.2f".format(TotalPerPerson)
                Text(text = "Total Per Person:",
                    style = MaterialTheme.typography.h5)
                Text(text = "$$total", style = MaterialTheme.typography.h4,
                    fontWeight = FontWeight.ExtraBold)

            }

    }
}


//This is the main content function which calls the bill form//
@ExperimentalComposeUiApi
@Preview
@Composable
fun MainContent(){
        BillForm(){ billAmt ->
            Log.d("TAG", "MainContent: ${billAmt.toInt()}")
        }
}


/*This is the Billform function which contains all the elements, including
a slider, 2 rounded icon buttons and displays the tip amount
These elements have been encapsulated under a column each element has been
placed row wise*/
@ExperimentalComposeUiApi
@Composable
fun BillForm(modifier: Modifier = Modifier,
                onValChange: (String) -> Unit = { }) {

    val totalBillState = remember {
        mutableStateOf("")
    }
    val validState = remember(totalBillState.value) {
        totalBillState.value.trim().isNotEmpty()
    }
    val keyboardController = LocalSoftwareKeyboardController.current

    val SliderpositionState = remember {
        mutableStateOf(0f)
    }

    val splitByState = remember {
        mutableStateOf(1)
    }

    val tipAmountState = remember {
        mutableStateOf(0.0)
    }

    val TotalPerPersonState = remember {
        mutableStateOf(0.0)
    }
    val tipPercent = (SliderpositionState.value * 100).toInt()
    Surface(
        modifier = Modifier
            .padding(2.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(corner = CornerSize(8.dp)),
        border = BorderStroke(width = 1.dp, color = Color.LightGray)
    ) {
        Column {
            TopHeader(TotalPerPerson = TotalPerPersonState.value)
            InputField(
                valueState = totalBillState,
                labelID = "Enter Bill",
                enabled = true,
                isSingleLine = true,
                onAction = KeyboardActions {
                    if (!validState) return@KeyboardActions
                    onValChange(totalBillState.value.trim())

                    keyboardController?.hide()
                })
            if (validState) {
                Row(
                    modifier = Modifier.padding(3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Split", modifier = Modifier.align(
                            alignment = Alignment.CenterVertically
                        )
                    )
                    Spacer(modifier = Modifier.width(120.dp))
                    Row(
                        modifier = Modifier.padding(horizontal = 3.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        RoundIconButton(
                            onClick = {
                                splitByState.value =
                                if(splitByState.value > 1) splitByState.value -1 else 1
                                TotalPerPersonState.value=
                                    calculateTotalPerPerson(TotalBill = totalBillState.value.toDouble(),
                                        tipPercent = tipPercent, splitBy = splitByState.value.toInt())
                            },
                            imageVector = Icons.Default.Remove
                        )
                        Text(
                            text = "${splitByState.value}", modifier = Modifier
                                .align(CenterVertically)
                                .padding(start = 9.dp, end = 9.dp)
                        )
                        RoundIconButton(
                            onClick = {
                                    splitByState.value =
                                    splitByState.value +1
                                TotalPerPersonState.value=
                                    calculateTotalPerPerson(TotalBill = totalBillState.value.toDouble(),
                                        tipPercent = tipPercent, splitBy = splitByState.value.toInt())
                            },
                            imageVector = Icons.Default.Add
                        )
                    }
                }

                //Tip Row
                Row(modifier = Modifier.padding(horizontal = 3.dp, vertical = 12.dp)) {
                    Text(
                        text = "Tip:",
                        modifier = Modifier.align(alignment = Alignment.CenterVertically)
                    )
                    Spacer(modifier = Modifier.width(200.dp))

                    Text(
                        text = "$ ${tipAmountState.value}",
                        modifier = Modifier.align(alignment = Alignment.CenterVertically)
                    )
                }

                //Tip Percentage
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "$tipPercent %")
                    Spacer(modifier = Modifier.height(14.dp))

                    Slider(
                        value = SliderpositionState.value,
                        onValueChange = { newVal ->
                            SliderpositionState.value = newVal
                            tipAmountState.value=
                                calculateTip(TotalBill = totalBillState.value.toDouble(),
                                    tipPercent = tipPercent)
                            TotalPerPersonState.value=
                                calculateTotalPerPerson(TotalBill = totalBillState.value.toDouble(),
                                    tipPercent = tipPercent, splitBy = splitByState.value.toInt())
                        },
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                        steps = 5,

                        )
                }

            } else {
                //Box() {}
            }
        }

    }
}


@Preview(showBackground = false)
@Composable
fun DefaultPreview() {
    JetTipAppTheme {
        MyApp {

        }
    }
}
