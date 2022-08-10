package com.api.produto.repository


import com.api.produto.model.image.Imagem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource(path="files", collectionResourceRel="files", )
interface ImageRepository: JpaRepository<Imagem, Long>