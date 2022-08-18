package com.api.produto.controller.request

import javax.validation.Valid
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

class DepositoRequest(
        @field:NotNull
        var id: Long,
        @field:NotBlank
        var codigoProduto: String,
        var saldo: Int,
        @field:Valid
        var local: LocalRequest
){
        constructor():this(0,"",0,LocalRequest())
        constructor( id: Long,codigoProduto: String,
                     local: LocalRequest):this(id,codigoProduto,0,local)
        constructor( codigoProduto: String,
                     saldo: Int,
                     local: LocalRequest):this(0,codigoProduto,saldo,local)

}