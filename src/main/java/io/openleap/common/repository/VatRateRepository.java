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

import io.openleap.common.model.VatRateEO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VatRateRepository extends JpaRepository<VatRateEO, Long> {
    // Find all by exact language code
    List<VatRateEO> findByLanguageCode(String languageCode);

    // Search by custom ID (exact match)
    Optional<VatRateEO> findByCustomIdAndLanguageCode(Integer customId, String languageCode);

    List<VatRateEO> findByCustomId(Integer customId);

    Optional<VatRateEO> findByLanguageCodeAndCustomId(String languageCode, Integer customId);
}
