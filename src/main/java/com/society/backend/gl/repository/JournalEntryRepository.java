package com.society.backend.gl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.society.backend.gl.entity.JournalEntry;

@Repository
public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {
}