package com.example.parcial.presentacion.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.parcial.presentacion.view.AgregarLugarScreen
import com.example.parcial.presentacion.view.CategoriaUi
import com.example.parcial.presentacion.view.CrearViajeScreen
import com.example.parcial.presentacion.view.DetalleViajeScreen
import com.example.parcial.presentacion.view.DetalleViajeUi
import com.example.parcial.presentacion.view.LugarItemUi
import com.example.parcial.presentacion.view.ViajeItemUi
import com.example.parcial.presentacion.view.ViajesScreen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

private enum class Destino { VIAJES, CREAR_VIAJE, DETALLE_VIAJE, AGREGAR_LUGAR }

private val viajesDeEjemplo = listOf(
    ViajeItemUi(id = "1", nombre = "Europa 2026", destino = "Italia"),
    ViajeItemUi(id = "2", nombre = "Santa Marta", destino = "Colombia"),
    ViajeItemUi(id = "3", nombre = "Bogotá Cultural", destino = "Colombia")
)

private val lugaresDeEjemplo = listOf(
    LugarItemUi(id = "1", nombre = "Coliseo Romano", fechaTentativa = "12 jun 2026"),
    LugarItemUi(id = "2", nombre = "Fontana di Trevi", fechaTentativa = "13 jun 2026"),
    LugarItemUi(id = "3", nombre = "Vaticano", fechaTentativa = "15 jun 2026")
)

private val categoriasDeEjemplo = listOf(
    CategoriaUi(id = 1, nombre = "Histórico"),
    CategoriaUi(id = 2, nombre = "Cultural"),
    CategoriaUi(id = 3, nombre = "Natural"),
    CategoriaUi(id = 4, nombre = "Religioso"),
    CategoriaUi(id = 5, nombre = "Gastronomía"),
    CategoriaUi(id = 6, nombre = "Aventura"),
    CategoriaUi(id = 7, nombre = "Otro")
)

private fun formatearFecha(millis: Long): String {
    val formato = SimpleDateFormat("d MMM yyyy", Locale.forLanguageTag("es-CO"))
    formato.timeZone = TimeZone.getTimeZone("UTC")
    return formato.format(Date(millis))
}

@Composable
fun AppNavGraph() {
    var destino by rememberSaveable { mutableStateOf(Destino.VIAJES) }
    var viajeSeleccionadoId by rememberSaveable { mutableStateOf("1") }

    BackHandler(enabled = destino != Destino.VIAJES) {
        destino = when (destino) {
            Destino.AGREGAR_LUGAR -> Destino.DETALLE_VIAJE
            else -> Destino.VIAJES
        }
    }

    when (destino) {
        Destino.VIAJES -> ViajesScreen(
            viajes = viajesDeEjemplo,
            onViajeClick = { id ->
                viajeSeleccionadoId = id
                destino = Destino.DETALLE_VIAJE
            },
            onCrearViajeClick = { destino = Destino.CREAR_VIAJE }
        )

        Destino.CREAR_VIAJE -> {
            var nombre by rememberSaveable { mutableStateOf("") }
            var destinoViaje by rememberSaveable { mutableStateOf("") }
            var fechaInicio by rememberSaveable { mutableStateOf("") }
            var fechaFin by rememberSaveable { mutableStateOf("") }
            CrearViajeScreen(
                nombre = nombre,
                destino = destinoViaje,
                fechaInicio = fechaInicio,
                fechaFin = fechaFin,
                onNombreChange = { nombre = it },
                onDestinoChange = { destinoViaje = it },
                onFechaInicioSeleccionada = { fechaInicio = formatearFecha(it) },
                onFechaFinSeleccionada = { fechaFin = formatearFecha(it) },
                onGuardarClick = { destino = Destino.VIAJES },
                onBackClick = { destino = Destino.VIAJES }
            )
        }

        Destino.DETALLE_VIAJE -> {
            val viaje = viajesDeEjemplo.firstOrNull { it.id == viajeSeleccionadoId } ?: viajesDeEjemplo.first()
            DetalleViajeScreen(
                viaje = DetalleViajeUi(
                    id = viaje.id,
                    nombre = viaje.nombre,
                    destino = viaje.destino,
                    fechaInicio = "10 jun 2026",
                    fechaFin = "20 jun 2026"
                ),
                lugares = if (viaje.id == "1") lugaresDeEjemplo else emptyList(),
                onAgregarLugarClick = { destino = Destino.AGREGAR_LUGAR },
                onBackClick = { destino = Destino.VIAJES }
            )
        }

        Destino.AGREGAR_LUGAR -> {
            var nombre by rememberSaveable { mutableStateOf("") }
            var descripcion by rememberSaveable { mutableStateOf("") }
            var ciudad by rememberSaveable { mutableStateOf("") }
            var categoriaId by rememberSaveable { mutableStateOf<Int?>(null) }
            var fechaTentativa by rememberSaveable { mutableStateOf("") }
            AgregarLugarScreen(
                nombre = nombre,
                descripcion = descripcion,
                ciudad = ciudad,
                categorias = categoriasDeEjemplo,
                categoriaSeleccionadaId = categoriaId,
                fechaTentativa = fechaTentativa,
                onNombreChange = { nombre = it },
                onDescripcionChange = { descripcion = it },
                onCiudadChange = { ciudad = it },
                onCategoriaSeleccionada = { categoriaId = it },
                onFechaTentativaSeleccionada = { fechaTentativa = formatearFecha(it) },
                onGuardarClick = { destino = Destino.DETALLE_VIAJE },
                onBackClick = { destino = Destino.DETALLE_VIAJE },
                rangoFechasViaje = "Entre 10 jun 2026 y 20 jun 2026"
            )
        }
    }
}
