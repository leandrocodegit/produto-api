package com.api.produto.service

import com.api.produto.build.ProdutoBuild
import com.api.produto.exceptions.EntityResponseException
import com.api.produto.mapper.ProdutoMapper
import com.api.produto.model.Deposito
import com.api.produto.model.Local
import com.api.produto.model.image.Imagem
import com.api.produto.repository.EstoqueRepository
import com.api.produto.repository.ImageRepository
import com.api.produto.repository.ImageStore
import com.api.produto.repository.ProdutoRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.apache.tomcat.jni.File
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.mock.web.MockMultipartFile
import org.springframework.web.multipart.MultipartFile
import java.util.*
import java.io.*


@ExtendWith(MockKExtension::class)
class ProdutoServiceTest {

    @InjectMockKs
    private lateinit var produtoService: ProdutoService
    @InjectMockKs
    private lateinit var estoqueService: EstoqueService
    @MockK
    private lateinit var produtoRepository: ProdutoRepository
    @MockK
    private lateinit var estoqueRepository: EstoqueRepository
    @MockK
    private lateinit var mapper: ProdutoMapper
    @BeforeEach
    fun setup(){

    }

    @Test
    fun `test create new produto`(){

        val produto = ProdutoBuild.produto("7050")
        every { produtoRepository.findById(produto.codigo).isPresent } returns false
        every { produtoService.createProduto(produto) } returns produto
        val produtoSave = produtoService.createProduto(produto)

        assertEquals(produto.codigo, produtoSave.codigo)
        assertEquals(produtoSave.estoque.depositos.size, 1)
        assertNotNull(produtoSave.estoque.depositos)
        assertEquals(produtoSave.estoque.depositos.first().produto?.codigo, produto.codigo)

    }

    @Test
    fun `test create new produto allread`(){

        val produto = ProdutoBuild.produto()

        every { produtoRepository.findById(produto.codigo).isPresent } returns true
        every { produtoRepository.findById(produto.codigo) } returns Optional.of(produto)
        every { produtoService.createProduto(produto) } returns produto

       assertThrows<EntityResponseException> { produtoService.createProduto(produto)  }
    }

    @Test
    fun `test list all produto`(){

        val produto = ProdutoBuild.produto()
        every { produtoRepository.findAll() } returns listOf(ProdutoBuild.produto("9001"),ProdutoBuild.produto("9002"))
        every { produtoService.listAllProdutos() } returns listOf(ProdutoBuild.produto("9001"),ProdutoBuild.produto("9002"))

       assert(produtoService.listAllProdutos().size == 2)
       assert(produtoService.listAllProdutos().first().codigo == "9001")
       assert(produtoService.listAllProdutos().last().codigo == "9002")

    }

    @Test
    fun `test valida estoque suficiente`(){

        val produto = ProdutoBuild.produto()
        every { produtoRepository.findById(produto.codigo) } returns Optional.of(produto)
        every { estoqueRepository.findById(produto.estoque.id) } returns Optional.of(produto.estoque)
        every { produtoService.findProdutoByCodigo(produto.codigo) } returns  produto

        assert(produtoService.validaEstoque(produto.codigo,90))
    }

    @Test
    fun `test valida estoque insuficiente`(){

        val produto = ProdutoBuild.produto()
        every { produtoRepository.findById(produto.codigo) } returns Optional.of(produto)
        every { estoqueRepository.findById(produto.estoque.id) } returns Optional.of(produto.estoque)
        every { produtoService.findProdutoByCodigo(produto.codigo) } returns  produto

        assertFalse(produtoService.validaEstoque(produto.codigo,101))
    }

    @Test
    fun `test valida estoque produto inexistente`(){

        val produto = ProdutoBuild.produto()
        every { produtoRepository.findById(produto.codigo) } returns Optional.empty()
        assertThrows<EntityResponseException> { produtoService.validaEstoque(produto.codigo, 100)  }
    }
    @Test
    fun `test valida estoque produto inativo`(){

        val produto = ProdutoBuild.produto(false)
        every { produtoRepository.findById(produto.codigo) } returns Optional.of(produto)
        assertThrows<EntityResponseException> { produtoService.validaEstoque(produto.codigo, 100)  }
    }

