package com.api.produto.service

import com.api.produto.build.ProdutoBuild
import com.api.produto.mapper.ProdutoMapper
import com.api.produto.model.image.ImageContentProfile
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
import org.aspectj.lang.annotation.Before
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
import org.springframework.web.bind.annotation.InitBinder
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


    @Test
    fun `test delete imagens com lista vazia`() {

        var imagem1 =  Imagem(1L,false,
                listOf( ImageContentProfile(UUID.randomUUID().toString(), 0, "type", false),
                        ImageContentProfile(UUID.randomUUID().toString(), 0, "type", true)))
        var imagem2 =  Imagem(2L,false,
                listOf( ImageContentProfile(UUID.randomUUID().toString(), 0, "type", false),
                        ImageContentProfile(UUID.randomUUID().toString(), 0, "type", true)))
        var imagem3 =  Imagem(3L,false,
                listOf( ImageContentProfile(UUID.randomUUID().toString(), 0, "type", false),
                        ImageContentProfile(UUID.randomUUID().toString(), 0, "type", true)))
        var imagem4 =  Imagem(4L,false,
                listOf( ImageContentProfile(UUID.randomUUID().toString(), 0, "type", false),
                        ImageContentProfile(UUID.randomUUID().toString(), 0, "type", true)))

        var produto = ProdutoBuild.produto()
        var listImagens =  mutableListOf(imagem1, imagem2, imagem3, imagem4)

        produto.imagens = listImagens

        every { produtoRepository.findById(produto.codigo) } returns Optional.of(produto)
        every { produtoRepository.save(produto) } returns produto

        produto.imagens!!.forEach{
            every { imageRepository.findById(it.id) } returns Optional.of(it)
            every { imageRepository.deleteById(it.id) } returns Unit
            it.profiles.forEach { result ->
                every { imageStore.setContent(it,) }
                every { imageStore.unsetContent(result) }
            }
        }

        imageStoreService.deleteImage(produto.codigo, imagem1.id)
        assertTrue(produto.imagens!!.size == 3)
        imageStoreService.deleteImage(produto.codigo, imagem2.id)
        assertTrue(produto.imagens!!.size == 2)
        imageStoreService.deleteImage(produto.codigo, imagem3.id)
        assertTrue(produto.imagens!!.size == 1)
        imageStoreService.deleteImage(produto.codigo, imagem4.id)
        assertTrue(produto.imagens!!.size == 0)


    }
}