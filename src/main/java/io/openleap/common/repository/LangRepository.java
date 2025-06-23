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

import io.openleap.common.model.LangEO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LangRepository extends JpaRepository<LangEO, Long> {

    List<LangEO> findByAlpha2CodeIgnoreCase(String alpha2Code);

    List<LangEO> findByAlpha3B(String alpha3B);

    List<LangEO> findByNameContainingIgnoreCase(String name);

    @Query("""
        SELECT l FROM LangEO l WHERE
            (:alpha2 IS NULL OR l.alpha2Code = :alpha2) AND
            (:alpha3 IS NULL OR l.alpha3B = :alpha3) AND
            (:name IS NULL OR LOWER(l.name) LIKE LOWER(CONCAT('%', :name, '%')))
        """)
    List<LangEO> search(String alpha2, String alpha3, String name);
}
