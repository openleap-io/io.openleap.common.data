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

package io.openleap.common.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(
        name = "common_vat_rate",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_vat_rate_rate_valid_from",
                        columnNames = {"rate", "valid_from"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class VatRateEO extends OlEntity {

    @Column(name = "custom_id")
    private Integer customId;

    /**
     * The VAT rate as a percentage (e.g. 19.00, 7.00, 0.00).
     */
    @Column(name = "rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal rate;

    /**
     * A short description of this rate (e.g. "standard", "reduced", "zero").
     */
    @Column(name = "description", nullable = false, length = 50)
    private String description;

    /**
     * The two-letter ISO language code for this rate (e.g. "de" for German).
     */
    @Column(name = "language_code", nullable = false, length = 2)
    private String languageCode;

    /**
     * The date from which this rate is effective.
     */
    @Column(name = "valid_from", nullable = false)
    private LocalDate validFrom;

    /**
     * The date until which this rate is effective (inclusive). Nullable if still current.
     */
    @Column(name = "valid_to")
    private LocalDate validTo;

}
