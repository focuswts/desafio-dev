package com.bgt.desafio.service;

import com.bgt.desafio.configuration.CnabTransactionDTOToCnabTransactionEntityConverter;
import com.bgt.desafio.constants.InputOutput;
import com.bgt.desafio.constants.Signal;
import com.bgt.desafio.dto.CnabNature;
import com.bgt.desafio.dto.StoreDTO;
import com.bgt.desafio.entity.CnabNatureEntity;
import com.bgt.desafio.entity.CnabTransactionEntity;
import com.bgt.desafio.repository.CnabTransactionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CnabProcessingServiceTest {

    @Mock
    private CnabTransactionRepository transactionRepository;

    @Mock
    private CnabTransactionDTOToCnabTransactionEntityConverter converter;

    @InjectMocks
    private CnabProcessingService cnabProcessingService;

    @Mock
    private MultipartFile multipartFile;

    @Mock
    private Model model;

    @Test
    public void testCreateNaturesMap() {
        Map<String, CnabNature> naturesByType = new HashMap<>();
        CnabProcessingService.createNaturesMap(naturesByType);

        assertEquals(naturesByType.size(), 9);
        assertNotNull(naturesByType.get("1"));
        assertNotNull(naturesByType.get("2"));
        assertNotNull(naturesByType.get("3"));
        assertNotNull(naturesByType.get("4"));
        assertNotNull(naturesByType.get("5"));
        assertNotNull(naturesByType.get("6"));
        assertNotNull(naturesByType.get("7"));
        assertNotNull(naturesByType.get("8"));
        assertNotNull(naturesByType.get("9"));
    }


    @Test
    public void testGetAllTransactionsByStoreName() {
        List<CnabTransactionEntity> allTransactions = new ArrayList<>();

        List<CnabTransactionEntity> storeTransactions = new ArrayList<>();

        CnabTransactionEntity transaction1 = new CnabTransactionEntity();
        transaction1.setStoreName("Store 1");
        transaction1.setValue(BigDecimal.TEN);
        transaction1.setNature(new CnabNatureEntity(1L, "Débito", InputOutput.SAIDA, Signal.POSITIVO, storeTransactions));
        allTransactions.add(transaction1);

        CnabTransactionEntity transaction2 = new CnabTransactionEntity();
        transaction2.setStoreName("Store 1");
        transaction2.setValue(BigDecimal.ONE);
        transaction2.setNature(new CnabNatureEntity(1L, "Crédito", InputOutput.ENTRADA, Signal.POSITIVO, storeTransactions));
        allTransactions.add(transaction2);

        CnabTransactionEntity transaction3 = new CnabTransactionEntity();
        transaction3.setStoreName("Store 2");
        transaction3.setValue(BigDecimal.TEN);
        transaction3.setNature(new CnabNatureEntity(1L, "Débito", InputOutput.SAIDA, Signal.POSITIVO, storeTransactions));
        allTransactions.add(transaction3);

        when(transactionRepository.findAll()).thenReturn(allTransactions);
        when(transactionRepository.findAllByStoreName("Store 1")).thenReturn(Arrays.asList(transaction1, transaction2));
        when(transactionRepository.findAllByStoreName("Store 2")).thenReturn(Collections.singletonList(transaction3));

        String viewName = cnabProcessingService.getAllTransactionsByStoreName(model);

        ArgumentCaptor<List<StoreDTO>> storesDataCaptor = ArgumentCaptor.forClass(List.class);
        verify(model).addAttribute(eq("stores"), storesDataCaptor.capture());

        List<StoreDTO> storesData = storesDataCaptor.getValue();
        assertEquals(3, storesData.size());

        StoreDTO store1 = storesData.get(0);
        assertEquals("Store 1", store1.getStoreName());
        assertEquals(Arrays.asList(transaction1, transaction2), store1.getStoreTransactions());
        assertEquals(BigDecimal.valueOf(11), store1.getSaldo());

        StoreDTO store2 = storesData.get(1);
        assertEquals("Store 1", store2.getStoreName());
        assertEquals(new BigDecimal(11), store2.getSaldo());

        assertEquals("stores", viewName);
    }


}

