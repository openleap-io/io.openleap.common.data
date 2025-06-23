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
import io.openleap.common.model.SalutationEO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface SalutationRepository extends JpaRepository<SalutationEO, Long> {
    // Find all by exact language code
    List<SalutationEO> findByLanguageCode(String languageCode);

    // Search by language code and partial salutation match (case-insensitive)
    @Query("SELECT s FROM SalutationEO s WHERE s.languageCode = :languageCode AND LOWER(s.salutation) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<SalutationEO> searchByLanguageCodeAndKeyword(@Param("languageCode") String languageCode, @Param("keyword") String keyword);

    // Search by custom ID (exact match)
    Optional<SalutationEO> findByCustomIdAndLanguageCode(Integer customId, String languageCode);

    List<SalutationEO> findByCustomId(Integer customId);
}
