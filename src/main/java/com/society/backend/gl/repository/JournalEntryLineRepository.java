package com.society.backend.gl.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.society.backend.gl.entity.JournalEntryLine;

public interface JournalEntryLineRepository
        extends JpaRepository<JournalEntryLine, Long> {

    List<JournalEntryLine> findByJournalId(Long journalId);
}

