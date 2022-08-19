package com.api.produto.controller

import com.api.produto.enuns.CodeError
import com.api.produto.exceptions.EntityResponseException
import com.api.produto.mapper.ProdutoMapper
import com.api.produto.model.image.Imagem
import com.api.produto.repository.ImageContentProfileRepository
import com.api.produto.repository.ImageRepository
import com.api.produto.repository.ImageStore
import com.api.produto.service.ImageStoreService
import com.api.produto.service.ProdutoService
import io.swagger.annotations.Api
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v1/imagem")
@Api(tags= ["Endpoints imagens"], description = ":")
class ImagemController(
        private val produtoService: ProdutoService,
        private val imageStoreService: ImageStoreService,
        private val imagemRepository: ImageRepository,
        private val imageStore: ImageStore,
) {

    @PostMapping("{codigo}")
    @ResponseStatus(HttpStatus.OK)
    fun saveImagem(@PathVariable codigo: String, @RequestParam("file") file: MultipartFile) {

        var produto = produtoService.findProdutoByCodigo(codigo)
        var imagemSave = imageStoreService.saveImage(produto, file.inputStream, file.size, file.contentType.toString())
        produto.imagens?.apply { add(imagemSave) }
        imageStoreService.atualizaLinkDeImagens(produtoService.updateProduto(produto))
    }

    @GetMapping("{id}")
    fun fileImagem(@PathVariable id: String): ResponseEntity<ByteArray> {
        if (imageStore.getResource(id).file.exists().not())
            ResponseEntity.badRequest()
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageStore.getResource(id).file.readBytes());
    }

    @DeleteMapping("delete/{codigo}/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun deleteImagem(@PathVariable codigo: String, @PathVariable id: Long) {
        imageStoreService.deleteImage(codigo, id)
    }

    @GetMapping("/icon/{codigo}/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun definerComoImagemPrincipal(@PathVariable codigo: String, @PathVariable id: Long) {
        imageStoreService.defineImagemPrincipal(codigo,id)
    }
}