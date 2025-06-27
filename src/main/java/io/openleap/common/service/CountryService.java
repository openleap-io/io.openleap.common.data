
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

import io.openleap.common.mapper.CountryMapper;
import io.openleap.common.model.CountryEO;
import io.openleap.common.model.TranslationEO;
import io.openleap.common.model.dto.Country;
import io.openleap.common.repository.CountryRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.openleap.common.repository.TranslationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CountryService {

    private final CountryRepository countryRepository;
    private final TranslationRepository translationRepository;
    private final CountryMapper countryMapper;


    public List<Country> findAll() {
        return countryMapper.toDtoList(countryRepository.findAll());
    }

    public Optional<Country> findByNumericCode(Integer code) {
        return countryRepository.findByNumericCode(code).map(countryMapper::toDto);
    }

    public Optional<Country> findByAlpha2(String code) {
        return countryRepository.findByAlpha2CodeIgnoreCase(code).map(countryMapper::toDto);
    }

    public Optional<Country> findByAlpha3(String code) {
        return countryRepository.findByAlpha3CodeIgnoreCase(code).map(countryMapper::toDto);
    }

    public List<Country> search(String alpha2, String alpha3, String name) {
        return countryMapper.toDtoList(countryRepository.search(alpha2, alpha3, name));
    }

    public Country save(Country entity) {
        return countryMapper.toDto(countryRepository.save(countryMapper.toEntity(entity)));
    }

    public void delete(Long id) {
        countryRepository.deleteById(id);
    }

    public int importFromRestCountries() throws Exception {
        String url = "https://restcountries.com/v3.1/all?fields=name,cca2,cca3,ccn3,region,subregion";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RuntimeException("Failed to fetch country data from restcountries.com");
        }
        return this.importFromJson(response.getBody());
    }

    public int importFromJson(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(json);

            int count = 0;
            for (JsonNode node : root) {
                // top-level fields
                String alpha2 = node.at("/cca2").asText();
                String alpha3 = node.at("/cca3").asText();
                String numeric = node.at("/ccn3").asText();
                String region = node.at("/region").asText();
                String subregion = node.at("/subregion").asText();

                // skip missing codes
                if (alpha2.isEmpty() || alpha3.isEmpty() || numeric.isEmpty()) {
                    continue;
                }

                // build Name.common & Name.official
                JsonNode nameNode = node.get("name");
                String commonName = nameNode.path("common").asText();
                String officialName = nameNode.path("official").asText();

                // build and save entity
                CountryEO country = CountryEO.builder()
                        .nameCommon(commonName)
                        .nameOfficial(officialName)
                        .alpha2Code(alpha2)
                        .alpha3Code(alpha3)
                        .numericCode(Integer.parseInt(numeric))
                        .region(region.isEmpty() ? null : region)
                        .subRegion(subregion.isEmpty() ? null : subregion)
                        .optLock(0L)
                        .build();

                country = countryRepository.save(country);
                // build up to 3 nativeName entries
//                Map<String, CountryEO.Translation> nativeMap = new HashMap<>();
                Set<TranslationEO> translations = new HashSet<>();
                JsonNode nativeNames = nameNode.path("nativeName");
                if (nativeNames.isObject()) {
                    for (Iterator<Map.Entry<String, JsonNode>> it = nativeNames.fields();
                         it.hasNext() ; ) {
                        Map.Entry<String, JsonNode> entry = it.next();
                        String lang = entry.getKey();
                        JsonNode t = entry.getValue();
                        String tCommon = t.path("common").asText();
                        String tOfficial = t.path("official").asText();
                        if (tCommon != null && !tCommon.isBlank()) {

                            translations.add(TranslationEO.builder()
                                    .id(TranslationEO.TranslationKey.builder()
                                            .entityId(country.getPKey())
                                            .entityType(Country.class.getCanonicalName())
                                            .langCode(lang)
                                            .fieldName("common")
                                            .build()
                                    )
                                    .value(tCommon)
                                    .build()
                            );
                        }
                        if (tOfficial != null && !tOfficial.isBlank()) {
                            translations.add(TranslationEO.builder()
                                    .id(TranslationEO.TranslationKey.builder()
                                            .entityId(country.getPKey())
                                            .entityType(Country.class.getCanonicalName())
                                            .langCode(lang)
                                            .fieldName("official")
                                            .build()
                                    )
                                    .value(tOfficial)
                                    .build()
                            );
                        }
                        if (translations.size() > 0) {
                            translationRepository.saveAll(translations);
                        }
                    }
                }
                count++;
            }
            return count;

        } catch (Exception e) {
            // you may want to log e
            return 0;
        }
    }


    private Integer tryParseInt(String val) {
        try {
            return Integer.parseInt(val.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
