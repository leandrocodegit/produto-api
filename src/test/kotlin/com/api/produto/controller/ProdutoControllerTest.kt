package com.api.produto.controller

import com.api.produto.build.ProdutoBuild
import com.api.produto.build.URL_PRODUTO
import com.api.produto.build.URL_PRODUTO_CODIGO
import com.api.produto.repository.ProdutoRepository
import com.api.produto.service.ProdutoService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
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
}