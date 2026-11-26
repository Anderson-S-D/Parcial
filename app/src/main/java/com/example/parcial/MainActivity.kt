package com.example.parcial

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.example.parcial.presentacion.navigation.AppNavGraph
import com.example.parcial.presentacion.viewmodel.CrearViajeViewModel
import com.example.parcial.presentacion.viewmodel.DetalleViajeViewModel
import com.example.parcial.presentacion.viewmodel.RegistrarLugarViewModel
import com.example.parcial.presentacion.viewmodel.ViajesViewModel
import com.example.parcial.ui.theme.ParcialTheme

class MainActivity : ComponentActivity() {

    private val factory by lazy { (application as ParcialApplication).container.viewModelFactory }
    private val viajesViewModel: ViajesViewModel by viewModels { factory }
    private val crearViajeViewModel: CrearViajeViewModel by viewModels { factory }
    private val detalleViajeViewModel: DetalleViajeViewModel by viewModels { factory }
    private val registrarLugarViewModel: RegistrarLugarViewModel by viewModels { factory }

    private val solicitudRedLocal = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        viajesViewModel.cargarViajes()
        registrarLugarViewModel.cargarCategorias()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        solicitarPermisoRedLocal()

        setContent {
            ParcialTheme {
                AppNavGraph(
                    viajesViewModel = viajesViewModel,
                    crearViajeViewModel = crearViajeViewModel,
                    detalleViajeViewModel = detalleViajeViewModel,
                    registrarLugarViewModel = registrarLugarViewModel
                )
            }
        }
    }

    private fun solicitarPermisoRedLocal() {
        val requierePermiso = Build.VERSION.SDK_INT >= VERSION_PERMISO_RED_LOCAL
        val concedido = ContextCompat.checkSelfPermission(this, PERMISO_RED_LOCAL) ==
            PackageManager.PERMISSION_GRANTED
        if (requierePermiso && !concedido) {
            solicitudRedLocal.launch(PERMISO_RED_LOCAL)
        }
    }

    private companion object {
        const val PERMISO_RED_LOCAL = "android.permission.ACCESS_LOCAL_NETWORK"
        const val VERSION_PERMISO_RED_LOCAL = 37
    }
}
