package com.api.produto.service

import com.api.produto.enuns.CodeError
import com.api.produto.exceptions.EntityResponseException
import com.api.produto.mapper.ProdutoMapper
import com.api.produto.model.Local
import com.api.produto.repository.LocalRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class LocalService(
        private val localRepository: LocalRepository
) {

    @Autowired
    private lateinit var mapper: ProdutoMapper

    fun listAllLocais() = localRepository.findAll()

    fun buscaLocalPorId(id: Long) = localRepository.findById(id).orElseThrow{
        throw EntityResponseException("Local com id $id nao encontrado", CodeError.NOT_FOUND)
    }
    fun atualizaLocal(local: Local): Local{
        local.apply {
            id = buscaLocalPorId(local.id).id
        }
        return localRepository.save(local)
    }

    fun createNovoLocal(local: Local): Local{
       if(localRepository.findById(local.id).isEmpty.not())
           throw EntityResponseException("Local com id ${local.id} ja cadastrado", CodeError.DUPLICATE)
        return localRepository.save(local)
    }

    fun deleteLocal(id: Long){
        localRepository.deleteById(buscaLocalPorId(id).id)
    }
}