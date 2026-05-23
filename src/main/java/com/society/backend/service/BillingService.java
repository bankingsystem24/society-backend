package com.society.backend.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.society.backend.entity.*;
import com.society.backend.enums.PaymentStatus;
import com.society.backend.repository.BillingRepository;
import com.society.backend.repository.FlatRepository;

@Service
public class BillingService {

    @Autowired
    private BillingRepository billingRepository;

    @Autowired
    private FlatRepository flatRepository;

    // 🔥 AUTO GENERATE MONTHLY BILLS
    public String generateMonthlyBills(Long societyId, String month, int year) {

        List<Flat> flats = flatRepository.findBySociety_Id(societyId);

        int createdCount = 0;

        for (Flat flat : flats) {

            // ❌ prevent duplicate bill
            boolean exists = billingRepository
                    .existsByFlatIdAndMonthAndYear(flat.getId(), month, year);

            if (exists) {
                continue;
            }

            Billing bill = new Billing();

            bill.setSociety(flat.getSociety());
            bill.setFlat(flat);

            bill.setMonth(month);
            bill.setYear(year);

            // 💰 basic logic (you can enhance later)
            double amount = flat.getMaintenanceAmount() != null
                    ? flat.getMaintenanceAmount()
                    : 0.0;

            bill.setMaintenanceAmount(amount);
            bill.setPenaltyAmount(0.0);
            bill.setTotalAmount(amount);

            bill.setStatus(PaymentStatus.PENDING);
            bill.setDueDate(LocalDate.now().plusDays(10));
            bill.setCreatedDate(LocalDate.now());

            billingRepository.save(bill);

            createdCount++;
        }

        return createdCount + " bills generated successfully for " + month + " " + year;
    }

    // 📌 GET SOCIETY BILLS
    public List<Billing> getBySociety(Long societyId) {
        return billingRepository.findBySocietyId(societyId);
    }

    // 📌 GET PENDING BILLS
    public List<Billing> getPending(Long societyId) {
        return billingRepository.findBySocietyIdAndStatus(
                societyId,
                PaymentStatus.PENDING);
    }
}