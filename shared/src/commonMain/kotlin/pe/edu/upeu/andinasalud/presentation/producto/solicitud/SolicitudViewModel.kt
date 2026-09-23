package pe.edu.upeu.andinasalud.presentation.solicitud

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.edu.upeu.andinasalud.domain.model.ModalidadAtencion
import pe.edu.upeu.andinasalud.domain.usecase.ObtenerCatalogosUseCase
import pe.edu.upeu.andinasalud.domain.usecase.SolicitarCitaUseCase
import pe.edu.upeu.andinasalud.domain.usecase.SolicitudCitaInvalidaException

class SolicitudViewModel(
    private val obtenerCatalogos: ObtenerCatalogosUseCase,
    private val solicitarCita: SolicitarCitaUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SolicitudUiState())
    val uiState: StateFlow<SolicitudUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            obtenerCatalogos().onSuccess { catalogos ->
                _uiState.value = _uiState.value.copy(
                    especialidades = catalogos.especialidades,
                    sedes = catalogos.sedes.map { it.nombre }
                )
            }
        }
    }

    fun onEspecialidadChange(v: String) = actualizar { it.copy(especialidad = v, errorEspecialidad = null) }
    fun onSedeChange(v: String) = actualizar { it.copy(sede = v, errorSede = null) }
    fun onFechaChange(v: String) = actualizar { it.copy(fecha = v, errorFecha = null) }
    fun onHoraChange(v: String) = actualizar { it.copy(hora = v, errorHora = null) }
    fun onMotivoChange(v: String) = actualizar { it.copy(motivo = v, errorMotivo = null) }

    fun onModalidadChange(v: ModalidadAtencion) = actualizar { it.copy(modalidad = v, errorModalidad = null) }

    private fun actualizar(cambio: (FormularioSolicitud) -> FormularioSolicitud) {
        _uiState.value = _uiState.value.copy(formulario = cambio(_uiState.value.formulario))
    }

    fun enviar() {
        if (_uiState.value.enviando) return
        _uiState.value = _uiState.value.copy(enviando = true)
        viewModelScope.launch {
            val f = _uiState.value.formulario
            solicitarCita(f.especialidad, f.sede, f.fecha, f.hora, f.motivo, f.modalidad).fold(
                onSuccess = { _uiState.value = _uiState.value.copy(enviando = false, exito = true, formulario = FormularioSolicitud()) },
                onFailure = { error ->
                    val errores = (error as? SolicitudCitaInvalidaException)?.errores
                    _uiState.value = _uiState.value.copy(
                        enviando = false,
                        formulario = f.copy(
                            errorEspecialidad = errores?.especialidad, errorSede = errores?.sede,
                            errorFecha = errores?.fecha, errorHora = errores?.hora,
                            errorMotivo = errores?.motivo, errorModalidad = errores?.modalidad
                        )
                    )
                }
            )
        }
    }

    fun onExitoMostrado() {
        _uiState.value = _uiState.value.copy(exito = false)
    }
}