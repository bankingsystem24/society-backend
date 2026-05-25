package com.society.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.society.backend.entity.Receipt;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
}
