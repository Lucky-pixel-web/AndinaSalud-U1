package pe.edu.upeu.bibliomobil.domain.usecase

import pe.edu.upeu.bibliomobil.domain.model.Cliente
import pe.edu.upeu.bibliomobil.domain.repository.ClienteRepository

class ListarClientesUseCase(
    private val clienteRepository: ClienteRepository
) {

    suspend operator fun invoke(): Result<List<Cliente>> = resultadoDe {
        clienteRepository.listar()
    }
}