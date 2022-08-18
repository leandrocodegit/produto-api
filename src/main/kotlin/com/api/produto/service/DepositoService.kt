package com.api.produto.service

import com.api.produto.controller.request.DepositoRequest
import com.api.produto.enuns.CodeError
import com.api.produto.exceptions.EntityResponseException
import com.api.produto.model.Deposito
import com.api.produto.model.Estoque
import com.api.produto.model.Local
import com.api.produto.model.Produto
import com.api.produto.repository.DepositoRespository
import com.api.produto.repository.EstoqueRepository
import com.api.produto.repository.ProdutoRepository
import org.springframework.stereotype.Service

@Service
class DepositoService(
        private val depositoRespository: DepositoRespository,
        private val produtoRepository: ProdutoRepository,
        private val estoqueRepository: EstoqueRepository
) {

    fun findDepositoByID(id: Long, codigoProduto: String) = depositoRespository.findByIdAndProdutoCodigo(
            id,
            codigoProduto).orElseThrow {
        throw EntityResponseException("Deposito nao vinculado ou nao encontrado", CodeError.CONTENT_INVALID)
    }

    private fun findProdutoByCodigo(codigo: String): Produto {
        return produtoRepository.findById(codigo)
                .orElseThrow { throw EntityResponseException("Produto nao encontrado", CodeError.NOT_FOUND) }
    }

    private fun atualizaSaldoEstoque(estoque: Estoque) =
            estoque.apply {
                estoque.estoqueAtual = estoque.depositos.sumOf { result -> result.saldo }
            }
    private fun isValid(produto: Produto, depositoRequest: DepositoRequest): Boolean{
            produto.let {
                it.estoque.depositos.any { result ->
                    result.codigo?.codigo == depositoRequest.codigoProduto
                }.let {
                    if (it.not())
                        throw EntityResponseException("Falha ao atualizar saldo", CodeError.CONTENT_INVALID)
                }
            }
        return true
    }

    fun listAllDepositosByProduto(codigo: String) =
            depositoRespository.findAllByProdutoCodigo(codigo)


    fun criarDeposito(depositoRequest: DepositoRequest) =
            findProdutoByCodigo(depositoRequest.codigoProduto).apply {
                estoque.depositos.add(
                        Deposito(
                                0,
                                depositoRequest.saldo,
                                Local(depositoRequest.local.id),this)
                )
            }.apply {
                atualizaSaldoEstoque(estoque)
            }.let {
                produtoRepository.save(it).estoque
            }

    fun atualizaSaldo(depositoRequest: DepositoRequest): Estoque {

        var deposito = findDepositoByID(depositoRequest.id, depositoRequest.codigoProduto)

                    deposito.apply {
                        saldo = depositoRequest.saldo
                    }.let {
                        depositoRespository.save(it)
                    }.let {
                        it.codigo?.estoque = atualizaSaldoEstoque(it.produto!!.estoque)
                    }
        return estoqueRepository.save(deposito.produto!!.estoque)
    }

    fun atualizaLocal(id: Long, codigoProduto: String,  novoLocal: Local) = findDepositoByID(id,codigoProduto).apply {
        local = novoLocal
    }.let { depositoRespository.save(it) }

    fun deleteDeposito(id: Long, codigoProduto: String) {
        findDepositoByID(id, codigoProduto).let {
            depositoRespository.deleteById(id)
        }
    }
}