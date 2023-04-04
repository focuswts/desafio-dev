package com.bgt.desafio.dto;

import com.bgt.desafio.constants.InputOutput;
import com.bgt.desafio.constants.Signal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CnabTransaction {

    private String type;

    private LocalDate date;

    private BigDecimal value;

    private String cpf;

    private String card;

    private LocalTime hour;

    private String storeOwner;

    private String storeName;

    private String nature;

    private InputOutput inputOutput;

    private Signal signal;

    public CnabTransaction(String line) {
        this.type = line.substring(0, 1);
        this.date = LocalDate.parse(line.substring(1, 9), DateTimeFormatter.ofPattern("yyyyMMdd"));
        this.value = new BigDecimal(line.substring(9, 19)).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        this.cpf = line.substring(19, 30);
        this.card = line.substring(30, 42);
        this.hour = LocalTime.parse(line.substring(42, 48), DateTimeFormatter.ofPattern("HHmmss"));
        this.storeOwner = line.substring(48, 62).trim();
        this.storeName = line.substring(62, 80).trim();
    }

}
