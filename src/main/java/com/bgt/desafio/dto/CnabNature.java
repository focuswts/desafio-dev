package com.bgt.desafio.dto;

import com.bgt.desafio.constants.InputOutput;
import com.bgt.desafio.constants.Signal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CnabNature {

    private String description;

    private InputOutput inputOutput;

    private Signal signal;

}
