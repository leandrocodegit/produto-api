package com.api.produto.service

import com.api.produto.build.ProdutoBuild
import com.api.produto.controller.request.DepositoLocalRequest
import com.api.produto.controller.request.DepositoRequest
import com.api.produto.controller.request.LocalRequest
import com.api.produto.controller.request.VendaDepositoRequest
import com.api.produto.exceptions.EntityResponseException
import com.api.produto.mapper.ProdutoMapper
import com.api.produto.model.Deposito
import com.api.produto.model.Local
import com.api.produto.repository.DepositoRespository
import com.api.produto.repository.EstoqueRepository
import com.api.produto.repository.LocalRepository
import com.api.produto.repository.ProdutoRepository
import com.ninjasquad.springmockk.MockkBean
import com.ninjasquad.springmockk.MockkBeans
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mapstruct.factory.Mappers
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import java.util.*

@ExtendWith(MockKExtension::class)
class DepositoServiceTest {

    @InjectMockKs
    private lateinit var depositoService: DepositoService
    @InjectMockKs
    private lateinit var estoqueService: EstoqueService
    @MockK
    private lateinit var produtoRepository: ProdutoRepository
    @MockK
    private lateinit var depositoRespository: DepositoRespository
    @MockK
    private lateinit var estoqueRepository: EstoqueRepository
    @MockK
    private lateinit var localRepository: LocalRepository

    private val produtoMapper = Mappers.getMapper(ProdutoMapper::class.java)

    @BeforeEach
    fun setup(){
    }

    @Test
    fun `test busca deposito id e produto relacionado`(){

        var deposito = Deposito(1, 0, Local(1), "9090")
        every { depositoRespository.findByIdAndProdutoCodigo(deposito.id,deposito.produtoCodigo!!) } returns Optional.of(deposito)

        var depositoSave = depositoService.findDepositoByID(deposito.id, "9090")

        assertEquals(1,depositoSave.id)
        assertEquals("9090",depositoSave.produtoCodigo!!)
    }

    @Test
    fun `test busca todos depositos por produto relacionado`(){

         var produto = ProdutoBuild.produto("8080")
        var depositos = mutableListOf(
                Deposito(1,   100, Local(1L),produto.codigo),
                Deposito(2,  100, Local(2L),produto.codigo),
                Deposito(3,  100, Local(2L),produto.codigo))

        every { depositoRespository.findAllByProdutoCodigo(produto.codigo) } returns depositos

        var depositosSave = depositoService.listAllDepositosByProduto(produto.codigo)

        assertEquals(3,depositos.size)
    }

    @Test
    fun `test busca deposito id e produto relacionado inexistente`(){

        var deposito = Deposito(1, 0, Local(1), "9090")
        every { depositoRespository.findByIdAndProdutoCodigo(deposito.id,deposito.produtoCodigo!!) } returns Optional.empty()

        assertThrows<EntityResponseException> { depositoService.findDepositoByID(deposito.id, "9090") }
    }

    @Test
    fun `test criar novo deposito valido`(){

        var depositoRequest = DepositoRequest(0,"7050",100, LocalRequest(3L))
        var deposito = produtoMapper.toEntity(depositoRequest)
        var produto = ProdutoBuild.produto("7050")
        produto.estoque.depositos = mutableListOf(
                Deposito(1,   100, Local(1L),produto.codigo),
                Deposito(2,  100, Local(2L),produto.codigo))

        every { estoqueRepository.findById(produto.estoque.id) } returns Optional.of(produto.estoque)
        every { estoqueRepository.save(produto.estoque) } returns produto.estoque
        every { depositoRespository.save(any()) } returns deposito
        every { produtoRepository.findById(produto.codigo) } returns Optional.of(produto)
        every { produtoRepository.save(produto) } returns produto

        var depositoSave = depositoService.criarDeposito(depositoRequest)

        assertTrue( depositoSave.depositos.any{ it.produtoCodigo == produto.codigo})
        assertEquals( 3, depositoSave.depositos.size)
        assertEquals( 300,produto.estoque.estoqueAtual)

        every {
            estoqueRepository.findById(produto.estoque.id)
            estoqueRepository.save(produto.estoque)
            depositoRespository.save(any())
            produtoRepository.findById(produto.codigo)
            produtoRepository.save(produto)
        }
    }

