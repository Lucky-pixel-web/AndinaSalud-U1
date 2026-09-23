package pe.edu.upeu.andinasalud.presentation.solicitud

import pe.edu.upeu.andinasalud.domain.model.ModalidadAtencion
data class FormularioSolicitud(
    val especialidad: String = "",
    val sede: String = "",
    val fecha: String = "",
    val hora: String = "",
    val motivo: String = "",
    val modalidad: ModalidadAtencion? = null,
    val errorEspecialidad: String? = null,
    val errorSede: String? = null,
    val errorFecha: String? = null,
    val errorHora: String? = null,
    val errorMotivo: String? = null,
    val errorModalidad: String? = null
)

data class SolicitudUiState(
    val especialidades: List<String> = emptyList(),
    val sedes: List<String> = emptyList(),
    val formulario: FormularioSolicitud = FormularioSolicitud(),
    val enviando: Boolean = false,
    val exito: Boolean = false
)