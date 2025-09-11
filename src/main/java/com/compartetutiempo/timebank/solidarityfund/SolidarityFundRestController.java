package com.compartetutiempo.timebank.solidarityfund;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.compartetutiempo.timebank.response.MessageResponse;

@RestController
@RequestMapping("/api/v1/solidarity-fund")
public class SolidarityFundRestController {

    private final SolidarityFundService solidarityFundService;

    @Autowired
    public SolidarityFundRestController(SolidarityFundService solidarityFundService) {
        this.solidarityFundService = solidarityFundService;
    }

    @GetMapping
    public ResponseEntity<MessageResponse<SolidarityFund>> find() {
        SolidarityFund solidarityFund = solidarityFundService.find();
        return ResponseEntity.ok(new MessageResponse<SolidarityFund>("Fondo Solidario encontrado con éxito.", solidarityFund));
    }

}
