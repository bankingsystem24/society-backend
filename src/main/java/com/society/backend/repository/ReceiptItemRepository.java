package com.society.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.society.backend.entity.ReceiptItem;

public interface ReceiptItemRepository extends JpaRepository<ReceiptItem, Long> {
}
