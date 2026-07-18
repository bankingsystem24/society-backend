package com.society.backend.gl.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.society.backend.gl.dto.CashBookRequest;
import com.society.backend.gl.dto.CashBookResponse;
import com.society.backend.gl.repository.JournalEntryLineRepository;

import lombok.AllArgsConstructor;

import java.time.LocalDate;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class CashBookService {
    
    private final JournalEntryLineRepository journalEntryLineRepository;

    @Transactional(readOnly = true)
    public List<CashBookResponse> getCashBook(CashBookRequest request)
    {

        BigDecimal balance = journalEntryLineRepository.getOpeningBalance(
                request.getSocietyId(),
                request.getAccountCode(),
                request.getFromDate());

        if(balance==null)
            balance=BigDecimal.ZERO;

        List<Object[]> rows = journalEntryLineRepository.getCashBook(
                request.getSocietyId(),
                request.getAccountCode(),
                request.getFromDate(),
                request.getToDate());

        List<CashBookResponse> list = new ArrayList<>();

        if(Boolean.TRUE.equals(request.getOpeningBalance())){

            CashBookResponse opening=new CashBookResponse();
            opening.setDate(request.getFromDate());
            opening.setParticulars("Opening Balance");
            opening.setReceipt(BigDecimal.ZERO);
            opening.setPayment(BigDecimal.ZERO);
            opening.setBalance(balance);

            list.add(opening);
        }

        for(Object[] row:rows){

            CashBookResponse dto=new CashBookResponse();

dto.setDate((LocalDate) row[0]);
dto.setVoucherNo((String) row[1]);
dto.setParticulars((String) row[2]);
dto.setNarration((String) row[3]);

BigDecimal receipt = row[4] == null
        ? BigDecimal.ZERO
        : BigDecimal.valueOf(((Number) row[4]).doubleValue());

BigDecimal payment = row[5] == null
        ? BigDecimal.ZERO
        : BigDecimal.valueOf(((Number) row[5]).doubleValue());

dto.setReceipt(receipt);
dto.setPayment(payment);

balance = balance.add(receipt).subtract(payment);
dto.setBalance(balance);

            dto.setParticulars((String)row[2]);

            list.add(dto);
        }

        return list;
    }
}
