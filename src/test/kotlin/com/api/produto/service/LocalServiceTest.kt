package com.api.produto.service

import com.api.produto.exceptions.EntityResponseException
import com.api.produto.model.Local
import com.api.produto.repository.LocalRepository
import io.mockk.Ordering
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import io.mockk.verifyAll
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import java.util.*

@ExtendWith(MockKExtension::class)
class LocalServiceTest {

    @InjectMockKs
    private lateinit var localService: LocalService
    @MockK
    private lateinit var localRepository: LocalRepository


    @Test
    fun `test list todos locais`(){

        every { localRepository.findAll() } returns listOf(Local(1), Local(2))

        var locais = localService.listAllLocais()

        verify(exactly = 1) {  localRepository.findAll() }

        assertEquals(2,locais.size)
        assertEquals(1L, locais[0].id)
        assertEquals(2L, locais[1].id)

    }

    @Test
    fun `test criar novo local`(){

        var local = Local(1,"Default","bobo")

        every { localRepository.findById(local.id) } returns Optional.empty()
        every { localRepository.save(local) } returns local


        var novoLocal = localService.createNovoLocal(local)

        verify{
            localRepository.findById(local.id)
            localRepository.save(local)
        }

        assertEquals(1,novoLocal.id)
    }

    @Test
    fun `test criar novo local ja cadastrado`(){

        var local = Local(1,"Default","bobo")

        every { localRepository.findById(local.id) } returns Optional.of(local)

        assertThrows<EntityResponseException>{localService.createNovoLocal(local)}
        verify(exactly = 1) {  localRepository.findById(local.id) }
    }

    @Test
    fun `test atualiza local cadastrado`(){

        var local = Local(1,"Default","bobo")
        var localEditado = Local(1,"atualizado","atualizado")

        every { localRepository.findById(local.id) } returns Optional.of(local)
        every { localRepository.save(any()) } returns localEditado

        var localSave = localService.atualizaLocal(localEditado)

        verify {
            localRepository.findById(local.id)
            localRepository.save(any())
        }

        assertEquals(localSave.id, local.id)
        assertEquals(localSave.nome, "atualizado")
        assertEquals(localSave.endereco, "atualizado")

    }

    @Test
    fun `test delete local cadastrado` (){

        var local = Local(1,"Default","bobo")

        every { localRepository.findById(local.id) } returns Optional.of(local)
        every { localRepository.deleteById(local.id) } returns Unit

        assertEquals(Unit, localService.deleteLocal(local.id))

        verify {
            localRepository.findById(local.id)
            localRepository.deleteById(local.id)
        }
    }

}