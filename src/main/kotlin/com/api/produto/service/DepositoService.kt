package com.api.produto.service

import com.api.produto.controller.request.DepositoLocalRequest
import com.api.produto.controller.request.DepositoRequest
import com.api.produto.enuns.CodeError
import com.api.produto.exceptions.EntityResponseException
import com.api.produto.model.Deposito
import com.api.produto.model.Estoque
import com.api.produto.model.Local
import com.api.produto.model.Produto
import com.api.produto.repository.DepositoRespository
import com.api.produto.repository.EstoqueRepository
import com.api.produto.repository.LocalRepository
import com.api.produto.repository.ProdutoRepository
import org.springframework.stereotype.Service

@Service
class DepositoService(
        private val depositoRespository: DepositoRespository,
        private val produtoRepository: ProdutoRepository,
        private val estoqueRepository: EstoqueRepository,
        private val localRepository: LocalRepository
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

    private fun atualizaSaldoEstoque(codigo: String) =
            findProdutoByCodigo(codigo).estoque.apply {
                estoqueAtual = depositos.sumOf { result -> result.saldo }
            }.let {
                estoqueRepository.save(it)
            }

    private fun isValid(produto: Produto, depositoRequest: DepositoRequest): Boolean {
        produto.let {
            it.estoque.depositos.any { result ->
                result.produtoCodigo == depositoRequest.codigoProduto
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
                                Local(depositoRequest.local.id), this.codigo)
                )
            }.let {
                atualizaSaldoEstoque(it.codigo)
            }

    fun atualizaSaldo(depositoRequest: DepositoRequest): Estoque {

        var deposito = findDepositoByID(depositoRequest.id, depositoRequest.codigoProduto)
        var produto = findProdutoByCodigo(depositoRequest.codigoProduto)

        deposito.apply {
            saldo = depositoRequest.saldo
        }.let {
            depositoRespository.save(it)
        }
        return atualizaSaldoEstoque(depositoRequest.codigoProduto)

    }

    fun atualizaLocal(dlr: DepositoLocalRequest) = findDepositoByID(dlr.id, dlr.codigoProduto).apply {
        local = localRepository.findById(dlr.localID).orElseThrow{
            throw EntityResponseException("Local com id ${dlr.localID} nao encontrado", CodeError.NOT_FOUND)
        }
    }.let { depositoRespository.save(it) }

    fun deleteDeposito(id: Long, codigoProduto: String) : Estoque {
       if(listAllDepositosByProduto(codigoProduto).size <= 1)
           throw EntityResponseException("Nao e possivel excluir todos os depositos", CodeError.CONTENT_EMPTY)
        findDepositoByID(id, codigoProduto).let {
            depositoRespository.deleteById(id)
        }.let {
           return atualizaSaldoEstoque(codigoProduto)
        }
    }
}