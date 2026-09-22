package pe.edu.upeu.andinasalud.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import pe.edu.upeu.andinasalud.presentation.lector.LectorViewModel
import pe.edu.upeu.andinasalud.presentation.libro.LibroViewModel
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