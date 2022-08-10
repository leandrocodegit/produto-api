package com.api.produto.service

import com.api.produto.enuns.CodeError
import com.api.produto.exceptions.EntityResponseException
import com.api.produto.model.Deposito
import com.api.produto.repository.DepositoRespository
import org.springframework.stereotype.Service

@Service
class DepositoService(
        private val depositoRespository: DepositoRespository
) {
    fun atualizaSaldo(deposito: Deposito): Deposito {
        var depositoDB = depositoRespository.findById(deposito.id).orElseThrow {
            throw EntityResponseException("Erro ao atualizar deposito", CodeError.PARAM_INVALID)
        }
        return depositoRespository.save(deposito)
    }


}