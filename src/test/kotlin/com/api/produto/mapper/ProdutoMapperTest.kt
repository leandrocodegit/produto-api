package com.api.produto.mapper

import com.api.produto.build.ProdutoBuild
import com.api.produto.controller.response.CategoriaResponse
import com.api.produto.controller.response.DepositoResponse
import com.api.produto.controller.response.EstoqueResponse
import com.api.produto.controller.response.ImagemResponse
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mapstruct.factory.Mappers

@ExtendWith(MockKExtension::class)
class ProdutoMapperTest() {

    private val mapper = Mappers.getMapper(ProdutoMapper::class.java)

    @Test
    fun `test mapper produto to response`(){

        var produto = ProdutoBuild.produto()
        var produtoResponse = mapper.toResponse(produto)

        assertInstanceOf(CategoriaResponse::class.java, produtoResponse.categoria )
        assertInstanceOf(EstoqueResponse::class.java, produtoResponse.estoque)
        assertInstanceOf(DepositoResponse::class.java, produtoResponse.estoque.depositos.first())
        assertInstanceOf(ImagemResponse::class.java, produtoResponse.imagens?.first() )

        assertTrue(produto.codigo == produtoResponse.codigo)
        assertTrue(produto.categoria.id == produtoResponse.categoria.id)
        assertTrue(produto.estoque.id == produtoResponse.estoque.id)
        assertTrue(produto.estoque.depositos.first().id == produtoResponse.estoque.depositos.first().id)
        assertTrue(produto.imagens?.first()?.id == produtoResponse.imagens?.first()?.id)

    }

    @Test
    fun `test mapper request to entity`(){

        var produtoRequest = ProdutoBuild.createRequest("9500")
        var produtoEntity = mapper.toEntity(produtoRequest)

        assertNotNull( produtoEntity.categoria )
        assertNotNull( produtoEntity.estoque)
        assertNull( produtoEntity.imagens )

        assertTrue(produtoRequest.codigo == produtoEntity.codigo)

    }

    @Test
    fun `test mapper request mesclar entity`(){

        var produtoRequest = ProdutoBuild.createRequest("9500")
        var produto = ProdutoBuild.produto("9500")
        var produtoEntity = mapper.toUpdateProduto(produtoRequest, produto)

        assertNotNull( produtoEntity.categoria )
        assertNotNull( produtoEntity.estoque)
        assertNotNull( produtoEntity.imagens )

        assertTrue(produtoEntity.descricao == produtoRequest.descricao)
        assertTrue(produtoRequest.codigo == produtoEntity.codigo)
        assertTrue(produtoEntity.imagens?.size == 2)

        produtoRequest.descricao = "descrição alterada"
        produtoEntity = mapper.toUpdateProduto(produtoRequest, produto)

        assertTrue(produtoEntity.descricao == produtoRequest.descricao)

    }
}