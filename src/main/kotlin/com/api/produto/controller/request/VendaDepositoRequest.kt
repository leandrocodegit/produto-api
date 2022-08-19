package com.api.produto.controller.request

import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Positive

class VendaDepositoRequest(
        @field:Positive
        var idDepostito: Long,
        @field:NotBlank
        var codigoProduto: String,
        @field:Min(1)
        var quantidade: Int
        )