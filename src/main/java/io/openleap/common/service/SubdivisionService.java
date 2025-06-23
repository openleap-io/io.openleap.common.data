
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

import io.openleap.common.mapper.SubdivisionMapper;
import io.openleap.common.model.SubdivisionEO;
import io.openleap.common.model.dto.Subdivision;
import io.openleap.common.repository.SubdivisionRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubdivisionService {

    private final SubdivisionRepository repository;
    private final SubdivisionMapper mapper;

    public List<Subdivision> findAll() {
        return mapper.toDtoList(repository.findAll());
    }

    public List<Subdivision> search(String countryCode, String languageCode, String name) {
        return mapper.toDtoList(repository.search(countryCode, languageCode, name));
    }

    public List<Subdivision> findBySubdivisionCode(String code) {
        return mapper.toDtoList(repository.findBySubdivisionCode(code));
    }

    public Optional<Subdivision> findBySubdivisionCodeAndLanguageCode(String code, String lang) {
        return repository.findBySubdivisionCodeAndLanguageCode(code, lang).map(mapper::toDto);
    }

    public int importFromCsvReader(CSVReader reader) throws IOException, CsvValidationException {
        String[] row;
        int count = 0;

        reader.readNext(); // skip header
        while ((row = reader.readNext()) != null) {
            if (row.length < 4) continue;

            Subdivision dto = Subdivision.builder()
                .countryCodeAlpha2(row[0].trim())
                .subdivisionCode(row[1].trim())
                .subdivisionName(row[2].trim())
                .languageCode(row[3].trim())
                .parentSubdivision(row.length > 4 ? row[4].trim() : null)
                .category(row.length > 5 ? row[5].trim() : null)
                .localVariant(row.length > 6 ? row[6].trim() : null)
                .build();

            repository.save(mapper.toEntity(dto));
            count++;
        }

        return count;
    }
}
