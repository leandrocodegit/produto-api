package com.api.produto.controller

import com.api.produto.build.*
import com.api.produto.controller.request.DepositoRequest
import com.api.produto.controller.request.LocalRequest
import com.api.produto.model.Deposito
import com.api.produto.model.Local
import com.api.produto.model.Produto
import com.api.produto.repository.DepositoRespository
import com.api.produto.repository.ProdutoRepository
import com.api.produto.service.DepositoService
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

@ConfiguracaoPadraoController
class DepositoControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc
    @Autowired
    private lateinit var depositoService: DepositoService
    @MockkBean
    private lateinit var produtoRepository: ProdutoRepository
    @MockkBean
    private lateinit var depositoRepository: DepositoRespository

    @Test
    fun `test lista todos depositos pelo codigo produto`(){

        var produto = ProdutoBuild.produto("1000", 100,150)

        every { produtoRepository.findById(produto.codigo) } returns Optional.of(produto)

        val perform = mockMvc.perform(get(URL_DEPOSITO_FILTRO, produto.codigo)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].saldo").value(100))
                .andExpect(jsonPath("$[1].saldo").value(150))
    }

    @Test
    fun `test busca deposito por id`(){

        var deposito = Deposito(1L, 123,Local(1L), ProdutoBuild.produto())

        every { depositoRepository.findById(deposito.id) } returns Optional.of(deposito)

        val perform = mockMvc.perform(get(URL_DEPOSITO_ID, deposito.id)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.saldo").value(123))
    }

    @Test
    fun `test criar novo deposito valido`(){

        var produto = ProdutoBuild.produto("2000", 100,150)
        var depositoRequest = DepositoRequest(produto.codigo, 123, LocalRequest(1L))
        var deposito = Deposito(3L,  123, Local(1L), ProdutoBuild.produto())
        val body = ObjectMapper().writeValueAsString(depositoRequest)

        every { produtoRepository.findById(produto.codigo) } returns Optional.of(produto)
        every { produtoRepository.save(produto) } returns produto
        every { depositoRepository.save(deposito) } returns deposito

        mockMvc.perform(post(URL_DEPOSITO)
                .contentType(APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk)
                .andExpect((jsonPath("$.estoqueAtual").value(373)))
    }

    @Test
    fun `test criar novo deposito com campo codigo produto invalido`(){

        var depositoRequest = DepositoRequest("", 123, LocalRequest(1L))
        val body = ObjectMapper().writeValueAsString(depositoRequest)

        mockMvc.perform(post(URL_DEPOSITO)
                .contentType(APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest)
                .andExpect((jsonPath("$.containsError").value(true)))
    }

    @Test
    fun `test criar novo deposito com produto inexistente`(){

        var produto = ProdutoBuild.produto("2000", 100,150)
        var depositoRequest = DepositoRequest(produto.codigo, 123, LocalRequest(1L))
        val body = ObjectMapper().writeValueAsString(depositoRequest)

        every { produtoRepository.findById(produto.codigo) } returns Optional.empty()

        mockMvc.perform(post(URL_DEPOSITO)
                .contentType(APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest)
                .andExpect((jsonPath("$.containsError").value(true)))
    }
}