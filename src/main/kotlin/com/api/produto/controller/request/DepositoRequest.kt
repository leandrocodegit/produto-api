package com.api.produto.controller.request

class DepositoRequest(
        var id: Long,
        var nome: String,
        var saldo: Int,
        var local: LocalRequest
)