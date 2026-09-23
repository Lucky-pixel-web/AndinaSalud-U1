package pe.edu.upeu.andinasalud.presentation.inicio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.edu.upeu.andinasalud.domain.model.EstadoCita
import pe.edu.upeu.andinasalud.domain.usecase.ObtenerCitasUseCase
import pe.edu.upeu.andinasalud.domain.usecase.ObtenerPacienteUseCase
import pe.edu.upeu.andinasalud.presentation.citas.CitaUi
import pe.edu.upeu.andinasalud.presentation.citas.aUi

data class InicioUiState(
    val nombrePaciente: String = "",
    val proximaCita: CitaUi? = null,
    val cargando: Boolean = true
)

class InicioViewModel(
    private val obtenerPaciente: ObtenerPacienteUseCase,
    private val obtenerCitas: ObtenerCitasUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(InicioUiState())
    val uiState: StateFlow<InicioUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val paciente = obtenerPaciente().getOrNull()
            val proxima = obtenerCitas().getOrNull()
                ?.firstOrNull { it.estado is EstadoCita.Programada }
                ?.aUi()
            _uiState.value = InicioUiState(
                nombrePaciente = paciente?.nombre ?: "",
                proximaCita = proxima,
                cargando = false
            )
        }
    }
}