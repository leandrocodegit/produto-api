package com.api.prdouto.model

import javax.persistence.*

@Entity
class Estoque(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long,
        var estoqueAtual: Int,
        @ManyToMany
        var depositos: List<Deposito>,
        var reserva: Int
) {
}