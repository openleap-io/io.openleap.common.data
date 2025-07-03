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
import io.openleap.common.mapper.PhoneTypeMapper;
import io.openleap.common.model.PhoneTypeEO;
import io.openleap.common.model.dto.PhoneType;
import io.openleap.common.repository.PhoneTypeRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PhoneTypeService {

    private final PhoneTypeRepository repository;
    private final PhoneTypeMapper mapper;
    private final ObjectMapper objectMapper;

    public List<PhoneType> findAll() {
        return mapper.toDtoList(repository.findAll());
    }

    public PhoneType save(PhoneType entity) {
        return mapper.toDto(repository.save(mapper.toEntity(entity)));
    }

    public int importFromJson(String jsonArray) throws Exception {
        List<PhoneTypeEO> items = objectMapper.readValue(jsonArray, new TypeReference<>() {});
        repository.saveAll(items);
        return items.size();
    }

    public Optional<PhoneType> getByIdAndLanguageCode(Integer id, String languageCode) {
        return repository.findByCustomIdAndLanguageCode(id, languageCode).map(mapper::toDto);
    }

    public List<PhoneType> findByCustomId(Integer id) {
        return mapper.toDtoList(repository.findByCustomId(id));
    }

    public List<PhoneType> findByLanguageCode(String code) {
        return mapper.toDtoList(repository.findByLanguageCode(code));
    }

    public List<PhoneType> findByLanguageCodeAndName(String code, String name) {
        return mapper.toDtoList(repository.searchByLanguageCodeAndKeyword(code, name));
    }
}
