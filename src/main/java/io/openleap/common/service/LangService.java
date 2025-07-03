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

package io.openleap.common.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import io.openleap.common.mapper.LangMapper;
import io.openleap.common.model.LangEO;
import io.openleap.common.model.dto.Lang;
import io.openleap.common.repository.LangRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class LangService {

    private final LangRepository repository;
    private final LangMapper mapper;


    public List<Lang> findAll() {
        return mapper.toDtoList(repository.findAll());
    }

    public Lang save(Lang lang) {
        return mapper.toDto(repository.save(mapper.toEntity(lang)));
    }

    public int importFromCsvReader(CSVReader reader) throws IOException, CsvValidationException {
            String[] row;
            int count = 0;

            reader.readNext(); // skip header
            while ((row = reader.readNext()) != null) {
                if (row.length < 4) continue;
                if (row[0].trim().length() > 3) continue; // skip invalid alpha3_b like "qaa-qtz"

                LangEO lang = LangEO.builder()
                        .alpha2Code(row[2].trim())        // ISO 639-1
                        .alpha3B(row[0].trim())           // ISO 639-2/B
                        .alpha3T(row[1].trim())           // ISO 639-2/T
                        .name(row[3].trim())              // English name
                        .nativeName(row.length > 4 ? row[4].trim() : null)
                        .optLock(0L)
                        .build();

                repository.save(lang);
                count++;
            }

            return count;

    }

    public List<Lang> search(String alpha2, String alpha3, String name) {
        return mapper.toDtoList(repository.search(alpha2, alpha3, name));
    }
}

