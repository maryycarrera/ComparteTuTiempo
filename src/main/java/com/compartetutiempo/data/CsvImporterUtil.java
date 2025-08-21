/*
 * Clase creada con GitHub Copilot Chat Extension. Editada posteriormente.
 */

package com.compartetutiempo.data;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvImporterUtil {

    public static <T> List<T> importCsv(String resourcePath, CsvEntityMapper<T> mapper, String delimiter) throws IOException {
        List<T> entities = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                CsvImporterUtil.class.getResourceAsStream(resourcePath)))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) { firstLine = false; continue; }
                String[] fields = line.split(delimiter);
                entities.add(mapper.map(fields));
            }
        }
        return entities;
    }

    public static <T> List<T> importCsvWithComma(String resourcePath, CsvEntityMapper<T> mapper) throws IOException {
        return importCsv(resourcePath, mapper, ",");
    }

    public static <T> List<T> importCsvWithSemicolon(String resourcePath, CsvEntityMapper<T> mapper) throws IOException {
        return importCsv(resourcePath, mapper, ";");
    }
}