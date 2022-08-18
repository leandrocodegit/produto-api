package com.api.produto.controller.request

class EstoqueRequest(
        val id: Long,
        var estoqueAtual: Int,
        var depositos: MutableList<DepositoRequest>,
        var reserva: Int
)