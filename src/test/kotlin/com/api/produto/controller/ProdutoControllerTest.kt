package com.api.produto.controller

import com.api.produto.build.*
import com.api.produto.controller.request.VendaRequest
import com.api.produto.mapper.ProdutoMapper
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

@ConfiguracaoPadraoController
class ProdutoControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc
    @Autowired
    private lateinit var produtoSevice: ProdutoService
    @MockkBean
    private lateinit var produtoRepository: ProdutoRepository
    @MockkBean
    private lateinit var estoqueRepository: EstoqueRepository
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
                .andExpect(jsonPath("$.estoque.id").value(1))
                .andExpect(jsonPath("$.estoque.depositos.size()").value(2))
                .andExpect(jsonPath("$.status").value(true))
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
    fun `test criar produto com campos obrigatorios invalidos`() {

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

        val body = ObjectMapper().writeValueAsString(produtoRequest)

        mockMvc.perform(post(URL_PRODUTO)
                .contentType(APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.containsError").value(true))
    }

    @Test
    fun `test criar produto com campos obrigatorio descricacao invalido`() {

        var produtoNewRequest = ProdutoBuild.createRequest("9000")
        produtoNewRequest.descricao = ""
        val body = ObjectMapper().writeValueAsString(produtoNewRequest)


        mockMvc.perform(post(URL_PRODUTO)
                .contentType(APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.containsError").value(true))
    }
    @Test
    fun `test criar produto com campos obrigatorio unidade invalido`() {

        var produtoRequest = ProdutoBuild.createRequest("9000")
        produtoRequest.unidade = ""
        val body = ObjectMapper().writeValueAsString(produtoRequest)

        mockMvc.perform(post(URL_PRODUTO)
                .contentType(APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.containsError").value(true))
    }

    @Test
    fun `test criar produto com campos obrigatorio preco invalido`() {

        var produtoRequest = ProdutoBuild.createRequest("9000")
        produtoRequest.preco = 0.0
        val body = ObjectMapper().writeValueAsString(produtoRequest)

        mockMvc.perform(post(URL_PRODUTO)
                .contentType(APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.containsError").value(true))
    }

    @Test
    fun `test criar produto com campos obrigatorio precoCusto invalido`() {

        var produtoRequest = ProdutoBuild.createRequest("9000")
        produtoRequest.precoCusto = 0.0
        val body = ObjectMapper().writeValueAsString(produtoRequest)

        mockMvc.perform(post(URL_PRODUTO)
                .contentType(APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.containsError").value(true))
    }

    @Test
    fun `test criar produto com campos obrigatorio pesoLiq invalido`() {

        var produtoRequest = ProdutoBuild.createRequest("9000")
        produtoRequest.pesoLiq = 0.0
        val body = ObjectMapper().writeValueAsString(produtoRequest)

        mockMvc.perform(post(URL_PRODUTO)
                .contentType(APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.containsError").value(true))
    }

    @Test
    fun `test criar produto com campos obrigatorio pesoBurto invalido`() {

        var produtoRequest = ProdutoBuild.createRequest("9000")
        produtoRequest.pesoBruto = 0.0
        var produto = ProdutoBuild.produto(produtoRequest.codigo)
        val body = ObjectMapper().writeValueAsString(produtoRequest)

        mockMvc.perform(post(URL_PRODUTO)
                .contentType(APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.containsError").value(true))
    }

    @Test
    fun `test criar produto com campos obrigatorio estoqueMinimo invalido`() {

        var produtoRequest = ProdutoBuild.createRequest("9000")
        produtoRequest.estoqueMinimo = 0
        val body = ObjectMapper().writeValueAsString(produtoRequest)

        mockMvc.perform(post(URL_PRODUTO)
                .contentType(APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.containsError").value(true))
    }
    @Test
    fun `test criar produto com campos obrigatorio estoqueMaximo invalido`() {

        var produtoRequest = ProdutoBuild.createRequest("9000")
        produtoRequest.estoqueMaximo = 0
        val body = ObjectMapper().writeValueAsString(produtoRequest)

        mockMvc.perform(post(URL_PRODUTO)
                .contentType(APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.containsError").value(true))
    }

    @Test
    fun `test criar produto com campos obrigatorio descricaoCurta invalido`() {

        var produtoRequest = ProdutoBuild.createRequest("9000")
        produtoRequest.descricaoCurta = ""
        val body = ObjectMapper().writeValueAsString(produtoRequest)

        mockMvc.perform(post(URL_PRODUTO)
                .contentType(APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.containsError").value(true))
    }

    @Test
    fun `test criar produto com campos obrigatorios largura invalido`() {

        var produtoRequest = ProdutoBuild.createRequest("9000")
        produtoRequest.largura = 0
        val body = ObjectMapper().writeValueAsString(produtoRequest)

        mockMvc.perform(post(URL_PRODUTO)
                .contentType(APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.containsError").value(true))
    }

    @Test
    fun `test criar produto com campos obrigatorio altura invalido`() {

        var produtoRequest = ProdutoBuild.createRequest("9000")
        produtoRequest.altura = 0
        val body = ObjectMapper().writeValueAsString(produtoRequest)

        mockMvc.perform(post(URL_PRODUTO)
                .contentType(APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.containsError").value(true))
    }

    @Test
    fun `test criar produto com campos obrigatorio profundidade invalido`() {

        var produtoRequest = ProdutoBuild.createRequest("9000")
        produtoRequest.profundidade = 0
        val body = ObjectMapper().writeValueAsString(produtoRequest)

        mockMvc.perform(post(URL_PRODUTO)
                .contentType(APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.containsError").value(true))
    }

    @Test
    fun `test criar produto com campos obrigatorio unidadeMedida invalido`() {

        var produtoRequest = ProdutoBuild.createRequest("9000")
        produtoRequest.unidadeMedida = ""
        val body = ObjectMapper().writeValueAsString(produtoRequest)

        mockMvc.perform(post(URL_PRODUTO)
                .contentType(APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.containsError").value(true))
    }

    @Test
    fun `test criar produto com campos obrigatorio marca invalido`() {

        var produtoRequest = ProdutoBuild.createRequest("9000")
        produtoRequest.marca = ""
        val body = ObjectMapper().writeValueAsString(produtoRequest)

        mockMvc.perform(post(URL_PRODUTO)
                .contentType(APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.containsError").value(true))
    }

    @Test
    fun `test criar produto com campos obrigatorio itensCaixa invalido`() {

        var produtoRequest = ProdutoBuild.createRequest("9000")
        produtoRequest.itensPorCaixa = 0
        val body = ObjectMapper().writeValueAsString(produtoRequest)

        mockMvc.perform(post(URL_PRODUTO)
                .contentType(APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.containsError").value(true))
    }

    @Test
    fun `test criar produto com campos obrigatorio freteGratis invalido`() {

        var produtoRequest = ProdutoBuild.createRequest("9000")
        produtoRequest.freteGratis = ""
        val body = ObjectMapper().writeValueAsString(produtoRequest)

        mockMvc.perform(post(URL_PRODUTO)
                .contentType(APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.containsError").value(true))
    }

    @Test
    fun `test muda status do produto ativo`(){

        var produto = ProdutoBuild.produto("8500")
        produto.status = true

        every { produtoRepository.findById(produto.codigo) } returns  Optional.of(produto)
        every { produtoRepository.save(produto) } returns produto

        mockMvc.perform(get(URL_PRODUTO_STATUS_CODIGO, produto.codigo))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.status").value(false))
    }

    @Test
    fun `test muda status do produto inativo`(){

        var produto = ProdutoBuild.produto("8500")
        produto.status = false

        every { produtoRepository.findById(produto.codigo) } returns  Optional.of(produto)
        every { produtoRepository.save(produto) } returns produto

        mockMvc.perform(get(URL_PRODUTO_STATUS_CODIGO, produto.codigo))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.status").value(true))
    }

    @Test
    fun `test update produto valido`(){

        var produtoRequest = ProdutoBuild.createRequest("8500")
        produtoRequest.descricao = "Descricao alterada com sucesso"
        var produto = ProdutoBuild.produto("8500")
        val body = ObjectMapper().writeValueAsString(produtoRequest)

        every { produtoRepository.findById(produto.codigo) } returns  Optional.of(produto)
        every { produtoRepository.save(produto) } returns produto

        mockMvc.perform(patch(URL_PRODUTO)
                .contentType(APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.descricao").value("Descricao alterada com sucesso"))
                .andExpect(jsonPath("$.categoria.id").value(1))
                .andExpect(jsonPath("$.imagens.size()").value(1))
    }

    @Test
    fun `test update produto invalido`(){

        var produtoRequest = ProdutoBuild.createRequest("8700")
        produtoRequest.descricao = ""
        var produto = ProdutoBuild.produto("8700")
        val body = ObjectMapper().writeValueAsString(produtoRequest)

        every { produtoRepository.findById(produto.codigo) } returns  Optional.of(produto)

        mockMvc.perform(patch(URL_PRODUTO)
                .contentType(APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.containsError").value(true))
    }


    @Test
    fun `test update produto nao cadastrado`(){

        var produtoRequest = ProdutoBuild.createRequest("8800")
        val body = ObjectMapper().writeValueAsString(produtoRequest)

        every { produtoRepository.findById(produtoRequest.codigo) } returns  Optional.empty()

        mockMvc.perform(patch(URL_PRODUTO)
                .contentType(APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.containsError").value(true))
    }

    @Test
    fun `test delete produto`(){

        val produto = ProdutoBuild.produto("8900")

        every { produtoRepository.findById(produto.codigo) } returns  Optional.of(produto)
        every { produtoRepository.delete(produto) } returns Unit

        mockMvc.perform(delete(URL_PRODUTO_CODIGO, produto.codigo))
                .andExpect(status().isOk)
    }

    @Test
    fun `test delete produto nao cadastrado`(){

        val produto = ProdutoBuild.produto("8901")

        every { produtoRepository.findById(produto.codigo) } returns  Optional.empty()

        mockMvc.perform(delete(URL_PRODUTO_CODIGO, produto.codigo))
                .andExpect(status().isBadRequest)
    }



}