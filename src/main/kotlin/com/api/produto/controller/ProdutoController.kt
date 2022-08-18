package com.api.produto.controller

import com.api.produto.controller.request.ProdutoRequest
import com.api.produto.controller.request.VendaRequest
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
    fun buscaProdutoByCodigo(@PathVariable codigo: String) =
            ResponseEntity.ok(mapper.toResponse(produtoService.findProdutoByCodigo(codigo)))

    @GetMapping
    fun listAllProduto() =
            ResponseEntity.ok(produtoService.listAllProdutos().map { mapper.toResponse(it) }.toList())

    @PatchMapping
    fun updateProduto(@RequestBody @Valid produtoRequest: ProdutoRequest) =
            ResponseEntity.ok(mapper.toResponse(produtoService.updateProduto(
                produtoRequest
        )))

    @GetMapping("/status/{codigo}")
    fun mudaStatusProduto(@PathVariable codigo: String) = ResponseEntity.ok(mapper.toResponse(produtoService.mudaStatus(
                codigo
        )))

    @DeleteMapping("/{codigo}")
    fun deleteProduto(@PathVariable codigo: String) = ResponseEntity.ok(produtoService.deleteProduto(codigo))

    @PostMapping("/venda")
    fun vendaProduto(@RequestBody @Valid vendaRequest: VendaRequest) = ResponseEntity.ok(produtoService.vendaProduto(
            vendaRequest.codigo,
            vendaRequest.quantidade
    ))

    @PostMapping
    fun createProduto(@RequestBody @Valid produtoRequest: ProdutoRequest): ResponseEntity<ProdutoResponse> {
        return ResponseEntity.ok(
                mapper.toResponse(
                        produtoService.createProduto(
                                mapper.toEntity(produtoRequest))))
    }


}