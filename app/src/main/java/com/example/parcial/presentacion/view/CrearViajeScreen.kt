package com.example.parcial.presentacion.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.parcial.ui.theme.ParcialTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearViajeScreen(
    nombre: String,
    destino: String,
    fechaInicio: String,
    fechaFin: String,
    onNombreChange: (String) -> Unit,
    onDestinoChange: (String) -> Unit,
    onFechaInicioSeleccionada: (Long) -> Unit,
    onFechaFinSeleccionada: (Long) -> Unit,
    onGuardarClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    nombreError: String? = null,
    destinoError: String? = null,
    fechaInicioError: String? = null,
    fechaFinError: String? = null,
    mensajeError: String? = null,
    guardando: Boolean = false
) {
    var mostrarCalendarioInicio by rememberSaveable { mutableStateOf(false) }
    var mostrarCalendarioFin by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = "Crear viaje", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .imePadding()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                BotonGuardar(
                    onClick = onGuardarClick,
                    guardando = guardando,
                    modifier = Modifier
                        .widthIn(max = 600.dp)
                        .fillMaxWidth()
                )
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
            Column(
                modifier = Modifier
                    .widthIn(max = 600.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column {
                    EtiquetaCampo(texto = "Nombre del viaje")
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = onNombreChange,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Ej. Europa 2026") },
                        singleLine = true,
                        isError = nombreError != null,
                        supportingText = nombreError?.let { { Text(it) } },
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences,
                            imeAction = ImeAction.Next
                        ),
                        shape = MaterialTheme.shapes.small
                    )
                }

                Column {
                    EtiquetaCampo(texto = "Destino")
                    OutlinedTextField(
                        value = destino,
                        onValueChange = onDestinoChange,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Ej. Italia") },
                        singleLine = true,
                        isError = destinoError != null,
                        supportingText = destinoError?.let { { Text(it) } },
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences,
                            imeAction = ImeAction.Done
                        ),
                        shape = MaterialTheme.shapes.small
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Column(modifier = Modifier.weight(1f)) {
                        EtiquetaCampo(texto = "Fecha de inicio")
                        CampoFecha(
                            valor = fechaInicio,
                            error = fechaInicioError,
                            descripcion = "Seleccionar fecha de inicio",
                            onClick = { mostrarCalendarioInicio = true }
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        EtiquetaCampo(texto = "Fecha de fin")
                        CampoFecha(
                            valor = fechaFin,
                            error = fechaFinError,
                            descripcion = "Seleccionar fecha de fin",
                            onClick = { mostrarCalendarioFin = true }
                        )
                    }
                }

                if (mensajeError != null) {
                    Text(
                        text = mensajeError,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }

    if (mostrarCalendarioInicio) {
        SelectorFechaDialog(
            onFechaSeleccionada = onFechaInicioSeleccionada,
            onDismiss = { mostrarCalendarioInicio = false }
        )
    }
    if (mostrarCalendarioFin) {
        SelectorFechaDialog(
            onFechaSeleccionada = onFechaFinSeleccionada,
            onDismiss = { mostrarCalendarioFin = false }
        )
    }
}

@Composable
private fun EtiquetaCampo(texto: String, obligatorio: Boolean = true) {
    Text(
        text = buildAnnotatedString {
            append(texto)
            if (obligatorio) {
                withStyle(SpanStyle(color = MaterialTheme.colorScheme.error)) { append(" *") }
            }
        },
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(bottom = 6.dp)
    )
}

@Composable
private fun CampoFecha(
    valor: String,
    error: String?,
    descripcion: String,
    onClick: () -> Unit
) {
    Box {
        OutlinedTextField(
            value = valor,
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            singleLine = true,
            placeholder = { Text("dd mmm aaaa") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.CalendarMonth,
                    contentDescription = descripcion,
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            isError = error != null,
            supportingText = error?.let { { Text(it) } },
            shape = MaterialTheme.shapes.small
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable(onClick = onClick)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectorFechaDialog(
    onFechaSeleccionada: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    val estadoCalendario = rememberDatePickerState()
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    estadoCalendario.selectedDateMillis?.let(onFechaSeleccionada)
                    onDismiss()
                },
                enabled = estadoCalendario.selectedDateMillis != null
            ) { Text("Aceptar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    ) {
        DatePicker(state = estadoCalendario)
    }
}

@Composable
private fun BotonGuardar(
    onClick: () -> Unit,
    guardando: Boolean,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        enabled = !guardando,
        modifier = modifier.height(52.dp),
        shape = MaterialTheme.shapes.small
    ) {
        if (guardando) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                strokeWidth = 2.dp,
                color = MaterialTheme.colorScheme.onPrimary
            )
        } else {
            Icon(imageVector = Icons.Outlined.Save, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Guardar", fontWeight = FontWeight.SemiBold)
        }
    }
}

@Preview(name = "Crear viaje", showBackground = true, showSystemUi = true)
@Composable
private fun CrearViajeScreenPreview() {
    ParcialTheme {
        CrearViajeScreen(
            nombre = "Europa 2026",
            destino = "Italia",
            fechaInicio = "10 jun 2026",
            fechaFin = "20 jun 2026",
            onNombreChange = {},
            onDestinoChange = {},
            onFechaInicioSeleccionada = {},
            onFechaFinSeleccionada = {},
            onGuardarClick = {},
            onBackClick = {}
        )
    }
}

@Preview(name = "Crear viaje - con errores", showBackground = true)
@Composable
private fun CrearViajeScreenErroresPreview() {
    ParcialTheme {
        CrearViajeScreen(
            nombre = "",
            destino = "Italia",
            fechaInicio = "20 jun 2026",
            fechaFin = "10 jun 2026",
            onNombreChange = {},
            onDestinoChange = {},
            onFechaInicioSeleccionada = {},
            onFechaFinSeleccionada = {},
            onGuardarClick = {},
            onBackClick = {},
            nombreError = "El nombre es obligatorio",
            fechaFinError = "Debe ser posterior al inicio"
        )
    }
}
