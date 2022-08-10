package com.api.produto.service

import com.api.produto.enuns.CodeError
import com.api.produto.exceptions.EntityResponseException
import com.api.produto.model.Estoque
import com.api.produto.repository.EstoqueRepository
import org.springframework.stereotype.Service
import kotlin.streams.toList

@Service
class EstoqueService(
        private val estoqueRepository: EstoqueRepository
) {
    fun findEstoqueById(id: Long): Estoque{
        return estoqueRepository.findById(id)
                .orElseThrow{throw EntityResponseException("Estoque nao encontrado", CodeError.NOT_FOUND)}
    }

    fun totalEstoque(id: Long): Int{
        var estoque = findEstoqueById(id)
        return estoque.depositos.stream().map { it.saldo }.toList().sum()
    }

    fun saveReserva(id: Long, quantidade: Int): Estoque{
        var estoque = findEstoqueById(id)
        if(totalEstoque(id) < quantidade)
            throw EntityResponseException("Saldo do estoque insuficiente para reserva", CodeError.SALDO_ERROR)
        estoque.reserva = estoque.reserva + quantidade
        estoque.estoqueAtual = totalEstoque(id) - estoque.reserva
        return estoqueRepository.save(estoque)
    }

}