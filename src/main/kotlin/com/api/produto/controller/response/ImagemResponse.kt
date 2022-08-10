package com.api.produto.controller.response

import org.springframework.content.commons.annotations.ContentId
import org.springframework.content.commons.annotations.ContentLength
import java.util.*

class ImagemResponse(
        var id: Long,
        var link: String?,
        var principal: Boolean = false,
        var contentId: UUID,
        var contentLength: Long,
        var contentMimeType: String
)
