package com.bgt.desafio.service;

import com.bgt.desafio.configuration.CnabTransactionDTOToCnabTransactionEntityConverter;
import com.bgt.desafio.constants.InputOutput;
import com.bgt.desafio.constants.Signal;
import com.bgt.desafio.dto.CnabNature;
import com.bgt.desafio.dto.CnabTransaction;
import com.bgt.desafio.dto.StoreDTO;
import com.bgt.desafio.entity.CnabTransactionEntity;
import com.bgt.desafio.repository.CnabTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CnabProcessingService {

    @Autowired
    private CnabTransactionRepository transactionRepository;

    @Autowired
    private CnabTransactionDTOToCnabTransactionEntityConverter converter;


    @Transactional
    public String cnabDataProcess(MultipartFile arquivo, Model model) throws IOException {

        List<CnabTransaction> transactions = new ArrayList<>();

        Map<String, CnabNature> naturesByType = new HashMap<>();
        createNaturesMap(naturesByType);

        buildTransactionsFromFile(arquivo, transactions, naturesByType);

        BigDecimal totalEntradas = getInputsTotal(transactions, InputOutput.ENTRADA);

        BigDecimal totalSaidas = getInputsTotal(transactions, InputOutput.SAIDA);

        List<CnabTransactionEntity> cnabTransactionEntities = transactionRepository.saveAll(getCnabTransactionEntityList(transactions));


        model.addAttribute("transactions", cnabTransactionEntities);
        model.addAttribute("transacoesSize", cnabTransactionEntities.size());
        model.addAttribute("totalEntradas", totalEntradas);
        model.addAttribute("totalSaidas", totalSaidas);

        return "resultado-cnab";
    }

    public static void buildTransactionsFromFile(MultipartFile arquivo, List<CnabTransaction> transactions, Map<String, CnabNature> naturesByType) throws IOException {
        try (
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(
                                arquivo.getInputStream(),
                                StandardCharsets.UTF_8)
                )
        ) {
            String line;

            while ((line = reader.readLine()) != null) {
                CnabTransaction transacao = new CnabTransaction(line);
                CnabNature nature = naturesByType.get(transacao.getType());
                if (nature != null) {
                    transacao.setNature(nature.getDescription());
                    transacao.setInputOutput(nature.getInputOutput());
                    transacao.setSignal(nature.getSignal());
                    transactions.add(transacao);
                }
            }
        }
    }

    public List<CnabTransactionEntity> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public String getAllTransactionsByStoreName(Model model) {
        List<CnabTransactionEntity> allTransactions = transactionRepository.findAll();

        List<StoreDTO> storesData = allTransactions.stream().map(
                transaction -> {
                    List<CnabTransactionEntity> allByStoreName = transactionRepository.findAllByStoreName(
                            transaction.getStoreName()
                    );

                    StoreDTO store = new StoreDTO();
                    store.setStoreName(transaction.getStoreName());
                    store.setStoreTransactions(allByStoreName);

                    store.setSaldo(this.getStorePositiveMoney(allByStoreName));

                    return store;
                }
        ).toList();

        model.addAttribute("stores", storesData);

        return "stores";
    }

    private BigDecimal getStorePositiveMoney(List<CnabTransactionEntity> allByStoreName) {

        BigDecimal allStoreInput = BigDecimal.ZERO;

        for (CnabTransactionEntity transaction : allByStoreName) {
            BigDecimal value = transaction.getValue();

            if (transaction.getNature().getSignal().equals(Signal.POSITIVO)) {
                allStoreInput = allStoreInput.add(value);
            } else {
                allStoreInput = allStoreInput.subtract(value);
            }
        }

        return allStoreInput;
    }


    public List<CnabTransactionEntity> getCnabTransactionEntityList(List<CnabTransaction> transactions) {
        return transactions.stream()
                .map(
                        cnabTransaction -> converter.toEntity(cnabTransaction)
                )
                .collect(
                        Collectors.toList()
                );
    }

    public static BigDecimal getInputsTotal(List<CnabTransaction> transactions, InputOutput entrada) {
        return transactions.stream()
                .filter(t -> t.getInputOutput() == entrada)
                .map(CnabTransaction::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static void createNaturesMap(Map<String, CnabNature> naturezasPorTipo) {
        naturezasPorTipo.put("1", new CnabNature("Débito", InputOutput.SAIDA, Signal.POSITIVO));
        naturezasPorTipo.put("2", new CnabNature("Boleto", InputOutput.SAIDA, Signal.NEGATIVO));
        naturezasPorTipo.put("3", new CnabNature("Financiamento", InputOutput.SAIDA, Signal.NEGATIVO));
        naturezasPorTipo.put("4", new CnabNature("Crédito", InputOutput.ENTRADA, Signal.POSITIVO));
        naturezasPorTipo.put("5", new CnabNature("Recebimento Empréstimo", InputOutput.ENTRADA, Signal.POSITIVO));
        naturezasPorTipo.put("6", new CnabNature("Vendas", InputOutput.ENTRADA, Signal.POSITIVO));
        naturezasPorTipo.put("7", new CnabNature("Recebimento TED", InputOutput.ENTRADA, Signal.POSITIVO));
        naturezasPorTipo.put("8", new CnabNature("Recebimento DOC", InputOutput.ENTRADA, Signal.POSITIVO));
        naturezasPorTipo.put("9", new CnabNature("Aluguel", InputOutput.SAIDA, Signal.NEGATIVO));
    }

}
