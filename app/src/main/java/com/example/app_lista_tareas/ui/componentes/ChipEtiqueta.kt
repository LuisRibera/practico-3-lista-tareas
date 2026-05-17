package com.example.app_lista_tareas.ui.componentes

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import com.example.app_lista_tareas.data.entidades.Etiqueta

@Composable
fun ChipEtiqueta(etiqueta: Etiqueta, modifier: Modifier = Modifier) {
    val color = MaterialTheme.colorScheme.secondary
    Box(
        modifier = modifier
            .background(color = color.copy(alpha = 0.1f), shape = RoundedCornerShape(50))
            .border(width = 1.dp, color = color.copy(alpha = 0.4f), shape = RoundedCornerShape(50))
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Text(
            text = etiqueta.nombre,
            color = color,
            fontSize = 11.sp
        )
    }
}
