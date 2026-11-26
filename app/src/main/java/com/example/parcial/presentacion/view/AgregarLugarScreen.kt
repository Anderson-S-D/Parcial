package com.example.parcial.presentacion.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.parcial.ui.theme.ParcialTheme

data class CategoriaUi(
    val id: Int,
    val nombre: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgregarLugarScreen(
    nombre: String,
    descripcion: String,
    ciudad: String,
    categorias: List<CategoriaUi>,
    categoriaSeleccionadaId: Int?,
    fechaTentativa: String,
    onNombreChange: (String) -> Unit,
    onDescripcionChange: (String) -> Unit,
    onCiudadChange: (String) -> Unit,
    onCategoriaSeleccionada: (Int) -> Unit,
    onFechaTentativaSeleccionada: (Long) -> Unit,
    onGuardarClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    rangoFechasViaje: String? = null,
    nombreError: String? = null,
    descripcionError: String? = null,
    ciudadError: String? = null,
    categoriaError: String? = null,
    fechaTentativaError: String? = null,
    mensajeError: String? = null,
    guardando: Boolean = false
) {
    var mostrarCalendario by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = "Agregar lugar", fontWeight = FontWeight.Bold) },
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
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Column {
                    EtiquetaCampo(texto = "Nombre del lugar")
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = onNombreChange,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Ej. Coliseo Romano") },
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
                    EtiquetaCampo(texto = "Descripción")
                    OutlinedTextField(
                        value = descripcion,
                        onValueChange = onDescripcionChange,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("¿Qué lo hace especial?") },
                        minLines = 3,
                        maxLines = 5,
                        isError = descripcionError != null,
                        supportingText = descripcionError?.let { { Text(it) } },
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences
                        ),
                        shape = MaterialTheme.shapes.small
                    )
                }

                Column {
                    EtiquetaCampo(texto = "Ciudad")
                    OutlinedTextField(
                        value = ciudad,
                        onValueChange = onCiudadChange,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Ej. Roma") },
                        singleLine = true,
                        isError = ciudadError != null,
                        supportingText = ciudadError?.let { { Text(it) } },
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Words,
                            imeAction = ImeAction.Done
                        ),
                        shape = MaterialTheme.shapes.small
                    )
                }

                Column {
                    EtiquetaCampo(texto = "Categoría")
                    SelectorCategoria(
                        categorias = categorias,
                        categoriaSeleccionadaId = categoriaSeleccionadaId,
                        error = categoriaError,
                        onCategoriaSeleccionada = onCategoriaSeleccionada
                    )
                }

                Column {
                    EtiquetaCampo(texto = "Fecha tentativa")
                    CampoFecha(
                        valor = fechaTentativa,
                        error = fechaTentativaError,
                        ayuda = rangoFechasViaje,
                        descripcion = "Seleccionar fecha tentativa",
                        onClick = { mostrarCalendario = true }
                    )
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

    if (mostrarCalendario) {
        SelectorFechaDialog(
            onFechaSeleccionada = onFechaTentativaSeleccionada,
            onDismiss = { mostrarCalendario = false }
        )
    }
}

@Composable
private fun SelectorCategoria(
    categorias: List<CategoriaUi>,
    categoriaSeleccionadaId: Int?,
    error: String?,
    onCategoriaSeleccionada: (Int) -> Unit
) {
    var expandido by remember { mutableStateOf(false) }
    var anchoCampoPx by remember { mutableIntStateOf(0) }
    val densidad = LocalDensity.current
    val seleccionada = categorias.firstOrNull { it.id == categoriaSeleccionadaId }

    Box {
        OutlinedTextField(
            value = seleccionada?.nombre.orEmpty(),
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .onSizeChanged { anchoCampoPx = it.width },
            readOnly = true,
            singleLine = true,
            placeholder = { Text("Selecciona una categoría") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.LocalOffer,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = if (expandido) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                    contentDescription = if (expandido) "Cerrar categorías" else "Abrir categorías"
                )
            },
            isError = error != null,
            supportingText = error?.let { { Text(it) } },
            shape = MaterialTheme.shapes.small
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable(enabled = categorias.isNotEmpty()) { expandido = true }
        )
        DropdownMenu(
            expanded = expandido,
            onDismissRequest = { expandido = false },
            modifier = Modifier.width(with(densidad) { anchoCampoPx.toDp() })
        ) {
            categorias.forEach { categoria ->
                DropdownMenuItem(
                    text = { Text(categoria.nombre) },
                    onClick = {
                        onCategoriaSeleccionada(categoria.id)
                        expandido = false
                    }
                )
            }
        }
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
    ayuda: String?,
    descripcion: String,
    onClick: () -> Unit
) {
    val textoInferior = error ?: ayuda
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
            supportingText = textoInferior?.let { { Text(it) } },
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

private val categoriasDeEjemplo = listOf(
    CategoriaUi(id = 1, nombre = "Histórico"),
    CategoriaUi(id = 2, nombre = "Cultural"),
    CategoriaUi(id = 3, nombre = "Natural"),
    CategoriaUi(id = 4, nombre = "Religioso"),
    CategoriaUi(id = 5, nombre = "Gastronomía"),
    CategoriaUi(id = 6, nombre = "Aventura"),
    CategoriaUi(id = 7, nombre = "Otro")
)

@Preview(name = "Agregar lugar", showBackground = true, showSystemUi = true)
@Composable
private fun AgregarLugarScreenPreview() {
    ParcialTheme {
        AgregarLugarScreen(
            nombre = "Coliseo Romano",
            descripcion = "Anfiteatro romano, uno de los principales atractivos de Roma.",
            ciudad = "Roma",
            categorias = categoriasDeEjemplo,
            categoriaSeleccionadaId = 1,
            fechaTentativa = "12 jun 2026",
            onNombreChange = {},
            onDescripcionChange = {},
            onCiudadChange = {},
            onCategoriaSeleccionada = {},
            onFechaTentativaSeleccionada = {},
            onGuardarClick = {},
            onBackClick = {},
            rangoFechasViaje = "Entre 10 jun 2026 y 20 jun 2026"
        )
    }
}

@Preview(name = "Agregar lugar - con errores", showBackground = true)
@Composable
private fun AgregarLugarScreenErroresPreview() {
    ParcialTheme {
        AgregarLugarScreen(
            nombre = "",
            descripcion = "",
            ciudad = "Roma",
            categorias = categoriasDeEjemplo,
            categoriaSeleccionadaId = null,
            fechaTentativa = "25 jun 2026",
            onNombreChange = {},
            onDescripcionChange = {},
            onCiudadChange = {},
            onCategoriaSeleccionada = {},
            onFechaTentativaSeleccionada = {},
            onGuardarClick = {},
            onBackClick = {},
            nombreError = "El nombre es obligatorio",
            descripcionError = "La descripción es obligatoria",
            categoriaError = "Selecciona una categoría",
            fechaTentativaError = "Debe estar entre 10 jun 2026 y 20 jun 2026"
        )
    }
}
