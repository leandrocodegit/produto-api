package com.api.produto.repository


import com.api.produto.model.Deposito
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface DepositoRespository: JpaRepository<Deposito, Long>{

    fun findByIdAndProdutoCodigo(id: Long, codigo: String): Optional<Deposito>
    fun findAllByProdutoCodigo(codigo: String): List<Deposito>
}

