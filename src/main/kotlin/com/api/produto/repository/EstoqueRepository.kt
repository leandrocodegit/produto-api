package com.api.produto.repository

import com.api.produto.model.Estoque
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EstoqueRepository: JpaRepository<Estoque, Long>