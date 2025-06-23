
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

import io.openleap.common.model.dto.Subdivision;
import io.openleap.common.service.SubdivisionService;
import com.opencsv.CSVReader;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.input.BOMInputStream;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
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
        prefix = "feature.subdivisions.api",
        name   = "enabled",
        havingValue = "true",
        matchIfMissing = true    // → if not set, the controller is ON
)
@RestController
@RequestMapping("${oleap.paths.api.subdivisions}:/api/common/subdivisions")
@RequiredArgsConstructor
public class SubdivisionController {

    private final SubdivisionService service;

    @GetMapping
    public List<Subdivision> findAll() {
        return service.findAll();
    }

    @GetMapping("/search")
    public List<Subdivision> search(
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String lang,
            @RequestParam(required = false) String name
    ) {
        return service.search(country, lang, name);
    }

    @GetMapping("/{param1}/{param2}")
    public ResponseEntity<Subdivision> getByCodeAndLang(@PathVariable String param1, @PathVariable String param2) {
        if (isLangCode(param1)) {
            return service.findBySubdivisionCodeAndLanguageCode(param2, param1)
                          .map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound().build());

        } else if (isLangCode(param2)) {
            return service.findBySubdivisionCodeAndLanguageCode(param1, param2)
                          .map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound().build());
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/{param}")
    public List<Subdivision> getBySubdivisionCode(@PathVariable(name = "param") String param) {
        return service.findBySubdivisionCode(param);
    }

    @PostMapping("/import")
    public ResponseEntity<String> importCsv(@RequestParam("file") MultipartFile file) {
        try (
            var inputStream = new BOMInputStream(file.getInputStream());
            var reader = new CSVReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
        ) {
            int count = service.importFromCsvReader(reader);
            return ResponseEntity.ok("Imported " + count + " subdivision records.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Failed to import data: " + e.getMessage());
        }
    }

    private boolean isLangCode(String s) {
        return s != null && s.matches("^[a-zA-Z]{2}$");
    }
}
