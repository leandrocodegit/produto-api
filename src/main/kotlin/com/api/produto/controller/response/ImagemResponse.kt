package com.api.produto.controller.response

import com.api.produto.model.image.ImageContentProfile
import org.springframework.content.commons.annotations.ContentId
import org.springframework.content.commons.annotations.ContentLength
import java.util.*
import javax.persistence.CascadeType
import javax.persistence.OneToMany

class ImagemResponse(
        var id: Long,
        var principal: Boolean,
        var profiles: List<ImageContentProfileResponse>
)