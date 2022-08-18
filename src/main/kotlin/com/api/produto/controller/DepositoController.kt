package com.api.produto.controller

import com.api.produto.controller.request.DepositoRequest
import com.api.produto.mapper.ProdutoMapper
import com.api.produto.service.DepositoService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid
import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities.Private

@RestController
@RequestMapping("/api/v1/deposito")
class DepositoController(
        private val depositoService: DepositoService,
        private val mapper: ProdutoMapper
) {

    @GetMapping("/{id}")
    fun buscaDeposito(@PathVariable id: Long) =
            ResponseEntity.ok(depositoService.findDepositoByID(id,""))

    @GetMapping("/filtro/{codigo}")
    fun listaDepositosPorProduto(@PathVariable codigo: String) =
//            ResponseEntity.ok(depositoService.listAllDepositosByProduto(codigo).map { mapper.toResponse(it) }.toList())
            ResponseEntity.ok(depositoService.listAllDepositosByProduto(codigo))

    @PostMapping
    fun criarNovoDeposito(@RequestBody @Valid depositoRequest: DepositoRequest) =
            ResponseEntity.ok(depositoService.criarDeposito(depositoRequest))
}