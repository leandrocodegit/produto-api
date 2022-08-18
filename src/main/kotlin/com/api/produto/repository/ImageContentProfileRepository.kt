package com.api.produto.repository

import com.api.produto.model.image.ImageContentProfile
import org.springframework.content.commons.repository.ContentStore
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.stereotype.Repository

@Repository
interface ImageContentProfileRepository: JpaRepository<ImageContentProfile, String>