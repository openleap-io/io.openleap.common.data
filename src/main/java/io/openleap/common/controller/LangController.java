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
import io.openleap.common.model.dto.Lang;
import io.openleap.common.service.LangService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.input.BOMInputStream;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
@ConditionalOnProperty(
        name            = "feature.common-data.api",
        havingValue     = "true",
        matchIfMissing  = true      // global default = on
)
@ConditionalOnProperty(
        prefix = "feature.languages.api",
        name   = "enabled",
        havingValue = "true",
        matchIfMissing = true    // → if not set, the controller is ON
)
@RestController
@RequestMapping("${oleap.paths.api.languages}:/api/common/languages")
@RequiredArgsConstructor
public class LangController {

    private final LangService langService;

    @GetMapping
    public List<Lang> findAll() {
        return langService.findAll();
    }

    @GetMapping("/search")
    public List<Lang> search(
            @RequestParam(required = false) String alpha2,
            @RequestParam(required = false) String alpha3,
            @RequestParam(required = false) String name
    ) {
        return langService.search(alpha2, alpha3, name);
    }

    @PostMapping("/importFromGithub")
    public ResponseEntity<String> importFromGithub() throws IOException {
        return this.importCsv("https://raw.githubusercontent.com/datasets/language-codes/refs/heads/main/data/language-codes-full.csv");
    }

    @PostMapping("/importURL")
    public ResponseEntity<String> importCsv(@RequestBody String downloadUrl) throws IOException {
        try (
                InputStream rawInput = new URL(downloadUrl).openStream();
                BOMInputStream bomIn = BOMInputStream.builder().setInputStream(rawInput).get();
                InputStreamReader inreader = new InputStreamReader(bomIn, StandardCharsets.UTF_8);
                CSVReader reader = new CSVReader(inreader);
        ) {

            int resultSet = langService.importFromCsvReader(reader);

            return ResponseEntity.ok("Imported " + resultSet + " records successfully.");

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Failed to import data: " + e.getMessage());
        }
    }
}
