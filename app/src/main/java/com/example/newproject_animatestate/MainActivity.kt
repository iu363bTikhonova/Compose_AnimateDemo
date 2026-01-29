package com.example.newproject_animatestate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring.DampingRatioHighBouncy
import androidx.compose.animation.core.Spring.StiffnessVeryLow
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class BoxColor {
    Red, Magenta
}

enum class BoxPosition {
    Start, End
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}
@Composable
fun MainScreen(){
    val scrollState =rememberScrollState()
    Column(
        Modifier.verticalScroll(scrollState)
    ){
        RotationDemo()
        ColorChangeDemo()
        MotionDemo()
        TransitionDemo()
    }
}
@Composable
fun MotionDemo() {
    var boxState by remember { mutableStateOf(BoxPosition.Start) }
    val screenWidth=(LocalConfiguration.current.screenWidthDp.dp)
    val boxSideLength = 70.dp
    val animatedOffset: Dp by animateDpAsState(
       targetValue=when(boxState){
           BoxPosition.Start->0.dp
           BoxPosition.End -> screenWidth-boxSideLength
       },
        //animationSpec=tween(500),label="Motion"
        animationSpec=spring(dampingRatio=DampingRatioHighBouncy,
            stiffness=StiffnessVeryLow), label="Motion"
        /*animationSpec=keyframes{
            durationMillis=1000
            100.dp.at(10)
            110.dp.at(500)
            200.dp.at(700)
        }*/
    )
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .offset(x = animatedOffset  , y = 20.dp)
                .size(boxSideLength)
                .background(Color.Red)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                boxState = when (boxState) {
                    BoxPosition.Start -> BoxPosition.End
                    BoxPosition.End -> BoxPosition.Start
                }
            },
            modifier=Modifier.padding(20.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(text="Move Box")
        }
    }
}
@Composable
fun TransitionDemo(){
    var boxState by remember{mutableStateOf(BoxPosition.Start)}
    val screenWidth=LocalConfiguration.current.screenWidthDp.dp
    val transition=updateTransition(targetState=boxState,
        label="Color and Motion")
    val animatedColor:Color by transition.animateColor(
        transitionSpec = {
            tween(4000)
        }, label="colorAnimation"
    ){
        state-> when(state){
            BoxPosition.End -> Color.Magenta
        BoxPosition.Start -> Color.Red
        }
    }
    val animatedOffset:Dp by transition.animateDp(
        transitionSpec = {
            tween(4000)
        },label="offsetAnimation"
    ){
        state->when(state){
            BoxPosition.Start -> 0.dp
        BoxPosition.End -> screenWidth-70.dp
        }
    }
    Column(Modifier.fillMaxWidth()){
        Box(
            modifier= Modifier
                .offset(x=animatedOffset,y=20.dp)
                .size(70.dp)
                .background(animatedColor)
        )
        Spacer(Modifier.height(50.dp))
        Button(
            onClick={
                boxState=when(boxState){
                    BoxPosition.Start -> BoxPosition.End
                    BoxPosition.End -> BoxPosition.Start
                }
            },
            modifier = Modifier.padding(20.dp).align(Alignment.CenterHorizontally)
        ){
            Text(text="StartAnimation")
        }
    }
}
@Composable
fun ColorChangeDemo() {
    var colorState by remember { mutableStateOf(BoxColor.Red) }
    val animatedColor: Color by animateColorAsState(
        targetValue = when (colorState) {
            BoxColor.Red -> Color.Magenta
            BoxColor.Magenta -> Color.Red
        },
        animationSpec = tween(4500), label = ("Color change")
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .padding(20.dp)
                .size(200.dp)
                .background(animatedColor)
        )
        Button(
            onClick = {
                colorState = when (colorState) {
                    BoxColor.Red -> BoxColor.Magenta
                    BoxColor.Magenta -> BoxColor.Red
                }
            },
            modifier = Modifier.padding(10.dp)
        ) {
            Text(text = "Change Color")
        }
    }
}


@Composable
fun RotationDemo() {
    var rotated by remember { mutableStateOf(false) }
    val angle by animateFloatAsState(
        targetValue = if (rotated) 360f else 0f,
        animationSpec = tween(durationMillis = 2500/*, easing=LinearEasing*/), label = "Rotate"
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Image(
            painter = painterResource(R.drawable.propeller),
            contentDescription = "fan",
            modifier = Modifier
                .rotate(angle)
                .padding(10.dp)
                .size(300.dp)

        )
        Button(
            onClick = { rotated = !rotated },
            modifier = Modifier.padding(10.dp)
        ) {
            Text(text = "Rotate Propeller")
        }

    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    RotationDemo()
}