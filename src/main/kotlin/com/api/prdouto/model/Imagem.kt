package com.api.prdouto.model

import org.springframework.content.commons.annotations.ContentId
import org.springframework.content.commons.annotations.ContentLength
import java.util.*
import javax.persistence.*

@Entity
class Imagem (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,
    var link: String?,
    var principal: Boolean = false,
    @ContentId
    var contentId: UUID,
    @ContentLength
    var contentLength: Long,
    var contentMimeType: String
)