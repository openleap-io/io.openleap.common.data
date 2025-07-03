
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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import io.openleap.common.mapper.CountryMapper;
import io.openleap.common.model.CountryEO;
import io.openleap.common.model.dto.Country;
import io.openleap.common.repository.CountryRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CountryService {

    private final CountryRepository repository;
    private final CountryMapper countryMapper;


    public List<Country> findAll() {
        return countryMapper.toDtoList(repository.findAll());
    }

    public Optional<Country> findByNumericCode(Integer code) {
        return repository.findByNumericCode(code).map(countryMapper::toDto);
    }

    public Optional<Country> findByAlpha2(String code) {
        return repository.findByAlpha2CodeIgnoreCase(code).map(countryMapper::toDto);
    }

    public Optional<Country> findByAlpha3(String code) {
        return repository.findByAlpha3CodeIgnoreCase(code).map(countryMapper::toDto);
    }

    public List<Country> search(String alpha2, String alpha3, String name) {
        return countryMapper.toDtoList(repository.search(alpha2, alpha3, name));
    }

    public Country save(Country entity) {
        return countryMapper.toDto(repository.save(countryMapper.toEntity(entity)));
    }

    public void delete(Long id) {
        repository.deleteById(id);
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

    @Transactional(readOnly = false)
    public int importFromJson(String json) throws Exception {
        try {

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(json);

            int count = 0;
            for (JsonNode node : root) {
                String name = node.at("/name/common").asText();
                String officialName = node.at("/name/official").asText();
                String nativeName = node.at("/name/nativeName/common").asText();
                String nativeOfficialName = node.at("/name/nativeName/official").asText();
                String alpha2 = node.at("/cca2").asText();
                String alpha3 = node.at("/cca3").asText();
                String numeric = node.at("/ccn3").asText();
                String region = node.at("/region").asText();
                String subregion = node.at("/subregion").asText();

                if (alpha2.isEmpty() || alpha3.isEmpty() || numeric.isEmpty()) continue;

                CountryEO country = CountryEO.builder()
                        .name(name)
                        .officialName(officialName)
                        .nativeName(nativeName)
                        .nativeOfficialName(nativeOfficialName)
                        .alpha2Code(alpha2)
                        .alpha3Code(alpha3)
                        .numericCode(Integer.parseInt(numeric))
                        .region(region.isEmpty() ? null : region)
                        .subRegion(subregion.isEmpty() ? null : subregion)
                        .optLock(0L)
                        .build();

                repository.save(country);
                repository.flush();
                count++;
            }
            return count;
        }catch (Exception e){
            return 0;
        }
    }


    public int importFromCsvReader(CSVReader reader) throws IOException, CsvValidationException {
        String[] row;
        int count = 0;

        reader.readNext(); // skip header

        while ((row = reader.readNext()) != null) {
            if (row.length < 4) continue;

            CountryEO country = CountryEO.builder()
                    .alpha2Code(row[1].trim())
                    .alpha3Code(row[2].trim())
                    .numericCode(Integer.parseInt(row[3].trim()))
                    .name(row[0].trim())
                    .iso3166_2(row.length > 4 ? row[4].trim() : null)
                    .region(row.length > 5 ? row[5].trim() : null)
                    .subRegion(row.length > 6 ? row[6].trim() : null)
                    .intermediateRegion(row.length > 7 ? row[7].trim() : null)
                    .regionCode(row.length > 8 ? tryParseInt(row[8]) : null)
                    .subRegionCode(row.length > 9 ? tryParseInt(row[9]) : null)
                    .intermediateRegionCode(row.length > 10 ? tryParseInt(row[10]) : null)
                    .optLock(0L)
                    .build();

            repository.save(country);
            count++;
        }

        return count;
    }

    private Integer tryParseInt(String val) {
        try {
            return Integer.parseInt(val.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
