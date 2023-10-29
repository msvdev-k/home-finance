package ru.msvdev.homefinance.io.csv;

import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;


public class CsvIO {

    public List<OperationCsvRow> importFromCsv(Path path) {
        try (Reader reader = Files.newBufferedReader(path)) {

            CsvToBean<OperationCsvRow> fromCsv = new CsvToBeanBuilder<OperationCsvRow>(reader)
                    .withType(OperationCsvRow.class)
                    .withQuoteChar(CSVWriter.DEFAULT_QUOTE_CHARACTER)
                    .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                    .withIgnoreEmptyLine(true)
                    .build();

            return fromCsv.parse();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void exportToCsv(Path path, List<OperationCsvRow> operationCsvRows) {
        try (Writer writer = Files.newBufferedWriter(path)) {

            StatefulBeanToCsv<OperationCsvRow> toCsv = new StatefulBeanToCsvBuilder<OperationCsvRow>(writer)
                    .withQuotechar(CSVWriter.DEFAULT_QUOTE_CHARACTER)
                    .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                    .build();

            toCsv.write(operationCsvRows);

        } catch (IOException | CsvRequiredFieldEmptyException | CsvDataTypeMismatchException e) {
            throw new RuntimeException(e);
        }
    }

}
