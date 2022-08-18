package com.api.produto.controller.request

import com.api.produto.enuns.CodeError
import com.api.produto.enuns.MensagemValidations
import com.api.produto.model.Categoria
import com.api.produto.model.Estoque
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

class ProdutoRequest(
        @field:NotBlank
        @field:Size(min = 1, max = 20)
        var codigo: String,
        @field:NotBlank
        @field:Size(min = 1, max = 40)
        var tipo: String,
        @field:NotBlank
        @field:Size(min = 20, max = 254)
        var descricao: String,
        @field:NotBlank
        @field:Size(min = 2, max = 40)
        var unidade: String,
        @field:Min(value = 1)
        var preco: Double,
        @field:Min(value = 1)
        var precoCusto: Double,
        @field:Min(value = 1)
        var pesoLiq: Double,
        @field:Min(value = 1)
        var pesoBruto: Double,
        @field:Min(value = 1)
        var estoqueMinimo: Int,
        @field:Min(value = 1)
        var estoqueMaximo: Int,
        var gtin: String,
        var gtinEmbalagem: String,
        @field:NotBlank
        @field:Size(min = 1, max = 254)
        var descricaoCurta: String,
        var descricaoComplementar: String,
        @field:Min(1)
        var largura: Int,
        @field:Min(1)
        var altura: Int,
        @field:Min(1)
        var profundidade: Int,
        @field:NotBlank
        @field:Size(min = 2, max = 40)
        var unidadeMedida: String,
        var dataInclusao: String,
        var dataAlteracao: String,
        @field:NotBlank
        @field:Size(min = 1, max = 40)
        var nomeFornecedor: String,
        @field:NotBlank
        @field:Size(min = 1, max = 40)
        var marca: String,
        var classFiscal: String,
        var cest: String,
        var origem: String,
        var idgrupo: String,
        var linkExterno: String,
        var observacoes: String,
        var grupo: String,
        @field:Min(1)
        var itensPorCaixa: Int,
        var volumes: Int,
        var urlVideo: String,
        var localizacao: String,
        var crossdocking: String,
        var garantia: Int,
        var condicao: String,
        @field:NotBlank
        var freteGratis: String,
        var producao: String,
        var dataValidade: String,
        var descricaoFornecedor: String,
        var codigopai: String,
        var status: Boolean,
        var categoria: Categoria,
) {
}