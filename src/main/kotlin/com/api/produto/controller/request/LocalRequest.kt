package com.api.produto.controller.request

class LocalRequest(
        var id: Long,
        var nome: String,
        var endereco: String
        ){
        constructor(): this(0,"","")
        constructor(id: Long): this(id,"","")
}
