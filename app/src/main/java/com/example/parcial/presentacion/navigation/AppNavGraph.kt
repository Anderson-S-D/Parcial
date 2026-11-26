package com.example.parcial.presentacion.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.parcial.presentacion.view.AgregarLugarScreen
import com.example.parcial.presentacion.view.CategoriaUi
import com.example.parcial.presentacion.view.CrearViajeScreen
import com.example.parcial.presentacion.view.DetalleViajeScreen
import com.example.parcial.presentacion.view.DetalleViajeUi
import com.example.parcial.presentacion.view.LugarItemUi
import com.example.parcial.presentacion.view.ViajeItemUi
import com.example.parcial.presentacion.view.ViajesScreen
import com.example.parcial.presentacion.viewmodel.CrearViajeViewModel
import com.example.parcial.presentacion.viewmodel.DetalleViajeViewModel
import com.example.parcial.presentacion.viewmodel.RegistrarLugarViewModel
import com.example.parcial.presentacion.viewmodel.ViajesViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

private enum class Destino { VIAJES, CREAR_VIAJE, DETALLE_VIAJE, AGREGAR_LUGAR }

private fun LocalDate.aTexto(): String =
    DateTimeFormatter.ofPattern("d MMM yyyy", Locale.forLanguageTag("es-CO")).format(this)

private fun Long.aLocalDate(): LocalDate =
    Instant.ofEpochMilli(this).atZone(ZoneOffset.UTC).toLocalDate()

