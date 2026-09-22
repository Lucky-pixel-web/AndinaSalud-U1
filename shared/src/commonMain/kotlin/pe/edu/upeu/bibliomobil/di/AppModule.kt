package pe.edu.upeu.bibliomobil.di

import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import pe.edu.upeu.bibliomobil.data.repository.LectorRepositorioEnMemoria
import pe.edu.upeu.bibliomobil.data.repository.LibroRepositorioEnMemoria
import pe.edu.upeu.bibliomobil.domain.repository.LectorRepository
import pe.edu.upeu.bibliomobil.domain.repository.LibroRepository
import pe.edu.upeu.bibliomobil.domain.usecase.ListarLectoresUseCase
import pe.edu.upeu.bibliomobil.domain.usecase.ListarLibrosUseCase
import pe.edu.upeu.bibliomobil.domain.usecase.RegistrarLectorUseCase
import pe.edu.upeu.bibliomobil.domain.usecase.RegistrarLibroUseCase
import pe.edu.upeu.bibliomobil.presentation.lector.LectorViewModel
import pe.edu.upeu.bibliomobil.presentation.libro.LibroViewModel
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

val dataModule = module {
    single<LibroRepository> { LibroRepositorioEnMemoria() }
    single<LectorRepository> { LectorRepositorioEnMemoria() }
}

val domainModule = module {
    factoryOf(::RegistrarLibroUseCase)
    factoryOf(::ListarLibrosUseCase)
    factoryOf(::RegistrarLectorUseCase)
    factoryOf(::ListarLectoresUseCase)
}

val presentationModule = module {
    factoryOf(::LibroViewModel)
    factoryOf(::LectorViewModel)
}

expect val platformModule: org.koin.core.module.Module

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(dataModule, domainModule, presentationModule, platformModule)
    }
}