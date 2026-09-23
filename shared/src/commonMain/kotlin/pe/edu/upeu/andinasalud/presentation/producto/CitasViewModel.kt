package pe.edu.upeu.andinasalud.presentation.citas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.edu.upeu.andinasalud.domain.model.EstadoCita
import pe.edu.upeu.andinasalud.domain.usecase.ObtenerCitasUseCase
import pe.edu.upeu.andinasalud.presentation.normalizarParaBusqueda

class CitasViewModel(
    private val obtenerCitas: ObtenerCitasUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CitasUiState())
    val uiState: StateFlow<CitasUiState> = _uiState.asStateFlow()

    init {
        cargarCitas()
    }

    fun cargarCitas() {
        _uiState.value = _uiState.value.copy(fase = FaseCitas.Cargando)
        viewModelScope.launch {
            obtenerCitas().fold(
                onSuccess = { citas ->
                    val citasUi = citas.map { it.aUi() }
                    _uiState.value = _uiState.value.copy(todasLasCitas = citasUi)
                    aplicarFiltros()
                },
                onFailure = {
                    _uiState.value = _uiState.value.copy(
                        fase = FaseCitas.Error("No se pudieron cargar las citas")
                    )
                }
            )
        }
    }

    fun onFiltroEstadoChange(filtro: FiltroEstado) {
        _uiState.value = _uiState.value.copy(filtroEstado = filtro)
        aplicarFiltros()
    }

    fun onBusquedaChange(texto: String) {
        _uiState.value = _uiState.value.copy(textoBusqueda = texto)
        aplicarFiltros()
    }

    /** RF-02 (filtro por estado) + RF-05 (búsqueda por especialidad o médico). */
    private fun aplicarFiltros() {
        val estado = _uiState.value.filtroEstado
        val busqueda = _uiState.value.textoBusqueda.normalizarParaBusqueda()

        val filtradas = _uiState.value.todasLasCitas
            .filter { cita ->
                when (estado) {
                    FiltroEstado.TODAS -> true
                    FiltroEstado.PROGRAMADA -> cita.estado is EstadoCita.Programada
                    FiltroEstado.ATENDIDA -> cita.estado is EstadoCita.Atendida
                    FiltroEstado.CANCELADA -> cita.estado is EstadoCita.Cancelada
                }
            }
            .filter { cita ->
                busqueda.isBlank() ||
                        cita.especialidad.normalizarParaBusqueda().contains(busqueda) ||
                        cita.medico.normalizarParaBusqueda().contains(busqueda)
            }

        _uiState.value = _uiState.value.copy(
            fase = if (filtradas.isEmpty()) FaseCitas.SinCitas else FaseCitas.ConCitas(filtradas)
        )
    }
}