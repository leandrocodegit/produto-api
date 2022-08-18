package com.api.produto.controller.response

import com.api.produto.model.Deposito
import javax.persistence.ManyToMany

class EstoqueResponse(
        val id: Long,
        var estoqueAtual: Int,
        var depositos: MutableList<DepositoResponse>,
        var reserva: Int
)