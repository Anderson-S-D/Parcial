package com.example.parcial

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.example.parcial.presentacion.navigation.AppNavGraph
import com.example.parcial.presentacion.viewmodel.CrearViajeViewModel
import com.example.parcial.presentacion.viewmodel.DetalleViajeViewModel
import com.example.parcial.presentacion.viewmodel.RegistrarLugarViewModel
import com.example.parcial.presentacion.viewmodel.ViajesViewModel
import com.example.parcial.ui.theme.ParcialTheme

class MainActivity : ComponentActivity() {

    private val factory by lazy { (application as ParcialApplication).container.viewModelFactory }
    private val viajesViewModel: ViajesViewModel by lazy { ViewModelProvider(this, factory)[ViajesViewModel::class.java] }
    private val crearViajeViewModel: CrearViajeViewModel by lazy { ViewModelProvider(this, factory)[CrearViajeViewModel::class.java] }
    private val detalleViajeViewModel: DetalleViajeViewModel by lazy { ViewModelProvider(this, factory)[DetalleViajeViewModel::class.java] }
    private val registrarLugarViewModel: RegistrarLugarViewModel by lazy { ViewModelProvider(this, factory)[RegistrarLugarViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

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
}
