package com.api.prdouto.service

import com.api.prdouto.enuns.CodeError
import com.api.prdouto.exceptions.EntityResponseException
import com.api.prdouto.model.Deposito
import com.api.prdouto.model.Produto
import com.api.prdouto.repository.DepositoRespository
import org.springframework.stereotype.Service

@Service
class DepositoService(
        private val depositoRespository: DepositoRespository
) {
    fun atualizaSaldo(deposito: Deposito): Deposito {
        var depositoDB = depositoRespository.findById(deposito.id).orElseThrow {
            throw EntityResponseException("Erro ao atualizar deposito", CodeError.PARAM_INVALID)
        }
        depositoDB.saldo = deposito.saldo
        return depositoRespository.save(deposito)
    }


}