package com.api.produto.repository

import com.api.produto.model.image.ImageContentProfile
import org.springframework.content.commons.repository.ContentStore

import java.util.UUID

interface ImageStore: ContentStore<ImageContentProfile, UUID>