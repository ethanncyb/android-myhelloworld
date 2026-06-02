package com.icodeland.myhelloworld

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.icodeland.myhelloworld.ui.theme.MyHelloWorldTheme
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyHelloWorldTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HelloWorldScreen(
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }
}

data class FallingEmoji(
    val id: Long,
    val emoji: String,
    val x: Float,
    val y: Float,
)

@Composable
fun HelloWorldScreen(modifier: Modifier = Modifier) {
    var currentTime by remember { mutableStateOf(LocalTime.now()) }
    val emojis = remember { mutableStateListOf<FallingEmoji>() }
    val emojiList = listOf("❤️", "⭐", "🔥", "✨", "🎉", "🎈", "🚀", "🌈", "🥳", "😎")

    LaunchedEffect(Unit) {
        while (true) {
            currentTime = LocalTime.now()
            delay(1000)
        }
    }

    val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")

    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    emojis.add(
                        FallingEmoji(
                            id = System.currentTimeMillis(),
                            emoji = emojiList.random(),
                            x = offset.x,
                            y = offset.y,
                        ),
                    )
                }
            },
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Hello World!",
                fontSize = 32.sp,
                style = MaterialTheme.typography.headlineLarge,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Current Time:",
                fontSize = 18.sp,
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                text = currentTime.format(formatter),
                fontSize = 48.sp,
                style = MaterialTheme.typography.displayLarge,
            )
        }

        emojis.forEach { emoji ->
            PopUpEmoji(
                emoji = emoji,
                onAnimationFinished = { emojis.remove(emoji) },
            )
        }
    }
}

@Composable
fun PopUpEmoji(emoji: FallingEmoji, onAnimationFinished: () -> Unit) {
    val animatable = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        animatable.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000),
        )
        onAnimationFinished()
    }

    Text(
        text = emoji.emoji,
        fontSize = 40.sp,
        modifier = Modifier
            .offset {
                IntOffset(
                    emoji.x.toInt() - 60,
                    (emoji.y - animatable.value * 200).toInt() - 60,
                )
            }
            .graphicsLayer(alpha = 1f - animatable.value),
    )
}

@Preview(showBackground = true)
@Composable
fun HelloWorldScreenPreview() {
    MyHelloWorldTheme {
        HelloWorldScreen()
    }
}
