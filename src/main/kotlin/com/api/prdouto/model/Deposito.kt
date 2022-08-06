package com.api.prdouto.model

import javax.persistence.*

@Entity
class Deposito (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,
    var nome: String,
    var saldo: Int,
    @Embedded
    var local: Local
)