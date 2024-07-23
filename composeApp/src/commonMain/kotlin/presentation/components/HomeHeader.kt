package presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierInfo
import androidx.compose.ui.unit.dp
import currencyproject.composeapp.generated.resources.Res
import currencyproject.composeapp.generated.resources.exchange_illustration
import currencyproject.composeapp.generated.resources.refresh_ic

import domain.model.RateStatus
import headerColor
import org.jetbrains.compose.resources.painterResource
import staleColor
import util.displayCurrentDateTime

@Composable
fun HomeHeader(status: RateStatus, onRateRefresh: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp))
            .background(headerColor)
            .padding(all = 24.dp)
    ) {
        Spacer(Modifier.height(24.dp))
        RateStatus(status = status, onRateRefresh = onRateRefresh)

    }
}

@Composable
fun RateStatus(
    status: RateStatus,
    onRateRefresh: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            Image(
                modifier = Modifier.size(50.dp),
                painter = painterResource(Res.drawable.exchange_illustration),
                contentDescription = "Exchange Rate Illustration"
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = displayCurrentDateTime(),
                    color = Color.White
                )
                Text(
                    text = status.title,
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    color = status.color
                )
            }
        }
        if (status == RateStatus.Stale) {
        IconButton(onClick = onRateRefresh) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(Res.drawable.refresh_ic),
                contentDescription = "Refresh Icon",
                tint = staleColor
            )
        }

        }
    }
}