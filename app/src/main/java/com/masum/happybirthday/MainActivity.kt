package com.masum.happybirthday

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.masum.happybirthday.ui.theme.HappyBirthdayTheme
import kotlinx.coroutines.delay
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HappyBirthdayTheme {
                val navController = rememberNavController()
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    NavHost(navController = navController, startDestination = "splash") {
                        composable("splash") { SplashScreen(navController) }
                        composable("reveal") { RevealButtonScreen(navController) }
                        composable("wish") { BirthdayWishScreen() }
                    }
                }
            }
        }
    }
}

@Composable
fun SplashScreen(navController: NavHostController) {
    LaunchedEffect(Unit) {
        delay(2000) // Show splash for 2 seconds
        navController.navigate("reveal") {
            popUpTo("splash") { inclusive = true }
        }
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "�� Welcome! 🎈", style = MaterialTheme.typography.headlineMedium)
    }
}

@Composable
fun RevealButtonScreen(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(onClick = { navController.navigate("wish") }) {
            Text(text = "Tap to reveal the surprise!")
        }
    }
}

@Composable
fun BirthdayWishScreen() {
    // Animation state
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    // Glowing border animation for photo
    val infiniteTransition = rememberInfiniteTransition(label = "glow")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000),
            repeatMode = RepeatMode.Restart
        ), label = "angle"
    )

    // Gradient background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFFFE0EC), Color(0xFFE0C3FC))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AnimatedVisibility(visible = visible, enter = fadeIn(), exit = fadeOut()) {
                Box(contentAlignment = Alignment.Center) {
                    // Glowing animated border
                    Box(
                        modifier = Modifier
                            .size(200.dp)
                            .rotate(angle)
                            .border(
                                width = 8.dp,
                                brush = Brush.sweepGradient(
                                    listOf(
                                        Color(0xFFFFA8B8),
                                        Color(0xFFB8A8FF),
                                        Color(0xFFB8FFD6),
                                        Color(0xFFFFA8B8)
                                    )
                                ),
                                shape = CircleShape
                            )
                    ) {}
                    Image(
                        painter = painterResource(id = R.drawable.her_photo),
                        contentDescription = "Her Photo",
                        modifier = Modifier
                            .size(180.dp)
                            .clip(CircleShape)
                            .shadow(16.dp, CircleShape)
                    )
                }
            }
            AnimatedVisibility(visible = visible, enter = fadeIn(), exit = fadeOut()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Happy Birthday! 🎂",
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color(0xFF7C3AED)
                    )
                    Text(
                        text = "Always keep going, keep growing up... Wishing you all the best!!",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color(0xFF6D28D9),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
            // Simple confetti effect (emoji)
            AnimatedVisibility(visible = visible, enter = fadeIn(), exit = fadeOut()) {
                Text("🎂💝🎈", style = MaterialTheme.typography.headlineMedium)
            }
        }
    }
}
