package com.api.produto.controller

import com.api.produto.controller.request.ProdutoRequest
import com.api.produto.controller.response.ProdutoResponse
import com.api.produto.file.RenderImagem
import com.api.produto.mapper.ProdutoMapper
import com.api.produto.model.image.ImageContentProfile
import com.api.produto.model.image.Imagem
import com.api.produto.repository.ImageContentProfileRepository
import com.api.produto.repository.ImageRepository
import com.api.produto.repository.ImageStore
import com.api.produto.service.ImageStoreService
import com.api.produto.service.ProdutoService
import org.springframework.content.commons.annotations.MimeType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*


@RestController
@RequestMapping("/api/v1/produto")
class ProdutoController(
        private val produtoService: ProdutoService,
        private val imageContentProfileRepository: ImageContentProfileRepository,
        private val imageStoreService: ImageStoreService,
        private val mapper: ProdutoMapper,
        private val imageStore: ImageStore,
        private val imagemRepository: ImageRepository
) {

    @GetMapping("{codigo}")
    fun buscaProdutoByCodigo(@PathVariable codigo: String): ResponseEntity<ProdutoResponse> {
        return ResponseEntity.ok(mapper.toResponse(produtoService.findProdutoByCodigo(codigo)))
    }

    @PostMapping
    fun createProduto(@RequestBody produtoRequest: ProdutoRequest): ResponseEntity<ProdutoResponse> {
        return ResponseEntity.ok(
                mapper.toResponse(
                        produtoService.createProduto(
                                mapper.toEntity(produtoRequest))))
    }

    @GetMapping
    fun listAllProduto(): ResponseEntity<List<ProdutoResponse>> {
        var list = produtoService.listAllProdutos().map { mapper.toResponse(it) }.toList()
        return ResponseEntity.ok(list)
    }

    @PostMapping("/file")
    fun saveImagem(@RequestParam("file") file: MultipartFile) {

        var produto = produtoService.findProdutoByCodigo("7000")

        var lista: MutableList<Imagem>? = mutableListOf()

        produto.imagens?.forEach {
            lista?.add(it)
        }

        lista?.add(imageStoreService.saveImage(file.inputStream, file.size, file.contentType.toString()))
        produto.imagens = lista
        produtoService.updateProduto(produto)
    }

    @DeleteMapping("/file/delete/{codigo}/{id}")
    fun deleteImagem(@PathVariable codigo: String, @PathVariable id: Long) {

        var imagem = produtoService.findProdutoByCodigo(codigo)
                .imagens?.filter { it.id == id  }?.first()

       imagem?.profiles?.forEach {
            try {
                imageContentProfileRepository.delete(it)
                imageStore.unsetContent(it)
            } catch (ex: Exception) {
                println(ex.message)
            }

            imageStoreService.deleteImage(id)
        }
    }

    @PatchMapping
    fun updateProduto(@RequestBody produtoRequest: ProdutoRequest): ResponseEntity<ProdutoResponse> {
        return ResponseEntity.ok(mapper.toResponse(produtoService.updateProduto(
                produtoRequest
        )))
    }

    @GetMapping("/icon/{codigo}/{id}")
    fun savaIconImageProduto(@PathVariable codigo: String, @PathVariable id: Long) {
        produtoService.defineImagemPrincipal(
                codigo,
                id
        )
    }
}