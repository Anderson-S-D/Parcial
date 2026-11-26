package com.example.parcial.presentacion.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.WorkOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.parcial.ui.theme.ParcialTheme

data class DetalleViajeUi(
    val id: String,
    val nombre: String,
    val destino: String,
    val fechaInicio: String,
    val fechaFin: String
)

data class LugarItemUi(
    val id: String,
    val nombre: String,
    val fechaTentativa: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleViajeScreen(
    viaje: DetalleViajeUi,
    lugares: List<LugarItemUi>,
    onAgregarLugarClick: (viajeId: String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    onLugarClick: (String) -> Unit = {},
    onOpcionesLugarClick: (String) -> Unit = {},
    onOpcionesViajeClick: () -> Unit = {}
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = viaje.nombre,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onOpcionesViajeClick) {
                        Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "Opciones del viaje")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAgregarLugarClick(viaje.id) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = CircleShape
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Agregar lugar")
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            LazyColumn(
                modifier = Modifier
                    .widthIn(max = 600.dp)
                    .fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 96.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item(key = "cabecera") {
                    CabeceraViaje(viaje = viaje)
                }
                item(key = "titulo_lugares") {
                    Text(
                        text = "Lugares a visitar (${lugares.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                if (lugares.isEmpty()) {
                    item(key = "sin_lugares") {
                        Text(
                            text = "Este viaje aún no tiene lugares. Toca + para agregar uno.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    items(items = lugares, key = { it.id }) { lugar ->
                        LugarCard(
                            lugar = lugar,
                            onClick = { onLugarClick(lugar.id) },
                            onOpcionesClick = { onOpcionesLugarClick(lugar.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CabeceraViaje(viaje: DetalleViajeUi) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconoEnCaja(icono = Icons.Outlined.WorkOutline, tamano = 56)
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = viaje.nombre,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                DatoConIcono(icono = Icons.Outlined.LocationOn, texto = viaje.destino)
                DatoConIcono(
                    icono = Icons.Outlined.CalendarMonth,
                    texto = "${viaje.fechaInicio} – ${viaje.fechaFin}"
                )
            }
        }
    }
}

@Composable
private fun LugarCard(
    lugar: LugarItemUi,
    onClick: () -> Unit,
    onOpcionesClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(start = 12.dp, top = 12.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconoEnCaja(icono = Icons.Filled.LocationOn, tamano = 40)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = lugar.nombre,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                DatoConIcono(icono = Icons.Outlined.CalendarMonth, texto = lugar.fechaTentativa)
            }
            IconButton(onClick = onOpcionesClick) {
                Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "Opciones del lugar")
            }
        }
    }
}

@Composable
private fun IconoEnCaja(icono: ImageVector, tamano: Int) {
    Surface(
        modifier = Modifier.size(tamano.dp),
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(imageVector = icono, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
private fun DatoConIcono(icono: ImageVector, texto: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 2.dp)
    ) {
        Icon(
            imageVector = icono,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = texto,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

private val viajeDeEjemplo = DetalleViajeUi(
    id = "1",
    nombre = "Europa 2026",
    destino = "Italia",
    fechaInicio = "10 jun 2026",
    fechaFin = "20 jun 2026"
)

private val lugaresDeEjemplo = listOf(
    LugarItemUi(id = "1", nombre = "Coliseo Romano", fechaTentativa = "12 jun 2026"),
    LugarItemUi(id = "2", nombre = "Fontana di Trevi", fechaTentativa = "13 jun 2026"),
    LugarItemUi(id = "3", nombre = "Vaticano", fechaTentativa = "15 jun 2026")
)

@Preview(name = "Detalle del viaje", showBackground = true, showSystemUi = true)
@Composable
private fun DetalleViajeScreenPreview() {
    ParcialTheme {
        DetalleViajeScreen(
            viaje = viajeDeEjemplo,
            lugares = lugaresDeEjemplo,
            onAgregarLugarClick = {},
            onBackClick = {}
        )
    }
}

@Preview(name = "Detalle del viaje - sin lugares", showBackground = true)
@Composable
private fun DetalleViajeScreenSinLugaresPreview() {
    ParcialTheme {
        DetalleViajeScreen(
            viaje = viajeDeEjemplo,
            lugares = emptyList(),
            onAgregarLugarClick = {},
            onBackClick = {}
        )
    }
}
