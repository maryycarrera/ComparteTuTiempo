package com.compartetutiempo.data;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.compartetutiempo.timebank.solidarityfund.SolidarityFund;
import com.compartetutiempo.timebank.solidarityfund.SolidarityFundRepository;
import jakarta.annotation.PostConstruct;

@Component
public class SolidarityFundCsvImporter {

    private final SolidarityFundRepository solidarityFundRepository;

    @Autowired
    public SolidarityFundCsvImporter(SolidarityFundRepository solidarityFundRepository) {
        this.solidarityFundRepository = solidarityFundRepository;
        System.out.println("[IMPORT] Bean SolidarityFundCsvImporter creado");
    }

    @PostConstruct
    public void importSolidarityFund() {
        System.out.println("[IMPORT] Iniciando importación de fondo solidario...");
        Optional<SolidarityFund> existingFund = solidarityFundRepository.findById(1);
        if (existingFund.isPresent()) {
            System.out.println("[IMPORT] Fondo solidario ya existe.");
            return;
        }
        try {
            List<SolidarityFund> solidarityFunds = CsvImporterUtil.importCsvWithComma("/data/solidarityfund.csv", fields -> {
                SolidarityFund solidarityFund = new SolidarityFund();
                solidarityFund.setHours(Integer.parseInt(fields[0].trim()));
                solidarityFund.setMinutes(Integer.parseInt(fields[1].trim()));

                return solidarityFund;
            });
            // START Generado con GitHub Copilot Chat Extension
            if (solidarityFunds.isEmpty()) {
                System.out.println("[IMPORT][ERROR] El CSV de fondo solidario está vacío.");
                return;
            }
            // END Generado con GitHub Copilot Chat Extension
            solidarityFundRepository.save(solidarityFunds.get(0));
            System.out.println("[IMPORT] Fondo solidario importado correctamente.");
        } catch (Exception e) {
            System.out.println("[IMPORT][ERROR] Error importando fondo solidario: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
