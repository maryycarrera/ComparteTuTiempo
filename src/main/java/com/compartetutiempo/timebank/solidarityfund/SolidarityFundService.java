package com.compartetutiempo.timebank.solidarityfund;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.compartetutiempo.timebank.exceptions.ResourceNotFoundException;

@Service
public class SolidarityFundService {

    private final SolidarityFundRepository solidarityFundRepository;

    @Autowired
    public SolidarityFundService(SolidarityFundRepository solidarityFundRepository) {
        this.solidarityFundRepository = solidarityFundRepository;
    }

    @Transactional(readOnly = true)
    public SolidarityFund find() {
        return solidarityFundRepository.findById(1)
                .orElseThrow(() -> new ResourceNotFoundException("Solidarity Fund", "id", 1));
    }

}
