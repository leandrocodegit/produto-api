package com.api.prdouto.service

import com.api.prdouto.build.ProdutoBuild
import com.api.prdouto.model.Imagem
import com.api.prdouto.repository.ImageRepository
import com.api.prdouto.repository.ImageStore
import com.api.prdouto.repository.ProdutoRepository
import org.springframework.content.commons.property.PropertyPath
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
class ImageStoreService(
        private val produtoRepository: ProdutoRepository,
        private val imageRepository: ImageRepository,
        private val imageStore: ImageStore
) {

    fun saveImage(file: MultipartFile): Imagem{

        var imagem = Imagem(
                0,
                file.originalFilename,
                false,
                UUID.randomUUID(),
                file.size,
                file.contentType!!,
        )

        imageStore.setContent(imagem, file.inputStream)
        return imageRepository.save(imagem)

    }
}