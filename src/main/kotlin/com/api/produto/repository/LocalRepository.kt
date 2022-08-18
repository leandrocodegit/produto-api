package com.api.produto.repository

import com.api.produto.model.Local
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LocalRepository: JpaRepository<Local, Long>