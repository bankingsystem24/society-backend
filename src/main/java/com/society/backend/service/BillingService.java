package com.society.backend.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.society.backend.entity.Billing;
import com.society.backend.entity.Flat;
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
                    .existsByFlatIdAndMonthAndYear(
                            flat.getId(),
                            month,
                            year
                    );

            if (exists) {
                continue;
            }

            Billing bill = new Billing();

            bill.setSociety(flat.getSociety());
            bill.setFlat(flat);

            bill.setMonth(month);
            bill.setYear(year);

            // 💰 maintenance amount
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

        return createdCount + " bills generated successfully for "
                + month + " " + year;
    }

    // 📌 GET SOCIETY BILLS
    public List<Billing> getBySociety(Long societyId) {

        return billingRepository.findBySocietyId(societyId);
    }

    // 📌 GET PENDING BILLS
    public List<Billing> getPending(Long societyId) {

        return billingRepository.findBySocietyIdAndStatus(
                societyId,
                PaymentStatus.PENDING
        );
    }

    // 📌 VIEW ALL BILLS WITH FILTER
    public List<Billing> viewAllBills(
            Long societyId,
            Long flatId,
            Integer fromYear,
            String month,
            PaymentStatus status

    ) {

        List<Billing> bills = List.of();

        // ✅ FLAT + MONTH
        if (flatId != null
                && month != null
                && !month.isEmpty()) {

            bills = billingRepository
                    .findBySocietyIdAndFlatIdAndMonth(
                            societyId,
                            flatId,
                            month
                    );
        }

        // ✅ FLAT ONLY
        else if (flatId != null) {

            bills = billingRepository
                    .findBySocietyIdAndFlatId(
                            societyId,
                            flatId
                    );
        }

        // ✅ MONTH ONLY
        else if (month != null
                && !month.isEmpty()) {

            bills = billingRepository
                    .findBySocietyIdAndMonth(
                            societyId,
                            month
                    );
        }

        // ✅ ALL BILLS
        else if  (status != null) {

                bills = bills.stream()
                        .filter(bill -> bill.getStatus().equals(status))
                        .toList();
                } else {

            bills = billingRepository
                    .findBySocietyId(societyId);
        }

        // ✅ FINANCIAL YEAR FILTER
        if (fromYear != null) {

            int toYear = fromYear + 1;

            bills = bills.stream()
                    .filter(bill -> {

                        String m = bill.getMonth().toUpperCase();

                        // APRIL → DECEMBER
                        if (
                                m.equals("APRIL") ||
                                m.equals("MAY") ||
                                m.equals("JUNE") ||
                                m.equals("JULY") ||
                                m.equals("AUGUST") ||
                                m.equals("SEPTEMBER") ||
                                m.equals("OCTOBER") ||
                                m.equals("NOVEMBER") ||
                                m.equals("DECEMBER")
                        ) {

                            return bill.getYear() == fromYear;
                        }

                        // JANUARY → MARCH
                        return bill.getYear() == toYear;
                    })
                    .toList();
        }

        return bills;
    }


}