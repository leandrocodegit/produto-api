package com.api.produto.controller

import com.api.produto.controller.request.VendaDepositoRequest
import com.api.produto.controller.request.VendaRequest
import com.api.produto.mapper.ProdutoMapper
import com.api.produto.service.DepositoService
import com.api.produto.service.EstoqueService
import com.api.produto.service.ProdutoService
import io.swagger.annotations.Api
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/venda")
@Api(tags= ["Endpoints venda"], description = ":")
class VendaController(
        private val produtoService: ProdutoService,
        private val depositoService: DepositoService,
        private val mapper: ProdutoMapper
) {

    @PostMapping
    fun registarVendaProduto(@RequestBody @Valid venda: VendaRequest) =
            ResponseEntity.ok(mapper.toResponse(produtoService.vendaProduto(
                    venda.codigo,
                    venda.quantidade
            )))

    @PatchMapping
    fun debitarVendaDeposito(@RequestBody @Valid venda: VendaDepositoRequest) =
            ResponseEntity.ok(mapper.toResponse(depositoService.decrementarSaldo(venda)))


}