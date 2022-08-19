package com.api.produto.controller

import com.api.produto.build.*
import com.api.produto.controller.request.DepositoRequest
import com.api.produto.controller.request.LocalRequest
import com.api.produto.controller.request.VendaDepositoRequest
import com.api.produto.controller.request.VendaRequest
import com.api.produto.mapper.ProdutoMapper
import com.api.produto.model.Deposito
import com.api.produto.model.Local
import com.api.produto.repository.DepositoRespository
import com.api.produto.repository.EstoqueRepository
import com.api.produto.repository.ProdutoRepository
import com.api.produto.service.ProdutoService
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.mapstruct.factory.Mappers
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

@ConfiguracaoPadraoController
class VendaControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc
    @Autowired
    private lateinit var produtoSevice: ProdutoService
    @MockkBean
    private lateinit var produtoRepository: ProdutoRepository
    @MockkBean
    private lateinit var estoqueRepository: EstoqueRepository
    @MockkBean
    private lateinit var depositoRepository: DepositoRespository
    private val mapper = Mappers.getMapper(ProdutoMapper::class.java)



    @Test
    fun `venda de produto com estoque valido`(){

        var produto = ProdutoBuild.produto("5000", 10, 5)

        var vendaRequest =  VendaRequest(produto.codigo, 10)
        val body = ObjectMapper().writeValueAsString(vendaRequest)

        every { produtoRepository.findById(produto.codigo) } returns Optional.of(produto)
        every { produtoRepository.save(produto) } returns produto
        every { estoqueRepository.findById(produto.estoque.id) } returns Optional.of(produto.estoque)
        every { estoqueRepository.save(produto.estoque) } returns produto.estoque

        mockMvc.perform(post(URL_VENDA)
                .contentType(APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.estoque.estoqueAtual").value(5))
                .andExpect(jsonPath("$.estoque.reserva").value(10))
    }

    @Test
    fun `venda de produto invalido`(){

        var produto = ProdutoBuild.produto("5000", 10, 5)

        var vendaRequest =  VendaRequest(produto.codigo, 10)
        val body = ObjectMapper().writeValueAsString(vendaRequest)

        every { produtoRepository.findById(produto.codigo) } returns Optional.empty()

        mockMvc.perform(post(URL_VENDA)
                .contentType(APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest)
    }

    @Test
    fun `venda de produto com formulario invalido`(){

        var produto = ProdutoBuild.produto("5000", 10, 5)

        var vendaRequest =  VendaRequest("", -10)
        val body = ObjectMapper().writeValueAsString(vendaRequest)

        every { produtoRepository.findById(produto.codigo) } returns Optional.empty()

        mockMvc.perform(post(URL_VENDA)
                .contentType(APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest)
    }

    @Test
    fun `venda de produto com estoque insuficiente`(){

        var produto = ProdutoBuild.produto("5050", 3, 0)

        var vendaRequest =  VendaRequest(produto.codigo, 5)
        val body = ObjectMapper().writeValueAsString(vendaRequest)

        every { produtoRepository.findById(produto.codigo) } returns Optional.of(produto)
        every { estoqueRepository.findById(produto.estoque.id) } returns Optional.of(produto.estoque)

        mockMvc.perform(post(URL_VENDA)
                .contentType(APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest)
    }

    @Test
    fun `venda de produto inativo`(){

        var produto = ProdutoBuild.produto("7000", 10, 5)
        produto.status = false

        var vendaRequest =  VendaRequest(produto.codigo, 10)
        val body = ObjectMapper().writeValueAsString(vendaRequest)

        every { produtoRepository.findById(produto.codigo) } returns Optional.of(produto)

        mockMvc.perform(post(URL_VENDA)
                .contentType(APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest)
    }

    @Test
    fun `test decrementa saldo deposito`(){


        var produto = ProdutoBuild.produto("7050")
        var deposito1 = Deposito(1L, 40, Local(1),produto.codigo)
        var deposito2 = Deposito(2L, 50, Local(1),produto.codigo)
        produto.estoque.reserva = 3
        produto.estoque.depositos = mutableListOf(
                deposito1,
                deposito2)

        var venda = VendaDepositoRequest(deposito1.id, produto.codigo, 3)
        val body = ObjectMapper().writeValueAsString(venda)

        every { produtoRepository.findById(produto.codigo) } returns Optional.of(produto)
        every { estoqueRepository.save(produto.estoque) } returns produto.estoque
        every { depositoRepository.findByIdAndProdutoCodigo(deposito1.id, produto.codigo) } returns Optional.of(deposito1)
        every { depositoRepository.save(deposito1) } returns deposito1

        mockMvc.perform(patch(URL_VENDA)
                .contentType(APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk)
                .andExpect((jsonPath("$.estoqueAtual").value(87)))
                .andExpect((jsonPath("$.reserva").value(0)))
                .andExpect((jsonPath("$.depositos[0].saldo").value(37)))
                .andExpect((jsonPath("$.depositos[1].saldo").value(50)))
    }

    @Test
    fun `test decrementa saldo deposito invalido`(){

        var produto = ProdutoBuild.produto("7050")
        var deposito = Deposito(1L, 40, Local(1),produto.codigo)

        var venda = VendaDepositoRequest(deposito.id, produto.codigo, 3)
        val body = ObjectMapper().writeValueAsString(venda)

        every { depositoRepository.findByIdAndProdutoCodigo(deposito.id, produto.codigo) } returns Optional.empty()

        mockMvc.perform(patch(URL_VENDA)
                .contentType(APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest)
    }

    @Test
    fun `test decrementa saldo deposito com produto invalido`(){

        var produto = ProdutoBuild.produto("7050")
        var deposito = Deposito(1L, 40, Local(1),produto.codigo)

        var venda = VendaDepositoRequest(deposito.id, produto.codigo, 3)
        val body = ObjectMapper().writeValueAsString(venda)

        every { depositoRepository.findByIdAndProdutoCodigo(deposito.id, produto.codigo) } returns Optional.of(deposito)
        every { produtoRepository.findById(produto.codigo) } returns Optional.empty()

        mockMvc.perform(patch(URL_VENDA)
                .contentType(APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest)
    }

    @Test
    fun `test decrementa saldo deposito com quantidade negativa`(){

        var venda = VendaDepositoRequest(1L, "5000", -3)
        val body = ObjectMapper().writeValueAsString(venda)

        mockMvc.perform(patch(URL_VENDA)
                .contentType(APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest)
    }

}