package com.wealth.sampatika.ui.plan.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.wealth.sampatika.ui.plan.model.GoalType

@Composable
fun GoalCategoryCard(
    type: GoalType,
    imageRes: Int,
    description: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val borderColor =
        if (isSelected)
            MaterialTheme.colorScheme.primary
        else
            Color.Transparent

    Card(
        modifier = modifier
            .height(120.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(2.dp, borderColor)
    ) {

        Column (
            verticalArrangement = Arrangement.SpaceBetween
        ){

            Image(
                painter = painterResource(id = imageRes),
                contentDescription = type.displayName,
                modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth()
                    .clip(
                        RoundedCornerShape(
                            topStart = 20.dp,
                            topEnd = 20.dp
                        )
                    ),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .padding(horizontal = 6.dp, vertical = 4.dp)
            ) {

                Text(
                    text = type.displayName,
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1
                )

                Text(
                    text = description,
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}