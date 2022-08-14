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
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*
import javax.validation.Valid
import kotlin.io.path.Path


@RestController
@RequestMapping("/api/v1/produto")
class ProdutoController(
        private val produtoService: ProdutoService,
        private val mapper: ProdutoMapper,
) {

    @GetMapping("{codigo}")
    fun buscaProdutoByCodigo(@PathVariable codigo: String): ResponseEntity<ProdutoResponse> {
        var lista:List<String> = mutableListOf()
        lista.plus("Emento adicionado com sucesso!")

        println(lista.toString())
        return ResponseEntity.ok(mapper.toResponse(produtoService.findProdutoByCodigo(codigo)))
    }

    @PostMapping
    fun createProduto(@RequestBody @Valid produtoRequest: ProdutoRequest): ResponseEntity<ProdutoResponse> {
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