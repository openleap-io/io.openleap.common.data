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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.openleap.common.mapper.VatRateMapper;
import io.openleap.common.model.dto.VatRate;
import io.openleap.common.repository.VatRateRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class VatRateService {

    private final VatRateRepository repository;
    private final VatRateMapper mapper;
    private final ObjectMapper objectMapper;

    public List<VatRate> findAll() {
        return mapper.toDtoList(repository.findAll());
    }

    public VatRate save(VatRate vr) {
        return mapper.toDto(repository.save(mapper.toEntity(vr)));
    }

    /**
     * Imports vat rate data from a json (which should return JSON array).
     *
     * @param jsonArray String JSON array of salutations
     * @return number of records imported
     */
    public int importFromJson(String jsonArray) throws Exception {
        List<VatRate> vatRates = objectMapper.readValue(
                jsonArray,
                new TypeReference<List<VatRate>>() {}
        );
        repository.saveAll(mapper.toEntityList(vatRates));
        return vatRates.size();
    }

    public List<VatRate> findByCustomId(Integer id){
        return mapper.toDtoList(repository.findByCustomId(id));
    }

    public List<VatRate> findByLanguageCode(String code){
        return mapper.toDtoList(repository.findByLanguageCode(code));
    }


    public Optional<VatRate> findByLanguageCodeAndCustomId(String langCode, Integer id) {
        return repository.findByCustomIdAndLanguageCode(id, langCode).map(mapper::toDto);
    }
}
