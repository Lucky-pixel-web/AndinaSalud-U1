package pe.edu.upeu.andinasalud.domain.model

sealed class EstadoPrestamo {
    data object Solicitado : EstadoPrestamo()
    data object Entregado : EstadoPrestamo()
    data object Devuelto : EstadoPrestamo()
    data class Vencido(val diasRetraso: Int) : EstadoPrestamo()
}