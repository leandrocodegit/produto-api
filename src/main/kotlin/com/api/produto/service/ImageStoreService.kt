package com.api.produto.service

import com.api.produto.enuns.CodeError
import com.api.produto.exceptions.EntityResponseException
import com.api.produto.file.RenderImagem
import com.api.produto.model.Produto
import com.api.produto.model.image.ImageContentProfile
import com.api.produto.model.image.Imagem
import com.api.produto.repository.ImageContentProfileRepository
import com.api.produto.repository.ImageRepository
import com.api.produto.repository.ImageStore
import com.api.produto.repository.ProdutoRepository
import org.springframework.stereotype.Component
import java.io.InputStream
import java.util.*
import java.util.function.Consumer

@Component
class ImageStoreService(
        private val imageRepository: ImageRepository,
        private val produtoService: ProdutoService,
        private val imageStore: ImageStore
) {

    fun findImagemById(id: Long): Imagem {
        return imageRepository.findById(id).orElseThrow {
            throw EntityResponseException("Imagem com id $id não encontrado", CodeError.REST_ERROR)
        }
    }

    fun saveImage(produto: Produto, file: InputStream, size: Long, type: String): Imagem {

        var principal = ImageContentProfile(UUID.randomUUID().toString(), size, type, false)
        imageStore.setContent(principal, file)

        var profile = ImageContentProfile(UUID.randomUUID().toString(), size, type, true)
        imageStore.setContent(profile, imageStore.getContent(principal))

        return Imagem(
                0,
                produto.imagens?.none {
                    it.principal
                }!!,
                listOf(principal, profile))
    }

    fun atualizaLinkDeImagens(produto: Produto): Produto {

        if (produto.imagens != null) {
            if (produto.imagens!!.isEmpty().not()) {
                produto.imagens!!.all() {
                    it.principal.not()
                }.let { result ->
                    if (result)
                        produto.imagens!!.random().apply {
                            principal = true
                        }
                }

                var image = produto.imagens?.find { it.principal }
                produto.imageThumbnail = image?.profiles?.find { it.isRendered }?.contentId
                produto.imageOriginal = image?.profiles?.find { it.isRendered.not() }?.contentId

            }
        }
        return produtoService.updateProduto(produto)
    }

    fun updateImagemPrincipal(produto: Produto, imagem: Imagem): Produto {

        produto.imagens?.forEach {
            if (it.id == imagem.id) {
                it.profiles.forEach { im ->
                    produto.imageOriginal = im.contentId
                    if (im.isRendered)
                        produto.imageThumbnail = im.contentId
                }
            } else {
                it.principal = false
            }
        }
        return produto
    }

    fun deleteImage(codigo: String, id: Long) {

        findImagemById(id)
        val produto = produtoService.findProdutoByCodigo(codigo)
        var imagem = produto.imagens?.find { it.id == id }

        if (imagem != null) {
            imagem?.profiles?.forEach { profile ->
                imageStore.unsetContent(profile)
            }
            produto.imagens?.remove(imagem)
            imageRepository.deleteById(id)
            atualizaLinkDeImagens(produtoService.updateProduto(produto))
        } else {
            throw EntityResponseException("Erro ao deletar arquivo", CodeError.CONTENT_EMPTY)
        }
    }

    fun defineImagemPrincipal(codigo: String, id: Long): Produto{
        var produtoDB = produtoService.findProdutoByCodigo(codigo)
        var imagem = findImagemById(id)
        if (produtoDB.imagens == null)
            throw EntityResponseException("Não existe imagens cadastradas para este produto", CodeError.PARAM_INVALID)
        produtoDB.imagens?.any { it.id == id }
                .let { result ->
                    if (result == true){
                        produtoDB.imagens?.forEach(Consumer {
                            it.principal = false
                        })
                        produtoDB.imagens?.find{it.id == id }.let {
                            if(it != null) {
                                it.principal = true
                                atualizaLinkDeImagens(produtoService.updateProduto(produtoDB))
                            }
                        }
                    }
                    else{
                        throw EntityResponseException("Imagem não encontrada", CodeError.NOT_FOUND)
                    }
                }
        return  produtoDB
    }

}
