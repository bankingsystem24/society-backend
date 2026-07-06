package com.society.backend.gl.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.society.backend.entity.Billing;
import com.society.backend.entity.SinkingFund;
import com.society.backend.enums.PaymentStatus;
import com.society.backend.gl.dto.BalanceSheetResponse;
import com.society.backend.gl.dto.BalanceSheetRow;
import com.society.backend.gl.dto.Payments;
import com.society.backend.gl.dto.ProfitLossItemDTO;
import com.society.backend.gl.dto.ProfitLossResponseDTO;
import com.society.backend.gl.entity.Contribution;
import com.society.backend.gl.entity.GlMaster;
import com.society.backend.gl.entity.GlOpeningBalance;
import com.society.backend.gl.repository.ContributionRepository;
import com.society.backend.gl.repository.GlMasterRepository;
import com.society.backend.gl.repository.GlOpeningBalanceRepository;
import com.society.backend.gl.repository.JournalEntryLineRepository;
import com.society.backend.repository.BillingRepository;
import com.society.backend.repository.SinkingFundRepository;

@Service
public class ReportService {

        private final JournalEntryLineRepository journalEntryLineRepository;
        private final GlOpeningBalanceRepository glOpeningBalanceRepository;
        private final GlMasterRepository glMasterRepository;
        private final BillingRepository billingRepository;
        private final SinkingFundRepository sinkingFundRepository;
        private final ContributionRepository contributionRepository;

        public ReportService(JournalEntryLineRepository journalEntryLineRepository,
                        GlOpeningBalanceRepository glOpeningBalanceRepository,
                        GlMasterRepository glMasterRepository,
                        BillingRepository billingRepository,
                        SinkingFundRepository sinkingFundRepository,
                        ContributionRepository contributionRepository) {
                this.journalEntryLineRepository = journalEntryLineRepository;
                this.glOpeningBalanceRepository = glOpeningBalanceRepository;
                this.glMasterRepository = glMasterRepository;
                this.billingRepository = billingRepository;
                this.sinkingFundRepository = sinkingFundRepository;
                this.contributionRepository = contributionRepository;
        }

        public ProfitLossResponseDTO getProfitAndLoss(Long societyId, Long financialYearId) {

                List<Object[]> rows = journalEntryLineRepository.getProfitLossData(societyId, financialYearId);

                List<ProfitLossItemDTO> income = new ArrayList<>();
                List<ProfitLossItemDTO> expense = new ArrayList<>();

                double totalIncome = 0;
                double totalExpense = 0;

                for (Object[] row : rows) {

                        Integer glCode = ((Number) row[0]).intValue();

                        String accountName = String.valueOf(row[1]);

                        String accountType = String.valueOf(row[2]);

                        Double debit = ((Number) row[3]).doubleValue();

                        Double credit = ((Number) row[4]).doubleValue();

                        if ("INCOME".equalsIgnoreCase(accountType)) {

                                double amount = credit - debit;

                                if (amount != 0) {

                                        income.add(
                                                        new ProfitLossItemDTO(
                                                                        glCode,
                                                                        accountName,
                                                                        amount));

                                        totalIncome += amount;
                                }
                        }

                        if ("EXPENSES".equalsIgnoreCase(accountType)) {

                                double amount = debit - credit;

                                if (amount != 0) {

                                        expense.add(
                                                        new ProfitLossItemDTO(
                                                                        glCode,
                                                                        accountName,
                                                                        amount));

                                        totalExpense += amount;
                                }
                        }
                }

                ProfitLossResponseDTO response = new ProfitLossResponseDTO();

                response.setIncome(income);
                response.setExpense(expense);

                response.setTotalIncome(totalIncome);
                response.setTotalExpense(totalExpense);

                response.setSurplus(
                                totalIncome - totalExpense);

                return response;
        }

