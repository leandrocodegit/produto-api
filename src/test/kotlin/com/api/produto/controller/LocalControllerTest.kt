package com.api.produto.controller

import com.api.produto.build.ConfiguracaoPadraoController
import com.api.produto.build.URL_LOCAL
import com.api.produto.build.URL_LOCAL_ID
import com.api.produto.controller.request.LocalRequest
import com.api.produto.mapper.ProdutoMapper
import com.api.produto.model.Local
import com.api.produto.repository.LocalRepository
import com.api.produto.service.LocalService
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import org.junit.jupiter.api.Test
import org.mapstruct.factory.Mappers
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.MediaType.*
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

@ConfiguracaoPadraoController
class LocalControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc
    @Autowired
    private lateinit var localService: LocalService
    @MockkBean
    private lateinit var localRepository: LocalRepository
    private val mapper = Mappers.getMapper(ProdutoMapper::class.java)

    @Test
    fun `test list todos locais`(){

        every { localRepository.findAll() } returns listOf(Local(1),Local(2))

        mockMvc.perform(get(URL_LOCAL)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.size()").value(2))

        every {
            localRepository.findAll()
        }
    }

    @Test
    fun `test busca local por id`(){

        every { localRepository.findById(1L) } returns Optional.of(Local(1L, "principal","principal"))

        mockMvc.perform(get(URL_LOCAL_ID, 1L)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("principal"))
                .andExpect(jsonPath("$.endereco").value("principal"))

        every {
            localRepository.findById(1L)
        }
    }

    @Test
    fun `test busca local por id nao cadastrado`(){

        every { localRepository.findById(1L) } returns Optional.empty()

        mockMvc.perform(get(URL_LOCAL_ID, 1L)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.containsError").value(true))

        every {
            localRepository.findById(1L)
        }
    }

    @Test
    fun `test criar novo local`(){

        var localRequest = LocalRequest("novo","novo")
        var body = ObjectMapper().writeValueAsString(localRequest)

        every { localRepository.findById(0) } returns Optional.empty()
        every { localRepository.save(any()) } returns mapper.toEntity(localRequest)

        mockMvc.perform(post(URL_LOCAL)
                .contentType(APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.nome").value("novo"))
                .andExpect(jsonPath("$.endereco").value("novo"))

        every {
            localRepository.findById(0)
            localRepository.save(any())
        }
    }

    @Test
    fun `test criar novo local ja cadastrado`(){

        var localRequest = LocalRequest(1,"novo","novo")
        var body = ObjectMapper().writeValueAsString(localRequest)

        every { localRepository.findById(1) } returns Optional.of( mapper.toEntity(localRequest))

        mockMvc.perform(post(URL_LOCAL)
                .contentType(APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.containsError").value(true))

        every {
            localRepository.findById(0)
            localRepository.save(any())
        }
    }

    @Test
    fun `test criar novo local com endereco vazio`(){

        var localRequest = LocalRequest("novo","")
        var body = ObjectMapper().writeValueAsString(localRequest)


        mockMvc.perform(post(URL_LOCAL)
                .contentType(APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.containsError").value(true))
    }

    @Test
    fun `test criar novo local com nome e endereco vazio`(){

        var localRequest = LocalRequest("","")
        var body = ObjectMapper().writeValueAsString(localRequest)


        mockMvc.perform(post(URL_LOCAL)
                .contentType(APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.containsError").value(true))

    }

    @Test
    fun `test criar novo local com nome vazio`(){

        var localRequest = LocalRequest("","novo")
        var body = ObjectMapper().writeValueAsString(localRequest)

        mockMvc.perform(post(URL_LOCAL)
                .contentType(APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.containsError").value(true))

    }

    @Test
    fun `test atualizar local cadastrado`(){

        var localRequest = LocalRequest(1,"atualizado","atualizado")
        var body = ObjectMapper().writeValueAsString(localRequest)

        every { localRepository.findById(1) } returns Optional.of(mapper.toEntity(localRequest))
        every { localRepository.save(any()) } returns mapper.toEntity(localRequest)

        mockMvc.perform(patch(URL_LOCAL)
                .contentType(APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("atualizado"))
                .andExpect(jsonPath("$.endereco").value("atualizado"))

        every {
            localRepository.findById(1)
            localRepository.save(any())
        }
    }

    @Test
    fun `test atualizar local nao cadastrado`(){

        var localRequest = LocalRequest(1,"atualizado","atualizado")
        var body = ObjectMapper().writeValueAsString(localRequest)

        every { localRepository.findById(1) } returns Optional.empty()

        mockMvc.perform(patch(URL_LOCAL)
                .contentType(APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.containsError").value(true))

        every {
            localRepository.findById(1)
        }
    }

    @Test
    fun `test atualizar local cadastrado com nome vazio`(){

        var localRequest = LocalRequest(1,"","atualizado")
        var body = ObjectMapper().writeValueAsString(localRequest)

        mockMvc.perform(patch(URL_LOCAL)
                .contentType(APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.containsError").value(true))

        every {
            localRepository.findById(1)
            localRepository.save(any())
        }
    }

    @Test
    fun `test atualizar local cadastrado com nome e endereco vazio`(){

        var localRequest = LocalRequest(1,"","")
        var body = ObjectMapper().writeValueAsString(localRequest)

        mockMvc.perform(patch(URL_LOCAL)
                .contentType(APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.containsError").value(true))

        every {
            localRepository.findById(1)
            localRepository.save(any())
        }
    }

    @Test
    fun `test atualizar local cadastrado com endereco vazio`(){

        var localRequest = LocalRequest(1,"atualizado","")
        var body = ObjectMapper().writeValueAsString(localRequest)

        mockMvc.perform(patch(URL_LOCAL)
                .contentType(APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.containsError").value(true))

        every {
            localRepository.findById(1)
            localRepository.save(any())
        }
    }

    @Test
    fun `test delete local cadastrado`(){

        var localRequest = LocalRequest(1)
        var body = ObjectMapper().writeValueAsString(localRequest)

        every { localRepository.findById(1) } returns Optional.of(mapper.toEntity(localRequest))
        every { localRepository.deleteById(1) } returns Unit

        mockMvc.perform(delete(URL_LOCAL)
                .contentType(APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk)

        every {
            localRepository.findById(1)
            localRepository.deleteById(1)
        }
    }

    @Test
    fun `test delete local nao cadastrado`(){

        var localRequest = LocalRequest(1)
        var body = ObjectMapper().writeValueAsString(localRequest)

        every { localRepository.findById(1) } returns Optional.empty()

        mockMvc.perform(delete(URL_LOCAL)
                .contentType(APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.containsError").value(true))

        every {
            localRepository.findById(1)
        }
    }



}