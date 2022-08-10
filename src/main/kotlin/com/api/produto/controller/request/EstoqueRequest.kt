package com.api.produto.controller.request

class EstoqueRequest(
        val id: Long,
        var estoqueAtual: Int,
        var depositos: List<DepositoRequest>,
        var reserva: Int
)