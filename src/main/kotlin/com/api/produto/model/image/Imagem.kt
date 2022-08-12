package com.api.produto.model.image

import jdk.net.UnixDomainPrincipal
import javax.persistence.*
import kotlin.jvm.Transient

@Entity
class Imagem(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long,
        var principal: Boolean,
        @OneToMany(cascade = [CascadeType.ALL])
        var profiles: List<ImageContentProfile>,
        var imageThumbnail: String? = ""
)