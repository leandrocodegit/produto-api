package com.api.produto.controller.response

import org.springframework.content.commons.annotations.ContentLength

class ImageContentProfileResponse(
        var contentId: String,
        var contentLength: Long,
        var contentMimeType: String,
        var isRendered: Boolean,
        var link: String)