    @Test
    fun `test debit estoque produto`(){

        val produto = ProdutoBuild.produto("7000")
        every { produtoRepository.findById(produto.codigo) } returns Optional.of(produto)
        every { produtoRepository.save(produto) } returns produto
        every { produtoService.findProdutoByCodigo(produto.codigo) } returns  produto
        every { estoqueRepository.findById(produto.estoque.id) } returns Optional.of(produto.estoque)
        every { estoqueRepository.save(produto.estoque) } returns produto.estoque

        val produtoSaveOne = produtoService.vendaProduto(produto.codigo, 2)

        assert(produtoService.validaEstoque(produto.codigo,90))
        assertTrue(produtoSaveOne.estoque.reserva == 2)
        assert(produtoSaveOne.estoque.estoqueAtual == 98)

        val produtoSaveTwo = produtoService.vendaProduto(produto.codigo, 8)

        assertFalse(produtoService.validaEstoque(produto.codigo,99))
        assertTrue(produtoSaveTwo.estoque.reserva == 10)
        assert(produtoSaveTwo.estoque.estoqueAtual == 90)

        val produtoSaveThree = produtoService.vendaProduto(produto.codigo, 1)

        assertTrue(produtoService.validaEstoque(produto.codigo,89))
        assertTrue(produtoSaveThree.estoque.reserva == 11)
        assert(produtoSaveThree.estoque.estoqueAtual == 89)

    }

    @Test
    fun `test contagem saldo estoque produto de todos depositos`(){

        val produto = ProdutoBuild.produto("52104", 100, 20)
        every { produtoRepository.findById(produto.codigo) } returns Optional.of(produto)
        every { estoqueRepository.findById(produto.estoque.id) } returns Optional.of(produto.estoque)

        assert(estoqueService.totalEstoque(produto.estoque.id) == 120)

    }

    @Test
    fun `test delete produto`(){

        var produto = ProdutoBuild.produto()
        every { produtoRepository.findById(produto.codigo) } returns Optional.of(produto)
        every { produtoRepository.delete(any()) } returns Unit

        assertTrue(produtoService.deleteProduto(produto.codigo) == Unit)
    }

    @Test
    fun `test delete produto que não existe`(){

        var produto = ProdutoBuild.produto()
        every { produtoRepository.findById(produto.codigo) } returns Optional.empty()

        assertThrows<EntityResponseException> { produtoService.deleteProduto(produto.codigo)}
    }

    @Test
    fun `test update produto`(){

        var produtoDB = ProdutoBuild.produto("500")
        var produtoRequest = ProdutoBuild.createRequest("500")
        every { produtoRepository.save(produtoDB) } returns produtoDB
        every { produtoRepository.findById(produtoDB.codigo) } returns Optional.of(produtoDB)
        every { mapper.toUpdateProduto(produtoRequest, produtoDB) } returns produtoDB

        produtoRequest.descricao = "descricao alterada"
        produtoRequest.status = false

        val produtoSave = produtoService.updateProduto(produtoRequest)
        assertTrue(produtoSave.imagens?.size == 1)
        assertTrue(produtoSave.descricao == produtoDB.descricao)
        assertTrue(produtoSave.status == produtoDB.status)
    }

    @Test
    fun `test update produto not found`(){

        var produtoNotFound = ProdutoBuild.createRequest("600")
        every { produtoRepository.findById(produtoNotFound.codigo) } returns Optional.empty()

        assertThrows<EntityResponseException> { produtoService.updateProduto(produtoNotFound) }
        produtoNotFound.codigo = ""
        assertThrows<EntityResponseException> { produtoService.updateProduto(produtoNotFound) }
    }

    @Test
    fun `test mudanca status produto`(){

        var produto = ProdutoBuild.produto("6500")
        every { produtoRepository.save(produto) } returns produto
        every { produtoRepository.findById(produto.codigo) } returns Optional.of(produto)

        produto.status = false
        assertTrue(produtoService.mudaStatus(produto.codigo).status)

        produto.status = true
        assertFalse(produtoService.mudaStatus(produto.codigo).status)
    }

}