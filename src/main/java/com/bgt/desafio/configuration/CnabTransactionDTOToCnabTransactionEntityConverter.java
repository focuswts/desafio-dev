package com.bgt.desafio.configuration;

import com.bgt.desafio.dto.CnabTransaction;
import com.bgt.desafio.entity.CnabNatureEntity;
import com.bgt.desafio.entity.CnabTransactionEntity;
import com.bgt.desafio.entity.CreditCardEntity;
import org.springframework.stereotype.Component;

@Component
public class CnabTransactionDTOToCnabTransactionEntityConverter {

    public CnabTransactionEntity toEntity(CnabTransaction transaction) {
        CnabTransactionEntity cnabTransaction = new CnabTransactionEntity();
        cnabTransaction.setType(transaction.getType());
        cnabTransaction.setDate(transaction.getDate());
        cnabTransaction.setValue(transaction.getValue());
        cnabTransaction.setCpf(transaction.getCpf());

        CreditCardEntity creditCard = new CreditCardEntity();
        creditCard.setNumber(transaction.getCard());
        cnabTransaction.setCreditCard(creditCard);

        cnabTransaction.setHour(transaction.getHour());
        cnabTransaction.setStoreOwner(transaction.getStoreOwner());
        cnabTransaction.setStoreName(transaction.getStoreName());

        CnabNatureEntity nature = new CnabNatureEntity();
        nature.setDescription(transaction.getNature());
        nature.setSignal(transaction.getSignal());
        nature.setInputOutput(transaction.getInputOutput());


        cnabTransaction.setNature(nature);
        return cnabTransaction;
    }


}
