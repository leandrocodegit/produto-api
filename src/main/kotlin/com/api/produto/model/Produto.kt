package com.api.produto.model

import com.api.produto.model.image.Imagem
import javax.persistence.*
import javax.validation.constraints.Null

@Entity
class Produto (
        @Id
    var codigo: String,
        var tipo: String,
        var descricao: String,
        var unidade: String,
        var preco: Double,
        var precoCusto: Double,
        var pesoLiq: Double?,
        var pesoBruto: Double,
        var estoqueMinimo: Int,
        var estoqueMaximo: Int,
        var gtin: String,
        var gtinEmbalagem: String,
        var descricaoCurta: String,
        var descricaoComplementar: String,
        var larguraProduto: String,
        var alturaProduto: String,
        var profundidadeProduto: String,
        var unidadeMedida: String,
        var dataInclusao: String,
        var dataAlteracao: String,
        var nomeFornecedor: String,
        var marca: String,
        var classFiscal: String,
        var cest: String,
        var origem: String,
        var idGrupoProduto: String,
        var linkExterno: String,
        var observacoes: String,
        var grupoProduto: String,
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
        @Null
    @OneToOne(cascade = [CascadeType.ALL])
    var imageThumbnail: Imagem?,
        @OneToMany(cascade = [CascadeType.ALL])
    var imagens: List<Imagem>?,
        @OneToOne
    var estoque: Estoque,
        @OneToOne
    var categoria: Categoria,
)