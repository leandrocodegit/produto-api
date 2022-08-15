package com.api.produto.model.image

import org.springframework.content.commons.annotations.ContentId
import org.springframework.content.commons.annotations.ContentLength
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "imagem_content_profile")
class ImageContentProfile(
        @Id
        @ContentId
        @Column(name = "content_id")
        var contentId: String,
        @ContentLength
        @Column(name = "content_length")
        var contentLength: Long,
        @Column(name = "content_mime_type")
        var contentMimeType: String,
        @Column(name = "is_rendered")
        var isRendered: Boolean
){
        @Transient
        var link: String = ""
                get() {
                        return "/api/v1/imagem/$contentId"
                }
}