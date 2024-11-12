package com.proyecto.WalkDog.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.*
import androidx.glance.action.actionStartActivity
import androidx.glance.appwidget.CheckBox
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.*
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.text.TextAlign
import androidx.glance.unit.ColorProvider
import com.proyecto.WalkDog.MainActivity

class TaskWidgetContent : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            GlanceTheme {
                TaskListContent()
            }
        }
    }

    @Composable
    private fun TaskListContent() {
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(ColorProvider(Color(0xFFF0F0F0)))  // Fondo gris claro para todo el widget
                .padding(16.dp),
            verticalAlignment = Alignment.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título del widget
            Text(
                text = "Mis Tareas Pendientes",
                style = TextStyle(
                    color = ColorProvider(Color(0xFF333333)),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                ),
                modifier = GlanceModifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            // Ejemplo de tareas en "tarjetas" simuladas con borde
            val tasks = listOf("Revisar reportes", "Enviar correo a cliente", "Preparar documentos")

            tasks.forEach { task ->
                // Contenedor exterior simula el borde con fondo gris
                Column(
                    modifier = GlanceModifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .background(ColorProvider(Color(0xFFD3D3D3)))  // Color gris claro para el borde
                        .padding(1.dp)  // Espacio para simular el grosor del borde
                ) {
                    // Contenedor interior con fondo blanco
                    Row(
                        modifier = GlanceModifier
                            .fillMaxWidth()
                            .background(ColorProvider(Color(0xFFFFFFFF)))  // Fondo blanco para la tarjeta
                            .padding(12.dp),  // Espaciado interno de la tarjeta
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalAlignment = Alignment.Start
                    ) {
                        CheckBox(checked = false, onCheckedChange = {
                            // Acción al marcar la tarea como completada
                        })
                        Text(
                            text = task,
                            modifier = GlanceModifier.padding(start = 8.dp),
                            style = TextStyle(
                                color = ColorProvider(Color(0xFF333333)),
                                fontSize = 14.sp
                            )
                        )
                    }
                }
            }

            // Botón de acción para abrir la app
            Button(
                text = "Abrir app",
                onClick = actionStartActivity<MainActivity>(),
                modifier = GlanceModifier
                    .padding(top = 20.dp)
                    .background(ColorProvider(Color(0xFF6200EA)))  // Color púrpura
                    .fillMaxWidth()
                    .padding(8.dp)
            )
        }
    }
}