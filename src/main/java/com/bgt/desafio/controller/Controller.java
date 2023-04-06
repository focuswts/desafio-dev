package com.bgt.desafio.controller;

import com.bgt.desafio.dto.CnabTransaction;
import com.bgt.desafio.entity.CnabTransactionEntity;
import com.bgt.desafio.service.CnabProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@org.springframework.stereotype.Controller
public class Controller {

    @Autowired
    private CnabProcessingService transactionService;

    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/upload")
    public String upload() {
        return "upload";
    }

    @GetMapping("/resultado-cnab")
    public String resultadoCnab() {
        return "resultado-cnab";
    }

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("file") MultipartFile file, Model model) throws IOException {
        return transactionService.cnabDataProcess(file, model);
    }

    @GetMapping("/stores")
    public String showStores(Model model) {
        return transactionService.getAllTransactionsByStoreName(model);
    }

    @GetMapping("/transactions")
    public String getTransactions(Model model) {
        List<CnabTransactionEntity> transactions = transactionService.getAllTransactions();

        model.addAttribute("transactions", transactions);
        return "resultado-cnab";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}

