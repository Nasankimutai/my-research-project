package com.procurement.project.service;

import com.procurement.project.dto.MlFeatureDTO;
import com.procurement.project.dto.MlResponseDTO;
import com.procurement.project.dto.TransactionDTO;
import com.procurement.project.model.Transaction;
import com.procurement.project.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private MlService mlService;

    @Autowired
    private AuditLogService auditLogService;

    // Convert Entity -> DTO
    private TransactionDTO mapToDTO(Transaction transaction){
        return new TransactionDTO(
                transaction.getId(),
                transaction.getTransactionId(),
                transaction.getSupplier(),
                transaction.getAmount(),
                transaction.getTransactionDate(),
                transaction.getStatus(),
                transaction.getDescription(),
                transaction.getCategory(),
                transaction.getItemName(),
                transaction.getPaymentMethod(),
                transaction.getDepartment(),
                transaction.getSupplierCountry(),
                transaction.getContractId(),
                transaction.getUnitPrice(),
                transaction.getQuantity(),
                transaction.getTotalPrice(),
                transaction.getApprovedBy(),
                transaction.getRiskScore(),
                transaction.getAnomalyFlag(),
                transaction.getAnomalyType(),
                transaction.getAnomalySeverity(),
                transaction.getAnomalyScore(),
                transaction.getReviewedBy(),
                transaction.getReviewNotes(),
                transaction.getCreatedAt(),
                transaction.getUpdatedAt()
        );
    }

    // Convert DTO -> Entity
    private Transaction mapToEntity(TransactionDTO dto){
        Transaction transaction = new Transaction();
        transaction.setId(dto.getId());
        transaction.setTransactionId(dto.getTransactionId());
        transaction.setSupplier(dto.getSupplier());
        transaction.setAmount(dto.getAmount());
        transaction.setTransactionDate(dto.getTransactionDate());
        transaction.setStatus(dto.getStatus());
        transaction.setDescription(dto.getDescription());
        transaction.setCategory(dto.getCategory());
        transaction.setItemName(dto.getItemName());
        transaction.setPaymentMethod(dto.getPaymentMethod());
        transaction.setDepartment(dto.getDepartment());
        transaction.setSupplierCountry(dto.getSupplierCountry());
        transaction.setContractId(dto.getContractId());
        transaction.setUnitPrice(dto.getUnitPrice());
        transaction.setQuantity(dto.getQuantity());
        transaction.setTotalPrice(dto.getTotalPrice());
        transaction.setApprovedBy(dto.getApprovedBy());
        transaction.setRiskScore(dto.getRiskScore());
        transaction.setAnomalyFlag(dto.getAnomalyFlag());
        transaction.setAnomalyType(dto.getAnomalyType());
        transaction.setAnomalySeverity(dto.getAnomalySeverity());
        transaction.setAnomalyScore(dto.getAnomalyScore());
        transaction.setReviewedBy(dto.getReviewedBy());
        transaction.setReviewNotes(dto.getReviewNotes());
        transaction.setCreatedAt(dto.getCreatedAt());
        transaction.setUpdatedAt(dto.getUpdatedAt());
        return transaction;
    }

    public TransactionDTO saveTransaction(TransactionDTO dto){
        Transaction transaction = mapToEntity(dto);

        // Build featureDTO
        MlFeatureDTO featureDTO = buildFeatureDTO(transaction);

        // Call ML
        MlResponseDTO mlResponseDTO = mlService.analyzeFeatures(featureDTO);

        // Store ML results
        transaction.setAnomalyFlag(mlResponseDTO.getLabel() == 1);
        transaction.setAnomalyType(mlResponseDTO.getAnomaly_type());
        transaction.setAnomalySeverity(mlResponseDTO.getSeverity());
        transaction.setAnomalyScore(mlResponseDTO.getScore());

        // Save
        Transaction savedTransaction = transactionRepository.save(transaction);
        return mapToDTO(savedTransaction);

        // log
    }

    private MlFeatureDTO buildFeatureDTO(Transaction transaction) {
        MlFeatureDTO featureDTO = new MlFeatureDTO();

        featureDTO.setTransaction_id(transaction.getTransactionId());

        LocalDateTime dt = transaction.getTransactionDate();
        double hour = (dt != null) ? dt.getHour() : 0.0;
        featureDTO.setHour(hour);

        double after_hours_flag = 0.0;
        if (dt != null) {
            DayOfWeek dayOfWeek = dt.getDayOfWeek();
            boolean isWeekend = (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY);
            boolean lateHour = (hour >= 22 || hour <= 4);
            if (isWeekend || lateHour) {
                after_hours_flag = 1.0;
            }
        }
        featureDTO.setAfter_hours_flag(after_hours_flag);

        // For demonstration, no real budget => 0.0
        double budget = 0.0;
        double amount = transaction.getAmount() != null ? transaction.getAmount() : 0.0;
        double budget_deviation = amount - budget;
        featureDTO.setBudget_deviation(budget_deviation);

        double round_number_flag = (amount % 1.0 == 0.0) ? 1.0 : 0.0;
        featureDTO.setRound_number_flag(round_number_flag);

        featureDTO.setDuplicate_flag(0.0);
        featureDTO.setSupplier_change_count(1.0);
        featureDTO.setUnexpected_supplier_flag(0.0);

        double supplier_frequency = 1.0; // placeholder
        featureDTO.setSupplier_frequency(supplier_frequency);

        double vendor_collusion_risk = 0.0;
        if (supplier_frequency > 100 && amount > 1000) {
            vendor_collusion_risk = 1.0;
        }
        featureDTO.setVendor_collusion_risk(vendor_collusion_risk);

        double budget_dev_freq = budget_deviation * supplier_frequency;
        featureDTO.setBudget_dev_freq(budget_dev_freq);

        return featureDTO;
    }

    public List<TransactionDTO> getAllTransactions(){
        return transactionRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<TransactionDTO> getTransactionById(Long id){
        return transactionRepository.findById(id).map(this::mapToDTO);
    }

    public List<TransactionDTO> getAnomalies(){
        return transactionRepository.findByAnomalyFlagTrue().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public TransactionDTO updateTransaction(Long id, TransactionDTO updatedDto){
        Optional<Transaction> existingTransaction = transactionRepository.findById(id);
        if(existingTransaction.isPresent()){
            Transaction transaction = existingTransaction.get();
            transaction.setStatus(updatedDto.getStatus());
            transaction.setRiskScore(updatedDto.getRiskScore());
            transaction.setAnomalyFlag(updatedDto.getAnomalyFlag());
            transaction.setAnomalyType(updatedDto.getAnomalyType());
            transaction.setAnomalySeverity(updatedDto.getAnomalySeverity());
            transaction.setAnomalyScore(updatedDto.getAnomalyScore());
            transaction.setReviewedBy(updatedDto.getReviewedBy());
            transaction.setReviewNotes(updatedDto.getReviewNotes());
            transaction.setUpdatedAt(updatedDto.getUpdatedAt());

            Transaction saved = transactionRepository.save(transaction);
            return mapToDTO(saved);
        } else {
            throw new RuntimeException("Transaction with id " + id + " not found");
        }
    }

    public void deleteTransaction(Long id){
        if(!transactionRepository.existsById(id)){
            throw new RuntimeException("Transaction with id " + id + " not found");
        }
        transactionRepository.deleteById(id);
    }
}
