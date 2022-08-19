package com.api.produto.model

import com.api.produto.model.image.Imagem
import javax.persistence.*

@Entity
@Embeddable
class Produto(
        @Id
        var codigo: String,
        @Column(name = "tipo")
        var tipo: String,
        @Column(name = "descricao")
        var descricao: String,
        @Column(name = "unidade")
        var unidade: String,
        @Column(name = "preco")
        var preco: Double,
        @Column(name = "preco_custo")
        var precoCusto: Double,
        @Column(name = "peso_liq")
        var pesoLiq: Double,
        @Column(name = "peso_bruto")
        var pesoBruto: Double,
        @Column(name = "estoque_minimo")
        var estoqueMinimo: Int,
        @Column(name = "estoque_maximo")
        var estoqueMaximo: Int,
        @Column(name = "gtin")
        var gtin: String,
        @Column(name = "gtin_embalagem")
        var gtinEmbalagem: String,
        @Column(name = "descricao_curta")
        var descricaoCurta: String,
        @Column(name = "descricacao_complementar")
        var descricaoComplementar: String,
        @Column(name = "largura")
        var largura: Int,
        @Column(name = "altura")
        var altura: Int,
        @Column(name = "profundidade")
        var profundidade: Int,
        @Column(name = "unidade_medida")
        var unidadeMedida: String,
        @Column(name = "data_inclusao")
        var dataInclusao: String,
        @Column(name = "data_alteracao")
        var dataAlteracao: String,
        @Column(name = "nome_fornecedor")
        var nomeFornecedor: String,
        @Column(name = "marca")
        var marca: String,
        @Column(name = "clase_fiscal")
        var classFiscal: String,
        @Column(name = "cest")
        var cest: String,
        @Column(name = "origem")
        var origem: String,
        @Column(name = "idgrupo")
        var idgrupo: String,
        @Column(name = "link_externo")
        var linkExterno: String,
        @Column(name = "observacoes")
        var observacoes: String,
        @Column(name = "grupo")
        var grupo: String,
        @Column(name = "itens_por_caixa")
        var itensPorCaixa: Int,
        @Column(name = "volumes")
        var volumes: Int,
        @Column(name = "url_video")
        var urlVideo: String,
        @Column(name = "localizacao")
        var localizacao: String,
        @Column(name = "crossdocking")
        var crossdocking: String,
        @Column(name = "garantia")
        var garantia: Int,
        @Column(name = "condicao")
        var condicao: String,
        @Column(name = "frete_gratis")
        var freteGratis: String,
        @Column(name = "producao")
        var producao: String,
        @Column(name = "data_validade")
        var dataValidade: String,
        @Column(name = "descricao_fornecedor")
        var descricaoFornecedor: String,
        @Column(name = "codigopai")
        var codigopai: String,
        @Column(name = "status")
        var status: Boolean,
        @Column(name = "image_original")
        var imageOriginal: String?,
        @Column(name = "image_thumbnail")
        var imageThumbnail: String?,
        @OneToMany(cascade = [CascadeType.ALL])
        var imagens: MutableList<Imagem>?,
        @OneToOne(cascade = [CascadeType.ALL])
        var estoque: Estoque,
        @OneToOne
        var categoria: Categoria,
)