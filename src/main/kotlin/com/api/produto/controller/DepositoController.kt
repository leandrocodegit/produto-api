package com.api.produto.controller

import com.api.produto.controller.request.DepositoLocalRequest
import com.api.produto.controller.request.DepositoRequest
import com.api.produto.mapper.ProdutoMapper
import com.api.produto.model.Local
import com.api.produto.service.DepositoService
import io.swagger.annotations.Api
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid
import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities.Private

@RestController
@RequestMapping("/api/v1/deposito")
@Api(tags= ["Endpoints depositos"], description = ":")
class DepositoController(
        private val depositoService: DepositoService,
        private val mapper: ProdutoMapper
) {

    @GetMapping("/{codigo}/{id}")
    fun buscaDeposito(@PathVariable codigo: String, @PathVariable id: Long) =
            ResponseEntity.ok(mapper.toResponse(depositoService.findDepositoByID(id,codigo)))

    @GetMapping("/{codigo}")
    fun listaDepositosPorProduto(@PathVariable codigo: String) =
            ResponseEntity.ok(depositoService.listAllDepositosByProduto(codigo).map { mapper.toResponse(it) }.toList())

    @PostMapping
    fun criarNovoDeposito(@RequestBody @Valid depositoRequest: DepositoRequest) =
            ResponseEntity.ok(mapper.toResponse(depositoService.criarDeposito(depositoRequest)))

    @PatchMapping
    fun atualizaSaldoDeposito(@RequestBody @Valid depositoRequest: DepositoRequest) =
            ResponseEntity.ok(mapper.toResponse(depositoService.atualizaSaldo(depositoRequest)))

    @PutMapping
    fun alteraLocalDeposito(@RequestBody @Valid depositoLocalRequest:  DepositoLocalRequest) =
            ResponseEntity.ok(mapper.toResponse(depositoService.atualizaLocal(depositoLocalRequest)))

    @DeleteMapping("/{codigo}/{id}")
    fun deleteDeposito(@PathVariable codigo: String, @PathVariable id: Long) =
            ResponseEntity.ok(mapper.toResponse(depositoService.deleteDeposito(id,codigo)))
}