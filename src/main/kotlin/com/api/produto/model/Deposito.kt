package com.api.produto.model

import javax.persistence.*

@Entity
@Table(name = "depositos")
class Deposito (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long,
    var saldo: Int,
    @OneToOne
    var local: Local,
    var produtoCodigo: String?
){
    constructor():this(0,0,Local(1),null)
    constructor(saldo: Int,local: Local):this(0,saldo,local,null)
    constructor(saldo: Int,local: Local, produto: String?):this(0,saldo,local,produto)
}