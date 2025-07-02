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

import io.openleap.common.model.dto.VatRate;
import io.openleap.common.service.VatRateService;
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
        prefix = "feature.vatrate.api",
        name   = "enabled",
        havingValue = "true",
        matchIfMissing = true    // → if not set, the controller is ON
)
@RestController
@RequestMapping("${oleap.paths.api.vatrates:/api/common/vatrates}")
public class VatRateController {

    private final VatRateService service;


    public VatRateController(VatRateService service) {
        this.service = service;
    }

    @GetMapping
    public List<VatRate> getAll() {
        return service.findAll();
    }

    @GetMapping("/{langCode}/{id}/")
    public Optional<VatRate> getByIdAndLanguageCode(@PathVariable(name = "langCode") String langCode, @PathVariable(name = "id") Integer id) {
        return service.findByLanguageCodeAndCustomId(langCode, id);
    }


    @GetMapping("/{param}/")
    public List<VatRate> getById(@PathVariable(name = "param") String param) {
        if (this.isInteger(param)) {
            return service.findByCustomId(Integer.parseInt(param));
        }
        return service.findByLanguageCode(param);
    }


    @PostMapping
    public VatRate create(@RequestBody VatRate sal) {
        return service.save(sal);
    }

    /**
     * @param jsonArray
     * @return
     */
    @PostMapping("/import")
    public ResponseEntity<String> importVatRateJson(@RequestBody String jsonArray) {
        try {
            int resultSet = service.importFromJson(jsonArray);

            return ResponseEntity.ok("Imported " + resultSet + " records successfully.");

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Failed to import data: " + e.getMessage());
        }
    }

    public boolean isInteger(String str) {
        if (str == null || str.isEmpty()) return false;
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}