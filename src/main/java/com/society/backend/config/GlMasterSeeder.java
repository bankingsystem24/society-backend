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

            if (repo.count() > 0) return; // prevent duplicate insert

            repo.saveAll(List.of(

                // ================= ASSETS =================
                create(1000, "ASSETS", "Cash in Hand"),
                create(1010, "ASSETS", "Bank - Savings Account"),
                create(1020, "ASSETS", "Bank - Fixed Deposit"),
                create(1030, "ASSETS", "Bank - Maintenance Fund"),
                create(1100, "ASSETS", "Maintenance Receivable (Members)"),
                create(1110, "ASSETS", "Late Fee Receivable"),
                create(1200, "ASSETS", "Interest Receivable"),
                create(1300, "ASSETS", "Advance to Vendors"),
                create(1400, "ASSETS", "Security Deposits Receivable"),
                create(1500, "ASSETS", "Prepaid Expenses"),

                // ================= LIABILITIES =================
                create(2000, "LIABILITIES", "Maintenance Received in Advance"),
                create(2010, "LIABILITIES", "Security Deposit (Members)"),
                create(2100, "LIABILITIES", "Sundry Creditors / Vendors Payable"),
                create(2110, "LIABILITIES", "Electricity Charges Payable"),
                create(2120, "LIABILITIES", "Water Charges Payable"),
                create(2130, "LIABILITIES", "Salary Payable"),
                create(2140, "LIABILITIES", "Audit Fees Payable"),
                create(2150, "LIABILITIES", "TDS Payable"),

                // ================= INCOME =================
                create(3000, "INCOME", "Maintenance Income"),
                create(3010, "INCOME", "Late Fee / Penalty Income"),
                create(3020, "INCOME", "Interest Income (FD/Bank)"),
                create(3030, "INCOME", "Parking Charges Income"),
                create(3040, "INCOME", "Advertisement / Rental Income"),
                create(3050, "INCOME", "Event Income"),
                create(3060, "INCOME", "Transfer Fees Income"),
                create(3070, "INCOME", "Miscellaneous Income"),

                // ================= EXPENSES =================
                create(4000, "EXPENSES", "Electricity Charges"),
                create(4010, "EXPENSES", "Water Charges"),
                create(4020, "EXPENSES", "Security Guard Salary"),
                create(4030, "EXPENSES", "Housekeeping Charges"),
                create(4040, "EXPENSES", "Repairs & Maintenance"),
                create(4050, "EXPENSES", "Lift Maintenance"),
                create(4060, "EXPENSES", "Garden Maintenance"),
                create(4070, "EXPENSES", "Office/Admin Expenses"),
                create(4080, "EXPENSES", "Printing & Stationery"),
                create(4090, "EXPENSES", "Audit Fees"),
                create(4100, "EXPENSES", "Legal & Professional Fees"),
                create(4110, "EXPENSES", "Bank Charges"),
                create(4120, "EXPENSES", "Society Event Expenses"),
                create(4130, "EXPENSES", "Insurance Expense"),
                create(4140, "EXPENSES", "AMC Charges"),

                // ================= RESERVES =================
                create(5000, "RESERVES", "General Reserve Fund"),
                create(5010, "RESERVES", "Sinking Fund"),
                create(5020, "RESERVES", "Repair Fund"),
                create(5030, "RESERVES", "Parking Fund"),
                create(5040, "RESERVES", "Building Maintenance Reserve")

            ));
        };
    }

    private GlMaster create(Integer code, String group, String name) {
        GlMaster g = new GlMaster();
        g.setGlCode(code);
        g.setGroupName(group);
        g.setAccountName(name);
        return g;
    }
}