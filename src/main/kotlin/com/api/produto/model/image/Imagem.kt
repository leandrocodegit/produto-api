package com.api.produto.model.image

import jdk.net.UnixDomainPrincipal
import javax.persistence.*

@Entity
class Imagem(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long,
        var principal: Boolean,
        @OneToMany(cascade = [CascadeType.ALL])
        var profiles: List<ImageContentProfile>
)