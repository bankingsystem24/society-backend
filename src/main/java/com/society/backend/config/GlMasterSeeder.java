package com.society.backend.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.society.backend.gl.entity.GlMaster;
import com.society.backend.gl.repository.GlMasterRepository;

@Configuration
public class GlMasterSeeder {

    @Bean
    CommandLineRunner seedGlAccounts(GlMasterRepository repo) {
        return args -> {

            // prevent duplicate seeding
            if (repo.count() > 0) {
                return;
            }

            Long societyId = 1L;

            List<GlMaster> list = List.of(

                // ================= ASSETS =================
                create(1000, "ASSETS", "Cash in Hand", societyId),
                create(1001, "ASSETS", "Bank Account", societyId),

                create(1100, "ASSETS", "Maintenance Receivable", societyId),
                create(1101, "ASSETS", "Member Receivable", societyId),

                create(1110, "ASSETS", "Late Fee Receivable", societyId),
                create(1120, "ASSETS", "Parking Charges Receivable", societyId),
                create(1130, "ASSETS", "Interest Receivable", societyId),

                create(1200, "ASSETS", "Advance to Vendors", societyId),
                create(1210, "ASSETS", "Security Deposit Recoverable", societyId),

                create(1300, "ASSETS", "Prepaid Insurance", societyId),
                create(1310, "ASSETS", "Prepaid AMC Expense", societyId),

                create(1400, "ASSETS", "Furniture & Fixtures", societyId),
                create(1410, "ASSETS", "Office Equipment", societyId),
                create(1420, "ASSETS", "Generator / DG Set", societyId),
                create(1430, "ASSETS", "CCTV & Security Systems", societyId),

                // ================= LIABILITIES =================
                create(2000, "LIABILITIES", "Maintenance Received in Advance", societyId),
                create(2010, "LIABILITIES", "Member Security Deposit", societyId),

                create(2100, "LIABILITIES", "Vendor Payable", societyId),
                create(2110, "LIABILITIES", "Electricity Charges Payable", societyId),
                create(2120, "LIABILITIES", "Water Charges Payable", societyId),
                create(2130, "LIABILITIES", "Salary Payable", societyId),
                create(2140, "LIABILITIES", "Audit Fees Payable", societyId),
                create(2150, "LIABILITIES", "TDS Payable", societyId),
                create(2160, "LIABILITIES", "GST Payable", societyId),

                // ================= INCOME =================
                create(3000, "INCOME", "Maintenance Income", societyId),
                create(3001, "INCOME", "Member Maintenance Income", societyId),
                create(3010, "INCOME", "Late Fee / Penalty Income", societyId),
                create(3020, "INCOME", "Interest Income", societyId),
                create(3030, "INCOME", "Parking Charges Income", societyId),
                create(3040, "INCOME", "Club House Income", societyId),
                create(3050, "INCOME", "Advertisement Income", societyId),
                create(3060, "INCOME", "Transfer Fee Income", societyId),
                create(3070, "INCOME", "Rental Income", societyId),
                create(3080, "INCOME", "Event Contribution Income", societyId),
                create(3090, "INCOME", "Miscellaneous Income", societyId),

                // ================= EXPENSES =================
                create(4000, "EXPENSES", "Electricity Charges", societyId),
                create(4010, "EXPENSES", "Water Charges", societyId),
                create(4020, "EXPENSES", "Security Guard Salary", societyId),
                create(4030, "EXPENSES", "Housekeeping Charges", societyId),
                create(4040, "EXPENSES", "Repairs & Maintenance", societyId),
                create(4050, "EXPENSES", "Lift Maintenance", societyId),
                create(4060, "EXPENSES", "Garden Maintenance", societyId),
                create(4070, "EXPENSES", "Office & Admin Expenses", societyId),
                create(4080, "EXPENSES", "Printing & Stationery", societyId),
                create(4090, "EXPENSES", "Audit Fees", societyId),
                create(4100, "EXPENSES", "Legal & Professional Fees", societyId),
                create(4110, "EXPENSES", "Bank Charges", societyId),
                create(4120, "EXPENSES", "Society Event Expenses", societyId),
                create(4130, "EXPENSES", "Insurance Expense", societyId),
                create(4140, "EXPENSES", "AMC Charges", societyId),
                create(4150, "EXPENSES", "Pest Control Charges", societyId),
                create(4160, "EXPENSES", "Generator Diesel Expense", societyId),
                create(4170, "EXPENSES", "Internet & Software Expense", societyId),

                // ================= RESERVES =================
                create(5000, "RESERVES", "General Reserve Fund", societyId),
                create(5010, "RESERVES", "Sinking Fund", societyId),
                create(5020, "RESERVES", "Repair Fund", societyId),
                create(5030, "RESERVES", "Parking Fund", societyId),
                create(5040, "RESERVES", "Building Maintenance Reserve", societyId),
                create(5050, "RESERVES", "Emergency Fund", societyId)
            );

            repo.saveAll(list);
        };
    }

    private GlMaster create(Integer code, String accountType, String accountName, Long societyId) {

        GlMaster g = new GlMaster();

        g.setGlCode(code);
        g.setAccountType(accountType);
        g.setGroupName(accountType);
        g.setAccountName(accountName);
        g.setSocietyId(societyId);
        g.setIsActive(true);

        return g;
    }
}