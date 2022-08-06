package com.api.prdouto.repository

import com.api.prdouto.model.Produto
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProdutoRepository: JpaRepository<Produto, String>