    @Test
    fun `test atualiza saldo deposito`(){

        var depositoRequest = DepositoRequest(1L,"7050",123, LocalRequest(1L))

        var produto = ProdutoBuild.produto("7050")
        var deposito1 = Deposito(1L, 0, Local(1), produto.codigo)
        var deposito2 = Deposito(2L, 0, Local(1), produto.codigo)
        produto.estoque.depositos = mutableListOf(
                deposito1,
                deposito2)

        every { produtoRepository.findById(produto.codigo) } returns Optional.of(produto)
        every { depositoRespository.findByIdAndProdutoCodigo(deposito1.id, produto.codigo) } returns Optional.of(deposito1)
        every { estoqueRepository.save(any()) } returns produto.estoque
        every { depositoRespository.save(deposito1) } returns deposito1

        var depositoSave = depositoService.atualizaSaldo(depositoRequest)

        assertEquals(123, depositoSave.estoqueAtual)

        verify {
            produtoRepository.findById(produto.codigo)
            depositoRespository.findByIdAndProdutoCodigo(deposito1.id, produto.codigo)
            estoqueRepository.save(any())
            depositoRespository.save(deposito1)
        }

    }

    @Test
    fun `test atualiza saldo deposito inexistente`(){

        var depositoRequest = DepositoRequest(1,"7050",123, LocalRequest(1L))
        var deposito = Deposito(1, 0, Local(1), "7050")

        every { depositoRespository.findByIdAndProdutoCodigo(deposito.id, depositoRequest.codigoProduto) } returns Optional.empty()

        assertThrows<EntityResponseException> {  depositoService.atualizaSaldo(depositoRequest)  }

    }

    @Test
    fun `test atualiza saldo deposito de produto nao vinculado`(){

        var depositoRequest = DepositoRequest(3L,"7050",123, LocalRequest(1L))

        var produto = ProdutoBuild.produto("7050")
        var deposito1 = Deposito(1L, 0, Local(1),produto.codigo)
        var deposito2 = Deposito(2L, 0, Local(1),produto.codigo)
        produto.estoque.depositos = mutableListOf(
                deposito1,
                deposito2)

        every { depositoRespository.findByIdAndProdutoCodigo(depositoRequest.id, depositoRequest.codigoProduto) } returns Optional.empty()
        assertThrows<EntityResponseException> {  depositoService.atualizaSaldo(depositoRequest)  }

    }

    @Test
    fun `test atualiza local deposito`(){

        var deposito = Deposito(1L, 0, Local(1), "7050")
        var depositoLocalRequest = DepositoLocalRequest(1L,"6000", 3L)

        every { localRepository.findById(3L) } returns Optional.of(Local(3L))
        every { depositoRespository.findByIdAndProdutoCodigo(deposito.id,depositoLocalRequest.codigoProduto) } returns Optional.of(deposito)
        every { depositoRespository.save(deposito) } returns deposito

        assertTrue( deposito.local.id == 1L)
        var depositoSave = depositoService.atualizaLocal(depositoLocalRequest)
        assertTrue( depositoSave.local.id == 3L)

        verify {
            localRepository.findById(3L)
            depositoRespository.findByIdAndProdutoCodigo(deposito.id,depositoLocalRequest.codigoProduto)
            depositoRespository.save(deposito)
        }

    }

