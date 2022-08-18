package com.api.produto.controller.request

import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank

class VendaRequest(
        @field:NotBlank
        var codigo: String,
        @field:Min(1)
        var quantidade: Int
        )