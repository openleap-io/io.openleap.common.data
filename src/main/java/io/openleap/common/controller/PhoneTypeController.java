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

import io.openleap.common.model.dto.PhoneType;
import io.openleap.common.service.PhoneTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@ConditionalOnProperty(
        name            = "feature.common-data.api",
        havingValue     = "true",
        matchIfMissing  = true      // global default = on
)
@ConditionalOnProperty(
        prefix = "feature.phonetypes.api",
        name   = "enabled",
        havingValue = "true",
        matchIfMissing = true    // → if not set, the controller is ON
)
@RestController
@RequestMapping("${oleap.paths.api.phonetypes:/api/common/phonetypes}")
@RequiredArgsConstructor
public class PhoneTypeController {

    private final PhoneTypeService service;

    @GetMapping
    public List<PhoneType> getAll() {
        return service.findAll();
    }

    @GetMapping("/{param1}/{param2}/")
    public Optional<PhoneType> getByIdAndLanguageCode(@PathVariable String param1, @PathVariable String param2) {
        if (isInteger(param1)) {
            return service.getByIdAndLanguageCode(Integer.parseInt(param1), param2);
        }
        if (isInteger(param2)) {
            return service.getByIdAndLanguageCode(Integer.parseInt(param2), param1);
        }
        return Optional.empty();
    }

    @GetMapping("/{param}/")
    public List<PhoneType> getById(@PathVariable String param) {
        if (isInteger(param)) {
            return service.findByCustomId(Integer.parseInt(param));
        }
        return service.findByLanguageCode(param);
    }

    @PostMapping
    public PhoneType create(@RequestBody PhoneType phoneType) {
        return service.save(phoneType);
    }

    @GetMapping("/search")
    public List<PhoneType> search(
            @RequestParam(required = false) String langCode,
            @RequestParam(required = false) String name
    ) {
        return service.findByLanguageCodeAndName(langCode, name);
    }

    @PostMapping("/import")
    public ResponseEntity<String> importData(@RequestBody String jsonArray) {
        try {
            int result = service.importFromJson(jsonArray);
            return ResponseEntity.ok("Imported " + result + " records successfully.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to import data: " + e.getMessage());
        }
    }

    private boolean isInteger(String str) {
        if (str == null || str.isEmpty()) return false;
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
