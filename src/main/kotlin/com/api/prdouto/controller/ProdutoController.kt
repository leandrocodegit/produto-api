package com.api.prdouto.controller

import com.api.prdouto.build.ProdutoBuild
import com.api.prdouto.model.Imagem
import com.api.prdouto.repository.ImageRepository
import com.api.prdouto.repository.ImageStore
import com.api.prdouto.repository.ProdutoRepository
import com.api.prdouto.service.ImageStoreService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.commons.CommonsMultipartFile
import java.util.*

@RestController
@RequestMapping("/api/v1/produto")
class ProdutoController(
        private val produtoRepository: ProdutoRepository,
        private val imageStoreService: ImageStoreService
) {

    @PostMapping("/file")
    fun saveImagem(@RequestParam("file") file: MultipartFile) {
        imageStoreService.saveImage(file)
    }
}