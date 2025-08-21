/*
 * Interfaz creada con GitHub Copilot Chat Extension.
 */

package com.compartetutiempo.data;

public interface CsvEntityMapper<T> {
    T map(String[] fields);
}
