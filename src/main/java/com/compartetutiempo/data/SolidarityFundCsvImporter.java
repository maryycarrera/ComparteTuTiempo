package com.compartetutiempo.data;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.compartetutiempo.timebank.solidarityfund.SolidarityFund;
import com.compartetutiempo.timebank.solidarityfund.SolidarityFundRepository;
import com.compartetutiempo.util.ProfileUtils;

import jakarta.annotation.PostConstruct;

@Component
public class SolidarityFundCsvImporter {

    private final SolidarityFundRepository solidarityFundRepository;
    private final Environment env;

    private static final Logger logger = LoggerFactory.getLogger(SolidarityFundCsvImporter.class);

    @Autowired
    public SolidarityFundCsvImporter(SolidarityFundRepository solidarityFundRepository, Environment env) {
        this.solidarityFundRepository = solidarityFundRepository;
        this.env = env;
        logger.info("[IMPORT] Bean SolidarityFundCsvImporter creado");
    }

    @PostConstruct
    public void importSolidarityFund() {
        logger.info("[IMPORT] Iniciando importación de fondo solidario...");
        Optional<SolidarityFund> existingFund = solidarityFundRepository.findById(1);
        if (existingFund.isPresent()) {
            logger.info("[IMPORT] Fondo solidario ya existe.");
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
                logger.error("[IMPORT][ERROR] El CSV de fondo solidario está vacío.");
                return;
            }
            // END Generado con GitHub Copilot Chat Extension
            solidarityFundRepository.save(solidarityFunds.get(0));
            logger.info("[IMPORT] Fondo solidario importado correctamente.");
        } catch (Exception e) {
            logger.error("[IMPORT][ERROR] Error importando fondo solidario: " + e.getMessage());
            if(!ProfileUtils.isProd(env)) {
                logger.debug("Stack Trace: ", e);
            }
        }
    }

}
