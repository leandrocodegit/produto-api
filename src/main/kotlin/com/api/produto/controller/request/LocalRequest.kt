package com.api.produto.controller.request

import com.api.produto.controller.valid.OnCreate
import com.api.produto.controller.valid.OnUpdate
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Positive

class LocalRequest(
        @field:Positive(groups = [OnUpdate::class])
        var id: Long,
        @field:NotBlank(groups = [OnCreate::class])
        var nome: String,
        @field:NotBlank(groups = [OnCreate::class])
        var endereco: String
        ){
        constructor(): this(0,"","")
        constructor(id: Long): this(id,"","")
        constructor(nome: String, endereco: String): this(0,nome,endereco)
}