    @Test
    fun `test delete deposito`(){

        var produto = ProdutoBuild.produto()
        var deposito = Deposito(1, 0, Local(1),produto.codigo)

        every { depositoRespository.findAllByProdutoCodigo( produto.codigo) } returns produto.estoque.depositos
        every { produtoRepository.findById(produto.codigo) } returns Optional.of(produto)
        every { depositoRespository.findByIdAndProdutoCodigo(deposito.id, produto.codigo) } returns Optional.of(deposito)
        every { estoqueRepository.save(produto.estoque) } returns produto.estoque
        every { depositoRespository.deleteById(deposito.id) } returns Unit

        assertTrue( depositoService.deleteDeposito(deposito.id,deposito.produtoCodigo!!) != null)

        verify {
            depositoRespository.findAllByProdutoCodigo( produto.codigo)
            produtoRepository.findById(produto.codigo)
            depositoRespository.findByIdAndProdutoCodigo(deposito.id, produto.codigo)
            estoqueRepository.save(produto.estoque)
            depositoRespository.deleteById(deposito.id)
        }

    }

    @Test
    fun `test delete com apenas um deposito cadastrado`(){

        var produto = ProdutoBuild.produto()
        var deposito = Deposito(1, 0, Local(1),produto.codigo)

        every { depositoRespository.findAllByProdutoCodigo( produto.codigo) } returns listOf(deposito)

        assertThrows<EntityResponseException> { depositoService.deleteDeposito(deposito.id,deposito.produtoCodigo!!)}

    }

    @Test
    fun `test delete deposito inexistente`(){

        var deposito = Deposito(1, 0, Local(1), "8000")

        every { depositoRespository.findAllByProdutoCodigo( "8000") } returns ProdutoBuild.produto().estoque.depositos
        every { depositoRespository.findByIdAndProdutoCodigo(deposito.id, deposito.produtoCodigo!!) } returns Optional.empty()

        assertThrows<EntityResponseException> {  depositoService.deleteDeposito(deposito.id, deposito.produtoCodigo!!)}

        verify {
            depositoRespository.findAllByProdutoCodigo( "8000")
            depositoRespository.findByIdAndProdutoCodigo(deposito.id, deposito.produtoCodigo!!)
        }

    }

    @Test
    fun `test decrementar saldo deposito`(){

        var produto = ProdutoBuild.produto("7050")
        var deposito1 = Deposito(1L, 20, Local(1),produto.codigo)
        var deposito2 = Deposito(2L, 30, Local(1),produto.codigo)
        produto.estoque.reserva = 3
        produto.estoque.depositos = mutableListOf(
                deposito1,
                deposito2)
        var venda = VendaDepositoRequest(1L,produto.codigo, 3)

        every { produtoRepository.findById(produto.codigo) } returns Optional.of(produto)
        every { depositoRespository.findByIdAndProdutoCodigo(deposito1.id, produto.codigo) } returns Optional.of(deposito1)
        every { estoqueRepository.save(any()) } returns produto.estoque
        every { depositoRespository.save(deposito1) } returns deposito1

        var novoEstoque = depositoService.decrementarSaldo(venda)

        verify {
            produtoRepository.findById(produto.codigo)
            depositoRespository.findByIdAndProdutoCodigo(deposito1.id, produto.codigo)
            estoqueRepository.save(any())
            depositoRespository.save(deposito1)
        }

        assertEquals(47, novoEstoque.estoqueAtual)
        assertEquals(17, novoEstoque.depositos.first().saldo)
        assertEquals(0, novoEstoque.reserva)

    }

    @Test
    fun `test decrementar saldo deposito invalido`(){


        var venda = VendaDepositoRequest(3L, "30000", 3)

        every { depositoRespository.findByIdAndProdutoCodigo(venda.idDepostito, venda.codigoProduto) } returns Optional.empty()

        assertThrows<EntityResponseException> {  depositoService.decrementarSaldo(venda)}
    }

    @Test
    fun `test decrementar saldo deposito produto invalido`(){

        var venda = VendaDepositoRequest(1L, "3000", 3)
        var deposito = Deposito(1L, 20, Local(1),venda.codigoProduto)

        every { depositoRespository.findByIdAndProdutoCodigo(deposito.id, venda.codigoProduto) } returns Optional.of(deposito)
        every { produtoRepository.findById( venda.codigoProduto) } returns Optional.empty()

        assertThrows<EntityResponseException> {  depositoService.decrementarSaldo(venda)}
    }
}