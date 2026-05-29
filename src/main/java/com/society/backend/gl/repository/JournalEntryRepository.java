package com.society.backend.gl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.society.backend.gl.entity.JournalEntry;

public interface JournalEntryRepository
        extends JpaRepository<JournalEntry, Long> {
}