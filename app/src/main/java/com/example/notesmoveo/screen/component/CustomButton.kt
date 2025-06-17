package com.example.notesmoveo.screen.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notesmoveo.ui.theme.Orange

@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    enable: Boolean = true,
    textSize: TextUnit = 20.sp,
){
    Button(
        modifier = modifier
            .fillMaxWidth(),
        onClick = onClick,
        enabled = enable,
        shape = RoundedCornerShape(size = 6.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Orange,
        ),
        contentPadding = PaddingValues(all = 20.dp)
    ){
        Text(
            text = text,
            fontSize = textSize,
            fontWeight = FontWeight.Medium
        )
    }
}