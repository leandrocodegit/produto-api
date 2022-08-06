package com.api.prdouto.service

import com.api.prdouto.enuns.CodeError
import com.api.prdouto.exceptions.EntityResponseException
import com.api.prdouto.model.Estoque
import com.api.prdouto.model.Produto
import com.api.prdouto.repository.EstoqueRepository
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