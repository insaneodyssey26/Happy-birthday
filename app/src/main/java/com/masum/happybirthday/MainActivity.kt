package com.masum.happybirthday

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.masum.happybirthday.ui.theme.HappyBirthdayTheme
import kotlinx.coroutines.delay
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.geometry.Offset
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.scale
import androidx.compose.foundation.layout.offset
import androidx.compose.animation.core.rememberInfiniteTransition
import coil.compose.AsyncImage
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.text.font.FontStyle

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HappyBirthdayApp()
        }
    }
}

@Composable
fun HappyBirthdayApp() {
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

@Composable
fun SplashScreen(navController: NavHostController) {
    LaunchedEffect(Unit) {
        delay(2000) // Show splash for 2 seconds
        navController.navigate("reveal") {
            popUpTo("splash") { inclusive = true }
        }
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "🎈 Welcome! 🎈", style = MaterialTheme.typography.headlineMedium)
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
fun AnimatedConfetti(modifier: Modifier = Modifier) {
    // Simple confetti animation using Canvas and random positions/colors
    val confettiCount = 40
    val confettiColors = listOf(
        Color(0xFFFFA8B8), Color(0xFFB8A8FF), Color(0xFFB8FFD6), Color(0xFFFFF59D), Color(0xFF80DEEA)
    )
    val random = remember { java.util.Random() }
    val transition = rememberInfiniteTransition(label = "confetti")
    val yOffsets = List(confettiCount) {
        transition.animateFloat(
            initialValue = -random.nextInt(200).toFloat(),
            targetValue = 1200f + random.nextInt(200),
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 3500 + random.nextInt(1000), easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Restart
            ), label = "y$it"
        )
    }
    Canvas(modifier = modifier.fillMaxSize()) {
        for (i in 0 until confettiCount) {
            drawCircle(
                color = confettiColors[i % confettiColors.size],
                radius = 8f + random.nextInt(8),
                center = Offset(
                    x = (size.width / confettiCount) * i + random.nextInt(30),
                    y = yOffsets[i].value
                )
            )
        }
    }
}

@Composable
fun BouncingEmojis(emojis: String) {
    val infiniteTransition = rememberInfiniteTransition(label = "bounce")
    val offsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -30f,
        animationSpec = infiniteRepeatable(
            animation = tween(700, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "bounceY"
    )
    Text(
        emojis,
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier.offset(y = offsetY.dp)
    )
}

@Composable
fun AnimatedGradientBackground(content: @Composable () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "bg")
    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ), label = "bgOffset"
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(0xFFFFE0EC), Color(0xFFE0C3FC), Color(0xFFB8FFD6)),
                    start = Offset(0f, offset),
                    end = Offset(offset, 0f)
                )
            )
    ) { content() }
}

@Composable
fun BirthdayWishScreen() {
    // Animation state
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    // Glowing border animation for photo (pulse)
    val infiniteTransition = rememberInfiniteTransition(label = "glow")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000),
            repeatMode = RepeatMode.Restart
        ), label = "angle"
    )
    val borderWidthPx by infiniteTransition.animateFloat(
        initialValue = 8f,
        targetValue = 16f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "borderWidth"
    )
    val borderWidth = borderWidthPx.dp

    AnimatedGradientBackground {
        AnimatedConfetti(modifier = Modifier)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 0.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top festive row
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 32.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text("🎈", fontSize = 36.sp)
                Text("✨", fontSize = 36.sp)
                Text("🎉", fontSize = 36.sp)
            }
            // Main content
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                AnimatedVisibility(visible = visible, enter = fadeIn(), exit = fadeOut()) {
                    Box(contentAlignment = Alignment.Center) {
                        // Glowing animated border (pulse)
                        Box(
                            modifier = Modifier
                                .size(200.dp)
                                .rotate(angle)
                                .border(
                                    width = borderWidth,
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
                Spacer(modifier = Modifier.height(20.dp))
                AnimatedVisibility(visible = visible, enter = fadeIn(), exit = fadeOut()) {
                    val scale by infiniteTransition.animateFloat(
                        initialValue = 1f,
                        targetValue = 1.15f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(900, easing = FastOutSlowInEasing),
                            repeatMode = RepeatMode.Reverse
                        ), label = "textScale"
                    )
                    Text(
                        text = "Happy Birthday! 🎂",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Cursive,
                            fontSize = 38.sp
                        ),
                        color = Color(0xFF7C3AED),
                        modifier = Modifier.scale(scale)
                    )
                }
                AnimatedVisibility(visible = visible, enter = fadeIn(), exit = fadeOut()) {
                    Text(
                        text = "Always keep going, keep growing up... Wishing you all the best!!",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 22.sp,
                            fontFamily = FontFamily.Serif,
                            fontStyle = FontStyle.Italic
                        ),
                        color = Color(0xFFB80060),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
                AnimatedVisibility(visible = visible, enter = fadeIn(), exit = fadeOut()) {
                    BouncingEmojis("🎂💝🎈")
                }
                Spacer(modifier = Modifier.height(24.dp))
                AnimatedVisibility(visible = visible, enter = fadeIn(), exit = fadeOut()) {
                    AsyncImage(
                        model = "android.resource://com.masum.happybirthday/drawable/cute_birthday",
                        contentDescription = "Cute Birthday GIF",
                        modifier = Modifier.size(120.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            // Bottom festive row
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text("💐", fontSize = 32.sp)
                Text("🥳", fontSize = 32.sp)
                Text("🌸", fontSize = 32.sp)
            }
        }
    }
}
