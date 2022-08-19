package com.api.produto.build

import com.api.produto.controller.request.*
import com.api.produto.model.*
import com.api.produto.model.image.ImageContentProfile
import com.api.produto.model.image.Imagem
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
                        6,
                        10,
                        8,
                        "MM",
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
                        null,
                        null,
                        mutableListOf(Imagem(
                                1L,
                                false,
                                listOf( ImageContentProfile(UUID.randomUUID().toString(), 0, "type", false),
                                        ImageContentProfile(UUID.randomUUID().toString(), 0, "type", true))
                        )),

                        Estoque(1L,
                                saldo1 + saldo2,
                                mutableListOf(
                                        Deposito(
                                                1L,
                                                saldo1,
                                                Local( 1, "Padrão ","Padrão"),codigo
                                        ),
                                        Deposito(
                                                1L,
                                                saldo2,
                                                Local( 1, "Padrão ","Padrão"), codigo
                                        )),0),
                        Categoria(
                                1L,
                                "Taças"
                        )
                )
        }

         fun createRequest(codigo: String): ProdutoRequest {
            return ProdutoRequest(
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
                    6,
                    10,
                    8,
                    "MM",
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
                    true,
                    Categoria(
                            1L,
                            "Taças"
                    )
            )
        }

    }
}