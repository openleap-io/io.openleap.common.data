
/*
 * This file is part of this software project.
 *
 *  Copyright (C) 2025 Dr.-Ing. Sören Kemmann
 *
 * This software is dual-licensed under:
 *
 * 1. The European Union Public License v.1.2 (EUPL)
 *    https://joinup.ec.europa.eu/collection/eupl
 *
 *     You may use, modify and redistribute this file under the terms of the EUPL.
 *
 *  2. A commercial license available from:
 *
 *     B+B Unternehmensberatung GmbH & Co.KG
 *     Robert-Bunsen-Straße 10
 *     67098 Bad Dürkheim
 *     Germany
 *     Contact: license@bb-online.de
 *
 *  You may choose which license to apply.
 */

package io.openleap.common.controller;

import com.opencsv.CSVReader;
import io.openleap.common.model.dto.Country;
import io.openleap.common.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.input.BOMInputStream;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
@ConditionalOnProperty(
        name            = "feature.common-data.api",
        havingValue     = "true",
        matchIfMissing  = true      // global default = on
)
@ConditionalOnProperty(
        prefix = "feature.countries.api",
        name   = "enabled",
        havingValue = "true",
        matchIfMissing = true    // → if not set, the controller is ON
)
@RestController
@RequestMapping("${oleap.paths.api.countries}:/api/common/countries")
@RequiredArgsConstructor
public class CountryController {

    private final CountryService countryService;

    @GetMapping
    public List<Country> findAll() {
        return countryService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Country> getById(@PathVariable String id) {
        Optional<Country> result;

        if (id.matches("\\d+")) {
            result = countryService.findByNumericCode(Integer.parseInt(id));
        } else if (id.length() == 2 && id.matches("[A-Za-z]{2}")) {
            result = countryService.findByAlpha2(id.toUpperCase());
        } else if (id.length() == 3 && id.matches("[A-Za-z]{3}")) {
            result = countryService.findByAlpha3(id.toUpperCase());
        } else {
            result = Optional.empty();
        }

        return result.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public List<Country> search(
            @RequestParam(required = false) String alpha2,
            @RequestParam(required = false) String alpha3,
            @RequestParam(required = false) String name
    ) {
        return countryService.search(alpha2, alpha3, name);
    }

    @PostMapping("/importCSV")
    public ResponseEntity<String> importCsv(@RequestParam("file") MultipartFile file) {
        try (
                var inputStream = new BOMInputStream(file.getInputStream());
                var reader = new CSVReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
        ) {
            int count = countryService.importFromCsvReader(reader);
            return ResponseEntity.ok("Imported " + count + " country records.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Import failed: " + e.getMessage());
        }
    }

    @PostMapping("/importRestCountry")
    public ResponseEntity<String> importRestCountry() {
        try{
            int count = countryService.importFromRestCountries();
            return ResponseEntity.ok("Imported " + count + " country records.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Import failed: " + e.getMessage());
        }
    }
}
