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
import io.openleap.common.mapper.SalutationMapper;
import io.openleap.common.model.dto.Salutation;
import io.openleap.common.repository.SalutationRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class SalutationService {

    private final SalutationRepository repository;
    private final SalutationMapper mapper;
    private final ObjectMapper objectMapper;

    public List<Salutation> findAll() {
        return mapper.toDtoList(repository.findAll());
    }

    public Salutation save(Salutation sal) {
        return mapper.toDto(repository.save(mapper.toEntity(sal)));
    }

    /**
     * Imports salutation data from a given URL (which should return JSON array).
     *
     * @param jsonArray String JSON array of salutations
     * @return number of records imported
     */
    public int importFromJson(String jsonArray) throws Exception {
        List<Salutation> importedSalutations = objectMapper.readValue(
                jsonArray,
                new TypeReference<List<Salutation>>() {}
        );
        repository.saveAll(mapper.toEntityList(importedSalutations));
        return importedSalutations.size();
    }

    public List<Salutation> findByLanguageCodeAndSalutation(String code, String sal){
        return mapper.toDtoList(repository.searchByLanguageCodeAndKeyword(code,sal));
    }

    public List<Salutation> findByCustomId(Integer id){
        return mapper.toDtoList(repository.findByCustomId(id));
    }

    public List<Salutation> findByLanguageCode(String code){
        return mapper.toDtoList(repository.findByLanguageCode(code));
    }

    public Optional<Salutation> getByIdAndLanguageCode(Integer id, String languageCode) {
        return repository.findByCustomIdAndLanguageCode(id, languageCode).map(mapper::toDto);
    }
}
