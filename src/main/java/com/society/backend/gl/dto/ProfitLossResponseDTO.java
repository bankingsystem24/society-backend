package com.society.backend.gl.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProfitLossResponseDTO {

    private List<ProfitLossItemDTO> income;
    private List<ProfitLossItemDTO> expense;

    private Double totalIncome;
    private Double totalExpense;

    private Double surplus;
}
