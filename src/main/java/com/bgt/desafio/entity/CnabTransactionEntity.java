package com.bgt.desafio.entity;

import lombok.Data;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "CNAB_TRANSACTION")
@Data
public class CnabTransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal value;

    @Column(nullable = false)
    private String cpf;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "credit_card_id", nullable = false)
    private CreditCardEntity creditCard;

    @Column(nullable = false)
    private LocalTime hour;

    @Column(nullable = false)
    private String storeOwner;

    @Column(nullable = false)
    private String storeName;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "nature_id", nullable = false)
    private CnabNatureEntity nature;

}
