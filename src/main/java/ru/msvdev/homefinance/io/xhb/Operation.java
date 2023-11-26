package ru.msvdev.homefinance.io.xhb;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class Operation {

    private LocalDate date;
    private BigDecimal amount;
    private Integer category;
    private String note;

}
