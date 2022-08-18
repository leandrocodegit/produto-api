package com.api.produto.model

import javax.persistence.*

@Entity
@Table(name = "estoques")
class Estoque(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long,
        @Column(name = "estoque_atual")
        var estoqueAtual: Int,
        @ManyToMany(cascade = [CascadeType.ALL])
        var depositos: MutableList<Deposito>,
        var reserva: Int,
) {
    constructor( ) : this(0, 0, mutableListOf(), 0)
    constructor(depositos: MutableList<Deposito>) : this(0,  0, depositos, 0)
}