package com.api.produto.controller.response

import com.api.produto.model.Categoria
import com.api.produto.model.Estoque
import com.api.produto.model.image.Imagem
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.OneToMany
import javax.persistence.OneToOne

class ProdutoResponse(
        var codigo: String,
        var tipo: String,
        var descricao: String,
        var unidade: String,
        var preco: Double,
        var precoCusto: Double,
        var pesoLiq: Double,
        var pesoBruto: Double,
        var estoqueMinimo: Int,
        var estoqueMaximo: Int,
        var gtin: String,
        var gtinEmbalagem: String,
        var descricaoCurta: String,
        var descricaoComplementar: String,
        var largura: Int,
        var altura: Int,
        var profundidade: Int,
        var unidadeMedida: String,
        var dataInclusao: String,
        var dataAlteracao: String,
        var nomeFornecedor: String,
        var marca: String,
        var classFiscal: String,
        var cest: String,
        var origem: String,
        var idgrupo: String,
        var linkExterno: String,
        var observacoes: String,
        var grupo: String,
        var itensPorCaixa: Int,
        var volumes: Int,
        var urlVideo: String,
        var localizacao: String,
        var crossdocking: String,
        var garantia: Int,
        var condicao: String,
        var freteGratis: String,
        var producao: String,
        var dataValidade: String,
        var descricaoFornecedor: String,
        var codigopai: String,
        var status: Boolean,
        var imagens: MutableList<Imagem>?,
        var estoque: EstoqueResponse,
        var categoria: CategoriaResponse,
) {
    var imageOriginal: String? = null
        get() {
            if(field != null)
                return "/api/v1/imagem/$field"
            return ""
        }
    var imageThumbnail: String? = null
        get() {
            if(field != null)
                return "/api/v1/imagem/$field"
            return ""
        }
}
