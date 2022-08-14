package com.api.produto.controller

import com.api.produto.build.ProdutoBuild
import com.api.produto.build.URL_PRODUTO
import com.api.produto.build.URL_PRODUTO_CODIGO
import com.api.produto.mapper.ProdutoMapper
import com.api.produto.repository.ProdutoRepository
import com.api.produto.service.ProdutoService
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mapstruct.factory.Mappers
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.*
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import java.util.*

@ExtendWith(MockKExtension::class)
@AutoConfigureMockMvc
@EnableWebMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProdutoControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc
    @Autowired
    private lateinit var produtoSevice: ProdutoService
    @MockkBean
    private lateinit var produtoRepository: ProdutoRepository
    private val mapper = Mappers.getMapper(ProdutoMapper::class.java)


    @Test
    fun `test find produto by codigo`(){

        var produto = ProdutoBuild.produto()
        every { produtoRepository.findById(produto.codigo) } returns Optional.of(produto)

        mockMvc.perform(
                get(URL_PRODUTO_CODIGO, produto.codigo)
                .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.codigo").value(produto.codigo))
    }

    @Test
    fun `test find all list produtos`(){

        every { produtoRepository.findAll() } returns listOf(ProdutoBuild.produto("1001"), ProdutoBuild.produto("1002"))
        mockMvc.perform(get(URL_PRODUTO)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$[0].codigo").value("1001"))
                .andExpect(jsonPath("$[1].codigo").value("1002"))

    }

    @Test
    fun `test criar produto valido`() {

        var produtoRequest = ProdutoBuild.createRequest("9000")
        var produto = ProdutoBuild.produto(produtoRequest.codigo)
        val body = ObjectMapper().writeValueAsString(produtoRequest)

        every { produtoRepository.findById(produto.codigo) } returns Optional.empty()
        every { produtoRepository.save(any()) } returns produto

        mockMvc.perform(post(URL_PRODUTO)
                .contentType(APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.codigo").value(produto.codigo))
    }

    @Test
    fun `test criar produto valido já cadastrado`() {

        var produtoRequest = ProdutoBuild.createRequest("9000")
        var produto = ProdutoBuild.produto(produtoRequest.codigo)
        val body = ObjectMapper().writeValueAsString(produtoRequest)

        every { produtoRepository.findById(produto.codigo) } returns Optional.of(produto)

        mockMvc.perform(post(URL_PRODUTO)
                .contentType(APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.containsError").value(true))
    }

    @Test
    fun `test criar produto com campos obrigatorios invalido`() {

        var produtoRequest = ProdutoBuild.createRequest("")
        produtoRequest.tipo = ""
        produtoRequest.descricao = ""
        produtoRequest.unidade = ""
        produtoRequest.preco = 0.0
        produtoRequest.precoCusto = 0.0
        produtoRequest.pesoLiq = 0.0
        produtoRequest.pesoBruto = 0.0
        produtoRequest.estoqueMinimo = 0
        produtoRequest.estoqueMaximo = 0
        produtoRequest.descricaoCurta = ""
        produtoRequest.largura = 0
        produtoRequest.altura = 0
        produtoRequest.profundidade = 0
        produtoRequest.unidadeMedida = ""
        produtoRequest.marca = ""
        produtoRequest.itensPorCaixa = 0
        produtoRequest.garantia = 0
        produtoRequest.freteGratis = ""

        var produto = ProdutoBuild.produto(produtoRequest.codigo)
        val body = ObjectMapper().writeValueAsString(produtoRequest)

        every { produtoRepository.findById(produto.codigo) } returns Optional.of(produto)
        every { produtoRepository.save(any()) } returns produto

        mockMvc.perform(post(URL_PRODUTO)
                .contentType(APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.containsError").value(true))
    }


}