package ru.msvdev.homefinance.io.csv;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;


@Data
public class OperationCsvRow {

    @CsvDate("yyyy-MM-dd")
    @CsvBindByName(column = "ДАТА", required = true)
    private LocalDate date;

    @CsvBindByName(column = "КАТЕГОРИЯ", required = true)
    private String category;

    @CsvBindByName(column = "СТОИМОСТЬ", required = true)
    private BigDecimal cost;

    @CsvBindByName(column = "ПРИМЕЧАНИЕ")
    private String note;

    @CsvBindByName(column = "ПРОВЕРЕНО")
    private Boolean check;

}
