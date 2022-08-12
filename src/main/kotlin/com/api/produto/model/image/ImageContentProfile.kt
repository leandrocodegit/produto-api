package com.api.produto.model.image

import org.springframework.content.commons.annotations.ContentId
import org.springframework.content.commons.annotations.ContentLength
import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class ImageContentProfile(
        @Id
        @ContentId
        var contentId: String,
        @ContentLength
        var contentLength: Long,
        var contentMimeType: String,
        var isRendered: Boolean
){
        @Transient
        var link: String = ""
                get() {
                        return "/api/v1/imagem/$contentId"
                }
}