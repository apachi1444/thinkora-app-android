package com.apachi.thinkora.feature.drawer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import com.apachi.thinkora.R

@Composable
fun DrawerContent(
    onLogoutClick: () -> Unit,
    onAchievementsClick: () -> Unit,
    onCustomQuotesClick: () -> Unit,
    onAnalyticsClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(320.dp) // Adjust width as needed
            .clip(RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp))
            .clip(RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(24.dp)
    ) {
        // --- Header Section ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.LightGray)
            ) {
                 // Placeholder for User Image
                 // Image(painter = rememberAsyncImagePainter("url"), contentDescription = null, contentScale = ContentScale.Crop)
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                 IconButton(
                    onClick = { /* TODO */ },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF1F5F9))
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
                IconButton(
                    onClick = { /* TODO */ },
                    modifier = Modifier
                         .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF1F5F9))
                ) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = "Favorites",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.settings_my_profile),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit Profile",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(16.dp)
            )
        }
        Text(
            text = "zararehman@domain.io", // Dummy email from screenshot
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        // --- Menu Items ---
        DrawerMenuItem(icon = Icons.Outlined.Star, label = "Achievements", onClick = onAchievementsClick)
        Spacer(modifier = Modifier.height(24.dp))
        DrawerMenuItem(icon = Icons.Outlined.Edit, label = "My Quotes", onClick = onCustomQuotesClick)
        Spacer(modifier = Modifier.height(24.dp))
        DrawerMenuItem(icon = Icons.Outlined.Person, label = "Detailed Analytics", onClick = onAnalyticsClick)
        Spacer(modifier = Modifier.height(24.dp))
        DrawerMenuItem(icon = Icons.Outlined.Delete, label = "Payment Methods", onClick = {})
        Spacer(modifier = Modifier.height(24.dp))
        DrawerMenuItem(icon = Icons.Outlined.DateRange, label = "Payment History", onClick = {})
        Spacer(modifier = Modifier.height(24.dp))
        DrawerMenuItem(icon = Icons.Outlined.Lock, label = "Change Password", onClick = {})
        Spacer(modifier = Modifier.height(24.dp))
        DrawerMenuItem(icon = Icons.Outlined.Person, label = "Invites Friends", onClick = {})
        Spacer(modifier = Modifier.height(24.dp))
        DrawerMenuItem(icon = Icons.Outlined.Info, label = "FAQs", onClick = {})

        Spacer(modifier = Modifier.weight(1f))

        // --- Footer ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onLogoutClick() }
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
             Icon(
                imageVector = Icons.Default.ExitToApp, // Using ExitToApp as Close/Logout
                contentDescription = "Logout",
                 tint = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = stringResource(R.string.drawer_logout),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun DrawerMenuItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium
        )
    }
}
