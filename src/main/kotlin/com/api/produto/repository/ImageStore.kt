package com.api.produto.repository

import com.api.produto.model.image.ImageContentProfile
import org.springframework.content.commons.repository.ContentStore
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.stereotype.Repository

import java.util.UUID

@RepositoryRestResource
interface ImageStore: ContentStore<ImageContentProfile, String>