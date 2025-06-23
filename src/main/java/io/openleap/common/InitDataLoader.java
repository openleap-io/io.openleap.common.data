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

package io.openleap.common;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import io.openleap.common.service.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.input.BOMInputStream;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Component
@Profile("INITDATA")
@RequiredArgsConstructor
@Slf4j
public class InitDataLoader {

    private final CountryService countryService;
    private final LangService langService;
    private final PhoneTypeService phoneTypeService;
    private final SalutationService salutationService;
    private final SubdivisionService subdivisionService;

    @PostConstruct
    public void init() {
        try {
            // Load country JSON from classpath
            {
                InputStream is = new ClassPathResource("data/country.json").getInputStream();
                String content = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                int countrySize = countryService.importFromJson(content);
                log.info("Initialized {} countries from JSON.", countrySize);
            }
            //load Languages csv from classpath
            {
                InputStream is = new ClassPathResource("data/language-codes-full.csv").getInputStream();
                BOMInputStream bomIn = BOMInputStream.builder().setInputStream(is).get();
                InputStreamReader inreader = new InputStreamReader(bomIn, StandardCharsets.UTF_8);
                int langSize = langService.importFromCsvReader(new CSVReader(inreader));
                log.info("Initialized {} languages from csv.", langSize);
            }

            // load phone types JSON from classpath
            {
                InputStream is = new ClassPathResource("data/phone_types_multilang.json").getInputStream();
                String content = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                int phoneTypeSize = phoneTypeService.importFromJson(content);
                log.info("Initialized {} phone-types from JSON.", phoneTypeSize);
            }

            // load phone types JSON from classpath
            {
                InputStream is = new ClassPathResource("data/salutation_multilang.json").getInputStream();
                String content = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                int salSize = salutationService.importFromJson(content);
                log.info("Initialized {} salutations from JSON.", salSize);
            }

            //load subdivision csv from classpath
            {
                InputStream is = new ClassPathResource("data/subdivisions.csv").getInputStream();
                BOMInputStream bomIn = BOMInputStream.builder().setInputStream(is).get();
                InputStreamReader inreader = new InputStreamReader(bomIn, StandardCharsets.UTF_8);
                int subdivisionSize = subdivisionService.importFromCsvReader(new CSVReader(inreader));
                log.info("Initialized {} subdivisions from csv.", subdivisionSize);
            }


        } catch (Exception e) {
            log.error("Failed to initialize data", e);
        }
    }
}