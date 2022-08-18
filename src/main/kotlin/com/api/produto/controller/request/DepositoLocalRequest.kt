package com.api.produto.controller.request

import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

class DepositoLocalRequest(
        @field:NotNull
        var id: Long,
        @field:NotBlank
        var codigoProduto: String,
        @field:NotNull
        var localID: Long
)