package com.apachi.auraskin.feature.analytics

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Healing
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.Nightlight
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.apachi.auraskin.designsystem.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(
    navController: NavController,
    viewModel: AnalyticsViewModel = hiltViewModel()
) {
    val analyticsData by viewModel.analyticsData.collectAsState(initial = emptyList())

    // Mock aggregate data for the combined chart
    val aggregateData = List(7) { dayIndex ->
        val totalCompletions = analyticsData.sumOf { it.last7DaysCompletions.getOrElse(dayIndex) { 0 } }
        val maxPossible = analyticsData.size.coerceAtLeast(1)
        (totalCompletions.toFloat() / maxPossible).coerceIn(0f, 1f)
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                // Page Header
                Text(
                    text = "Skin Insights",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Discover the subtle rhythms connecting your habits to your complexion.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(32.dp))
            }

            // Habit Synergy Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.large) // 24dp
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(24.dp)
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.8f))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Icon(Icons.Outlined.AutoAwesome, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("HABIT SYNERGY DETECTED", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Your consistent evening routine is compounding hydration benefits.",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = MaterialTheme.typography.titleLarge.lineHeight
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = { },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Apply Recommendations")
                        }
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }

            // Correlation Analysis Chart
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
                    Text("Correlation Analysis", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurface)
                    Text("LAST 7 DAYS", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Spacer(modifier = Modifier.height(16.dp))
                
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.large)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(24.dp)
                ) {
                    Column {
                        HybridChart(barData = if (aggregateData.isEmpty()) List(7){0f} else aggregateData)
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun").forEachIndexed { index, day ->
                                Text(
                                    text = day,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (index == 6) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontWeight = if (index == 6) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(12.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primaryContainer))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Habit Consistency", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface)
                            
                            Spacer(modifier = Modifier.width(24.dp))
                            
                            Box(modifier = Modifier.size(width = 12.dp, height = 4.dp).clip(CircleShape).background(MaterialTheme.colorScheme.secondaryContainer))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Skin Score", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }

            // Bento Grid Metrics
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    BentoCard(
                        icon = Icons.Outlined.WaterDrop,
                        iconTint = MaterialTheme.colorScheme.primary,
                        title = "Hydration Link",
                        value = "Strong",
                        modifier = Modifier.weight(1f).aspectRatio(1f)
                    )
                    BentoCard(
                        icon = Icons.Outlined.Healing,
                        iconTint = MaterialTheme.colorScheme.secondary,
                        title = "Recovery Rate",
                        value = "+2.4/wk",
                        modifier = Modifier.weight(1f).aspectRatio(1f)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.large)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("SURFACE TEXTURE", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("Smoother", style = MaterialTheme.typography.headlinesmall, color = MaterialTheme.colorScheme.onSurface)
                    }
                    Box(modifier = Modifier.size(48.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surface), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Face, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }

            // Activity Influence
            item {
                Text("Activity Influence", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.height(16.dp))
                
                ActivityInfluenceItem(
                    icon = Icons.Outlined.Nightlight,
                    title = "Evening Double Cleanse",
                    subtitle = "High Positive Impact",
                    trendIcon = Icons.Outlined.TrendingUp,
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    iconColor = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Spacer(modifier = Modifier.height(12.dp))
                ActivityInfluenceItem(
                    icon = Icons.Outlined.LightMode,
                    title = "Sunscreen",
                    subtitle = "Steady Protection",
                    trendIcon = Icons.Outlined.Shield,
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    iconColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}

@Composable
fun HybridChart(barData: List<Float>) {
    val barColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
    val activeBarColor = MaterialTheme.colorScheme.primary
    val lineStrokeBrush = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.secondary,
            MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f)
        )
    )
    
    Box(modifier = Modifier.fillMaxWidth().height(200.dp)) {
        // Bars
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            barData.forEachIndexed { index, ratio ->
                val adjustedRatio = if (ratio == 0f) 0.1f else ratio // Ensure minimum height for visual presence
                Box(
                    modifier = Modifier
                        .fillMaxHeight(adjustedRatio)
                        .width(32.dp)
                        .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                        .background(if (index == 6) activeBarColor else barColor)
                )
            }
        }
        
        // Mock Skin Score Line Chart via Canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val path = Path()
            
            // Mock curve points
            val points = listOf(
                Offset(0f, height * 0.7f),
                Offset(width * 0.25f, height * 0.85f),
                Offset(width * 0.5f, height * 0.4f),
                Offset(width * 0.8f, height * 0.3f),
                Offset(width, height * 0.3f)
            )
            
            path.moveTo(points[0].x, points[0].y)
            path.cubicTo(
                points[1].x, points[1].y,
                points[2].x, points[2].y,
                points[3].x, points[3].y
            )
            
            drawPath(
                path = path,
                brush = lineStrokeBrush,
                style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
            )
            
            // Draw points
            points.forEach { point ->
                drawCircle(
                    brush = lineStrokeBrush,
                    radius = 6.dp.toPx(),
                    center = point
                )
            }
        }
    }
}

@Composable
fun BentoCard(icon: androidx.compose.ui.graphics.vector.ImageVector, iconTint: Color, title: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surface), contentAlignment = Alignment.Center) {
            Icon(icon, contentDescription = null, tint = iconTint)
        }
        Column {
            Text(title.uppercase(), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            Text(value, style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun ActivityInfluenceItem(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, subtitle: String, trendIcon: androidx.compose.ui.graphics.vector.ImageVector, containerColor: Color, iconColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(48.dp).clip(CircleShape).background(containerColor), contentAlignment = Alignment.Center) {
            Icon(icon, contentDescription = null, tint = iconColor)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
            Text(subtitle.uppercase(), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Box(modifier = Modifier.size(32.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primaryContainer.copy(alpha=0.3f)), contentAlignment = Alignment.Center) {
            Icon(trendIcon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
        }
    }
}
