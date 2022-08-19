package com.api.produto.controller

import com.api.produto.build.*
import com.api.produto.controller.request.DepositoLocalRequest
import com.api.produto.controller.request.DepositoRequest
import com.api.produto.controller.request.LocalRequest
import com.api.produto.controller.request.VendaDepositoRequest
import com.api.produto.model.Deposito
import com.api.produto.model.Local
import com.api.produto.model.Produto
import com.api.produto.repository.DepositoRespository
import com.api.produto.repository.EstoqueRepository
import com.api.produto.repository.LocalRepository
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
    @MockkBean
    private lateinit var estoqueRepository: EstoqueRepository
    @MockkBean
    private lateinit var localRepository: LocalRepository

    @Test
    fun `test lista todos depositos pelo codigo produto`(){

        var produto = ProdutoBuild.produto("1000", 100,150)

        every { depositoRepository.findAllByProdutoCodigo(produto.codigo) } returns produto.estoque.depositos

        val perform = mockMvc.perform(get(URL_DEPOSITO_FILTRO, produto.codigo)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].saldo").value(100))
                .andExpect(jsonPath("$[1].saldo").value(150))
    }

    @Test
    fun `test lista todos depositos pelo codigo produto inexistente`(){

        var produto = ProdutoBuild.produto("10001", 100,150)

        every { depositoRepository.findAllByProdutoCodigo(produto.codigo) } returns listOf()

        val perform = mockMvc.perform(get(URL_DEPOSITO_FILTRO, produto.codigo)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk)
    }

    @Test
    fun `test busca deposito por id`(){

        var deposito = Deposito(1L, 123,Local(1L), "9001")

        every { depositoRepository.findByIdAndProdutoCodigo(1L, "9001") } returns Optional.of(deposito)

        val perform = mockMvc.perform(get(URL_DEPOSITO_ID, "9001", deposito.id)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.saldo").value(123))
    }

    @Test
    fun `test criar novo deposito valido`(){

        var produto = ProdutoBuild.produto("2000", 100,150)
        var depositoRequest = DepositoRequest(produto.codigo, 123, LocalRequest(1L))
        var deposito = Deposito(3L,  123, Local(1L), produto.codigo)
        val body = ObjectMapper().writeValueAsString(depositoRequest)

        every { produtoRepository.findById(produto.codigo) } returns Optional.of(produto)
        every { estoqueRepository.save(produto.estoque) } returns produto.estoque
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

    @Test
    fun `test atualiza saldo deposito`(){

        var produto = ProdutoBuild.produto("7050")
        var deposito1 = Deposito(1L, 100, Local(1),produto.codigo)
        var deposito2 = Deposito(2L, 150, Local(1),produto.codigo)
        produto.estoque.depositos = mutableListOf(
                deposito1,
                deposito2)

        var depositoRequest = DepositoRequest(1L,produto.codigo, 123, LocalRequest(1L))
        val body = ObjectMapper().writeValueAsString(depositoRequest)

        every { produtoRepository.findById(produto.codigo) } returns Optional.of(produto)
        every { estoqueRepository.save(produto.estoque) } returns produto.estoque
        every { depositoRepository.findByIdAndProdutoCodigo(deposito1.id, produto.codigo) } returns Optional.of(deposito1)
        every { depositoRepository.save(deposito1) } returns deposito1

        mockMvc.perform(patch(URL_DEPOSITO)
                .contentType(APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk)
                .andExpect((jsonPath("$.estoqueAtual").value(273)))
    }

    @Test
    fun `test delete deposito`(){

        var produto = ProdutoBuild.produto("7060")
        var deposito1 = Deposito(1L, 100, Local(1),produto.codigo)
        var deposito2 = Deposito(2L, 150, Local(1),produto.codigo)
        produto.estoque.depositos = mutableListOf(
                deposito1,
                deposito2)


        every { depositoRepository.findAllByProdutoCodigo( produto.codigo) } returns produto.estoque.depositos
        every { produtoRepository.findById(produto.codigo) } returns Optional.of(produto.apply { estoque.depositos = mutableListOf(deposito2) })
        every { estoqueRepository.save(produto.estoque) } returns produto.estoque
        every { depositoRepository.findByIdAndProdutoCodigo(deposito1.id, produto.codigo) } returns Optional.of(deposito1)
        every { depositoRepository.deleteById(deposito1.id) } returns Unit

        mockMvc.perform(delete(URL_DEPOSITO_ID, produto.codigo, deposito1.id)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect((jsonPath("$.depositos.size()").value(1)))
                .andExpect((jsonPath("$.estoqueAtual").value(150)))
    }

    @Test
    fun `test alterar local deposito`(){

        var deposito1 = Deposito(1L, 100, Local(1),"6000")
        var depositoLocalRequest = DepositoLocalRequest(1L,"6000", 2L)
        var body = ObjectMapper().writeValueAsString(depositoLocalRequest)


        every { localRepository.findById(2L) } returns Optional.of(Local(2L))
        every { depositoRepository.save(deposito1) } returns deposito1
        every { depositoRepository.findByIdAndProdutoCodigo(deposito1.id,"6000") } returns Optional.of(deposito1)

        mockMvc.perform(put(URL_DEPOSITO)
                .contentType(APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk)
                .andExpect((jsonPath("$.local.id").value(2L)))
                .andExpect((jsonPath("$.saldo").value(100)))
    }


}