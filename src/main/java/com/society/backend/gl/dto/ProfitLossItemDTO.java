package com.society.backend.gl.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfitLossItemDTO {

    private Integer glCode;
    private String accountName;
    private Double amount;
}
