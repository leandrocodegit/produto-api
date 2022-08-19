package com.api.produto.controller

import com.api.produto.controller.request.LocalRequest
import com.api.produto.controller.valid.OnCreate
import com.api.produto.controller.valid.OnUpdate
import com.api.produto.mapper.ProdutoMapper
import com.api.produto.service.LocalService
import io.swagger.annotations.Api
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/local")
@Api(tags= ["Endpoints locais"], description = ":")
class LocalController(
        private val localService: LocalService,
        private val mapper: ProdutoMapper
) {

    @GetMapping
    fun listAllLocal() = ResponseEntity.ok(localService.listAllLocais().map { mapper.toResponse(it) }.toList())

    @GetMapping("/{id}")
    fun buscaLocalPorID(@PathVariable id:Long) = ResponseEntity.ok(mapper.toResponse(localService.buscaLocalPorId(id)))

    @PostMapping
    fun criarNovoLocal(@RequestBody @Validated(OnCreate::class) localRequest: LocalRequest) =
            ResponseEntity.ok(mapper.toResponse(
                    localService.createNovoLocal(mapper.toEntity(localRequest))))

    @PatchMapping
    fun atualizarLocal(@RequestBody @Validated(OnUpdate::class,OnCreate::class)  localRequest: LocalRequest) =
            ResponseEntity.ok(mapper.toResponse(
                    localService.atualizaLocal(mapper.toEntity(localRequest))))

    @DeleteMapping
    fun deleteLocal(@RequestBody @Validated(OnUpdate::class) localRequest: LocalRequest) = ResponseEntity.ok(localService.deleteLocal(localRequest.id))

}