        public BalanceSheetResponse generate(Long societyId, Long fyId) {

                Map<Integer, BalanceSheetRow> map = new LinkedHashMap<>();

                // -----------------------------
                // Load GL Masters
                // -----------------------------
                List<GlMaster> masters = glMasterRepository.findBySocietyIdAndIsActiveTrue(societyId);

                for (GlMaster master : masters) {

                        String group = master.getGroupName();

                        // Skip P&L accounts
                        if ("INCOME".equalsIgnoreCase(group)
                                        || "EXPENSES".equalsIgnoreCase(group)) {
                                continue;
                        }

                        BalanceSheetRow row = new BalanceSheetRow();

                        row.setGlCode(master.getGlCode());
                        row.setGlName(master.getAccountName());
                        row.setAccountType(group);

                        row.setOpeningBalance(0.0);
                        row.setDebit(0.0);
                        row.setCredit(0.0);
                        row.setClosingBalance(0.0);

                        map.put(master.getGlCode(), row);
                }

                // -----------------------------
                // Opening Balances
                // -----------------------------
                List<GlOpeningBalance> openings = glOpeningBalanceRepository.findBySociety_IdAndFinancialYearId(
                                societyId,
                                fyId);

                for (GlOpeningBalance ob : openings) {

                        BalanceSheetRow row = map.get(ob.getGlCode());

                        if (row == null)
                                continue;

                        double opening = 0;

                        if ("ASSETS".equalsIgnoreCase(row.getAccountType())) {

                                opening = (ob.getOpeningDebit() == null ? 0 : ob.getOpeningDebit())
                                                -
                                                (ob.getOpeningCredit() == null ? 0 : ob.getOpeningCredit());

                        } else {

                                opening = (ob.getOpeningCredit() == null ? 0 : ob.getOpeningCredit())
                                                -
                                                (ob.getOpeningDebit() == null ? 0 : ob.getOpeningDebit());

                        }

                        row.setOpeningBalance(opening);
                        row.setClosingBalance(opening);
                }

                // -----------------------------
                // Journal Transactions
                // -----------------------------
                List<Object[]> transactions = journalEntryLineRepository.getTransactionTotals(
                                societyId,
                                fyId);

                for (Object[] obj : transactions) {

                        Integer gl = ((Number) obj[0]).intValue();

                        double debit = ((Number) obj[1]).doubleValue();

                        double credit = ((Number) obj[2]).doubleValue();

                        BalanceSheetRow row = map.get(gl);

                        if (row == null)
                                continue;

                        row.setDebit(debit);
                        row.setCredit(credit);

                        double opening = row.getOpeningBalance();

                        double closing;

                        if ("ASSETS".equalsIgnoreCase(row.getAccountType())) {

                                closing = opening + debit - credit;

                        } else {

                                closing = opening + credit - debit;

                        }

                        row.setClosingBalance(closing);
                }

                // -----------------------------
                // Current Year Profit
                // -----------------------------
                ProfitLossResponseDTO pl = getProfitAndLoss(societyId, fyId);

                BalanceSheetRow surplus = new BalanceSheetRow();

                surplus.setGlCode(999999);
                surplus.setGlName("Current Year Surplus");
                surplus.setAccountType("EQUITY");
                surplus.setOpeningBalance(0.0);
                surplus.setDebit(0.0);
                surplus.setCredit(0.0);
                surplus.setClosingBalance(pl.getSurplus());

                // -----------------------------
                // Prepare Response
                // -----------------------------
                List<BalanceSheetRow> assets = new ArrayList<>();
                List<BalanceSheetRow> liabilities = new ArrayList<>();
                List<BalanceSheetRow> equity = new ArrayList<>();

                double totalAssets = 0;
                double totalLiabilities = 0;
                double totalEquity = 0;

                for (BalanceSheetRow row : map.values()) {

                        // Skip zero balances
                        if (Math.abs(row.getClosingBalance()) < 0.01)
                                continue;

                        switch (row.getAccountType().toUpperCase()) {

                                case "ASSETS":

                                        assets.add(row);
                                        totalAssets += row.getClosingBalance();
                                        break;

                                case "LIABILITIES":

                                        liabilities.add(row);
                                        totalLiabilities += row.getClosingBalance();
                                        break;

                                case "EQUITY":

                                        equity.add(row);
                                        totalEquity += row.getClosingBalance();
                                        break;
                        }
                }

                if (Math.abs(pl.getSurplus()) > 0.01) {

                        equity.add(surplus);
                        totalEquity += pl.getSurplus();
                }

                BalanceSheetResponse response = new BalanceSheetResponse();

                response.setAssets(assets);
                response.setLiabilities(liabilities);
                response.setEquity(equity);

                response.setTotalAssets(totalAssets);
                response.setTotalLiabilities(totalLiabilities);
                response.setTotalEquity(totalEquity);

                return response;
        }

        public List<Payments> getPayments(Long societyId, Long financialYearId) {
                List<Payments> response = new ArrayList<>();

                List<Billing> bills = billingRepository.findBySocietyIdAndFinancialYearId(societyId, financialYearId);
                for (Billing bill : bills) {
                        Payments payment = new Payments();
                        payment.setFinancialYearId(financialYearId);
                        payment.setFlatNo(bill.getFlat().getFlatNo());
                        payment.setMemberName(bill.getFlat().getOwner().getName());
                        payment.setSocietyId(bill.getSociety().getId());
                        payment.setDescription("Maintenance");
                        payment.setTotalAmount(bill.getTotalAmount());
                        payment.setStatus(bill.getStatus());
                        response.add(payment);
                }
                List<SinkingFund> sinkingFunds = sinkingFundRepository.findBySocietyIdAndFinancialYearId(societyId,financialYearId);
                for (SinkingFund sf : sinkingFunds){
                        Payments payment = new Payments();
                        payment.setFinancialYearId(financialYearId);
                        payment.setFlatNo(sf.getFlat().getFlatNo());
                        payment.setMemberName(sf.getFlat().getOwner().getName());
                        payment.setSocietyId(sf.getSociety().getId());
                        payment.setDescription("Sinking Fund");
                        payment.setTotalAmount(sf.getAmount());
                        payment.setStatus(sf.getStatus());
                        response.add(payment); 
                }
                List<Contribution> contributions = contributionRepository.findBySocietyIdAndFinancialYearIdAndType(societyId,financialYearId,"COMPULSORY");
                for (Contribution c : contributions){
                        Payments payment = new Payments();
                        payment.setFinancialYearId(financialYearId);
                        payment.setFlatNo(c.getFlat().getFlatNo());
                        payment.setMemberName(c.getFlat().getOwner().getName());
                        payment.setSocietyId(c.getSociety().getId());
                        payment.setDescription("Contributions");
                        payment.setTotalAmount(c.getAmount());
                        payment.setStatus(c.getStatus());
                        response.add(payment); 
                }
                List<Contribution> vcontributions = contributionRepository.findBySocietyIdAndFinancialYearIdAndTypeAndStatus(societyId,financialYearId,"VOLUNTARY",PaymentStatus.PAID);
                for (Contribution c : vcontributions){
                        Payments payment = new Payments();
                        payment.setFinancialYearId(financialYearId);
                        payment.setFlatNo(c.getFlat().getFlatNo());
                        payment.setMemberName(c.getFlat().getOwner().getName());
                        payment.setSocietyId(c.getSociety().getId());
                        payment.setDescription("Contributions");
                        payment.setTotalAmount(c.getAmount());
                        payment.setStatus(c.getStatus());
                        response.add(payment); 
                }

                return response;
        }
}
