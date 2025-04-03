package com.procurement.project.repository;

import com.procurement.project.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAnomalyFlagTrue(); //Get all flagged anomalies
    List<Transaction> findBySupplier(String supplier); // Get transactions by supplier
}
