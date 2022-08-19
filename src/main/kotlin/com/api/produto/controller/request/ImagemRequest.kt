package com.api.produto.controller.request

import java.util.*

class ImagemRequest(
        var id: Long,
        var link: String?,
        var principal: Boolean = false,
        var contentId: UUID,
        var contentLength: Long,
        var contentMimeType: String
)
