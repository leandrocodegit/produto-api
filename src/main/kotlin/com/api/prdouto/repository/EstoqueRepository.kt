package com.api.prdouto.repository

import com.api.prdouto.model.Deposito
import com.api.prdouto.model.Estoque
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EstoqueRepository: JpaRepository<Estoque, Long>