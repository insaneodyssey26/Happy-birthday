package com.masum.happybirthday

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.animateColorAsState
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
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
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
import androidx.compose.foundation.clickable
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
        delay(2000)
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
    val confettiCount = 40
    val confettiColors = listOf(
        Color(0xFFFFA8B8),
        Color(0xFFFFF59D),
        Color(0xFFB8FFD6),
        Color(0xFFB8A8FF),
        Color(0xFF80DEEA),
        Color(0xFFFFE0EC),
        Color(0xFFE0C3FC)
    )
    val random = remember { java.util.Random() }
    val transition = rememberInfiniteTransition(label = "confetti")
    val yOffsets = List(confettiCount) {
        transition.animateFloat(
            initialValue = -random.nextInt(200).toFloat(),
            targetValue = 1200f + random.nextInt(200),
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 6000 + random.nextInt(2000), easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Restart
            ), label = "y$it"
        )
    }
    val xDrifts = List(confettiCount) {
        transition.animateFloat(
            initialValue = 0f,
            targetValue = (-30..30).random().toFloat(),
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 6000 + random.nextInt(2000), easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ), label = "x$it"
        )
    }
    Canvas(modifier = modifier.fillMaxSize()) {
        for (i in 0 until confettiCount) {
            drawCircle(
                color = confettiColors[i % confettiColors.size],
                radius = 8f + random.nextInt(8),
                center = Offset(
                    x = (size.width / confettiCount) * i + xDrifts[i].value,
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
                    colors = listOf(
                        Color(0xFF181C2F),
                        Color(0xFF232946),
                        Color(0xFF39375B),
                        Color(0xFF4F2E5E),
                        Color(0xFF2C394B),
                        Color(0xFF3A506B),
                        Color(0xFF5F4B8B),
                        Color(0xFF00C9A7),
                        Color(0xFFB983FF),
                        Color(0xFF181C2F)
                    ),
                    start = Offset(0f, offset),
                    end = Offset(offset, 0f)
                )
            )
    ) { content() }
}

@Composable
fun BirthdayWishScreen() {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

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
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 32.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text("🎈", fontSize = 36.sp)
                Text("✨", fontSize = 36.sp)
                Text("🎉", fontSize = 36.sp)
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                AnimatedVisibility(visible = visible, enter = fadeIn(), exit = fadeOut()) {
                    Box(contentAlignment = Alignment.Center) {
                        val glowColor = Color(0xFFFFF59D).copy(alpha = 0.5f)
                        Box(
                            modifier = Modifier
                                .size(260.dp)
                                .shadow(60.dp, CircleShape, ambientColor = glowColor, spotColor = glowColor)
                                .background(glowColor, CircleShape)
                                .align(Alignment.Center)
                        ) {}
                        Box(
                            modifier = Modifier
                                .size(220.dp)
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
                                .align(Alignment.Center)
                        ) {}
                        Image(
                            painter = painterResource(id = R.drawable.her_photo),
                            contentDescription = "Her Photo",
                            modifier = Modifier
                                .size(200.dp)
                                .clip(CircleShape)
                                .shadow(24.dp, CircleShape)
                                .align(Alignment.Center)
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
                    val glowState = remember { mutableStateOf(true) }
                    val glowAnimColor by animateColorAsState(
                        targetValue = if (glowState.value) Color(0xFFFFF59D) else Color(0xFFFFE0EC),
                        animationSpec = tween(durationMillis = 1200, easing = FastOutSlowInEasing)
                    )
                    LaunchedEffect(Unit) {
                        while (true) {
                            glowState.value = !glowState.value
                            delay(1200)
                        }
                    }
                    Text(
                        text = "Happy Birthday! 🎂",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Cursive,
                            fontSize = 44.sp,
                            shadow = Shadow(
                                color = glowAnimColor,
                                offset = Offset(0f, 0f),
                                blurRadius = 24f
                            )
                        ),
                        color = Color(0xFFFFF59D),
                        modifier = Modifier
                            .scale(scale)
                            .padding(bottom = 32.dp)
                    )
                }
                AnimatedVisibility(visible = visible, enter = fadeIn(), exit = fadeOut()) {
                    val glowState2 = remember { mutableStateOf(true) }
                    val glowAnimColor2 by animateColorAsState(
                        targetValue = if (glowState2.value) Color(0xFFFFE0EC) else Color(0xFFB8FFD6),
                        animationSpec = tween(durationMillis = 1200, easing = FastOutSlowInEasing)
                    )
                    LaunchedEffect(Unit) {
                        while (true) {
                            glowState2.value = !glowState2.value
                            delay(1200)
                        }
                    }
                    Text(
                        text = "Always keep bettering yourself, keep growing up... Wishing you all the best!!",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 26.sp,
                            fontFamily = FontFamily.Serif,
                            fontStyle = FontStyle.Italic,
                            lineHeight = 36.sp
                        ),
                        color = Color(0xFFFFE0EC),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .fillMaxWidth()
                            .padding(start = 8.dp, end = 8.dp, bottom = 32.dp),
                        textAlign = TextAlign.Center,
                        maxLines = Int.MAX_VALUE,
                        overflow = TextOverflow.Clip
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
                var showNote by remember { mutableStateOf(false) }
                val animatedGradient = rememberInfiniteTransition(label = "noteBtnGradient")
                val gradientShift by animatedGradient.animateFloat(
                    initialValue = 0f,
                    targetValue = 1000f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(4000, easing = FastOutSlowInEasing),
                        repeatMode = RepeatMode.Restart
                    ), label = "gradientShift"
                )
                AnimatedVisibility(visible = !showNote, enter = fadeIn(), exit = fadeOut()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .height(48.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFFB8FFD6),
                                        Color(0xFFFFA8B8),
                                        Color(0xFFFFF59D),
                                        Color(0xFFB8A8FF),
                                        Color(0xFFB8FFD6)
                                    ),
                                    start = androidx.compose.ui.geometry.Offset(0f, gradientShift),
                                    end = androidx.compose.ui.geometry.Offset(gradientShift, 0f)
                                )
                            )
                            .shadow(12.dp, CircleShape)
                            .border(1.5.dp, Color.White, CircleShape)
                            .clickable { showNote = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            androidx.compose.material3.Icon(
                                painter = painterResource(id = R.drawable.ic_sticky_note),
                                contentDescription = "Note Icon",
                                tint = Color(0xFF232946),
                                modifier = Modifier.size(20.dp).padding(end = 6.dp)
                            )
                            Text(
                                text = "Show Note",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF232946),
                                    fontSize = 18.sp
                                ),
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
                AnimatedVisibility(visible = showNote, enter = fadeIn(), exit = fadeOut()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(60.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFE0EC)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Akhono implement kora hoini :(",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF232946),
                                fontSize = 18.sp
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(48.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text("💐", fontSize = 36.sp, color = Color(0xFFFFF59D))
                    Text("🥳", fontSize = 36.sp, color = Color(0xFFFFE0EC))
                    Text("🌸", fontSize = 36.sp, color = Color(0xFFB8FFD6))
                }
            }
        }
    }
}
