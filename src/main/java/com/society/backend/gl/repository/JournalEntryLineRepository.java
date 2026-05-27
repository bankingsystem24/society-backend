package com.society.backend.gl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.society.backend.gl.entity.JournalEntryLine;

public interface JournalEntryLineRepository extends JpaRepository<JournalEntryLine, Long> {
}