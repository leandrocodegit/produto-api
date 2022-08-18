package com.api.produto.mapper


import com.api.produto.controller.request.*
import com.api.produto.controller.response.*
import com.api.produto.model.*
import com.api.produto.model.image.Imagem
import org.mapstruct.BeanMapping
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.Mappings
import org.mapstruct.NullValuePropertyMappingStrategy

@Mapper(componentModel = "spring")
interface ProdutoMapper {

    @Mappings(
         //  Mapping(target = "categoria", expression = "java(toResponse(produto.getCategoria()))")
    )

    fun toResponse(produto: Produto): ProdutoResponse
    fun toResponse(estoque: Estoque): EstoqueResponse
    fun toResponse(deposito: Deposito): DepositoResponse
    fun toResponse(imagem: Imagem): ImagemResponse
    fun toResponse(local: Local): LocalResponse
    @Mapping(target = "estoque", expression = "java(new Estoque())")
    fun toEntity(produtoRequest: ProdutoRequest): Produto
    fun toEntity(categoriaRequest: CategoriaRequest): Categoria
    fun toEntity(estoqueRequest: EstoqueRequest): Estoque
    fun toEntity(depositoRequest: DepositoRequest): Deposito
    fun toEntity(imagemRequest: ImagemRequest): Imagem
    fun toEntity(localRequest: LocalRequest): Local

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    fun toUpdateProduto(request: ProdutoRequest, @MappingTarget entity: Produto): Produto

}