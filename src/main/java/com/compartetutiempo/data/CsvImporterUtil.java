/*
 * Clase creada con GitHub Copilot Chat Extension. Editada posteriormente.
 */

package com.compartetutiempo.data;

import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

public class CsvImporterUtil {

    public static <T> List<T> importCsv(String resourcePath, CsvEntityMapper<T> mapper, String delimiter) throws IOException, CsvValidationException {
        List<T> entities = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new InputStreamReader(
                CsvImporterUtil.class.getResourceAsStream(resourcePath)))) {
            String[] fields;
            boolean firstLine = true;
            while ((fields = reader.readNext()) != null) {
                if (firstLine) { firstLine = false; continue; }
                entities.add(mapper.map(fields));
            }
        }
        return entities;
    }

    public static <T> List<T> importCsvWithComma(String resourcePath, CsvEntityMapper<T> mapper) throws IOException, CsvValidationException {
        return importCsv(resourcePath, mapper, ",");
    }

    public static <T> List<T> importCsvWithSemicolon(String resourcePath, CsvEntityMapper<T> mapper) throws IOException, CsvValidationException {
        return importCsv(resourcePath, mapper, ";");
    }
}