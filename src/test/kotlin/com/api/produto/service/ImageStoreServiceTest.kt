package com.api.produto.service

import com.api.produto.build.ProdutoBuild
import com.api.produto.mapper.ProdutoMapper
import com.api.produto.model.image.ImageContentProfile
import com.api.produto.model.image.Imagem
import com.api.produto.repository.EstoqueRepository
import com.api.produto.repository.ImageRepository
import com.api.produto.repository.ImageStore
import com.api.produto.repository.ProdutoRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.mock.web.MockMultipartFile
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileInputStream
import java.util.*

@ExtendWith(MockKExtension::class)
class ImageStoreServiceTest {


    @InjectMockKs
    private lateinit var produtoService: ProdutoService
    @InjectMockKs
    private lateinit var estoqueService: EstoqueService
    @MockK
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
    fun `test delete imagens`() {

        val path = kotlin.io.path.Path("").toAbsolutePath()
        val file: MultipartFile = MockMultipartFile("sacola.png", FileInputStream(File("$path/midia/sacola.png")))

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
                every { imageStore.setContent(result,file.inputStream) } returns result
                every { imageStore.unsetContent(result) } returns result
            }
        }

        imageStoreService.deleteImage(produto.codigo, imagem1.id)
        assertTrue(produto.imagens!!.size == 3)
        assertFalse(produto.imagens!!.any { it.id == 1L })
        assertTrue(produto.imagens!!.any { it.id == 2L })
        assertTrue(produto.imagens!!.any { it.id == 3L })
        assertTrue(produto.imagens!!.any { it.id == 4L })

        imageStoreService.deleteImage(produto.codigo, imagem2.id)
        assertTrue(produto.imagens!!.size == 2)
        assertFalse(produto.imagens!!.any { it.id == 1L })
        assertFalse(produto.imagens!!.any { it.id == 2L })
        assertTrue(produto.imagens!!.any { it.id == 3L })
        assertTrue(produto.imagens!!.any { it.id == 4L })

        imageStoreService.deleteImage(produto.codigo, imagem3.id)
        assertTrue(produto.imagens!!.size == 1)
        assertFalse(produto.imagens!!.any { it.id == 1L })
        assertFalse(produto.imagens!!.any { it.id == 2L })
        assertFalse(produto.imagens!!.any { it.id == 3L })
        assertTrue(produto.imagens!!.any { it.id == 4L })

        imageStoreService.deleteImage(produto.codigo, imagem4.id)
        assertTrue(produto.imagens!!.size == 0)
        assertFalse(produto.imagens!!.any { it.id == 1L })
        assertFalse(produto.imagens!!.any { it.id == 2L })
        assertFalse(produto.imagens!!.any { it.id == 3L })
        assertFalse(produto.imagens!!.any { it.id == 4L })

    }

    @Test
    fun `test delete file`() {

        val path = kotlin.io.path.Path("").toAbsolutePath()
        val file: MultipartFile = MockMultipartFile("sacola.png", FileInputStream(File("$path/midia/sacola.png")))

        var imagem =  Imagem(1L,false,
                listOf( ImageContentProfile(UUID.randomUUID().toString(), 0, "type", false),
                        ImageContentProfile(UUID.randomUUID().toString(), 0, "type", true)))

        var produto = ProdutoBuild.produto()
        var listImagens =  mutableListOf(imagem)

        produto.imagens = listImagens

        every { produtoRepository.findById(produto.codigo) } returns Optional.of(produto)
        every { produtoRepository.save(produto) } returns produto

        produto.imagens!!.forEach{
            every { imageRepository.findById(it.id) } returns Optional.of(it)
            every { imageRepository.deleteById(it.id) } returns Unit
            it.profiles.forEach { result ->
                every { imageStore.setContent(result,file.inputStream) } returns result
                every { imageStore.unsetContent(result) } returns result
                every { imageStore.getResource(result.contentId) } returns file.resource
            }
        }

        assertTrue(imageStore.getResource(imagem.profiles.first().contentId).exists())
        imageStoreService.deleteImage(produto.codigo, imagem.id)
        assertTrue(produto.imagens!!.size == 0)

    }
}