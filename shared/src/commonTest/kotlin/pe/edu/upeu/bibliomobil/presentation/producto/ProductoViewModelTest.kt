package pe.edu.upeu.bibliomobil.presentation.producto

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import pe.edu.upeu.bibliomobil.data.repository.FakeProductoRepository
import pe.edu.upeu.bibliomobil.domain.model.Producto
import pe.edu.upeu.bibliomobil.domain.usecase.ListarProductosUseCase
import pe.edu.upeu.bibliomobil.domain.usecase.RegistrarProductoUseCase
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class ProductoViewModelTest {

    @BeforeTest
    fun instalarMain() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @AfterTest
    fun restaurarMain() {
        Dispatchers.resetMain()
    }

    private fun nuevoViewModel(
        repositorio: FakeProductoRepository = FakeProductoRepository()
    ) = ProductoViewModel(
        registrarProducto = RegistrarProductoUseCase(repositorio),
        listarProductos = ListarProductosUseCase(repositorio)
    )

    @Test
    fun repositorioVacioProduceFaseSinProductos() = runTest {

        val viewModel = nuevoViewModel(FakeProductoRepository())

        assertEquals(
            ProductoUiState.Fase.SinProductos,
            viewModel.uiState.value.fase
        )
    }

    @Test
    fun repositorioConProductosProduceFaseConProductos() = runTest {

        val repositorio = FakeProductoRepository(
            mutableListOf(
                Producto(id = 1L, nombre = "Paracetamol", precio = 8.5, stock = 100),
                Producto(id = 2L, nombre = "Ibuprofeno", precio = 12.0, stock = 50),
                Producto(id = 3L, nombre = "Amoxicilina", precio = 18.5, stock = 20)
            )
        )

        val fase = assertIs<ProductoUiState.Fase.ConProductos>(
            nuevoViewModel(repositorio).uiState.value.fase
        )

        assertEquals(3, fase.productos.size)
    }

    @Test
    fun repositorioQueLanzaExcepcionProduceFaseError() = runTest {

        val repositorio = FakeProductoRepository().apply {
            fallaAlListar = IllegalStateException("Sin conexión con el inventario")
        }

        val fase = assertIs<ProductoUiState.Fase.Error>(
            nuevoViewModel(repositorio).uiState.value.fase
        )

        assertEquals("Sin conexión con el inventario", fase.mensaje)
    }

    @Test
    fun precioCeroDejaErrorEnFormularioSinLlamarAlRepositorio() = runTest {

        val repositorio = FakeProductoRepository()
        val viewModel = nuevoViewModel(repositorio)

        viewModel.onNombreChange("Paracetamol")
        viewModel.onPrecioChange("0")
        viewModel.onStockChange("100")
        viewModel.registrar()

        val estado = viewModel.uiState.value

        assertEquals("El precio debe ser mayor que cero", estado.formulario.precioError)
        assertNull(estado.formulario.nombreError)
        assertNull(estado.formulario.stockError)
        assertEquals(0, repositorio.vecesQueSeLlamoRegistrar)
    }
}