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

package io.openleap.common.repository;

import io.openleap.common.model.CountryEO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CountryRepository extends JpaRepository<CountryEO, Long> {

    @Query("""
        SELECT c FROM CountryEO c WHERE
            (:alpha2 IS NULL OR c.alpha2Code = :alpha2) AND
            (:alpha3 IS NULL OR c.alpha3Code = :alpha3) AND
            (:name IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')))
        """)
    List<CountryEO> search(String alpha2, String alpha3, String name);


    Optional<CountryEO> findByNumericCode(Integer numericCode);
    Optional<CountryEO> findByAlpha2CodeIgnoreCase(String alpha2Code);
    Optional<CountryEO> findByAlpha3CodeIgnoreCase(String alpha3Code);
}

