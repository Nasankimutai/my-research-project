package com.procurement.project.controller;

import com.procurement.project.dto.TransactionDTO;
import com.procurement.project.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionDTO> createTransaction(@RequestBody TransactionDTO transactionDTO){
        return ResponseEntity.ok(transactionService.saveTransaction(transactionDTO));
    }

    @GetMapping
    public ResponseEntity<List<TransactionDTO>> getAllTransactions(){
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDTO> getTransationById(@PathVariable Long id){
        Optional<TransactionDTO> transaction = transactionService.getTransactionById(id);
        return transaction.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionDTO> updateTransaction(@PathVariable Long id, @RequestBody TransactionDTO transactionDTO){
        return ResponseEntity.ok(transactionService.updateTransaction(id, transactionDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTransaction(@PathVariable Long id){
        transactionService.deleteTransaction(id);
        return ResponseEntity.ok("Transaction deleted successfully");
    }

    @PostMapping("/bulk")
    public ResponseEntity<String> bulkUploadTransactions(@RequestBody Map<String, List<TransactionDTO>> payload) {
        List<TransactionDTO> transactions = payload.get("transactions");

        if (transactions == null || transactions.isEmpty()) {
            return ResponseEntity.badRequest().body("No transactions provided");
        }

        List<TransactionDTO> savedTransactions = transactions.stream()
                .map(transactionService::saveTransaction)
                .collect(Collectors.toList());

        return ResponseEntity.ok("Successfully uploaded " + savedTransactions.size() + " transactions");
    }
}
