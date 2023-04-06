package com.bgt.desafio.repository;

import com.bgt.desafio.entity.CnabTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CnabTransactionRepository extends JpaRepository<CnabTransactionEntity, Long> {

    List<CnabTransactionEntity> findAllByStoreName(String storeName);

}
