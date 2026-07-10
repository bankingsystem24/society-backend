package com.society.backend.gl.service;
import java.util.List;
import org.springframework.stereotype.Service;
import com.society.backend.entity.AccountingYear;
import com.society.backend.gl.dto.TrialBalanceDTO;
import com.society.backend.gl.entity.ProfitLossDetail;
import com.society.backend.gl.entity.ProfitLossHeader;
import com.society.backend.gl.repository.ProfitLossDetailRepository;
import com.society.backend.gl.repository.ProfitLossHeaderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class ProfitLossSnapshotService {

    private final ProfitLossHeaderRepository headerRepository;
    private final ProfitLossDetailRepository detailRepository;

    @Transactional
    public void saveSnapshot(AccountingYear year,List<TrialBalanceDTO> trialBalance,String username) 
    {
        double totalIncome = 0;
        double totalExpense = 0;

        // Prevent duplicate snapshots
        headerRepository.findByFinancialYearId(year.getId())
                .ifPresent(headerRepository::delete);

        ProfitLossHeader header = new ProfitLossHeader();
        header.setSocietyId(year.getSociety().getId());
        header.setFinancialYearId(year.getId());
        header.setFromDate(year.getStartDate());
        header.setToDate(year.getEndDate());
        header.setCreatedBy(username);
        header.setCreatedAt(LocalDateTime.now());

        header = headerRepository.save(header);

        for (TrialBalanceDTO dto : trialBalance) {

        System.out.println(
        "GL=" + dto.getGlCode()
        + ", Name=" + dto.getAccountName()
        + ", Group=" + dto.getGroupName()
        + ", ClosingDebit=" + dto.getClosingDebit()
        + ", ClosingCredit=" + dto.getClosingCredit());

            // Income Accounts
            if ("INCOME".equalsIgnoreCase(dto.getGroupName())
                    && dto.getClosingCredit() > 0) {

                ProfitLossDetail detail = new ProfitLossDetail();

                detail.setHeader(header);
                detail.setGlCode(dto.getGlCode());
                detail.setAccountName(dto.getAccountName());
                detail.setGroupName("INCOME");
                detail.setAmount(dto.getClosingCredit());

                detailRepository.save(detail);

                totalIncome += dto.getClosingCredit();
            }

            // Expense Accounts
            if ("EXPENSES".equalsIgnoreCase(dto.getGroupName())
                    && dto.getClosingDebit() > 0) {

                ProfitLossDetail detail = new ProfitLossDetail();

                detail.setHeader(header);
                detail.setGlCode(dto.getGlCode());
                detail.setAccountName(dto.getAccountName());
                detail.setGroupName("EXPENSE");
                detail.setAmount(dto.getClosingDebit());

                detailRepository.save(detail);

                totalExpense += dto.getClosingDebit();
            }
        }

        header.setTotalIncome(totalIncome);
        header.setTotalExpense(totalExpense);
        header.setNetProfit(totalIncome - totalExpense);

        headerRepository.save(header);
    }
}