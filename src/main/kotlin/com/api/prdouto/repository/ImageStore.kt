package com.api.prdouto.repository

import com.api.prdouto.model.Imagem
import org.springframework.content.commons.repository.ContentStore

import java.util.UUID

interface ImageStore: ContentStore<Imagem, UUID>