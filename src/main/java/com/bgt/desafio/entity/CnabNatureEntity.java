package com.bgt.desafio.entity;

import com.bgt.desafio.constants.InputOutput;
import com.bgt.desafio.constants.Signal;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "NATURE")
public class CnabNatureEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private InputOutput inputOutput;

    @Column(name = "signal_type", nullable = false)
    private Signal signal;

    @OneToMany(mappedBy = "nature", cascade = CascadeType.ALL)
    private List<CnabTransactionEntity> transactions;

}
