package com.api.prdouto.build

import com.api.prdouto.model.*
import java.util.*

class ProdutoBuild {

    companion object {
        fun produto() = create("8000", true, 100,0)

        fun produto(status: Boolean) = create("8000", status, 100,0)
        fun produto(codigo: String) = create(codigo, true, 100,0)
        fun produto(codigo: String, status: Boolean) = create(codigo, status, 100,0)
        fun produto(codigo: String, status: Boolean, saldo: Int) = create(codigo, status, saldo, saldo)
        fun produto(codigo: String, deposito1: Int, deposito2: Int) = create(codigo, true, deposito1, deposito2)
        fun produto(codigo: String, status: Boolean, deposito1: Int, deposito2: Int) = create(codigo, status, deposito1, deposito2)

        private fun create(codigo: String, status: Boolean, saldo1: Int, saldo2: Int): Produto {
            return Produto(
                    codigo,
                    "Taça",
                    "Jogo com 6 taças cristal",
                    "JG",
                    400.0,
                    400.0,
                    6.0,
                    6.0,
                    1,
                    100,
                    "",
                    "",
                    "Jogo com 6 taças cristal",
                    "",
                    "6",
                    "10",
                    "8",
                    "MM",
                    "",
                    "",
                    "",
                    "Decoratem",
                    "Bohemia",
                    "",
                    "",
                    "Importado",
                    "",
                    "",
                    "",
                    "",
                    6,
                    0,
                    "",
                    "",
                    "",
                    12,
                    "",
                    "N",
                    "",
                    "",
                    "",
                    "",
                    status,
                    listOf(Imagem(
                            1L,
                            "",
                            true,
                            UUID.randomUUID(),
                            0,
                            "null"
                    )),
                    Estoque(1L,
                            saldo1 + saldo2,
                            listOf(
                            Deposito(
                                    1L,
                                    "Local",
                                    saldo1,
                                    Local( "CD")
                            ),
                            Deposito(
                                    2L,
                                    "Local",
                                    saldo2,
                                    Local( "CD")
                            )),0),
                    Categoria(
                            1L,
                            "Taças",
                            ""
                    ),
                    Imagem(
                            1L,
                            "",
                            true,
                            UUID.randomUUID(),
                            0,
                            "null"
                    ),
            )
        }
    }
}