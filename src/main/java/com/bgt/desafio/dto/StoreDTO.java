package com.bgt.desafio.dto;

import com.bgt.desafio.entity.CnabTransactionEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreDTO {

    String storeName;

    private List<CnabTransactionEntity> storeTransactions;

    BigDecimal saldo;

}
