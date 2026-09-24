package pe.edu.upeu.andinasalud.di

import org.koin.core.context.startKoin
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import pe.edu.upeu.andinasalud.data.repository.CitaRepositoryFake
import pe.edu.upeu.andinasalud.domain.repository.CitaRepository
import pe.edu.upeu.andinasalud.domain.usecase.CancelarCitaUseCase
import pe.edu.upeu.andinasalud.domain.usecase.ObtenerCitasUseCase
import pe.edu.upeu.andinasalud.domain.usecase.SolicitarCitaUseCase
import pe.edu.upeu.andinasalud.presentation.citas.CitasViewModel
import pe.edu.upeu.andinasalud.presentation.detalle.DetalleCitaViewModel
import pe.edu.upeu.andinasalud.domain.usecase.ObtenerCatalogosUseCase
import pe.edu.upeu.andinasalud.presentation.solicitud.SolicitudViewModel
import pe.edu.upeu.andinasalud.presentation.inicio.InicioViewModel
import pe.edu.upeu.andinasalud.domain.usecase.ContarCitasProgramadasUseCase
import pe.edu.upeu.andinasalud.domain.usecase.ObtenerPacienteUseCase
import pe.edu.upeu.andinasalud.domain.usecase.ReprogramarCitaUseCase

val dataModule = module {
    single<CitaRepository> { CitaRepositoryFake() }
}

val domainModule = module {
    factoryOf(::ObtenerCitasUseCase)
    factoryOf(::SolicitarCitaUseCase)
    factoryOf(::CancelarCitaUseCase)
    factoryOf(::ObtenerCatalogosUseCase)
    factoryOf(::ContarCitasProgramadasUseCase)
    factoryOf(::ObtenerPacienteUseCase)
    factoryOf(::ReprogramarCitaUseCase)
}

val presentationModule = module {
    factoryOf(::CitasViewModel)
    factory { (citaId: Int) -> DetalleCitaViewModel(citaId, get(), get(), get()) }
    factoryOf(::SolicitudViewModel)
    factoryOf(::InicioViewModel)

}

expect val platformModule: org.koin.core.module.Module

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(dataModule, domainModule, presentationModule, platformModule)
    }
}