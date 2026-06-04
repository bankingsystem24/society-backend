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
create(1001, "ASSETS", "Bank Account"),

create(1100, "ASSETS", "Maintenance Receivable"),
create(1101, "ASSETS", "Member Receivable"),

create(1110, "ASSETS", "Late Fee Receivable"),
create(1120, "ASSETS", "Parking Charges Receivable"),
create(1130, "ASSETS", "Interest Receivable"),

create(1200, "ASSETS", "Advance to Vendors"),
create(1210, "ASSETS", "Security Deposit Recoverable"),

create(1300, "ASSETS", "Prepaid Insurance"),
create(1310, "ASSETS", "Prepaid AMC Expense"),

create(1400, "ASSETS", "Furniture & Fixtures"),
create(1410, "ASSETS", "Office Equipment"),
create(1420, "ASSETS", "Generator / DG Set"),
create(1430, "ASSETS", "CCTV & Security Systems"),

// ================= LIABILITIES =================

create(2000, "LIABILITIES", "Maintenance Received in Advance"),
create(2010, "LIABILITIES", "Member Security Deposit"),

create(2100, "LIABILITIES", "Vendor Payable"),
create(2110, "LIABILITIES", "Electricity Charges Payable"),
create(2120, "LIABILITIES", "Water Charges Payable"),
create(2130, "LIABILITIES", "Salary Payable"),
create(2140, "LIABILITIES", "Audit Fees Payable"),
create(2150, "LIABILITIES", "TDS Payable"),
create(2160, "LIABILITIES", "GST Payable"),

// ================= INCOME =================

create(3000, "INCOME", "Maintenance Income"),
create(3001, "INCOME", "Member Maintenance Income"),

create(3010, "INCOME", "Late Fee / Penalty Income"),
create(3020, "INCOME", "Interest Income"),

create(3030, "INCOME", "Parking Charges Income"),
create(3040, "INCOME", "Club House Income"),
create(3050, "INCOME", "Advertisement Income"),
create(3060, "INCOME", "Transfer Fee Income"),
create(3070, "INCOME", "Rental Income"),

create(3080, "INCOME", "Event Contribution Income"),
create(3090, "INCOME", "Miscellaneous Income"),
create(4002, "INCOME", "Interest Income"),

// ================= EXPENSES =================

create(4000, "EXPENSES", "Electricity Charges"),
create(4010, "EXPENSES", "Water Charges"),

create(4020, "EXPENSES", "Security Guard Salary"),
create(4030, "EXPENSES", "Housekeeping Charges"),
create(4040, "EXPENSES", "Repairs & Maintenance"),

create(4050, "EXPENSES", "Lift Maintenance"),
create(4060, "EXPENSES", "Garden Maintenance"),
create(4070, "EXPENSES", "Office & Admin Expenses"),

create(4080, "EXPENSES", "Printing & Stationery"),
create(4090, "EXPENSES", "Audit Fees"),

create(4100, "EXPENSES", "Legal & Professional Fees"),
create(4110, "EXPENSES", "Bank Charges"),

create(4120, "EXPENSES", "Society Event Expenses"),
create(4130, "EXPENSES", "Insurance Expense"),

create(4140, "EXPENSES", "AMC Charges"),
create(4150, "EXPENSES", "Pest Control Charges"),
create(4160, "EXPENSES", "Generator Diesel Expense"),
create(4170, "EXPENSES", "Internet & Software Expense"),

// ================= RESERVES =================

create(5000, "RESERVES", "General Reserve Fund"),
create(5010, "RESERVES", "Sinking Fund"),
create(5020, "RESERVES", "Repair Fund"),
create(5030, "RESERVES", "Parking Fund"),
create(5040, "RESERVES", "Building Maintenance Reserve"),
create(5050, "RESERVES", "Emergency Fund") 
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