@Composable
fun AppNavGraph(
    viajesViewModel: ViajesViewModel,
    crearViajeViewModel: CrearViajeViewModel,
    detalleViajeViewModel: DetalleViajeViewModel,
    registrarLugarViewModel: RegistrarLugarViewModel
) {
    var destino by rememberSaveable { mutableStateOf(Destino.VIAJES) }
    var viajeSeleccionadoId by rememberSaveable { mutableStateOf<String?>(null) }

    val viajesState by viajesViewModel.uiState.collectAsStateWithLifecycle()
    val crearState by crearViajeViewModel.uiState.collectAsStateWithLifecycle()
    val detalleState by detalleViajeViewModel.uiState.collectAsStateWithLifecycle()
    val registrarState by registrarLugarViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viajeSeleccionadoId) {
        viajeSeleccionadoId?.let { registrarLugarViewModel.preparar(it) }
    }

    LaunchedEffect(crearState.viajeCreado) {
        if (crearState.viajeCreado != null) {
            viajesViewModel.cargarViajes()
            crearViajeViewModel.reset()
            destino = Destino.VIAJES
        }
    }

    LaunchedEffect(registrarState.guardado) {
        if (registrarState.guardado) {
            viajeSeleccionadoId?.let { detalleViajeViewModel.cargar(it) }
            registrarLugarViewModel.reset()
            destino = Destino.DETALLE_VIAJE
        }
    }

    BackHandler(enabled = destino != Destino.VIAJES) {
        destino = when (destino) {
            Destino.AGREGAR_LUGAR -> Destino.DETALLE_VIAJE
            else -> Destino.VIAJES
        }
    }

    when (destino) {
        Destino.VIAJES -> {
            ViajesScreen(
                viajes = viajesState.viajes.map {
                    ViajeItemUi(id = it.id, nombre = it.nombre, destino = it.destino)
                },
                cargando = viajesState.cargando,
                mensajeError = viajesState.error,
                onReintentar = { viajesViewModel.cargarViajes() },
                onViajeClick = { id ->
                    viajeSeleccionadoId = id
                    detalleViajeViewModel.cargar(id)
                    destino = Destino.DETALLE_VIAJE
                },
                onCrearViajeClick = {
                    crearViajeViewModel.reset()
                    destino = Destino.CREAR_VIAJE
                }
            )
        }

        Destino.CREAR_VIAJE -> {
            CrearViajeScreen(
                nombre = crearState.nombre,
                destino = crearState.destino,
                fechaInicio = crearState.fechaInicio?.aTexto().orEmpty(),
                fechaFin = crearState.fechaFin?.aTexto().orEmpty(),
                onNombreChange = crearViajeViewModel::onNombreChange,
                onDestinoChange = crearViajeViewModel::onDestinoChange,
                onFechaInicioSeleccionada = { crearViajeViewModel.onFechaInicioChange(it.aLocalDate()) },
                onFechaFinSeleccionada = { crearViajeViewModel.onFechaFinChange(it.aLocalDate()) },
                onGuardarClick = { crearViajeViewModel.crearViaje() },
                onBackClick = { destino = Destino.VIAJES },
                mensajeError = crearState.error,
                guardando = crearState.guardando
            )
        }

        Destino.DETALLE_VIAJE -> {
            val viaje = detalleState.viaje
            if (viaje == null) {
                EstadoCarga(
                    cargando = detalleState.cargando,
                    error = detalleState.error,
                    textoVacio = "No se pudo cargar el viaje",
                    onReintentar = { viajeSeleccionadoId?.let { detalleViajeViewModel.cargar(it) } }
                )
            } else {
                DetalleViajeScreen(
                    viaje = DetalleViajeUi(
                        id = viaje.id,
                        nombre = viaje.nombre,
                        destino = viaje.destino,
                        fechaInicio = viaje.fechaInicio.aTexto(),
                        fechaFin = viaje.fechaFin.aTexto()
                    ),
                    lugares = detalleState.lugares.map {
                        LugarItemUi(
                            id = it.id,
                            nombre = it.nombre,
                            fechaTentativa = it.fechaTentativa.aTexto()
                        )
                    },
                    onAgregarLugarClick = { id ->
                        viajeSeleccionadoId = id
                        registrarLugarViewModel.reset()
                        registrarLugarViewModel.preparar(id)
                        destino = Destino.AGREGAR_LUGAR
                    },
                    onBackClick = { destino = Destino.VIAJES }
                )
            }
        }

        Destino.AGREGAR_LUGAR -> {
            val id = viajeSeleccionadoId
            if (id == null) {
                LaunchedEffect(Unit) { destino = Destino.VIAJES }
            } else {
                AgregarLugarScreen(
                    nombre = registrarState.nombre,
                    descripcion = registrarState.descripcion,
                    ciudad = registrarState.ciudad,
                    categorias = registrarState.categorias.map {
                        CategoriaUi(id = it.id, nombre = it.nombre)
                    },
                    categoriaSeleccionadaId = registrarState.categoriaId,
                    fechaTentativa = registrarState.fechaTentativa?.aTexto().orEmpty(),
                    onNombreChange = registrarLugarViewModel::onNombreChange,
                    onDescripcionChange = registrarLugarViewModel::onDescripcionChange,
                    onCiudadChange = registrarLugarViewModel::onCiudadChange,
                    onCategoriaSeleccionada = registrarLugarViewModel::onCategoriaChange,
                    onFechaTentativaSeleccionada = {
                        registrarLugarViewModel.onFechaTentativaChange(it.aLocalDate())
                    },
                    onGuardarClick = { registrarLugarViewModel.guardar() },
                    onBackClick = { destino = Destino.DETALLE_VIAJE },
                    rangoFechasViaje = detalleState.viaje?.let {
                        "Entre ${it.fechaInicio.aTexto()} y ${it.fechaFin.aTexto()}"
                    },
                    mensajeError = registrarState.error,
                    guardando = registrarState.guardando
                )
            }
        }
    }
}

@Composable
private fun EstadoCarga(
    cargando: Boolean,
    error: String?,
    textoVacio: String,
    onReintentar: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            when {
                cargando -> CircularProgressIndicator()
                error != null -> {
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                    Button(onClick = onReintentar) { Text("Reintentar") }
                }
                else -> Text(text = textoVacio, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
