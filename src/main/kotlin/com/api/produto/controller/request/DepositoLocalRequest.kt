package com.api.produto.controller.request

import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

class DepositoLocalRequest(
        @field:Positive
        var id: Long,
        @field:NotBlank
        var codigoProduto: String,
        @field:Positive
        var localID: Long
)