package com.api.prdouto.repository

import com.api.prdouto.model.Deposito
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DepositoRespository: JpaRepository<Deposito, Long>