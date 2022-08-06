package com.api.prdouto.repository

import com.api.prdouto.model.Imagem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource(path="files", collectionResourceRel="files", )
interface ImageRepository: JpaRepository<Imagem, Long>