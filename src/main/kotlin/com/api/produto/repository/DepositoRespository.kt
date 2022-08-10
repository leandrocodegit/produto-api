package com.api.produto.repository


import com.api.produto.model.Deposito
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DepositoRespository: JpaRepository<Deposito, Long>