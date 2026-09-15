package pe.edu.upeu.bibliomobil.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import pe.edu.upeu.bibliomobil.data.repository.ClienteRepositorioEnMemoria
import pe.edu.upeu.bibliomobil.data.repository.ProductoRepositorioEnMemoria
import pe.edu.upeu.bibliomobil.domain.repository.ClienteRepository
import pe.edu.upeu.bibliomobil.domain.repository.ProductoRepository
import pe.edu.upeu.bibliomobil.domain.usecase.ListarClientesUseCase
import pe.edu.upeu.bibliomobil.domain.usecase.ListarProductosUseCase
import pe.edu.upeu.bibliomobil.domain.usecase.RegistrarClienteUseCase
import pe.edu.upeu.bibliomobil.domain.usecase.RegistrarProductoUseCase
import pe.edu.upeu.bibliomobil.presentation.cliente.ClienteViewModel
import pe.edu.upeu.bibliomobil.presentation.producto.ProductoViewModel


val dataModule = module {
    single<ProductoRepository> { ProductoRepositorioEnMemoria() }
    single<ClienteRepository> { ClienteRepositorioEnMemoria() }
}

val domainModule = module {
    factory { RegistrarProductoUseCase(get()) }
    factory { ListarProductosUseCase(get()) }
    factory { RegistrarClienteUseCase(get()) }
    factory { ListarClientesUseCase(get()) }
}

val presentationModule = module {
    viewModel { ProductoViewModel(get(), get()) }
    viewModel { ClienteViewModel(get(), get()) }
}


expect val platformModule: Module

fun initKoin(configuracionAdicional: KoinApplication.() -> Unit = {}) {
    startKoin {
        configuracionAdicional()
        modules(
            dataModule,
            domainModule,
            presentationModule,
            platformModule
        )
    }
}