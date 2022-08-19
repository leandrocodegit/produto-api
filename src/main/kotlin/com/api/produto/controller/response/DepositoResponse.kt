package com.api.produto.controller.response

import com.api.produto.model.Local
import javax.persistence.Embedded

class DepositoResponse(
        var id: Long,
        var saldo: Int,
        var local: LocalResponse
)