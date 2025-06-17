package com.example.notesmoveo.screen.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NoteAdd
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notesmoveo.utils.Resources

@Composable
fun EmptyListMessage(
    modifier: Modifier = Modifier,
    message: String = Resources.Note.emptyListTitle,
    subMessage: String = Resources.Note.emptyListSutTitle
) {

    Column (
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = message,
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = subMessage,
            fontSize = 18.sp,
            color = Color.Black.copy(0.6f)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Icon(
            modifier = Modifier.size(size = 120.dp),
            imageVector = Icons.Default.NoteAdd,
            contentDescription = "List icon"
        )
    }
}