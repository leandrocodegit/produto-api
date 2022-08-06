package com.api.prdouto.service

import com.api.prdouto.enuns.CodeError
import com.api.prdouto.exceptions.EntityResponseException
import com.api.prdouto.model.Imagem
import com.api.prdouto.model.Produto
import com.api.prdouto.repository.ImageRepository
import com.api.prdouto.repository.ProdutoRepository
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class ProdutoService(
        private val produtoRepository: ProdutoRepository,
        private val estoqueService: EstoqueService,
        private val imageStoreService: ImageStoreService
) {

    fun findProdutoByCodigo(codigo: String): Produto{
        return produtoRepository.findById(codigo)
                .orElseThrow { throw EntityResponseException("Produto nao encontrado", CodeError.NOT_FOUND)}
    }

    fun listAllProdutos(): List<Produto>{
        return produtoRepository.findAll()
    }

    fun createProduto(produto: Produto): Produto{
        if(produtoRepository.findById(produto.codigo).isPresent)
            throw EntityResponseException("Produto com id ${produto.codigo} ja cadastrado", CodeError.NOT_FOUND)
        return produtoRepository.save(produto)
    }

    fun validaEstoque(codigo: String, quantidade: Int): Boolean{
       var produto = findProdutoByCodigo(codigo)
        if(produto.status.not())
            throw EntityResponseException("Produto com id ${produto.codigo} inativo", CodeError.INACTIVE)
        if(estoqueService.totalEstoque(produto.estoque.id) - produto.estoque.reserva  >= quantidade)
            return true
        return false
    }

    fun vendaProduto(codigo: String, quantidade: Int): Produto{
      var produroDB = findProdutoByCodigo(codigo)
        validaEstoque(codigo, quantidade)
        estoqueService.saveReserva(produroDB.estoque.id, quantidade)
       return produtoRepository.save(produroDB)
    }

    fun saveImagem(codigo: String, file: MultipartFile): Produto{
        var produroDB = findProdutoByCodigo(codigo)
        produroDB.imagens = produroDB.imagens.apply { imageStoreService.saveImage(file) }
        return produroDB
    }



}