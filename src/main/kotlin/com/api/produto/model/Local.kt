package com.api.produto.model


import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity

class Local(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long,
        var nome: String,
        var endereco: String
        ){
    constructor(): this(0,"","")
    constructor(id: Long): this(id,"","")
}