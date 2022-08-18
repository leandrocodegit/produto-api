package com.api.produto.service

import com.api.produto.controller.request.ProdutoRequest
import com.api.produto.enuns.CodeError
import com.api.produto.exceptions.EntityResponseException
import com.api.produto.file.ImageFile
import com.api.produto.file.RenderImagem
import com.api.produto.mapper.ProdutoMapper
import com.api.produto.model.Deposito
import com.api.produto.model.Estoque
import com.api.produto.model.Local
import com.api.produto.model.image.Imagem
import com.api.produto.model.Produto
import com.api.produto.model.image.ImageContentProfile
import com.api.produto.repository.ImageStore
import com.api.produto.repository.LocalRepository
import com.api.produto.repository.ProdutoRepository
import org.mapstruct.factory.Mappers
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
class ProdutoService(
        private val produtoRepository: ProdutoRepository
) {
    @Autowired
    private lateinit var estoqueService: EstoqueService
    @Autowired
    private lateinit var mapper: ProdutoMapper

    fun findProdutoByCodigo(codigo: String): Produto{
        return produtoRepository.findById(codigo)
                .orElseThrow { throw EntityResponseException("Produto nao encontrado", CodeError.NOT_FOUND)}
    }

    fun listAllProdutos(): List<Produto>{
        return produtoRepository.findAll()
    }

    fun createProduto(produto: Produto): Produto{
        if(produtoRepository.findById(produto.codigo).isPresent)
            throw EntityResponseException("Produto com id ${produto.codigo} ja cadastrado", CodeError.DUPLICATE)
        produto.estoque = Estoque(
                mutableListOf(Deposito( 0, Local(1L),produto)))
        return produtoRepository.save(produto)
    }

    fun validaEstoque(codigo: String, quantidade: Int): Boolean{
       var produto = findProdutoByCodigo(codigo)
        if(produto.status.not())
            throw EntityResponseException("Produto com id ${produto.codigo} inativo", CodeError.INACTIVE)
        if(estoqueService.totalEstoque(produto.estoque!!.id) - produto.estoque!!.reserva  >= quantidade)
            return true
        return false
    }

    fun vendaProduto(codigo: String, quantidade: Int): Produto{
      var produroDB = findProdutoByCodigo(codigo)
        validaEstoque(codigo, quantidade)
        estoqueService.saveReserva(produroDB.estoque!!.id, quantidade)
       return produtoRepository.save(produroDB)
    }

    fun deleteProduto(codigo: String){
        val produto = findProdutoByCodigo(codigo)
        produtoRepository.delete(produto)
    }

    fun updateProduto(produtoRequest: ProdutoRequest): Produto{
        if(produtoRequest.codigo == null || produtoRequest.codigo.isEmpty())
            throw EntityResponseException("Campo id é obrigatório!", CodeError.FORMAT_INVALID)
         val produtoDB = findProdutoByCodigo(produtoRequest.codigo)
       return produtoRepository.save(mapper.toUpdateProduto(produtoRequest, produtoDB))
    }

    fun updateProduto(produto: Produto): Produto{
        if(produto.codigo == null || produto.codigo.isEmpty())
            throw EntityResponseException("Campo id é obrigatório!", CodeError.FORMAT_INVALID)
        val produtoDB = findProdutoByCodigo(produto.codigo)
        return produtoRepository.save(produto)
    }

    fun mudaStatus(codigo: String): Produto{
        var produtoDB = findProdutoByCodigo(codigo)
        produtoDB.status = produtoDB.status.not()
       return produtoRepository.save(produtoDB)
    }

}