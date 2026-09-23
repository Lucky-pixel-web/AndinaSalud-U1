package pe.edu.upeu.andinasalud.domain.usecase

import pe.edu.upeu.andinasalud.domain.model.Sede
import pe.edu.upeu.andinasalud.domain.repository.CitaRepository

data class Catalogos(val sedes: List<Sede>, val especialidades: List<String>)

class ObtenerCatalogosUseCase(private val citaRepository: CitaRepository) {
    suspend operator fun invoke(): Result<Catalogos> = resultadoDe {
        Catalogos(citaRepository.obtenerSedes(), citaRepository.obtenerEspecialidades())
    }
}