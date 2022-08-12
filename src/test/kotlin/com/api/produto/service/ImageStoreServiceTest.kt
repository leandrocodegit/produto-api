package com.api.produto.service

import com.api.produto.build.ProdutoBuild
import com.api.produto.mapper.ProdutoMapper
import com.api.produto.model.image.Imagem
import com.api.produto.repository.EstoqueRepository
import com.api.produto.repository.ImageRepository
import com.api.produto.repository.ImageStore
import com.api.produto.repository.ProdutoRepository
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.hibernate.service.spi.InjectService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.mock.mockito.MockBeans
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import java.util.*

@ExtendWith(MockKExtension::class)
class ImageStoreServiceTest {


    @InjectMockKs
    private lateinit var produtoService: ProdutoService

    @InjectMockKs
    private lateinit var estoqueService: EstoqueService

    private lateinit var imageStoreService: ImageStoreService

    @MockK
    private lateinit var produtoRepository: ProdutoRepository

    @MockK
    private lateinit var estoqueRepository: EstoqueRepository

    @MockK
    private lateinit var imageStore: ImageStore

    @MockK
    private lateinit var imageRepository: ImageRepository

    @MockK
    private lateinit var mapper: ProdutoMapper

    @BeforeEach
    fun setup() {

        imageStoreService = ImageStoreService(
                imageRepository,
                produtoService,
                imageStore,
        )

        imageStoreService.apply {
            imageRepository
            produtoService
            imageStore
        }
    }

    @Test
    fun `test atualiza link imagens`() {

        var produto = ProdutoBuild.produto()
        var imagem: Imagem = ProdutoBuild.produto().imagens!!.first()
        var imageProfile = produto.imagens?.first()?.profiles?.find { it.isRendered.not() }

        every { produtoRepository.findById(produto.codigo) } returns Optional.of(produto)
        every { produtoRepository.save(produto) } returns produto
        every { imageRepository.save(imagem) } returns imagem

        assertTrue(produto.imageOriginal == null)
        assertTrue(imageStoreService.atualizaLinkDeImagens(produto).imageOriginal == imageProfile?.contentId)
        assertFalse(produto.imageOriginal == null)

    }

    @Test
    fun `test atualiza link de imagens com lista vazia`() {

        var produto = ProdutoBuild.produto()
        produto.imagens = null

        every { produtoRepository.findById(produto.codigo) } returns Optional.of(produto)
        every { produtoRepository.save(produto) } returns produto

        assertTrue(produto.imagens == null)
        assertTrue(imageStoreService.atualizaLinkDeImagens(produto).imageOriginal == null)

    }
}