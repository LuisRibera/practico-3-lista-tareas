package com.example.app_lista_tareas.ui.componentes

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import com.example.app_lista_tareas.data.entidades.Prioridad

@Composable
fun ChipPrioridad(prioridad: Prioridad, modifier: Modifier = Modifier) {
    val color = colorDePrioridad(prioridad)
    val etiqueta = when (prioridad) {
        Prioridad.ALTA -> "Alta"
        Prioridad.MEDIA -> "Media"
        Prioridad.BAJA -> "Baja"
    }
    Box(
        modifier = modifier
            .background(color = color.copy(alpha = 0.12f), shape = RoundedCornerShape(50))
            .border(width = 1.dp, color = color, shape = RoundedCornerShape(50))
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Text(
            text = etiqueta,
            color = color,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

fun colorDePrioridad(prioridad: Prioridad): Color = when (prioridad) {
    Prioridad.ALTA -> Color(0xFFE53935)
    Prioridad.MEDIA -> Color(0xFFFFB300)
    Prioridad.BAJA -> Color(0xFF43A047)
}
