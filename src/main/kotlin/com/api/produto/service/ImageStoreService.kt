package com.api.produto.service

import com.api.produto.enuns.CodeError
import com.api.produto.exceptions.EntityResponseException
import com.api.produto.file.RenderImagem
import com.api.produto.model.image.ImageContentProfile
import com.api.produto.model.image.Imagem
import com.api.produto.repository.ImageContentProfileRepository
import com.api.produto.repository.ImageRepository
import com.api.produto.repository.ImageStore
import org.springframework.stereotype.Component
import java.io.InputStream
import java.util.*

@Component
class ImageStoreService(
        private val imageContentProfileRepository: ImageContentProfileRepository,
        private val imageRepository: ImageRepository,
        private val imageStore: ImageStore
) {
    fun saveImage(file: InputStream, size:Long, type: String): Imagem {

        var principal = ImageContentProfile(0,UUID.randomUUID(), size, type)
        imageStore.setContent(principal, file)

        var profile = ImageContentProfile(0,UUID.randomUUID(), size, type)
        imageStore.setContent(profile, imageStore.getContent(principal))

        var imagem = Imagem(
                0,
                false,
                listOf(principal,profile)
        )
       RenderImagem.createIcon(imagem)
        return  imagem
    }
    fun deleteImage(id: Long){
        var img = imageRepository.findById(id).get()
        if(img.profiles.isEmpty())
            throw EntityResponseException("Imagem com id $id não encontrado", CodeError.NOT_FOUND)
        imageContentProfileRepository.deleteAll()
        imageRepository.delete(img)
    }

}
