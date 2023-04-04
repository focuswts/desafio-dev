package com.bgt.desafio.repository;

import com.bgt.desafio.entity.CnabTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CnabTransactionRepository extends JpaRepository<CnabTransactionEntity, Long> {
}
